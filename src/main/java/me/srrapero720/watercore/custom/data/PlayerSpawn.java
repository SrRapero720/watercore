package me.srrapero720.watercore.custom.data;

import me.srrapero720.watercore.WaterCore;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class PlayerSpawn extends SavedData {
    // DATA MANAGMENT
    private ResourceLocation dimension;
    private final double[] position = new double[] {0, 128, 0};
    private final int[] angle = new int[] {0, 0};

    // GETTERS
    public ResourceLocation getDimension() { return dimension; }
    public Vec3 getPosition() { return new Vec3(position[0], position[1], position[2]); }
    public double getX() { return position[0]; }
    public double getY() { return position[1]; }
    public double getZ() { return position[2]; }
    public int[] getAngle() { return angle; }
    public int getRotX() { return angle[0]; }
    public int getRotY() { return angle[1]; }
    public boolean invalid() { return dimension == null; }

    // SETTERS
    public PlayerSpawn setDimension(ResourceLocation location) {
        if (location == null) throw new IllegalArgumentException("WATERCoRE API: ResourceLocation cannot be null");
        dimension = location;
        this.setDirty(true);
        return this;
    }
    public PlayerSpawn setDimension(@NotNull String dim) {
        if (dim.isEmpty()) throw new IllegalArgumentException("WATERCoRE API: You tried to set a empty data dimension");
        dimension = new ResourceLocation(dim);
        this.setDirty(true);
        return this;
    }

    public PlayerSpawn setDimension(@NotNull ResourceKey<Level> dimension) {
        this.dimension = dimension.location();
        this.setDirty(true);
        return this;
    }

    public PlayerSpawn setCoordinates(@NotNull BlockPos blockPos, int rotX, int rotY) {
        this.position[0] = blockPos.getX();
        this.position[1] = blockPos.getY();
        this.position[2] = blockPos.getZ();
        this.angle[0] = rotX;
        this.angle[1] = rotY;
        this.setDirty(true);
        return this;
    }

    public PlayerSpawn setCoordinates(@NotNull Vec3 cords, int rotX, int rotY) {
        this.position[0] = cords.x;
        this.position[1] = cords.y;
        this.position[2] = cords.z;
        this.angle[0] = rotX;
        this.angle[1] = rotY;
        this.setDirty(true);
        return this;
    }

    // SAVE / LOAD ENDPOINT
    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag tag) {
        tag.putString("dimension", dimension.toString());
        tag.putDouble("x", position[0]);
        tag.putDouble("y", position[1]);
        tag.putDouble("z", position[2]);
        tag.putIntArray("angle", angle);
        this.setDirty(false);
        return tag;
    }

    public static @NotNull PlayerSpawn load(@NotNull CompoundTag tag) {
        var instance = create();
        var dimension = tag.getString("dimension");

        if (!dimension.isEmpty()) {
            var angle = tag.getIntArray("angle");
            instance.dimension = new ResourceLocation(dimension);
            instance.position[0] = tag.getDouble("x");
            instance.position[1] = tag.getDouble("y");
            instance.position[2] = tag.getDouble("z");
            instance.angle[0] = angle[0];
            instance.angle[1] = angle[1];
        }

        return instance;
    }

    // SELF INSTANCE
    public static @NotNull PlayerSpawn create() { return new PlayerSpawn(); }


    // FETCH WHAT DATA
    public static PlayerSpawn fetch(Mode mode,  @NotNull MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(PlayerSpawn::load, PlayerSpawn::create, mode.toString());
    }


    // SEPARATORS
    public enum Mode {
        LOBBY(new ResourceLocation(WaterCore.ID, "lobbyspawn")),
        WORLD(new ResourceLocation(WaterCore.ID, "worldspawn"));

        final ResourceLocation value;
        Mode(ResourceLocation location) {
            value = location;
        }

        @Override
        public String toString() {
            return value.toString();
        }
    }
}
