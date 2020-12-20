package com.laithailibrary.serverlibrary.client.callservice.support;

import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;
import com.laithailibrary.serverlibrary.client.clientsession.*;
import com.laithailibrary.sharelibrary.db.dbobject.*;
import com.sun.net.httpserver.*;
import exc.*;

public class SessionActiveHttpHandler implements HttpHandler {

	private boolean m_bolReturnJSONString = false;

	public static final String REQUEST = "/SessionActive";
	public static final String REQUESTJSON = "/json/SessionActive";

	public SessionActiveHttpHandler() {}

	public SessionActiveHttpHandler(boolean p_bolReturnJSONString) {
		m_bolReturnJSONString = p_bolReturnJSONString;
	}

	@Override
	public void handle(HttpExchange p_exchange) throws IOException {
		try {
			String strResponse = getReturnString();
			int intResponseSize = strResponse.getBytes().length;

			Headers responseheaders = p_exchange.getResponseHeaders();

			if(m_bolReturnJSONString) {
				responseheaders.set("Content-Type", "application/json; charset=UTF-8");

			} else {
				responseheaders.set("Content-Type", "text/html; charset=UTF-8");
			}

			responseheaders.set("Date", DBDate.getCurrentDate().getString(new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US)));
			responseheaders.set("Content-length", Long.toString(intResponseSize));

			p_exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, intResponseSize);

			OutputStream output = p_exchange.getResponseBody();
			output.write(strResponse.getBytes());

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	private String getReturnString() throws GException {
		try {
			String strReturn;
			int intNumberOfValidSession = SessionManager.getNumberOfValidSession();

			if(m_bolReturnJSONString) {
				strReturn = "{\"NumberOfValidSession\": " + intNumberOfValidSession + "}";

			} else {
				strReturn = "Session is valid #" + intNumberOfValidSession;
			}

			return strReturn;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}
}
