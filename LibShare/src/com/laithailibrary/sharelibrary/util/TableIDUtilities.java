package com.laithailibrary.sharelibrary.util;

import java.util.*;
import com.laithailibrary.sharelibrary.bean.*;
import com.laithailibrary.sharelibrary.collection.*;
import com.laithailibrary.sharelibrary.db.dbobject.*;
import com.laithailibrary.sharelibrary.db.dbutilities.*;
import com.laithailibrary.sharelibrary.field.*;
import com.laithailibrary.sharelibrary.session.*;
import com.laithailibrary.sharelibrary.util.support.*;
import exc.*;

/**
 * Created with IntelliJ IDEA.
 * User: dumkrerng
 * Date: 5/13/12
 * Time: 12:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class TableIDUtilities {

	private static final String LOCK = "lock";

	private static Map<KEYTagPrimaryKey, DBString> s_mapLestID_KEYTagPrimaryKey = new GMap<>();

	public static final int SIZE_PrimaryKeyID = 16;
//	public static final DBString TableID_DAFT = new DBString("TableID_DAFT");

	private TableIDUtilities() {}

	public static DBString getNextPrimaryKeyID(DBDataTable p_dbDataTable, DBDate p_dbDate, ISessionID p_sessionid) throws GException {
		try {
			synchronized(LOCK) {
				DBString dbstrTagPrimaryKey = new DBString(p_dbDataTable.getTableID().getID());

				int intMonthInYear = p_dbDate.getMonthInYear();
				DBYear dbyYear = p_dbDate.getYear();
				String strYear_TwoDigit = dbyYear.getString_TwoDigit();

				String strMonthInYear = String.valueOf(intMonthInYear);

				if(strMonthInYear.length() < 2) {
					strMonthInYear = "0" + strMonthInYear;
				}

				dbstrTagPrimaryKey = dbstrTagPrimaryKey.concat(strYear_TwoDigit).concat(strMonthInYear);

				TableName tablename = p_dbDataTable.getTableName();
				KEYTagPrimaryKey key = new KEYTagPrimaryKey(tablename.getTableName(), dbstrTagPrimaryKey.getString());

				DBString dbstrPrimaryKeyID_Lest = new DBString();

				if(s_mapLestID_KEYTagPrimaryKey.containsKey(key)) {
					dbstrPrimaryKeyID_Lest = s_mapLestID_KEYTagPrimaryKey.get(key);

				} else {
					String strFieldName_PrimaryKey = p_dbDataTable.getFieldPrimaryKey().getPrimaryKeyName();

					StringBuilder builder = new StringBuilder();
					builder.append("SELECT MAX(").append(strFieldName_PrimaryKey).append(") AS MaxOfPrimaryKeyID\n");
					builder.append("FROM ").append(tablename.getTableName()).append('\n');
					builder.append("WHERE ").append(strFieldName_PrimaryKey).append(" LIKE ").append(dbstrTagPrimaryKey.getStringSQL_StartWith());

					GStatement stm = new GStatement(DBUtilities.getConnection(p_sessionid));
					GResultSet rst = stm.executeQuery(builder.toString());

					if(rst.next()) {
						dbstrPrimaryKeyID_Lest = new DBString(rst, "MaxOfPrimaryKeyID");
					}
				}

				Integer intNextPrimaryKeyID_Number = 0;

				if(dbstrPrimaryKeyID_Lest.isValid()) {
					DBString dbstrNextTableID_Number = dbstrPrimaryKeyID_Lest.replaceAll(dbstrTagPrimaryKey.getString(), "");
					intNextPrimaryKeyID_Number = Integer.valueOf(dbstrNextTableID_Number.getString());
					intNextPrimaryKeyID_Number++;
				}

				String strNewPrimaryKeyID_WithoutTag = String.valueOf(intNextPrimaryKeyID_Number);
				int intNewPrimaryKeyID_Size = strNewPrimaryKeyID_WithoutTag.length();

				int intTagPrimaryKey_Size = dbstrTagPrimaryKey.length();
				int intNumber_Size = SIZE_PrimaryKeyID - intTagPrimaryKey_Size;

				if(intNewPrimaryKeyID_Size > intNumber_Size) {
					throw new GException("New Primary Key is overflow!");
				}

				while(intNewPrimaryKeyID_Size < intNumber_Size) {
					strNewPrimaryKeyID_WithoutTag = "0" + strNewPrimaryKeyID_WithoutTag;

					intNewPrimaryKeyID_Size = strNewPrimaryKeyID_WithoutTag.length();
				}

				DBString dbstrNextPrimaryKeyID = dbstrTagPrimaryKey.concat(new DBString(strNewPrimaryKeyID_WithoutTag));

				s_mapLestID_KEYTagPrimaryKey.put(key, dbstrNextPrimaryKeyID);

				return dbstrNextPrimaryKeyID;
			}
		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}
}
