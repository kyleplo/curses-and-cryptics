package com.kyleplo.curses_and_crusades.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.kyleplo.curses_and_crusades.CursesAndCrusades;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

@Mixin(targets = {"net/minecraft/world/inventory/GrindstoneMenu$2", "net/minecraft/world/inventory/GrindstoneMenu$3"})
public abstract class GrindstoneMenuInputSlotMixin {
    private static TagKey<Item> WHETSTONE = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(CursesAndCrusades.MOD_ID, "whetstones"));

    @Inject(method = "mayPlace(Lnet/minecraft/world/item/ItemStack;)Z", at = @At("RETURN"), cancellable = true)
    private void mayPlace(ItemStack itemStack, CallbackInfoReturnable<Boolean> ci) {
        ci.setReturnValue(ci.getReturnValueZ() || itemStack.is(WHETSTONE));
    }
}