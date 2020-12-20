package com.laithailibrary.sharelibrary.clientinfo;

import java.io.*;
import java.util.*;
import com.laithailibrary.sharelibrary.session.*;
import exc.*;

/**
 * Created with IntelliJ IDEA.
 * User: dumkrerng
 * Date: 11/1/12
 * Time: 10:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClientInfo implements IClientInfo, Externalizable {

	private Properties m_properties;
	private String m_strMAC = "";

	private static final long serialVersionUID = 5;

	public ClientInfo() {}

	public ClientInfo(Properties p_properties) throws GException {
		try {
			m_properties = p_properties;

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public ClientInfo(String p_strMAC) throws GException {
		try {
			m_properties = new Properties();
			m_strMAC = p_strMAC;

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public void writeExternal(ObjectOutput out) throws IOException {
		try {
			out.writeObject(m_properties);
			out.writeUTF(m_strMAC);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			out.close();

			throw new IOException(exception);
		}
	}

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		try {
			m_properties = (Properties)in.readObject();
			m_strMAC = in.readUTF();

		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			in.close();

			throw new IOException(exception);
		}
	}

	public String getMAC() {
		return m_strMAC;
	}

	public Properties getProperties() {
		return m_properties;
	}
}
