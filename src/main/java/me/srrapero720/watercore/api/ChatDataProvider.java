package me.srrapero720.watercore.api;

import com.mojang.authlib.GameProfile;
import me.srrapero720.watercore.internal.WaterConfig;
import me.srrapero720.watercore.internal.WaterConsole;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public class ChatDataProvider {
    private static net.luckperms.api.LuckPerms LP;

    public static void init() {
        try {
//            var lucko = IntrusiveClassLoader.findAll("net.lucko");
//            if (!lucko.isEmpty()) for (var czz: lucko.toArray()) {
//                WaterConsole.justPrint(czz.getClass().getSimpleName());
//            }

            var clazz = Class.forName("net.luckperms.api.LuckPermsProvider");
            LP = net.luckperms.api.LuckPermsProvider.get();

            WaterConsole.log(ChatDataProvider.class.toString(), "Using 'LuckPerms' as Prefix provider");
        } catch (Exception e) {
            WaterConsole.log(ChatDataProvider.class.toString(), "Luckperms no found");
        }
    }

    public static TextComponent createPlayerDisplayName(Player player) {
        String format = WaterConfig.get("PLAYER_FORMAT");
        if (LP != null) {
            var prefixSuffix = getPrefixSuffix(player.getGameProfile());
            return new TextComponent(format.replaceAll(Type.PREFIX, prefixSuffix[0])
                    .replaceAll(Type.PLAYER, ((IPlayerEntity) player).getPlayername().getString())
                    .replaceAll(Type.SUFFIX, prefixSuffix[1]));

        }
        return new TextComponent(((IPlayerEntity) player).getPlayername().getString());
    }

    public static TextComponent parse(String format, Player player, String ...extras) {
        var displayname = createPlayerDisplayName(player).getString();
        var playername = ((IPlayerEntity) player).getPlayername().getString();
        var profilename = player.getGameProfile().getName();
        if (profilename == null) profilename = playername;

        StringBuilder result = new StringBuilder(format
                .replaceAll(Type.PLAYER, playername)
                .replaceAll(Type.PROFILENAME, profilename)
                .replaceAll(Type.DISPLAY, displayname));

        for (var extra: extras) result.append(" ").append(extra);
        return new TextComponent(MinecraftChatColor.parse(result.toString()));
    }

    public static String @NotNull [] getPrefixSuffix(GameProfile player) {
        var result = new String[] { "", "" };
        try {
            var data = LP.getUserManager().getUser(player.getId()).getCachedData().getMetaData();
            if (data.getPrefix() != null) result[0] = data.getPrefix();
            if (data.getSuffix() != null) result[1] = data.getSuffix();
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
        public static final String PROFILENAME = "%profilename%";
        public static final String PREFIX = "%prefix%";
        public static final String SUFFIX = "%suffix%";
        public static final String NICK = "%nick%";
        public static final String VANISH = "%vanish%";

        public static String parse() {
            return "";
        }
    }
}
