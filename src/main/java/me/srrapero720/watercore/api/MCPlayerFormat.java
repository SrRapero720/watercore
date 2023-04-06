package me.srrapero720.watercore.api;

import me.srrapero720.watercore.internal.WaterConfig;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class MCPlayerFormat {

    @Contract("_ -> new")
    public static @NotNull TextComponent createPlayerDisplayName(Player player) {
        String format = WaterConfig.get("PLAYER_FORMAT");
        if (LPMetadata.isLoadedApi()) {
            var prefixSuffix = LPMetadata.getPrefixSuffix(player.getGameProfile());
            return new TextComponent(format.replaceAll(Type.PREFIX, prefixSuffix[0])
                    .replaceAll(Type.PLAYER, player.getName().getString())
                    .replaceAll(Type.SUFFIX, prefixSuffix[1]));

        }
        return new TextComponent(player.getName().getString());
    }

    @Contract("_, _, _ -> new")
    public static @NotNull TextComponent parse(String format, Player player, String ...extras) {
        var displayname = createPlayerDisplayName(player).getString();
        var playername = player.getName().getString();
        var profilename = player.getGameProfile().getName();
        if (profilename == null) profilename = playername;

        StringBuilder result = new StringBuilder(format
                .replaceAll(Type.PLAYER, playername)
                .replaceAll(Type.PROFILENAME, profilename)
                .replaceAll(Type.DISPLAY, displayname));

        for (var extra: extras) result.append(" ").append(extra);
        return new TextComponent(MCTextFormat.parse(result.toString()));
    }

    @Contract("_, _, _ -> new")
    public static @NotNull TextComponent format(@NotNull Format format, Player player, String ...extras) {
        return parse(format.toString(), player, extras);
    }


    public enum Format {
        CHAT("CHAT_FORMAT"),
        JOIN("JOIN_FORMAT"),
        LEAVE("LEAVE_FORMAT");

        final String formatName;
        Format(String formatName) {
            this.formatName = formatName;
        }

        @Override
        public String toString() { return WaterConfig.get(formatName); }
    }


    public static class Type {
        public static final String PLAYER = "%playername%";
        public static final String DISPLAY = "%displayname%";
        public static final String PROFILENAME = "%profilename%";
        public static final String PREFIX = "%prefix%";
        public static final String SUFFIX = "%suffix%";
    }
}
