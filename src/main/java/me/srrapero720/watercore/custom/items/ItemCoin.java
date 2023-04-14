package me.srrapero720.watercore.custom.items;

import me.srrapero720.watercore.internal.WRegistry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

public class ItemCoin extends Item {
    public ItemCoin(Rarity rarity) { super(new Properties().tab(WRegistry.tab("main")).stacksTo(64).rarity(rarity)); }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return super.getItemStackLimit(stack);
    }
}