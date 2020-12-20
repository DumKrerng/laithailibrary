package com.laithailibrary.logger;

import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: dumkrerng
 * Date: 12/14/12
 * Time: 10:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class GLog {

	public final static Logger LOGGER = Logger.getLogger(GLog.class.getName());

	private GLog() {}

	public static void info(String p_strString) {
		try {
		  LOGGER.info(p_strString);

		} catch (Exception exception) {
		  exception.printStackTrace();
		}
	}

	public static void severe(String p_strString) {
		try {
		  LOGGER.severe(p_strString);

		} catch (Exception exception) {
		  exception.printStackTrace();
		}
	}

	public static void warning(String p_strString) {
		try {
		  LOGGER.warning(p_strString);

		} catch (Exception exception) {
		  exception.printStackTrace();
		}
	}
}
