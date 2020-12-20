package com.laithailibrary.sharelibrary.servicecall;

import java.io.*;
import exc.*;

public class AppVersion implements Externalizable {

	private String m_strAppName = "";
	private String m_strAppVersion_Client = "";
	private String m_strAppVersion_Server = "";

	private static final long serialVersionUID = 1;

	public AppVersion() {}

	public AppVersion(String p_strAppName, String p_strAppVersion_Client) {
		m_strAppName = p_strAppName;
		m_strAppVersion_Client = p_strAppVersion_Client;
	}

	public void writeExternal(ObjectOutput out) throws IOException {
		try {
			out.writeUTF(m_strAppName);
			out.writeUTF(m_strAppVersion_Client);
			out.writeUTF(m_strAppVersion_Server);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			out.close();

			throw new IOException(exception);
		}
	}

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		try {
			m_strAppName = in.readUTF();
			m_strAppVersion_Client = in.readUTF();
			m_strAppVersion_Server = in.readUTF();

		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			in.close();

			throw new IOException(exception);
		}
	}

	public String getAppName() {
		return m_strAppName;
	}

	public String getAppVersion_Client() {
		return m_strAppVersion_Client;
	}

	public void setAppVersion_Server(String p_strAppVersion_Server) {
		m_strAppVersion_Server = p_strAppVersion_Server;
	}

	public String getAppVersion_Server() {
		return m_strAppVersion_Server;
	}

	public boolean isUpdate() {
		if(m_strAppVersion_Client.compareTo(m_strAppVersion_Server) == 0) {
			return true;
		}

		return false;
	}
}
