package com.kyleplo.curses_and_crusades.mixin;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.kyleplo.curses_and_crusades.CursesAndCrusades;

import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

@Mixin(TargetingConditions.class)
public abstract class TargetingConditionsMixin {
    @Redirect(method = "test", at = @At(value = "INVOKE", target = "Ljava/lang/Math;max(DD)D"))
    public double testRange(double range, double min, ServerLevel serverLevel, @Nullable LivingEntity attacker, LivingEntity potentialTarget) {
        double modifiedRange = range;
        if (potentialTarget.getAttributes().hasAttribute(CursesAndCrusades.DETECTABLE_RANGE)) {
            modifiedRange += potentialTarget.getAttributeValue(CursesAndCrusades.DETECTABLE_RANGE);
        }
        return Math.max(modifiedRange, min);
    }
}
