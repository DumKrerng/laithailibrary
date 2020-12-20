package com.laithailibrary.sharelibrary.session;

import exc.ExceptionHandler;
import exc.GException;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * Created by IntelliJ IDEA.
 * User: dumkrerng
 * Date: 4/23/12
 * Time: 11:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class SessionID implements ISessionID, Comparable<ISessionID>, Externalizable {

	private String m_strSessionID = "";

	private static String APPTYPE = "PC";
	private String m_strSessionID_01 = "";
	private String m_strSessionID_02 = "";

	private int m_intSubNumber = 0;

//	public static String m_strProName = "";
//	public static String m_strProVer = "";

	private static final long serialVersionUID = 3;

	public SessionID() {}

	public SessionID(String p_strSessionID) throws GException {
		try {
			m_strSessionID = p_strSessionID;

			String[] strSessionIDs = m_strSessionID.split("-");

			if(strSessionIDs.length == 3) {
				APPTYPE = strSessionIDs[0];
				m_strSessionID_01 = strSessionIDs[1];
				m_strSessionID_02 = strSessionIDs[2];

			} if(strSessionIDs.length == 2) {
				m_strSessionID_01 = strSessionIDs[0];
				m_strSessionID_02 = strSessionIDs[1];
			}

			if(!p_strSessionID.startsWith(APPTYPE)) {
				m_strSessionID = APPTYPE + '-' + m_strSessionID;
			}
		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public void writeExternal(ObjectOutput out) throws IOException {
		try {
			out.writeUTF(m_strSessionID);
			out.writeUTF(m_strSessionID_01);
			out.writeUTF(m_strSessionID_02);
			out.writeInt(m_intSubNumber);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			out.close();

			throw new IOException(exception);
		}
	}

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		try {
			m_strSessionID = in.readUTF();
			m_strSessionID_01 = in.readUTF();
			m_strSessionID_02 = in.readUTF();
			m_intSubNumber = in.readInt();

		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			in.close();

			throw new IOException(exception);
		}
	}

	public String getAppType() {
		return APPTYPE;
	}

	public String getSessionID() {
		return m_strSessionID;
	}

	public String getSessionID_01() {
		return m_strSessionID_01;
	}

	public String getSessionID_02() {
		return m_strSessionID_02;
	}

	public void incrementSubNumber() {
		m_intSubNumber++;
	}

	public int getSubNumber() {
		return m_intSubNumber;
	}

	public String toString() {
		if(m_intSubNumber > 0) {
			String strSubNumber = Integer.toString(m_intSubNumber);

			if(m_intSubNumber > 10) {
				strSubNumber = '0' + strSubNumber;
			}

			return  m_strSessionID + '-' + strSubNumber;

		} else {
			return  m_strSessionID;
		}
	}

	public int compareTo(ISessionID p_sessionid) {
		return this.toString().compareTo(p_sessionid.toString());
	}
}
