package me.srrapero720.watercore.api.placeholder.provider;

import me.srrapero720.watercore.api.placeholder.Placeholder;
import me.srrapero720.watercore.api.placeholder.Placeholder.Format;
import org.jetbrains.annotations.NotNull;

import java.util.stream.IntStream;

public enum Color implements CharSequence {
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

    final String id, format;
    Color(String id, String format) {
        this.id = id;
        this.format = format;
        Placeholder.register(Format.AND, id, format);
    }


    @Override public int length() {return format.length();}
    @Override public char charAt(int index) { return format.charAt(index); }
    @Override public boolean isEmpty() {return format.isEmpty();}
    @NotNull @Override public CharSequence subSequence(int start, int end) { return format.subSequence(start, end); }
    @NotNull @Override public String toString() {return format;}
    @NotNull @Override public IntStream chars() {return format.chars();}
    @NotNull @Override public IntStream codePoints() {return format.codePoints();}

    public static void load() {}
}
