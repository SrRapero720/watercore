package me.srrapero720.watercore.mixin;

import com.mojang.serialization.Dynamic;
import me.srrapero720.watercore.api.ego.PlayerName;
import me.srrapero720.watercore.custom.data.PlayerSpawn;
import me.srrapero720.watercore.custom.data.storage.SimplePlayerStorage;
import me.srrapero720.watercore.internal.WConsole;
import me.srrapero720.watercore.internal.WUtil;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.players.PlayerList;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.*;

@Mixin(value = PlayerList.class, priority = 72)
public abstract class PlayerListMixin {
    @Shadow @Final private MinecraftServer server;
    @Shadow @Nullable public abstract CompoundTag load(ServerPlayer p_11225_);
    @Shadow public abstract void broadcastMessage(Component p_11265_, ChatType p_11266_, UUID p_11267_);
    @Shadow public abstract MinecraftServer getServer();
    @Unique private final HashMap<String, ServerPlayer> playersByNameWC = new HashMap<>();

    @Inject(method = "addPlayer", at = @At(value = "TAIL"), remap = false)
    public void injectAddPlayer(ServerPlayer player, CallbackInfoReturnable<Boolean> cir) {
        playersByNameWC.put(player.getName().getString().toLowerCase(), player);
    }

    @Inject(method = "removePlayer", at = @At(value = "TAIL"), remap = false)
    public void injectRemovePlayer(@NotNull ServerPlayer player, CallbackInfoReturnable<Boolean> cir) {
        playersByNameWC.remove(player.getName().getString().toLowerCase());
    }

    @Inject(method = "getPlayerByName", at = @At("HEAD"), cancellable = true)
    public void injectGetPlayerByName(@NotNull String name, @NotNull CallbackInfoReturnable<ServerPlayer> cir) {
        cir.setReturnValue(playersByNameWC.get(name.toLowerCase()));
    }

    @Inject(method = "remove", at = @At(value = "HEAD"))
    public void injectorRemove(@NotNull ServerPlayer player, CallbackInfo ci) {
        playersByNameWC.remove(player.getName().getString().toLowerCase());
    }

    @Inject(method = "getPlayersWithAddress", at = @At(value = "HEAD"), cancellable = true)
    public void injectGetPlayersWithAddress(String address, @NotNull CallbackInfoReturnable<List<ServerPlayer>> cir) {
        var playerArray = new LinkedList<>(playersByNameWC.values());
        playerArray.removeIf(serverPlayer -> !serverPlayer.getIpAddress().equals(address));
        cir.setReturnValue(playerArray);
    }

    @Inject(method = "getPlayerNamesArray", at = @At(value = "HEAD"), cancellable = true)
    public void injectGetPlayerNamesArray(@NotNull CallbackInfoReturnable<String[]> cir) {
        cir.setReturnValue(playersByNameWC.keySet().toArray(new String[0]));
    }

    @Inject(method = "getPlayerCount", at = @At(value = "HEAD"), cancellable = true)
    public void injectGetPlayerCount(@NotNull CallbackInfoReturnable<Integer> cir) { cir.setReturnValue(playersByNameWC.size()); }

    @ModifyVariable(method = "placeNewPlayer", at = @At(value = "STORE"))
    public ResourceKey<Level> injectLocalPlaceNewPlayer(ResourceKey<Level> levelResourceKey, Connection connection, ServerPlayer player) {
        var tag = this.load(player);
        var spawnData = PlayerSpawn.fetch(PlayerSpawn.Mode.WORLD, server);
        var spawnLevel = WUtil.fetchLevel(server.getAllLevels(), spawnData.getDimension());
        var spawnLevelRes = spawnLevel == null ? Level.OVERWORLD : spawnLevel.dimension();

        return tag != null
                ? DimensionType.parseLegacy(new Dynamic<>(NbtOps.INSTANCE, tag.get("Dimension"))).resultOrPartial(WConsole::justPrint).orElse(spawnLevelRes)
                : spawnLevelRes;
    }

