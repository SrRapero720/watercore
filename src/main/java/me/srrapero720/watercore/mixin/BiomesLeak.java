package me.srrapero720.watercore.mixin;

import it.unimi.dsi.fastutil.longs.Long2FloatLinkedOpenHashMap;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Supplier;

@Mixin(value = Biome.class, priority = 72)
public class BiomesLeak {
    // Incompatible con Saturn
    private static final ThreadLocal<Long2FloatLinkedOpenHashMap> TEMP_CACHE;
    static {
        TEMP_CACHE = ThreadLocal.withInitial(BiomesLeak::supplier);
//        TEMP_CACHE = ThreadLocal.withInitial(() -> Util.make(() -> {
//            final var openHashMap = new Long2FloatLinkedOpenHashMap(1024, 0.25F) { @Override protected void rehash(int n) {} };
//            openHashMap.defaultReturnValue(Float.NaN);
//            return openHashMap;
//        }));
    }

    private static @NotNull Long2FloatLinkedOpenHashMap supplier() {
        final var map = new Long2FloatLinkedOpenHashMap(1024, 0.25F) { @Override protected void rehash(int n) {} };
        map.defaultReturnValue(Float.NaN);
        return map;
    }

    @Redirect(method = "<init>", at = @At(value = "INVOKE",target = "Ljava/lang/ThreadLocal;withInitial(Ljava/util/function/Supplier;)Ljava/lang/ThreadLocal;"))
    private ThreadLocal<Long2FloatLinkedOpenHashMap> useStaticThreadLocal(Supplier<?> supplier) {
        return TEMP_CACHE;
    }
}