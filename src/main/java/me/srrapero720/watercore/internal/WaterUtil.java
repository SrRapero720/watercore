package me.srrapero720.watercore.internal;

import me.srrapero720.watercore.api.MCTextFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLLoader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.mixin.injection.struct.InjectorGroupInfo;
import org.spongepowered.asm.mixin.transformer.ClassInfo;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class WaterUtil {
    public static final Set<FriendlyByteBuf> BUFFERS = Collections.synchronizedSet(new HashSet<>());

    public static int toTicks(final double sec) { return (int) (sec * 20); }
    public static boolean isModLoading(String id) { return FMLLoader.getLoadingModList().getModFileById(id) != null; }
    public static boolean isModLoaded(String id) { return ModList.get().isLoaded(id); }
    public static boolean existsPackage(String id) { return Package.getPackage(id) != null; }

    public static @NotNull String getBroadcastPrefix() { return getBroadcastPrefix(MCTextFormat.parse("&6")); }
    public static @NotNull String getBroadcastPrefix(String color) { return "&e&l[&bWATERC&eo&bRE&e&l] " + color; }

    public static Vec3 findCenter(double x, double y, double z) {
        var deltaX = x - (int) x;
        var currentX = ((deltaX > -0.75D && deltaX < -0.25D)) ? (int) x - 0.5D : (deltaX > 0.25D && deltaX < 0.75D) ? (int) x + 0.5D : Math.round(x);

        var deltaZ = z - (int) z;
        var currentZ = ((deltaZ > -0.75D && deltaZ < -0.25D)) ? (int) z - 0.5D : (deltaZ > 0.25D && deltaZ < 0.75D) ? (int) z + 0.5D : Math.round(z);

        var centerY = (int) y + 0.25D;
        return new Vec3(currentX, centerY, currentZ);
    }
    public static long secondsToMilis(long sec) { return sec * 1000; }

    public static int fixAngle(double input) { return fixAngle(Math.round(input)); }
    public static int fixAngle(float input) { return fixAngle(Math.round(input)); }
    public static int fixAngle(int input) {
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

    public static File getGameDir() {
        return FMLEnvironment.dist == Dist.CLIENT ? Minecraft.getInstance().gameDirectory : new File("");
    }

    public static boolean isLong(String s) {
        try { Long.valueOf(s); return true; } catch (Exception e) { return false; }
    }

    public static boolean isFloat(String s) {
        try { Float.valueOf(s); return true; } catch (Exception e) { return false; }
    }

    public static boolean isInt(String s) {
        try { Integer.valueOf(s); return true; } catch (Exception e) { return false; }
    }

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

    private static final String OBJECT = "java/lang/Object";

    public static @Nullable ServerLevel findLevel(@NotNull Iterable<ServerLevel> levels, ResourceLocation hint) {
        for (var lvl: levels)
            if (lvl.dimension().location().toString().equals(hint.toString())) return lvl;

        return null;
    }

    public static void runInNewThread(Runnable runnable) {
        new Thread(runnable).start();
    }

    private static void emptyClassInfo() throws NoSuchFieldException, IllegalAccessException {
        WaterConsole.error("SpongeMixinTool", "Cleaning cache of Mixins is currently disabled. because TraceMixin feature got broken.");
        if (true) return; // Disabled cache cleaning
        if (WaterUtil.isModLoading("not-that-cc")) return; // Crashes crafty crashes if it crashes
        Field cacheField = ClassInfo.class.getDeclaredField("cache");
        cacheField.setAccessible(true);
        @SuppressWarnings("unchecked")
        var cache = ((Map<String, ClassInfo>)cacheField.get(null));
        ClassInfo jlo = cache.get(OBJECT);
        cache.clear();
        cache.put(OBJECT, jlo);
    }


    public static void loadMixinsAndClearMixinsCache() {
        MixinEnvironment.getCurrentEnvironment().audit();
        try { //Why is SpongePowered stealing so much ram for this garbage?
            var noGroupField = InjectorGroupInfo.Map.class.getDeclaredField("NO_GROUP");
            noGroupField.setAccessible(true);
            var noGroup = noGroupField.get(null);
            var membersField = noGroup.getClass().getDeclaredField("members");
            membersField.setAccessible(true);
            ((List<?>) membersField.get(noGroup)).clear(); // Clear spongePoweredCache
            emptyClassInfo();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static void printTrace(StackTraceElement[] stackTrace, StringBuilder crashReportBuilder) {
        if (stackTrace != null && stackTrace.length > 0) {
            crashReportBuilder.append("\n[WATERCoRE] Mixins in Stacktrace:");

            try {
                List<String> classNames = new ArrayList<>();
                for (StackTraceElement el : stackTrace) {
                    if (!classNames.contains(el.getClassName())) {
                        classNames.add(el.getClassName());
                    }
                }

                boolean found = false;
                for (String className : classNames) {
                    ClassInfo classInfo = ClassInfo.fromCache(className);
                    if (classInfo != null) {
                        // Workaround for bug in Mixin, where it adds to the wrong thing :(
                        Object mixinInfoSetObject;
                        try {
                            Method getMixins = ClassInfo.class.getDeclaredMethod("getMixins");
                            getMixins.setAccessible(true);
                            mixinInfoSetObject = getMixins.invoke(classInfo);
                        } catch (Exception e) {
                            // Fabric loader >=0.12.0 proguards out this method; use the field instead
                            var mixinsField = ClassInfo.class.getDeclaredField("mixins");
                            mixinsField.setAccessible(true);
                            mixinInfoSetObject = mixinsField.get(classInfo);
                        }

                        @SuppressWarnings("unchecked") Set<IMixinInfo> mixinInfoSet = (Set<IMixinInfo>) mixinInfoSetObject;

                        if (mixinInfoSet.size() > 0) {
                            crashReportBuilder.append("\n\t");
                            crashReportBuilder.append(className);
                            crashReportBuilder.append(":");
                            for (IMixinInfo info : mixinInfoSet) {
                                crashReportBuilder.append("\n\t\t");
                                crashReportBuilder.append(info.getClassName());
                                crashReportBuilder.append(" (");
                                crashReportBuilder.append(info.getConfig().getName());
                                crashReportBuilder.append(")");
                            }
                            found = true;
                        }
                    }
                }

                if (!found) {
                    crashReportBuilder.append(" None found");
                }
            } catch (Exception e) {
                crashReportBuilder.append(" Failed to find Mixin metadata: ").append(e);
            }
        }
    }

}
