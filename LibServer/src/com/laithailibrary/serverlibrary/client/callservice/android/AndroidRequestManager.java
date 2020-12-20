package com.laithailibrary.serverlibrary.client.callservice.android;

import java.io.*;
import java.net.*;
import com.laithailibrary.logger.*;
import com.laithailibrary.serverlibrary.client.callservice.android.handler.*;
import com.laithailibrary.serverlibrary.client.clientsession.*;
import com.laithailibrary.sharelibrary.bean.request.*;
import com.laithailibrary.sharelibrary.bean.request.support.*;
import com.laithailibrary.sharelibrary.bean.result.*;
import com.laithailibrary.sharelibrary.bean.result.support.*;
import com.laithailibrary.sharelibrary.clientInfo.*;
import com.laithailibrary.sharelibrary.session.*;
import exc.*;

public class AndroidRequestManager {

	private AndroidRequestManager() {}

	public AndroidRequestManager(Socket p_clientsocket, AndroidRequest p_request) throws GException {
		ANDSessionID sessionid = null;

		try {
			int intHeader = p_request.getHeader();

			if(intHeader == AndroidRequestHeader.HELLO) {
				GLog.info("Android say \"Hello Server.\".");

				AndroidHelloBean bean = (AndroidHelloBean)p_request;

				ANDClientInfo clientinfo = new ANDClientInfo(bean.getMAC());

				sessionid = SessionManager.createSessionID_Android(clientinfo);
				ANDSessionData sessiondata = (ANDSessionData)SessionManager.getSessionData(sessionid);

				ObjectOutputStream output = new ObjectOutputStream(p_clientsocket.getOutputStream());
				output.flush();
				output.writeObject(sessiondata);

				GLog.info("Client SessionID: " + sessionid.toString() + " is connected.");

			} else if(intHeader == AndroidRequestHeader.GOODBYE) {
				GLog.info("Android say \"Good Bye Server.\".");

				AndroidGoodByeBean bean = (AndroidGoodByeBean)p_request;
				ANDSessionData sessiondata = bean.getANDSessionData();

				SessionManager.deleteSessionID(sessiondata.getSessionID());

				GLog.info("Android SessionID: " + sessiondata.getSessionID().toString() + " is disconnected.");

			} else {
				sessionid = p_request.getANDSessionData().getANDSessionID();

				if(sessionid.toString().length() <= 0) {
					throw new GException("ANDSessionID is invalid.");
				}

				String strRequestName = p_request.getRequestName();

				GLog.info("Android request: \"" + strRequestName + "\" on SessionID " + sessionid.toString());

				ANDRequestHandler requesthandler = AndroidRequestHandlerManager.newANDRequestHandlerObject(strRequestName);
				AndroidResult bean = requesthandler.doHandle(p_request);
				bean.setRequestName(strRequestName);

				ObjectOutputStream output = new ObjectOutputStream(p_clientsocket.getOutputStream());
				output.flush();
				output.writeObject(bean);
			}
		} catch(Exception exception) {
		  ExceptionHandler.display(exception);

		  try {
			  AndroidResult bean = new AndroidResult(AndroidResultHeader.EXCEPTION_ERROR);
			  bean.setRequestName(p_request.getRequestName());
			  bean.setExceptionError(exception.getMessage());

			  ObjectOutputStream output = new ObjectOutputStream(p_clientsocket.getOutputStream());
			  output.flush();
			  output.writeObject(bean);

		  } catch(Exception exc) {
			  ExceptionHandler.display(exc);
		  }

		  throw new GException(exception);

		} finally {
			if(sessionid != null) {
				SessionManager.setLastActive(sessionid);
			}
		}
	}
}
