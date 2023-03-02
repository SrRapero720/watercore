package me.srrapero720.watercore.mixin;


import com.mojang.authlib.GameProfile;
import com.mojang.serialization.Dynamic;
import io.netty.buffer.Unpooled;
import me.srrapero720.watercore.custom.data.LobbyData;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.players.PlayerList;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagNetworkSerialization;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeEventFactory;
import me.srrapero720.watercore.SrConsole;
import me.srrapero720.watercore.SrRegistry;
import me.srrapero720.watercore.SrUtil;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;

@Mixin(value = PlayerList.class, priority = 0)
public abstract class PlayerListMixin {

    @Shadow
    @Final
    private MinecraftServer server;

    @Shadow
    @Nullable
    public abstract CompoundTag load(ServerPlayer p_11225_);

    @Shadow
    @Final
    private static Logger LOGGER;

    @Shadow
    @Final
    private RegistryAccess.Frozen registryHolder;

    @Shadow
    public abstract int getMaxPlayers();

    @Shadow
    private int viewDistance;

    @Shadow
    private int simulationDistance;

    @Shadow
    public abstract void sendPlayerPermissionLevel(ServerPlayer p_11290_);

    @Shadow
    protected abstract void updateEntireScoreboard(ServerScoreboard p_11274_, ServerPlayer p_11275_);

    @Shadow
    public abstract void broadcastMessage(Component p_11265_, ChatType p_11266_, UUID p_11267_);

    @Shadow
    public abstract boolean addPlayer(ServerPlayer player);

    @Shadow
    @Final
    private Map<UUID, ServerPlayer> playersByUUID;

    @Shadow
    public abstract void broadcastAll(Packet<?> p_11269_);

    @Shadow
    public abstract void sendLevelInfo(ServerPlayer p_11230_, ServerLevel p_11231_);

    @Shadow
    @Final
    private List<ServerPlayer> players;

    @Shadow
    public abstract MinecraftServer getServer();

    @Inject(method = "broadcastMessage(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/ChatType;Ljava/util/UUID;)V", at = @At(value = "RETURN"))
    public void broadcast_3(Component p_11265_, ChatType p_11266_, UUID p_11267_, CallbackInfo ci) {
        SrConsole.log("Broadcast_3", "Running broadcast_3 with:" + p_11265_.getString());
    }

    @Inject(method = "broadcastMessage(Lnet/minecraft/network/chat/Component;Ljava/util/function/Function;Lnet/minecraft/network/chat/ChatType;Ljava/util/UUID;)V", at = @At(value = "RETURN"))
    public void broadcast_4(Component p_143992_, Function<ServerPlayer, Component> p_143993_, ChatType p_143994_, UUID p_143995_, CallbackInfo ci) {
        SrConsole.log("Broadcast_4", "Running broadcast_4 with: " + p_143992_.getString());
    }

    @Inject(method = "broadcastToTeam", at = @At(value = "RETURN"))
    public void broadcast_5(Player p_11250_, Component p_11251_, CallbackInfo ci) {
        SrConsole.log("Broadcast_5", "Running broadcast_5 with: " + p_11251_.getString());
    }


    @Inject(method = "broadcastToAllExceptTeam", at = @At(value = "RETURN"))
    public void broadcast_7(Player p_11250_, Component p_11251_, CallbackInfo ci) {
        SrConsole.log("Broadcast_7", "Running broadcast_7 with: " + p_11251_.getString());
    }

