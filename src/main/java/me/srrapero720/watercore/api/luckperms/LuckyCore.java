package me.srrapero720.watercore.api.luckperms;

import jdk.jfr.Experimental;
import me.srrapero720.watercore.api.thread.ThreadUtil;
import me.srrapero720.watercore.internal.WConsole;
import me.srrapero720.watercore.internal.WRegistry;
import me.srrapero720.watercore.internal.WUtil;
import net.minecraft.world.entity.player.Player;

@Experimental
public class LuckyCore {
    static net.luckperms.api.LuckPerms LP;
    private static final String NAME = "LuckyCore";
    public static boolean isPresent() { return LP != null || !WUtil.isClientSide(); }
    public static net.luckperms.api.LuckPerms instance() { return LP; }

    public static void init() {
        ThreadUtil.threadTry(() -> {
            var clazz = Class.forName("net.luckperms.api.LuckPermsProvider");
            LP = (net.luckperms.api.LuckPerms) clazz.getMethod("get").invoke(null);

            WConsole.log(NAME, "Luckperms is present - Running permission registry");
            registerAllPermissions();
            WConsole.log(NAME, "All permissions registered");
        }, (e) -> WConsole.log(NAME, "Failed to load Luckperms. Is even installed? or we are in bukkit?"), WRegistry::cleanAllNodePerms);
    }

    private static void registerAllPermissions() {
    }



    public static String playerStringPermissionNode(Player player, String nodePerm) {
        var permissionData = LP.getUserManager().getUser(player.getGameProfile().getId()).getCachedData().getPermissionData();
        permissionData.checkPermission(nodePerm);
        return "";
    }

    // YOU NEVER NEED TO HAVE ACCESS TO THAT METHOD
    public static String playerStringMetadata_beta(Player player, String key) {
        return ThreadUtil.tryAndReturn((defaultVar) -> {
            var data = LP.getUserManager().getUser(player.getGameProfile().getId()).getCachedData().getMetaData();
            return data.getMetaValue(key);
        }, null);
    }

    // YOU NEVER NEED TO HAVE ACCESS TO THAT METHOD
    public static int playerIntMetadata_beta(Player player, String key, int def) {
        return ThreadUtil.tryAndReturn((defaultVar) -> {
            var data = LP.getUserManager().getUser(player.getGameProfile().getId()).getCachedData().getMetaData();
            return data.getMetaValue(key, Integer::parseInt).orElse(def);
        }, def);
    }

    /* PREFIX-SUFFIX BUILDER */
    public record PrefixSuffix(String prefix, String suffix) {
        public PrefixSuffix(String prefix, String suffix) {
            this.prefix = prefix == null ? "" : prefix;
            this.suffix = suffix == null ? "" : suffix;
        }
    }
}
