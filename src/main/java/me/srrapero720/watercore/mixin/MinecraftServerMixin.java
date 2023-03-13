package me.srrapero720.watercore.mixin;

import me.srrapero720.watercore.custom.data.LobbyData;
import me.srrapero720.watercore.internal.WaterConsole;
import me.srrapero720.watercore.internal.WaterRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.Map;

@Mixin(value = MinecraftServer.class, priority = 0)
public abstract class MinecraftServerMixin {
    @Shadow @Final private Map<ResourceKey<Level>, ServerLevel> levels;

    @Shadow @Nullable public abstract ServerLevel getLevel(ResourceKey<Level> p_129881_);

    @Inject(method = "runServer()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;updateStatusIcon(Lnet/minecraft/network/protocol/status/ServerStatus;)V"))
    public void onRunningServer(CallbackInfo ci) {
        var lobbyData = LobbyData.fetch((MinecraftServer) (Object) this);
        if (lobbyData.isEmpty()) {
            WaterConsole.warn("MinecraftServerMixin", "WATERCoRE no found any spawn pos. using default");
            var currentLevel = levels.get(Level.OVERWORLD);
            lobbyData.setCords(currentLevel.getSharedSpawnPos(), 0, 0).save(new CompoundTag());
        }

        var lobbyLevel = getLevel(WaterRegistry.findDimension("lobby"));
        var statelobby = lobbyLevel.getBlockState(new BlockPos(0, 128, 0));
        if (statelobby.is(Blocks.AIR)) {
            lobbyLevel.setBlock(new BlockPos(0, 128, 0), Blocks.AIR.defaultBlockState(), 0);
        }
    }
}
