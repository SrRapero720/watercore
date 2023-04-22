package me.srrapero720.watercore.custom.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.srrapero720.watercore.api.luckperms.LuckyMeta;
import me.srrapero720.watercore.api.thread.ThreadUtil;
import me.srrapero720.watercore.internal.WUtil;
import me.srrapero720.watercore.internal.forge.W$SConfig;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

public class WatercoreComm extends AbstractComm {
    public WatercoreComm(CommandDispatcher<CommandSourceStack> dispatcher) {
        super(dispatcher);

        // COMMAND REGISTER
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
                )
                // TEST FOR LUCKPERMS
                .then(Commands.literal("luckperms-bench")
                        .executes(WatercoreComm::runTest))
        );
    }

    //========================================== //
    //    UTILITY REQUIRED FOR THAT COMMAND
    //========================================== //
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

    public static int runTest(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        context.getSource().sendSuccess(new TextComponent(WUtil.broadcastPrefix().concat("Running...")), true);
        var player = context.getSource().getPlayerOrException();

        // BENCH 1
        var metaValue = LuckyMeta.getMetaNodeValue(player, WUtil.getCommPerm("command.back", "cooldown"), String.valueOf(W$SConfig.backCooldown()));
        context.getSource().sendSuccess(new TextComponent(WUtil.broadcastPrefix() + "Benchmark-1: result of back cooldown is" + metaValue), true);

        // BENCH 2
//        var contextValie = LuckyMeta.getContextData(player, )

        return 0;
    }
}
