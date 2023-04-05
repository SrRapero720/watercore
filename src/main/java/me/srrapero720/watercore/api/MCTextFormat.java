package me.srrapero720.watercore.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Pattern;

public class MCTextFormat {
    private static final HashMap<String, String> STORED_FORMATS = new HashMap<>();

    public static String parse(String content) {
        var allMatches = new ArrayList<String>();
        var m = Pattern.compile("&\\w").matcher(content);
        while (m.find()) {
            if (!allMatches.contains(m.group())) allMatches.add(m.group());
        }

        for (var format: allMatches) content = content.replaceAll(format, Objects.requireNonNullElse(STORED_FORMATS.get(format), format));

        return content;
    }

    static {
        STORED_FORMATS.put("&0", "§0");
        STORED_FORMATS.put("&1", "§1");
        STORED_FORMATS.put("&2", "§2");
        STORED_FORMATS.put("&3", "§3");
        STORED_FORMATS.put("&4", "§4");
        STORED_FORMATS.put("&5", "§5");
        STORED_FORMATS.put("&6", "§6");
        STORED_FORMATS.put("&7", "§7");
        STORED_FORMATS.put("&8", "§8");
        STORED_FORMATS.put("&9", "§9");
        STORED_FORMATS.put("&a", "§a");
        STORED_FORMATS.put("&b", "§b");
        STORED_FORMATS.put("&c", "§c");
        STORED_FORMATS.put("&d", "§d");
        STORED_FORMATS.put("&e", "§e");
        STORED_FORMATS.put("&f", "§f");
        STORED_FORMATS.put("&k", "§k");
        STORED_FORMATS.put("&l", "§l");
        STORED_FORMATS.put("&m", "§m");
        STORED_FORMATS.put("&n", "§n");
        STORED_FORMATS.put("&o", "§o");
        STORED_FORMATS.put("&r", "§r");
    }
}
