package me.srrapero720.watercore.mixin.common;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.ExpirableValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(LivingEntity.class)
@Deprecated(forRemoval = true, since = "mc1.19.3")
public class LivingEntityMixin extends EntityMixin {
    @Shadow protected Brain<?> brain;

    @Override
    public void injectRemove(CallbackInfo ci) {
        this.brain.memories.keySet().forEach(module -> this.brain.setMemoryInternal(module, Optional.empty().map(ExpirableValue::of)));
    }
}
