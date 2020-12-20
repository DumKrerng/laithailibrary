package com.laithailibrary.serverlibrary.server.serviceserver;

import com.laithailibrary.sharelibrary.db.dbobject.*;
import com.laithailibrary.sharelibrary.db.dbutilities.*;
import com.laithailibrary.sharelibrary.session.*;
import com.laithailibrary.sharelibrary.sqlstatement.support.*;
import exc.*;

/**
 * Created by dumkrerng on 25/11/2560.
 */
public class GWebShortLinkManager {

	private static final String[] STRING_NOT_ALLOWED = {"'", "~", "!", "^", "\\", ";", "[", "]", "`"};

	private static final String LOCK = "GWebShortLinkManager";

	public GWebShortLinkManager() {}

	public static void createWebShortLinkTable(ISessionID p_sessionid) throws GException {
		try {
			DBUtilities.getConnection(p_sessionid).transactionBegin();

			StringBuilder buffer = new StringBuilder();
			buffer.append("CREATE TABLE WebShortLink_tbl(\n");
			buffer.append("WebShortLinkID VARCHAR(6) PRIMARY KEY NOT NULL,\n");
			buffer.append("WebFullLinkValue VARCHAR(500) NOT NULL)");

			GStatement stmt = new GStatement(DBUtilities.getConnection(p_sessionid));
			stmt.executeUpdate(buffer.toString());

			buffer = new StringBuilder();
			buffer.append("CREATE INDEX ix_WebFullLinkValue_WebShortLink_tbl\n");
			buffer.append("ON WebShortLink_tbl(WebFullLinkValue)");

			stmt = new GStatement(DBUtilities.getConnection(p_sessionid));
			stmt.executeUpdate(buffer.toString());

			DBUtilities.getConnection(p_sessionid).commit();

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public DBString getNewWebShortLinkID(ISessionID p_sessionid, String p_strWebFullLinkValue, TransactionBegin p_transactionBegin) throws GException {
		synchronized(LOCK) {
			try {
				String strWebShortLinkID;
				boolean inserted = false;

				if(p_transactionBegin == TransactionBegin.Yes) {
					DBUtilities.getConnection(p_sessionid).transactionBegin();
				}

				do {
					strWebShortLinkID = getWebShortLinkID();

					StringBuilder buffer = new StringBuilder();
					buffer.append("SELECT *\n");
					buffer.append("FROM WebShortLink_tbl\n");
					buffer.append("WHERE WebShortLinkID = ").append(new DBString(strWebShortLinkID).getStringSQL());

					GStatement stmt = new GStatement(DBUtilities.getConnection(p_sessionid));
					GResultSet rst = stmt.executeQuery(buffer);

					if(!rst.next()) {
						buffer = new StringBuilder();
						buffer.append("INSERT INTO WebShortLink_tbl(WebShortLinkID, WebFullLinkValue)\n");
						buffer.append("VALUES(").append(new DBString(strWebShortLinkID).getStringSQL()).append(",\n");
						buffer.append(new DBString(p_strWebFullLinkValue).getStringSQL()).append(")\n");

						stmt = new GStatement(DBUtilities.getConnection(p_sessionid));
						stmt.executeUpdate(buffer.toString());

						inserted = true;
					}
				} while(!inserted);

				if(p_transactionBegin == TransactionBegin.Yes) {
					DBUtilities.getConnection(p_sessionid).commit();
				}

				return new DBString(strWebShortLinkID);

			} catch(Exception exception) {
				ExceptionHandler.display(exception);
				throw new GException(exception);
			}
		}
	}

	private String getWebShortLinkID() throws GException {
		try {
			String strReturn;
			boolean finish;

			do {
				strReturn = randomstring();
				finish = true;

				for(String strTemp : STRING_NOT_ALLOWED) {
					if(strReturn.contains(strTemp)) {
						finish = false;
					}
				}
			} while(!finish);

			return strReturn;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}


	private static String randomstring() {
		int n = 6;
		byte b[] = new byte[n];
		for (int i = 0; i < n; i++) {
			b[i] = (byte)rand('A', 'z');
		}

		return new String(b);
	}

	private static int rand(int lo, int hi) {
		java.util.Random rn = new java.util.Random();
		int n = hi - lo + 1;
		int i = rn.nextInt(n);
		if (i < 0)
			i = -i;
		return lo + i;
	}
}
