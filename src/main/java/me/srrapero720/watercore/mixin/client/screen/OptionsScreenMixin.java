package me.srrapero720.watercore.mixin.client.screen;

import me.srrapero720.watercore.screens.LanguageSelectScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.OptionsScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value = OptionsScreen.class)
public abstract class OptionsScreenMixin extends Screen {
    protected OptionsScreenMixin(Component p_96550_) { super(p_96550_); }

    @ModifyArg(method = "init()V", index = 0, at = @At(ordinal = 8, value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/OptionsScreen;addRenderableWidget(Lnet/minecraft/client/gui/components/events/GuiEventListener;)Lnet/minecraft/client/gui/components/events/GuiEventListener;"))
    public GuiEventListener buildLangBtn(GuiEventListener par1) {
        return new Button(this.width / 2 - 155, this.height / 6 + 96 - 6, 150, 20, new TranslatableComponent("options.language"), (p_96268_) -> {
            this.minecraft.setScreen(new LanguageSelectScreen(this, this.minecraft.options, this.minecraft.getLanguageManager()));
        });
    }
}
