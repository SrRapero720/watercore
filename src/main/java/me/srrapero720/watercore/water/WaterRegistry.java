package me.srrapero720.watercore.water;

import me.srrapero720.watercore.WaterCore;
import me.srrapero720.watercore.api.ChatDataProvider;
import me.srrapero720.watercore.custom.commands.SetLobbyPosComm;
import me.srrapero720.watercore.custom.commands.SpawnComm;
import me.srrapero720.watercore.custom.items.BanHammer;
import me.srrapero720.watercore.custom.items.BaseCoin;
import me.srrapero720.watercore.custom.items.BaseViolin;
import me.srrapero720.watercore.custom.items.SuperWand;
import me.srrapero720.watercore.custom.potions.BlessedPotion;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.registries.*;
import net.minecraftforge.server.command.ConfigCommand;
import me.srrapero720.watercore.custom.commands.BroadcastComm;
import me.srrapero720.watercore.custom.config.WaterConfig;
import me.srrapero720.watercore.custom.tabs.DefaultTab;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = WaterCore.ID)
public class WaterRegistry {
    public enum Type { TABS, SOUND, POTION, ITEM, BLOCKS, BLOCK_ENTITIES, LEVELS }

    // REGISTRY FOR TABS (why?)
    private static final Map<String, CreativeModeTab> TABS = new HashMap<>();

