package com.timmydont.wherearetherocas.lib.utils;

import org.apache.log4j.Logger;

public class LoggerUtils {

	public static void debug(Logger logger, String message, Object... items) {
		logger.debug(String.format(message, items));
	}

	public static void warn(Logger logger, String message, Object... items) {
		logger.warn(String.format(message, items));
	}

	public static void error(Logger logger, String message, Object... items) {
		logger.error(String.format(message, items));
	}
	
	public static void error(Logger logger, Exception e, String message, Object... items) {
		logger.error(String.format(message, items), e);
	}
}
