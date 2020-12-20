package com.laithailibrary.serverlibrary.client.callservice;

import java.io.*;
import java.lang.reflect.*;
import java.net.*;
import com.laithailibrary.logger.*;
import com.laithailibrary.serverlibrary.client.clientsession.*;
import com.laithailibrary.sharelibrary.bean.*;
import com.laithailibrary.sharelibrary.db.dbutilities.*;
import com.laithailibrary.sharelibrary.servicecall.*;
import com.laithailibrary.sharelibrary.session.*;
import exc.*;

public class CallServiceMethod_DataTableQuery {

	public CallServiceMethod_DataTableQuery(Socket p_clientsocket, ServiceCall_DataTableQuery p_servicecall) throws GException {
		ObjectOutputStream objOutput = null;

		ISessionID sessionid = p_servicecall.getSessionID();

		try {
			String strMethodName = p_servicecall.getMethodName();
			Object[] parameters = p_servicecall.getParameters();

			String strMethodName_Full = p_servicecall.getClassDataTable().getCanonicalName() + '.' +  strMethodName + "()";

			GLog.info("Client call: " + strMethodName_Full + " on SessionID " + sessionid);

			objOutput = new ObjectOutputStream(p_clientsocket.getOutputStream());

			DataTable datatable = p_servicecall.getClassDataTable().newInstance();
			datatable.setSessionID(p_servicecall.getSessionID());

			Object objReturn;

			if(parameters != null
				&& parameters.length > 0) {

				Class<?>[] paramtypes = CallServiceParamType.getParamTypes_DataTableQuery(parameters);

				Method method = datatable.getClass().getMethod(strMethodName, paramtypes);
				objReturn = method.invoke(datatable, parameters);

			} else {
				Method method = datatable.getClass().getMethod(strMethodName);
				objReturn = method.invoke(datatable);
			}

			if(strMethodName.startsWith("deleteData")) {
				objReturn = "!$MSG$Deleted.";
			}

			objOutput.writeObject(objReturn);

		} catch (InvocationTargetException exception) {
			try {
				String strMessage = exception.getTargetException().getMessage();

				if(strMessage == null) {
					strMessage = exception.toString();
				}

				objOutput.writeObject("!$EXP$" + strMessage);

			} catch(Exception exc) {
				ExceptionHandler.display(exc);
			}

			ExceptionHandler.display(exception);
			throw new GException(exception);

		} catch (Exception exception) {
			try {
				String strException = exception.toString();
				objOutput.writeObject("!$EXP$" + strException);

			} catch(Exception exc) {
				ExceptionHandler.display(exc);
			}

			ExceptionHandler.display(exception);
			throw new GException(exception);

		} finally {
			try {
				DBUtilities.closeConnection(sessionid);
				p_clientsocket.close();

			} catch(Exception exception) {
				ExceptionHandler.display(exception);
			}

			SessionManager.setLastActive(sessionid);

			GLog.info("Return object to " + sessionid);
		}
	}
}
