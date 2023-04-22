package me.srrapero720.watercore.custom.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.srrapero720.watercore.api.luckperms.LuckyCore;
import me.srrapero720.watercore.api.luckperms.LuckyNode;
import me.srrapero720.watercore.custom.data.storage.SimplePlayerStorage;
import me.srrapero720.watercore.internal.WUtil;
import me.srrapero720.watercore.internal.forge.W$ServerConfig;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;


public class BackComm extends AbstractComm {
    public net.luckperms.api.node.Node COOLDOWN_NODE;
    public BackComm(CommandDispatcher<CommandSourceStack> dispatcher) {
        super(dispatcher);

        // LUCKY STUFF
        if (LuckyCore.isPresent()) COOLDOWN_NODE = LuckyNode.registerMetaNode("watercore.command.back.cooldown", W$ServerConfig.get("back_cooldown"));

        // COMMAND REGISTER
        dispatcher.register(Commands.literal("back").executes(BackComm::backWithoutIndexAndRunPlayer)
                .then(Commands.argument("index", IntegerArgumentType.integer(0, 10)).executes(BackComm::backRunPlayer)));
    }


    //========================================== //
    //    UTILITY REQUIRED FOR THAT COMMAND
    //========================================== //
    protected static int backWithoutIndexAndRunPlayer(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        return backRaw(0, context.getSource().getPlayerOrException(), context);
    }

    protected static int backWithoutIndex(ServerPlayer player, CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        return backRaw(0, player, context);
    }

    protected static int backRunPlayer(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        return backRaw(context.getArgument("index", int.class), context.getSource().getPlayerOrException(), context);
    }

    protected static int backWithoutIndexAndArgPlayer(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        return backRaw(0, EntityArgument.getPlayer(context, "player"), context);
    }

    protected static int backArgPlayer(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        return backRaw(context.getArgument("index", int.class), EntityArgument.getPlayer(context, "player"), context);
    }

    protected static int backRaw(int index, ServerPlayer player, CommandContext<CommandSourceStack> context) {
        var server = player.getServer();
        var levels = server.getAllLevels();
        var post = SimplePlayerStorage.getBack(index, player);

        if (post == null) {
            context.getSource().sendFailure(new TranslatableComponent("wc.command.back.failed"));
            return 0;
        }

        if (!SimplePlayerStorage.updateBackCooldown(player)) {
            context.getSource().sendFailure(new TranslatableComponent("wc.command.back.cooldown", SimplePlayerStorage.loadBackCooldown(player)));
            return 0;
        }

        player.teleportTo(WUtil.fetchLevel(levels, post.getDimension()), post.getX(), post.getY(), post.getZ(), post.getRotY(), post.getRotX());
        return 0;
    }
}
