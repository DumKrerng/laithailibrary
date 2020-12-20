package com.laithailibrary.serverlibrary.client.callservice;

import java.io.*;
import java.lang.reflect.*;
import java.net.*;
import com.laithailibrary.logger.*;
import com.laithailibrary.serverlibrary.client.clientsession.*;
import com.laithailibrary.serverlibrary.client.register.*;
import com.laithailibrary.sharelibrary.db.dbutilities.*;
import com.laithailibrary.sharelibrary.interfaceclass.*;
import com.laithailibrary.sharelibrary.servicecall.*;
import com.laithailibrary.sharelibrary.session.*;
import exc.*;

public class CallServiceMethod {

	private CallServiceMethod() {}

	public CallServiceMethod(Socket p_clientsocket, ServiceCall_Method p_servicecall) throws GException {
		ObjectOutputStream objOutput = null;

		ISessionID sessionid = p_servicecall.getSessionID();

		try {
			String strInterfaceClassName = p_servicecall.getServiceName();
			String strMethodName = p_servicecall.getMethodName();
			Object[] parameters = p_servicecall.getParameters();

			String strMethodName_Full = strInterfaceClassName + '.' +  strMethodName + "()";

			GLog.info("Client call: " + strMethodName_Full + " on SessionID " + sessionid);

			objOutput = new ObjectOutputStream(p_clientsocket.getOutputStream());

			if(!ServiceClassRegister.containsClass(strInterfaceClassName)) {
				GLog.severe("Not Register " + strInterfaceClassName);

				objOutput.writeObject("!$EXP$Not Register " + strInterfaceClassName);

			} else {
				InterfaceService interfaceservice = ServiceClassRegister.getClass(strInterfaceClassName).newInstance();
				interfaceservice.setSessionID(sessionid);

				Object objReturn;

				if(parameters != null
					&& parameters.length > 0) {

					Class<?>[] paramtypes = CallServiceParamType.getParamTypes(parameters);

					Method method = interfaceservice.getClass().getMethod(strMethodName, paramtypes);
					objReturn = method.invoke(interfaceservice, parameters);

					Class clsReturn = method.getReturnType();

					if(clsReturn.getName().compareTo("void") == 0) {
						objReturn = "!$MSG$" + "Successful";
					}

				} else {
					Method method = interfaceservice.getClass().getMethod(strMethodName);
					objReturn = method.invoke(interfaceservice);

					Class clsReturn = method.getReturnType();

					if(clsReturn.getName().compareTo("void") == 0) {
						objReturn = "!$MSG$" + "Successful";
					}
				}

				objOutput.writeObject(objReturn);
			}
		} catch (InvocationTargetException exception) {
			try {
				String strMessage = exception.getTargetException().toString();

				if(strMessage == null) {
					strMessage = exception.getTargetException().getMessage();
				}

				strMessage = strMessage.replace("exc.GException: ", "");

				objOutput.writeObject("!$EXP$" + strMessage);

			} catch(Exception exc) {
			  ExceptionHandler.display(exc);
			}

			ExceptionHandler.display(exception);
			throw new GException(exception);

		} catch (Exception exception) {
			try {
				String strMessage = exception.toString();
				strMessage = strMessage.replace("exc.GException: ", "");

				objOutput.writeObject("!$EXP$" + strMessage);

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
