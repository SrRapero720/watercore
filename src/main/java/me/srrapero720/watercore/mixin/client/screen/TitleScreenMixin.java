package me.srrapero720.watercore.mixin.client.screen;

import me.srrapero720.watercore.screens.LanguageSelectScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value = TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {
    protected TitleScreenMixin(Component p_96550_) {
        super(p_96550_);
    }

    @ModifyArg(method = "init()V", index = 0, at = @At(ordinal = 1, value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/TitleScreen;addRenderableWidget(Lnet/minecraft/client/gui/components/events/GuiEventListener;)Lnet/minecraft/client/gui/components/events/GuiEventListener;"))
    public GuiEventListener overwriteLangButton(GuiEventListener par1) {
        int l = this.height / 4 + 48;

        return new ImageButton(this.width / 2 - 124, l + 72 + 12, 20, 20, 0, 106, 20,
                Button.WIDGETS_LOCATION, 256, 256, (p_96791_) -> {
            this.minecraft.setScreen(new LanguageSelectScreen(this, this.minecraft.options, this.minecraft.getLanguageManager()));
        }, new TranslatableComponent("narrator.button.language"));
    }
}
