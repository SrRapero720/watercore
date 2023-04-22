package me.srrapero720.watercore.api.luckperms;

import net.minecraft.world.entity.player.Player;

public class LuckyPerm {
    public static boolean havePermission(Player player, String nodePerm) {
        var permissionData = LuckyCore.LP.getUserManager().getUser(player.getGameProfile().getId()).getCachedData().getPermissionData();
        return permissionData.checkPermission(nodePerm).asBoolean();
    }
}
