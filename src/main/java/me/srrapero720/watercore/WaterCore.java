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
        WaterRegistry.register();
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(WaterRegistry.class);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(WaterCore::crashIfBannedModIsPresent);
        WaterConsole.justPrint("WATERCoRE setup completed");
    }

    public static void crashIfBannedModIsPresent(FMLCommonSetupEvent event) {
        if (WaterUtil.isModLoading("memoryleakfix")) throw new IncompatibleModInstalled("MemoryLeakFix is explicit incompatible with WATERCoRE.");
    }

    public static class IncompatibleModInstalled extends RuntimeException {
        public IncompatibleModInstalled(String info) { super(info); }
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {

        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // SrConsole.debug(MODULE, "Registrando bloque: " + blockRegistryEvent.getName().toString());
        }

        @SubscribeEvent
        public static void onItemsRegistry(final RegistryEvent.Register<Item> itemRegistryEvent) {
            // SrConsole.debug(MODULE, "Registrando item: " + itemRegistryEvent.getName().toString());
        }
    }
}
