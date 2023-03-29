package me.srrapero720.watercore.custom.tabs;

import me.srrapero720.watercore.internal.WaterRegistry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SmartCreativeTab extends CreativeModeTab {
    final String iconName;
    public SmartCreativeTab(String label, String item_registry) {
        super(label);
        iconName = item_registry;
    }

    @Override
    public @NotNull ItemStack makeIcon() { return new ItemStack(WaterRegistry.findItemOnly(iconName)); }
}
