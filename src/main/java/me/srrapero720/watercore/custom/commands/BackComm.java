package me.srrapero720.watercore.custom.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.srrapero720.watercore.custom.data.storage.SimplePlayerStorage;
import me.srrapero720.watercore.internal.WaterConsole;
import me.srrapero720.watercore.internal.WaterUtil;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TranslatableComponent;
import org.jetbrains.annotations.NotNull;

public class BackComm {
    public static void register(@NotNull CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("back").executes((context -> teleport(0, context)))
                .then(Commands.argument("index", IntegerArgumentType.integer(0, 10)).executes(BackComm::teleportWithIndex)));
    }

    private static int teleportWithIndex(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        return teleport(context.getArgument("index", int.class), context);
    }


    private static int teleport(int index, CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        var player = context.getSource().getPlayerOrException();
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

        player.teleportTo(WaterUtil.findLevel(levels, post.getDimension()), post.getX(), post.getY(), post.getZ(), post.getRotY(), post.getRotX());


        return 0;
    }
}
