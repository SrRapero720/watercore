package me.srrapero720.watercore.custom.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.srrapero720.watercore.custom.data.WorldSpawnData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import me.srrapero720.watercore.internal.WaterUtil;
import me.srrapero720.watercore.custom.data.LobbySpawnData;
import org.jetbrains.annotations.NotNull;

public class SetLobbySpawnComm {
    public static void register(@NotNull CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("setlobbyspawn").requires((c) -> c.hasPermission(3)).executes(SetLobbySpawnComm::saveLobbySpawn));
        dispatcher.register(Commands.literal("setworldspawn").requires((c) -> c.hasPermission(3)).executes(SetLobbySpawnComm::saveWorldSpawn));
    }

    public static int saveLobbySpawn(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        var player = context.getSource().getPlayerOrException();
        var server = player.getServer();

        if (server != null) {
            var position = WaterUtil.findCenter(player.getX(), player.getY(), player.getZ());

            LobbySpawnData.fetch(server).setDimension(player.getLevel().dimension()).setCords(position, WaterUtil.fixAngle(player.getYRot()), WaterUtil.fixAngle(player.getXRot())).save(new CompoundTag());
            context.getSource().sendSuccess(new TranslatableComponent("wc.command.setlobbyspawn.success",
                    WaterUtil.twoDecimal(player.getX()),
                    WaterUtil.twoDecimal(player.getY()),
                    WaterUtil.twoDecimal(player.getZ()),
                    WaterUtil.fixAngle(player.getYRot()),
                    player.getLevel().dimension().location()), true);
        } else context.getSource().sendFailure(new TranslatableComponent("wc.command.setlobbyspawn.failed"));

        return 0;
    }

    public static int saveWorldSpawn(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        var player = context.getSource().getPlayerOrException();
        var server = player.getServer();

        if (server != null) {
            var position = WaterUtil.findCenter(player.getX(), player.getY(), player.getZ());

            WorldSpawnData.fetch(server).setDimension(player.getLevel().dimension()).setCords(position, WaterUtil.fixAngle(player.getYRot()), WaterUtil.fixAngle(player.getXRot())).save(new CompoundTag());
            context.getSource().sendSuccess(new TranslatableComponent("wc.command.setlobbyspawn.success",
                    WaterUtil.twoDecimal(player.getX()),
                    WaterUtil.twoDecimal(player.getY()),
                    WaterUtil.twoDecimal(player.getZ()),
                    WaterUtil.fixAngle(player.getYRot()),
                    player.getLevel().dimension().location()), true);
        } else context.getSource().sendFailure(new TranslatableComponent("wc.command.setlobbyspawn.failed"));

        return 0;
    }
}
