package me.srrapero720.watercore.api.placeholder.provider;

import me.srrapero720.watercore.api.luckperms.LPMetadata;
import me.srrapero720.watercore.api.placeholder.IPlaceholder;
import me.srrapero720.watercore.internal.WConfig;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class PlayerPlaceholder extends IPlaceholder {
    public PlayerPlaceholder(String id) { super(id); }

    @Contract("_ -> new")
    public static @NotNull TextComponent createPlayerDisplayName(Player player) {
        String format = WConfig.get("PLAYER_FORMAT");
        if (LPMetadata.isLoadedLuckperms()) {
            var ps = LPMetadata.playerPrefixSuffix(player.getGameProfile());
            return new TextComponent(format.replaceAll(Type.PREFIX, ps.prefix())
                    .replaceAll(Type.PLAYER, player.getName().getString())
                    .replaceAll(Type.SUFFIX, ps.suffix()));

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
        return new TextComponent(FormatPlaceholder.colors(result.toString()));
    }

    @Contract("_, _, _ -> new")
    public static @NotNull TextComponent format(@NotNull Format format, Player player, String ...extras) {
        return parse(format.toString(), player, extras);
    }

    @Override
    public String parse(String pattern, Object... any) {
        return null;
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
        public String toString() { return WConfig.get(formatName); }
    }


    public static class Type {
        public static final String PLAYER = "\\{playername}";
        public static final String DISPLAY = "\\{displayname}";
        public static final String PROFILENAME = "\\{profilename}";
        public static final String PREFIX = "\\{prefix}";
        public static final String SUFFIX = "\\{suffix}";
    }
}
