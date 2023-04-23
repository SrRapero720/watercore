package me.srrapero720.watercore.custom.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.srrapero720.watercore.custom.data.PlayerSpawn;
import me.srrapero720.watercore.internal.WCoreUtil;
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
        var position = WCoreUtil.calculateNearbyCenter(player.getX(), player.getY(), player.getZ());

        PlayerSpawn.fetch(mode, server)
                .setDimension(player.getLevel().dimension())
                .setCoordinates(position, WCoreUtil.fixAngle(player.getXRot()), WCoreUtil.fixAngle(player.getYRot()));


        // TODO: Implement something on MCFormat
        context.getSource().sendSuccess(new TranslatableComponent("wc.command.setspawn.success",
                "§c" + WCoreUtil.twoDecimal(position.x),
                "§c" + WCoreUtil.twoDecimal(position.y),
                "§c" + WCoreUtil.twoDecimal(position.z),
                "§c" + WCoreUtil.fixAngle(player.getYRot()),
                "§c" + player.getLevel().dimension().location()), true);

        return 0;
    }
}
