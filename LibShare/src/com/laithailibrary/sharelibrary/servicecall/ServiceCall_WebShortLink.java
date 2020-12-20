package com.laithailibrary.sharelibrary.servicecall;

import com.laithailibrary.sharelibrary.interfaceclass.*;
import com.laithailibrary.sharelibrary.session.*;
import exc.*;
import java.io.*;

/**
 * Created by dumkrerng on 25/11/2560.
 */
public class ServiceCall_WebShortLink extends BaseServiceCall {

	private String m_strWebShortLinkID = "";

	private static final long serialVersionUID = 3;

	public ServiceCall_WebShortLink() {}

	public ServiceCall_WebShortLink(ISessionID p_sessionid, String p_setServerAddress, String p_strWebShortLinkID) throws GException {
		super(p_sessionid, InterfaceService.class, "WebShortLink", p_setServerAddress);

		m_strWebShortLinkID = p_strWebShortLinkID;
	}

	public void writeExternal(ObjectOutput out) throws IOException {
		try {
			super.writeExternal(out);

			out.writeUTF(m_strWebShortLinkID);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			out.close();

			throw new IOException(exception);
		}
	}

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		try {
			super.readExternal(in);

			m_strWebShortLinkID = in.readUTF();

		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			in.close();

			throw new IOException(exception);
		}
	}

	public String getWebShortLinkID() {
		return m_strWebShortLinkID;
	}
}
