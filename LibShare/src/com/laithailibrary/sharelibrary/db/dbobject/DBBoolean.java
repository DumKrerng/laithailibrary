package com.laithailibrary.sharelibrary.db.dbobject;

import com.laithailibrary.sharelibrary.db.dbutilities.GResultSet;
import exc.*;
import exc.ExceptionHandler;
import java.math.*;

/**
 * Created by IntelliJ IDEA.
 * User: dumkrerng
 * Date: 6/5/11
 * Time: 10:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class DBBoolean extends DBObject {

	public static final DBBoolean TRUE = new DBBoolean(true);
	public static final DBBoolean FALSE = new DBBoolean(false);

	private static final long serialVersionUID = 3;

	public DBBoolean() {
		super("N/A");
	}

	public DBBoolean(String p_strSting) {
		super(p_strSting);
	}

	public DBBoolean(boolean p_boolean) {
		super(p_boolean);
	}

	public DBBoolean(int p_int) {
		super(p_int);
	}

	public DBBoolean(Integer p_integer) {
		super(p_integer);
	}

	public DBBoolean(GResultSet p_resultSet, String p_strColumnName) throws GException {
		super("N/A");

		try {
			Boolean bolResult = p_resultSet.getBoolean(p_strColumnName);

			super.setBolData(bolResult);

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public Boolean getBoolean() {
		if(m_strData.compareToIgnoreCase(STRING_FALSE) == 0) {
			return false;

		} else if(m_strData.compareToIgnoreCase(STRING_TRUE) == 0) {
			return true;

		} else {
			try {
				BigDecimal bdTemp = new BigDecimal(m_strData);

				if(bdTemp.compareTo(BigDecimal.ZERO) == 0) {
					return false;
				}
			} catch(Exception exception) {
				return false;
			}
		}

		return true;
	}

	public boolean isTrue() {
		if(getBoolean()) {
			return true;

		} else {
			return false;
		}
	}

	public boolean isFalse() {
		if(!getBoolean()) {
			return true;

		} else {
			return false;
		}
	}

	public String getStringSQL() {
		String strReturn = "'" + super.getString() + "'";

		return strReturn;
	}

	protected String getInvalidDataString() {
		return "N/A";
	}

	public String getCSVString() {
		if(getBoolean()) {
			return "TRUE";
		}

		return "FALSE";
	}

	public Boolean getValueReport() {
		return getBoolean();
	}

	public String getSortString() {
		String strSortString = "!$2BOL$!";

		DBInteger dbintTemp;

		if(getBoolean()) {
			dbintTemp = new DBInteger(1);

		} else {
			dbintTemp = new DBInteger(0);
		}

		strSortString = strSortString + dbintTemp.getSortString_WithoutTag();

		return strSortString;
	}

	public int compareTo(I_DBObject p_dbObject) {
		try {
			if(p_dbObject instanceof DBBoolean) {
				return this.getSortString().compareTo(p_dbObject.getSortString());
			} else {
				throw new GException("Attempting compare " + getClass().getName() + " to " + p_dbObject.getClass().getName());
			}
		} catch(Exception exception) {
			ExceptionHandler.display(exception);
		}

		return -1;
	}

	public DBBoolean clone() {
		return new DBBoolean(getBoolean());
	}
}
