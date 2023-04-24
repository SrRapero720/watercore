package me.srrapero720.watercore.mixin.external;

import de.keksuccino.fancymenu.FancyMenu;
import de.keksuccino.fancymenu.menu.fancy.MenuCustomizationEvents;
import de.keksuccino.konkrete.file.FileUtils;
import net.minecraft.client.Options;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.File;
import java.io.IOException;

@Deprecated(since = "Keks confirm a fix", forRemoval = true)
@Mixin(Options.class)
public class FM01_Options {
    private static final int scale;
    static {
        FancyMenu.updateConfig();
        scale = FancyMenu.config.getOrDefault("defaultguiscale", -1);
    }

    @Redirect(method = "processOptions", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Options$FieldAccess;process(Ljava/lang/String;I)I", ordinal = 2))
    public int injectProcessOptions(Options.FieldAccess instance, String s, int i) {
        if ((scale != -1) && (scale != 0)) {
            var f = FancyMenu.INSTANCE_DATA_DIR;
            if (!f.exists()) f.mkdirs();

            var f2 = new File(f.getPath() + "/default_scale_set.fm");
            var f3 = new File("mods/fancymenu/defaultscaleset.fancymenu");
            if (!f2.exists() && !f3.exists()) {
                try {
                    f2.createNewFile();
                    FileUtils.writeTextToFile(f2, false, "you're not supposed to be here! shoo!");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                //LOGGER.info("########################### FANCYMENU: SETTING DEFAULT GUI SCALE!");
                return scale;
            }
        }
        return instance.process(s, i);
    }
}