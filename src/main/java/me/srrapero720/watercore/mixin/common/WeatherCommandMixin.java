package me.srrapero720.watercore.mixin.common;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.commands.WeatherCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WeatherCommand.class)
public class WeatherCommandMixin {
    @Inject(method = "setClear", at = @At("HEAD"))
    private static void injectSetClear(CommandSourceStack stack, int time, CallbackInfoReturnable<Integer> cir) {

    }
}
