package me.srrapero720.watercore.utility;

import me.srrapero720.watercore.api.placeholder.Placeholder;
import me.srrapero720.watercore.api.thread.ThreadUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLLoader;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.net.URL;

public class Tools {
    public static final String OBJECT = "java/lang/Object";
    public static final String GD_URL = "https://drive.google.com/uc?id=%FILE_ID%&export=download";
    public static final File GAME_DIR = new File("");

    public static int toTicks(final double sec) { return (int) (sec * 20); }


    // IS LOADED SOMETHING
    public static boolean isModFMLoading(String id) { return FMLLoader.getLoadingModList().getModFileById(id) != null; }
    public static boolean isModLoaded(String id) { return ModList.get().isLoaded(id); }
    public static boolean isModInstalled(String id) { return false; } //TODO: make a file check
    public static boolean isPackageLoaded(String id) { return Package.getPackage(id) != null; }

    // SIDE CHECK
    public static boolean isClientSide() { return FMLEnvironment.dist == Dist.CLIENT; }
    public static boolean isServerSide() { return FMLEnvironment.dist == Dist.DEDICATED_SERVER; }
    public static boolean isNoside() { return !isClientSide() && !isServerSide(); }

    // PREFIX BUILER
    public static @NotNull String broadcastPrefix() { return Placeholder.parse("&e&l[&bWATERC&eo&bRE&e&l] "); }
    public static @NotNull String broadcastPrefix(String color) { return Placeholder.parse("&e&l[&bWATERC&eo&bRE&e&l] " + color) ; }

    @Contract(pure = true)
    @OnlyIn(Dist.CLIENT)
    public static @NotNull Minecraft mc() { return Minecraft.getInstance(); }
    @OnlyIn(Dist.CLIENT)
    public static float toDeltaFrames() { return mc().isPaused() ? 1.0F : mc().getFrameTime(); }

    public static long secToMillis(final long sec) { return sec * 1000; }

    public static @Nullable ServerLevel fetchLevel(@NotNull Iterable<ServerLevel> levels, ResourceLocation hint) {
        for (var lvl: levels) if (lvl.dimension().location().toString().equals(hint.toString())) return lvl;
        return null;
    }

    @SuppressWarnings("ConstantValue")
    public static boolean isLong(String s) { return ThreadUtil.tryAndReturn((def) -> Long.valueOf(s) != null, false); }
    @SuppressWarnings("ConstantValue")
    public static boolean isFloat(String s) { return ThreadUtil.tryAndReturn((def) -> Float.valueOf(s) != null, false); }
    @SuppressWarnings("ConstantValue")
    public static boolean isInt(String s) { return ThreadUtil.tryAndReturn((def) -> Integer.valueOf(s) != null, false); }

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


    // GOOGLE DRIVE DIRECT DOWNLOAD GENERATOR
    public static String googleDriveDownload(String url) {
        if (!url.contains("drive.google.com/file/d/")) return null;
        return ThreadUtil.tryAndReturn((defaultVar) -> {
            var url1 = new URL(url);
            var paths = url1.getPath().split("/");
            return GD_URL.replace("%FILE_ID%", paths[2]);
        }, null);
    }
}
