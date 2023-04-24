package me.srrapero720.watercore.custom.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;

public class WarpComm extends AbstractComm {
    protected WarpComm(CommandDispatcher<CommandSourceStack> ignored) {
        super(ignored);
    }
}
