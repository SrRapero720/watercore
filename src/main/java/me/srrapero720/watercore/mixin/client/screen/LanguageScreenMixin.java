package me.srrapero720.watercore.mixin.client.screen;

import me.srrapero720.watercore.custom.screens.LanguageSelectScreen;
import net.minecraft.client.resources.language.LanguageManager;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LanguageSelectScreen.class)
public class LanguageScreenMixin {

    @Redirect(method = {"lambda$init$0", "lambda$m_7856_$0"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/language/LanguageManager;onResourceManagerReload(Lnet/minecraft/server/packs/resources/ResourceManager;)V"))
    public void redirectResourceManagerReload(LanguageManager instance, ResourceManager p_118973_) {

    }
}
