package me.srrapero720.watercore.mixin.client;

import me.srrapero720.watercore.internal.WaterUtil;
import me.srrapero720.watercore.internal.WaterConsole;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.Main;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Main.class)
@OnlyIn(Dist.CLIENT)
public class MinecraftMainMixin {
    @Redirect(method = "main([Ljava/lang/String;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;renderOnThread()Z"))
    private static boolean loadAllMixinsThenShouldRenderAsync(@NotNull Minecraft instance) {
        WaterConsole.warn("SpongeLeakFixServer", "Starting force-loading all mixins and cleaning cache");
        WaterUtil.loadMixinsAndClearMixinsCache();
        return instance.renderOnThread();
    }
}