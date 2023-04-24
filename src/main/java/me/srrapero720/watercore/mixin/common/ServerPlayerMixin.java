package me.srrapero720.watercore.mixin.common;

import me.srrapero720.watercore.internal.WCoreInternals;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ServerPlayer.class, priority = 72)
public class ServerPlayerMixin {
    @Inject(method = "teleportTo(Lnet/minecraft/server/level/ServerLevel;DDDFF)V", at = @At(value = "HEAD"))
    public void injectTeleportTo1(ServerLevel pNewLevel, double pX, double pY, double pZ, float pYaw, float pPitch, CallbackInfo ci) {
        WCoreInternals.core$onPlayerTeleport((ServerPlayer) (Object) this);
    }

    @Inject(method = "teleportTo(DDD)V", at = @At(value = "HEAD"))
    public void injectTeleportTo2(double pX, double pY, double pZ, CallbackInfo ci) {
        WCoreInternals.core$onPlayerTeleport((ServerPlayer) (Object) this);
    }
}
