package me.srrapero720.watercore.mixin.common;

import com.mojang.brigadier.AmbiguityConsumer;
import com.mojang.brigadier.CommandDispatcher;
import me.srrapero720.watercore.internal.WCoreInternals;
import me.srrapero720.watercore.internal.WCoreRegistry;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Commands.class)
public class CommandMixin {
    @Redirect(method = "<init>", at = @At(value = "INVOKE", remap = false, target = "Lcom/mojang/brigadier/CommandDispatcher;findAmbiguities(Lcom/mojang/brigadier/AmbiguityConsumer;)V"))
    public void redirectCommands(CommandDispatcher<CommandSourceStack> instance, AmbiguityConsumer<CommandSourceStack> consumer) {
        WCoreInternals.core$loadAllComands(instance);
        instance.findAmbiguities(consumer);
    }
}
