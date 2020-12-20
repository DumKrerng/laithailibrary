package com.laithailibrary.logger;

import java.io.*;
import java.util.logging.*;

/**
 * Created with IntelliJ IDEA.
 * User: dumkrerng
 * Date: 12/12/12
 * Time: 11:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class GLogManager {
	/**
	 * Defining a log level. Level.ALL logs all levels of messages.
	 */
	private GLogManager() {}

	public static void setup() throws IOException {
		try {
			File folderLog = new File("log");

			if(folderLog.exists() == false) {
				if(folderLog.isDirectory() == false) {
					folderLog.mkdir();
				}
			}

			FileInputStream configFile = new FileInputStream("Logging.properties");
			LogManager.getLogManager().readConfiguration(configFile);

			configFile.close();

		} catch (IOException ex) {
			System.out.println("WARNING: Could not open configuration file");
			System.out.println("WARNING: Logging not configured (console output only)");

			System.exit(14);
		}
	}
}