    /**
     * @author SrRapero720
     * @reason Some stuff needs to be changed in this silly function, sorry if broke things.
     */
    @Overwrite
    public void placeNewPlayer(Connection connection, ServerPlayer player) {
        var profile = player.getGameProfile();
        var profileCache = this.server.getProfileCache();

        var optional = profileCache.get(profile.getId());
        var s = optional.map(GameProfile::getName).orElse(profile.getName());
        profileCache.add(profile);

        var tag = this.load(player);
        final var levelRes = tag != null
                ? DimensionType.parseLegacy(new Dynamic<>(NbtOps.INSTANCE, tag.get("Dimension"))).resultOrPartial(SrConsole::justPrint).orElse(SrRegistry.dimension("LOBBY"))
                : SrRegistry.dimension("LOBBY");

        // If you remove or change the name of your dimension, you know the problem... but here no notify about it
        var levelResult = this.server.getLevel(levelRes);
        final var level = levelResult != null ? levelResult : this.server.overworld();

        player.setLevel(level);
        String s1 = "local";
        if (connection.getRemoteAddress() != null) {
            s1 = connection.getRemoteAddress().toString();
        }

        // No es necesario sobreescribir a SrConsole
        LOGGER.info("{}[{}] logged in with entity id {} at ({}, {}, {})", player.getName().getString(), s1, player.getId(), player.getX(), player.getY(), player.getZ());

        LevelData leveldata = level.getLevelData();
        player.loadGameTypes(tag);
        ServerGamePacketListenerImpl servergamepacketlistenerimpl = new ServerGamePacketListenerImpl(this.server, connection, player);
        net.minecraftforge.network.NetworkHooks.sendMCRegistryPackets(connection, "PLAY_TO_CLIENT");
        GameRules gamerules = leveldata.getGameRules();
        boolean flag = gamerules.getBoolean(GameRules.RULE_DO_IMMEDIATE_RESPAWN);
        boolean flag1 = gamerules.getBoolean(GameRules.RULE_REDUCEDDEBUGINFO);
        servergamepacketlistenerimpl.send(new ClientboundLoginPacket(player.getId(), leveldata.isHardcore(), player.gameMode.getGameModeForPlayer(), player.gameMode.getPreviousGameModeForPlayer(), this.server.levelKeys(), this.registryHolder, level.dimensionTypeRegistration(), level.dimension(), BiomeManager.obfuscateSeed(level.getSeed()), this.getMaxPlayers(), this.viewDistance, this.simulationDistance, flag1, !flag, level.isDebug(), level.isFlat()));
        servergamepacketlistenerimpl.send(new ClientboundCustomPayloadPacket(ClientboundCustomPayloadPacket.BRAND, (new FriendlyByteBuf(Unpooled.buffer())).writeUtf(this.getServer().getServerModName())));
        servergamepacketlistenerimpl.send(new ClientboundChangeDifficultyPacket(leveldata.getDifficulty(), leveldata.isDifficultyLocked()));
        servergamepacketlistenerimpl.send(new ClientboundPlayerAbilitiesPacket(player.getAbilities()));
        servergamepacketlistenerimpl.send(new ClientboundSetCarriedItemPacket(player.getInventory().selected));
        MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.OnDatapackSyncEvent((PlayerList) (Object) this, player));
        servergamepacketlistenerimpl.send(new ClientboundUpdateRecipesPacket(this.server.getRecipeManager().getRecipes()));
        servergamepacketlistenerimpl.send(new ClientboundUpdateTagsPacket(TagNetworkSerialization.serializeTagsToNetwork(this.registryHolder)));
        this.sendPlayerPermissionLevel(player);
        player.getStats().markAllDirty();
        player.getRecipeBook().sendInitialRecipeBook(player);
        this.updateEntireScoreboard(level.getScoreboard(), player);
        this.server.invalidateStatus();


        // CHANGED FOR WATERCORE
        var component = SrUtil.createJoinMessage(player.getDisplayName().getString(), s);
        this.broadcastMessage(component, ChatType.SYSTEM, Util.NIL_UUID);

        int timePlayed = player.getStats().getValue(Stats.CUSTOM.get(Stats.PLAY_TIME));
        if (timePlayed != 0) {
            servergamepacketlistenerimpl.teleport(player.getX(), player.getY(), player.getZ(), player.getYRot(), player.getXRot());
        } else {
            var lobbyFetch = LobbyData.fetch(server);
            final var cords = lobbyFetch.getCords();
            final var rot = lobbyFetch.getRotation();
            final var mPos = cords != null ?  new BlockPos(cords[0], cords[1], cords[2]) : new BlockPos(0, 128, 0);
            servergamepacketlistenerimpl.teleport(mPos.getX(), mPos.getY() + 1, mPos.getZ(), rot[0], rot[1]);
        }

