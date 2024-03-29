package me.srrapero720.watercore.internal;

import me.srrapero720.watercore.api.placeholder.Placeholder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class WCoreBanned {
    private static final List<Entry> entries = new ArrayList<>();
    static {
        entries.add(new Entry("memoryleakfix", "MemoryLeakFix", "is explicit incompatible with WATERCoRE, we do the same"));
    }

    public static void validate() {
        for(var entry: entries) if (WCoreUtil.isModFMLoading(entry.id)) throw new IncompatibleModInstalled(entry.toString());
    }


    public record Entry(String id, String name, String reason) {
        @Override
        public @NotNull String toString() { return Placeholder.parse("&6&l".concat(this.name.concat(" &c".concat(this.reason)))); }
    }

    public static final class IncompatibleModInstalled extends RuntimeException {
        public IncompatibleModInstalled(String info) { super(info); }
    }
}
