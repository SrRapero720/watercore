package me.srrapero720.watercore.custom.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.srrapero720.watercore.api.placeholder.Placeholder;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.MessageArgument;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import me.srrapero720.watercore.internal.forge.W$ServerConfig;
import net.minecraft.server.MinecraftServer;
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
                .executes((context -> broadcastToServer(context, Placeholder.parse(W$ServerConfig.get("broadcast_prefix"))))))
        );
    }

    private static int broadcastToServer(CommandContext<CommandSourceStack> context, String prefix) throws CommandSyntaxException {
        try {
            var server = context.getSource().getServer();
            var c = MessageArgument.getMessage(context, "message");
            return broadcastToServer(server, c, prefix);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private static int broadcastToServer(@NotNull MinecraftServer server, @NotNull Component c, String prefix) {
        final var mPrefix = prefix != null ? prefix : "";
        server.getPlayerList().broadcastMessage(new TextComponent(Placeholder.parse(mPrefix + c.getString())), ChatType.SYSTEM, Util.NIL_UUID);
        return 0;
    }

    private static int broadcastToServer(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        return broadcastToServer(context, null);
    }
}
