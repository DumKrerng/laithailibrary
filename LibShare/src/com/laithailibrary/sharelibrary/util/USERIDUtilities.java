package com.laithailibrary.sharelibrary.util;

import com.laithailibrary.sharelibrary.db.dbobject.*;
import com.laithailibrary.sharelibrary.field.DataField;
import com.laithailibrary.sharelibrary.field.TableName;
import com.laithailibrary.sharelibrary.collection.GMap;
import com.laithailibrary.sharelibrary.bean.DBDataTable;
import com.laithailibrary.sharelibrary.db.dbutilities.DBUtilities;
import com.laithailibrary.sharelibrary.db.dbutilities.GResultSet;
import com.laithailibrary.sharelibrary.db.dbutilities.GStatement;
import com.laithailibrary.sharelibrary.locale.*;
import com.laithailibrary.sharelibrary.session.*;
import exc.ExceptionHandler;
import exc.GException;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: dumkrerng
 * Date: 5/13/12
 * Time: 12:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class USERIDUtilities {

	private static Map<DBString, DBString> m_mapLestUSERID_Tag = new GMap<>();

	public static final String TAGFORMAT_YYMMTT = "YYMMTT";
	public static final String TAGFORMAT_TTYYMM = "TTYYMM";

	public static final int DIGT_OF_NUMBER = 6;
	public static final DBString USERID_DAFT = new DBString("USERID_DAFT");

	public static GLocale LOCALE = GLocale.English;

	private static final Object KEYLOCK = new Object();

	private USERIDUtilities() {}

	public static DBString getUSERID_Daft() {
		try {
			StringBuilder buffer = new StringBuilder();
			buffer.append(USERID_DAFT).append('-').append(DBDateTime.getCurrentDateTime().getString_DefaultFormat());

			return new DBString(buffer.toString());

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
		}

		return new DBString();
	}

	public static boolean isUSERID_Daft(DBString p_dbstrUSERID) throws GException {
		if(p_dbstrUSERID.startsWith(USERID_DAFT)) {
			return true;
		}

		return false;
	}

	public static DBString getNextUSERID(DBDataTable p_dbDataTable, DataField p_datafield, DBDate p_dbDate, ISessionID p_sessionid) throws GException {
		try {
			return getNextUSERID(p_dbDataTable, p_datafield, p_dbDate, p_dbDataTable.getUSERIDTag(), p_sessionid);

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public static DBString getNextUSERID(DBDataTable p_dbDataTable, DataField p_datafield, DBDate p_dbDate, String p_strUSERIDTag,
		ISessionID p_sessionid) throws GException {

		return getNextUSERID(p_dbDataTable, p_datafield, p_dbDate, p_strUSERIDTag, TAGFORMAT_YYMMTT, DIGT_OF_NUMBER, p_sessionid);
	}

	public static DBString getNextUSERID(DBDataTable p_dbDataTable, DataField p_datafield, DBDate p_dbDate, String p_strUSERIDTag,
		String p_strTagFormat, int p_intDigitOfNumber, ISessionID p_sessionid) throws GException {

		try {
			synchronized(KEYLOCK) {
				String strTAG = p_strTagFormat;
				strTAG = strTAG.replace("TT", p_strUSERIDTag);

				if(p_dbDate.isValid()) {
					int intMonthInYear = p_dbDate.getMonthInYear();
					DBYear dbyYear = p_dbDate.getYear();
					String strYear_TwoDigit = dbyYear.getString_TwoDigit(LOCALE);

					String strMonthInYear = String.valueOf(intMonthInYear);

					if(strMonthInYear.length() < 2) {
						strMonthInYear = "0" + strMonthInYear;
					}

					strTAG = strTAG.replace("YY", strYear_TwoDigit);
					strTAG = strTAG.replace("MM", strMonthInYear);

				} else {
					strTAG = strTAG.replace("YY", "");
					strTAG = strTAG.replace("MM", "");
				}

				DBString dbstrNextUSERID_Lest = new DBString();
				DBString dbstrTag = new DBString(strTAG);

				if(m_mapLestUSERID_Tag.containsKey(dbstrTag)) {
					dbstrNextUSERID_Lest = m_mapLestUSERID_Tag.get(dbstrTag);

				} else {
					TableName tablename = p_dbDataTable.getTableName();
					String strFieldName = p_datafield.getFieldName();

					StringBuilder builder = new StringBuilder();
					builder.append("SELECT MAX(").append(strFieldName).append(") AS MaxOfUSERID\n");
					builder.append("FROM ").append(tablename.getTableName()).append('\n');
					builder.append("WHERE ").append(strFieldName).append(" LIKE ").append(dbstrTag.getStringSQL_StartWith());

					GStatement stm = new GStatement(DBUtilities.getConnection(p_sessionid));
					GResultSet rst = stm.executeQuery(builder.toString());

					if(rst.next()) {
						dbstrNextUSERID_Lest = new DBString(rst, "MaxOfUSERID");
					}
				}

				Integer intNextUSERID_Number = 1;

				if(dbstrNextUSERID_Lest.isValid()) {
					DBString dbstrNextUSERID_Number = dbstrNextUSERID_Lest.replaceAll(dbstrTag.getString(), "");
					intNextUSERID_Number = Integer.valueOf(dbstrNextUSERID_Number.getString());
					intNextUSERID_Number++;
				}

				String strNewUSERID_Number = String.valueOf(intNextUSERID_Number);

				if(strNewUSERID_Number.length() > p_intDigitOfNumber) {
					throw new GException("USERID is overflow!");
				}

				while(strNewUSERID_Number.length() < p_intDigitOfNumber) {
					strNewUSERID_Number = "0" + strNewUSERID_Number;
				}

				DBString dbstrNextUSERID = dbstrTag.concat(new DBString(strNewUSERID_Number));

				m_mapLestUSERID_Tag.put(dbstrTag, dbstrNextUSERID);

				return dbstrNextUSERID;
			}
		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}
}
