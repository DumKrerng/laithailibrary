package com.laithailibrary.logger;

import exc.ExceptionHandler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Created with IntelliJ IDEA.
 * User: dumkrerng
 * Date: 12/14/12
 * Time: 10:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class GLogFormatter extends Formatter {

	public synchronized String format(LogRecord record) {
		String recordStr = "";

		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
			String strCurrentDate = df.format(new java.util.Date());

			recordStr = "[" + strCurrentDate + "]" + " " + record.getLevel() + ":" + " " + record.getMessage() + "\n";

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		}

		return recordStr;
	}
}
