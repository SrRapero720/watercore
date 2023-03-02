package me.srrapero720.watercore;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("watercore")
public class WaterCore {
    public static final String PROTOCOL = "1.2";
    public static final String ID = "WATERCORE";
    public static final IEventBus BUS = FMLJavaModLoadingContext.get().getModEventBus();

    public WaterCore() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener((event -> SrConsole.warn(getClass().toString(), "WATERCoRE is present.")));
        SrRegistry.register();
        MinecraftForge.EVENT_BUS.register(SrRegistry.class);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {

        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            //SrConsole.debug(MODULE, "Registrando bloque: " + blockRegistryEvent.getName().toString());
        }

        @SubscribeEvent
        public static void onItemsRegistry(final RegistryEvent.Register<Item> itemRegistryEvent) {
            //SrConsole.debug(MODULE, "Registrando item: " + itemRegistryEvent.getName().toString());
        }
    }
}
