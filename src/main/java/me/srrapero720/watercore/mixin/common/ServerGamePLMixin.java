package me.srrapero720.watercore.mixin.common;

import me.srrapero720.watercore.internal.PlayerName;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ServerChatEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = ServerGamePacketListenerImpl.class, priority = 72)
public class ServerGamePLMixin {
    @Shadow public ServerPlayer player;


    @ModifyArg(method = "onDisconnect", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/PlayerList;broadcastMessage(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/ChatType;Ljava/util/UUID;)V"))
    public Component modifyOnDisconnectArgument(Component p_11265_) {
        return PlayerName.format(PlayerName.Format.LEAVE, player);
    }

    // FORCES TO FORGE USES WATERCORE FORMAT OVER OTHER MODS
    @Redirect(method = "handleChat(Lnet/minecraft/server/network/TextFilter$FilteredText;)V", at = @At(
            value = "INVOKE",
            remap = false,
            target = "Lnet/minecraftforge/common/ForgeHooks;onServerChatEvent(Lnet/minecraft/server/network/ServerGamePacketListenerImpl;Ljava/lang/String;Lnet/minecraft/network/chat/Component;Ljava/lang/String;Lnet/minecraft/network/chat/Component;)Lnet/minecraftforge/event/ServerChatEvent;"
    ))
    public ServerChatEvent redirectHandleChatForgeHooks(ServerGamePacketListenerImpl net, String raw, Component comp, String filtered, Component filteredComp) {
        return ForgeHooks.onServerChatEvent(
                net,
                raw,
                PlayerName.format(PlayerName.Format.CHAT, player, raw),
                filtered,
                filtered.isEmpty() ? null : PlayerName.format(PlayerName.Format.CHAT, player, filtered)
        );
    }
}
