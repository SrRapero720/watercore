package me.srrapero720.watercore.custom.items;

import me.srrapero720.watercore.internal.WaterRegistry;
import me.srrapero720.watercore.internal.WaterConsole;
import net.minecraft.network.protocol.game.ClientboundStopSoundPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import me.srrapero720.watercore.WaterCore;
import org.jetbrains.annotations.NotNull;

public class BaseViolin extends Item {
    private boolean playing = false;
    private final float pitch;
    public BaseViolin(float pitch) {
        super(new Properties().rarity(Rarity.EPIC).tab(WaterRegistry.tab("main")));
        this.pitch = pitch;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level world, @NotNull Player player, InteractionHand hand) {
        player.startUsingItem(hand);
        return super.use(world, player, hand);
    }

    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack p_40678_) {return UseAnim.BOW;}

    @Override
    public boolean canContinueUsing(ItemStack oldStack, ItemStack newStack) { return true; }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        final var soundEv = WaterRegistry.soundOnly("violin");
        context.getLevel().playSound(context.getPlayer(), context.getPlayer(), soundEv, SoundSource.PLAYERS, 0.75f, pitch);
        return super.onItemUseFirst(stack, context);
    }

    @Override
    public void onUsingTick(ItemStack stack, LivingEntity player, int count) {
        WaterConsole.warn("BaseViolin", "Running onUsingTick");
        super.onUsingTick(stack, player, count);
    }

    @Override
    public void releaseUsing(ItemStack itemStack, Level level, LivingEntity livingEntity, int unknownVar) {
        // final var soundEv = SrRegistry.soundOnly("violin");
//        level.playSound((Player) livingEntity, livingEntity, soundEv, SoundSource.PLAYERS, pitch, 1.0f);
        var packet = new ClientboundStopSoundPacket(new ResourceLocation(WaterCore.ID, "violin"), SoundSource.PLAYERS);

        for(ServerPlayer serverplayer : level.getServer().getPlayerList().getPlayers()) {
            serverplayer.connection.send(packet);
        }

        super.releaseUsing(itemStack, level, livingEntity, unknownVar);
    }

    @Override
    public boolean useOnRelease(ItemStack stack) {
        WaterConsole.warn("BaseViolin", "Running useOnRelease");
        return super.useOnRelease(stack);
    }
}
