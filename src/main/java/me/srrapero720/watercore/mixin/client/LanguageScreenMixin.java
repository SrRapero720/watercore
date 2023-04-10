package me.srrapero720.watercore.mixin.client;

import net.minecraft.client.Options;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.LanguageSelectScreen;
import net.minecraft.client.gui.screens.OptionsSubScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.LanguageManager;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LanguageSelectScreen.class)
public class LanguageScreenMixin extends OptionsSubScreen {

    @Shadow private LanguageSelectScreen.LanguageSelectionList packSelectionList;
    @Shadow @Final LanguageManager languageManager;

    public LanguageScreenMixin(Screen p_96284_, Options p_96285_, Component p_96286_) { super(p_96284_, p_96285_, p_96286_); }

    @Redirect(method = "init", at = @At(value = "INVOKE", ordinal = 1, target = "Lnet/minecraft/client/gui/screens/LanguageSelectScreen;addRenderableWidget(Lnet/minecraft/client/gui/components/events/GuiEventListener;)Lnet/minecraft/client/gui/components/events/GuiEventListener;"))
    public GuiEventListener redirectResourceManagerReload(LanguageSelectScreen ignored, GuiEventListener moreIgnored) {
        return this.addRenderableWidget(new Button(this.width / 2 - 155 + 160, this.height - 38, 150, 20, CommonComponents.GUI_DONE, (p_96099_) -> {
            var entry = this.packSelectionList.getSelected();
            if (entry != null && !entry.language.getCode().equals(this.languageManager.getSelected().getCode())) {
                this.languageManager.setSelected(entry.language);
                this.options.languageCode = entry.language.getCode();
                this.minecraft.getLanguageManager().onResourceManagerReload(this.minecraft.getResourceManager());
                this.options.save();
            }

            this.minecraft.setScreen(this.lastScreen);
        }));
    }
}