        this.addPlayer(player);
        this.playersByUUID.put(player.getUUID(), player);
        this.broadcastAll(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.ADD_PLAYER, player));

        for (int i = 0; i < this.players.size(); ++i) {
            player.connection.send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.ADD_PLAYER, this.players.get(i)));
        }

        level.addNewPlayer(player);
        this.server.getCustomBossEvents().onPlayerConnect(player);
        this.sendLevelInfo(player, level);
        if (!this.server.getResourcePack().isEmpty()) {
            player.sendTexturePack(this.server.getResourcePack(), this.server.getResourcePackHash(), this.server.isResourcePackRequired(), this.server.getResourcePackPrompt());
        }

        for (MobEffectInstance mobeffectinstance : player.getActiveEffects()) {
            servergamepacketlistenerimpl.send(new ClientboundUpdateMobEffectPacket(player.getId(), mobeffectinstance));
        }

        if (tag != null && tag.contains("RootVehicle", 10)) {
            CompoundTag compoundtag1 = tag.getCompound("RootVehicle");
            ServerLevel finalLevel = level;
            Entity entity1 = EntityType.loadEntityRecursive(compoundtag1.getCompound("Entity"), level, (p_11223_) ->
                    !finalLevel.addWithUUID(p_11223_) ? null : p_11223_);
            if (entity1 != null) {
                UUID uuid;
                if (compoundtag1.hasUUID("Attach")) {
                    uuid = compoundtag1.getUUID("Attach");
                } else {
                    uuid = null;
                }

                if (entity1.getUUID().equals(uuid)) {
                    player.startRiding(entity1, true);
                } else {
                    for (Entity entity : entity1.getIndirectPassengers()) {
                        if (entity.getUUID().equals(uuid)) {
                            player.startRiding(entity, true);
                            break;
                        }
                    }
                }

                if (!player.isPassenger()) {
                    LOGGER.warn("Couldn't reattach entity to player");
                    entity1.discard();

                    for (Entity entity2 : entity1.getIndirectPassengers()) {
                        entity2.discard();
                    }
                }
            }
        }

        player.initInventoryMenu();
        ForgeEventFactory.firePlayerLoggedIn(player);
    }


    /**
     * @author SrRapero720
     * @reason Mainly reason I do that is for performance, to 2 teleports on ForgeEvents become in a little stun :)
     * also, code is shit and I want to rewrite it
     */
    @Overwrite
    public ServerPlayer respawn(ServerPlayer p_11237_, boolean p_11238_) {
        this.players.remove(p_11237_);
        p_11237_.getLevel().removePlayerImmediately(p_11237_, Entity.RemovalReason.DISCARDED);
        BlockPos blockpos = p_11237_.getRespawnPosition();
        float f = p_11237_.getRespawnAngle();
        boolean flag = p_11237_.isRespawnForced();
        ServerLevel serverlevel = this.server.getLevel(p_11237_.getRespawnDimension());
        Optional<Vec3> optional;
        if (serverlevel != null && blockpos != null) {
            optional = Player.findRespawnPositionAndUseSpawnBlock(serverlevel, blockpos, f, flag, p_11238_);
        } else {
            optional = Optional.empty();
        }

        ServerLevel serverlevel1 = serverlevel != null && optional.isPresent() ? serverlevel : this.server.getLevel(SrRegistry.dimension("LOBBY"));
        ServerPlayer serverplayer = new ServerPlayer(this.server, serverlevel1, p_11237_.getGameProfile());
        serverplayer.connection = p_11237_.connection;
        serverplayer.restoreFrom(p_11237_, p_11238_);
        serverplayer.setId(p_11237_.getId());
        serverplayer.setMainArm(p_11237_.getMainArm());

        for(String s : p_11237_.getTags()) {
            serverplayer.addTag(s);
        }

        boolean flag2 = false;
        if (optional.isPresent()) {
            BlockState blockstate = serverlevel1.getBlockState(blockpos);
            boolean flag1 = blockstate.is(Blocks.RESPAWN_ANCHOR);
            Vec3 vec3 = optional.get();
            float f1;
            if (!blockstate.is(BlockTags.BEDS) && !flag1) {
                f1 = f;
            } else {
                Vec3 vec31 = Vec3.atBottomCenterOf(blockpos).subtract(vec3).normalize();
                f1 = (float) Mth.wrapDegrees(Mth.atan2(vec31.z, vec31.x) * (double)(180F / (float)Math.PI) - 90.0D);
            }

            serverplayer.moveTo(vec3.x, vec3.y, vec3.z, f1, 0.0F);
            serverplayer.setRespawnPosition(serverlevel1.dimension(), blockpos, f, flag, false);
            flag2 = !p_11238_ && flag1;
        } else {
            var dataFetch = LobbyData.fetch(server);
            var cords = dataFetch.getCords();
            var mPos = cords != null ?  new BlockPos(cords[0], cords[1], cords[2]) : new BlockPos(0, 128, 0);
            var mRot = dataFetch.getRotation();

            serverplayer.setPos(mPos.getX(), mPos.getY(), mPos.getZ());
            serverplayer.setYRot(mRot[0]);
            serverplayer.setXRot(mRot[1]);
            serverplayer.setRespawnPosition(serverlevel1.dimension(), mPos, mRot[0], flag, false);

            if (blockpos != null) {
                serverplayer.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.NO_RESPAWN_BLOCK_AVAILABLE, 0.0F));
            }
        }



        while(!serverlevel1.noCollision(serverplayer) && serverplayer.getY() < (double)serverlevel1.getMaxBuildHeight()) {
            serverplayer.setPos(serverplayer.getX(), serverplayer.getY() + 1.0D, serverplayer.getZ());
        }

        LevelData leveldata = serverplayer.level.getLevelData();
        serverplayer.connection.send(new ClientboundRespawnPacket(serverplayer.level.dimensionTypeRegistration(), serverplayer.level.dimension(), BiomeManager.obfuscateSeed(serverplayer.getLevel().getSeed()), serverplayer.gameMode.getGameModeForPlayer(), serverplayer.gameMode.getPreviousGameModeForPlayer(), serverplayer.getLevel().isDebug(), serverplayer.getLevel().isFlat(), p_11238_));
        serverplayer.connection.teleport(serverplayer.getX(), serverplayer.getY(), serverplayer.getZ(), serverplayer.getYRot(), serverplayer.getXRot());
        serverplayer.connection.send(new ClientboundSetDefaultSpawnPositionPacket(serverlevel1.getSharedSpawnPos(), serverlevel1.getSharedSpawnAngle()));
        serverplayer.connection.send(new ClientboundChangeDifficultyPacket(leveldata.getDifficulty(), leveldata.isDifficultyLocked()));
        serverplayer.connection.send(new ClientboundSetExperiencePacket(serverplayer.experienceProgress, serverplayer.totalExperience, serverplayer.experienceLevel));
        this.sendLevelInfo(serverplayer, serverlevel1);
        this.sendPlayerPermissionLevel(serverplayer);
        serverlevel1.addRespawnedPlayer(serverplayer);
        this.addPlayer(serverplayer);
        this.playersByUUID.put(serverplayer.getUUID(), serverplayer);
        serverplayer.initInventoryMenu();
        serverplayer.setHealth(serverplayer.getHealth());
        net.minecraftforge.event.ForgeEventFactory.firePlayerRespawnEvent(serverplayer, p_11238_);
        if (flag2) {
            serverplayer.connection.send(new ClientboundSoundPacket(SoundEvents.RESPAWN_ANCHOR_DEPLETE, SoundSource.BLOCKS, (double)blockpos.getX(), (double)blockpos.getY(), (double)blockpos.getZ(), 1.0F, 1.0F));
        }

        return serverplayer;
    }
}
