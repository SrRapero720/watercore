package me.srrapero720.watercore.api;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.HashMap;
import java.util.function.Supplier;

@OnlyIn(Dist.DEDICATED_SERVER)
public class PlaceholdersApi {
    private static HashMap<String, Supplier<String>> PH_STRING = new HashMap<>();
    private static HashMap<String, Supplier<Integer>> PH_INT = new HashMap<>();
    private static HashMap<String, Supplier<Float>> PH_FLOAT = new HashMap<>();
    private static HashMap<String, Supplier<Long>> PH_LONG = new HashMap<>();

    public static Class<PlaceholdersApi> registerString(String name, Supplier<String> supplier) {
        PH_STRING.put(name, supplier);
        return PlaceholdersApi.class;
    }

    public static Class<PlaceholdersApi> registerInt(String name, Supplier<Integer> supplier) {
        PH_INT.put(name, supplier);
        return PlaceholdersApi.class;
    }

    public static Class<PlaceholdersApi> registerFloat(String name, Supplier<Float> supplier) {
        PH_FLOAT.put(name, supplier);
        return PlaceholdersApi.class;
    }
    public static Class<PlaceholdersApi> registerLong(String name, Supplier<Long> supplier) {
        PH_LONG.put(name, supplier);
        return PlaceholdersApi.class;
    }



    public static String getString(String name) {
        return PH_STRING.get(name).get();
    }

    public static int getInt(String name) {
        return PH_INT.get(name).get();
    }

    public static float getFloat(String name) {
        return PH_FLOAT.get(name).get();
    }

    public static long getLong(String name) {
        return PH_LONG.get(name).get();
    }
}