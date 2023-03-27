package me.srrapero720.watercore.mixin.client;

import io.netty.buffer.AbstractReferenceCountedByteBuf;
import me.srrapero720.watercore.internal.WaterUtil;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@OnlyIn(Dist.CLIENT)
@Mixin(Minecraft.class)
public class MinecraftClientMixin {

    @Inject(method = "tick", at = @At("RETURN"))
    private void releaseAfterTick(CallbackInfo ci) {
        WaterUtil.BUFFERS.removeIf((buffer) -> {
            if (buffer.source instanceof AbstractReferenceCountedByteBuf) return true;
            return buffer.refCnt() == 0 && buffer.release();
        });
    }
}
