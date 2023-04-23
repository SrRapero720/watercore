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

public class WCoreTraceUtil {
    private static void spongeEmptyClassInfo() throws NoSuchFieldException, IllegalAccessException {
        WConsole.error("SpongeMixinTool", "Cleaning cache of Mixins is currently disabled. because TraceMixin feature got broken.");
        if (true) return; // Disabled cache cleaning
        if (WCoreUtil.isModFMLoading("not-that-cc")) return; // Crashes crafty crashes if it crashes
        Field cacheField = ClassInfo.class.getDeclaredField("cache");
        cacheField.setAccessible(true);
        @SuppressWarnings("unchecked")
        var cache = ((Map<String, ClassInfo>)cacheField.get(null));
        ClassInfo jlo = cache.get(WCoreUtil.OBJECT);
        cache.clear();
        cache.put(WCoreUtil.OBJECT, jlo);
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


}
