package com.laithailibrary.sharelibrary.session;

import java.util.*;
import com.laithailibrary.sharelibrary.collection.*;
import com.laithailibrary.sharelibrary.thread.*;
import exc.*;

/**
 * Created with IntelliJ IDEA.
 * User: dumkrerng
 * Date: 11/9/12
 * Time: 10:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClientSessionID {

	private static final Object LOCK = new Object();

	private static Map<GThreadGroup, ISessionID> s_mapSessionID_ThreadGroup = new GMap<>();
	private static Set<ISessionID> m_setSessionIDs = new GSet<>();

	private ClientSessionID() {}

	public static void addClientCallService(GThreadGroup p_threadgroup, ISessionID p_sessionid) {
		synchronized(LOCK) {
			while(m_setSessionIDs.contains(p_sessionid)) {
				p_sessionid.incrementSubNumber();
			}

			s_mapSessionID_ThreadGroup.put(p_threadgroup, p_sessionid);
			m_setSessionIDs.add(p_sessionid);
		}
	}

	public static ISessionID getSessionID(GThreadGroup p_threadgroup) throws GException {
		synchronized(LOCK) {
			Thread current = Thread.currentThread();

			ThreadGroup currentgroup = current.getThreadGroup();
			String strGroupName_Current = currentgroup.getName();

			if(strGroupName_Current.compareTo("main") == 0) {
				strGroupName_Current = String.valueOf(current.getId());
			}

			if(strGroupName_Current.compareTo(p_threadgroup.getName()) != 0) {
				throw new GException("Server Thread Error!!!");
			}

			if(!s_mapSessionID_ThreadGroup.containsKey(p_threadgroup)) {
				throw new GException("Server Thread Error!!!");
			}

			return s_mapSessionID_ThreadGroup.get(p_threadgroup);
		}
	}

	public static GThreadGroup getThreadGroup(ISessionID p_sessionid) throws GException {
		synchronized(LOCK) {
			try {
				for(GThreadGroup threadgroup : s_mapSessionID_ThreadGroup.keySet()) {
					ISessionID sessionid_Temp = s_mapSessionID_ThreadGroup.get(threadgroup);

					if(p_sessionid.compareTo(sessionid_Temp) == 0) {
						return threadgroup;
					}
				}
			} catch (Exception exception) {
				ExceptionHandler.display(exception);
			}
		}

		throw new GException("Server Thread Error!!!");
	}

	public static void finish(ISessionID p_sessionid) {
		synchronized(LOCK) {
			try {
				for(GThreadGroup threadgroup : s_mapSessionID_ThreadGroup.keySet()) {
					ISessionID sessionid_Temp = s_mapSessionID_ThreadGroup.get(threadgroup);

					if(p_sessionid.compareTo(sessionid_Temp) == 0) {
						s_mapSessionID_ThreadGroup.remove(threadgroup);
						m_setSessionIDs.remove(p_sessionid);

						break;
					}
				}
			} catch (Exception exception) {
				ExceptionHandler.display(exception);
			}
		}
	}

	public static boolean isEmptyCallService() {
		synchronized(LOCK) {
			int intSize = s_mapSessionID_ThreadGroup.size();

			if(intSize == 0) {
				return true;
			}
		}

		return false;
	}
}
