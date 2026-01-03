package com.kyleplo.curses_and_cryptics.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.kyleplo.curses_and_cryptics.CursesAndCryptics;
import com.kyleplo.curses_and_cryptics.CursesAndCrypticsRegistry;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.stream.Stream;

@Mixin(AnvilMenu.class)
public abstract class AnvilMenuMixin extends ItemCombinerMenuMixin {
    @Shadow
    private final DataSlot cost;

    private AnvilMenuMixin () {
        cost = null;
        throw new AssertionError("this shouldn't happen");
    }

    @WrapMethod(method = "createResult")
    private void createResult(Operation<Void> original) {
        AnvilMenu anvilMenu = AnvilMenu.class.cast(this);
        ItemStack stack1 = anvilMenu.slots.get(0).getItem();
        ItemStack stack2 = anvilMenu.slots.get(1).getItem();

        boolean hasMisappropriationCurse = false;

        if (!stack2.isEmpty()) {
            ItemEnchantments itemEnchantments = stack1.getOrDefault(DataComponents.ENCHANTMENTS,
                    ItemEnchantments.EMPTY);
            for (Object2IntMap.Entry<Holder<Enchantment>> entry : itemEnchantments.entrySet()) {
                Holder<Enchantment> holder = entry.getKey();
                if (holder.is(CursesAndCrypticsRegistry.IMMUTABILITY_CURSE)) {
                    anvilMenu.slots.get(AnvilMenu.RESULT_SLOT).set(ItemStack.EMPTY);
                    return;
                }

                if (holder.is(CursesAndCrypticsRegistry.MISAPPROPRIATION_CURSE)) {
                    hasMisappropriationCurse = true;
                }
            }
        }

        original.call();

        if (CursesAndCryptics.config.crypticEnchantedBookCombining && stack1.is(CursesAndCrypticsRegistry.CRYPTIC_ENCHANTED_BOOK)
                && stack2.is(CursesAndCrypticsRegistry.CRYPTIC_ENCHANTED_BOOK)) {
            int stack1Level = stack1.getOrDefault(CursesAndCrypticsRegistry.CRYPTIC_ENCHANTED_BOOK_LEVEL.value(), 1);
            int stack2Level = stack2.getOrDefault(CursesAndCrypticsRegistry.CRYPTIC_ENCHANTED_BOOK_LEVEL.value(), 1);
            int levelSum = stack1Level + stack2Level;
            ItemStack resultingBook = new ItemStack(CursesAndCrypticsRegistry.CRYPTIC_ENCHANTED_BOOK);
            resultingBook.set(CursesAndCrypticsRegistry.CRYPTIC_ENCHANTED_BOOK_LEVEL.value(), levelSum);
            anvilMenu.slots.get(AnvilMenu.RESULT_SLOT).set(resultingBook);
            int potentialCost = levelSum - (Math.max(stack1Level, stack2Level) / 2);
            cost.set(Math.max(1, levelSum > 40 ? Math.max(potentialCost, 41) : potentialCost));
        }

        if (stack1.has(DataComponents.REPAIR_COST) && !hasMisappropriationCurse && stack2.is(CursesAndCrypticsRegistry.MISAPPROPRIATION_SIGIL)) {
            ItemStack result = stack1.copy();
            result.remove(DataComponents.REPAIR_COST);

            this.access.execute((level, blockPos) -> {
                if (CursesAndCryptics.config.misappropriationCurse) {
                    result.enchant(level.registryAccess().getOrThrow(CursesAndCrypticsRegistry.MISAPPROPRIATION_CURSE), 1);
                }
                anvilMenu.slots.get(AnvilMenu.RESULT_SLOT).set(result);
                cost.set(Math.max(stack1.getOrDefault(DataComponents.REPAIR_COST, 0), 1));
            });
        }

        if (stack1.has(DataComponents.ENCHANTABLE) && ((!stack1.is(Items.BOOK) && !stack1.has(DataComponents.STORED_ENCHANTMENTS)) || CursesAndCryptics.config.crypticEnchantedBookWorksOnBooks) && stack2.is(CursesAndCrypticsRegistry.CRYPTIC_ENCHANTED_BOOK)) {
            ItemStack result = stack1.copy();
            if (CursesAndCryptics.config.crypticEnchantedBookHideResult) {
                result.set(CursesAndCrypticsRegistry.RESULTS_HIDDEN.value(), Unit.INSTANCE);
            }

            this.access.execute((level, blockPos) -> {
                boolean enchantsApplied = false;
                for (int enchantLevel = stack2.getOrDefault(CursesAndCrypticsRegistry.CRYPTIC_ENCHANTED_BOOK_LEVEL.value(), 1); enchantLevel > 0; enchantLevel -= 0) {
                    Stream<Holder<Enchantment>> enchants = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT)
                        .get(CursesAndCrypticsRegistry.CRYPTIC_ENCHANTED_BOOK_ENCHANTMENT).map(HolderSet.Named::stream).orElse(Stream.empty());
                    List<EnchantmentInstance> possibleEnchants = EnchantmentHelper.selectEnchantment(level.random,
                            stack1, enchantLevel, enchants);
                    if (possibleEnchants.size() > 0 && EnchantmentHelper.isEnchantmentCompatible(result.getEnchantments().keySet(), possibleEnchants.getFirst().enchantment())) {
                        enchantsApplied = true;
                        result.enchant(possibleEnchants.getFirst().enchantment(),
                                    possibleEnchants.getFirst().level());
                        enchantLevel -= Math.max(possibleEnchants.getFirst().enchantment().value().getAnvilCost(), 2);
                    } else {
                        enchantLevel -= 5;
                    }
                }

                if (enchantsApplied) {
                    result.set(DataComponents.REPAIR_COST, AnvilMenu.calculateIncreasedRepairCost(stack1.getOrDefault(DataComponents.REPAIR_COST, 0)));
                    anvilMenu.slots.get(AnvilMenu.RESULT_SLOT).set(result);
                    cost.set(stack1.getOrDefault(DataComponents.REPAIR_COST, 0) + stack2.getOrDefault(CursesAndCrypticsRegistry.CRYPTIC_ENCHANTED_BOOK_LEVEL.value(), 1));
                }
            });
        }

