package me.srrapero720.watercore.custom.items;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import me.srrapero720.watercore.SrRegistry;

public class BaseCash extends Item {
    public BaseCash(Rarity rarity) {
        super(new Item.Properties().tab(SrRegistry.tab("MAIN")).stacksTo(512).rarity(rarity).setNoRepair());
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return super.getItemStackLimit(stack) != 512 ? 512 : super.getItemStackLimit(stack);
    }
}
