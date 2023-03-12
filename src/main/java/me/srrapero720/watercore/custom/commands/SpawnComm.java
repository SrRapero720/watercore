package me.srrapero720.watercore.custom.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.srrapero720.watercore.water.WaterRegistry;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import me.srrapero720.watercore.custom.data.LobbyData;
import org.jetbrains.annotations.NotNull;

public class SpawnComm {
    public static void register(@NotNull CommandDispatcher<CommandSourceStack> dispatcher) {
        var first=
                dispatcher.register(Commands.literal("spawn").executes(SpawnComm::teleportPlayer));

        dispatcher.register(Commands.literal("lobby").redirect(first));
    }

    public static int teleportPlayer(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        var player = context.getSource().getPlayerOrException();

        var dimData = LobbyData.fetch(player.server);
        var server = context.getSource().getServer();

        var cords = dimData.getCords();
        var rot = dimData.getRotation();
        var mPos = new BlockPos(cords[0], cords[1], cords[2]);

        player.teleportTo(server.getLevel(WaterRegistry.dimension("LOBBY")), mPos.getX(), mPos.getY() + 1, mPos.getZ(), rot[0], rot[1]);
        return 0;
    }
}
