package com.laithailibrary.sharelibrary.servicecall;

import com.laithailibrary.sharelibrary.clientinfo.ClientInfo;
import exc.ExceptionHandler;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * Created with IntelliJ IDEA.
 * User: dumkrerng
 * Date: 11/1/12
 * Time: 10:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class FirstConnect implements Externalizable {

	private ClientInfo m_clientinfo;

	private static final long serialVersionUID = 6695383790847721453L;

	public FirstConnect() {}

	public FirstConnect(ClientInfo p_clientinfo) {
		m_clientinfo = p_clientinfo;
	}

	public void writeExternal(ObjectOutput out) throws IOException {
		try {
			out.writeObject(m_clientinfo);

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);

			out.close();

		  throw new IOException(exception);
		}
	}

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		try {
			m_clientinfo = (ClientInfo)in.readObject();

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);

			in.close();

		  throw new IOException(exception);
		}
	}

	public ClientInfo getClientInfo() {
		return m_clientinfo;
	}
}
