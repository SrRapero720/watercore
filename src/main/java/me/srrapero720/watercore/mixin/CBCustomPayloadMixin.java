package me.srrapero720.watercore.mixin;

import me.srrapero720.watercore.internal.WaterUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ClientboundCustomPayloadPacket.class, priority = 0)
public class CBCustomPayloadMixin {
    @Shadow @Final private FriendlyByteBuf data;

    @Inject(method = "handle(Lnet/minecraft/network/protocol/game/ClientGamePacketListener;)V", at = @At(value = "RETURN"))
    public void injectHandle(ClientGamePacketListener packet, CallbackInfo ci) {
        WaterUtil.BUFFERS.add(this.data);
    }
}
