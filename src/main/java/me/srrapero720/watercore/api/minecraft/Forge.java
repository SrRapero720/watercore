package me.srrapero720.watercore.api.minecraft;

import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.function.Consumer;

public class Forge {
    public static IEventBus getEventBus() {
        return FMLJavaModLoadingContext.get().getModEventBus();
    }

    public static <T extends Event> void addFMLListener(Consumer<T> consumer) {
        getEventBus().addListener(consumer);
    }

}
