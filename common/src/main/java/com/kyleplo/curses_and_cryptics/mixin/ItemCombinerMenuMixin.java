package com.kyleplo.curses_and_cryptics.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ItemCombinerMenu;

@Mixin(ItemCombinerMenu.class)
public abstract class ItemCombinerMenuMixin {
    @Shadow
    public ContainerLevelAccess access;
}
