package me.srrapero720.watercore.mixin;

import me.srrapero720.watercore.api.MCPlayerFormat;
import me.srrapero720.watercore.internal.WaterConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.network.TextFilter;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ServerChatEvent;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(value = ServerGamePacketListenerImpl.class, priority = 72)
public abstract class ServerGamePLMixin {
    @Shadow public ServerPlayer player;


    @ModifyArg(method = "onDisconnect", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/PlayerList;broadcastMessage(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/ChatType;Ljava/util/UUID;)V"))
    public Component modifyOnDisconnect(Component p_11265_) {
        return MCPlayerFormat.parse(WaterConfig.get("LEAVE_FORMAT"), player);
    }

    @Redirect(method = "handleChat(Lnet/minecraft/server/network/TextFilter$FilteredText;)V", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraftforge/common/ForgeHooks;onServerChatEvent(Lnet/minecraft/server/network/ServerGamePacketListenerImpl;Ljava/lang/String;Lnet/minecraft/network/chat/Component;Ljava/lang/String;Lnet/minecraft/network/chat/Component;)Lnet/minecraftforge/event/ServerChatEvent;"
    ))
    public ServerChatEvent redirectHandleChatForgeHooks(ServerGamePacketListenerImpl net, String raw, Component comp, String filtered, Component filteredComp) {
        return ForgeHooks.onServerChatEvent(
                net,
                raw,
                MCPlayerFormat.parse(WaterConfig.get("CHAT_FORMAT"), player),
                filtered,
                filtered.isEmpty() ? null : MCPlayerFormat.parse(WaterConfig.get("CHAT_FORMAT"), player, filtered)
        );
    }
}
