package com.laithailibrary.sharelibrary.service;

import java.io.*;
import java.net.*;
import com.laithailibrary.logger.*;
import com.laithailibrary.sharelibrary.db.dbobject.*;
import com.laithailibrary.sharelibrary.db.dbutilities.*;
import com.laithailibrary.sharelibrary.servicecall.*;
import com.laithailibrary.sharelibrary.session.*;
import com.laithailibrary.sharelibrary.socket.*;
import com.laithailibrary.sharelibrary.support.*;
import exc.*;

/**
 * Created by dumkrerng on 25/11/2560.
 */
public class GWebShortLink {

	private GWebShortLink() {}

	public static DBString getWebFullLinkValue(ISessionID p_sessionid, String p_strWebShortLinkID) throws GException {
		try {
			DBString dbstrWebFullLinkValue = new DBString();

			if(GUtilities.isServer()) {
				StringBuilder builder = new StringBuilder();
				builder.append("SELECT WebShortLinkID, WebFullLinkValue\n");
				builder.append("FROM WebShortLink_tbl\n");
				builder.append("WHERE WebShortLinkID = ").append(new DBString(p_strWebShortLinkID).getStringSQL());

				GStatement stmt = new GStatement(DBUtilities.getConnection(p_sessionid));
				GResultSet rst = stmt.executeQuery(builder);

				if(rst.next()) {
					dbstrWebFullLinkValue = new DBString(rst, "WebFullLinkValue");
				}
			} else {
				ServiceCall_WebShortLink servicecall = new ServiceCall_WebShortLink(p_sessionid, AppClientUtilities.getServerAddress(),
					p_strWebShortLinkID);

				GLog.info("Get WebFullLinkValue.");

				Socket socket = ClientSocket.createSocket(AppClientUtilities.getServerAddress(), AppClientUtilities.getServerPort());

				OutputStream output = socket.getOutputStream();
				ObjectOutputStream objOutputStream = new ObjectOutputStream(output);
				objOutputStream.writeObject(servicecall);
				objOutputStream.flush();

				InputStream input = socket.getInputStream();
				ObjectInputStream objInputStream = new ObjectInputStream(input);
				dbstrWebFullLinkValue = (DBString)objInputStream.readObject();
			}

			return dbstrWebFullLinkValue;

		} catch(SocketTimeoutException p_excTimeout) {
			throw new GException("Connection Timeout!!!");

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}
}
