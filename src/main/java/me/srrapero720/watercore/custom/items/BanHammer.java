package me.srrapero720.watercore.custom.items;

import me.srrapero720.watercore.internal.WaterRegistry;
import me.srrapero720.watercore.internal.WaterConsole;
import net.minecraft.Util;
import net.minecraft.network.chat.*;
import net.minecraft.server.players.UserBanListEntry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import me.srrapero720.watercore.internal.WaterUtil;

public class BanHammer extends Item {
    public BanHammer() {
        super(new Properties().tab(WaterRegistry.tab("admin")).stacksTo(1).rarity(Rarity.EPIC).fireResistant().setNoRepair().defaultDurability(1));
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        if (entity instanceof Player) {
            final var entitypos = entity.position();
            final var l2 = new LightningBolt(EntityType.LIGHTNING_BOLT, player.level);
            l2.setDamage(24);
            l2.setPos(entitypos);
            entity.level.addFreshEntity(l2);
            entity.kill();

            var t = new Thread(() -> {
                try {
                    // PREVENTS TOMBS NO GET APPEARING IF PLAYER REVIVE EXISTS
                    Thread.sleep(500);
                    if (!((Player) entity).isDeadOrDying()) entity.kill();

                    // PREVENTS TOMBS NO GET APPEARING
                    Thread.sleep(2000);
                    var server = entity.level.getServer();
                    var playerList = server.getPlayerList();
                    var bans = playerList.getBans();
                    var mPlayer = (Player) entity;
                    var banReason = new TranslatableComponent("watercore.response.banhammer").getString();

                    if (!bans.isBanned(mPlayer.getGameProfile()))
                        bans.add(new UserBanListEntry(mPlayer.getGameProfile(),null, "WATERCoRE", null, banReason));

                    playerList.getPlayer(entity.getUUID()).connection.disconnect(new TranslatableComponent("watercore.response.banhammer"));
                    playerList.broadcastMessage(new TextComponent(WaterUtil.getBroadcastPrefix() + "&6El jugador §4" + entity.getName().getString() + "§6 fue golpeado por el martillo del BAN"), ChatType.SYSTEM, Util.NIL_UUID);
                } catch (Exception e) { WaterConsole.warn("BanHammer", "Ocurrio un error al banear al usuario"); }
            });
            t.start();
        } else entity.remove(Entity.RemovalReason.DISCARDED);

        return super.onLeftClickEntity(stack, player, entity);
    }
}
