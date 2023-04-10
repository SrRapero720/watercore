package me.srrapero720.watercore;

import me.srrapero720.watercore.internal.WaterConsole;
import me.srrapero720.watercore.internal.WaterRegistry;
import me.srrapero720.watercore.internal.WaterUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(WaterCore.ID)
public class WaterCore {
    public static final String PROTOCOL = "1.2";
    public static final String ID = "watercore";
    public static IEventBus bus() { return FMLJavaModLoadingContext.get().getModEventBus(); }

    public WaterCore() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(WaterCore::crashIfBannedModIsPresent);
        WaterRegistry.register();
        MinecraftForge.EVENT_BUS.register(WaterRegistry.class);
        WaterConsole.justPrint("WATERCoRE setup completed");
    }


    public static void crashIfBannedModIsPresent(FMLCommonSetupEvent event) {
        if (WaterUtil.isModLoading("memoryleakfix")) throw new IncompatibleModInstalled("MemoryLeakFix is explicit incompatible with WATERCoRE.");
    }

    public static class IncompatibleModInstalled extends RuntimeException {
        public IncompatibleModInstalled(String info) { super(info); }
    }
}
