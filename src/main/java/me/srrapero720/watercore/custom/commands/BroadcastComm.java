package me.srrapero720.watercore.custom.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.srrapero720.watercore.api.MCFormat;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.MessageArgument;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.TextComponent;
import me.srrapero720.watercore.internal.WaterConfig;
import net.minecraft.network.chat.TranslatableComponent;
import org.jetbrains.annotations.NotNull;

public class BroadcastComm {
    public static void register(@NotNull CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("broadcast-raw")
                .requires((p_137800_) -> p_137800_.hasPermission(3))
                .then(Commands.argument("message", MessageArgument.message())
                .executes(BroadcastComm::broadcastToServer))
        );

        dispatcher.register(Commands.literal("broadcast")
                .requires((p_137800_) -> p_137800_.hasPermission(3))
                .then(Commands.argument("message", MessageArgument.message())
                .executes((context -> broadcastToServer(context, MCFormat.parse(WaterConfig.get("BROADCAST_PREFIX"))))))
        );
    }

    private static int broadcastToServer(CommandContext<CommandSourceStack> context, String prefix) throws CommandSyntaxException {
        final var mPrefix = prefix != null ? prefix : "";
        try {
            var source = context.getSource();
            var server = source.getServer();
            var c = MessageArgument.getMessage(context, "message");
            server.getPlayerList().broadcastMessage(new TextComponent(MCFormat.parse(mPrefix + c.getString())), ChatType.SYSTEM, Util.NIL_UUID);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private static int broadcastToServer(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        return broadcastToServer(context, null);
    }
}
