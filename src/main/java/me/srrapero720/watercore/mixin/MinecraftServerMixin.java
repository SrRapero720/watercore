package me.srrapero720.watercore.mixin;

import me.srrapero720.watercore.custom.data.LobbySpawnData;
import me.srrapero720.watercore.custom.data.WorldSpawnData;
import me.srrapero720.watercore.internal.WaterConsole;
import me.srrapero720.watercore.internal.WaterRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
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
    public void onRunningServer(CallbackInfo ci) {
        var lobbyData = LobbySpawnData.fetch((MinecraftServer) (Object) this);
        var spawnData = WorldSpawnData.fetch((MinecraftServer) (Object) this);

        if (lobbyData.isEmpty()) {
            WaterConsole.warn("MinecraftServerMixin", "No found any WorldSpawn pos. using default");
            lobbyData.setCords(levels.get(Level.OVERWORLD).getSharedSpawnPos(), 0, 0).save(new CompoundTag());
        }

        if (spawnData.isEmpty()) {
            WaterConsole.warn("MinecraftServerMixin", "No found any LobbySpawn pos. using default");
            spawnData.setCords(levels.get(Level.OVERWORLD).getSharedSpawnPos(), 0, 0).save(new CompoundTag());
        }

        var lobbyLevel = getLevel(WaterRegistry.findDimension("lobby"));
        var statelobby = lobbyLevel.getBlockState(new BlockPos(0, 128, 0));
        if (statelobby.is(Blocks.AIR)) {
            lobbyLevel.setBlock(new BlockPos(0, 128, 0), Blocks.BEDROCK.defaultBlockState(), 0);
        }
    }
}
