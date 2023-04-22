package me.srrapero720.watercore.api.ego;

import me.srrapero720.watercore.api.luckperms.LuckyCore;
import me.srrapero720.watercore.api.luckperms.LuckyMeta;
import me.srrapero720.watercore.api.luckperms.LuckyNode;
import me.srrapero720.watercore.api.placeholder.Placeholder;
import me.srrapero720.watercore.api.thread.ThreadUtil;
import me.srrapero720.watercore.internal.forge.W$ServerConfig;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class PlayerName {
    public static final Map<String, Holder> HOLDERS = new HashMap<>();

    static {
        HOLDERS.put("playername", new Holder("playername", player -> player.getName().getString()));
        HOLDERS.put("displayname", new Holder("displayname", player -> displayname(player).getString()));
        HOLDERS.put("profilename", new Holder("profilename", player -> player.getGameProfile().getName()));
        HOLDERS.put("prefix", new Holder("prefix", LuckyMeta::prefix));
        HOLDERS.put("suffix", new Holder("suffix", LuckyMeta::suffix));

        // REGISTER SOME THINGS
        LuckyNode.registerMetaNode("watercore.api.displayname", "{prefix}{playername}{suffix}");
    }

    @Contract("_ -> new")
    public static @NotNull TextComponent displayname(Player player) {
        return ThreadUtil.tryAndReturn(defaultVar -> {
            if (!LuckyCore.isPresent()) return new TextComponent(player.getName().getString());
            var nodeValue = LuckyMeta.getMetaNodeValue(player, "watercore.api.displayname");

            String format = nodeValue != null ? nodeValue : W$ServerConfig.get("displayname_format");
            if (format == null) return defaultVar;

            for (var holder : HOLDERS.values())
                if (!holder.id().contains("display")) format = format.replaceAll(holder.id(), holder.parse(player));
            return new TextComponent(format);
        }, new TextComponent(player.getName().getString()));
    }

    @Contract("_, _, _ -> new")
    public static @NotNull TextComponent parse(String format, Player player, String... extras) {
        if (!LuckyCore.isPresent()) return new TextComponent(player.getName().getString());

        var mFormat = format;
        for (var holder : HOLDERS.values()) mFormat = mFormat.replaceAll(holder.id(), holder.parse(player));

        var result = new StringBuilder(mFormat);
        for (var extra : extras) result.append(" ").append(extra);
        return new TextComponent(Placeholder.parse(result.toString()));
    }

    @Contract("_, _, _ -> new")
    public static @NotNull TextComponent format(@NotNull Format format, Player player, String... extras) {
        return parse(format.toString(), player, extras);
    }

    public record Holder(String id, PlayerSupplier supplier) {
        public Holder(String id, PlayerSupplier supplier) {
            this.id = Placeholder.Format.KEY.parseID(id);
            this.supplier = supplier;
        }

        @Override
        public String id() { return id; }
        public String parse(Player player) { return supplier.get(player); }
    }

    public interface PlayerSupplier { String get(Player player); }

    public enum Format {
        CHAT("chat_format"),
        JOIN("join_format"),
        LEAVE("leave_format");

        final String formatName;
        Format(String formatName) { this.formatName = formatName; }

        @Override
        public String toString() { return W$ServerConfig.get(formatName); }
    }
}
