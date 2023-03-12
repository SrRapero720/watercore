package me.srrapero720.watercore.network;

import me.srrapero720.watercore.WaterCore;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Deprecated
public class EventNetwork {
    private static int MSGID = 0;
    private static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(WaterCore.ID, "main"),
            () -> WaterCore.PROTOCOL,
            WaterCore.PROTOCOL::equals,
            WaterCore.PROTOCOL::equals
    );

    public static SimpleChannel net() { return INSTANCE; }
    public static void putMessage() { MSGID++; }


    public static <T> void addNetworkMessage(Class<T> msgType, BiConsumer<T, FriendlyByteBuf> enc, Function<FriendlyByteBuf, T> dec, BiConsumer<T, Supplier<NetworkEvent.Context>> msgConsumer) {
        INSTANCE.registerMessage(MSGID, msgType, enc, dec, msgConsumer);
        MSGID++;
    }

    public static void handle(String msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // Work that needs to be thread-safe (most work)
            ServerPlayer sender = ctx.get().getSender(); // the client that sent this packet
            // Do stuff
        });
        ctx.get().setPacketHandled(true);
    }
}
