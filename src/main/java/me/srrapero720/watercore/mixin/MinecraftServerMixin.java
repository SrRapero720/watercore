package me.srrapero720.watercore.mixin;

import me.srrapero720.watercore.custom.data.PlayerSpawn;
import me.srrapero720.watercore.internal.WConsole;
import me.srrapero720.watercore.internal.WCoreRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.Map;

@Mixin(value = MinecraftServer.class)
public abstract class MinecraftServerMixin {
    @Shadow @Final private Map<ResourceKey<Level>, ServerLevel> levels;
    @Shadow @Nullable public abstract ServerLevel getLevel(ResourceKey<Level> p_129881_);

    @Inject(method = "runServer()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;updateStatusIcon(Lnet/minecraft/network/protocol/status/ServerStatus;)V"))
    public void injectRunServer(CallbackInfo ci) {
        var lobby = PlayerSpawn.fetch(PlayerSpawn.Mode.LOBBY, (MinecraftServer) (Object) this);
        var spawn = PlayerSpawn.fetch(PlayerSpawn.Mode.WORLD, (MinecraftServer) (Object) this);

        if (lobby.invalid()) {
            WConsole.warn("MinecraftServer", "No found any WorldSpawn pos. using default");
            lobby.setDimension(Level.OVERWORLD);
            lobby.setCoordinates(levels.get(Level.OVERWORLD).getSharedSpawnPos(), 0, 0);
        }

        if (spawn.invalid()) {
            WConsole.warn("MinecraftServer", "No found any LobbySpawn pos. using default");
            spawn.setDimension(Level.OVERWORLD);
            spawn.setCoordinates(levels.get(Level.OVERWORLD).getSharedSpawnPos(), 0, 0);
        }

        var lobbyLevel = getLevel(WCoreRegistry.findDimension("lobby"));
        var statelobby = lobbyLevel.getBlockState(new BlockPos(0, 128, 0));
        if (statelobby.is(Blocks.AIR))
            lobbyLevel.setBlock(new BlockPos(0, 128, 0), Blocks.BEDROCK.defaultBlockState(), 0);
    }
}
