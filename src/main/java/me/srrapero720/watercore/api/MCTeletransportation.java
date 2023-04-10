package me.srrapero720.watercore.api;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public abstract class MCTeletransportation {
    protected ResourceLocation dimension;
    protected double x = 0;
    protected double y = 128;
    protected double z = 0;
    protected float rotX = 0;
    protected float rotY = 0;

    public MCTeletransportation() {}
    public MCTeletransportation(ResourceLocation dimension, double @NotNull[]  position, float @NotNull[] angle) {
        this.dimension = dimension;
        this.x = position[0];
        this.y = position[1];
        this.z = position[2];
        this.rotX = angle[0];
        this.rotY = angle[1];
    }

    // GETTERS
    public ResourceLocation getDimension() { return dimension; }
    public Vec3 getPosition() { return new Vec3(x, y, z); }
    public double getX() { return x; }
    public double getY() { return y; }
    public double getZ() { return z; }
    public float[] getAngle() { return new float[] { rotX, rotY }; }
    public float getRotX() { return rotX; }
    public float getRotY() { return rotY; }
    public boolean invalid() { return dimension == null; }

    // API... PREVENTS TO OVERRIDE EVERYTHING
    protected abstract void onDataUpdated();

    // ADVANCED SETTERS
    public MCTeletransportation setDimension(ResourceLocation location) {
        if (location == null) throw new IllegalArgumentException("WATERCoRE API: ResourceLocation cannot be null");
        this.dimension = location;
        this.onDataUpdated();
        return this;
    }

    public MCTeletransportation setDimension(@NotNull String dim) {
        if (dim.isEmpty()) throw new IllegalArgumentException("WATERCoRE API: You tried to set a empty data dimension");
        this.dimension = new ResourceLocation(dim);
        this.onDataUpdated();
        return this;
    }

    public MCTeletransportation setDimension(@NotNull ResourceKey<Level> dimension) {
        this.dimension = dimension.location();
        this.onDataUpdated();
        return this;
    }

    public MCTeletransportation setCoordinates(double x, double y, double z, float rotX, float rotY) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.rotX = rotX;
        this.rotY = rotY;
        this.onDataUpdated();
        return this;
    }

    public MCTeletransportation setCoordinates(@NotNull BlockPos blockPos, float rotX, float rotY) {
        this.x = blockPos.getX();
        this.y = blockPos.getY();
        this.z = blockPos.getZ();
        this.rotX = rotX;
        this.rotY = rotY;
        this.onDataUpdated();
        return this;
    }

    public MCTeletransportation setCoordinates(@NotNull Vec3 cords, float rotX, float rotY) {
        this.x = cords.x;
        this.y = cords.y;
        this.z = cords.z;
        this.rotX = rotX;
        this.rotY = rotY;
        this.onDataUpdated();
        return this;
    }
}
