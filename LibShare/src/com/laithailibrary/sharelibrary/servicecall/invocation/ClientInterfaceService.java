package com.laithailibrary.sharelibrary.servicecall.invocation;

import java.io.*;
import java.net.*;
import com.laithailibrary.logger.*;
import com.laithailibrary.sharelibrary.servicecall.*;
import com.laithailibrary.sharelibrary.socket.*;
import com.laithailibrary.sharelibrary.support.*;
import exc.*;

public class ClientInterfaceService {

	public ClientInterfaceService() {}

	public Object executeMethod(ServiceCall_Method p_servicecall) throws GException {
		try {
			Object objReturn = null;

			boolean bolTry;
			int intTryCount = 0;

			do {
				intTryCount++;

				try {
					String strMethodName_Full = p_servicecall.getClassService().getCanonicalName() + '.' + p_servicecall.getMethodName() + "()";

					if(intTryCount > 1) {
						GLog.info("Call service " + strMethodName_Full + ".(Try=" + intTryCount + ')');

					} else {
						GLog.info("Call service " + strMethodName_Full + ".");
					}

					Socket socket = ClientSocket.createSocket(AppClientUtilities.getServerAddress(), AppClientUtilities.getServerPort());

					ObjectOutputStream objOutputStream = new ObjectOutputStream(socket.getOutputStream());
					objOutputStream.writeObject(p_servicecall);
					objOutputStream.flush();

					ObjectInputStream objInputStream = new ObjectInputStream(socket.getInputStream());
					objReturn = objInputStream.readObject();

					if(objReturn instanceof String) {
						objOutputStream.close();
						objInputStream.close();

						String strReturnMessage = (String)objReturn;

						if(strReturnMessage.startsWith("!$EXP$")) {
							throw new GException(strReturnMessage.replace("!$EXP$", ""));

						} else if(strReturnMessage.startsWith("!$MSG$")) {
							GLog.info(strReturnMessage.replace("!$MSG$", ""));
						}
					} else {
						objOutputStream.close();
						objInputStream.close();
					}

					bolTry = false;

				} catch(SocketTimeoutException p_excTimeout) {
					throw new GException("Connection Timeout!!!");

				} catch(Exception exception) {
					String strErrorMessage = exception.getMessage();

					if(strErrorMessage == null) {
						strErrorMessage = "EOF Exception!!!";
					}

					if(strErrorMessage.compareTo("NULL") == 0) {
						strErrorMessage = "EOF Exception!!!";
					}

					if(strErrorMessage.contains("Connection refused")
						|| strErrorMessage.contains("no such object")
						|| strErrorMessage.contains("Connection reset")) {

						bolTry = true;

						if(intTryCount >= ClientSocket.TRYLIMIT) {
							bolTry = false;

							ExceptionHandler.display(exception);
							throw new GException(exception);
						}
					} else {
						ExceptionHandler.display(exception);
						throw new GException(exception);
					}

					Thread.sleep(ClientSocket.SLEEP);
				}
			} while(bolTry);

			if(objReturn == null) {
				throw new GException("Service Return NULL!!!");
			}

			return objReturn;

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}
}
