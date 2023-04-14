package me.srrapero720.watercore.api.placeholder.provider;

import me.srrapero720.watercore.api.placeholder.IPlaceholder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class FormatPlaceholder extends IPlaceholder {
    private static final Map<String, String> COLORS = new HashMap<>();

    public FormatPlaceholder(String id) { super(id); }


    /**
     * Parse acceptable color placeholders
     * @param content - String with color formats. Example:  %.c% or &c returns §c (RED)
     * @apiNote This could be replaced when PlaceholderProtocol mod is ready
    */
    public static String colors(String content) {
        var matches = new ArrayList<String>();
        var pattern = Pattern.compile("(&\\w)|(%\\.\\w%)").matcher(content);

        while (pattern.find()) if (!matches.contains(pattern.group())) matches.add(pattern.group());

        for (var format: matches) content = content.replaceAll(format, Color.find(format).toString());
        return content;
    }

    @Override
    public String parse(String pattern, Object... any) { return colors(pattern); }

    public enum Color {
        BLACK("0", "§0"),
        DARK_BLUE("1", "§1"),
        DARK_GREEN("2", "§2"),
        DARK_AQUA("3", "§3"),
        DARK_RED("4", "§4"),
        DARK_PURPLE("5", "§5"),
        GOLD("6", "§6"),
        GRAY("7", "§7"),
        DARK_GRAY("8", "§8"),
        BLUE("9", "§9"),
        GREEN("a", "§a"),
        AQUA("b", "§b"),
        RED("c", "§c"),
        LIGHT_PURPLE("d", "§d"),
        YELLOW("e", "§e"),
        WHITE("f", "§f"),
        OBFUSCATED("k", "§k"),
        BOLD("l", "§l"),
        STRIKETHROUGH("m", "§m"),
        UNDERLINE("n", "§n"),
        ITALIC("o", "§o"),
        RESET("r", "§r");


        final String id;
        final String format;
        Color(String id, String format) {
            this.id = id;
            this.format = format;
            COLORS.put(id, format);
        }

        static String find(String string) {
            return COLORS.getOrDefault(string
                    .replace("%", "")
                    .replace("&", "")
                    .replace(".", ""), RESET.toString());
        }

        @Override
        public @NotNull String toString() { return format; }
    }
}
