package me.srrapero720.watercore.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CraftingTableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CraftingTableBlock.class)
public abstract class BrokenMixin {
    @Inject(method = "use", at = @At(value = "HEAD"))
    public void injectHurtEnemy(BlockState p_52233_, Level p_52234_, BlockPos p_52235_, Player p_52236_, InteractionHand p_52237_, BlockHitResult p_52238_, CallbackInfoReturnable<InteractionResult> cir) {
        throw new RuntimeException("This mixin is full of BROKEN!!");
    }
}
