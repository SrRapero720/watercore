package me.srrapero720.watercore.api.minecraft;

import com.mojang.brigadier.CommandDispatcher;
import me.srrapero720.watercore.utility.Logg;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.server.permission.events.PermissionGatherEvent;
import net.minecraftforge.server.permission.nodes.PermissionNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@SuppressWarnings({"unchecked", "unused"})
@Mod.EventBusSubscriber
@Deprecated(forRemoval = true)
// THIS NEEDS TO BE REWRITED OR BEGIN A SIMPLE UTIL.
public class URegistry {
    private final String ID;
    private static boolean isInit = false;

    // STATIC STORAGE REGISTRY
    private static final List<CommandLoader> COMMANDS = new ArrayList<>();
    private static final List<PermissionNode<?>> PERMS = new ArrayList<>();

    // STATIC STORAGE REGISTRY
    private static final Map<ResourceLocation, CreativeModeTab> TABS = new HashMap<>();
    private static final Map<ResourceLocation, ResourceKey<Level>> LEVELS = new HashMap<>();
    private static final Map<ResourceLocation, ResourceKey<DimensionType>> DIMENSION_TYPES = new HashMap<>();

    // LOCAL REGISTRY
    private final DeferredRegister<Potion> POTIONS;
    private final DeferredRegister<SoundEvent> SOUNDSEV;
    private final DeferredRegister<Item> ITEMS;
    private final DeferredRegister<Block> BLOCKS;
    private final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES;

    public static void init() { if (!isInit) MinecraftForge.EVENT_BUS.register(URegistry.class); isInit = true; }

    public URegistry(String id) {
        this.ID = id;
        POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, this.ID);
        SOUNDSEV = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, this.ID);
        ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, this.ID);
        BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, this.ID);
        BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, this.ID);
    }

    public final String getId() { return ID; }

    /* ====================================================
     *                      REGISTERS
     * ==================================================== */
    public URegistry register(IEventBus bus) {
        if (!isInit) init();
        SOUNDSEV.register(bus);
        POTIONS.register(bus);
        ITEMS.register(bus);
        BLOCKS.register(bus);
        BLOCK_ENTITIES.register(bus);
        Logg.debug("Successful registered bus for {} registry", this.getId());
        return this;
    }

    public RegistryObject<Potion> registerPotion(String id, Supplier<Potion> potion) { return POTIONS.register(id, potion); }
    public RegistryObject<SoundEvent> registerSoundEv(String id, Supplier<SoundEvent> ev) { return SOUNDSEV.register(id, ev); }
    public RegistryObject<Item> registerItem(String id, Supplier<Item> item) { return ITEMS.register(id, item); }
    public RegistryObject<Block> registerBlock(String id, Supplier<Block> block) { return BLOCKS.register(id, block); }
    public <T extends BlockEntity> RegistryObject<BlockEntityType<T>> registerTile(String id, Supplier<BlockEntityType<T>> tile) {
        return BLOCK_ENTITIES.register(id, tile);
    }

    public ResourceKey<Level> registerWorld(String id, Supplier<ResourceLocation> level) {
        LEVELS.put(level.get(), ResourceKey.create(Registry.DIMENSION_REGISTRY, level.get()));
        registerWorldType(id, level);
        return LEVELS.get(level.get());
    }

    public ResourceKey<DimensionType> registerWorldType(String id, Supplier<ResourceLocation> level) {
        DIMENSION_TYPES.put(level.get(), ResourceKey.create(Registry.DIMENSION_TYPE_REGISTRY, level.get()));
        return DIMENSION_TYPES.get(level.get());
    }

    public CreativeModeTab registerTab(String id, Supplier<CreativeModeTab> tab) {
        final var t = tab.get();
        TABS.put(new ResourceLocation(ID, id), t);
        return t;
    }

    /* ====================================================
     *                  SPECIAL REGISTER
     * ==================================================== */
    public <T> PermissionNode<T> registerPermNode(String id, PermissionNode<T> permissionNode) {
        PERMS.add(permissionNode);
        return permissionNode;
    }

    public void registerComm(CommandLoader comm) { COMMANDS.add(comm); }

    public void registerConfig(ModConfig.Type type, ForgeConfigSpec spec, String filename) {
        ModLoadingContext.get().registerConfig(type, spec, filename + ".toml");
    }

    /* ====================================================
     *                      GETTERS
     * ==================================================== */
    public static SoundEvent soundEvent(ResourceLocation location) { return ForgeRegistries.SOUND_EVENTS.getValue(location); }
    public static Potion potion(ResourceLocation location) { return ForgeRegistries.POTIONS.getValue(location); }
    public static Item item(ResourceLocation location) { return ForgeRegistries.ITEMS.getValue(location); }
    public static Block block(ResourceLocation location) { return ForgeRegistries.BLOCKS.getValue(location); }
    public static <T extends BlockEntity> BlockEntityType<T> tile(ResourceLocation location) { return (BlockEntityType<T>) ForgeRegistries.BLOCK_ENTITIES.getValue(location); }

    public static <T extends BlockEntity> BlockEntityType<T> tile(ResourceLocation location, T cast) {
        return (BlockEntityType<T>) ForgeRegistries.BLOCK_ENTITIES.getValue(location);
    }

    public static ResourceKey<Level> world(ResourceLocation location) { return LEVELS.get(location); }
    public static ResourceKey<DimensionType> worldType(ResourceLocation location) { return DIMENSION_TYPES.get(location); }
    public static CreativeModeTab tab(ResourceLocation location) { return TABS.get(location); }

    /* ====================================================
     *                HANDLERS AND LISTENERS
     * ==================================================== */
    @SubscribeEvent
    public static void onCommandLoad(RegisterCommandsEvent event) { for (var comm: COMMANDS) comm.load(event.getDispatcher()); }
    @SubscribeEvent
    public static void onPermissionLoad(PermissionGatherEvent.Nodes event) { for (var node: PERMS) event.addNodes(node); }

    /* ====================================================
     *                      TOOLS
     * ==================================================== */
    public interface CommandLoader {
        void load(CommandDispatcher<CommandSourceStack> dispatcher);
    }
}
