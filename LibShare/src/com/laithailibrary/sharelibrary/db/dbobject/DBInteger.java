package com.laithailibrary.sharelibrary.db.dbobject;

import java.math.*;
import java.util.*;
import com.laithailibrary.sharelibrary.datatableview.*;
import com.laithailibrary.sharelibrary.db.dbutilities.*;
import com.laithailibrary.sharelibrary.field.*;
import com.laithailibrary.sharelibrary.session.*;
import exc.*;

/**
 * Created by IntelliJ IDEA.
 * User: dumkrerng
 * Date: 6/5/11
 * Time: 1:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class DBInteger extends DBObject {

	public static final DBInteger Zero = new DBInteger(0);

	public static final int INVALID_INTEGER = -999999999;

	private static final long serialVersionUID = 2;

	public DBInteger() {
		super(INVALID_INTEGER);
	}

	public DBInteger(int p_int) {
		super(p_int);
	}

	public DBInteger(Integer p_integer) {
		super(p_integer);
	}

	public DBInteger(String p_string) {
		super(p_string);
	}

	public DBInteger(GResultSet p_resultSet, String p_strColumnName) throws GException {
		super(INVALID_INTEGER);

		try {
			int intResult = p_resultSet.getInt(p_strColumnName);

			if(intResult != INVALID_INTEGER) {
				super.setIntData(intResult);
			}
		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public DBInteger(DataVectorTable p_dtvView, String p_strColumnName) throws GException {
		super(INVALID_INTEGER);

		try {
			DataVectorRow dvrRow = p_dtvView.getDataRow();
			DBInteger dbintValue = (DBInteger)dvrRow.getDataAtColumnName(p_strColumnName);

			if(dbintValue.isValid()) {
				if(dbintValue.compareTo(new DBInteger(INVALID_INTEGER)) != 0) {
					super.setIntData(Integer.parseInt(dbintValue.getString()));
				}
			}
		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public DBInteger(DataVectorRow p_dvrRow, String p_strColumnName) throws GException {
		super(INVALID_INTEGER);

		try {
			DBInteger dbintValue = (DBInteger)p_dvrRow.getDataAtColumnName(p_strColumnName);

			if(dbintValue.compareTo(new DBInteger(INVALID_INTEGER)) != 0) {
				super.setIntData(Integer.parseInt(dbintValue.getString()));
			}
		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	protected void setInvalidData() {
		m_strData = String.valueOf(INVALID_INTEGER);
	}

	protected String getInvalidDataString() {
		return String.valueOf(INVALID_INTEGER);
	}

	public Integer getIntValue() {
		if(isValid()) {
			return Integer.parseInt(m_strData);
		}

		return 0;
	}

	public String getString() {
		if(isInvalid()) {
			return "";
		}

		return m_strData;
	}

	public boolean isInvalid() {
		if(m_strData.compareTo(String.valueOf(INVALID_INTEGER)) == 0) {
			return true;
		}

		if(m_strData.equalsIgnoreCase(getInvalidDataString())) {
			return true;
		}

		return false;
	}

	public boolean isValid() {
		if(m_strData.compareTo(String.valueOf(INVALID_INTEGER)) == 0) {
			return false;
		}

		if(!m_strData.equalsIgnoreCase(getInvalidDataString())) {
			return true;
		}

		return false;
	}

	public static DBInteger getNextItemNumber(DataField p_datafield, DataField p_dataieldGroupID, DBObject p_dbobjGroupID,
		ISessionID p_sessionid) throws GException {

		try {
			if(p_dbobjGroupID.isInvalid()) {
				throw new GException("Invalid GroupID!");
			}

			TableName tablename = p_datafield.getTableName();
			String strFieldName = p_datafield.getFieldName();

			String strFieldName_GroupID = p_dataieldGroupID.getFieldName();

		  StringBuilder builder = new StringBuilder();
			builder.append("SELECT MAX(").append(strFieldName).append(") MaxOfItemNumber\n");
			builder.append("FROM ").append(tablename.getTableName()).append("\n");
			builder.append("WHERE ").append(strFieldName_GroupID).append(" = ").append(p_dbobjGroupID.getStringSQL());

			GStatement stm = new GStatement(DBUtilities.getConnection(p_sessionid));
			GResultSet rst = stm.executeQuery(builder.toString());

			DBInteger dbintNextItemNumber = new DBInteger();

			if(rst.next()) {
				dbintNextItemNumber = new DBInteger(rst, "MaxOfItemNumber");
			}

			if(dbintNextItemNumber.isInvalid()) {
				dbintNextItemNumber = new DBInteger(1);

			} else {
				dbintNextItemNumber = dbintNextItemNumber.add(1);
			}

			return dbintNextItemNumber;

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public static boolean isInteger(String p_strData) throws GException {
		try {
		  Integer.getInteger(p_strData);

		} catch(Exception exception) {
			return false;
		}

		return true;
	}

	public DBInteger add(int p_int) throws GException {
		try {
			int intTemp = this.getIntValue() + p_int;

			return new DBInteger(intTemp);

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public DBInteger add(DBInteger p_dbInteger) throws GException {
		try {
			int intTemp = this.getIntValue() + p_dbInteger.getIntValue();

			return new DBInteger(intTemp);

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public DBInteger negate() throws GException {
		try {
			if(isInvalid()) {
				return new DBInteger();
			}

			int intNewValue = getIntValue() * -1;
			DBInteger dbintReturn = new DBInteger(intNewValue);

			return dbintReturn;

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public DBInteger multiply(int p_intValue) throws GException {
		return multiply(new DBInteger(p_intValue));
	}

	public DBInteger multiply(Integer p_dbInteger) throws GException {
		return multiply(new DBInteger(p_dbInteger));
	}

	public DBInteger multiply(DBInteger p_dbInteger) throws GException {
		try {
			if(p_dbInteger.isInvalid()
				|| isInvalid()) {

				return new DBInteger();
			}

			int intNewValue = getIntValue() * p_dbInteger.getIntValue();
			DBInteger dbintReturn = new DBInteger(intNewValue);

			return dbintReturn;

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public DBDecimal divide(DBInteger p_dbInteger) throws GException {
		return divide(p_dbInteger, SizeAfterDecimal, RoundingMode.HALF_UP);
	}

	public DBDecimal divide(DBInteger p_dbInteger, int p_intScale, RoundingMode p_roundingmode) throws GException {
		try {
			if(p_dbInteger.isInvalid()
				|| isInvalid()) {

				return new DBDecimal();
			}

			DBDecimal dbDecimal1 = new DBDecimal(getIntValue());
			DBDecimal dbDecimal2 = new DBDecimal(p_dbInteger.getIntValue());
			DBDecimal dbdReturn = dbDecimal1.divide(dbDecimal2, p_intScale, p_roundingmode);

			return dbdReturn;

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public DBInteger getSumValue(List<DataVectorRow> p_lsSelectedDataVectorRows, String p_strColumnName) throws GException {
		try {
			DBInteger dbintSumValue = new DBInteger(0);

			for(DataVectorRow dvrRow : p_lsSelectedDataVectorRows) {
				DBInteger dbintValue = dvrRow.getDataAtColumnName(DBInteger.class, p_strColumnName);
				dbintSumValue = dbintSumValue.add(dbintValue);
			}

			return dbintSumValue;

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public DBInteger getSumValue(GResultSet p_rst, String p_strColumnName) throws GException {
		try {
			p_rst.beforeFirst();

			DBInteger dbintSumValue = new DBInteger(0);

			while(p_rst.next()) {
				DBInteger dbintValue = new DBInteger(p_rst, p_strColumnName);
				dbintSumValue = dbintSumValue.add(dbintValue);
			}

			p_rst.beforeFirst();

			return dbintSumValue;

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public DBInteger getSumValue(DataVectorTable p_dvtTable, String p_strColumnName) throws GException {
		try {
			p_dvtTable.beforeFirst();

			DBInteger dbintSumValue = new DBInteger(0);

			while(p_dvtTable.next()) {
				DataVectorRow dvrRow = p_dvtTable.getDataRow();
				DBInteger dbintValue = dvrRow.getDataAtColumnName(DBInteger.class, p_strColumnName);
				dbintSumValue = dbintSumValue.add(dbintValue);
			}

			p_dvtTable.beforeFirst();

			return dbintSumValue;

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public String getStringSQL() {
		String strReturn;

		if(isInvalid()) {
			strReturn = "NULL";

		} else {
			strReturn = getString();
		}

		return strReturn;
	}

	public String getCSVString() {
		String strReturn = getString();

		if(isInvalid()) {
			strReturn = "";
		}

		return strReturn;
	}

	public Integer getValueReport() {
		if(isInvalid()) {
			return null;
		}

		return Integer.valueOf(getStringSQL());
	}

	public String getSortString() {
		return getSortString("!$0INT$!");
	}

	public String getSortString(String p_strTag) {
		String strSortString = p_strTag;

		try {
			if(isValid()) {
				String strFill = "0";

				if(this.getIntValue() < 0) {
					strFill = "z";
				}

				String strData_ToSort = super.getString();
				StringTokenizer token = new StringTokenizer(strData_ToSort, ".");
				String strData_01 = token.nextToken();
				String strData_02 = "0";

				if(strData_01.length() < 19) {
					int intLength = strData_01.length();

					for(int index = intLength - 1; index <= 19; index++) {
						strData_01 = strFill + strData_01;
					}
				}

				if(this.getIntValue() < 0) {
					strData_01 = '&' + strData_01;

				} else {
					strData_01 = '0' + strData_01;
				}

				if(strData_02.length() < SizeAfterDecimal) {
					int intLength = strData_02.length();

					for(int index = intLength - 1; index <= SizeAfterDecimal; index++) {
						strData_02 = strData_02 + '0';
					}
				}

				strSortString = strSortString + strData_01 + '.' + strData_02;
			}
		} catch (Exception exception) {
			ExceptionHandler.display(exception);
		}

		return strSortString;
	}

	public String getSortString_WithoutTag() {
		return getSortString("");
	}

	public int compareTo(I_DBObject p_dbObject) {
		try {
			if(p_dbObject instanceof DBInteger) {
				return this.getSortString().compareTo(p_dbObject.getSortString());
			} else {
				throw new GException("Attempting compare " + getClass().getName() + " to " + p_dbObject.getClass().getName());
			}
		} catch(Exception exception) {
			ExceptionHandler.display(exception);
		}

		return -1;
	}

	public DBInteger clone() {
		try {
			return new DBInteger(getIntValue());

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
		}

		return new DBInteger();
	}
}
