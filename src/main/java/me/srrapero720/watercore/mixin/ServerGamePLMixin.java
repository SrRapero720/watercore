package me.srrapero720.watercore.mixin;

import me.srrapero720.watercore.api.ChatDataProvider;
import me.srrapero720.watercore.internal.WaterConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundChatPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.network.TextFilter;
import net.minecraft.world.entity.player.ChatVisiblity;
import net.minecraftforge.common.ForgeHooks;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = ServerGamePacketListenerImpl.class, priority = 0)
public abstract class ServerGamePLMixin {
    @Shadow public ServerPlayer player;
    @Final @Shadow private MinecraftServer server;
    @Shadow private int chatSpamTickCount;

    @Shadow public abstract void disconnect(Component p_9943_);
    @Shadow protected abstract void handleCommand(String p_9958_);
    @Shadow public abstract void send(Packet<?> p_9830_);

    @Shadow protected abstract boolean isSingleplayerOwner();

//    @ModifyArg(method = "onDisconnect", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/PlayerList;broadcastMessage(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/ChatType;Ljava/util/UUID;)V"))
//    public Component modifyOnDisconnect(Component p_11265_) {
//        return ChatDataProvider.parse(WaterConfig.get("LEAVE_FORMAT"), player);
//    }

    @Shadow @Final private static Logger LOGGER;

    /**
     * @author SrRapero720
     * @reason MAGMA DO WEIRD STUFF
     */
    @Overwrite
    public void onDisconnect(Component component) {
        LOGGER.info("WATERCORE: {} lost connection: {}", this.player.getName().getString(), component.getString());
        this.server.invalidateStatus();
        this.server.getPlayerList().broadcastMessage(ChatDataProvider.parse(WaterConfig.get("LEAVE_FORMAT"), player), ChatType.SYSTEM, Util.NIL_UUID);
        this.player.disconnect();
        this.server.getPlayerList().remove(this.player);
        this.player.getTextFilter().leave();
        if (this.isSingleplayerOwner()) {
            LOGGER.info("WATERCORE: Stopping singleplayer server as player logged out");
            this.server.halt(false);
        }
    }

    /**
     * @author SrRapero720
     * @reason Yeah, I don't want to recode this shit when I ported it to fabric, so I made my own default impl but no
     * breaking forge hooks
     */
    @Overwrite
    private void handleChat(TextFilter.FilteredText text) {
        if (this.player.getChatVisibility() == ChatVisiblity.HIDDEN) {
            this.send(new ClientboundChatPacket((new TranslatableComponent("chat.disabled.options")).withStyle(ChatFormatting.RED), ChatType.SYSTEM, Util.NIL_UUID));
        } else {
            this.player.resetLastActionTime();
            var msg = text.getRaw();
            if (!msg.startsWith("/")) {
                var msgFiltered = text.getFiltered();
                var component = ChatDataProvider.parse(WaterConfig.get("CHAT_FORMAT"), player, msgFiltered.isEmpty() ? msgFiltered : msg);
                var event = ForgeHooks.onServerChatEvent((ServerGamePacketListenerImpl) (Object) this, msg, component, msgFiltered, component);

                if (event != null && event.getComponent() != null)
                    this.server.getPlayerList().broadcastMessage(event.getComponent(), (p_184197_) ->
                            this.player.shouldFilterMessageTo(p_184197_) ?
                            event.getFilteredComponent() : event.getComponent(), ChatType.CHAT, this.player.getUUID());
            } else this.handleCommand(msg);

            this.chatSpamTickCount += 20;
            if (this.chatSpamTickCount > 200 && !this.server.getPlayerList().isOp(this.player.getGameProfile())) {
                this.disconnect(new TranslatableComponent("disconnect.spam"));
            }
        }
    }
}
