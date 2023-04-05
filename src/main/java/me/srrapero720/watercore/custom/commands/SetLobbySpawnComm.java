package me.srrapero720.watercore.custom.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.srrapero720.watercore.custom.data.PlayerSpawn;
import me.srrapero720.watercore.internal.WaterUtil;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TranslatableComponent;
import org.jetbrains.annotations.NotNull;

public class SetLobbySpawnComm {
    public static void register(@NotNull CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("setlobbyspawn").requires((c) -> c.hasPermission(3))
                .executes((context) -> saveSpawn(PlayerSpawn.Mode.LOBBY, context)));
        dispatcher.register(Commands.literal("setworldspawn").requires((c) -> c.hasPermission(3))
                .executes((context) -> saveSpawn(PlayerSpawn.Mode.WORLD, context)));
    }

    public static int saveSpawn(PlayerSpawn.Mode mode, CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        var player = context.getSource().getPlayerOrException();
        var server = player.getServer();
        var position = WaterUtil.findCenter(player.getX(), player.getY(), player.getZ());

        PlayerSpawn.fetch(mode, server)
                .setDimension(player.getLevel().dimension())
                .setCoordinates(position, WaterUtil.fixAngle(player.getXRot()), WaterUtil.fixAngle(player.getYRot()));
                //.save(new CompoundTag()); // This is necessary?


        // TODO: Implement something on MCFormat
        context.getSource().sendSuccess(new TranslatableComponent("wc.command.setspawn.success",
                "§c" + WaterUtil.twoDecimal(position.x),
                "§c" + WaterUtil.twoDecimal(position.y),
                "§c" + WaterUtil.twoDecimal(position.z),
                "§c" + WaterUtil.fixAngle(player.getYRot()),
                "§c" + player.getLevel().dimension().location()), true);

        return 0;
    }
}
