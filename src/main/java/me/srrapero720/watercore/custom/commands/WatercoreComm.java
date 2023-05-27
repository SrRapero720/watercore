package me.srrapero720.watercore.custom.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.srrapero720.watercore.internal.PlayerName;
import me.srrapero720.watercore.api.luckperms.LuckyMeta;
import me.srrapero720.watercore.api.thread.ThreadUtil;
import me.srrapero720.watercore.utility.Tools;
import me.srrapero720.watercore.utility.Logg;
import me.srrapero720.watercore.WaterPerms;
import me.srrapero720.watercore.WaterConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Style;
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

                // SETTERS
                .then(Commands.literal("set")
                        .then(Commands.literal("join_format") .then(Commands.argument("format", StringArgumentType.greedyString()).executes(context -> updateFormat(context, PlayerName.Format.JOIN))))
                        .then(Commands.literal("leave_format").then(Commands.argument("format", StringArgumentType.greedyString()).executes(context -> updateFormat(context, PlayerName.Format.LEAVE))))
                        .then(Commands.literal("chat_format") .then(Commands.argument("format", StringArgumentType.greedyString()).executes(context -> updateFormat(context, PlayerName.Format.CHAT)))))
        );
    }

    //========================================== //
    //    UTILITY REQUIRED FOR THAT COMMAND
    //========================================== //
    public static int threadLoggerExecution(CommandContext<CommandSourceStack> context) {
        if (ThreadUtil.threadLoggerEnabled()) ThreadUtil.threadLoggerKill();
        else ThreadUtil.threadLogger();
        context.getSource().sendSuccess(new TranslatableComponent("wc.command.watercore.threads.logger", Tools.broadcastPrefix()), false);
        return 0;
    }

    public static int threadListExecution(CommandContext<CommandSourceStack> context) {
        ThreadUtil.showThreads();
        context.getSource().sendSuccess(new TranslatableComponent("wc.command.watercore.threads.list", Tools.broadcastPrefix()), false);
        return 0;
    }

    public static int updateFormat(CommandContext<CommandSourceStack> context, PlayerName.Format format) {
        var result = StringArgumentType.getString(context, "format");
        switch (format) {
            case JOIN -> WaterConfig.setJoinFormat(result);
            case LEAVE -> WaterConfig.setLeaveFormat(result);
            case CHAT -> WaterConfig.setChatFormat(result);
        }

        // TODO: update this bullshit success messages
        context.getSource().sendSuccess(new TranslatableComponent("wc.command.watercore.set.config.success", Tools.broadcastPrefix()).withStyle(ChatFormatting.GREEN), true);
        return 0;
    }

    public static int runTest(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        context.getSource().sendSuccess(new TextComponent(Tools.broadcastPrefix().concat("Running...")), true);
        var player = context.getSource().getPlayerOrException();

        // BENCH 1
        ThreadUtil.trySimple(() -> {
            var backCooldown = LuckyMeta.getIntMetaNodeValue(player, BackComm.COOLDOWN_NODE.getNode(), WaterConfig.backCooldown());
            context.getSource().sendSuccess(new TextComponent(Tools.broadcastPrefix() + "Benchmark-1: Result of back cooldown is " + backCooldown), true);
        }, (e) -> {
            context.getSource().sendFailure(new TextComponent(Tools.broadcastPrefix("&c") + "Benchmark-1: Failed to continue bench"));
            Logg.log("Benchmark-1: Exception detected {}", e);
        });

        // BENCH 1.1
        ThreadUtil.trySimple(() -> {
            var backCooldown = WaterPerms.playerPermissionInt(player.getUUID(), WaterPerms.BACK_COOLDOWN);
            context.getSource().sendSuccess(new TextComponent(Tools.broadcastPrefix() + "Benchmark-1.1: Result of back cooldown is " + backCooldown), true);
        }, (e) -> {
            context.getSource().sendFailure(new TextComponent(Tools.broadcastPrefix("&c") + "Benchmark-1.1: Failed to continue bench"));
            Logg.log("Benchmark-1.1: Exception detected {}", e);
        });

        // BENCH 2
        ThreadUtil.trySimple(() -> {
            var displayName = LuckyMeta.getMetaNodeValue(player, PlayerName.DISPLAYNAME.getNode(), "Non specified");
            context.getSource().sendSuccess(new TextComponent(Tools.broadcastPrefix() + "Benchmark-2: Result of displayname is " + displayName), true);
            context.getSource().sendSuccess(new TextComponent(Tools.broadcastPrefix() + "Benchmark-2: Running PlayerName.displayname()"), true);
            context.getSource().sendSuccess(new TextComponent(Tools.broadcastPrefix() + "Benchmark-2: Result of running that is " + PlayerName.displayname(player).getString()), true);
        }, (e) -> {
            context.getSource().sendFailure(new TextComponent(Tools.broadcastPrefix("&c") + "Benchmark-2: Failed to continue bench"));
            Logg.log("Benchmark-2: Exception detected {}", e);
        });

        // BENCH 2.2
        ThreadUtil.trySimple(() -> {
            var displayName = WaterPerms.playerPermissionString(player.getUUID(), WaterPerms.DISPLAYNAME_VALUE);
            context.getSource().sendSuccess(new TextComponent(Tools.broadcastPrefix() + "Benchmark-2.1: Result of displayname is " + displayName), true);
            context.getSource().sendSuccess(new TextComponent(Tools.broadcastPrefix() + "Benchmark-2.1: Running PlayerName.displayname()"), true);
            context.getSource().sendSuccess(new TextComponent(Tools.broadcastPrefix() + "Benchmark-2.1: Result of running that is " + PlayerName.displayname(player).getString()), true);
        }, (e) -> {
            context.getSource().sendFailure(new TextComponent(Tools.broadcastPrefix("&c") + "Benchmark-2.1: Failed to continue bench"));
            Logg.log("Benchmark-2.1: Exception detected {}", e);
        });

        return 0;
    }
}
