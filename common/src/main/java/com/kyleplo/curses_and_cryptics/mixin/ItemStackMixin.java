package com.kyleplo.curses_and_cryptics.mixin;

import java.util.function.Consumer;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.kyleplo.curses_and_cryptics.CursesAndCrypticsRegistry;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Inject(method = "getBarWidth", at = @At(value = "HEAD"), cancellable = true)
    public void getBarWidth(CallbackInfoReturnable<Integer> ci) {
        ItemStack itemStack = ItemStack.class.cast(this);

        if (!itemStack.isEnchanted() || itemStack.has(CursesAndCrypticsRegistry.RESULTS_HIDDEN.value())) return;

        ItemEnchantments itemEnchantments = itemStack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);

        for (Object2IntMap.Entry<Holder<Enchantment>> entry : itemEnchantments.entrySet()) {
            Holder<Enchantment> holder = entry.getKey();
            if (holder.is(CursesAndCrypticsRegistry.QUIXOTISM_CURSE)) {
                ci.setReturnValue(Item.MAX_BAR_WIDTH);
                return;
            }
        }
    }

    @Inject(method = "getBarColor", at = @At(value = "HEAD"), cancellable = true)
    public void getBarColor(CallbackInfoReturnable<Integer> ci) {
        ItemStack itemStack = ItemStack.class.cast(this);

        if (!itemStack.isEnchanted() || itemStack.has(CursesAndCrypticsRegistry.RESULTS_HIDDEN.value())) return;

        ItemEnchantments itemEnchantments = itemStack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);

        for (Object2IntMap.Entry<Holder<Enchantment>> entry : itemEnchantments.entrySet()) {
            Holder<Enchantment> holder = entry.getKey();
            if (holder.is(CursesAndCrypticsRegistry.QUIXOTISM_CURSE)) {
                ci.setReturnValue(ChatFormatting.RED.getColor());
                return;
            }
        }
    }

    @Inject(method = "isBarVisible", at = @At(value = "HEAD"), cancellable = true)
    public void isBarVisible(CallbackInfoReturnable<Boolean> ci) {
        ItemStack itemStack = ItemStack.class.cast(this);

        if (!itemStack.isEnchanted() || itemStack.has(CursesAndCrypticsRegistry.RESULTS_HIDDEN.value())) return;

        ItemEnchantments itemEnchantments = itemStack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);

        for (Object2IntMap.Entry<Holder<Enchantment>> entry : itemEnchantments.entrySet()) {
            Holder<Enchantment> holder = entry.getKey();
            if (holder.is(CursesAndCrypticsRegistry.QUIXOTISM_CURSE)) {
                ci.setReturnValue(true);
                return;
            }
        }
    }

    @WrapMethod(method = "addDetailsToTooltip")
    public void addDetailsToTooltip(Item.TooltipContext tooltipContext, TooltipDisplay tooltipDisplay, @Nullable Player player, TooltipFlag tooltipFlag, Consumer<Component> consumer, Operation<Void> original) {
        ItemStack itemStack = ItemStack.class.cast(this);

        if (itemStack.isEnchanted()) {
            ItemEnchantments itemEnchantments = itemStack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);

            for (Object2IntMap.Entry<Holder<Enchantment>> entry : itemEnchantments.entrySet()) {
                Holder<Enchantment> holder = entry.getKey();
                if (holder.is(CursesAndCrypticsRegistry.QUIXOTISM_CURSE)) {
                    tooltipDisplay = tooltipDisplay.withHidden(DataComponents.DAMAGE, true);
                }

                if (holder.is(CursesAndCrypticsRegistry.OBSCURING_CURSE)) {
                    tooltipDisplay = tooltipDisplay.withHidden(DataComponents.ATTRIBUTE_MODIFIERS, true);
                }
            }
        }

        ItemLore originalItemLore = itemStack.getOrDefault(DataComponents.LORE, ItemLore.EMPTY);
        ItemLore itemLore = originalItemLore;

        if (itemStack.has(CursesAndCrypticsRegistry.RESULTS_HIDDEN.value())) {
            tooltipDisplay = tooltipDisplay.withHidden(DataComponents.ATTRIBUTE_MODIFIERS, true).withHidden(DataComponents.ENCHANTMENTS, true).withHidden(DataComponents.STORED_ENCHANTMENTS, true);
            itemLore = itemLore.withLineAdded(Component.translatable("container.take_item_hint").withStyle(Style.EMPTY.withColor(ChatFormatting.GRAY).withItalic(false)));
        }

        if (itemStack.has(CursesAndCrypticsRegistry.CRYPTIC_ENCHANTED_BOOK_LEVEL.value()) || itemStack.is(CursesAndCrypticsRegistry.CRYPTIC_ENCHANTED_BOOK)) {
            itemLore = itemLore.withLineAdded(Component.translatable("item.curses_and_cryptics.cryptic_enchanted_book.level_tooltip", itemStack.getOrDefault(CursesAndCrypticsRegistry.CRYPTIC_ENCHANTED_BOOK_LEVEL.value(), 1)).withStyle(Style.EMPTY.withColor(ChatFormatting.GRAY).withItalic(false)));
        }

        itemStack.set(DataComponents.LORE, itemLore);
        original.call(tooltipContext, tooltipDisplay, player, tooltipFlag, consumer);

        itemStack.set(DataComponents.LORE, originalItemLore);
    }

    @WrapMethod(method = "isEnchantable")
    public boolean isEnchantable(Operation<Boolean> original) {
        boolean originalValue = original.call();
        ItemStack itemStack = ItemStack.class.cast(this);

        if (itemStack.isEnchanted() && originalValue) {
            ItemEnchantments itemEnchantments = itemStack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);

            for (Object2IntMap.Entry<Holder<Enchantment>> entry : itemEnchantments.entrySet()) {
                Holder<Enchantment> holder = entry.getKey();
                if (holder.is(CursesAndCrypticsRegistry.IMMUTABILITY_CURSE)) {
                    return false;
                }
            }
        }

        return originalValue;
    }
}
