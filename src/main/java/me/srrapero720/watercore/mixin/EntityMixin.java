package me.srrapero720.watercore.mixin;

import me.srrapero720.watercore.custom.data.IPlayerData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin implements IPlayerData {
    private CompoundTag persistentTag;

    @Override
    public CompoundTag getPersistentData() {
        return persistentTag == null ? this.persistentTag = new CompoundTag() : this.persistentTag;
    }

    @Inject(method = "save", at = @At(value = "HEAD"))
    public void injectSave(CompoundTag tag, CallbackInfoReturnable<Boolean> cir) {
        tag.put("watercore.player_data", tag);
    }

    @Inject(method = "load", at = @At(value = "HEAD"))
    public void injectRead(CompoundTag tag, CallbackInfo ci) {

        if (tag.contains("watercore.player_data", 10))
            persistentTag = tag.getCompound("watercore.player_data");
    }
}
