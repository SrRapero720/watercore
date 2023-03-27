package me.srrapero720.watercore.mixin.client;

import com.mojang.blaze3d.platform.TextureUtil;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.system.MemoryUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

@OnlyIn(Dist.CLIENT)
@Mixin(value = TextureUtil.class, priority = 0)
public class TextureReloadLeak {

    /**
     * @author SrRapero720
     * @reason I'm disappointment with Mojang code
     * Can be removed in 1.19.3
     */
    @Overwrite
    public static ByteBuffer readResource(InputStream stream) throws IOException {
        if (stream instanceof FileInputStream fileStream) {
            var filechannel = fileStream.getChannel();
            var bytebuffer = MemoryUtil.memAlloc((int) filechannel.size() + 1);

            try {
                while(filechannel.read(bytebuffer) != -1);
            } catch (IOException e) {
                MemoryUtil.memFree(bytebuffer);
                throw e;
            }

            return bytebuffer;
        } else {
            var bytebuffer = MemoryUtil.memAlloc(8192);
            var byteChannel = Channels.newChannel(stream);

            try {
                while(byteChannel.read(bytebuffer) != -1)
                    if (bytebuffer.remaining() == 0)
                        bytebuffer = MemoryUtil.memRealloc(bytebuffer, bytebuffer.capacity() * 2);
            } catch (IOException e) {
                MemoryUtil.memFree(bytebuffer);
                throw e;
            }

            return bytebuffer;
        }
    }
}