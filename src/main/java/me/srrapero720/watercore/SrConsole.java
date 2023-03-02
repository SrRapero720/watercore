package me.srrapero720.watercore;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

public class SrConsole {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static void log(String from, String log) { LOGGER.info("[WCoRE/" + from + "]: " + log); }
    public static void warn(String from, String log) { LOGGER.warn("[WCoRE/" + from + "]: " + log); }
    public static void debug(String from, String log) { LOGGER.debug("[WCoRE/" + from + "]: " + log); }
    public static void error(String from, String log) { LOGGER.error("[WCoRE/" + from + "]: " + log); }
    public static void success(String from, String log) { LOGGER.info("[WCoRE/" + from + "]: " + log); }
    public static void justPrint(String log) { LOGGER.info(log); }
}
