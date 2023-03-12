package me.srrapero720.watercore.mixin_util;

import me.srrapero720.watercore.water.WaterConsole;
import me.srrapero720.watercore.water.WaterUtil;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.injection.struct.InjectorGroupInfo;
import org.spongepowered.asm.mixin.transformer.ClassInfo;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class SpongeMixinTool {
    private static final String OBJECT = "java/lang/Object";

    private static void emptyClassInfo() throws NoSuchFieldException, IllegalAccessException {
        if (WaterUtil.isModOnline("not-that-cc")) return; // Crashes crafty crashes if it crashes
        Field cacheField = ClassInfo.class.getDeclaredField("cache");
        cacheField.setAccessible(true);
        @SuppressWarnings("unchecked")
        var cache = ((Map<String, ClassInfo>)cacheField.get(null));
        ClassInfo jlo = cache.get(OBJECT);
        cache.clear();
        cache.put(OBJECT, jlo);
    }


    public static void forceLoadAllMixinsAndClearSpongePoweredCache() {
        WaterConsole.error("SpongeMixinTool", "Cleaning cache of Mixins is currently disabled. because TraceMixin feature got broken.");
        if (true) return;
        MixinEnvironment.getCurrentEnvironment().audit();
        try { //Why is SpongePowered stealing so much ram for this garbage?
            Field noGroupField = InjectorGroupInfo.Map.class.getDeclaredField("NO_GROUP");
            noGroupField.setAccessible(true);
            Object noGroup = noGroupField.get(null);
            Field membersField = noGroup.getClass().getDeclaredField("members");
            membersField.setAccessible(true);
            ((List<?>) membersField.get(noGroup)).clear(); // Clear spongePoweredCache
            emptyClassInfo();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
