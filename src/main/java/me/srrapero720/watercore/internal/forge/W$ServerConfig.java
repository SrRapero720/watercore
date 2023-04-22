package me.srrapero720.watercore.internal.forge;

import me.srrapero720.watercore.internal.WUtil;
import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class W$ServerConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;
    private static final Map<String, ForgeConfigSpec.ConfigValue<?>> CONFIGS = new HashMap<>();


    static {
        //WATERCoRE ->
        BUILDER.push("watercore").comment(
                "Now Compatible with LuckPerms metadata (Prefix and suffix)",
                "Support (&f and §f) color formatting"
        );

        //WATERCoRE -> format
        BUILDER.push("format");

        CONFIGS.put("join_format", BUILDER.comment(
                "Modify message of joining players",
                "Placeholders: {displayname} {playername} {profilename}"
        ).define("join_format", "" + "&e&l[&b{displayname}&e&l] &aJoined"));

        CONFIGS.put("leave_format", BUILDER.comment(
                "Modify message of leaving players",
                "Placeholders: {displayname} {playername} {profilename}"
        ).define("leave_format", "&e&l[&b{displayname}&e&l] &cLeave"));

        CONFIGS.put("chat_format", BUILDER.comment(
                "Modify chat format",
                "Placeholders: {displayname} {playername} {profilename}",
                "NOTE: {displayname} is for Luckperms prefix + nickname + suffix"
        ).define("chat_format", "&e&l[&b{displayname}&e&l]&7"));

        CONFIGS.put("displayname_format", BUILDER.comment(
                "Modify how player's displayname works",
                "Only works with LUCKPERMS",
                "Placeholders: {prefix} {suffix} {playername}"
        ).define("displayname_format", "{prefix}{playername}{suffix}"));

        //WATERCoRE ->
        BUILDER.pop();



        //WATERCoRE -> commands
        BUILDER.push("commands").comment(
                "Support (&f and §f) color formatting"
        );

        //WATERCoRE -> commands -> back
        BUILDER.push("/back");
        CONFIGS.put("back_cooldown", BUILDER.comment("Cooldown usage of /back command in seconds")
                .define("cooldown", 20));
        CONFIGS.put("back_history_limit", BUILDER.comment("Max accessible location history for players")
                .define("max_history", 20));

        //WATERCoRE -> commands
        BUILDER.pop();

        //WATERCoRE -> commands -> broadcast
        BUILDER.push("/broadcast").comment("Alias 'broadcast-raw' is also affected by this configuration");
        CONFIGS.put("broadcast_prefix", BUILDER.comment("prefix from /broadcast output")
                .define("broadcast_prefix", WUtil.broadcastPrefix()));

        //WATERCoRE -> commands
        BUILDER.pop();

        //WATERCoRE ->
        BUILDER.pop();

        // ->
        BUILDER.pop();


        //RUNNING BASICS
        SPEC = BUILDER.build();
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(@NotNull String name) { return (T) CONFIGS.get(name).get(); }
    @SuppressWarnings("unchecked")
    public static <T> void set(String name, T value) { ((ForgeConfigSpec.ConfigValue<T>) CONFIGS.get(name)).set(value); }
}
