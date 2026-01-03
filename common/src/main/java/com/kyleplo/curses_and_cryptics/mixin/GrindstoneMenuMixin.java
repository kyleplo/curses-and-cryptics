package com.kyleplo.curses_and_cryptics.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.kyleplo.curses_and_cryptics.CursesAndCryptics;
import com.kyleplo.curses_and_cryptics.CursesAndCrypticsRegistry;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Unit;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.GrindstoneMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;

@Mixin(GrindstoneMenu.class)
public class GrindstoneMenuMixin {
    @Shadow
    public ContainerLevelAccess access;

    @WrapMethod(method = "computeResult")
    private ItemStack computeResult(ItemStack input1, ItemStack input2, Operation<ItemStack> original) {
        ItemStack originalValue = original.call(input1, input2);

        if (CursesAndCryptics.config.crypticEnchantedBookGrindstoning && originalValue.isEmpty() && (input1.isEmpty() || input2.isEmpty()) && (input1.is(CursesAndCrypticsRegistry.CRYPTIC_ENCHANTED_BOOK) || input2.is(CursesAndCrypticsRegistry.CRYPTIC_ENCHANTED_BOOK))) {
            return new ItemStack(Items.BOOK);
        } else if (originalValue.isEmpty() && !input1.isEmpty() && !input2.isEmpty()
                && (input1.is(CursesAndCrypticsRegistry.WHETSTONES) || input2.is(CursesAndCrypticsRegistry.WHETSTONES))) {
            if (input1.is(CursesAndCrypticsRegistry.WHETSTONES) && input2.is(CursesAndCrypticsRegistry.WHETSTONES)) {
                return originalValue;
            }

            ItemStack whetstone = input1.is(CursesAndCrypticsRegistry.WHETSTONES) ? input1 : input2;
            ItemStack other = input1.is(CursesAndCrypticsRegistry.WHETSTONES) ? input2 : input1;

            if (!EnchantmentHelper.hasAnyEnchantments(other)) {
                return originalValue;
            }

            ItemStack resultItem;
            if (whetstone.is(CursesAndCrypticsRegistry.ENCHANTED_BLESSED_WHETSTONE)) {
                resultItem = removeCursesFrom(other.copy());
            } else {
                resultItem = other.copy();

                ItemEnchantments itemEnchantments = resultItem.getOrDefault(other.is(Items.ENCHANTED_BOOK) ? DataComponents.STORED_ENCHANTMENTS : DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
                if (itemEnchantments.size() == 0) {
                    return originalValue;
                }
                Holder<Enchantment>[] enchants = (Holder<Enchantment>[]) itemEnchantments.keySet().toArray(new Holder[0]);

                RandomSource random = this.access.evaluate((Level level, BlockPos blockPos) -> {
                    return level.getRandom();
                }, RandomSource.create());

                boolean removed = false;
                
                for (int i = 0; i < 50; i++) {
                    int rand = random.nextInt(itemEnchantments.size());
                    if (whetstone.is(CursesAndCrypticsRegistry.BLESSED_WHETSTONE) || !enchants[rand].is(EnchantmentTags.CURSE)) {
                        itemEnchantments = EnchantmentHelper.updateEnchantments(resultItem, (mutable) -> {
                            mutable.removeIf((holder) -> {
                                return enchants[rand].getRegisteredName().equals(holder.getRegisteredName());
                            });
                        });
                        resultItem.set(other.is(Items.ENCHANTED_BOOK) ? DataComponents.STORED_ENCHANTMENTS : DataComponents.ENCHANTMENTS, itemEnchantments);
                        removed = true;
                        break;
                    }
                }

                if (!removed) {
                    return originalValue;
                }

                if (resultItem.is(Items.ENCHANTED_BOOK) && itemEnchantments.isEmpty()) {
                    resultItem = resultItem.transmuteCopy(Items.BOOK);
                }
            }
            if (CursesAndCryptics.config.whetstoneHideResult) {
                resultItem.set(CursesAndCrypticsRegistry.RESULTS_HIDDEN.value(), Unit.INSTANCE);
            }
            return resultItem;
        }
        return originalValue;
    }

    private ItemStack removeCursesFrom(ItemStack itemStack) {
        ItemEnchantments itemEnchantments = EnchantmentHelper.updateEnchantments(itemStack, (mutable) -> {
            mutable.removeIf((holder) -> {
                return holder.is(EnchantmentTags.CURSE);
            });
        });
        if (itemStack.is(Items.ENCHANTED_BOOK) && itemEnchantments.isEmpty()) {
            itemStack = itemStack.transmuteCopy(Items.BOOK);
        }

        int i = 0;

        for (int j = 0; j < itemEnchantments.size(); ++j) {
            i = AnvilMenu.calculateIncreasedRepairCost(i);
        }

        itemStack.set(DataComponents.REPAIR_COST, i);
        return itemStack;
    }
}
