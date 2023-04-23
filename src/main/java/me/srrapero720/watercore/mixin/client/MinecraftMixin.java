package me.srrapero720.watercore.mixin.client;

import com.mojang.blaze3d.platform.*;
import io.netty.buffer.AbstractReferenceCountedByteBuf;
import me.srrapero720.watercore.internal.WConsole;
import me.srrapero720.watercore.internal.WCoreUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;
import java.nio.ByteBuffer;

@OnlyIn(Dist.CLIENT)
@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Unique private ByteBuffer WCBBuffer;
    @Shadow @Nullable public Entity crosshairPickEntity;
    @Shadow @Nullable public HitResult hitResult;
    @Shadow public Entity cameraEntity;

    @Inject(method = "tick", at = @At("RETURN"))
    private void releaseAfterTick(CallbackInfo ci) {
        WCoreUtil.BUFFERS.removeIf((buffer) -> {
            if (buffer.source instanceof AbstractReferenceCountedByteBuf) return true;
            return buffer.refCnt() == 0 && buffer.release();
        });
    }

    // REDIRECTS GLUTIL TO SAVE THE ByteBuffer ANS USE IT LATER
    @Redirect(method = "grabHugeScreenshot", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/GlUtil;allocateMemory(I)Ljava/nio/ByteBuffer;"))
    private ByteBuffer setNewByteBuffer(int size, File dir, int uW, int uH, int w, int h) {
        return this.WCBBuffer = GlUtil.allocateMemory(uW * uH * 3);
    }

    // IF SCREENSHOT FAILS, CLEARS EVERYTHING
    @Inject(method = "grabHugeScreenshot", at = @At(value = "INVOKE", remap = false, target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;Ljava/lang/Throwable;)V" ))
    private void freeByteBuffer(File __, int ___, int ____, int _____, int ______, CallbackInfoReturnable<Component> ________) {
        GlUtil.freeMemory(this.WCBBuffer);
    }


    // FIX MEMORYLEAK: WORLD KEEPS LOADED.
    @Inject(method = "updateScreenAndTick", at = @At(value = "INVOKE",target = "Lnet/minecraft/client/Minecraft;runTick(Z)V",shift = At.Shift.BEFORE))
    private void injectUpdateScreenAndTick(Screen screen, CallbackInfo ci) {
        WConsole.log("r", "Cleaning crosshair, hitresult and cameraEntity");
        this.crosshairPickEntity = null;
        this.hitResult = null;
        this.cameraEntity = null;
        System.gc();
    }
}
