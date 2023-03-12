package me.srrapero720.watercore.api;

import com.mojang.authlib.GameProfile;
import me.srrapero720.watercore.custom.config.WaterConfig;
import me.srrapero720.watercore.water.WaterConsole;
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

    public static TextComponent createChatMessage(ServerPlayer player, String msg) {
        if (LP != null) {
            var pf = getPrefixSuffix(player.getGameProfile());
            return new TextComponent(pf[0] + msg + pf[1]);
        } else {
            String format = WaterConfig.get("CHAT_FORMAT");
            return new TextComponent(format.replaceAll("%player%", player.getDisplayName().getString()) + msg);
        }
    }

    public static String[] getPrefixSuffix(GameProfile player) {
        var result = new String[] { "", "" };
        try {
            var data = LP.getUserManager().getUser(player.getId()).getCachedData().getMetaData();
            result[0] = data.getPrefix();
            result[1] = data.getSuffix();
            return result;
        } catch(Exception ise) {
            WaterConsole.error("LuckPermsUtil", ise.getMessage());
            ise.printStackTrace();
        }

        return result;
    }
}
