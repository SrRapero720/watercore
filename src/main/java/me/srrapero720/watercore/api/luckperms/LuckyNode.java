package me.srrapero720.watercore.api.luckperms;

import me.srrapero720.watercore.api.thread.ThreadUtil;

public class LuckyNode {
    //========================================== //
    //           REGISTER NEW METANODE
    //========================================== //
    public static net.luckperms.api.node.Node registerMetaNode(String keyNode, String matchValues) {
        return ThreadUtil.tryAndReturn((defaultVar) -> {
            var clazz = Class.forName("net.luckperms.api.node.types.MetaNode");
            return (net.luckperms.api.node.Node) clazz.getMethod("builder", String.class, String.class)
                    .invoke(clazz, keyNode, matchValues);
        }, null);
    }
}
