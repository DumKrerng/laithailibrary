package com.laithailibrary.sharelibrary.servicecall.invocation;

import java.lang.reflect.*;
import com.laithailibrary.sharelibrary.interfaceclass.*;
import com.laithailibrary.sharelibrary.servicecall.*;
import com.laithailibrary.sharelibrary.session.*;
import com.laithailibrary.sharelibrary.support.*;
import exc.*;

public class MethodInvocationHandler implements InvocationHandler {

	private MethodInvocationHandler me;

	private Class<? extends InterfaceService> m_class;
	private ISessionID m_sessionid;

	public MethodInvocationHandler(Class<? extends InterfaceService> p_class, ISessionID p_sessionid) {
		me = this;

		m_class = p_class;
		m_sessionid = p_sessionid;
	}

	public Object invoke(Object p_proxy, Method p_method, Object[] p_args) throws Throwable {
		try {
			String strMethodName_Full = p_method.getDeclaringClass().getCanonicalName() + '.' +  p_method.getName() + "()";

			if(p_method.getName().compareTo("toString") == 0) {
				return p_proxy.getClass().getCanonicalName() + "->" + me.m_class.getCanonicalName();
			}

			int intCountOfParameter = p_method.getParameters().length;

			if(intCountOfParameter > 0) {
				int index = 0;

				for(Object objParameter : p_args) {
					index++;

					if(objParameter == null) {
						StringBuilder bufferError = new StringBuilder();
						bufferError.append("Method: ").append(strMethodName_Full).append('\n');
						bufferError.append("Parameter#: ").append(index).append(" is NULL.");

						throw new GException(bufferError);
					}
				}
			}

			ServiceCall_Method servicecall = new ServiceCall_Method(m_sessionid, m_class, p_method.getName(), p_args, AppClientUtilities.getServerAddress());

			ClientInterfaceService service = new ClientInterfaceService();

			return service.executeMethod(servicecall);

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}
}
