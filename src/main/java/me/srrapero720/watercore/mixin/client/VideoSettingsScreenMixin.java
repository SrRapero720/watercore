package me.srrapero720.watercore.mixin.client;

import me.srrapero720.watercore.internal.WCoreInternals;
import me.srrapero720.watercore.mixin.client.util.ModelManagerAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.VideoSettingsScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.concurrent.CompletableFuture;

@OnlyIn(Dist.CLIENT)
@Mixin(value = VideoSettingsScreen.class, priority = 0)
public class VideoSettingsScreenMixin {
    @Redirect(method = "removed", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;delayTextureReload()Ljava/util/concurrent/CompletableFuture;"))
    private CompletableFuture<Void> reloadMipMapLevels(Minecraft client) {
        WCoreInternals.qol$mipmapUpdate(client, (ModelManagerAccessor) client.getModelManager());
        return null;
    }
}