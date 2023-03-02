package me.srrapero720.watercore.custom.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import me.srrapero720.watercore.SrRegistry;
import me.srrapero720.watercore.SrUtil;
import me.srrapero720.watercore.custom.data.LobbyData;
import org.jetbrains.annotations.NotNull;

public class SetLobbyPosComm {
    public static void register(@NotNull CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("setlobbyspawn").requires((p_137800_) -> p_137800_.hasPermission(3))
                        .executes(SetLobbyPosComm::saveLobbySpawn));

    }

    public static int saveLobbySpawn(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        var player = context.getSource().getPlayerOrException();
        var server = player.getServer();

        if (!SrRegistry.dimension("Lobby").equals(player.getLevel().dimension())) {
            context.getSource().sendFailure(new TranslatableComponent("watercore.command.setlobbyspawn.failed"));
            return 1;
        }

        LobbyData.fetch(server).setCords(player.getOnPos(), SrUtil.fixAngle(player.getYRot()), SrUtil.fixAngle(player.getXRot())).save(new CompoundTag());
        context.getSource().sendSuccess(new TranslatableComponent("watercore.command.setlobbyspawn.success",
                SrUtil.twoDecimal(player.getX()), SrUtil.twoDecimal(player.getY()), SrUtil.twoDecimal(player.getZ()), SrUtil.fixAngle(player.getYRot())), true);
        return 0;
    }
}
