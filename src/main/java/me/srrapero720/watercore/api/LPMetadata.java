package me.srrapero720.watercore.api;

import com.mojang.authlib.GameProfile;
import me.srrapero720.watercore.internal.WaterConsole;
import org.jetbrains.annotations.NotNull;

public class LPMetadata {
    private static net.luckperms.api.LuckPerms LP;
    private static final String NAME = LPMetadata.class.getSimpleName();

    public static boolean isLoadedApi() { return LP != null; }

    public static void init() {
        try {
            var clazz = Class.forName("net.luckperms.api.LuckPermsProvider");
            LP = net.luckperms.api.LuckPermsProvider.get();

            WaterConsole.log(NAME, "Using 'LuckPerms' as Prefix provider");
        } catch (Exception ignored) { WaterConsole.log(NAME, "Luckperms no found"); }
    }

    public static @NotNull String[] getPrefixSuffix(GameProfile player) {
        var result = new String[] { "", "" };
        try {
            var data = LP.getUserManager().getUser(player.getId()).getCachedData().getMetaData();
            if (data.getPrefix() != null) result[0] = data.getPrefix();
            if (data.getSuffix() != null) result[1] = data.getSuffix();
            return result;
        } catch(Exception ise) {
            WaterConsole.error(MCPlayerFormat.class.getName(), ise.getMessage());
            ise.printStackTrace();
        }

        return result;
    }
}
