package com.laithailibrary.sharelibrary.session;

/**
 * Created by dumkrerng on 11/9/2559.
 */
public class SessionClientUtility {

	private static ISessionID s_sessionid = null;

	private SessionClientUtility() {}

	public static void setClientSessionID(ISessionID p_sessionid) {
		s_sessionid = p_sessionid;
	}

	public static ISessionID getClientSessionID() {
		return s_sessionid;
	}
}
