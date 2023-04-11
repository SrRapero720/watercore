package me.srrapero720.watercore.internal;

import me.srrapero720.watercore.api.MCTextFormat;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class WaterBans {
    private static final List<Entry> entries;
    static {
        entries = new ArrayList<>();
//        entries.add(new Entry("fancymenu", "FancyMenu", "is explicit incompatible with WATERCoRE, this is a dummy test"));
        entries.add(new Entry("memoryleakfix", "MemoryLeakFix", "is explicit incompatible with WATERCoRE, we do the same"));
    }

    public static void validate() {
        for(var entry: entries) if (WaterUtil.isModLoading(entry.id)) throw new IncompatibleModInstalled(entry.toString());
    }


    public record Entry(String id, String name, String reason) {
        @Override
        public @NotNull String toString() { return MCTextFormat.parse("&6&l".concat(this.name.concat(" &c".concat(this.reason)))); }
    }

    public static final class IncompatibleModInstalled extends RuntimeException {
        public IncompatibleModInstalled(String info) { super(info); }
    }
}
