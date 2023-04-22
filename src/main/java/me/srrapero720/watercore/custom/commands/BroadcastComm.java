package me.srrapero720.watercore.custom.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import me.srrapero720.watercore.api.placeholder.Placeholder;
import me.srrapero720.watercore.api.thread.ThreadUtil;
import me.srrapero720.watercore.internal.forge.W$ServerConfig;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.MessageArgument;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;

public class BroadcastComm extends AbstractComm {
    public BroadcastComm(CommandDispatcher<CommandSourceStack> dispatcher) {
        super(dispatcher);

        // COMMAND REGISTER
        dispatcher.register(Commands.literal("broadcast")
                .requires((p_137800_) -> p_137800_.hasPermission(3))
                .then(Commands.argument("message", MessageArgument.message())
                        .executes((context -> broadcastToServer(context, Placeholder.parse(W$ServerConfig.get("broadcast_prefix"))))))
        );

        dispatcher.register(Commands.literal("broadcast-raw")
                .requires((p_137800_) -> p_137800_.hasPermission(3))
                .then(Commands.argument("message", MessageArgument.message())
                        .executes(BroadcastComm::broadcastToServer))
        );

    }

    //========================================== //
    //    UTILITY REQUIRED FOR THAT COMMAND
    //========================================== //
    private static int broadcastToServer(CommandContext<CommandSourceStack> context, String prefix) {
        return ThreadUtil.tryAndReturn((defaultVar) -> {
            var server = context.getSource().getServer();
            var c = MessageArgument.getMessage(context, "message");
            return broadcastToServer(server, c, prefix);
        }, 1);
    }

    private static int broadcastToServer(@NotNull MinecraftServer server, @NotNull Component c, String prefix) {
        final var mPrefix = prefix != null ? prefix : "";
        server.getPlayerList().broadcastMessage(new TextComponent(Placeholder.parse(mPrefix + c.getString())), ChatType.SYSTEM, Util.NIL_UUID);
        return 0;
    }

    private static int broadcastToServer(CommandContext<CommandSourceStack> context) {
        return broadcastToServer(context, null);
    }
}
