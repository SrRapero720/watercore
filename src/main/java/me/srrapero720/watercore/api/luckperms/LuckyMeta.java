package me.srrapero720.watercore.api.luckperms;

import com.mojang.authlib.GameProfile;
import me.srrapero720.watercore.api.thread.ThreadUtil;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public class LuckyMeta {
    //========================================== //
    //           METADATA FROM NODES
    //========================================== //
    public static String getMetaNodeValue(Player profile, String key, String def) { return getMetaNodeValue(profile.getGameProfile(), key, def); }
    public static String getMetaNodeValue(GameProfile profile, String key, String def) {
        return ThreadUtil.tryAndReturn(defaultVar -> {
            if (!LuckyCore.isPresent()) return defaultVar;

            var LP = LuckyCore.instance();
            var user = LP.getUserManager().getUser(profile.getId());

            return user.getCachedData().getMetaData().getMetaValue(key);
        }, def);
    }

    public static String getMetaNodeValue(Player profile, String key) { return getMetaNodeValue(profile.getGameProfile(), key, null); }
    public static String getMetaNodeValue(GameProfile profile, String key) { return getMetaNodeValue(profile, key, null); }


    //========================================== //
    //        METADATA FROM NODES BUT INT
    //========================================== //
    public static int getIntMetaNodeValue(Player profile, String key, int def) { return getIntMetaNodeValue(profile.getGameProfile(), key, def); }
    public static int getIntMetaNodeValue(GameProfile profile, String key, int def) {
        return ThreadUtil.tryAndReturn(defaultVar -> {
            if (!LuckyCore.isPresent()) return defaultVar;

            var LP = LuckyCore.instance();
            var user = LP.getUserManager().getUser(profile.getId());

            return user.getCachedData().getMetaData().getMetaValue(key, Integer::parseInt).orElse(defaultVar);
        }, def);
    }

    public static int getIntMetaNodeValue(Player profile, String key) { return getIntMetaNodeValue(profile.getGameProfile(), key, -1); }
    public static int getIntMetaNodeValue(GameProfile profile, String key) { return getIntMetaNodeValue(profile, key, -1); }


    //========================================== //
    //             CONTEXT DATA
    //========================================== //
    public static String getContextData(Player profile, String node, String key, String def) { return getContextData(profile.getGameProfile(), node, key, def); }
    public static String getContextData(GameProfile profile, String node, String key, String def) {
        return ThreadUtil.tryAndReturn((defaultVar) -> {
            if (!LuckyCore.isPresent()) return defaultVar;

            var LP = LuckyCore.instance();
            var user = LP.getUserManager().getUser(profile.getId());
            var perm = user.getCachedData()
                    .getPermissionData((net.luckperms.api.query.QueryOptions) Class.forName("net.luckperms.api.query.QueryOptions").getMethod("nonContextual").invoke(null))
                    .queryPermission(node);

            return perm.node().getContexts().getValues(key).toArray(new String[]{})[0];
        }, def);
    }

    public static int getIntContextData(GameProfile profile, String node, String key, int def) {
        return ThreadUtil.tryAndReturn((defaultVar) -> Integer.parseInt(getContextData(profile, node, key, String.valueOf(def))), def);
    }


    //========================================== //
    //             PREFIX AND SUFFIX
    //========================================== //
    public static LuckyCore.PrefixSuffix prefixSuffix(GameProfile player) {
        return ThreadUtil.tryAndReturn((defaultVar) -> {
            if (!LuckyCore.isPresent()) return defaultVar;
            var data = LuckyCore.instance().getUserManager().getUser(player.getId()).getCachedData().getMetaData();
            return new LuckyCore.PrefixSuffix(data.getPrefix(), data.getSuffix());
        }, new LuckyCore.PrefixSuffix("", ""));
    }

    /* PREFIX STANDALONE */
    public static String prefix(@NotNull Player player) { return prefix(player.getGameProfile()); }
    public static String prefix(GameProfile player) {
        return ThreadUtil.tryAndReturn((defaultVar) -> {
            if (!LuckyCore.isPresent()) return defaultVar;
            var data = LuckyCore.instance().getUserManager().getUser(player.getId()).getCachedData().getMetaData();
            var prefix = data.getPrefix();
            return prefix == null ? defaultVar : prefix;
        }, "");
    }

    /* SUFFIX STANDALONE */
    public static String suffix(@NotNull Player player) { return suffix(player.getGameProfile()); }
    public static String suffix(GameProfile player) {
        return ThreadUtil.tryAndReturn((defaultVar) -> {
            if (!LuckyCore.isPresent()) return defaultVar;
            var data = LuckyCore.instance().getUserManager().getUser(player.getId()).getCachedData().getMetaData();
            var suffix = data.getSuffix();
            return suffix == null ? defaultVar : suffix;
        }, "");
    }
}
