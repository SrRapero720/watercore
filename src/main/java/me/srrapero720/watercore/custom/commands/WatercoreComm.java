package me.srrapero720.watercore.custom.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import org.jetbrains.annotations.NotNull;

public class WatercoreComm {
    public static void register(@NotNull CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("watercore").requires((c) -> c.hasPermission(3))
                .then(Commands.literal("lobby").executes(SpawnComm::teleportPlayerToLobby)));
    }
}
