package com.laithailibrary.sharelibrary.thread;

import exc.*;

/**
 * Created by dumkrerng on 4/5/2561.
 */
public class GThreadManager {

	private GThreadManager() {}

	public static boolean hasStackTrace(String p_strClassName, String p_strMethodName) throws GException {
		try {
			StackTraceElement[] traceelements = Thread.currentThread().getStackTrace();

			for(StackTraceElement trace : traceelements) {
				String strClassName = trace.getClassName();
				String strMethodName = trace.getMethodName();

				if(strClassName.compareTo(p_strClassName) == 0
					&& strMethodName.compareTo(p_strMethodName) == 0) {

					return true;
				}
			}

			return false;

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public static boolean hasStackTrace(String p_strClassName) throws GException {
		try {
			StackTraceElement[] traceelements = Thread.currentThread().getStackTrace();

			for(StackTraceElement trace : traceelements) {
				String strClassName = trace.getClassName();

				if(strClassName.compareTo(p_strClassName) == 0) {
					return true;
				}
			}

			return false;

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}
}