    @Redirect(method = "placeNewPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;teleport(DDDFF)V"))
    public void redirectPlaceNewPlayerTeleport(ServerGamePacketListenerImpl packet, double p_9775_, double p_9776_, double p_9777_, float p_9778_, float p_9779_, Connection connection, ServerPlayer player) {
        var spawnData = PlayerSpawn.fetch(PlayerSpawn.Mode.WORLD, server);
        int timePlayed = player.getStats().getValue(Stats.CUSTOM.get(Stats.PLAY_TIME));

        if (timePlayed == 0) {
            final var mPos = new BlockPos(spawnData.getX(), spawnData.getY(), spawnData.getZ());
            packet.teleport(mPos.getX(), mPos.getY() + 1, mPos.getZ(), spawnData.getRotY(), spawnData.getRotX());
        } else packet.teleport(player.getX(), player.getY(), player.getZ(), player.getYRot(), player.getXRot());
    }

    @Inject(method = "placeNewPlayer", at = @At(value = "INVOKE", remap = false, target = "Lnet/minecraft/server/players/PlayerList;addPlayer(Lnet/minecraft/server/level/ServerPlayer;)Z"))
    public void injectPlaceNewPlayerPutPlayer(Connection connection, ServerPlayer player, CallbackInfo ci) {
        playersByNameWC.put(player.getName().getString().toLowerCase(), player);
    }

    @Redirect(method = "placeNewPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/PlayerList;broadcastMessage(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/ChatType;Ljava/util/UUID;)V"))
    public void redirectBroadcastMessage(PlayerList list, Component obsolete, ChatType type, UUID uuid, Connection connection, ServerPlayer player) {
        var component = PlayerName.format(PlayerName.Format.JOIN, player);
        this.broadcastMessage(component, ChatType.SYSTEM, Util.NIL_UUID);
        if (player.level.isClientSide()) player.sendMessage(component, ChatType.SYSTEM, Util.NIL_UUID);
    }


    @Inject(method = "placeNewPlayer", at = @At("TAIL"))
    public void injectTailPlaceNewPlayer(Connection connection, ServerPlayer player, CallbackInfo ci) {
        if (player.level.isClientSide())
            player.sendMessage(PlayerName.format(PlayerName.Format.JOIN, player), ChatType.SYSTEM, Util.NIL_UUID);
    }


    /*
     * RESPAWN MIXINS
     * THIS MIXINS ARE REQUIRED TO RELOCATED SPAWN DATA
     */
    @Inject(method = "respawn", at = @At("HEAD"))
    public void injectRespawnHead(ServerPlayer player, boolean isBooleanic, CallbackInfoReturnable<ServerPlayer> cir) {
        SimplePlayerStorage.saveBackData(player);
    }

    @ModifyVariable(method = "respawn", ordinal = 1, at = @At(value = "STORE"))
    public ServerLevel modifyRespawnServerLevel(ServerLevel instance, @NotNull ServerPlayer player, boolean isBooleanic) {
        var respawnPos = player.getRespawnPosition();
        var respawnAngle = player.getRespawnAngle();
        var respawnForce = player.isRespawnForced();

        // WATERCORE LOBBY DATA
        var lobbyData = PlayerSpawn.fetch(PlayerSpawn.Mode.LOBBY, server);
        var lobbyLevel = WUtil.fetchLevel(server.getAllLevels(), lobbyData.getDimension());

        var level = this.server.getLevel(player.getRespawnDimension());
        Optional<Vec3> optional = (level != null && respawnPos != null)
                ? Player.findRespawnPositionAndUseSpawnBlock(level, respawnPos, respawnAngle, respawnForce, isBooleanic)
                : Optional.empty();

        return level != null && optional.isPresent() ? level : (lobbyLevel == null ? this.server.getLevel(Level.OVERWORLD) : lobbyLevel.getLevel());
    }


    @Redirect(method = "respawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;setMainArm(Lnet/minecraft/world/entity/HumanoidArm;)V"))
    public void injectRespawnIsPresent(ServerPlayer freshPlayer, HumanoidArm humanoidArm, ServerPlayer oldPlayer, boolean isBooleanic) {
        var level = this.server.getLevel(oldPlayer.getRespawnDimension());
        var optional = (level != null && oldPlayer.getRespawnPosition() != null)
                ? Player.findRespawnPositionAndUseSpawnBlock(level, oldPlayer.getRespawnPosition(), oldPlayer.getRespawnAngle(), oldPlayer.isRespawnForced(), isBooleanic)
                : Optional.empty();

        if (optional.isPresent()) return;

        var lobbyData = PlayerSpawn.fetch(PlayerSpawn.Mode.LOBBY, server);
        var lobbyLevel = WUtil.fetchLevel(server.getAllLevels(), lobbyData.getDimension());

        freshPlayer.setPos(lobbyData.getX(), lobbyData.getY(), lobbyData.getZ());
        freshPlayer.setXRot(lobbyData.getRotX());
        freshPlayer.setYRot(lobbyData.getRotY());
        freshPlayer.setRespawnPosition(lobbyLevel.dimension(),
                new BlockPos(lobbyData.getX(), lobbyData.getY(), lobbyData.getZ()), lobbyData.getRotY(), oldPlayer.isRespawnForced(), false);
    }

    @Redirect(method = "respawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;setHealth(F)V"))
    public void injectTailRespawn(ServerPlayer instance, float v) {
        this.playersByNameWC.put(instance.getName().getString().toLowerCase(), instance);
        instance.setHealth(v);
    }
}
