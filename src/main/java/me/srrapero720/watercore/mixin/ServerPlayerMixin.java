package me.srrapero720.watercore.mixin;

import com.mojang.authlib.GameProfile;
import me.srrapero720.watercore.custom.data.BackData;
import me.srrapero720.watercore.custom.data.storage.SimplePlayerStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player {
    public ServerPlayerMixin(Level p_36114_, BlockPos p_36115_, float p_36116_, GameProfile p_36117_) {
        super(p_36114_, p_36115_, p_36116_, p_36117_);
    }

    @Inject(method = "teleportTo(Lnet/minecraft/server/level/ServerLevel;DDDFF)V", at = @At(value = "HEAD"))
    public void injectTeleportTo2(ServerLevel p_9000_, double p_9001_, double p_9002_, double p_9003_, float p_9004_, float p_9005_, CallbackInfo ci) {
        SimplePlayerStorage.saveBackData((ServerPlayer) (Object) this);
    }


    @Inject(method = "teleportTo(DDD)V", at = @At(value = "HEAD"))
    public void injectTeleportTo(double p_8969_, double p_8970_, double p_8971_, CallbackInfo ci) {
        SimplePlayerStorage.saveBackData((ServerPlayer) (Object) this);
    }
}
