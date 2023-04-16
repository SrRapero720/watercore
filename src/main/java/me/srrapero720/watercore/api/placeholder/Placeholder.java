package me.srrapero720.watercore.api.placeholder;

import me.srrapero720.watercore.api.placeholder.provider.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


public class Placeholder {
    public static final Map<String, CharSequence> HOLDERS = new HashMap<>();
    static {
        Color.load();
    }


    /**
     * This method parses any pattern with the next syntax: "&c" or "{id}"
     * You can register your own placeholders using:
     * <pre>PlaceholderManager.regisger(Format format, String id, String value)</pre>
    */
    public static String parse(String content) {
        var matches = new ArrayList<String>();
        var pattern = Pattern.compile("(&\\w)|(\\{[a-zA-Z_][a-zA-Z0-9_.]*})").matcher(content);

        while (pattern.find()) if (!matches.contains(pattern.group())) matches.add(pattern.group());

        for (final var format: matches) content = content.replaceAll(format, get(format, format).toString());
        matches.clear();
        return content;
    }

    public static void register(Format format, String id, String value) {
        HOLDERS.put(format.parseID(id), value);
    }

    public static CharSequence get(String id, String defaultVar) {
        return HOLDERS.getOrDefault(id, defaultVar);
    }

    public static CharSequence get(String id) {
        return HOLDERS.getOrDefault(id, id);
    }

    public enum Format {
        AND("&"),
        KEY("\\{", "}");

        final String first;
        final String end;
        Format(String firstKey, String end) {
            this.first = firstKey;
            this.end = end;
        }

        Format(String firstKey) {
            this.first = firstKey;
            this.end = "";
        }

        public String parseID(String id) {
            return first.concat(id).concat(end);
        }
    }
}
