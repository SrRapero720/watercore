package me.srrapero720.watercore.mixin;

import me.srrapero720.watercore.api.ChatDataProvider;
import me.srrapero720.watercore.api.IPlayerEntity;
import me.srrapero720.watercore.internal.WaterConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.*;

import java.util.Collection;


// TODO: USES player#getName() instead self playername
@Mixin(value = Player.class, priority = 0)
public abstract class PlayerMixin extends LivingEntity implements IPlayerEntity {
    @Shadow @Final private Collection<MutableComponent> prefixes;
    @Shadow @Final private Collection<MutableComponent> suffixes;
    @Shadow private Component displayname;

    protected PlayerMixin(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) { super(p_20966_, p_20967_); }

    @Override
    public Component getPlayername() { return getName(); }

    @Shadow public abstract @NotNull Component getName();
    @Shadow protected abstract MutableComponent decorateDisplayNameComponent(MutableComponent p_36219_);

    /**
     * @author SrRapero720
     * @reason No longer support forge playerDisplayName, and i need to enforce my displayname over other mods.
     */
    @Overwrite
    public @NotNull Component getDisplayName() {

        this.displayname = ChatDataProvider.createPlayerDisplayName((Player) (Object) this);

        MutableComponent mutablecomponent = new TextComponent("");
        mutablecomponent = prefixes.stream().reduce(mutablecomponent, MutableComponent::append);
        mutablecomponent = mutablecomponent.append(PlayerTeam.formatNameForTeam(this.getTeam(), this.displayname));
        mutablecomponent = suffixes.stream().reduce(mutablecomponent, MutableComponent::append);
        return this.decorateDisplayNameComponent(mutablecomponent);
    }
}
