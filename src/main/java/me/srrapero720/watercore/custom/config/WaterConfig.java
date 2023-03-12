package me.srrapero720.watercore.custom.config;

import me.srrapero720.watercore.internal.WaterUtil;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.HashMap;
import java.util.Map;

public class WaterConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;
    private static final Map<String, ForgeConfigSpec.ConfigValue<?>> CONFIGS = new HashMap<>();


    static {
        //WATERCoRE -> Config
        BUILDER.push("WATERCoRE");
        CONFIGS.put("JOIN_MESSAGE", BUILDER.comment("Change the message for incoming players", "Supports (&) color formatting", "Placeholders: %player% %alias% %player-alias%")
                .define("JOIN_MESSAGE", "" + "&e%player% joined"));
        CONFIGS.put("LEAVE_MESSAGE", BUILDER.comment("Change the message for leaving players", "Supports (&) color formatting", "Placeholders: %player% %alias% %player-alias%", "NO WORKING YET")
                .define("LEAVE_MESSAGE", "&e%player% leave"));

        BUILDER.pop();

        //EssentialZ -> Config
        BUILDER.push("EssentialsZ");
        CONFIGS.put("BROADCAST_PREFIX", BUILDER.comment("How look the prefix of /broadcast output", "Supports color formatting using & and §")
                .define("BROADCAST_PREFIX", WaterUtil.getBroadcastPrefix()));
        CONFIGS.put("CHAT_FORMAT", BUILDER.comment(
                "Modify the chat prefix",
                "Supports (&) color formatting",
                "Placeholders: %player% %displayname% %rank%",
                "NOTE: %displayname% is reserved for Luckperms prefix + nickname"
        ).define("CHAT_FORMAT", "<%player%> "));
        BUILDER.pop();


        //Watercore Basics
        SPEC = BUILDER.build();
    }

    @SuppressWarnings("UncheckedCast")
    public static <T> T get(String name) {
        return (T) CONFIGS.get(name).get();
    }
}
