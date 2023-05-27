package me.srrapero720.watercore;

import me.srrapero720.watercore.utility.Tools;
import net.minecraftforge.common.ForgeConfigSpec;

public class WaterConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    /*=====================================================
                 WATERCORE API FORMAT (read-only)
    *======================================================*/
    private static final ForgeConfigSpec.ConfigValue<String> JOIN_FORMAT;
    private static final ForgeConfigSpec.ConfigValue<String> LEAVE_FORMAT;
    private static final ForgeConfigSpec.ConfigValue<String> CHAT_FORMAT;
    private static final ForgeConfigSpec.ConfigValue<String> DISPLAYNAME_FORMAT;
    private static final ForgeConfigSpec.ConfigValue<String> DISPLAYNAME_NICK_FORMAT;

    // GETTERS
    public static String getJoinFormat() { return JOIN_FORMAT.get(); }
    public static String getLeaveFormat() { return LEAVE_FORMAT.get(); }
    public static String getChatFormat() { return CHAT_FORMAT.get(); }

    // SETTERS
    public static void setJoinFormat(String value) { JOIN_FORMAT.set(value); }
    public static void setLeaveFormat(String value) { LEAVE_FORMAT.set(value); }
    public static void setChatFormat(String value) { CHAT_FORMAT.set(value); }

    public static String displaynameFormat() { return DISPLAYNAME_FORMAT.get(); }
    public static String displaynameNickFormat() { return DISPLAYNAME_NICK_FORMAT.get(); }

    /*=====================================================
                     BACK COMMAND (read-only)
    *======================================================*/
    private static final ForgeConfigSpec.IntValue BACK_COOLDOWN;
    private static final ForgeConfigSpec.IntValue BACK_HISTORY_LIMIT;

    // GETTERS
    public static int backCooldown() { return BACK_COOLDOWN.get(); }
    public static int backHistoryLimit() { return BACK_HISTORY_LIMIT.get(); }

    /*=====================================================
                        BROADCAST COMMAND
    *======================================================*/
    private static final ForgeConfigSpec.ConfigValue<String> BROADCAST_PREFIX;

    // GETTERS
    public static String getBroadcastPrefix() { return BROADCAST_PREFIX.get(); }

    // SETTERS
    public static void getBroadcastPrefix(String value) { BROADCAST_PREFIX.set(value); }

    static {
        //WATERCoRE ->
        BUILDER.push("watercore").comment(
                "Now Compatible with LuckPerms metadata (Prefix and suffix)",
                "Support (&f and §f) color formatting"
        );

        //WATERCoRE -> format
        BUILDER.push("format");

        JOIN_FORMAT = BUILDER.comment(
                "Modify message of joining players",
                "Placeholders: {displayname} {playername} {profilename}"
        ).define("join_format", "" + "&e&l[&b{displayname}&e&l] &aJoined");

        LEAVE_FORMAT = BUILDER.comment(
                "Modify message of leaving players",
                "Placeholders: {displayname} {playername} {profilename}"
        ).define("leave_format", "&e&l[&b&c-&l] &c{displayname}");

        CHAT_FORMAT = BUILDER.comment(
                "Modify chat format",
                "Placeholders: {displayname} {playername} {profilename}",
                "NOTE: {displayname} is for Luckperms prefix + nickname + suffix"
        ).define("chat_format", "&e&l[&b{displayname}&e&l]&7");

        DISPLAYNAME_FORMAT = BUILDER.comment(
                "Modify how player's displayname works",
                "Placeholders: {prefix} {suffix} {playername}",
                "NOTE: Prefix and suffix only works if luckperms is installed"
        ).define("displayname_format", "{prefix}{playername}{suffix}");

        DISPLAYNAME_NICK_FORMAT = BUILDER.comment(
                "Modify how player's displayname works when players are using /nick <name>",
                "Placeholders: {prefix} {suffix} {playername}",
                "NOTE: Prefix and suffix only works if luckperms is installed"
        ).define("displayname_format", "{prefix}{playername}{suffix}");

        //WATERCoRE ->
        BUILDER.pop();


        //WATERCoRE -> commands
        BUILDER.push("commands").comment(
                "Support (&f and §f) color formatting"
        );

        //WATERCoRE -> commands -> back
        BUILDER.push("/back");
        BACK_COOLDOWN = BUILDER.comment(
                "Cooldown usage of /back command in seconds",
                "Can be overwrited with luckperms using node 'watercore.command.back.cooldown'"
        ).defineInRange("cooldown", 20, 0, Integer.MAX_VALUE);
        BACK_HISTORY_LIMIT = BUILDER.comment(
                "Max accessible location history for players",
                "This cannot be overwrited by luckperms"
        ).defineInRange("cooldown", 20, 0, Integer.MAX_VALUE);

        //WATERCoRE -> commands
        BUILDER.pop();

        //WATERCoRE -> commands -> broadcast
        BUILDER.push("/broadcast").comment("Alias 'broadcast-raw' is also affected by this configuration");
        BROADCAST_PREFIX = BUILDER.comment("prefix from /broadcast output").define("broadcast_prefix", Tools.broadcastPrefix());

        //WATERCoRE -> commands
        BUILDER.pop();

        //WATERCoRE ->
        BUILDER.pop();

        // ->
        BUILDER.pop();


        //RUNNING BASICS
        SPEC = BUILDER.build();
    }
}
