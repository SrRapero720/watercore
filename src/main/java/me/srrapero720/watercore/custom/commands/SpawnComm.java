package me.srrapero720.watercore.custom.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.srrapero720.watercore.internal.WaterRegistry;
import me.srrapero720.watercore.internal.WaterUtil;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import me.srrapero720.watercore.custom.data.LobbySpawnData;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.NotNull;

public class SpawnComm {
    public static void register(@NotNull CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("spawn").executes(SpawnComm::teleportPlayer));
        dispatcher.register(Commands.literal("lobby").executes(SpawnComm::teleportPlayer));
    }

    public static int teleportPlayerToLobby(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        var player = context.getSource().getPlayerOrException();
        player.teleportTo(player.server.getLevel(WaterRegistry.findDimension("lobby")), 0, 128, 0, 0, 0);
        return 0;
    }

    public static int teleportPlayer(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        var player = context.getSource().getPlayerOrException();

        var dimData = LobbySpawnData.fetch(player.server);
        var server = player.server;

        var cords = dimData.getCords();
        var rot = dimData.getRotation();
        var level = dimData.getDimension();
        var mPos = new BlockPos(cords[0], cords[1], cords[2]);

        ServerLevel result = WaterUtil.findLevel(server.getAllLevels(), level);

        player.teleportTo(result == null ? server.getLevel(WaterRegistry.findDimension("lobby")) : result, mPos.getX(), mPos.getY(), mPos.getZ(), rot[0], rot[1]);
        return 0;
    }
}
