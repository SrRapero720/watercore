package me.srrapero720.watercore.custom.data;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class BackData {
    public final ResourceLocation dimension;
    public final double x, y, z;
    public final float xRot, yRot;

    public BackData(ResourceLocation dim, double x, double y, double z, float xRot, float yRot) {
        dimension = dim;
        this.x = x;
        this.y = y;
        this.z = z;
        this.xRot = xRot;
        this.yRot = yRot;
    }

    public static void saveLastPosition(@NotNull ServerPlayer player) {
        var data = ((IPlayerData) player).getPersistentData();
        data.putBoolean("backValid", true);
        data.putString("backDimension", player.getLevel().dimension().location().toString());
        data.putDouble("backX", player.getX());
        data.putDouble("backY", player.getY());
        data.putDouble("backZ", player.getZ());
        data.putFloat("backXRot", player.getXRot());
        data.putFloat("backYRot", player.getYRot());
    }

    public static @Nullable BackData loadLastPosition(@NotNull ServerPlayer player) {
        var data = ((IPlayerData) player).getPersistentData();

        return data.getBoolean("backValid") ? new BackData(
                new ResourceLocation(data.getString("backDimension")),
                data.getDouble("backX"), data.getDouble("backY"), data.getDouble("backZ"),
                data.getFloat("backXRot"), data.getFloat("backYRot")
        ) : null;
    }
}
