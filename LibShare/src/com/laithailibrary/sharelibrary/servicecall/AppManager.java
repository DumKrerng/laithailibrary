package com.laithailibrary.sharelibrary.servicecall;

import com.laithailibrary.sharelibrary.session.*;
import exc.*;
import java.io.*;

public class AppManager implements Externalizable {

	private ISessionID m_sessionid = null;
	private String m_strAppName = "";
	private String m_strRequestString = "";

	private static final long serialVersionUID = 1;

	public AppManager() {}

	public AppManager(ISessionID p_sessionid, String p_strAppName) {
		m_sessionid = p_sessionid;
		m_strAppName = p_strAppName;
	}

	public void writeExternal(ObjectOutput out) throws IOException {
		try {
			out.writeObject(m_sessionid);
			out.writeUTF(m_strAppName);
			out.writeUTF(m_strRequestString);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			out.close();

			throw new IOException(exception);
		}
	}

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		try {
			m_sessionid = (ISessionID)in.readObject();
			m_strAppName = in.readUTF();
			m_strRequestString = in.readUTF();

		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			in.close();

			throw new IOException(exception);
		}
	}

	public ISessionID getSessionID() {
		return m_sessionid;
	}

	public String getAppName() {
		return m_strAppName;
	}

	public void setRequestString(String p_strRequestString) {
		m_strRequestString = p_strRequestString;
	}

	public String getRequestString() {
		return m_strRequestString;
	}
}
