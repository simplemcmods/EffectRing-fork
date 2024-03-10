package com.sysnote8.effectring.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sysnote8.effectring.Tags;

public class MyLog {

    private static Logger MainLogger = LogManager.getLogger(Tags.modid);

    public static void info(String str) {
        MainLogger.info(str);
    }

    public static void warn(String str) {
        MainLogger.warn(str);
    }

    public static void error(String str) {
        MainLogger.error(str);
    }
}
