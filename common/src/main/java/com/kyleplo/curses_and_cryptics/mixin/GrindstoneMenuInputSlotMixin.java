package com.kyleplo.curses_and_cryptics.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.kyleplo.curses_and_cryptics.CursesAndCrypticsRegistry;

import net.minecraft.world.item.ItemStack;

@Mixin(targets = {"net/minecraft/world/inventory/GrindstoneMenu$2", "net/minecraft/world/inventory/GrindstoneMenu$3"})
public abstract class GrindstoneMenuInputSlotMixin {
    @Inject(method = "mayPlace(Lnet/minecraft/world/item/ItemStack;)Z", at = @At("RETURN"), cancellable = true)
    private void mayPlace(ItemStack itemStack, CallbackInfoReturnable<Boolean> ci) {
        ci.setReturnValue(ci.getReturnValueZ() || itemStack.is(CursesAndCrypticsRegistry.WHETSTONES) || itemStack.is(CursesAndCrypticsRegistry.CRYPTIC_ENCHANTED_BOOK));
    }
}