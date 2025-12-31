package com.kyleplo.curses_and_crusades.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.kyleplo.curses_and_crusades.CursesAndCrusadesRegistry;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

@Mixin(targets = "net/minecraft/world/inventory/GrindstoneMenu$4")
public class GrindstoneMenuOutputSlotMixin {
    @Inject(method = "getExperienceFromItem(Lnet/minecraft/world/item/ItemStack;)I", at = @At("INVOKE"), cancellable = true)
    private void getExperienceFromItem(ItemStack itemStack, CallbackInfoReturnable<Integer> ci) {
        if (itemStack.is(CursesAndCrusadesRegistry.WHETSTONES)) {
            ci.setReturnValue(Integer.MIN_VALUE);
        }
    }

    @Inject(method = "onTake", at = @At("INVOKE"), cancellable = true)
    private void onTake(Player player, ItemStack eventItemStack, CallbackInfo ci) {
        if (player.level().isClientSide()) {
            return;
        }

        ItemStack takenItemStack = eventItemStack;

        if (takenItemStack.isEmpty()) {
            Inventory inv = player.getInventory();
            int slot = -1;
            for (int i = 0; i < inv.getContainerSize(); i++) {
                if (inv.getItem(i).has(CursesAndCrusadesRegistry.POST_GRINDSTONE_PROCESSING.value())) {
                    slot = i;
                    break;
                }
            }

            if (slot != -1) {
                takenItemStack = inv.getItem(slot);
            }
        }

        takenItemStack.remove(CursesAndCrusadesRegistry.POST_GRINDSTONE_PROCESSING.value());
    }

    @WrapOperation(method = "onTake", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/Container;setItem(ILnet/minecraft/world/item/ItemStack;)V"))
    private void onTakeSetItem(Container self, int slot, ItemStack item, Operation<Void> original) {
        if (self.getItem(slot).is(CursesAndCrusadesRegistry.WHETSTONES)) {
            self.getItem(slot).consume(1, null);
        } else {
            original.call(self, slot, item);
        }
    }
}
