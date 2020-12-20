package com.laithailibrary.sharelibrary.session;

import java.util.*;
import com.laithailibrary.sharelibrary.collection.*;
import com.laithailibrary.sharelibrary.db.dbobject.*;
import com.laithailibrary.sharelibrary.thread.*;
import exc.*;

public class SessionQueue {

	private int m_intTimeWaiting = 3000;//milliseconds

	private static final Set<GKey> s_setKeys = new GSet<>();
	private static final Map<GKey, ISessionID> s_mapSessionID_Keys = new GMap<>();

	private static final String LOCK = "LOCK";

	private ISessionID m_sessionid = null;
	private GKey m_key = null;
	private boolean m_bolFirst = false;

	public SessionQueue() throws GException {
		init(3000);
	}

	public SessionQueue(int p_intTimeWaiting) throws GException {
		init(p_intTimeWaiting);
	}

	private void init(int p_intTimeWaiting) throws GException {
		m_intTimeWaiting = p_intTimeWaiting;
		m_sessionid = ClientSessionID.getSessionID(new GThreadGroup(Thread.currentThread()));
	}

	public void enqueue(DBString p_dbstrkey) throws GException {
		m_key = new GKey(1);
		m_key.addValue(0, p_dbstrkey);

		baseEnqueue(m_key, m_sessionid);
	}

	public void enqueue(GKey p_key) throws GException {
		m_key = p_key;

		baseEnqueue(m_key, m_sessionid);
	}

	private void baseEnqueue(GKey p_key, ISessionID p_sessionid) throws GException {
		try {
//			ISessionID sessionid = ClientSessionID.getSessionID(new GThreadGroup(Thread.currentThread()));
//			System.out.println("enqueue --> " + p_key.toString() + " --> " + sessionid.getSessionID());

			while(true) {
				boolean bolFoundKey = false;

				synchronized(LOCK) {
					if(s_setKeys.contains(p_key)) {
						if(s_mapSessionID_Keys.containsKey(p_key)) {
							ISessionID sessionid_Temp = s_mapSessionID_Keys.get(p_key);

							if(sessionid_Temp.compareTo(p_sessionid) == 0) {
								return;
							}
						}

						bolFoundKey = true;
					}
				}

				if(bolFoundKey) {
					if(m_intTimeWaiting <= 0) {
						throw new GException("Action is busy!!!");
					}

//					System.out.println("Sleep --> " + p_key.toString() + " --> " + sessionid.getSessionID());
					Thread.sleep(m_intTimeWaiting);

				} else {
					break;
				}
			}

			s_setKeys.add(p_key);
			s_mapSessionID_Keys.put(p_key, p_sessionid);

			m_bolFirst = true;

//			System.out.println("Run --> " + p_key.toString() + " --> " + sessionid.getSessionID());

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public void dequeue() throws GException {
		baseDequeue(m_key);
	}

	private void baseDequeue(GKey p_key) throws GException {
		try {
			if(m_bolFirst) {
				synchronized(LOCK) {
					s_setKeys.remove(p_key);
					s_mapSessionID_Keys.remove(p_key);

//				ISessionID sessionid = ClientSessionID.getSessionID(new GThreadGroup(Thread.currentThread()));
//				System.out.println("dequeue --> " + p_key.toString() + " --> " + sessionid.getSessionID());
				}
			}
		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}
}
