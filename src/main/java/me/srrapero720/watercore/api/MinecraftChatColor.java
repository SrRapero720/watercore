package me.srrapero720.watercore.api;

public class MinecraftChatColor {
    private MinecraftChatColor() {

    }

    public static MinecraftChatColor red() { return null; }

    public static String parse(String message) {
        return message.replaceAll("&", "§");
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
