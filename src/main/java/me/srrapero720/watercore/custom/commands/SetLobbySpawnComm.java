package me.srrapero720.watercore.custom.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.srrapero720.watercore.custom.data.PlayerSpawn;
import me.srrapero720.watercore.utility.Tools;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TranslatableComponent;

public class SetLobbySpawnComm extends AbstractComm {
    public SetLobbySpawnComm(CommandDispatcher<CommandSourceStack> dispatcher) {
        super(dispatcher);
        dispatcher.register(Commands.literal("setworldspawn").requires((c) -> c.hasPermission(3))
                .executes((context) -> saveSpawn(PlayerSpawn.Mode.WORLD, context)));
        var slb = dispatcher.register(Commands.literal("setlobbyspawn").requires((c) -> c.hasPermission(3))
                .executes((context) -> saveSpawn(PlayerSpawn.Mode.LOBBY, context)));
        dispatcher.register(Commands.literal("sethubspawn").requires((c) -> c.hasPermission(3)).redirect(slb));
    }


    //========================================== //
    //    UTILITY REQUIRED FOR THAT COMMAND
    //========================================== //
    public static int saveSpawn(PlayerSpawn.Mode mode, CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        var player = context.getSource().getPlayerOrException();
        var server = player.getServer();
        var position = Tools.calculateNearbyCenter(player.getX(), player.getY(), player.getZ());

        PlayerSpawn.fetch(mode, server)
                .setDimension(player.getLevel().dimension())
                .setCoordinates(position, Tools.fixAngle(player.getXRot()), Tools.fixAngle(player.getYRot()));


        // TODO: Implement something on MCFormat
        context.getSource().sendSuccess(new TranslatableComponent("wc.command.setspawn.success",
                "§c" + Tools.twoDecimal(position.x),
                "§c" + Tools.twoDecimal(position.y),
                "§c" + Tools.twoDecimal(position.z),
                "§c" + Tools.fixAngle(player.getYRot()),
                "§c" + player.getLevel().dimension().location()), true);

        return 0;
    }
}
