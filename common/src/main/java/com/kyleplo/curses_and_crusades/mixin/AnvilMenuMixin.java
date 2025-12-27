package com.kyleplo.curses_and_crusades.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.kyleplo.curses_and_crusades.CursesAndCrusadesRegistry;
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
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;
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
    @WrapMethod(method = "createResult")
    private void createResult(Operation<Void> original) {
        AnvilMenu anvilMenu = AnvilMenu.class.cast(this);
        ItemStack stack1 = anvilMenu.slots.get(0).getItem();
        ItemStack stack2 = anvilMenu.slots.get(1).getItem();

        if (!stack2.isEmpty()) {
            ItemEnchantments itemEnchantments = stack1.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
            for (Object2IntMap.Entry<Holder<Enchantment>> entry : itemEnchantments.entrySet()) {
                Holder<Enchantment> holder = entry.getKey();
                if (holder.is(CursesAndCrusadesRegistry.IMMUTABILITY_CURSE)) {
                    anvilMenu.slots.get(AnvilMenu.RESULT_SLOT).set(ItemStack.EMPTY);
                    return;
                }
            }
        }

        original.call();

        this.access.execute((level, blockPos) -> {
                if (level.isClientSide()) {
                    return;
                }
                BlockState blockState = level.getBlockState(blockPos);
                ItemStack resultItemStack = anvilMenu.slots.get(AnvilMenu.RESULT_SLOT).getItem();
                resultItemStack.set(CursesAndCrusadesRegistry.POST_ANVIL_PROCESSING.value(), blockState.is(Blocks.DAMAGED_ANVIL) ? 2
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
                if (inv.getItem(i).has(CursesAndCrusadesRegistry.POST_ANVIL_PROCESSING.value())) {
                    slot = i;
                    break;
                }
            }

            if (slot != -1) {
                takenItemStack = inv.getItem(slot);
            }
        }

        ItemEnchantments itemEnchantments = takenItemStack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);

        boolean hasInstability = false;
        int enchants = 0;

        for (Object2IntMap.Entry<Holder<Enchantment>> entry : itemEnchantments.entrySet()) {
            Holder<Enchantment> holder = entry.getKey();
            if (holder.is(CursesAndCrusadesRegistry.INSTABILITY_CURSE)) {
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
            player.level().playSound(null, player.blockPosition(), SoundEvents.ITEM_BREAK.value(), SoundSource.PLAYERS, 0.8f, 1f);
            ci.cancel();
            return;
        }

        if (takenItemStack.isEnchanted()) {
            int anvilDamage = takenItemStack.get(CursesAndCrusadesRegistry.POST_ANVIL_PROCESSING.value());
            this.access.execute((level, blockPos) -> {
                int enchantsNeededForCurse = 2 - anvilDamage;
                double curseMultiplier = anvilDamage == 2 ? 0.1
                        : (anvilDamage == 1 ? 0.5 : 0.02);
                if (itemEnchantments.size() > enchantsNeededForCurse
                        && player.getRandom().nextFloat() < (itemEnchantments.size() - enchantsNeededForCurse) * curseMultiplier) {
                    Stream<Holder<Enchantment>> curses = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT)
                            .get(EnchantmentTags.CURSE).map(HolderSet.Named::stream).orElse(Stream.empty());
                    List<EnchantmentInstance> possibleCurses = EnchantmentHelper.selectEnchantment(player.getRandom(),
                            takenItemStack, 40, curses);
                    if (possibleCurses.size() > 0) {
                        takenItemStack.enchant(possibleCurses.getFirst().enchantment(), possibleCurses.getFirst().level());
                        level.playSound(null, blockPos.getX(), blockPos.getY(), blockPos.getZ(),
                                 CursesAndCrusadesRegistry.ANVIL_APPLY_CURSE, SoundSource.BLOCKS, 1.0F, 1.0F);
                    }
                }
            });
        }

        takenItemStack.remove(CursesAndCrusadesRegistry.POST_ANVIL_PROCESSING.value());
    }
}
