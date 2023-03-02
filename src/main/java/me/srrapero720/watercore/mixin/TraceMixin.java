package me.srrapero720.watercore.mixin;

import me.srrapero720.watercore.mixin_util.TraceMixinUtil;
import net.minecraft.CrashReport;
import me.srrapero720.watercore.SrConsole;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = CrashReport.class, priority = 0)
public abstract class TraceMixin {
	@Shadow
	private StackTraceElement[] uncategorizedStackTrace;

	@Inject(method = "getDetails(Ljava/lang/StringBuilder;)V", at = @At(value = "FIELD", target = "Lnet/minecraft/CrashReport;details:Ljava/util/List;"))
	private void addTrace(StringBuilder crashReportBuilder, CallbackInfo ci) {
		SrConsole.debug("MixinWC", "Creando CrashReport con mixins");

		int trailingNewlineCount = 0;
		// Remove trailing \n
		if (crashReportBuilder.charAt(crashReportBuilder.length() - 1) == '\n') {
			crashReportBuilder.deleteCharAt(crashReportBuilder.length() - 1);
			trailingNewlineCount++;
		}
		if (crashReportBuilder.charAt(crashReportBuilder.length() - 1) == '\n') {
			crashReportBuilder.deleteCharAt(crashReportBuilder.length() - 1);
			trailingNewlineCount++;
		}
		TraceMixinUtil.printTrace(uncategorizedStackTrace, crashReportBuilder);
		crashReportBuilder.append("\n".repeat(trailingNewlineCount));
	}
}
