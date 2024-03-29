package me.srrapero720.watercore.api.luckperms;

import jdk.jfr.Experimental;
import me.srrapero720.watercore.api.thread.ThreadUtil;
import me.srrapero720.watercore.internal.WLogger;
import me.srrapero720.watercore.internal.WCoreUtil;

@Experimental
public class LuckyCore {
    static net.luckperms.api.LuckPerms LP;
    private static final String NAME = "LuckyCore";
    public static boolean isPresent() { return LP != null || !WCoreUtil.isClientSide(); }
    public static net.luckperms.api.LuckPerms instance() { return LP; }

    public static void init() {
        ThreadUtil.threadTry(() -> {
            var clazz = Class.forName("net.luckperms.api.LuckPermsProvider");
            LP = (net.luckperms.api.LuckPerms) clazz.getMethod("get").invoke(null);

            WLogger.log("Luckperms is present");
        }, (e) -> WLogger.log("Failed to load Luckperms. Is even installed? or we are in bukkit?"), null);
    }


    /* PREFIX-SUFFIX BUILDER */
    public record PrefixSuffix(String prefix, String suffix) {
        public PrefixSuffix(String prefix, String suffix) {
            this.prefix = prefix == null ? "" : prefix;
            this.suffix = suffix == null ? "" : suffix;
        }
    }

    @SuppressWarnings("unused")
    public static class SafeModeException extends RuntimeException {

    }
}
