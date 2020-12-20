package com.laithailibrary.sharelibrary.servicecall.invocation;

import java.lang.reflect.*;
import com.laithailibrary.sharelibrary.bean.*;
import com.laithailibrary.sharelibrary.servicecall.*;
import com.laithailibrary.sharelibrary.session.*;
import com.laithailibrary.sharelibrary.support.*;
import exc.*;

public class DataTableQueryInvocationHandler implements InvocationHandler {

	private ISessionID m_sessionid;
	private Class<? extends DataTable> m_clsDataTable;
//	private Class<? extends DBDataTable> m_clsDBDataTable;

//	public DataTableQueryInvocationHandler(ISessionID p_sessionid, Class<? extends DBDataTable> p_clsDBDataTable) {
//		m_sessionid = p_sessionid;
//		m_clsDBDataTable = p_clsDBDataTable;
//	}

	public DataTableQueryInvocationHandler(ISessionID p_sessionid, Class<? extends DataTable> p_clsDataTable) {
		m_sessionid = p_sessionid;
		m_clsDataTable = p_clsDataTable;
	}

	public Object invoke(Object p_proxy, Method p_method, Object[] p_args) throws Throwable {
		try {
			String strMethodName_Full = m_clsDataTable.getCanonicalName() + '.' +  p_method.getName() + "()";

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

			ServiceCall_DataTableQuery servicecall = new ServiceCall_DataTableQuery(m_sessionid, m_clsDataTable, p_method.getName(),
				p_args, AppClientUtilities.getServerAddress());

			ClientInterfaceService service = new ClientInterfaceService();

			return service.executeMethod(servicecall);

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}
}
