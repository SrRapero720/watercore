package me.srrapero720.watercore.custom.items;

import me.srrapero720.watercore.internal.WaterRegistry;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import me.srrapero720.watercore.internal.WaterUtil;
import org.jetbrains.annotations.NotNull;

public class SuperWand extends Item {
    public SuperWand() {
        super(new Properties().tab(WaterRegistry.tab("ADMIN")).stacksTo(1).rarity(Rarity.EPIC).fireResistant().setNoRepair());
    }
    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level world, Player player, @NotNull InteractionHand interactionHand) {
        if (interactionHand.equals(InteractionHand.MAIN_HAND)) {
            final var hit =  player.pick(64, 0f, false);
            final var pos = ((BlockHitResult) hit).getBlockPos();
            final var l1 = new LightningBolt(EntityType.LIGHTNING_BOLT, world);
            final var l2 = new LightningBolt(EntityType.LIGHTNING_BOLT, world);
            l1.setDamage(24);
            l2.setDamage(24);

            if (hit.getType() == HitResult.Type.MISS) {
                l1.setPos(player.getX() + (Math.random() * 50), player.getY() - 30, player.getZ() - (Math.random() * 50));
                l2.setPos(player.getX() - (Math.random() * 50), player.getY() - 30, player.getZ() + (Math.random() * 50));
            } else {
                l1.setPos(pos.getX(), pos.getY(), pos.getZ());
                l2.setPos(pos.getX(), pos.getY(), pos.getZ());
            }

            world.addFreshEntity(l1);
            world.addFreshEntity(l2);
        } else applyGodEffects(player);
        return super.use(world, player, interactionHand);
    }

    public void applyGodEffects(Player player) {
        player.addEffect(new MobEffectInstance(MobEffects.LEVITATION, WaterUtil.toTicks(0.5), 3, false, false, false));
        player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, WaterUtil.toTicks(7), 5, false, false, false));

        for (MobEffectInstance i: WaterRegistry.potionOnly("blessed_3").getEffects())
            player.addEffect(new MobEffectInstance(i.getEffect(), i.getDuration(), i.getAmplifier(), i.isAmbient(), i.isVisible(), false));
    }

    @Override
    public boolean isFoil(ItemStack p_41453_) { return true; }
    @Override
    public boolean isDamageable(ItemStack stack) {return false; }
    @Override
    public boolean isFireResistant() { return false; }
}