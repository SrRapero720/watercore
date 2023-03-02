package me.srrapero720.watercore.custom.items;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import me.srrapero720.watercore.SrRegistry;

public class BaseCoin extends Item {
    public BaseCoin(Rarity rarity) { super(new Properties().tab(SrRegistry.tab("MAIN")).stacksTo(64).rarity(rarity)); }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return super.getItemStackLimit(stack);
    }
}