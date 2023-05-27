package me.srrapero720.watercore.api.minecraft;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public abstract class TeleportTarget {
    protected ResourceLocation dimension;
    protected double x = 0;
    protected double y = 128;
    protected double z = 0;
    protected float rotX = 0;
    protected float rotY = 0;

    protected TeleportTarget() {}
    public TeleportTarget(ResourceLocation dimension, double @NotNull[]  position, float @NotNull[] angle) {
        this.dimension = dimension;
        this.x = position[0];
        this.y = position[1];
        this.z = position[2];
        this.rotX = angle[0];
        this.rotY = angle[1];
    }

    public static @NotNull Vec3 getNearbyCenter(double x, double y, double z) {
        var deltaX = x - (int) x;
        var currentX = ((deltaX > -0.75D && deltaX < -0.25D)) ? (int) x - 0.5D : (deltaX > 0.25D && deltaX < 0.75D) ? (int) x + 0.5D : Math.round(x);

        var deltaZ = z - (int) z;
        var currentZ = ((deltaZ > -0.75D && deltaZ < -0.25D)) ? (int) z - 0.5D : (deltaZ > 0.25D && deltaZ < 0.75D) ? (int) z + 0.5D : Math.round(z);

        var centerY = (int) y + 0.1D;
        return new Vec3(currentX, centerY, currentZ);
    }

    public static int getFixAngle(final float input) { return getFixAngle(Math.round(input)); }
    public static int getFixAngle(final int input) {
        var angle = input;

        if (angle >= 0 && angle <= 45) angle = 0;
        else if (angle >= 45 && angle <= 90) angle = 90;
        else if (angle >= 90 && angle <= 135) angle = 90;
        else if (angle >= 135 && angle <= 180) angle = 180;
        else if (angle >= -180 && angle <= -135) angle = -180;
        else if (angle >= -135 && angle <= -90) angle = -90;
        else if (angle >= -90 && angle <= -45) angle = -90;
        else if (angle >= -45 && angle <= 0) angle = 0;

        return angle;
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
    public TeleportTarget setDimension(ResourceLocation location) {
        if (location == null) throw new IllegalArgumentException("WATERCoRE API: ResourceLocation cannot be null");
        this.dimension = location;
        this.onDataUpdated();
        return this;
    }

    public TeleportTarget setDimension(@NotNull String dim) {
        if (dim.isEmpty()) throw new IllegalArgumentException("WATERCoRE API: You tried to set a empty data dimension");
        this.dimension = new ResourceLocation(dim);
        this.onDataUpdated();
        return this;
    }

    public TeleportTarget setDimension(@NotNull ResourceKey<Level> dimension) {
        this.dimension = dimension.location();
        this.onDataUpdated();
        return this;
    }

    public TeleportTarget setCoordinates(double x, double y, double z, float rotX, float rotY) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.rotX = rotX;
        this.rotY = rotY;
        this.onDataUpdated();
        return this;
    }

    public TeleportTarget setCoordinates(@NotNull BlockPos blockPos, float rotX, float rotY) {
        this.x = blockPos.getX();
        this.y = blockPos.getY();
        this.z = blockPos.getZ();
        this.rotX = rotX;
        this.rotY = rotY;
        this.onDataUpdated();
        return this;
    }

    public TeleportTarget setCoordinates(@NotNull Vec3 cords, float rotX, float rotY) {
        this.x = cords.x;
        this.y = cords.y;
        this.z = cords.z;
        this.rotX = rotX;
        this.rotY = rotY;
        this.onDataUpdated();
        return this;
    }
}
