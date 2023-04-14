package me.srrapero720.watercore.api.luckperms;

import com.mojang.authlib.GameProfile;
import me.srrapero720.watercore.api.thread.ThreadUtil;
import me.srrapero720.watercore.internal.WConsole;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public class LPMetadata {
    private static net.luckperms.api.LuckPerms LP;
    private static final String NAME = "LPMetadata";
    public static boolean isLoadedLuckperms() { return LP != null; }

    public static void init() {
        ThreadUtil.trySimple(() -> {
            var clazz = Class.forName("net.luckperms.api.LuckPermsProvider");
            LP = (net.luckperms.api.LuckPerms) clazz.getMethod("get").invoke(null);
        });

        if (isLoadedLuckperms()) WConsole.log(NAME, "Luckperms detected... using metadata info");
        else WConsole.log(NAME, "Luckperms undetected, safe mode enabled");
    }

    public static PrefixSuffix playerPrefixSuffix(GameProfile player) {
        return ThreadUtil.tryAndReturn((defaultVar) -> {
            var data = LP.getUserManager().getUser(player.getId()).getCachedData().getMetaData();
            return new PrefixSuffix(data.getPrefix(), data.getSuffix());
        }, new PrefixSuffix("", ""));
    }


    public static int playerIntMetadata(GameProfile player, String nodePerm, int def) {
        var metadata = playerStringMetadata(player, nodePerm);
        return Math.max(ThreadUtil.tryAndReturn((defaultVar) -> Integer.parseInt(metadata), def), 0);
    }


    // YOU NEVER NEED TO HAVE ACCESS TO THAT METHOD
    private static String playerStringMetadata(GameProfile player, String nodePerm) {
        return ThreadUtil.tryAndReturn((defaultVar) -> {
            var user = LP.getUserManager().getUser(player.getId());
            var context = LP.getContextManager().getQueryOptions(user);
            return context.map(queryOptions -> user.getCachedData().getMetaData(queryOptions).getMetaValue(nodePerm)).orElse(null);
        }, null);
    }

    /* NODE BUILDER */
    public static final class NodeBuilder {

    }

    /* PREFIX-SUFFIX BUILDER */
    public record PrefixSuffix(String prefix, String suffix) {
        public PrefixSuffix(String prefix, String suffix) {
            this.prefix = prefix == null ? "" : prefix;
            this.suffix = suffix == null ? "" : suffix;
        }
    }
}
