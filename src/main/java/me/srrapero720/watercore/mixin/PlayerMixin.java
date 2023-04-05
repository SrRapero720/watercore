package me.srrapero720.watercore.mixin;

import me.srrapero720.watercore.api.MCPlayerFormat;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.scores.PlayerTeam;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Collection;


// TODO: USES player#getName() instead self playername
@Mixin(value = Player.class, priority = 100)
public abstract class PlayerMixin extends LivingEntity {
    @Shadow @Final private Collection<MutableComponent> prefixes;
    @Shadow @Final private Collection<MutableComponent> suffixes;
    @Shadow private Component displayname;

    protected PlayerMixin(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) { super(p_20966_, p_20967_); }

    @Shadow public abstract @NotNull Component getName();
    @Shadow protected abstract MutableComponent decorateDisplayNameComponent(MutableComponent p_36219_);

    @Redirect(method = "getDisplayName", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/event/ForgeEventFactory;getPlayerDisplayName(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/network/chat/Component;)Lnet/minecraft/network/chat/Component;"))
    public Component getDisplayName(Player instance, Component value) {
        return MCPlayerFormat.createPlayerDisplayName((Player) (Object) this);
    }
}
