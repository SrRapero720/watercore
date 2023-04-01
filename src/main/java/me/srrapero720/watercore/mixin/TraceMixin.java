package me.srrapero720.watercore.mixin;

import me.srrapero720.craftycrashes.SMAPper;
import me.srrapero720.watercore.internal.WaterUtil;
import me.srrapero720.watercore.internal.WaterConsole;
import net.minecraft.CrashReport;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = CrashReport.class, priority = 0)
public abstract class TraceMixin {
	@Shadow private StackTraceElement[] uncategorizedStackTrace;
	@Shadow @Final private Throwable exception;

	@Inject(method = "<init>", at = @At(value = "TAIL"))
	private void fixCause2(CallbackInfo call) {
		SMAPper.apply(exception, "java.", "sun.", "net.minecraftforge.fml.", "com.mojang.authlib.");
	}

	@Inject(method = "getDetails(Ljava/lang/StringBuilder;)V", at = @At(value = "FIELD", target = "Lnet/minecraft/CrashReport;details:Ljava/util/List;"))
	private void addTrace(StringBuilder crashReportBuilder, CallbackInfo ci) {
		WaterConsole.debug("MixinWC", "Creando CrashReport con mixins");

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
		WaterUtil.printTrace(uncategorizedStackTrace, crashReportBuilder);
		crashReportBuilder.append("\n".repeat(trailingNewlineCount));
	}
}
