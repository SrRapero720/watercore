package me.srrapero720.watercore.mixin.client;

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
@Mixin(value = ClientPacketListener.class, priority = 0)
public class ClientPacketMixin {
    @Shadow private ClientLevel level;

    // I change my old rusty Overwrite with a brand new Redirect
    // Credits to Forget me chunk...
    @Redirect(method = "queueLightUpdate(Lnet/minecraft/network/protocol/game/ClientboundForgetLevelChunkPacket;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;queueLightUpdate(Ljava/lang/Runnable;)V"))
    private void redirectQueueLightUpdate(ClientLevel instance, Runnable runnable, ClientboundForgetLevelChunkPacket packet) {
        instance.queueLightUpdate(() -> {
            LevelLightEngine levellightengine = this.level.getLightEngine();

            levellightengine.enableLightSources(new ChunkPos(packet.getX(), packet.getZ()), false);
            this.level.setLightReady(packet.getX(), packet.getZ());
        });
    }
}
