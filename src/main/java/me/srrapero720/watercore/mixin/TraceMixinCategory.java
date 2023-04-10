package me.srrapero720.watercore.mixin;

import me.srrapero720.wrappycrashes.SMAPper;
import me.srrapero720.watercore.internal.WaterConsole;
import me.srrapero720.watercore.internal.WaterUtil;
import net.minecraft.CrashReportCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = CrashReportCategory.class, priority = 72)
public class TraceMixinCategory {
	@Shadow private StackTraceElement[] stackTrace;

	@Inject(method = "getDetails", at = @At("TAIL"))
	private void injectGetDetailsToAddTrace(StringBuilder crashReportBuilder, CallbackInfo ci) {
		WaterConsole.debug("MixinWC", "Agregando trace");
		WaterUtil.printTrace(stackTrace, crashReportBuilder);
	}

	@Inject(method = "fillInStackTrace", at = @At(value = "INVOKE", target = "Ljava/lang/System;arraycopy(Ljava/lang/Object;ILjava/lang/Object;II)V", shift = At.Shift.AFTER))
	private void injectFillInStackTraceToSMAPCause(int p_128149_, CallbackInfoReturnable<Integer> cir) {
		WaterConsole.warn("trimStackTrace", "Running ahead");
		SMAPper.apply(stackTrace, "java.", "sun.", "net.minecraftforge.fml.", "com.mojang.authlib.");
	}
}
