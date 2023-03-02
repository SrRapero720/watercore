package me.srrapero720.watercore;

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
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.server.command.ConfigCommand;
import me.srrapero720.watercore.custom.commands.BroadcastComm;
import me.srrapero720.watercore.custom.config.WaterConfig;
import me.srrapero720.watercore.custom.tabs.DefaultTab;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = WaterCore.ID)
public class SrRegistry {
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

    // REGISTRY FOR DIMENSIONS
    private static final Map<String, ResourceKey<Level>> LEVELS = new HashMap<>();
    private static final Map<String, ResourceKey<DimensionType>> DIMENSION_TYPES = new HashMap<>();

    static {

        /* SOUNDS */
        SOUNDS_MAP.put("watermine", SOUNDS.register("watermine", () -> new SoundEvent(new ResourceLocation(WaterCore.ID, "watermine"))));
        SOUNDS_MAP.put("violin", SOUNDS.register("violin", () -> new SoundEvent(new ResourceLocation(WaterCore.ID, "violin"))));
        SOUNDS.register(WaterCore.BUS);

        /* POTIONS */
        POTIONS_MAP.put("blessed_1", POTIONS.register("blessed_1", () -> new BlessedPotion(SrUtil.toTicks(180), 1)));
        POTIONS_MAP.put("blessed_2", POTIONS.register("blessed_2", () -> new BlessedPotion(SrUtil.toTicks(180), 2)));
        POTIONS_MAP.put("blessed_3", POTIONS.register("blessed_3", () -> new BlessedPotion(SrUtil.toTicks(180), 3)));
        POTIONS.register(WaterCore.BUS);

        /* ITEMS */
        ITEMS_MAP.put("COPPER", ITEMS.register("coppercoin", () -> new BaseCoin(Rarity.COMMON)));
        ITEMS_MAP.put("IRON", ITEMS.register("ironcoin", () -> new BaseCoin(Rarity.COMMON)));
        ITEMS_MAP.put("GOLDEN", ITEMS.register("goldencoin", () -> new BaseCoin(Rarity.UNCOMMON)));
        ITEMS_MAP.put("DIAMOND", ITEMS.register("diamondcoin", () -> new BaseCoin(Rarity.RARE)));
        ITEMS_MAP.put("EMERALD", ITEMS.register("emeraldcoin", () -> new BaseCoin(Rarity.RARE)));
        ITEMS_MAP.put("NETHERITE", ITEMS.register("netheritecoin", () -> new BaseCoin(Rarity.EPIC)));
        ITEMS_MAP.put("PENDORITE", ITEMS.register("pendoritecoin", () -> new BaseCoin(Rarity.EPIC)));
        // TOOLS
        ITEMS_MAP.put("GODWAND", ITEMS.register("godwand", SuperWand::new));
        ITEMS_MAP.put("BANHAMMER", ITEMS.register("banhammer", BanHammer::new));
        ITEMS_MAP.put("SMALL_VIOLIN", ITEMS.register("small_violin", () -> new BaseViolin(0.75f)));
        ITEMS_MAP.put("VIOLIN", ITEMS.register("violin", () -> new BaseViolin(1.0f)));
        ITEMS.register(WaterCore.BUS);

        /* TABS */
        TABS.put("MAIN", new DefaultTab("watercore.MAIN", "COPPER"));
        TABS.put("ADMIN", new DefaultTab("watercore.ADMIN", "PENDORITE"));

        /* BLOCKS */
        //BLOCKS_MAP.put("brass_door", BLOCKS.register("brass_door", BrassDoor::new));
        BLOCKS.register(WaterCore.BUS);

        /* DIMENSIONS */
        LEVELS.put("LOBBY", ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(WaterCore.ID, "lobby")));
        // WATERMINE
        LEVELS.put("ENGY", ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(WaterCore.ID, "enginners")));
        LEVELS.put("MAGIC", ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(WaterCore.ID, "magical")));
        LEVELS.put("EVENTS", ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(WaterCore.ID, "events")));

        /* DIMENSION TYPES */
        DIMENSION_TYPES.put("LOBBY", ResourceKey.create(Registry.DIMENSION_TYPE_REGISTRY, LEVELS.get("LOBBY").getRegistryName()));
        //WATERMiNE
        DIMENSION_TYPES.put("ENGY", ResourceKey.create(Registry.DIMENSION_TYPE_REGISTRY, LEVELS.get("ENGY").getRegistryName()));
        DIMENSION_TYPES.put("MAGIC", ResourceKey.create(Registry.DIMENSION_TYPE_REGISTRY, LEVELS.get("MAGIC").getRegistryName()));
        DIMENSION_TYPES.put("EVENTS", ResourceKey.create(Registry.DIMENSION_TYPE_REGISTRY, LEVELS.get("EVENTS").getRegistryName()));

        /* CONFIG */
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, WaterConfig.SPEC, WaterCore.ID + "-server.toml");
    }

    /* POTIONS */
    public static RegistryObject<Potion> potion(String name) { return POTIONS_MAP.get(name); }
    public static Potion potionOnly(String name) { return POTIONS_MAP.get(name).get(); }

    /* SOUNDS */
    public static RegistryObject<SoundEvent> sound(String name) { return SOUNDS_MAP.get(name); }
    public static SoundEvent soundOnly(String name) {return SOUNDS_MAP.get(name).get();}

    /* ITEMS */
    public static RegistryObject<Item> item(String name) {return ITEMS_MAP.get(name);}
    public static Item itemOnly(String name) {return ITEMS_MAP.get(name).get();}

    /* TABS */
    public static CreativeModeTab tab(String name) {return TABS.get(name);}

    /* BLOCKS */
    public static RegistryObject<Block> block(String name) {return BLOCKS_MAP.get(name);}
    public static Block blockOnly(String name) {return BLOCKS_MAP.get(name).get();}
    public static RegistryObject<? extends Item> blockItemCreate(String name, Item item) {
        return ITEMS_MAP.put(name, ITEMS.register(name, () -> item));
    }

    /* DIMENSIONS */
    public static ResourceKey<Level> dimension(String name) {return LEVELS.get(name);}
    public static ResourceKey<DimensionType> dimensionType(String name) {return DIMENSION_TYPES.get(name);}


    public static void register() {
        SrConsole.log("SrRegistry", "Loaded WATERCoRE registry");
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
        SrConsole.log(getClass().toString(), "WATERCoRE running on server");
    }

    @SubscribeEvent
    public static void onServerIsRunning(ServerStartedEvent event) {
    }
}
