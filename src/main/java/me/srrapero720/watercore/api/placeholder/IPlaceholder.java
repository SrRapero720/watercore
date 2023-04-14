package me.srrapero720.watercore.api.placeholder;

public abstract class IPlaceholder {
    protected final String ID;

    public IPlaceholder(String id) { this.ID = id; }

    public abstract String parse(String pattern, Object... any);
}
