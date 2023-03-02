package me.srrapero720.watercore.mixin;

import net.minecraft.CrashReportCategory;
import me.srrapero720.watercore.SrConsole;
import me.srrapero720.watercore.mixin_util.TraceMixinUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = CrashReportCategory.class, priority = 0)
public abstract class TraceMixinCategory {
	@Shadow private StackTraceElement[] stackTrace;

	@Inject(method = "getDetails", at = @At("TAIL"))
	private void addTrace(StringBuilder crashReportBuilder, CallbackInfo ci) {
		SrConsole.debug("MixinWC", "Agregando trace");
		TraceMixinUtil.printTrace(stackTrace, crashReportBuilder);
	}
}
