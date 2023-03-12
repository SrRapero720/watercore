package me.srrapero720.watercore.custom.items;

import me.srrapero720.watercore.internal.WaterRegistry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

public class BaseCoin extends Item {
    public BaseCoin(Rarity rarity) { super(new Properties().tab(WaterRegistry.tab("MAIN")).stacksTo(64).rarity(rarity)); }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return super.getItemStackLimit(stack);
    }
}