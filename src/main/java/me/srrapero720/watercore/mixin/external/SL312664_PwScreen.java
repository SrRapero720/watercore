package me.srrapero720.watercore.mixin.external;


import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import top.seraphjack.simplelogin.SLConstants;
import top.seraphjack.simplelogin.client.PasswordHolder;
import top.seraphjack.simplelogin.client.SetPasswordScreen;

import java.util.UUID;

@Mixin(SetPasswordScreen.class)
public abstract class SL312664_PwScreen extends Screen {
    @Shadow private EditBox password;

    @Shadow private Button buttonRandom;

    @Shadow private Button buttonComplete;

    /**
     * @author SrRapero720
     * @reason Modify all
     */
    @Overwrite
    @Override
    protected void init() {
        this.password = this.addRenderableWidget(new EditBox(font, width / 2 - 100, height / 2, 170, 20, new TranslatableComponent("simplelogin.password")));
        this.password.setBordered(true);
        this.password.setEditable(true);
        this.password.setMaxLength(SLConstants.MAX_PASSWORD_LENGTH);
        this.password.setFilter((p) -> p.length() <= SLConstants.MAX_PASSWORD_LENGTH);
        this.password.setResponder((p) -> buttonComplete.active = !p.isEmpty());

        this.buttonRandom = this.addRenderableWidget(new Button(width / 2 + 80, height / 2, 20, 20,
                new TextComponent("R"), (btn) -> this.password.setValue(UUID.randomUUID().toString())));

        this.buttonComplete = this.addRenderableWidget(new Button(width / 2 - 100, height / 2 + 40, 200, 20, CommonComponents.GUI_DONE, (btn) -> {
            if (!this.password.getValue().isEmpty()) {
                PasswordHolder.instance().initialize(this.password.getValue());
                onClose();
            }
        }));
        this.buttonComplete.active = false;
        this.setInitialFocus(this.password);
    }

    protected SL312664_PwScreen(Component p_96550_) { super(p_96550_); }


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

//        this.password.render(poseStack, mouseX, mouseY, partialTicks);
//        this.buttonRandom.render(poseStack, mouseX, mouseY, partialTicks);
//        this.buttonComplete.render(poseStack, mouseX, mouseY, partialTicks);
        super.render(pose, mouseX, mouseY, pTicks);
    }
}