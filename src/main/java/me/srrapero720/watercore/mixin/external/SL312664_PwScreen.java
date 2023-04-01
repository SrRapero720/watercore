package me.srrapero720.watercore.mixin.external;


import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.seraphjack.simplelogin.client.SetPasswordScreen;

@OnlyIn(Dist.CLIENT)
@Mixin(value = SetPasswordScreen.class, remap = false)
public abstract class SL312664_PwScreen extends Screen {
    @Shadow private EditBox password;

    protected SL312664_PwScreen(Component p_96550_) { super(p_96550_); }

    @Inject(method = "init", at = @At(value = "TAIL"))
    protected void injectInit(CallbackInfo ci) {
        addRenderableWidget(password);
    }

    /**
     * @author SrRapero720
     * @reason uhhhh
     */
    @Overwrite
    @Override
    public void render(@NotNull PoseStack pose, int mouseX, int mouseY, float pTicks) {
        this.setFocused(this.password);
        this.password.setFocus(true);
        renderBackground(pose);

        int middle = width / 2;
        drawCenteredString(pose, font, new TranslatableComponent("simplelogin.password.title"), middle, height / 4, 0xFFFFFF);
        super.render(pose, mouseX, mouseY, pTicks);
    }
}
