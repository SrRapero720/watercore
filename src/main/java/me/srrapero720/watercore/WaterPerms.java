package me.srrapero720.watercore;

import me.srrapero720.watercore.api.placeholder.Placeholder;
import me.srrapero720.watercore.api.thread.ThreadUtil;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.server.permission.PermissionAPI;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import net.minecraftforge.server.permission.nodes.PermissionTypes;

import java.util.UUID;

public class WaterPerms {

    public static final PermissionNode<Integer> BACK_COOLDOWN;
    public static final PermissionNode<String> DISPLAYNAME_VALUE;


    public static final PermissionNode<Boolean> CHAT_COLORS;
    public static PermissionNode<Boolean> CHAT_STYLES;
    public static final PermissionNode<Boolean> CHAT_MARKDOWN;
    public static PermissionNode<Boolean> TABLIST_DISPLAYNAME;
    public static PermissionNode<Boolean> TABLIST_PF;

    static {
        BACK_COOLDOWN = create("watercore.command.back.cooldown", 20, "back cooldown usage", "Set custom back cooldown usage");
        DISPLAYNAME_VALUE = create("watercore.player.displayname.format", WaterConfig.displaynameFormat(), "back cooldown usage", "Set custom back cooldown usage");

        CHAT_COLORS = create("watercore.chat.colors", true, "Chat colors", "Enables/Disables colors in chat");
        CHAT_MARKDOWN = create("watercore.chat.styles", true, "Chat markdown styling", "Enables/Disables markdown styling in chat");


//        CHAT_STYLES = register("chat.styles", true, "Chat styles", "Enables/Disables styles in chat");
//        TABLIST_DISPLAYNAME = register("tablist.nickname", true, "Tab list nicknames", "Enables/Disables nicknames showing in the tab list");
//        TABLIST_PF = register("tablist.metadata", true, "Tab list metadata", "Enables/Disables prefixes&suffixes showing in the tab list");
    }

    public static void register() {}

    /* ====================================================
     *                NODE CREATORS
     * ==================================================== */
    @SuppressWarnings("unchecked")
    private static PermissionNode<Boolean> create(String node, boolean def, String title, String desc) {
        return ThreadUtil.tryAndReturn((defaultVar) -> new PermissionNode<>(WaterCore.ID, node, PermissionTypes.BOOLEAN, (player, uuid, context) -> def)
                .setInformation(new TextComponent(title), new TextComponent(Placeholder.parse(desc))), null, null);
    }

    @SuppressWarnings("unchecked")
    private static PermissionNode<Integer> create(String node, int def, String title, String desc) {
        return ThreadUtil.tryAndReturn((defaultVar) -> new PermissionNode<>(WaterCore.ID, node, PermissionTypes.INTEGER, (player, uuid, context) -> def)
                .setInformation(new TextComponent(title), new TextComponent(Placeholder.parse(desc))), null, null);
    }

    @SuppressWarnings("unchecked")
    private static PermissionNode<String> create(String node, String def, String title, String desc) {
        return ThreadUtil.tryAndReturn((defaultVar) -> new PermissionNode<>(WaterCore.ID, node, PermissionTypes.STRING, (player, uuid, context) -> def)
                .setInformation(new TextComponent(title), new TextComponent(Placeholder.parse(desc))), null, null);
    }


    /* ====================================================
     *             SIMPLE VALUE CASTERS
     * ==================================================== */
    public static int playerPermissionInt(UUID uuid, PermissionNode<Integer> node) {
        return ThreadUtil.tryAndReturn((defaultVar) ->   PermissionAPI.getOfflinePermission(uuid, node), ThreadUtil::printStackTrace, -1);
    }

    public static String playerPermissionString(UUID uuid, PermissionNode<String> node) {
        return ThreadUtil.tryAndReturn((defaultVar) -> PermissionAPI.getOfflinePermission(uuid, node), ThreadUtil::printStackTrace, "");
    }

    public static boolean playerHasPermission(UUID uuid, PermissionNode<Boolean> node) {
        return ThreadUtil.tryAndReturn((defaultVar) -> PermissionAPI.getOfflinePermission(uuid, node), ThreadUtil::printStackTrace, false);
    }
}
