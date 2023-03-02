package me.srrapero720.watercore.custom.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import org.jetbrains.annotations.NotNull;

public class BackComm {
    public static void register(@NotNull CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("back").executes(context -> {
            return 0;
        }).then(Commands.argument("player", EntityArgument.players())).executes(context -> {
            return 0;
        }));
    }

    private static int findPlayer() {
        return 0;
    }


    private static int teleport() {
        return 1;
    }
}
