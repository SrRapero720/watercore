package me.srrapero720.watercore.mixin.client.screen;

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

import java.util.ArrayList;
import java.util.List;

@Mixin(value = Screen.class, priority = 0)
public class ScreenMixin {
    @Shadow @Final public List<Widget> renderables;
    @Shadow @Final private List<GuiEventListener> children;
    @Shadow @Final private List<NarratableEntry> narratables;

    @Inject(method = "addRenderableWidget", at = @At(value = "HEAD"), cancellable = true, remap = false)
    protected <T extends GuiEventListener & Widget & NarratableEntry> void addRenderableWidget(T renderable, CallbackInfoReturnable<T> cir) {
        this.renderables.add(renderable);
        this.children.add(renderable);
        this.narratables.add(renderable);
        cir.setReturnValue(renderable);
    }

    @Inject(method = "addWidget", at = @At(value = "HEAD"), cancellable = true, remap = false)
    protected <T extends GuiEventListener & NarratableEntry> void addWidget(T widget, CallbackInfoReturnable<T> cir) {
        if (widget instanceof Widget renderable) this.renderables.add(renderable);
        this.children.add(widget);
        this.narratables.add(widget);
        cir.setReturnValue(widget);
    }
}
