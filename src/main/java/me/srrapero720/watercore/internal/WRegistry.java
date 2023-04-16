package me.srrapero720.watercore.internal;

import me.srrapero720.watercore.WaterCore;
import me.srrapero720.watercore.api.luckperms.LPMetadata;
import me.srrapero720.watercore.custom.commands.*;
import me.srrapero720.watercore.custom.items.BanHammer;
import me.srrapero720.watercore.custom.items.ItemCoin;
import me.srrapero720.watercore.custom.items.ItenViolin;
import me.srrapero720.watercore.custom.items.ItemGodWand;
import me.srrapero720.watercore.custom.potions.BlessedPotion;
import me.srrapero720.watercore.custom.potions.CursedPotion;
import me.srrapero720.watercore.custom.tabs.SmartCreativeTab;
import me.srrapero720.watercore.internal.forge.W$ServerConfig;
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
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = WaterCore.ID)
public class WRegistry {
    public enum Type { TABS, SOUND, POTION, ITEM, BLOCKS, BLOCK_ENTITIES, LEVELS}
    private static final Map<String, WRegistry> REGISTRIES = new HashMap<>();
    private final String MOD_ID;

    // REGISTRY FOR TABS [STATIC]
    private static final Map<String, CreativeModeTab> TABS = new HashMap<>();

    // REGISTRY FOR POTIONS
    private final DeferredRegister<Potion> POTIONS;
    private final Map<String, RegistryObject<Potion>> POTIONS_MAP = new HashMap<>();

    // REGISTRY FOR SOUNDS
    private final DeferredRegister<SoundEvent> SOUNDS;
    private final Map<String, RegistryObject<SoundEvent>> SOUNDS_MAP = new HashMap<>();

    // REGISTRY FOR ITEMS
    private final DeferredRegister<Item> ITEMS;
    private final Map<String, RegistryObject<Item>> ITEMS_MAP = new HashMap<>();

    // REGISTRY FOR BLOCKS
    private final DeferredRegister<Block> BLOCKS;
    private final Map<String, RegistryObject<Block>> BLOCKS_MAP = new HashMap<>();

    private final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES;
    private final Map<String, RegistryObject<BlockEntityType<?>>> BLOCK_ENTITIES_MAP = new HashMap<>();

    // REGISTRY FOR DIMENSIONS
    private final Map<String, ResourceKey<Level>> LEVELS = new HashMap<>();
    private final Map<String, ResourceKey<DimensionType>> DIMENSION_TYPES = new HashMap<>();

