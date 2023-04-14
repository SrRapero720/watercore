package me.srrapero720.watercore.internal;

import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.mixin.injection.struct.InjectorGroupInfo;
import org.spongepowered.asm.mixin.transformer.ClassInfo;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WTrace {
    // TODO: Separate this feature. Create other mod to run this and toggle features.
    private static void spongeEmptyClassInfo() throws NoSuchFieldException, IllegalAccessException {
        WConsole.error("SpongeMixinTool", "Cleaning cache of Mixins is currently disabled. because TraceMixin feature got broken.");
        if (true) return; // Disabled cache cleaning
        if (WUtil.isModFMLoading("not-that-cc")) return; // Crashes crafty crashes if it crashes
        Field cacheField = ClassInfo.class.getDeclaredField("cache");
        cacheField.setAccessible(true);
        @SuppressWarnings("unchecked")
        var cache = ((Map<String, ClassInfo>)cacheField.get(null));
        ClassInfo jlo = cache.get(WUtil.OBJECT);
        cache.clear();
        cache.put(WUtil.OBJECT, jlo);
    }

    public static void loadMixinsAndClearCache() {
        MixinEnvironment.getCurrentEnvironment().audit();
        try {
            var noGroupField = InjectorGroupInfo.Map.class.getDeclaredField("NO_GROUP");
            noGroupField.setAccessible(true);
            var noGroup = noGroupField.get(null);
            var membersField = noGroup.getClass().getDeclaredField("members");
            membersField.setAccessible(true);
            ((List<?>) membersField.get(noGroup)).clear(); // Clear spongePoweredCache
            spongeEmptyClassInfo();
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
