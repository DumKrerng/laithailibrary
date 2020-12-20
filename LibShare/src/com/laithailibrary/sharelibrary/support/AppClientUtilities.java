package com.laithailibrary.sharelibrary.support;

import com.laithailibrary.sharelibrary.locale.*;
import com.laithailibrary.sharelibrary.session.*;
import exc.*;

/**
 * Created with IntelliJ IDEA.
 * User: dumkrerng
 * Date: 11/2/12
 * Time: 10:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class AppClientUtilities {

	private static String s_strServerAddress = "";
	private static int s_intServerPort = 0;
	private static SessionData s_sessiondata = null;
	private static GLocaleID s_localeid = null;

	private AppClientUtilities() {}

	public static void setServerAddress(String p_strServerAddress) {
		s_strServerAddress = p_strServerAddress;
	}

	public static String getServerAddress() {
		return s_strServerAddress;
	}

	public static void setServerPort(int p_intServerPort) {
		s_intServerPort = p_intServerPort;
	}

	public static int getServerPort() {
		return s_intServerPort;
	}

	public static void setSessionData(SessionData p_sessiondata, GLocaleID p_localeid) throws GException {
		try {
			s_sessiondata = p_sessiondata;
			s_localeid = p_localeid;

			GUtilities.setProName(p_sessiondata.getProName());
			GUtilities.setProVersion(p_sessiondata.getProVer());

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public static SessionData getSessionData() {
		return s_sessiondata;
	}

	public static ISessionID getSessionID() {
		return s_sessiondata.getSessionID();
	}

	public static GLocaleID getLocaleID() {
		return s_localeid;
	}

	public static GLocale getLocale() {
		return s_localeid.getLocale();
	}
}
