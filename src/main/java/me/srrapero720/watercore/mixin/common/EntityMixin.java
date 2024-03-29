package me.srrapero720.watercore.mixin.common;

import me.srrapero720.watercore.custom.data.storage.IPlayerStorage;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin implements IPlayerStorage {
    private CompoundTag persistentTag;

    @Override
    public CompoundTag getWatercoreData() {
        return persistentTag == null ? this.persistentTag = new CompoundTag() : this.persistentTag;
    }

    @Inject(method = "save", at = @At(value = "HEAD"))
    public void injectSave(CompoundTag tag, CallbackInfoReturnable<Boolean> cir) {
        if (persistentTag != null) tag.put("watercore.player_data", persistentTag);
    }

    @Inject(method = "load", at = @At(value = "HEAD"))
    public void injectRead(@NotNull CompoundTag tag, CallbackInfo ci) {
        if (tag.contains("watercore.player_data", 10))
            persistentTag = tag.getCompound("watercore.player_data");
    }

    @Inject(method = "remove", at = @At("TAIL"), allow = 1)
    protected void injectRemove(CallbackInfo ci) {}
}
