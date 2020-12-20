package com.laithailibrary.serverlibrary.server.paymentgateway;

import javax.net.ssl.*;
import java.io.*;
import java.net.*;
import com.laithailibrary.sharelibrary.db.dbobject.*;
import exc.*;
import org.apache.commons.codec.binary.*;
import org.json.*;

public class OmiseManager {

	private String m_strOmise_PKEY = "";
	private String m_strOmise_SKEY = "";

	private OmiseManager() {}

	public OmiseManager(String p_strOmise_PKEY, String p_strOmise_SKEY) {
		m_strOmise_PKEY = p_strOmise_PKEY;
		m_strOmise_SKEY = p_strOmise_SKEY;
	}

	public JSONObject doCharges_THB_Token(String p_strOmiseToken, String p_strPaidAmount_Satang, String p_strDescription, String p_strURLReturn) throws GException {
		try {
			if(p_strOmiseToken.isEmpty()) {
				throw new GException("Invalid OmiseToken!!!");
			}

			if(p_strPaidAmount_Satang.isEmpty()) {
				throw new GException("Invalid PaidAmount!!!");
			}

			StringBuilder bufferParameter = new StringBuilder();
			bufferParameter.append("amount=").append(p_strPaidAmount_Satang);
			bufferParameter.append("&currency=THB");
			bufferParameter.append("&card=").append(p_strOmiseToken);
			bufferParameter.append("&description=").append(p_strDescription);

			if(!p_strURLReturn.isEmpty()) {
				bufferParameter.append("&return_uri=").append(p_strURLReturn);
			}

			String authorization = m_strOmise_SKEY;
			String encoded = Base64.encodeBase64String(authorization.getBytes());
			authorization = "Basic " + encoded;

			return getResponse(authorization, "https://api.omise.co/charges", bufferParameter.toString());

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public JSONObject getChargesTransaction(DBDate p_dbdtDateBegin, DBDate p_dbdtDateEnd) throws GException {
		try {
			if(p_dbdtDateBegin.isInvalid()
				|| p_dbdtDateEnd.isInvalid()) {

				throw new GException("Invalid Date!!!");
			}

			StringBuilder bufferParameter = new StringBuilder();
			bufferParameter.append("from=").append(p_dbdtDateBegin.getString_DefaultFormat());
			bufferParameter.append("&to=").append(p_dbdtDateEnd.getString_DefaultFormat());

			String authorization = m_strOmise_SKEY;
			String encoded = Base64.encodeBase64String(authorization.getBytes());
			authorization = "Basic " + encoded;

			return getResponse(authorization, "GET", "https://api.omise.co/charges?" + bufferParameter, "");

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public JSONObject getEventsTransaction(DBDate p_dbdtDateBegin, DBDate p_dbdtDateEnd) throws GException {
		try {
			if(p_dbdtDateBegin.isInvalid()
				|| p_dbdtDateEnd.isInvalid()) {

				throw new GException("Invalid Date!!!");
			}

			StringBuilder bufferParameter = new StringBuilder();
			bufferParameter.append("from=").append(p_dbdtDateBegin.getString_DefaultFormat());
			bufferParameter.append("&to=").append(p_dbdtDateEnd.getString_DefaultFormat());

			String authorization = m_strOmise_SKEY;
			String encoded = Base64.encodeBase64String(authorization.getBytes());
			authorization = "Basic " + encoded;

			return getResponse(authorization, "GET", "https://api.omise.co/events?" + bufferParameter, "");

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public JSONObject getBalance() throws GException {
		try {
			String authorization = m_strOmise_SKEY;
			String encoded = Base64.encodeBase64String(authorization.getBytes());
			authorization = "Basic " + encoded;

			return getResponse(authorization, "GET", "https://api.omise.co/balance", "");

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	private JSONObject getResponse(String p_strAuthorization, String p_strRequestAction, String p_strURL, String p_strParameters) throws GException {
		try {
			URL url = new URL(p_strURL);

			return getResponse(p_strAuthorization, p_strRequestAction, url, p_strParameters);

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	private JSONObject getResponse(String p_strAuthorization, String p_strURL, String p_strParameters) throws GException {
		try {
			URL url = new URL(p_strURL);

			return getResponse(p_strAuthorization, "", url, p_strParameters);

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	private JSONObject getResponse(String p_strAuthorization, String p_strRequestAction, URL p_url, String p_strParameters) throws GException {
		try {
			HttpsURLConnection conn = (HttpsURLConnection)p_url.openConnection();

			if(p_strRequestAction.isEmpty()) {
				conn.setRequestMethod("POST");

			} else {
				conn.setRequestMethod(p_strRequestAction);
			}

			conn.addRequestProperty("Authorization", p_strAuthorization);
			JSONObject jsonResponse = connect(conn, p_strParameters);

			return jsonResponse;

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	private JSONObject connect(HttpsURLConnection p_conn, String p_strParameters) throws GException {
		try {
			p_conn.setDoInput(true);

			if(p_conn.getRequestMethod().compareTo("GET") == 0) {
				p_conn.setDoOutput(false);
				p_conn.connect();

			} else {
				p_conn.setDoOutput(true);
				p_conn.connect();

				OutputStreamWriter writer = new OutputStreamWriter(p_conn.getOutputStream());
				writer.write(p_strParameters);
				writer.close();
			}

			BufferedReader reader;
			boolean bolResponseError = false;

			if(p_conn.getResponseCode() >= 300) {
				reader = new BufferedReader(new InputStreamReader(p_conn.getErrorStream(), "utf-8"));
				bolResponseError = true;

			} else {
				reader = new BufferedReader(new InputStreamReader(p_conn.getInputStream(), "utf-8"));
			}

			StringBuilder bufferResponse = new StringBuilder();
			String line = reader.readLine();

			while(line != null) {
				bufferResponse.append(line).append('\n');

				line = reader.readLine();
			}

			JSONObject jsonResponse = new JSONObject(bufferResponse.toString());

			if(bolResponseError) {
				String strCode = jsonResponse.getString("code");
				String strMessage = jsonResponse.getString("message");
				String strObject = jsonResponse.getString("object");

				StringBuilder bufferError = new StringBuilder();
				bufferError.append("Request: ").append(p_conn.getURL().toString()).append('\n');
				bufferError.append("Method: ").append(p_conn.getRequestMethod()).append('\n');
				bufferError.append("Code: ").append(strCode).append('\n');
				bufferError.append("Message: ").append(strMessage).append('\n');
				bufferError.append("Object: ").append(strObject).append('\n');

				throw new GException(bufferError);
			}

			return jsonResponse;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}
}
