package me.srrapero720.watercore.custom.data;

import me.srrapero720.watercore.api.data.TeleportToMC;
import me.srrapero720.watercore.custom.data.storage.IPlayerStorage;
import me.srrapero720.watercore.utility.Logg;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class BackData extends TeleportToMC {
    public BackData(ResourceLocation dim, double x, double y, double z, float xRot, float yRot) {
        super(dim, new double[] { x, y, z }, new float[] { xRot, yRot });
    }

    public BackData(ServerPlayer player) {
        this.dimension = player.getLevel().dimension().location();
        this.x = player.getX();
        this.y = player.getY();
        this.z = player.getZ();
        this.rotX = player.getXRot();
        this.rotY = player.getYRot();
    }

    @Override
    protected void onDataUpdated() { /* UNUSED HERE */ }

    @Deprecated(forRemoval = true)
    public static void saveLastPosition(@NotNull ServerPlayer player) {
        Logg.warn("called method...");
        var data = ((IPlayerStorage) player).getWatercoreData();
        data.putBoolean("backValid", true);
        data.putString("backDimension", player.getLevel().dimension().location().toString());
        data.putDouble("backX", player.getX());
        data.putDouble("backY", player.getY());
        data.putDouble("backZ", player.getZ());
        data.putFloat("backXRot", player.getXRot());
        data.putFloat("backYRot", player.getYRot());
    }

    @Deprecated(forRemoval = true)
    public static void saveDeathPosition(@NotNull ServerPlayer oldPlayer, ServerPlayer brandNewPlayer) {
        var data = ((IPlayerStorage) brandNewPlayer).getWatercoreData();

        data.putBoolean("backValid", true);
        data.putString("backDimension", oldPlayer.getLevel().dimension().location().toString());
        data.putDouble("backX", oldPlayer.getX());
        data.putDouble("backY", oldPlayer.getY());
        data.putDouble("backZ", oldPlayer.getZ());
        data.putFloat("backXRot", oldPlayer.getXRot());
        data.putFloat("backYRot", oldPlayer.getYRot());
    }

    @Deprecated(forRemoval = true)
    public static @Nullable BackData loadLastPosition(@NotNull ServerPlayer player) {
        var data = ((IPlayerStorage) player).getWatercoreData();

        return data.getBoolean("backValid") ? new BackData(
                new ResourceLocation(data.getString("backDimension")),
                data.getDouble("backX"), data.getDouble("backY"), data.getDouble("backZ"),
                data.getFloat("backXRot"), data.getFloat("backYRot")
        ) : null;
    }
}
