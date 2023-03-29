package me.srrapero720.watercore.custom.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.srrapero720.watercore.custom.data.BackData;
import me.srrapero720.watercore.custom.data.storage.SimplePlayerStorage;
import me.srrapero720.watercore.internal.WaterUtil;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TranslatableComponent;
import org.jetbrains.annotations.NotNull;

public class BackComm {
    public static void register(@NotNull CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("back").executes(BackComm::teleport));
    }


    private static int teleport(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        var player = context.getSource().getPlayerOrException();
        var server = player.getServer();
        var levels = server.getAllLevels();

        var post = SimplePlayerStorage.loadBackData(player);

        if (post == null) {
            context.getSource().sendFailure(new TranslatableComponent("wc.command.back.failed"));
            return 0;
        }

        if (!SimplePlayerStorage.updateBackCooldown(player)) {
            context.getSource().sendFailure(new TranslatableComponent("wc.command.back.cooldown", SimplePlayerStorage.loadBackCooldown(player)));
            return 0;
        }

        player.teleportTo(WaterUtil.findLevel(levels, post.dimension), post.x, post.y, post.z, post.yRot, post.xRot);
        return 0;
    }
}
