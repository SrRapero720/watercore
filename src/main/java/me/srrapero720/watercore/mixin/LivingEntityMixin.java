package me.srrapero720.watercore.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Optional;

@Mixin(LivingEntity.class)
@Deprecated(forRemoval = true, since = "1.19.4")
public abstract class LivingEntityMixin extends Entity {
    @Shadow protected Brain<?> brain;

    public LivingEntityMixin(EntityType<?> p_19870_, Level p_19871_) { super(p_19870_, p_19871_); }

    @Override
    public void remove(Entity.RemovalReason reason) {
        super.remove(reason);
        this.brain.memories.keySet().forEach(module -> this.brain.memories.put(module, Optional.empty()));
    }
}
