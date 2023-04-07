package me.srrapero720.watercore.mixin.external;

import me.srrapero720.watercore.api.MCPlayerFormat;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import redstonedubstep.mods.vanishmod.VanishUtil;

import java.util.List;
import java.util.UUID;

@Mixin(value = VanishUtil.class, priority = 72)
public class VM423535_VanishUtil {

    @Redirect(method = "sendJoinOrLeaveMessageToPlayers", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;sendMessage(Lnet/minecraft/network/chat/Component;Ljava/util/UUID;)V"))
    private static void redirectSendJoinOrLeaveNewComponent(ServerPlayer instance, Component p_9144_, UUID p_9145_, List<ServerPlayer> playerList, ServerPlayer sender, boolean leaveMessage, boolean beforeStatusChange) {
        instance.sendMessage(MCPlayerFormat.format(leaveMessage ? MCPlayerFormat.Format.LEAVE : MCPlayerFormat.Format.JOIN, instance), sender.getUUID());
    }
}
