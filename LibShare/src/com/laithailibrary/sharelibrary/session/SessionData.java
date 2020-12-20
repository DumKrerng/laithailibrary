package com.laithailibrary.sharelibrary.session;

import java.io.*;
import com.laithailibrary.sharelibrary.clientinfo.*;
import com.laithailibrary.sharelibrary.servicecall.*;
import exc.*;

/**
 * Created by IntelliJ IDEA.
 * User: dumkrerng
 * Date: 4/23/12
 * Time: 11:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class SessionData implements ISessionData, Externalizable {

	private SessionID m_sessionid = null;
	private ClientInfo m_clientinfo = null;

	private String m_strProName = "";
	private String m_strProVer = "";

	private static final long serialVersionUID = 2;

	public SessionData() {}

	public SessionData(SessionID p_sessionid, FirstConnect p_firstconnect, String p_strProName, String p_strProVer) {
		m_sessionid = p_sessionid;
		m_clientinfo = p_firstconnect.getClientInfo();
		m_strProName = p_strProName;
		m_strProVer = p_strProVer;
	}

	public SessionData(String p_strSessionID, String p_strMAC) throws GException {
		m_sessionid = new SessionID(p_strSessionID);
		m_clientinfo = new ClientInfo(p_strMAC);
	}

	public void writeExternal(ObjectOutput out) throws IOException {
		try {
			out.writeObject(m_sessionid);
			out.writeObject(m_clientinfo);
			out.writeUTF(m_strProName);
			out.writeUTF(m_strProVer);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			out.close();

			throw new IOException(exception);
		}
	}

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		try {
			m_sessionid = (SessionID)in.readObject();
			m_clientinfo = (ClientInfo)in.readObject();
			m_strProName = in.readUTF();
			m_strProVer = in.readUTF();

		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			in.close();

			throw new IOException(exception);
		}
	}

	public SessionID getSessionID() {
		return m_sessionid;
	}

	public ClientInfo getClientInfo() {
		return m_clientinfo;
	}

	public String getProName() {
		return m_strProName;
	}

	public String getProVer() {
		return m_strProVer;
	}
}
