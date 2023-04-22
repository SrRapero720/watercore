package me.srrapero720.watercore.api.luckperms;

import me.srrapero720.watercore.api.thread.ThreadUtil;

public class LuckyNode {
    //========================================== //
    //           REGISTER NEW METANODE
    //========================================== //
    public static net.luckperms.api.node.Node registerMetaNode(String keyNode, String matchValues) {
        return ThreadUtil.tryAndReturn((defaultVar) -> {
            var clazz = Class.forName("net.luckperms.api.node.types.MetaNode");
            var method = (net.luckperms.api.node.types.MetaNode.Builder) clazz.getMethod("builder", String.class, String.class).invoke(clazz, keyNode, matchValues);

            return (net.luckperms.api.node.Node) method.getClass().getMethod("build").invoke(null);
        }, null);
    }
}
