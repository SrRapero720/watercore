package me.srrapero720.watercore.api.luckperms;

import com.mojang.authlib.GameProfile;
import me.srrapero720.watercore.api.thread.ThreadUtil;
import me.srrapero720.watercore.internal.WConsole;
import net.minecraft.world.entity.player.Player;
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

    public static String playerPrefix(Player player) { return playerPrefix(player.getGameProfile()); }
    public static String playerPrefix(GameProfile player) {
        return ThreadUtil.tryAndReturn((defaultVar) -> {
            var data = LP.getUserManager().getUser(player.getId()).getCachedData().getMetaData();
            var prefix = data.getPrefix();
            return prefix == null ? defaultVar : prefix;
        }, "");
    }

    public static String playerSuffix(Player player) { return playerSuffix(player.getGameProfile()); }
    public static String playerSuffix(GameProfile player) {
        return ThreadUtil.tryAndReturn((defaultVar) -> {
            var data = LP.getUserManager().getUser(player.getId()).getCachedData().getMetaData();
            var suffix = data.getSuffix();
            return suffix == null ? defaultVar : suffix;
        }, "");
    }


//    public static int playerIntMetadata(GameProfile player, String nodePerm, int def) {
//        var metadata = playerStringMetadata(player, nodePerm);
//        return Math.max(ThreadUtil.tryAndReturn((defaultVar) -> Integer.parseInt(metadata), def), 0);
//    }
//
//
//    // YOU NEVER NEED TO HAVE ACCESS TO THAT METHOD
//    private static String playerStringMetadata(GameProfile player, String nodePerm) {
//        return ThreadUtil.tryAndReturn((defaultVar) -> {
//            var user = LP.getUserManager().getUser(player.getId());
//            var context = LP.getContextManager().getQueryOptions(user);
//            return context.map(queryOptions -> user.getCachedData().getMetaData(queryOptions).getMetaValue(nodePerm)).orElse(null);
//        }, null);
//    }

    // YOU NEVER NEED TO HAVE ACCESS TO THAT METHOD
    private static String playerStringMetadata_beta(Player player, String nodePerm) {
        return ThreadUtil.tryAndReturn((defaultVar) -> {
            var data = LP.getUserManager().getUser(player.getGameProfile().getId()).getCachedData().getMetaData();
            return data.getMetaValue(nodePerm);
        }, null);
    }

    // YOU NEVER NEED TO HAVE ACCESS TO THAT METHOD
    private static int playerIntMetadata_beta(Player player, String nodePerm, int def) {
        return ThreadUtil.tryAndReturn((defaultVar) -> {
            var data = LP.getUserManager().getUser(player.getGameProfile().getId()).getCachedData().getMetaData();
            return data.getMetaValue(nodePerm, Integer::parseInt).orElse(def);
        }, def);
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
