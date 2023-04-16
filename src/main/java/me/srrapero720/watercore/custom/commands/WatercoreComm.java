package me.srrapero720.watercore.custom.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import me.srrapero720.watercore.api.thread.ThreadUtil;
import me.srrapero720.watercore.internal.WUtil;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import org.jetbrains.annotations.NotNull;

public class WatercoreComm {
    public static void register(@NotNull CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("watercore").requires((c) -> c.hasPermission(3))
                //TELEPORT TO LOBBY DIMENSION
                .then(Commands.literal("lobby").executes(SpawnComm::teleportPlayerToLobby))
                .then(Commands.literal("spawn").executes(SpawnComm::teleportPlayerToLobby))

                //TELEPORT ADMIN TO OTHER PLAYER'S BACK HISTORY
                .then(Commands.literal("back")
                        .then(Commands.argument("player", EntityArgument.players()).executes(BackComm::backWithoutIndexAndArgPlayer))
                        .then(Commands.argument("index", IntegerArgumentType.integer(0, 10)).executes(BackComm::backArgPlayer)))

                // THREAD UTIL
                .then(Commands.literal("threads")
                        .then(Commands.literal("logger").then(Commands.literal("toggle").executes(WatercoreComm::threadLoggerExecution)))
                        .then(Commands.literal("list").executes(WatercoreComm::threadListExecution))
                ));
    }

    public static int threadLoggerExecution(CommandContext<CommandSourceStack> context) {
        if (ThreadUtil.threadLoggerEnabled()) ThreadUtil.threadLoggerKill();
        else ThreadUtil.threadLogger();
        context.getSource().sendSuccess(new TranslatableComponent("wc.command.watercore.threads.logger", WUtil.broadcastPrefix()), false);
        return 0;
    }

    public static int threadListExecution(CommandContext<CommandSourceStack> context) {
        ThreadUtil.showThreads();
        context.getSource().sendSuccess(new TranslatableComponent("wc.command.watercore.threads.list", WUtil.broadcastPrefix()), false);
        return 0;
    }
}
