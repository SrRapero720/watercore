package me.srrapero720.watercore;

import me.srrapero720.watercore.api.forge.Forge;
import me.srrapero720.watercore.api.placeholder.Placeholder;
import me.srrapero720.watercore.utility.Tools;
import me.srrapero720.watercore.utility.Logg;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.ArrayList;
import java.util.List;

@Mod(WaterCore.ID)
public class WaterCore {
    public static final String PROTOCOL = "1.2";
    public static final String ID = "watercore";
    public static final IEventBus BUS = Forge.getEventBus();
    public WaterCore() {
        Forge.addFMLListener(ValidateState::new);
        WCoreRegistry.init();
        Logg.warn(":: Setup completed :::");
    }

    /* ====================================================
     *                    INTERNAL STUFF
     * ==================================================== */
    public static class ValidateState {
        public ValidateState(FMLCommonSetupEvent event) {
            List<Entry> entries = new ArrayList<>();
            entries.add(new Entry("memoryleakfix", "MemoryLeakFix", "is explicit incompatible with WATERCoRE, we do the same"));

            for(var entry: entries) if (Tools.isModFMLoading(entry.id)) throw new IllegalStateException(entry.msg());
        }

        public record Entry(String id, String name, String reason) {
            public String msg() { return Placeholder.parse("&6&l".concat(this.name.concat(" &c".concat(this.reason)))); }
        }
    }
}
