package me.srrapero720.watercore.mixin;

import com.mojang.brigadier.AmbiguityConsumer;
import com.mojang.brigadier.CommandDispatcher;
import me.srrapero720.watercore.internal.WRegistry;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Commands.class)
public class CommandMixin {

    @Redirect(method = "<init>", at = @At(value = "INVOKE_ASSIGN", target = "Lcom/mojang/brigadier/CommandDispatcher;findAmbiguities(Lcom/mojang/brigadier/AmbiguityConsumer;)V"))
    public void redirectCommands(CommandDispatcher<CommandSourceStack> instance, AmbiguityConsumer<CommandSourceStack> consumer) {
        for (var comm: WRegistry.getCommandRegistry()) comm.load(instance);
        instance.findAmbiguities(consumer);
    }
}
