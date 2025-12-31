package com.kyleplo.curses_and_crusades.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.kyleplo.curses_and_crusades.CursesAndCrusadesRegistry;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.util.RandomSource;
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

        if (originalValue.isEmpty() && !input1.isEmpty() && !input2.isEmpty()
                && (input1.is(CursesAndCrusadesRegistry.WHETSTONES) || input2.is(CursesAndCrusadesRegistry.WHETSTONES))) {
            if (input1.is(CursesAndCrusadesRegistry.WHETSTONES) && input2.is(CursesAndCrusadesRegistry.WHETSTONES)) {
                return originalValue;
            }

            ItemStack whetstone = input1.is(CursesAndCrusadesRegistry.WHETSTONES) ? input1 : input2;
            ItemStack other = input1.is(CursesAndCrusadesRegistry.WHETSTONES) ? input2 : input1;

            if (!EnchantmentHelper.hasAnyEnchantments(other)) {
                return originalValue;
            }

            ItemStack resultItem;
            if (whetstone.is(CursesAndCrusadesRegistry.ENCHANTED_BLESSED_WHETSTONE)) {
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
                    if (whetstone.is(CursesAndCrusadesRegistry.BLESSED_WHETSTONE) || !enchants[rand].is(EnchantmentTags.CURSE)) {
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
            resultItem.set(CursesAndCrusadesRegistry.POST_GRINDSTONE_PROCESSING.value(), whetstone.is(CursesAndCrusadesRegistry.BLESSED_WHETSTONE));
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
