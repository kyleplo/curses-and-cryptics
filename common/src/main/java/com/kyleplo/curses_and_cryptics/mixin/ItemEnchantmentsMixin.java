package com.kyleplo.curses_and_cryptics.mixin;

import java.util.Iterator;
import java.util.function.Consumer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.kyleplo.curses_and_cryptics.CursesAndCrypticsRegistry;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;

@Mixin(ItemEnchantments.class)
public abstract class ItemEnchantmentsMixin {
    @Inject(method = "addToTooltip", at = @At(value = "HEAD"), cancellable = true)
    public void addToTooltip(Item.TooltipContext tooltipContext, Consumer<Component> consumer, TooltipFlag tooltipFlag, DataComponentGetter dataComponentGetter, CallbackInfo ci) {
        ItemEnchantments itemEnchantments = ItemEnchantments.class.cast(this);
        Iterator<Object2IntMap.Entry<Holder<Enchantment>>> iterator = itemEnchantments.entrySet().iterator();

        boolean hasObscuring = false;
        int hidden = 0;

        while (iterator.hasNext()) {
            Object2IntMap.Entry<Holder<Enchantment>> enchant = iterator.next();
            if (enchant.getIntValue() > 0) {
                if (enchant.getKey().is(CursesAndCrypticsRegistry.OBSCURING_CURSE)) {
                    consumer.accept(Enchantment.getFullname(enchant.getKey(), enchant.getIntValue()));
                    hasObscuring = true;
                } else {
                    hidden++;
                }
            }
        }

        if (hasObscuring) {
            for (int i = 0; i < hidden; i++) {
                consumer.accept(Component.translatable("enchantment.curses_and_cryptics.obscuring_hidden").withStyle(ChatFormatting.DARK_GRAY));
            }
            ci.cancel();
            return;
        }
    }
}
