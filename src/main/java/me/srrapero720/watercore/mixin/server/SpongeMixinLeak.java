package me.srrapero720.watercore.mixin.server;

import me.srrapero720.watercore.mixin_util.SpongeMixinTool;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import me.srrapero720.watercore.SrConsole;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
@OnlyIn(Dist.DEDICATED_SERVER)
public class SpongeMixinLeak {
    @Inject(method = "loadLevel", at = @At("RETURN"))
    private void onFinishedLoadingWorlds(CallbackInfo ci) {
        SrConsole.warn("SpongeLeakFixServer", "Forzando carga de todos los mixins y limpiando cache");
        SpongeMixinTool.forceLoadAllMixinsAndClearSpongePoweredCache();
    }
}