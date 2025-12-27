package com.kyleplo.curses_and_crusades.mixin;

import org.spongepowered.asm.mixin.Mixin;

import com.kyleplo.curses_and_crusades.CursesAndCrusadesRegistry;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @WrapMethod(method = "createLivingAttributes")
    private static AttributeSupplier.Builder createLivingAttributes(Operation<AttributeSupplier.Builder> original) {
        return original.call().add(CursesAndCrusadesRegistry.DETECTABLE_RANGE);
    }
}
