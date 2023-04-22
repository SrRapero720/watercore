package me.srrapero720.watercore.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Deprecated
public class WConsole {
    private static final Logger LOGGER = LoggerFactory.getLogger("WATERCoRE");
    private static final String PREFIX = "WATERCoRE: ";

    public static <T> void detect(T clazzOrObject) {
        if (clazzOrObject != null && clazzOrObject.getClass() != null) {
            LOGGER.info(PREFIX + "Instance created of " + clazzOrObject.getClass().getName());
        } else LOGGER.info(PREFIX + "Handled an null instance... WUT?");
    }

    public static <T> void detect(Class<T> clazzOrObject) {
        if (clazzOrObject != null) {
            LOGGER.info(PREFIX + "Running static of " + clazzOrObject.getName());
        } else LOGGER.info(PREFIX + "Handled an null class... ARE YOU STUPID? or this is ADVANCED JAVA?");
    }

    public static void log(String from, String log) { LOGGER.info("[WCoRE/" + from + "]: " + log); }
    public static void warn(String from, String log) { LOGGER.warn("[WCoRE/" + from + "]: " + log); }
    public static void debug(String from, String log) { LOGGER.debug("[WCoRE/" + from + "]: " + log); }
    public static void error(String from, String log) { LOGGER.error("[WCoRE/" + from + "]: " + log); }
    public static void success(String from, String log) { LOGGER.info("[WCoRE/" + from + "]: " + log); }
    public static void justPrint(String log) { LOGGER.info(log); }
}
