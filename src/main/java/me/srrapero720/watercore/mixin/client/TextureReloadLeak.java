package me.srrapero720.watercore.mixin.client;

import com.mojang.blaze3d.platform.TextureUtil;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.system.MemoryUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

@OnlyIn(Dist.CLIENT)
@Mixin(value = TextureUtil.class, priority = 0)
public class TextureReloadLeak {
    //By Fx Morin - thanks to Icyllis Milica for [MC-226729](https://bugs.mojang.com/browse/MC-226729)
    @Inject(method = "readResource(Ljava/io/InputStream;)Ljava/nio/ByteBuffer;",locals = LocalCapture.CAPTURE_FAILHARD,at = @At(
                    value = "INVOKE",
                    target = "Lorg/lwjgl/system/MemoryUtil;memAlloc(I)Ljava/nio/ByteBuffer;",
                    shift = At.Shift.BY,
                    by = 2,
                    ordinal = 0
            ), cancellable = true
    )
    private static void readResourceWithoutLeak1(InputStream p_85304_, CallbackInfoReturnable<ByteBuffer> cir, ByteBuffer $$3, FileInputStream $$1, FileChannel $$2) throws IOException {
        try {
            while($$2.read($$3) != -1) {}
        } catch (IOException e) {
            MemoryUtil.memFree($$3);
            throw e;
        }
        cir.setReturnValue($$3);
    }


    @Inject(method = "readResource(Ljava/io/InputStream;)Ljava/nio/ByteBuffer;",locals = LocalCapture.CAPTURE_FAILHARD,at = @At(
                    value = "INVOKE",
                    target = "Ljava/nio/channels/Channels;newChannel(Ljava/io/InputStream;)" +
                            "Ljava/nio/channels/ReadableByteChannel;",
                    shift = At.Shift.BY,
                    by = 2,
                    ordinal = 0
            ),
            cancellable = true
    )
    private static void readResourceWithoutLeak2(InputStream p_85304_, CallbackInfoReturnable<ByteBuffer> cir, ByteBuffer $$4, ReadableByteChannel $$5) throws IOException {
        try {
            while($$5.read($$4) != -1)
                if ($$4.remaining() == 0) $$4 = MemoryUtil.memRealloc($$4, $$4.capacity() * 2);
        } catch (IOException e) {
            MemoryUtil.memFree($$4);
            throw e;
        }
        cir.setReturnValue($$4);
    }
}