package com.kyleplo.curses_and_crusades.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.kyleplo.curses_and_crusades.CursesAndCrusades;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

@Mixin(AnvilBlock.class)
public class AnvilBlockMixin {
    TagKey<Item> ANVIL_REPAIR = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(CursesAndCrusades.MOD_ID, "anvil_repair"));

    @Inject(method = "useWithoutItem", at = @At(value = "HEAD"), cancellable = true)
    protected void useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult, CallbackInfoReturnable<InteractionResult> ci) {
        if (!level.isClientSide() && player.getMainHandItem().is(ANVIL_REPAIR) && !blockState.is(Blocks.ANVIL)) {
            player.getMainHandItem().consume(1, player);

            BlockState repairedAnvil = (BlockState)Blocks.CHIPPED_ANVIL.defaultBlockState().setValue(AnvilBlock.FACING, (Direction)blockState.getValue(AnvilBlock.FACING));
            if (blockState.is(Blocks.CHIPPED_ANVIL)) {
                repairedAnvil = (BlockState)Blocks.ANVIL.defaultBlockState().setValue(AnvilBlock.FACING, (Direction)blockState.getValue(AnvilBlock.FACING));
            }

            level.playSound(null, blockPos, SoundEvents.ANVIL_USE, SoundSource.BLOCKS, 1f, 1f);
            level.setBlock(blockPos, repairedAnvil, 2);
            level.levelEvent(1030, blockPos, 0);

            ci.setReturnValue(InteractionResult.SUCCESS);
            ci.cancel();
            return;
        }
    }
}
