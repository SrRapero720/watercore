package me.srrapero720.watercore;

import me.srrapero720.watercore.api.minecraft.URegistry;
import me.srrapero720.watercore.custom.commands.*;
import me.srrapero720.watercore.custom.items.BanHammer;
import me.srrapero720.watercore.custom.items.ItemCoin;
import me.srrapero720.watercore.custom.items.ItemGodWand;
import me.srrapero720.watercore.custom.items.ItenViolin;
import me.srrapero720.watercore.custom.potions.BlessedPotion;
import me.srrapero720.watercore.custom.potions.CursedPotion;
import me.srrapero720.watercore.custom.tabs.WCoreTab;
import me.srrapero720.watercore.utility.Tools;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.config.ModConfig;
import org.checkerframework.common.value.qual.IntRange;

public class WaterRegistry {
    public static final URegistry REGISTRY = new URegistry(WaterCore.ID).register(WaterCore.BUS);

    public static void init() {
        /* TABS */
        REGISTRY.registerTab("main", () -> new WCoreTab("watercore.MAIN", "ironcoin"));
        REGISTRY.registerTab("admin", () -> new WCoreTab("watercore.ADMIN", "banhammer"));

        /* SOUNDS */
        REGISTRY.registerSoundEv("watermine", () -> new SoundEvent(new ResourceLocation(WaterCore.ID, "watermine")));
        REGISTRY.registerSoundEv("violin", () -> new SoundEvent(new ResourceLocation(WaterCore.ID, "violin")));

        /* POTIONS */
        REGISTRY.registerPotion("blessed_1", () -> new BlessedPotion(Tools.toTicks(180), 1));
        REGISTRY.registerPotion("blessed_2", () -> new BlessedPotion(Tools.toTicks(180), 2));
        REGISTRY.registerPotion("blessed_3", () -> new BlessedPotion(Tools.toTicks(180), 3));
        REGISTRY.registerPotion("cursed_1", () -> new CursedPotion(Tools.toTicks(180), 1));
        REGISTRY.registerPotion("cursed_2", () -> new CursedPotion(Tools.toTicks(180), 2));
        REGISTRY.registerPotion("cursed_3", () -> new CursedPotion(Tools.toTicks(180), 3));

        /* ITEMS */
        REGISTRY.registerItem("coppercoin", () -> new ItemCoin(Rarity.COMMON));
        REGISTRY.registerItem("ironcoin", () -> new ItemCoin(Rarity.COMMON));
        REGISTRY.registerItem("goldencoin", () -> new ItemCoin(Rarity.UNCOMMON));
        REGISTRY.registerItem("diamondcoin", () -> new ItemCoin(Rarity.UNCOMMON));
        REGISTRY.registerItem("emeraldcoin", () -> new ItemCoin(Rarity.RARE));
        REGISTRY.registerItem("netheritecoin", () -> new ItemCoin(Rarity.EPIC));
        REGISTRY.registerItem("pendoritecoin", () -> new ItemCoin(Rarity.EPIC));
        REGISTRY.registerItem("godwand", ItemGodWand::new);
        REGISTRY.registerItem("banhammer", BanHammer::new);
        REGISTRY.registerItem("small_violin", () -> new ItenViolin(0.75f));
        REGISTRY.registerItem("violin", () -> new ItenViolin(1.0f));

        /* BLOCKS */
        //BLOCKS_MAP.put("brass_door", BLOCKS.register("brass_door", BrassDoor::new));

        /* DIMENSIONS */
        REGISTRY.registerWorld("lobby", () ->  new ResourceLocation(WaterCore.ID, "lobby"));
        REGISTRY.registerWorld("engineers", () -> new ResourceLocation(WaterCore.ID, "enginners"));
        REGISTRY.registerWorld("magical", () -> new ResourceLocation(WaterCore.ID, "magical"));
        REGISTRY.registerWorld("events", () -> new ResourceLocation(WaterCore.ID, "events"));

        /* LUCKPERMS CONFIGURATION REGISTER */
//        LuckyNode.register("watercore.displayname", W$SConfig.displaynameFormat());
//        LuckyNode.register("watercore.command.back.cooldown", String.valueOf(W$SConfig.backCooldown()));

        /* COMMANDS */
        REGISTRY.registerComm(BackComm::new);
        REGISTRY.registerComm(BroadcastComm::new);
        REGISTRY.registerComm(SetLobbySpawnComm::new);
        REGISTRY.registerComm(SpawnComm::new);
        REGISTRY.registerComm(WatercoreComm::new);

        /* CONFIG */
        REGISTRY.registerConfig(ModConfig.Type.SERVER, WaterConfig.SPEC, WaterCore.ID);
    }

    public static CreativeModeTab getTabMain() { return URegistry.tab(new ResourceLocation(WaterCore.ID, "main")); }
    public static CreativeModeTab getTabAdmin() { return URegistry.tab(new ResourceLocation(WaterCore.ID, "admin")); }
    public static CursedPotion getCursedPotion(@IntRange(from = 1, to = 3) int level) { return (CursedPotion) URegistry.potion(new ResourceLocation(WaterCore.ID, "cursed_" + level)); }
    public static BlessedPotion getBlessedPotion(@IntRange(from = 1, to = 3) int level) { return (BlessedPotion) URegistry.potion(new ResourceLocation(WaterCore.ID, "blessed_" + level)); }
    public static SoundEvent getViolinSound() { return URegistry.soundEvent(new ResourceLocation(WaterCore.ID, "violin")); }
    public static ResourceKey<Level> getWorldDimension(String id) { return URegistry.world(new ResourceLocation(WaterCore.ID, id)); }
}
