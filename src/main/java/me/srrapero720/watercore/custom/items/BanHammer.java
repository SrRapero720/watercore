package me.srrapero720.watercore.custom.items;

import me.srrapero720.watercore.api.MCTextFormat;
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
        if (entity instanceof Player banned) {
            final var l2 = new LightningBolt(EntityType.LIGHTNING_BOLT, banned.level);
            l2.setDamage(24);
            l2.setPos(banned.position());
            banned.level.addFreshEntity(l2);
            banned.kill();

            WaterUtil.runInNewThread(() -> {
                try {
                    Thread.sleep(500);
                    if (banned.isDeadOrDying()) banned.kill();

                    // PREVENTS TOMBS NO GET APPEARING
                    Thread.sleep(2000);
                    var playerList = banned.level.getServer().getPlayerList();
                    var banReason = new TranslatableComponent("wc.response.banhammer").getString();

                    if (!playerList.getBans().isBanned(banned.getGameProfile()))
                        playerList.getBans().add(new UserBanListEntry(banned.getGameProfile(), null, "WATERCoRE", null, banReason));

                    playerList.getPlayerByName(player.getName().getString()).connection.disconnect(new TranslatableComponent("wc.response.banhammer"));
                    playerList.broadcastMessage(
                            new TranslatableComponent("item.watercore.banhammer.broadcast",
                                    WaterUtil.getBroadcastPrefix("&6"),
                                    MCTextFormat.parse("&c") + banned.getName().getString(),
                                    MCTextFormat.parse("&6")),
                            ChatType.SYSTEM, Util.NIL_UUID);

                } catch (Exception e) { WaterConsole.warn("BanHammer", "Ocurrio un error al banear al usuario"); }
            });
        } else entity.remove(Entity.RemovalReason.DISCARDED);

        return super.onLeftClickEntity(stack, player, entity);
    }
}
