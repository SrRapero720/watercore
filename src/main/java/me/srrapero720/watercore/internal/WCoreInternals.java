package me.srrapero720.watercore.internal;

import com.mojang.brigadier.CommandDispatcher;
import io.netty.buffer.AbstractReferenceCountedByteBuf;
import me.srrapero720.watercore.WaterRegistry;
import me.srrapero720.watercore.api.thread.ThreadUtil;
import me.srrapero720.watercore.custom.data.PlayerSpawn;
import me.srrapero720.watercore.custom.data.storage.SimplePlayerStorage;
import me.srrapero720.watercore.mixin.client.util.ModelManagerAccessor;
import me.srrapero720.watercore.utility.Logg;
import me.srrapero720.watercore.utility.Tools;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundForgetLevelChunkPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.lighting.LevelLightEngine;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.injection.struct.InjectorGroupInfo;
import org.spongepowered.asm.mixin.transformer.ClassInfo;

import java.util.*;
import java.util.function.Predicate;

/**
 * This class is only designed to center every method on mixins right here
 * (keep mixins only to be mixins)
 * Please, if this class mess up with something in your code open an issue on Github
 * DO NOT USE METHODS FROM HERE... NEVER EVER. NEVER!!!
 */
public final class WCoreInternals {
    /* ====================================================
     *                BUFFERS MEMORY LEAK FIX
     * ==================================================== */
    private static final Predicate<FriendlyByteBuf> BUF_PREDICATE = (buffer) -> {
        if (buffer.source instanceof AbstractReferenceCountedByteBuf) return true;
        return buffer.refCnt() == 0 && buffer.release();
    };
    public static final Set<FriendlyByteBuf> BUFFERS = Collections.synchronizedSet(new HashSet<>());
    public static void buffers$add(FriendlyByteBuf byteBuf) { BUFFERS.add(byteBuf); }
    public static void buffers$flush() { BUFFERS.removeIf(BUF_PREDICATE); }


    /* ====================================================
     *                CORE LOADING COMMANDS
     * ==================================================== */
    public static void core$loadAllComands(CommandDispatcher<CommandSourceStack> dispatcher) {
//        for (var comm: GlobalRegistry.getCommandRegistry()) comm.load(dispatcher);
    }

    public static void core$prepareDimensionSetup(MinecraftServer server) {
        var lobby = PlayerSpawn.fetch(PlayerSpawn.Mode.LOBBY, server);
        var spawn = PlayerSpawn.fetch(PlayerSpawn.Mode.WORLD, server);

        if (lobby.invalid()) {
            Logg.warn("No found any WORLDSPAWN pos. using default worldspawn");
            lobby.setDimension(Level.OVERWORLD);
            lobby.setCoordinates(server.getLevel(Level.OVERWORLD).getSharedSpawnPos(), 0, 0);
        }

        if (spawn.invalid()) {
            Logg.warn("No found any LOBBYSPAWN pos. using default worldspawn");
            spawn.setDimension(Level.OVERWORLD);
            spawn.setCoordinates(server.getLevel(Level.OVERWORLD).getSharedSpawnPos(), 0, 0);
        }

        // TODO: INVESTIGATE HOW THE FUCK STOP DOING THIS
        var lobbyLevel = server.getLevel(WaterRegistry.getWorldDimension("lobby"));
        var statelobby = lobbyLevel.getBlockState(new BlockPos(0, 128, 0));
        if (statelobby.is(Blocks.AIR)) {
            lobbyLevel.setBlock(new BlockPos(0, 128, 1), Blocks.BEDROCK.defaultBlockState(), 0);
            lobbyLevel.setBlock(new BlockPos(0, 128, 0), Blocks.BEDROCK.defaultBlockState(), 0);
            lobbyLevel.setBlock(new BlockPos(0, 128, -1), Blocks.BEDROCK.defaultBlockState(), 0);
            lobbyLevel.setBlock(new BlockPos(1, 128, 1), Blocks.BEDROCK.defaultBlockState(), 0);
            lobbyLevel.setBlock(new BlockPos(1, 128, 0), Blocks.BEDROCK.defaultBlockState(), 0);
            lobbyLevel.setBlock(new BlockPos(1, 128, -1), Blocks.BEDROCK.defaultBlockState(), 0);
            lobbyLevel.setBlock(new BlockPos(-1, 128, 1), Blocks.BEDROCK.defaultBlockState(), 0);
            lobbyLevel.setBlock(new BlockPos(-1, 128, 0), Blocks.BEDROCK.defaultBlockState(), 0);
            lobbyLevel.setBlock(new BlockPos(-1, 128, -1), Blocks.BEDROCK.defaultBlockState(), 0);
        }
    }

    public static void core$onPlayerTeleport(ServerPlayer player) {
        SimplePlayerStorage.saveBackData(player);
    }

    /* ====================================================
     *             QUALITY OF LIFE FEATURES
     * ==================================================== */
    public static void qol$mipmapUpdate(Minecraft client, ModelManagerAccessor modelmanager) {
        var modelBakery = modelmanager.callPrepare(client.getResourceManager(), client.getProfiler());
        modelmanager.callApply(modelBakery, client.getResourceManager(), client.getProfiler());
    }

    /* ====================================================
     *             MEMORYLEAKS METHODS TO FIX IT
     * ==================================================== */
    @SuppressWarnings("unchecked")
    private static void leaks$flushClassInfo() throws NoSuchFieldException, IllegalAccessException, SecurityException {
        Logg.warn("Cleaning ClassInfo cache...");

        // Craftitrace ensure working
        if (Tools.isModFMLoading("craftitrace") || Tools.isModLoaded("craftitrace")) {
            Logg.error("Cleaning ClassInfo is disabled when craftitrace is installed (keeps all working together)");
            return;
        }

        var cField = ClassInfo.class.getDeclaredField("cache");
        cField.setAccessible(true);

        var cache = ((Map<String, ClassInfo>) cField.get(null));
        ClassInfo info = cache.get(Tools.OBJECT);
        cache.clear();
        cache.put(Tools.OBJECT, info);
    }

    public static void leaks$flushSpongePoweredMixinCache() {
        Logg.warn("Force-loading all classes with pending mixins and cleaning sponge cache");

        MixinEnvironment.getCurrentEnvironment().audit();
        ThreadUtil.trySimple(() -> {
            // GET FIELD "NO_GROUP" IN Map CLASS
            final var gField = InjectorGroupInfo.Map.class.getDeclaredField("NO_GROUP");
            gField.setAccessible(true);
            final var noGroup = gField.get(null);

            // GET FIELD "members" INSIDE "NO_GROUP" CLASS DEF
            final var membersField = noGroup.getClass().getDeclaredField("members");
            membersField.setAccessible(true);

            //CLEARS SPONGEPOWERED MIXIN CACHE
            ((List<?>) membersField.get(noGroup)).clear();
            leaks$flushClassInfo();
            Logg.log("All classes with mixins are loaded and flushed");
        }, ThreadUtil::printStackTrace);
    }

    public static void leaks$forgetChunkLightUpdate(ClientLevel clientLevel, ClientboundForgetLevelChunkPacket packet) {
        clientLevel.queueLightUpdate(() -> {
            LevelLightEngine levellightengine = clientLevel.getLightEngine();

            levellightengine.enableLightSources(new ChunkPos(packet.getX(), packet.getZ()), false);
            clientLevel.setLightReady(packet.getX(), packet.getZ());
        });
    }
}
