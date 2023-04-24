package me.srrapero720.watercore.mixin.client;

import me.srrapero720.watercore.internal.WCoreInternals;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundForgetLevelChunkPacket;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@OnlyIn(Dist.CLIENT)
@Mixin(value = ClientPacketListener.class, priority = 72)
public class ClientPacketMixin {
    @Redirect(
            method = "queueLightUpdate(Lnet/minecraft/network/protocol/game/ClientboundForgetLevelChunkPacket;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;queueLightUpdate(Ljava/lang/Runnable;)V")
    )
    private void redirectQueueLightUpdate(ClientLevel clientLevel, Runnable runnable, ClientboundForgetLevelChunkPacket packet) {
        WCoreInternals.leaks$forgetChunkLightUpdate(clientLevel, packet);
    }
}
