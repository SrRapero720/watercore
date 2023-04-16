package me.srrapero720.watercore.mixin;

import me.srrapero720.watercore.api.ego.PlayerName;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;


@Mixin(value = Player.class, priority = 72)
public class PlayerMixin {
    @Shadow
    public @NotNull Component getName() { throw new IllegalCallerException("Uhhh, how you do this crash?"); }

    @Redirect(method = "getDisplayName", at = @At(value = "INVOKE", remap = false, target = "Lnet/minecraftforge/event/ForgeEventFactory;getPlayerDisplayName(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/network/chat/Component;)Lnet/minecraft/network/chat/Component;"))
    public Component redirectGetDisplayName(Player instance, Component value) {
        return PlayerName.displayname((Player) (Object) this);
    }
}
