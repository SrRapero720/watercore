package me.srrapero720.watercore.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WLogger {
    private static final String PREFIX = "WATERCoRE";
    private static final Logger LOGGER = LoggerFactory.getLogger(PREFIX);

    public static <T> void detect(T object) {
        if (object != null && object.getClass() != null) LOGGER.info("Instance created of " + object.getClass().getName());
        else LOGGER.info("Handled an null instance... WUT?");
    }

    public static <T> void detect(Class<T> clazz) {
        if (clazz != null) LOGGER.info("Running static of " + clazz.getName());
        else LOGGER.info("Handled an null class... ARE YOU STUPID? or this is ADVANCED JAVA?");
    }

    public static void log(String log) { LOGGER.info(log); }
    public static void warn(String log) { LOGGER.warn(log); }
    public static void debug(String log) { LOGGER.debug(log); }
    public static void error(String log) { LOGGER.error(log); }
    public static void success(String log) { LOGGER.info(log); }
    public static void justPrint(String log) { LOGGER.info(log); }

    public static void log(String log, Object... args) { LOGGER.info(log, args); }
    public static void warn(String log, Object... args) { LOGGER.warn(log, args); }
    public static void debug(String log, Object... args) { LOGGER.debug(log, args); }
    public static void error(String log, Object... args) { LOGGER.error(log, args); }
    public static void success(String log, Object... args) { LOGGER.info(log, args); }
    public static void justPrint(String log, Object... args) { LOGGER.info(log, args); }
}
