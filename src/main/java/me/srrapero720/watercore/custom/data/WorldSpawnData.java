package me.srrapero720.watercore.custom.data;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class WorldSpawnData extends SavedData {
    private static WorldSpawnData instance;

    private String dimensionID = "minecraft";
    private String dimensionName = "overworld";
    private int[] cords = new int[]{0, 128, 0};
    private int[] rotation = new int[]{0, 0};
    public int[] getCords() { return cords; }
    public int[] getRotation() { return rotation; }
    public boolean isEmpty() { return cords[0] == 0 && cords[1] == 128 && cords[2] == 0; }
    public ResourceLocation getDimension() { return new ResourceLocation(dimensionID, dimensionName); }

    public WorldSpawnData setDimension(@NotNull ResourceKey<Level> dimension) {
        dimensionID = dimension.location().getNamespace();
        dimensionName = dimension.location().getPath();
        this.setDirty(true);
        return this;
    }

    public WorldSpawnData setCords(@NotNull BlockPos cords, float rotX, float rotY) {

        this.cords = new int[] { cords.getX(), cords.getY(), cords.getZ()};
        this.rotation = new int[] { (int) rotX, (int) rotY };
        this.setDirty(true);
        return this;
    }

    public WorldSpawnData setCords(@NotNull Vec3 cords, float rotX, float rotY) {
        this.cords = new int[] {(int) cords.x, (int) cords.y, (int) cords.z};
        this.rotation = new int[] { (int) rotX, (int) rotY };
        this.setDirty(true);
        return this;
    }

    public static @NotNull WorldSpawnData load(@NotNull CompoundTag tag) {
        instance = create();
        var rawDimensionID = tag.getString("dimensionID");
        var rawDimensionName = tag.getString("dimensionName");
        var rawPosition = tag.getIntArray("spawnPosition");
        var rawAngle = tag.getIntArray("spawnAngle");

        if (!rawDimensionID.isEmpty()) instance.dimensionID = rawDimensionID;
        if (!rawDimensionName.isEmpty()) instance.dimensionName = rawDimensionName;
        if (rawAngle.length != 0) instance.rotation = rawAngle;
        if (rawPosition.length != 0) instance.cords = rawPosition;

        return instance;
    }

    @Contract(" -> new")
    public static @NotNull WorldSpawnData create() { return instance = new WorldSpawnData(); }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag tag) {
        tag.putString("dimensionID", dimensionID);
        tag.putString("dimensionName", dimensionName);
        tag.putIntArray("spawnPosition", cords);
        tag.putIntArray("spawnAngle", rotation);
        this.setDirty(false);
        return tag;
    }

    public static WorldSpawnData fetch(@NotNull MinecraftServer server) {
        server.overworld().getDataStorage().computeIfAbsent(WorldSpawnData::load, WorldSpawnData::create, "WCWorldSpawnData");
        return instance;
    }
}
