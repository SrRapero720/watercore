package me.srrapero720.watercore.custom.data;

import me.srrapero720.watercore.WaterCore;
import me.srrapero720.watercore.api.minecraft.TeleportTarget;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

public class PlayerSpawn extends SavedData {
    // DATA MANAGMENT
    private final TeleportTarget teleport;
    public PlayerSpawn() {
        this.teleport = new TeleportTarget() {
            @Override
            protected void onDataUpdated() { PlayerSpawn.this.setDirty(); }
        };
    }

    // SAVE / LOAD ENDPOINT
    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag tag) {
        tag.putString("dimension", teleport.getDimension().toString());
        tag.putDouble("x", teleport.getX());
        tag.putDouble("y", teleport.getY());
        tag.putDouble("z", teleport.getZ());
        tag.putFloat("rotX", teleport.getRotX());
        tag.putFloat("rotY", teleport.getRotY());
        this.setDirty(false);
        return tag;
    }

    public static @NotNull PlayerSpawn load(@NotNull CompoundTag tag) {
        var self = create();
        var dimension = tag.getString("dimension");

        if (!dimension.isEmpty()) {
            self.teleport.setDimension(dimension);
            self.teleport.setCoordinates(
                    tag.getDouble("x"),
                    tag.getDouble("y"),
                    tag.getDouble("z"),
                    tag.getFloat("rotX"),
                    tag.getFloat("rotY")
            );
        }
        return self;
    }

    // SELF INSTANCE
    public static @NotNull PlayerSpawn create() { return new PlayerSpawn(); }


    // FETCH WHAT DATA
    @Deprecated
    public static TeleportTarget fetch(Mode mode, @NotNull MinecraftServer server) {
        var self = server.overworld().getDataStorage().computeIfAbsent(PlayerSpawn::load, PlayerSpawn::create, mode.toString());
        return self.teleport;
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
        public String toString() { return value.toString().replace(":", "_"); }
    }
}