    // REGISTRY FOR POTIONS
    private static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, WaterCore.ID);
    private static final Map<String, RegistryObject<Potion>> POTIONS_MAP = new HashMap<>();

    // REGISTRY FOR SOUNDS
    private static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, WaterCore.ID);
    private static final Map<String, RegistryObject<SoundEvent>> SOUNDS_MAP = new HashMap<>();

    // REGISTRY FOR ITEMS
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, WaterCore.ID);
    private static final Map<String, RegistryObject<Item>> ITEMS_MAP = new HashMap<>();

    // REGISTRY FOR BLOCKS
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, WaterCore.ID);
    private static final Map<String, RegistryObject<Block>> BLOCKS_MAP = new HashMap<>();

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, WaterCore.ID);
    private static final Map<String, RegistryObject<BlockEntityType<?>>> BLOCK_ENTITIES_MAP = new HashMap<>();

    // REGISTRY FOR DIMENSIONS
    private static final Map<String, ResourceKey<Level>> LEVELS = new HashMap<>();
    private static final Map<String, ResourceKey<DimensionType>> DIMENSION_TYPES = new HashMap<>();

    static {
        /* TABS */
        register(Type.TABS,"main", () -> new DefaultTab("watercore.MAIN", "small_violin"));
        register(Type.TABS,"admin", () -> new DefaultTab("watercore.ADMIN", "banhammer"));

        /* SOUNDS */
        register(Type.SOUND,"watermine", () -> new SoundEvent(new ResourceLocation(WaterCore.ID, "watermine")));
        register(Type.SOUND,"violin", () -> new SoundEvent(new ResourceLocation(WaterCore.ID, "violin")));

        /* POTIONS */
        register(Type.POTION,"blessed_1", () -> new BlessedPotion(WaterUtil.toTicks(180), 1));
        register(Type.POTION,"blessed_2", () -> new BlessedPotion(WaterUtil.toTicks(180), 2));
        register(Type.POTION,"blessed_3", () -> new BlessedPotion(WaterUtil.toTicks(180), 3));

        /* ITEMS */
        register(Type.ITEM,"coppercoin", () -> new BaseCoin(Rarity.COMMON));
        register(Type.ITEM,"ironcoin", () -> new BaseCoin(Rarity.COMMON));
        register(Type.ITEM,"goldencoin", () -> new BaseCoin(Rarity.UNCOMMON));
        register(Type.ITEM,"diamondcoin", () -> new BaseCoin(Rarity.UNCOMMON));
        register(Type.ITEM,"emeraldcoin", () -> new BaseCoin(Rarity.RARE));
        register(Type.ITEM,"netheritecoin", () -> new BaseCoin(Rarity.EPIC));
        register(Type.ITEM,"pendoritecoin", () -> new BaseCoin(Rarity.EPIC));

        register(Type.ITEM,"godwand", SuperWand::new);
        register(Type.ITEM,"banhammer", BanHammer::new);
        register(Type.ITEM,"small_violin", () -> new BaseViolin(0.75f));
        register(Type.ITEM,"big_violin", () -> new BaseViolin(1.0f));

        /* BLOCKS */
        //BLOCKS_MAP.put("brass_door", BLOCKS.register("brass_door", BrassDoor::new));


        /* DIMENSIONS */
        LEVELS.put("lobby", ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(WaterCore.ID, "lobby")));
        LEVELS.put("engineers", ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(WaterCore.ID, "enginners")));
        LEVELS.put("magical", ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(WaterCore.ID, "magical")));
        LEVELS.put("events", ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(WaterCore.ID, "events")));

        /* DIMENSION TYPES */
        DIMENSION_TYPES.put("lobby", ResourceKey.create(Registry.DIMENSION_TYPE_REGISTRY, LEVELS.get("lobby").getRegistryName()));
        DIMENSION_TYPES.put("engineers", ResourceKey.create(Registry.DIMENSION_TYPE_REGISTRY, LEVELS.get("engineers").getRegistryName()));
        DIMENSION_TYPES.put("magical", ResourceKey.create(Registry.DIMENSION_TYPE_REGISTRY, LEVELS.get("magical").getRegistryName()));
        DIMENSION_TYPES.put("events", ResourceKey.create(Registry.DIMENSION_TYPE_REGISTRY, LEVELS.get("events").getRegistryName()));

        /* CONFIG */
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, WaterConfig.SPEC, WaterCore.ID + "-server.toml");
    }

    /* POTIONS GETTERS */
    public static RegistryObject<Potion> potion(String name) { return POTIONS_MAP.get(name); }
    public static @NotNull Potion potionOnly(String name) { return POTIONS_MAP.get(name).get(); }

    /* SOUNDS GETTERS */
    public static RegistryObject<SoundEvent> sound(String name) { return SOUNDS_MAP.get(name); }
    public static @NotNull SoundEvent soundOnly(String name) { return SOUNDS_MAP.get(name).get(); }

    /* ITEMS GETTERS */
    public static RegistryObject<Item> item(String name) { return ITEMS_MAP.get(name); }
    public static @NotNull Item itemOnly(String name) { return ITEMS_MAP.get(name).get(); }

    /* TABS GETTERS */
    public static CreativeModeTab tab(String name) { return TABS.get(name); }

    /* BLOCKS GETTERS */
    public static RegistryObject<Block> block(String name) { return BLOCKS_MAP.get(name); }
    public static @NotNull Block blockOnly(String name) { return BLOCKS_MAP.get(name).get(); }
    public static RegistryObject<? extends Item> blockItemCreate(String name, Item item) { return ITEMS_MAP.put(name, ITEMS.register(name, () -> item)); }

    /* BLOCK ENTITIES GETTERS */
    public static RegistryObject<BlockEntityType<?>> blockEntity(String name) {return BLOCK_ENTITIES_MAP.get(name); }
    public static @NotNull BlockEntityType<?> blockEntityOnly(String name) { return BLOCK_ENTITIES_MAP.get(name).get(); }

    /* DIMENSIONS GETTERS */
    public static ResourceKey<Level> dimension(String name) { return LEVELS.get(name);}
    public static ResourceKey<DimensionType> dimensionType(String name) { return DIMENSION_TYPES.get(name); }


    @SuppressWarnings("unchecked")
    public static void register(Type type, String id, @NotNull Supplier<?> supplier) {
        switch (type) {
            case TABS -> TABS.put(id, ((Supplier<CreativeModeTab>) supplier).get());
            case SOUND -> SOUNDS_MAP.put(id, SOUNDS.register(id, (Supplier<SoundEvent>) supplier));
            case POTION -> POTIONS_MAP.put(id, POTIONS.register(id, (Supplier<Potion>) supplier));
            case ITEM -> ITEMS_MAP.put(id, ITEMS.register(id, (Supplier<Item>) supplier));
            case BLOCKS -> BLOCKS_MAP.put(id, BLOCKS.register(id, (Supplier<Block>) supplier));
            case BLOCK_ENTITIES -> BLOCK_ENTITIES_MAP.put(id, BLOCK_ENTITIES.register(id, (Supplier<BlockEntityType<?>>) supplier));
            case LEVELS -> {
                var res = ((Supplier<ResourceLocation>) supplier).get();
                LEVELS.put(id, ResourceKey.create(Registry.DIMENSION_REGISTRY, res));
                DIMENSION_TYPES.put(id, ResourceKey.create(Registry.DIMENSION_TYPE_REGISTRY, res));
            }
            default -> WaterConsole.error(WaterRegistry.class.getSimpleName(), "Failed to register missing type");
        }
    }

    public static void register() {
        WaterConsole.log("SrRegistry", "Loading WATERCoRE registry");
        SOUNDS.register(WaterCore.bus());
        POTIONS.register(WaterCore.bus());
        ITEMS.register(WaterCore.bus());
        BLOCKS.register(WaterCore.bus());
        BLOCK_ENTITIES.register(WaterCore.bus());
        WaterConsole.log("SrRegistry", "Load ended for WATERCoRE registry");
    }


    /* REGISTER FOR COMMANDS */
    @SubscribeEvent
    public static void onGameCommandRegister(RegisterCommandsEvent event) {
        BroadcastComm.register(event.getDispatcher());
        ConfigCommand.register(event.getDispatcher());
        SetLobbyPosComm.register(event.getDispatcher());
        SpawnComm.register(event.getDispatcher());
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        // MANTENIENE LA DATA ORIGINAL
        // La data original de que?
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        WaterConsole.log(getClass().toString(), "WATERCoRE running on server");
        ChatDataProvider.init();
    }

    @SubscribeEvent
    public static void onServerIsRunning(ServerStartedEvent event) {
        ChatDataProvider.init();
    }
}
