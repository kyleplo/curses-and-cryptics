package com.kyleplo.curses_and_cryptics.mixin;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.kyleplo.curses_and_cryptics.CursesAndCrypticsRegistry;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

@Mixin(TargetingConditions.class)
public abstract class TargetingConditionsMixin {
    @Shadow
    private double range;

    @Shadow
    private boolean checkLineOfSight;

    @WrapMethod(method = "test")
    public boolean test(ServerLevel serverLevel, @Nullable LivingEntity attacker, LivingEntity potentialTarget, Operation<Boolean> original) {
        double originalRange = this.range;
        boolean originalLineOfSight = this.checkLineOfSight;

        if (potentialTarget.getAttributes().hasAttribute(CursesAndCrypticsRegistry.DETECTABLE_RANGE)) {
            double attrVal = potentialTarget.getAttributeValue(CursesAndCrypticsRegistry.DETECTABLE_RANGE);
            this.range += attrVal;
            if (attrVal > 0) {
                this.checkLineOfSight = false;
            }
        }

        boolean value = original.call(serverLevel, attacker, potentialTarget);

        this.range = originalRange;
        this.checkLineOfSight = originalLineOfSight;

        return value;
    }
}
