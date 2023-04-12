package me.srrapero720.watercore.internal;

import me.srrapero720.watercore.api.MCTextFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLLoader;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.*;

public class WaterUtil {
    public static final Set<FriendlyByteBuf> BUFFERS = Collections.synchronizedSet(new HashSet<>());
    public static final String OBJECT = "java/lang/Object";
    public static final File GAME_DIR = isClientSide() ? mc().gameDirectory : new File("");

    public static int toTicks(final double sec) { return (int) (sec * 20); }


    // IS LOADED SOMETHING
    public static boolean isModFMLoading(String id) { return FMLLoader.getLoadingModList().getModFileById(id) != null; }
    public static boolean isModLoaded(String id) { return ModList.get().isLoaded(id); }
    public static boolean isPackageLoaded(String id) { return Package.getPackage(id) != null; }

    // SIDE CHECK
    public static boolean isClientSide() { return FMLEnvironment.dist == Dist.CLIENT; }
    public static boolean isServerSide() { return FMLEnvironment.dist == Dist.DEDICATED_SERVER; }
    public static boolean isNoside() { return !isClientSide() && !isServerSide(); }

    // PREFIX BUILER
    public static @NotNull String getBroadcastPrefix() { return getBroadcastPrefix(MCTextFormat.parse("&6")); }
    public static @NotNull String getBroadcastPrefix(String color) { return "&e&l[&bWATERC&eo&bRE&e&l] " + color; }

    @Contract(pure = true)
    @OnlyIn(Dist.CLIENT)
    public static @NotNull Minecraft mc() { return Minecraft.getInstance(); }
    @OnlyIn(Dist.CLIENT)
    public static float toDeltaFrames() { return mc().isPaused() ? 1.0F : mc().getFrameTime(); }

    @Contract("_, _, _ -> new")
    public static @NotNull Vec3 calculateNearbyCenter(double x, double y, double z) {
        var deltaX = x - (int) x;
        var currentX = ((deltaX > -0.75D && deltaX < -0.25D)) ? (int) x - 0.5D : (deltaX > 0.25D && deltaX < 0.75D) ? (int) x + 0.5D : Math.round(x);

        var deltaZ = z - (int) z;
        var currentZ = ((deltaZ > -0.75D && deltaZ < -0.25D)) ? (int) z - 0.5D : (deltaZ > 0.25D && deltaZ < 0.75D) ? (int) z + 0.5D : Math.round(z);

        var centerY = (int) y + 0.1D;
        return new Vec3(currentX, centerY, currentZ);
    }

    public static long secToMillis(final long sec) { return sec * 1000; }
    public static int fixAngle(final float input) { return fixAngle(Math.round(input)); }
    public static int fixAngle(final int input) {
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

    public static @Nullable ServerLevel fetchLevel(@NotNull Iterable<ServerLevel> levels, ResourceLocation hint) {
        for (var lvl: levels) if (lvl.dimension().location().toString().equals(hint.toString())) return lvl;
        return null;
    }

    @SuppressWarnings("ConstantValue")
    public static boolean isLong(String s) { return tryWithReturn(() -> Long.valueOf(s) != null, false); }
    @SuppressWarnings("ConstantValue")
    public static boolean isFloat(String s) { return tryWithReturn(() -> Float.valueOf(s) != null, false); }
    @SuppressWarnings("ConstantValue")
    public static boolean isInt(String s) { return tryWithReturn(() -> Integer.valueOf(s) != null, false); }

    /* THANKS STACKOVERFLOW
    * https://stackoverflow.com/questions/5051395/java-float-123-129456-to-123-12-without-rounding
    */
    public static float twoDecimal(double number) { return twoDecimal(Double.toString(number)); }
    public static float twoDecimal(float number) { return twoDecimal(Float.toString(number)); }
    public static float twoDecimal(String number) {
        StringBuilder sbFloat = new StringBuilder(number);
        int start = sbFloat.indexOf(".");
        if (start < 0) {
            return Float.parseFloat(sbFloat.toString());
        }
        int end = start+3;
        if((end)>(sbFloat.length()-1)) end = sbFloat.length();

        String twoPlaces = sbFloat.substring(start, end);
        sbFloat.replace(start, sbFloat.length(), twoPlaces);
        return Float.parseFloat(sbFloat.toString());
    }

    public static <T> T tryWithReturn(ReturnableRunnable<T> runnable, T defaultVar) {
        try {return runnable.run();
        } catch (Exception ignored) { return defaultVar; }
    }

    public interface ReturnableRunnable<T> { T run(); }
}
