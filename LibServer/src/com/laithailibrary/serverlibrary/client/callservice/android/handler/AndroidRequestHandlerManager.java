package com.laithailibrary.serverlibrary.client.callservice.android.handler;

import com.laithailibrary.sharelibrary.collection.*;
import exc.*;
import java.util.*;

public class AndroidRequestHandlerManager {

	private static final Map<String, Class<? extends ANDRequestHandler>> s_mapANDRequestHandler_RequestName = new GMap<>();

	private AndroidRequestHandlerManager() {}

	public static void addRequestHandler(String p_strRequestName, Class<? extends ANDRequestHandler> p_class) throws GException {
		try {
			if(s_mapANDRequestHandler_RequestName.containsKey(p_strRequestName)) {
				throw new GException("Duplicate Request Name: " + p_strRequestName);
			}

			s_mapANDRequestHandler_RequestName.put(p_strRequestName, p_class);

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public static ANDRequestHandler newANDRequestHandlerObject(String p_strRequestName) throws GException {
		try {
			if(!s_mapANDRequestHandler_RequestName.containsKey(p_strRequestName)) {
				throw new GException("Not found Request Name: " + p_strRequestName);
			}

			Class<? extends ANDRequestHandler> aClass = s_mapANDRequestHandler_RequestName.get(p_strRequestName);

			return aClass.newInstance();

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}
}
