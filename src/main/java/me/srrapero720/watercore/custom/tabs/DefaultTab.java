package me.srrapero720.watercore.custom.tabs;

import me.srrapero720.watercore.water.WaterRegistry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class DefaultTab extends CreativeModeTab {
    final String iconName;
    public DefaultTab(String label, String item_registry) {
        super(label);
        iconName = item_registry;
    }

    @Override
    public @NotNull ItemStack makeIcon() { return new ItemStack(WaterRegistry.itemOnly(iconName)); }
}
