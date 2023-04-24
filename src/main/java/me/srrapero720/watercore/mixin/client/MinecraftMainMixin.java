package me.srrapero720.watercore.mixin.client;

import me.srrapero720.watercore.internal.WCoreInternals;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.Main;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Main.class)
@OnlyIn(Dist.CLIENT)
public class MinecraftMainMixin {
    @Shadow @Final private static Logger LOGGER;

    @Redirect(method = "main([Ljava/lang/String;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;renderOnThread()Z"))
    private static boolean redirectRenderOnThread(@NotNull Minecraft instance) {

        WCoreInternals.leaks$flushSpongePoweredMixinCache();
        return instance.renderOnThread();
    }
}