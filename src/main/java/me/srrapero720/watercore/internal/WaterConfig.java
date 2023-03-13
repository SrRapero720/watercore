package me.srrapero720.watercore.internal;

import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class WaterConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;
    private static final Map<String, ForgeConfigSpec.ConfigValue<?>> CONFIGS = new HashMap<>();


    static {
        //WATERCoRE ->
        BUILDER.push("WATERCoRE").comment(
                "Now Compatible with LuckPerms",
                "Support (&f and §f) color formatting"
        );

        //WATERCoRE -> SERVER_CONFIG
        BUILDER.push("SERVER_CONFIG");

        CONFIGS.put("JOIN_FORMAT", BUILDER.comment(
                "Modify message of joining players",
                "Placeholders: %displayname% %playername% %alias%").define("JOIN_FORMAT", "" + "&e&l[&b%displayname%&e&l] &aJoined"));

        CONFIGS.put("LEAVE_FORMAT", BUILDER.comment(
                "Modify message of leaving players",
                "Placeholders: %displayname% %playername% %alias%").define("LEAVE_FORMAT", "&e&l[&b%displayname%&e&l] &cLeave"));

        var player_format = BUILDER.comment(
                "Modify chat format",
                "Placeholders: %displayname% %playername% %alias%",
                "NOTE: %displayname% is reserved for Luckperms prefix + nickname"
        ).define("CHAT_FORMAT", "&e&l[&b%displayname%&e&l]&9");

        CONFIGS.put("PLAYER_FORMAT", player_format);
        CONFIGS.put("CHAT_FORMAT", player_format);

        //WATERCoRE ->
        BUILDER.pop();



        //WATERCoRE -> COMMANDS
        BUILDER.push("COMMANDS").comment(
                "Now Compatible with LuckPerms",
                "Support (&f and §f) color formatting"
        );

        CONFIGS.put("BROADCAST_PREFIX",
                BUILDER.comment("prefix from /broadcast output").define("BROADCAST_PREFIX", WaterUtil.getBroadcastPrefix()));



        //WATERCoRE ->
        BUILDER.pop();

        // ->
        BUILDER.pop();


        //RUNNING BASICS
        SPEC = BUILDER.build();
    }

    @SuppressWarnings("UncheckedCast")
    public static <T> T get(@NotNull String name) {
        if (name.equals("CHAT_FORMAT")) WaterConsole.warn("WaterConfig", "Using deprecated config CHAT_FORMAT");
        return (T) CONFIGS.get(name).get();
    }
}
