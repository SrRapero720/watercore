package me.srrapero720.watercore.custom.potions;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.Potion;

public class CursedPotion extends Potion {
    public CursedPotion(int t, int l) {
        super(
                new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, t * l, 255, false, false),
                new MobEffectInstance(MobEffects.BAD_OMEN, t * l, 1, false, false),
                new MobEffectInstance(MobEffects.UNLUCK, t * l, 255, false, false),
                new MobEffectInstance(MobEffects.WEAKNESS, t * l, 255, false, false),
                new MobEffectInstance(MobEffects.CONFUSION, t * l, 255, false, false),
                new MobEffectInstance(MobEffects.BLINDNESS, t * l, 1, false, false),
                new MobEffectInstance(MobEffects.POISON, t * l, 255, false, false),
                new MobEffectInstance(MobEffects.GLOWING, t * l, 1, false, false),
                new MobEffectInstance(MobEffects.SLOW_FALLING, t * l, 255, false, false),
                new MobEffectInstance(MobEffects.HUNGER, t * l, 255, false, false)
        );
    }
}