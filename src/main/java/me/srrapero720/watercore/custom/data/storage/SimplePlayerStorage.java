package me.srrapero720.watercore.custom.data.storage;

import me.srrapero720.watercore.custom.data.BackData;
import me.srrapero720.watercore.internal.WaterConfig;
import me.srrapero720.watercore.internal.WaterUtil;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class SimplePlayerStorage {
    private static final Map<String, BackData> PLAYER_BACKDATA = new HashMap<>();
    private static final Map<String, Long> PLAYER_BACKCOOLDOWN = new HashMap<>();


    public static long loadBackCooldown(ServerPlayer player) {
        return PLAYER_BACKCOOLDOWN.getOrDefault(player.getName().getString(), 0L);
    }
    public static boolean updateBackCooldown(ServerPlayer player) {
        if (System.nanoTime() >= PLAYER_BACKCOOLDOWN.getOrDefault(player.getName().getString(), 0L)) {
            PLAYER_BACKCOOLDOWN.put(player.getName().getString(), System.nanoTime() + WaterUtil.secondsToMilis(WaterConfig.get("BACK_COOLDOWN")));
            return true;
        }
        return false;
    }


    public static void saveBackData(@NotNull ServerPlayer player) {
        PLAYER_BACKDATA.put(player.getName().getString(), new BackData(player));
    }

    public static BackData loadBackData(ServerPlayer player) {
        return PLAYER_BACKDATA.get(player.getName().getString());
    }
}
