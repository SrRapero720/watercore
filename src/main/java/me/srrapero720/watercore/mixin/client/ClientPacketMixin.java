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

@OnlyIn(Dist.CLIENT)
@Mixin(value = ClientPacketListener.class, priority = 0)
public abstract class ClientPacketMixin {
    @Shadow private ClientLevel level;

    /**
     * @author SrRapero720
     * @reason For better performance, I remove the iterator related with light engine (and mixin lambdas no works in
     * forge)
     * I love mixins <3
     */
    @Overwrite
    private void queueLightUpdate(ClientboundForgetLevelChunkPacket p_194253_) {
        this.level.queueLightUpdate(() -> {
            LevelLightEngine levellightengine = this.level.getLightEngine();

            levellightengine.enableLightSources(new ChunkPos(p_194253_.getX(), p_194253_.getZ()), false);
            this.level.setLightReady(p_194253_.getX(), p_194253_.getZ());
        });
    }
}
