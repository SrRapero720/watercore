package me.srrapero720.watercore;

import me.srrapero720.watercore.internal.WMLBanned;
import me.srrapero720.watercore.internal.WConsole;
import me.srrapero720.watercore.internal.WRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(WaterCore.ID)
public class WaterCore {
    public static final String PROTOCOL = "1.2";
    public static final String ID = "watercore";
    public static IEventBus bus() { return FMLJavaModLoadingContext.get().getModEventBus(); }

    public WaterCore() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener((FMLCommonSetupEvent event) -> WMLBanned.validate());
        WRegistry.register();
        MinecraftForge.EVENT_BUS.register(WRegistry.class);
        WConsole.justPrint("WATERCoRE setup completed");
    }
}
