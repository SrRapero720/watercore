package me.srrapero720.watercore.mixin;


import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import net.minecraft.tags.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TagKey.class)
public class TagKeyMixin {
    @Redirect(method = "<clinit>",
            at = @At(value = "INVOKE", target = "Lcom/google/common/collect/Interners;newStrongInterner()Lcom/google/common/collect/Interner;")
    )
    private static Interner<TagKey<?>> memoryLeakFix$useWeakInterner() {
        return Interners.newWeakInterner();
    }
}
