package me.srrapero720.watercore.custom.tabs;

import me.srrapero720.watercore.WaterCore;
import me.srrapero720.watercore.api.minecraft.URegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Tab is only works with WATERCoRE Registry
 * If you didn't use WCoreRegistry then this tab no gonna works for you
 */
public class WCoreTab extends CreativeModeTab {
    final String iconName;
    public WCoreTab(String label, String item_registry) {
        super(label);
        iconName = item_registry;
    }

    @Override
    public @NotNull ItemStack makeIcon() { return new ItemStack(URegistry.item(new ResourceLocation(WaterCore.ID, iconName))); }
}