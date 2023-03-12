package me.srrapero720.watercore.api;

import com.mojang.authlib.GameProfile;
import me.srrapero720.watercore.custom.config.WaterConfig;
import me.srrapero720.watercore.internal.WaterConsole;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;

public class ChatDataProvider {
    private static net.luckperms.api.LuckPerms LP;

    public static void init() {
        try {
            WaterConsole.log(ChatDataProvider.class.toString(), "Using 'LuckPerms' prefix provider");
            LP = net.luckperms.api.LuckPermsProvider.get();
        } catch (Exception e) {
            WaterConsole.log(ChatDataProvider.class.toString(), "Using 'default' prefix provider (watercore config)");
        }
    }

    public static TextComponent parse(String format, ServerPlayer player, String ...extras) {
        var displayname = "";
        var playername = player.getDisplayName().getString();
        var alias = player.getGameProfile().getName();
        if (alias == null) alias = playername;

        if (LP != null) {
            var prefixSuffix = getPrefixSuffix(player.getGameProfile());
            displayname = prefixSuffix[0] + playername + prefixSuffix[1];
        } else displayname = playername;

        StringBuilder result = new StringBuilder(format
                .replaceAll(Type.PLAYER, playername)
                .replaceAll(Type.ALIAS, alias)
                .replaceAll(Type.DISPLAY, displayname));

        for (var extra: extras) result.append(" ").append(extra);

        return new TextComponent(result.toString());
    }

    public static TextComponent message(ServerPlayer player, String msg) {
        String format = WaterConfig.get("CHAT_FORMAT");
        var displayname = "";
        var playername = player.getDisplayName().getString();
        var alias = player.getGameProfile().getName();
        if (LP != null) {
            var pf = getPrefixSuffix(player.getGameProfile());
            displayname = pf[0] + playername + pf[1];
        } else displayname = playername;

        return new TextComponent(format
                .replaceAll(Type.PLAYER, playername)
                .replaceAll(Type.ALIAS, alias == null ? playername : alias)
                .replaceAll(Type.DISPLAY, displayname)+ " " + msg);
    }

    public static TextComponent connection(String format, ServerPlayer player) {
        var displayname = "";
        var playername = player.getDisplayName().getString();
        var alias = player.getGameProfile().getName();
        if (LP != null) {
            var ps = getPrefixSuffix(player.getGameProfile());
            displayname = ps[0] + playername + ps[1];
        } else { displayname = playername; }

        return new TextComponent(format
                .replaceAll(Type.PLAYER, playername)
                .replaceAll(Type.ALIAS, alias == null ? playername : alias)
                .replaceAll(Type.DISPLAY, displayname));
    }

    public static String[] getPrefixSuffix(GameProfile player) {
        var result = new String[] { "", "" };
        try {
            var data = LP.getUserManager().getUser(player.getId()).getCachedData().getMetaData();
            result[0] = data.getPrefix();
            result[1] = data.getSuffix();
            return result;
        } catch(Exception ise) {
            WaterConsole.error(ChatDataProvider.class.getName(), ise.getMessage());
            ise.printStackTrace();
        }

        return result;
    }

    public static class Type {
        public static final String PLAYER = "%playername%";
        public static final String DISPLAY = "%displayname%";
        public static final String ALIAS = "%alias%";
        public static final String NICK = "%nick%";
        public static final String VANISH = "%vanish%";

        public static String parse() {
            return "";
        }
    }
}
