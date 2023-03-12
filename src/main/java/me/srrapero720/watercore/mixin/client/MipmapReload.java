package me.srrapero720.watercore.mixin.client;

import me.srrapero720.watercore.mixin.util.ModelManagerAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.VideoSettingsScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import me.srrapero720.watercore.water.WaterConsole;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.concurrent.CompletableFuture;

@OnlyIn(Dist.CLIENT)
@Mixin(value = VideoSettingsScreen.class, priority = 0)
public class MipmapReload {
    @Redirect(method = "removed", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;delayTextureReload()Ljava/util/concurrent/CompletableFuture;"))
    private CompletableFuture<Void> reloadMipMapLevels(Minecraft client) {
        WaterConsole.debug("MixinWC", "Actualizando mipmap");
        ((ModelManagerAccessor) client.getModelManager()).callApply(((ModelManagerAccessor)client.getModelManager()).callPrepare(client.getResourceManager(), client.getProfiler()), client.getResourceManager(), client.getProfiler());
        WaterConsole.debug("MixinWC", "Mipmap actualizado");
        return null;
    }
}