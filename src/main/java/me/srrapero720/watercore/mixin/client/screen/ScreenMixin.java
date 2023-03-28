package me.srrapero720.watercore.mixin.client.screen;

import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(value = Screen.class, priority = 0)
public class ScreenMixin {
    @Shadow @Final public List<Widget> renderables;
    @Shadow @Final private List<GuiEventListener> children;
    @Shadow @Final private List<NarratableEntry> narratables;

    /**
     * @author SrRapero720
     * @reason Removed redirecto to addWidget
     */
    @Overwrite(remap = false)
    protected <T extends GuiEventListener & Widget & NarratableEntry> T addRenderableWidget(T renderable) {
        this.renderables.add(renderable);
        this.children.add(renderable);
        this.narratables.add(renderable);
        return renderable;
    }

    /**
     * @author SrRapero720
     * @reason Forces all Widgets to be added has Renderables
     */
    @Overwrite(remap = false)
    protected <T extends GuiEventListener & NarratableEntry> T addWidget(T widget) {
        if (widget instanceof Widget renderable) this.renderables.add(renderable);
        this.children.add(widget);
        this.narratables.add(widget);
        return widget;
    }
}
