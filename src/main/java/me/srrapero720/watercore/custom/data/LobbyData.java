package me.srrapero720.watercore.custom.data;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.phys.Vec3;
import me.srrapero720.watercore.internal.WaterUtil;
import me.srrapero720.watercore.WaterCore;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class LobbyData extends SavedData {
    private static final String NAME = WaterCore.ID + "_dimensions";
    public static final File DATADIR = new File(WaterUtil.getGameDir(), "/config/fancymenu");

    private static LobbyData instance;

    private String dimID = "minecraft";
    private String dimName = "overworld";
    private int[] cords = new int[]{0, 128, 0};
    private int[] rotation = new int[]{0, 0};
    public int[] getCords() { return cords; }
    public int[] getRotation() { return rotation; }
    public boolean isEmpty() { return cords[0] == 0 && cords[1] == 128 && cords[2] == 0; }
    public ResourceLocation getDimension() { return new ResourceLocation(dimID, dimName); }

    public LobbyData setDimension(ResourceKey<Level> dimension) {
        dimID = dimension.location().getNamespace();
        dimName = dimension.location().getPath();
        this.setDirty(true);
        return this;
    }

    public LobbyData setCords(@NotNull BlockPos cords, float rotX, float rotY) {
        this.cords = new int[] { cords.getX(), cords.getY(), cords.getZ()};
        this.rotation = new int[] { (int) rotX, (int) rotY };
        this.setDirty(true);
        return this;
    }

    @Deprecated
    public LobbyData setCords(@NotNull Vec3 cords, float rotX, float rotY) {
        this.cords = new int[] {(int) cords.x, (int) cords.y, (int) cords.z};
        this.rotation = new int[] { (int) rotX, (int) rotY };
        this.setDirty(true);
        return this;
    }

    public static @NotNull LobbyData load(@NotNull CompoundTag tag) {
        instance = create();
        var rawCords = tag.getIntArray("lobbySpawn");
        var rawRotation = tag.getIntArray("lobbySpawnRot");
        var rawDimId = tag.getString("lobbyDimId");
        var rawDimName = tag.getString("lobbyDimName");


        if (!rawDimId.isEmpty()) instance.dimID = rawDimId;
        if (!rawDimName.isEmpty()) instance.dimName = rawDimName;
        if (rawRotation.length != 0) instance.rotation = rawRotation;
        if (rawCords.length != 0) instance.cords = rawCords;

        return instance;
    }

    @Contract(" -> new")
    public static @NotNull LobbyData create() { return instance = new LobbyData(); }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag tag) {
        tag.putString("lobbyDimId", dimID);
        tag.putString("lobbyDimName", dimName);
        tag.putIntArray("lobbySpawn", cords);
        tag.putIntArray("lobbySpawnRot", rotation);
        this.setDirty(false);
        return tag;
    }

    public static LobbyData fetch(@NotNull MinecraftServer server) {
        server.overworld().getDataStorage().computeIfAbsent(LobbyData::load, LobbyData::create, "DimensionData");
        return instance;
    }
}
