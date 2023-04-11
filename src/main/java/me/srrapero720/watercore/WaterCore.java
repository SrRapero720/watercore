package me.srrapero720.watercore;

import me.srrapero720.watercore.internal.WaterBans;
import me.srrapero720.watercore.internal.WaterConsole;
import me.srrapero720.watercore.internal.WaterRegistry;
import me.srrapero720.watercore.internal.WaterUtil;
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
        FMLJavaModLoadingContext.get().getModEventBus().addListener((FMLCommonSetupEvent event) -> WaterBans.validate());
        WaterRegistry.register();
        MinecraftForge.EVENT_BUS.register(WaterRegistry.class);
        WaterConsole.justPrint("WATERCoRE setup completed");
    }
}
