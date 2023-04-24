package me.srrapero720.watercore.mixin.common;

import me.srrapero720.watercore.internal.WCoreInternals;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MinecraftServer.class)
public abstract class MinecraftServerMixin {
    @Inject(method = "runServer()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;updateStatusIcon(Lnet/minecraft/network/protocol/status/ServerStatus;)V"))
    public void injectRunServer(CallbackInfo ci) { WCoreInternals.core$prepareDimensionSetup((MinecraftServer) (Object) this); }
}