        this.access.execute((level, blockPos) -> {
            if (level.isClientSide()) {
                return;
            }
            BlockState blockState = level.getBlockState(blockPos);
            ItemStack resultItemStack = anvilMenu.slots.get(AnvilMenu.RESULT_SLOT).getItem();
            resultItemStack.set(CursesAndCrypticsRegistry.POST_ANVIL_PROCESSING.value(),
                    blockState.is(Blocks.DAMAGED_ANVIL) ? 2
                            : (blockState.is(Blocks.CHIPPED_ANVIL) ? 1 : 0));
        });
    }

    private ItemStack takenItemStack;

    @Inject(method = "onTake", at = @At(value = "HEAD"), cancellable = true)
    private void onTake(Player player, ItemStack eventItemStack, CallbackInfo ci) {
        if (player.level().isClientSide()) {
            return;
        }

        AnvilMenu anvilMenu = AnvilMenu.class.cast(this);

        takenItemStack = eventItemStack;

        if (takenItemStack.isEmpty()) {
            Inventory inv = player.getInventory();
            int slot = -1;
            for (int i = 0; i < inv.getContainerSize(); i++) {
                if (inv.getItem(i).has(CursesAndCrypticsRegistry.POST_ANVIL_PROCESSING.value())) {
                    slot = i;
                    break;
                }
            }

            if (slot != -1) {
                takenItemStack = inv.getItem(slot);
            }
        }

        ItemEnchantments itemEnchantments = takenItemStack.getOrDefault(DataComponents.ENCHANTMENTS,
                ItemEnchantments.EMPTY);

        boolean hasInstability = false;
        int enchants = 0;

        for (Object2IntMap.Entry<Holder<Enchantment>> entry : itemEnchantments.entrySet()) {
            Holder<Enchantment> holder = entry.getKey();
            if (holder.is(CursesAndCrypticsRegistry.INSTABILITY_CURSE)) {
                hasInstability = true;
            } else {
                enchants++;
            }
        }

        if (hasInstability && player.getRandom().nextFloat() < ((float) enchants / (4.0 + (float) enchants))
                && !takenItemStack.has(DataComponents.UNBREAKABLE)) {
            takenItemStack.setCount(0);
            anvilMenu.slots.get(AnvilMenu.INPUT_SLOT).set(ItemStack.EMPTY);
            anvilMenu.slots.get(AnvilMenu.RESULT_SLOT).set(ItemStack.EMPTY);
            player.level().playSound(null, player.blockPosition(), SoundEvents.ITEM_BREAK.value(), SoundSource.PLAYERS,
                    0.8f, 1f);
            ci.cancel();
            return;
        }

        if (takenItemStack.isEnchanted() && CursesAndCryptics.config.anvilCurses) {
            int anvilDamage = takenItemStack.get(CursesAndCrypticsRegistry.POST_ANVIL_PROCESSING.value());
            this.access.execute((level, blockPos) -> {
                int enchantsNeededForCurse = 2 - anvilDamage;
                double curseMultiplier = anvilDamage == 2 ? CursesAndCryptics.config.damagedAnvilCurseChance
                        : (anvilDamage == 1 ? CursesAndCryptics.config.chippedAnvilCurseChance
                                : CursesAndCryptics.config.defaultAnvilCurseChance);
                if (itemEnchantments.size() > enchantsNeededForCurse
                        && player.getRandom().nextFloat() < (itemEnchantments.size() - enchantsNeededForCurse)
                                * curseMultiplier) {
                    Stream<Holder<Enchantment>> curses = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT)
                            .get(EnchantmentTags.CURSE).map(HolderSet.Named::stream).orElse(Stream.empty());
                    List<EnchantmentInstance> possibleCurses = EnchantmentHelper.selectEnchantment(player.getRandom(),
                            takenItemStack, 40, curses);
                    if (possibleCurses.size() > 0 && EnchantmentHelper.isEnchantmentCompatible(takenItemStack.getEnchantments().keySet(), possibleCurses.getFirst().enchantment())) {
                        takenItemStack.enchant(possibleCurses.getFirst().enchantment(),
                                possibleCurses.getFirst().level());
                        level.playSound(null, blockPos.getX(), blockPos.getY(), blockPos.getZ(),
                                CursesAndCrypticsRegistry.ANVIL_APPLY_CURSE, SoundSource.BLOCKS, 1.0F, 1.0F);
                    }
                }
            });
        }

        takenItemStack.remove(CursesAndCrypticsRegistry.POST_ANVIL_PROCESSING.value());
        takenItemStack.remove(CursesAndCrypticsRegistry.RESULTS_HIDDEN.value());
    }
}
