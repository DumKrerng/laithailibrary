package com.laithailibrary.sharelibrary.servicecall;

import java.io.*;
import com.laithailibrary.sharelibrary.session.*;
import exc.*;

/**
 * Created by dumkrerng on 20/9/2559.
 */
public class ClientSayGoodbye implements Externalizable {

	private SessionData m_sessiondata;

	private static final long serialVersionUID = 1;

	public ClientSayGoodbye() {}

	public ClientSayGoodbye(SessionData p_sessiondata) throws GException {
		m_sessiondata = p_sessiondata;
	}

	public void writeExternal(ObjectOutput out) throws IOException {
		try {
			out.writeObject(m_sessiondata);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			out.close();

			throw new IOException(exception);
		}
	}

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		try {
			m_sessiondata = (SessionData)in.readObject();

		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			in.close();

			throw new IOException(exception);
		}
	}

	public SessionData getSessionData() {
		return m_sessiondata;
	}
}
