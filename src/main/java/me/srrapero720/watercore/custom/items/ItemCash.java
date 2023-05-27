package me.srrapero720.watercore.custom.items;

import me.srrapero720.watercore.WaterRegistry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

public class ItemCash extends Item {
    public ItemCash(Rarity rarity) {
        super(new Item.Properties().tab(WaterRegistry.getTabMain()).stacksTo(512).rarity(rarity).setNoRepair());
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return super.getItemStackLimit(stack) != 512 ? 512 : super.getItemStackLimit(stack);
    }
}
