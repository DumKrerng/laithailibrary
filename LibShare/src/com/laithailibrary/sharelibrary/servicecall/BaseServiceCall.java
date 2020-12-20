package com.laithailibrary.sharelibrary.servicecall;

import java.io.*;
import com.laithailibrary.sharelibrary.interfaceclass.*;
import com.laithailibrary.sharelibrary.session.*;
import exc.*;

/**
 * Created with IntelliJ IDEA.
 * User: dumkrerng
 * Date: 11/1/12
 * Time: 10:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class BaseServiceCall implements Externalizable, IServiceCell {

	private ISessionID m_sessionid = null;
	private Class<? extends InterfaceService> m_clsService = null;
	private String m_strMethodName = "";
  private String m_srtServerAddress = "";

	private static final long serialVersionUID = 5;

	public BaseServiceCall() {}

	public BaseServiceCall(ISessionID p_sessionid, Class<? extends InterfaceService> p_clsService, String p_strMethodName,
		String p_setServerAddress) throws GException {

		try {
		  m_sessionid = p_sessionid;
		  m_clsService = p_clsService;
			m_strMethodName = p_strMethodName;
      m_srtServerAddress = p_setServerAddress;

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public void writeExternal(ObjectOutput out) throws IOException {
		try {
			out.writeObject(m_sessionid);
			out.writeObject(m_clsService);
			out.writeUTF(m_strMethodName);
			out.writeUTF(m_srtServerAddress);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			out.close();

			throw new IOException(exception);
		}
	}

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		try {
			m_sessionid = (ISessionID)in.readObject();
			m_clsService = (Class<? extends InterfaceService>)in.readObject();
			m_strMethodName = in.readUTF();
			m_srtServerAddress = in.readUTF();

		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			in.close();

			throw new IOException(exception);
		}
	}

	public ISessionID getSessionID() {
		return m_sessionid;
	}

	public Class<? extends InterfaceService> getClassService() {
		return m_clsService;
	}

	public String getServerAddress() {
			return m_srtServerAddress;
	}

	public String getServiceName() {
		return getClassService().getName();
	}

	public String getMethodName() {
		return m_strMethodName;
	}
}
