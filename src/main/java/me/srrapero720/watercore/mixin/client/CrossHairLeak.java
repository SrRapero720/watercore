package me.srrapero720.watercore.mixin.client;

import me.srrapero720.watercore.water.WaterConsole;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class CrossHairLeak {
    @Shadow @Nullable
    public Entity crosshairPickEntity;
    @Shadow @Nullable
    public HitResult hitResult;

    @Inject(method = "updateScreenAndTick", at = @At(value = "INVOKE",target = "Lnet/minecraft/client/Minecraft;runTick(Z)V",shift = At.Shift.BEFORE))
    private void resetTarget(Screen screen, CallbackInfo ci) {
        WaterConsole.log("WorldLoaderFix", "Cleaning CrossHair and HitResult");
        this.crosshairPickEntity = null;
        this.hitResult = null;
        System.gc();
    }
}
