package me.srrapero720.watercore.mixin.server;

import me.srrapero720.watercore.internal.WCoreInternals;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
@OnlyIn(Dist.DEDICATED_SERVER)
public class MinecraftServerMixin {

    // FIXES MIXIN "MEMORY LEAK". IN REALITY IS MORE LIKE CLEANING NON USED STUFF.
    @Inject(method = "loadLevel", at = @At("RETURN"))
    private void onFinishedLoadingWorlds(CallbackInfo ci) {
        WCoreInternals.leaks$flushSpongePoweredMixinCache();
    }
}