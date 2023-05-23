package me.srrapero720.watercore.mixin.client;

import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = Screen.class, priority = 72)
public class ScreenMixin {
    @Shadow @Final public List<Widget> renderables;

    @Inject(method = { "m_7787_", "addWidget" }, remap = false, at = @At(value = "RETURN"))
    protected <T extends GuiEventListener & NarratableEntry> void addWidget(T widget, CallbackInfoReturnable<T> cir) {
//        if ((widget instanceof Widget renderable) && !renderables.contains(renderable)) this.renderables.add(renderable);
    }
}