    public WRegistry(String modId) {
        this.MOD_ID = modId;
        POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, this.MOD_ID);
        SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, this.MOD_ID);
        ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, this.MOD_ID);
        BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, this.MOD_ID);
        BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, this.MOD_ID);

        REGISTRIES.put(modId, this);
    }
    public String getModId() { return MOD_ID; }
    public void register(IEventBus bus) {
        SOUNDS.register(bus);
        POTIONS.register(bus);
        ITEMS.register(bus);
        BLOCKS.register(bus);
        BLOCK_ENTITIES.register(bus);
    }

    static {
        /* TABS */
        register(Type.TABS,"main", () -> new SmartCreativeTab("watercore.MAIN", "ironcoin"));
        register(Type.TABS,"admin", () -> new SmartCreativeTab("watercore.ADMIN", "banhammer"));

        /* SOUNDS */
        register(Type.SOUND,"watermine", () -> new SoundEvent(new ResourceLocation(WaterCore.ID, "watermine")));
        register(Type.SOUND,"violin", () -> new SoundEvent(new ResourceLocation(WaterCore.ID, "violin")));

        /* POTIONS */
        register(Type.POTION,"blessed_1", () -> new BlessedPotion(WUtil.toTicks(180), 1));
        register(Type.POTION,"blessed_2", () -> new BlessedPotion(WUtil.toTicks(180), 2));
        register(Type.POTION,"blessed_3", () -> new BlessedPotion(WUtil.toTicks(180), 3));

        register(Type.POTION,"cursed_1", () -> new CursedPotion(WUtil.toTicks(180), 1));
        register(Type.POTION,"cursed_2", () -> new CursedPotion(WUtil.toTicks(180), 2));
        register(Type.POTION,"cursed_3", () -> new CursedPotion(WUtil.toTicks(180), 3));

        /* ITEMS */
        register(Type.ITEM,"coppercoin", () -> new ItemCoin(Rarity.COMMON));
        register(Type.ITEM,"ironcoin", () -> new ItemCoin(Rarity.COMMON));
        register(Type.ITEM,"goldencoin", () -> new ItemCoin(Rarity.UNCOMMON));
        register(Type.ITEM,"diamondcoin", () -> new ItemCoin(Rarity.UNCOMMON));
        register(Type.ITEM,"emeraldcoin", () -> new ItemCoin(Rarity.RARE));
        register(Type.ITEM,"netheritecoin", () -> new ItemCoin(Rarity.EPIC));
        register(Type.ITEM,"pendoritecoin", () -> new ItemCoin(Rarity.EPIC));

        register(Type.ITEM,"godwand", ItemGodWand::new);
        register(Type.ITEM,"banhammer", BanHammer::new);
        register(Type.ITEM,"small_violin", () -> new ItenViolin(0.75f));
        register(Type.ITEM,"violin", () -> new ItenViolin(1.0f));

        /* BLOCKS */
        //BLOCKS_MAP.put("brass_door", BLOCKS.register("brass_door", BrassDoor::new));

        /* DIMENSIONS */
        register(Type.LEVELS,"lobby", () -> new ResourceLocation(WaterCore.ID, "lobby"));
        register(Type.LEVELS,"engineers", () -> new ResourceLocation(WaterCore.ID, "enginners"));
        register(Type.LEVELS,"magical", () -> new ResourceLocation(WaterCore.ID, "magical"));
        register(Type.LEVELS,"events", () -> new ResourceLocation(WaterCore.ID, "events"));

        /* CONFIG */
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, W$ServerConfig.SPEC, WaterCore.ID + "-server.toml");
    }

    /* POTIONS GETTERS */
    public RegistryObject<Potion> potion(String name) { return POTIONS_MAP.get(name); }
    public @NotNull Potion potionOnly(String name) { return POTIONS_MAP.get(name).get(); }

    //STATIC POTION
    public static @Nullable Potion findPotionOnly(String name) {
        var result = findPotion(name);
        if (result != null) return result.get(); else return null;
    }
    public static @Nullable RegistryObject<Potion> findPotion(String name) {
        for (var reg: REGISTRIES.values()) if (reg.potion(name) != null) return reg.potion(name);
        return null;
    }

    /* SOUNDS GETTERS */
    public RegistryObject<SoundEvent> sound(String name) { return SOUNDS_MAP.get(name); }
    public @NotNull SoundEvent soundOnly(String name) { return SOUNDS_MAP.get(name).get(); }

    //STATIC SOUND
    public static @Nullable SoundEvent findSoundOnly(String name) {
        var result = findSound(name);
        if (result != null) return result.get(); else return null;
    }
    public static @Nullable RegistryObject<SoundEvent> findSound(String name) {
        for (var reg: REGISTRIES.values()) if (reg.sound(name) != null) return reg.sound(name);
        return null;
    }

    /* ITEMS GETTERS */
    public RegistryObject<Item> item(String name) { return ITEMS_MAP.get(name); }
    public @NotNull Item itemOnly(String name) { return ITEMS_MAP.get(name).get(); }

    //STATIC ITEMS
    public static @Nullable Item findItemOnly(String name) {
        var result = findItem(name);
        if (result != null) return result.get(); else return null;
    }
    public static @Nullable RegistryObject<Item> findItem(String name) {
        for (var reg: REGISTRIES.values()) if (reg.item(name) != null) return reg.item(name);
        return null;
    }


    /* TABS GETTERS */
    public CreativeModeTab tabOnly(String name) { return tab(name); }
    public static CreativeModeTab tab(String name) { return TABS.get(name); }

    /* BLOCKS GETTERS */
    public RegistryObject<Block> block(String name) { return BLOCKS_MAP.get(name); }
    public @NotNull Block blockOnly(String name) { return BLOCKS_MAP.get(name).get(); }

    //STATIC BLOCKS
    public static @Nullable Block findBlockOnly(String name) {
        var result = findBlock(name);
        if (result != null) return result.get(); else return null;
    }
    public static @Nullable RegistryObject<Block> findBlock(String name) {
        for (var reg: REGISTRIES.values()) if (reg.block(name) != null) return reg.block(name);
        return null;
    }


    /* BLOCK ENTITIES GETTERS */
    public RegistryObject<BlockEntityType<?>> blockEntity(String name) {return BLOCK_ENTITIES_MAP.get(name); }
    public @NotNull BlockEntityType<?> blockEntityOnly(String name) { return BLOCK_ENTITIES_MAP.get(name).get(); }

    //STATIC BLOCK ENTITIES
    public static @Nullable BlockEntityType<?> findBlockEntityOnly(String name) {
        var result = findBlockEntity(name);
        if (result != null) return result.get(); else return null;
    }
    public static @Nullable RegistryObject<BlockEntityType<?>> findBlockEntity(String name) {
        for (var reg: REGISTRIES.values()) if (reg.blockEntity(name) != null) return reg.blockEntity(name);
        return null;
    }


    /* DIMENSIONS GETTERS */
    public ResourceKey<Level> dimension(String name) { return LEVELS.get(name);}
    public ResourceKey<DimensionType> dimensionType(String name) { return DIMENSION_TYPES.get(name); }

    //STATIC DIMENSION
    public static @Nullable ResourceKey<Level> findDimension(String name) {
        for (var reg: REGISTRIES.values()) if (reg.dimension(name) != null) return reg.dimension(name);
        return null;
    }

    //STATIC DIMENSION
    public static @Nullable ResourceKey<DimensionType> findDimensionType(String name) {
        for (var reg: REGISTRIES.values()) if (reg.dimensionType(name) != null) return reg.dimensionType(name);
        return null;
    }


    public static void register(Type type, String id, @NotNull Supplier<?> supplier) {
        var main = REGISTRIES.get(WaterCore.ID) == null ? new WRegistry(WaterCore.ID) : REGISTRIES.get(WaterCore.ID);
        main.register(type, new ResourceLocation(WaterCore.ID, id), supplier);
    }

    @SuppressWarnings("unchecked")
    // TODO: Recreate this again to support advance register types
    public void register(@NotNull Type type, @NotNull ResourceLocation location, @NotNull Supplier<?> supplier) {
        var id = location.getPath();
        switch (type) {
            case TABS -> TABS.put(id, ((Supplier<CreativeModeTab>) supplier).get());
            case SOUND -> SOUNDS_MAP.put(id, SOUNDS.register(id, (Supplier<SoundEvent>) supplier));
            case POTION -> POTIONS_MAP.put(id,POTIONS.register(id, (Supplier<Potion>) supplier));
            case ITEM -> ITEMS_MAP.put(id, ITEMS.register(id, (Supplier<Item>) supplier));
            case BLOCKS -> BLOCKS_MAP.put(id, BLOCKS.register(id, (Supplier<Block>) supplier));
            case BLOCK_ENTITIES -> BLOCK_ENTITIES_MAP.put(id, BLOCK_ENTITIES.register(id, () ->
                    ((Supplier<BlockEntityType.Builder<?>>) supplier).get().build(null)));
            case LEVELS -> {
                var res = ((Supplier<ResourceLocation>) supplier).get();
                LEVELS.put(id, ResourceKey.create(Registry.DIMENSION_REGISTRY, res));
                DIMENSION_TYPES.put(id, ResourceKey.create(Registry.DIMENSION_TYPE_REGISTRY, res));
            }
            default -> WConsole.error(WRegistry.class.getSimpleName(), "Failed to register missing type");
        }
    }


    public static void register() {
        WConsole.log("WaterRegistry", "Loading WATERCoRE registries");
        for (var main: REGISTRIES.values()) {
            WConsole.log("WaterRegistry", "Reading " + main.getModId());
            main.register(WaterCore.bus());
        }
        WConsole.log("WaterRegistry", "all WATERCoRE registries are loaded");
    }


    /* REGISTER FOR COMMANDS */
    @SubscribeEvent
    public static void onGameCommandRegister(RegisterCommandsEvent event) {
        BackComm.register(event.getDispatcher());
        BroadcastComm.register(event.getDispatcher());
        SetLobbySpawnComm.register(event.getDispatcher());
        SpawnComm.register(event.getDispatcher());
        WatercoreComm.register(event.getDispatcher());
//        ConfigCommand.register(event.getDispatcher());

    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        // MANTENIENE LA DATA ORIGINAL
        // La data original de que?
    }
    @SubscribeEvent
    public static void onPlayerXD(LivingDeathEvent event) {
        // MANTENIENE LA DATA ORIGINAL
        // La data original de que?
    }


    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        WConsole.log(getClass().toString(), "WATERCoRE running on server");
        LPMetadata.init();
    }



    @SubscribeEvent
    public static void onServerIsRunning(ServerStartedEvent event) {
        LPMetadata.init();
    }
}
