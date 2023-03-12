package me.srrapero720.watercore.custom.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import me.srrapero720.watercore.internal.WaterRegistry;
import me.srrapero720.watercore.internal.WaterUtil;
import me.srrapero720.watercore.custom.data.LobbyData;
import org.jetbrains.annotations.NotNull;

public class SetLobbyPosComm {
    public static void register(@NotNull CommandDispatcher<CommandSourceStack> dispatcher) {
        var main = dispatcher.register(Commands.literal("setlobbyspawn").requires((c) -> c.hasPermission(3)).executes(SetLobbyPosComm::saveLobbySpawn));
        dispatcher.register(Commands.literal("setworldspawn").redirect(main));
    }

    public static int saveLobbySpawn(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        var player = context.getSource().getPlayerOrException();
        var server = player.getServer();

        if (server != null) {
            LobbyData.fetch(server).setDimension(player.getLevel().dimension()).setCords(player.getOnPos(), WaterUtil.fixAngle(player.getYRot()), WaterUtil.fixAngle(player.getXRot())).save(new CompoundTag());
            context.getSource().sendSuccess(new TranslatableComponent("watercore.command.setlobbyspawn.success",
                    WaterUtil.twoDecimal(player.getX()),
                    WaterUtil.twoDecimal(player.getY()),
                    WaterUtil.twoDecimal(player.getZ()),
                    WaterUtil.fixAngle(player.getYRot())), true);
        } else context.getSource().sendSuccess(new TranslatableComponent("watercore.command.setlobbyspawn.failed"), true);

        return 0;
    }
}
