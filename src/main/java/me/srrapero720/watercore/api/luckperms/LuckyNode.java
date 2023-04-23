package me.srrapero720.watercore.api.luckperms;

import me.srrapero720.watercore.api.thread.ThreadUtil;

import java.util.HashMap;
import java.util.Map;

public class LuckyNode {
    private final net.luckperms.api.node.Node NODE;
    private LuckyNode(Object entry) { NODE = (net.luckperms.api.node.Node) entry; }

    /**
     * casting "net.luckperms.api.node.Node" is safe, its designed like this just to prevent ClassDefNotFound
     * @return net.luckperms.api.node.Node as Object instance
     */
    public Object getLuckNode() {
        return NODE;
    }

    public String getNode() {
        if (!LuckyCore.isPresent()) throw new LuckyCore.SafeModeException();
        return NODE.getKey();
    }

    /* ====================================================
    *                REGISTER NEW META-NODE
    *  ==================================================== */
    private static final Map<String, LuckyNode> LUCKY_NODES = new HashMap<>();
    public static void register(String keyNode, String matchValues) {
        LuckyNode result = ThreadUtil.tryAndReturn((defaultVar) -> {
            var clazz = Class.forName("net.luckperms.api.node.types.MetaNode");
            var method = (net.luckperms.api.node.types.MetaNode.Builder) clazz.getMethod("builder", String.class, String.class).invoke(clazz, keyNode, matchValues);

            return new LuckyNode(method.getClass().getMethod("build").invoke(null));
        }, null);

        if (result != null) LUCKY_NODES.put(result.getNode(), result);
    }

    public static LuckyNode read(String id) {
        return LUCKY_NODES.get(id);
    }
}
