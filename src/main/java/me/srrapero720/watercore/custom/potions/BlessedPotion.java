package me.srrapero720.watercore.custom.potions;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.Potion;

public class BlessedPotion extends Potion {
    public BlessedPotion(int t, int l) {
        super(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, t * l, 3, false, false),
                new MobEffectInstance(MobEffects.JUMP, t * l, 1, false, false),
                new MobEffectInstance(MobEffects.DAMAGE_BOOST, t * l, 255, false, false),
                new MobEffectInstance(MobEffects.LUCK, t * l, 255, false, false),
                new MobEffectInstance(MobEffects.NIGHT_VISION, t * l, 1, false, false),
                new MobEffectInstance(MobEffects.HEALTH_BOOST, t * l, 255, false, false),
                new MobEffectInstance(MobEffects.REGENERATION, t * l, 255, false, false),
                new MobEffectInstance(MobEffects.DIG_SPEED, t * l, 255, false, false),
                new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, t * l, 255, false, false),
                new MobEffectInstance(MobEffects.HERO_OF_THE_VILLAGE, t * l, 255, false, false),
                new MobEffectInstance(MobEffects.FIRE_RESISTANCE, t * l, 1, false, false),
                new MobEffectInstance(MobEffects.ABSORPTION, t * l, 255, false, false),
                new MobEffectInstance(MobEffects.GLOWING, t * l, 1, false, false),
                new MobEffectInstance(MobEffects.SATURATION, t * l, 255, false, false)
        );
    }


}