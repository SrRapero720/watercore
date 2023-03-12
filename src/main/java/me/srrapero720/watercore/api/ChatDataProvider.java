package me.srrapero720.watercore.api;

import com.mojang.authlib.GameProfile;
import me.srrapero720.watercore.internal.WaterConsole;
import me.srrapero720.watercore.internal.WaterUtil;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

public class ChatDataProvider {
    private static net.luckperms.api.LuckPerms LP;

    public static void init() {
        try {
            WaterConsole.log(ChatDataProvider.class.toString(), "Using 'LuckPerms' as Prefix provider");
            LP = net.luckperms.api.LuckPermsProvider.get();
        } catch (Exception e) {
            WaterConsole.log(ChatDataProvider.class.toString(), "Luckperms no found");
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

        return new TextComponent(MinecraftChatColor.parse(result.toString()));
    }

    public static String @NotNull [] getPrefixSuffix(GameProfile player) {
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
