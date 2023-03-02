package me.srrapero720.watercore.custom.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.level.Level;

public class StupidSheep extends Sheep {
    public StupidSheep(EntityType<? extends Sheep> p_29806_, Level p_29807_) {
        super(p_29806_, p_29807_);
    }

    @Override
    public void tick() {
        super.tick();
    }
}
