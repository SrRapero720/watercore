package me.srrapero720.watercore.custom.data;

import me.srrapero720.watercore.WaterCore;
import me.srrapero720.watercore.api.MCTeletransportation;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

public class PlayerSpawn extends SavedData {
    // DATA MANAGMENT
    private final MCTeletransportation teletransportation;
    public PlayerSpawn() {
        this.teletransportation = new MCTeletransportation() {
            @Override
            protected void onDataUpdated() { PlayerSpawn.this.setDirty(); }
        };
    }

    // SAVE / LOAD ENDPOINT
    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag tag) {
        tag.putString("dimension", teletransportation.getDimension().toString());
        tag.putDouble("x", teletransportation.getX());
        tag.putDouble("y", teletransportation.getY());
        tag.putDouble("z", teletransportation.getZ());
        tag.putFloat("rotX", teletransportation.getRotX());
        tag.putFloat("rotY", teletransportation.getRotY());
        this.setDirty(false);
        return tag;
    }

    public static @NotNull PlayerSpawn load(@NotNull CompoundTag tag) {
        var self = create();
        var dimension = tag.getString("dimension");

        if (!dimension.isEmpty()) {
            self.teletransportation.setDimension(dimension);
            self.teletransportation.setCoordinates(
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
    public static MCTeletransportation fetch(Mode mode,  @NotNull MinecraftServer server) {
        var self = server.overworld().getDataStorage().computeIfAbsent(PlayerSpawn::load, PlayerSpawn::create, mode.toString());
        return self.teletransportation;
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
