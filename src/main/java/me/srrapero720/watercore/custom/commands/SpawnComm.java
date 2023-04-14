package me.srrapero720.watercore.custom.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.srrapero720.watercore.custom.data.PlayerSpawn;
import me.srrapero720.watercore.internal.WRegistry;
import me.srrapero720.watercore.internal.WUtil;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import org.jetbrains.annotations.NotNull;

public class SpawnComm {
    public static void register(@NotNull CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("spawn").executes(SpawnComm::teleportPlayer));
        dispatcher.register(Commands.literal("lobby").executes(SpawnComm::teleportPlayer));
    }

    public static int teleportPlayerToLobby(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        var player = context.getSource().getPlayerOrException();
        var level = player.server.getLevel(WRegistry.findDimension("lobby"));
        player.teleportTo(level, 0, 128, 0, 0, 0);
        return 0;
    }

    public static int teleportPlayer(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        var player = context.getSource().getPlayerOrException();

        var spawn = PlayerSpawn.fetch(PlayerSpawn.Mode.LOBBY, player.server);
        var result = WUtil.fetchLevel(player.server.getAllLevels(), spawn.getDimension());

        player.teleportTo(result, spawn.getX(), spawn.getY(), spawn.getZ(), spawn.getRotY(), spawn.getRotX());
        return 0;
    }
}
