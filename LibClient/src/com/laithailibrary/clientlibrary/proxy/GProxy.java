package com.laithailibrary.clientlibrary.proxy;

import java.io.*;
import java.lang.reflect.Proxy;
import java.net.*;
import com.laithailibrary.logger.*;
import com.laithailibrary.sharelibrary.interfaceclass.*;
import com.laithailibrary.sharelibrary.service.*;
import com.laithailibrary.sharelibrary.servicecall.invocation.*;
import com.laithailibrary.sharelibrary.session.*;
import com.laithailibrary.sharelibrary.socket.*;
import com.laithailibrary.sharelibrary.support.*;
import exc.*;
import msg.*;
import pp.*;

public class GProxy {

	private GProxy() {}

	public static <T extends InterfaceService> T newInstance(Class<T> p_class) throws GException {
		try {
			SessionData sessiondata = AppClientUtilities.getSessionData();
			SessionID sessionid = sessiondata.getSessionID();

			if(sessionid == null) {
				new ExcDialog("Invalid SessionID!");
				new MsgDialog("Program is close.");

				System.exit(ProgramExit.InvalidSessionID);
			}

			return newInstance(p_class, sessiondata);

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public static <T extends InterfaceService> T newInstance(Class<T> p_class, SessionData p_sessiondata) throws GException {
		try {
			return (T)Proxy.newProxyInstance(p_class.getClassLoader(), new Class<?>[] {p_class}, new MethodInvocationHandler(p_class,
				p_sessiondata.getSessionID()));

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public static <T extends InterfaceService> T newInstance(Class<T> p_class, ISessionID p_sessionid) throws GException {
		try {
			return (T)Proxy.newProxyInstance(p_class.getClassLoader(), new Class<?>[] {p_class}, new MethodInvocationHandler(p_class,
				p_sessionid));

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public static void stopServer() throws GException {
		try {
			Socket socket = ClientSocket.createSocket();

			GLog.info("Stop Server . . .");

			OutputStream output = socket.getOutputStream();
			output.flush();
			ObjectOutputStream objOutput = new ObjectOutputStream(output);
			objOutput.writeObject(ServiceStringConstant.REQUEST_StopServerService);

			InputStream input = socket.getInputStream();
			ObjectInputStream objInput = new ObjectInputStream(input);
			String strResult = (String)objInput.readObject();

			if(strResult.compareTo(ServiceStringConstant.RESPOND_ServerServiceIsStopping) == 0) {
				new MsgDialog("Server Service is stopping . . .", ExcDialog.MessageType.Message);

			} else if(strResult.compareTo(ServiceStringConstant.RESPOND_SessionServiceExecuteIsNotEmpty) == 0) {
				throw new GException("Session Service is not empty!!!");

			} else if(strResult.compareTo(ServiceStringConstant.RESPOND_NotSupportServiceString) == 0) {
				throw new GException("Not support Service String!!!");

			} else {
				throw new GException("Service Error!!!");
			}
		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}
}
