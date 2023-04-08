package me.srrapero720.watercore.mixin;

import me.srrapero720.watercore.internal.WaterConsole;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.ServerLevelData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public class ServerLevelMixin {

    @Shadow @Final private ServerLevelData serverLevelData;

    @Inject(method = "advanceWeatherCycle", at = @At("HEAD"))
    public void injectAdvanceWeatherCycle(CallbackInfo ci) {
        int i = this.serverLevelData.getClearWeatherTime();
        int j = this.serverLevelData.getThunderTime();
        int k = this.serverLevelData.getRainTime();
        WaterConsole.justPrint("Clear: " + i + " Thunder: " + j + " Rain: " + k);
    }
}
