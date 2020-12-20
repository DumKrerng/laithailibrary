package com.laithailibrary.serverlibrary.client.clientsession;

import java.util.*;
import com.laithailibrary.logger.*;
import com.laithailibrary.sharelibrary.clientInfo.*;
import com.laithailibrary.sharelibrary.clientinfo.*;
import com.laithailibrary.sharelibrary.collection.*;
import com.laithailibrary.sharelibrary.db.dbobject.*;
import com.laithailibrary.sharelibrary.servicecall.*;
import com.laithailibrary.sharelibrary.session.*;
import com.laithailibrary.sharelibrary.support.*;
import exc.*;

public class SessionManager {

	private static final String LOCK = "lock";
	private static final String LOCK_GenerateSessionID = "GenerateSessionID";

	private static final Map<ISessionID, ISessionData> s_mapSessionData = new GMap<>();
	private static final Map<ISessionID, DBDateTime> s_mapLastActive_SessionID = new GMap<>();

	private static final int LIMITSESSION = 10000;

	private SessionManager() {}

	public static SessionID createSessionID(FirstConnect p_firstconnect) throws GException {
		try {
			synchronized(LOCK) {
				if(s_mapSessionData.size() > LIMITSESSION) {
					throw new GException("Client-Session over limit.\nSession Limit: " + LIMITSESSION);
				}

				String strAPPTYPE = "PC";
				ClientInfo clientinfo = p_firstconnect.getClientInfo();
				String strMAC = clientinfo.getMAC();

				if(strMAC.startsWith("WEB")) {
					strAPPTYPE = "WEB";

				} else if(strMAC.startsWith("API")) {
					strAPPTYPE = "API";
				}

				SessionID sessionid = generateSessionID(strAPPTYPE);

				while(s_mapSessionData.containsKey(sessionid)) {
					sessionid = generateSessionID(strAPPTYPE);
				}

				SessionData sessiondata = new SessionData(sessionid, p_firstconnect, GUtilities.getProName(), GUtilities.getProVersion());

				s_mapSessionData.put(sessionid, sessiondata);

				return sessionid;
			}
		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public static ANDSessionID createSessionID_Android(ANDClientInfo p_clientinfo) throws GException {
		try {
			synchronized(LOCK) {
				ANDSessionID sessionid = generateSessionID_Android();

				while(s_mapSessionData.containsKey(sessionid)) {
					sessionid = generateSessionID_Android();
				}

				ANDSessionData sessiondata = new ANDSessionData(sessionid, p_clientinfo, GUtilities.getProName(), GUtilities.getProVersion());

				s_mapSessionData.put(sessionid, sessiondata);

				return sessionid;
			}
		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public static void deleteSessionID(ISessionID p_sessionid) {
		synchronized(LOCK) {
			s_mapSessionData.remove(p_sessionid);
			s_mapLastActive_SessionID.remove(p_sessionid);
		}
	}

	public static ISessionData getSessionData(ISessionID p_sessionid) throws GException {
		try {
			if(!s_mapSessionData.containsKey(p_sessionid)) {
				throw new GException("Invalid SessionID(" + p_sessionid + ")!");
			}

			return s_mapSessionData.get(p_sessionid);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public static void printSessionIDs() throws GException {
		synchronized(LOCK) {
			try {
				GLog.info("**********************");

				for(ISessionID sessionid : s_mapSessionData.keySet()) {
					GLog.info(sessionid.toString());
				}

				GLog.info("**********************");

			} catch (Exception exception) {
				ExceptionHandler.display(exception);
				throw new GException(exception);
			}
		}
	}

	private static SessionID generateSessionID(String p_strAPPTYPE) throws GException {
		try {
			synchronized(LOCK_GenerateSessionID) {
				String strRandomString_01 = randomstring();
				String strRandomString_02 = randomstring();

				strRandomString_01 = stringToHex(strRandomString_01);
				strRandomString_02 = stringToHex(strRandomString_02);

				return new SessionID(p_strAPPTYPE + '-' + strRandomString_01 + '-' + strRandomString_02);
			}
		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	private static ANDSessionID generateSessionID_Android() throws GException {
		try {
			synchronized(LOCK_GenerateSessionID) {
				String strRandomString_01 = randomstring();
				String strRandomString_02 = randomstring();

				strRandomString_01 = stringToHex(strRandomString_01);
				strRandomString_02 = stringToHex(strRandomString_02);

				return new ANDSessionID(strRandomString_01 + "-" + strRandomString_02);
			}
		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public static void setTimeoutSession(int p_intTimeoutMinute) throws GException {
		try {
			int intTimeoutMilliseconds = p_intTimeoutMinute * 60 * 1000;

			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						while(true) {
							Thread.sleep(intTimeoutMilliseconds);
							clearSession(p_intTimeoutMinute);
						}
					} catch(Exception exception) {
						ExceptionHandler.display(exception);
					}
				}
			}).start();

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public static void setLastActive(ISessionID p_sessionid) throws GException {
		try {
			synchronized(LOCK) {
				s_mapLastActive_SessionID.put(p_sessionid, DBDateTime.getCurrentDateTime());
			}
		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public static void clearSession(int p_intTimeoutMinute) throws GException {
		try {
			synchronized(LOCK) {
				GLog.info("Session clearing. . .");

				Set<ISessionID> setSessionIDs = new GSet<>(s_mapLastActive_SessionID.keySet());

				if(setSessionIDs.size() > 0) {
					DBDateTime dbdtmCurrentDateTime = DBDateTime.getCurrentDateTime();

					for(ISessionID sessionid : setSessionIDs) {
						if(s_mapLastActive_SessionID.containsKey(sessionid)) {
							DBDateTime dbdtmLastActive = s_mapLastActive_SessionID.get(sessionid);
							int intDiff_Minutes = dbdtmCurrentDateTime.getDiff_Minutes(dbdtmLastActive);

							if(intDiff_Minutes > p_intTimeoutMinute) {
								s_mapLastActive_SessionID.remove(sessionid);

								s_mapSessionData.remove(sessionid);
							}
						}
					}
				}

				setSessionIDs = new GSet<>(s_mapLastActive_SessionID.keySet());

				GLog.info("Session be Cleared.");
				GLog.info("Session is valid #" + setSessionIDs.size());
			}
		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public static void clearAllSession() throws GException {
		try {
			synchronized(LOCK) {
				GLog.info("Session clearing. . .");

				Set<ISessionID> setSessionIDs = new GSet<>(s_mapLastActive_SessionID.keySet());

				for(ISessionID sessionid : setSessionIDs) {
					s_mapLastActive_SessionID.remove(sessionid);
					s_mapSessionData.remove(sessionid);
				}

				setSessionIDs = new GSet<>(s_mapLastActive_SessionID.keySet());

				GLog.info("Session be Cleared.");
				GLog.info("Session is valid #" + setSessionIDs.size());
			}
		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public static void finish(ISessionID p_sessionid) {
		try {
			setLastActive(p_sessionid);
			ClientSessionID.finish(p_sessionid);

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
		}
	}

	public static int getNumberOfValidSession() {
		synchronized(LOCK) {
			return s_mapLastActive_SessionID.size();
		}
	}

	private static String randomstring() {
		int n = 5;
		byte b[] = new byte[n];
		for (int i = 0; i < n; i++) {
			b[i] = (byte)rand('a', 'z');
		}

		return new String(b);
	}

	private static int rand(int lo, int hi) {
		java.util.Random rn = new java.util.Random();
		int n = hi - lo + 1;
		int i = rn.nextInt(n);
		if (i < 0)
			i = -i;
		return lo + i;
	}

	private static String stringToHex(String p_strString) {
		char[] chars = p_strString.toCharArray();
		StringBuffer strBuffer = new StringBuffer();

		for (int i = 0; i < chars.length; i++) {
			strBuffer.append(Integer.toHexString((int) chars[i]));
		}

		return strBuffer.toString();
	}
}
