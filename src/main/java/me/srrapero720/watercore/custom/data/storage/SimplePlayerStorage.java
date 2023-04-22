package me.srrapero720.watercore.custom.data.storage;

import me.srrapero720.watercore.api.luckperms.LuckyMeta;
import me.srrapero720.watercore.custom.data.BackData;
import me.srrapero720.watercore.internal.forge.W$SConfig;
import me.srrapero720.watercore.internal.WUtil;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimplePlayerStorage {
    private static final Map<String, List<BackData>> PLAYER_BACKDATA = new HashMap<>();
    private static final Map<String, Long> PLAYER_BACKCOOLDOWN = new HashMap<>();

    public static long loadBackCooldown(ServerPlayer player) {
        return PLAYER_BACKCOOLDOWN.getOrDefault(player.getName().getString(), 0L);
    }
    public static boolean updateBackCooldown(ServerPlayer player) {
        if (System.nanoTime() >= loadBackCooldown(player)) {
            int cooldown = LuckyMeta.getIntMetaNodeValue(player, "watercore.command.back.cooldown", W$SConfig.backCooldown());
            PLAYER_BACKCOOLDOWN.put(player.getName().getString(), System.nanoTime() + WUtil.secToMillis(cooldown));
            return true;
        }
        return false;
    }


    public static void saveBackData(@NotNull ServerPlayer player) {
        var key = player.getName().getString();
        var list = PLAYER_BACKDATA.getOrDefault(key, new ArrayList<>());

        list.add(0, new BackData(player));
        if (list.size() > 10) list.remove(list.size() - 1);

        // ENSURE SAVING
        PLAYER_BACKDATA.put(key, list);
    }

    public static BackData getBack(int index, ServerPlayer player) {
        var key = player.getName().getString();
        List<BackData> list;
        if ((list = PLAYER_BACKDATA.get(key)) == null) return null;
        if (index >= list.size()) return null;
        return list.get(index);
    }
}
