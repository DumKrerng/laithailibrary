package com.laithailibrary.sharelibrary.servicecall;

import com.laithailibrary.sharelibrary.interfaceclass.*;
import com.laithailibrary.sharelibrary.session.*;
import exc.*;
import java.io.*;

public class ServiceCall_Method extends BaseServiceCall {

	private Object[] m_parameters;

	private static final long serialVersionUID = 1;

	public ServiceCall_Method() {}

	public ServiceCall_Method(ISessionID p_sessionid, Class<? extends InterfaceService> p_clsService, String p_strMethodName_Full,
		Object[] p_parameters, String p_setServerAddress) throws GException {

		super(p_sessionid, p_clsService, p_strMethodName_Full, p_setServerAddress);

		m_parameters = p_parameters;
	}

	public void writeExternal(ObjectOutput out) throws IOException {
		try {
			super.writeExternal(out);

			out.writeObject(m_parameters);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			out.close();

			throw new IOException(exception);
		}
	}

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		try {
			super.readExternal(in);

			m_parameters = (Object[])in.readObject();

		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			in.close();

			throw new IOException(exception);
		}
	}

	public Object[] getParameters() {
		return m_parameters;
	}
}
