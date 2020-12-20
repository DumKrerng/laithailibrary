package com.laithailibrary.sharelibrary.db.dbutilities;

import com.laithailibrary.sharelibrary.collection.GList;
import com.laithailibrary.sharelibrary.collection.GMap;
import com.laithailibrary.sharelibrary.db.dbobject.*;
import exc.*;
import exc.ExceptionHandler;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: dumkrerng
 * Date: 3/1/12
 * Time: 10:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class GResultSet {

	private List<String> m_lsColumnName = null;
	private Map<Integer, Vector<Object>> m_mapData_RowNumber = null;
	private Map<String, Integer> m_mapColumnNumber_ColumnName = null;
	private int m_intColumnCount;

	private int m_intRowCount;
	private int m_intIndexRow;

	private static final long serialVersionUID = 12;

	public GResultSet() {}

	public GResultSet(ResultSet p_resultset) throws GException {
		try {
			m_lsColumnName = new GList<>();
			m_mapData_RowNumber = new GMap<>();
			m_mapColumnNumber_ColumnName = new GMap<>();
			m_intColumnCount = 0;
			m_intRowCount = 0;
			m_intIndexRow = -1;
			
			ResultSetMetaData resultSetMetaData = p_resultset.getMetaData();
			m_intColumnCount = resultSetMetaData.getColumnCount();

			for(int ColumnIndex = 1; ColumnIndex <= m_intColumnCount; ColumnIndex++) {
				String strColumnName = resultSetMetaData.getColumnLabel(ColumnIndex).toLowerCase();
				m_lsColumnName.add(strColumnName);
				m_mapColumnNumber_ColumnName.put(strColumnName, Integer.valueOf(ColumnIndex - 1));
			}

			int intRowNumber = 0;

			while(p_resultset.next()) {
				Vector<Object> vector = new Vector<>(m_intColumnCount);
				for(String strColumnName : m_lsColumnName) {
					Object objData = p_resultset.getObject(strColumnName);
					vector.add(objData);
				}

				m_mapData_RowNumber.put(intRowNumber, vector);

				intRowNumber++;
			}

			m_intRowCount = intRowNumber;

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public String getString(String p_strColumnName) throws GException {
		try {
			if(m_mapColumnNumber_ColumnName.containsKey(p_strColumnName.toLowerCase())) {
				Integer intColumnNumber = m_mapColumnNumber_ColumnName.get(p_strColumnName.toLowerCase());

				int intCurrentIndexRow = getCurrentIndexRow();
				Vector<Object> vector = m_mapData_RowNumber.get(Integer.valueOf(intCurrentIndexRow));
				String strValue = getStringValue(vector, intColumnNumber);

				if(strValue == null) {
					strValue = "";
				}

				return strValue;

			} else {
				throw new GException("Not-Existing Column Name: " + p_strColumnName + ".");
			}
		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public int getInt(String p_strColumnName) throws GException {
		try {
			if(m_mapColumnNumber_ColumnName.containsKey(p_strColumnName.toLowerCase())) {
				Integer intColumnNumber = m_mapColumnNumber_ColumnName.get(p_strColumnName.toLowerCase());

				int intCurrentIndexRow = getCurrentIndexRow();
				Vector<Object> vector = m_mapData_RowNumber.get(Integer.valueOf(intCurrentIndexRow));
				String strValue = getStringValue(vector, intColumnNumber);

				Integer intData = null;

				if(strValue.length() > 0) {
					intData = Integer.parseInt(strValue);
				}

				if(intData == null) {
					return DBInteger.INVALID_INTEGER;
				}

				return intData.intValue();

			} else {
				throw new GException("Not-Existing Column Name: " + p_strColumnName + ".");
			}
		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public Boolean getBoolean(String p_strColumnName) throws GException {
		try {
			if(m_mapColumnNumber_ColumnName.containsKey(p_strColumnName.toLowerCase())) {
				Integer intColumnNumber = m_mapColumnNumber_ColumnName.get(p_strColumnName.toLowerCase());

				int intCurrentIndexRow = getCurrentIndexRow();
				Vector<Object> vector = m_mapData_RowNumber.get(Integer.valueOf(intCurrentIndexRow));
				String strValue = getStringValue(vector, intColumnNumber);

				if(strValue == null) {
					throw new GException("DBBoolean must be valid.");
				}

				Boolean bolData;

				if(strValue.compareToIgnoreCase("FALSE") == 0
					|| strValue.compareToIgnoreCase("0") == 0) {

					bolData = Boolean.FALSE;

				} else {
					bolData = Boolean.TRUE;
				}

				return bolData;

			} else {
				throw new GException("Not-Existing Column Name: " + p_strColumnName + ".");
			}
		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public DBDate getDate(String p_strColumnName) throws GException {
		try {
			if(m_mapColumnNumber_ColumnName.containsKey(p_strColumnName.toLowerCase())) {
				Integer intColumnNumber = m_mapColumnNumber_ColumnName.get(p_strColumnName.toLowerCase());

				int intCurrentIndexRow = getCurrentIndexRow();
				Vector<Object> vector = m_mapData_RowNumber.get(Integer.valueOf(intCurrentIndexRow));
				String strValue = getStringValue(vector, intColumnNumber);
				DBDate dbdData;

				if(strValue.length() <= 0) {
					dbdData = new DBDate();

				} else {
					dbdData = new DBDate(strValue);
				}

				return dbdData;

			} else {
				throw new GException("Not-Existing Column Name: " + p_strColumnName + ".");
			}
		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public DBDateTime getDateTime(String p_strColumnName) throws GException {
		try {
			if(m_mapColumnNumber_ColumnName.containsKey(p_strColumnName.toLowerCase())) {
				Integer intColumnNumber = m_mapColumnNumber_ColumnName.get(p_strColumnName.toLowerCase());

				int intCurrentIndexRow = getCurrentIndexRow();
				Vector<Object> vector = m_mapData_RowNumber.get(Integer.valueOf(intCurrentIndexRow));
				String strValue = getStringValue(vector, intColumnNumber);
				DBDateTime dbdtDataTime;

				if(strValue.length() <= 0) {
					dbdtDataTime = new DBDateTime();

				} else {
					dbdtDataTime = new DBDateTime(strValue);
				}

				return dbdtDataTime;

			} else {
				throw new GException("Not-Existing Column Name: " + p_strColumnName + ".");
			}
		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public DBDecimal getDecimal(String p_strColumnName) throws GException {
		try {
			if(m_mapColumnNumber_ColumnName.containsKey(p_strColumnName.toLowerCase())) {
				Integer intColumnNumber = m_mapColumnNumber_ColumnName.get(p_strColumnName.toLowerCase());

				int intCurrentIndexRow = getCurrentIndexRow();
				Vector<Object> vector = m_mapData_RowNumber.get(Integer.valueOf(intCurrentIndexRow));
				String strValue = getStringValue(vector, intColumnNumber);

				DBDecimal dbdcData;

				if(strValue.length() <= 0) {
					dbdcData = new DBDecimal(DBDecimal.INVALID_DBDecimal);

				} else {
					dbdcData = new DBDecimal(strValue);
				}

				return dbdcData;

			} else {
				throw new GException("Not-Existing Column Name: " + p_strColumnName + ".");
			}
		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public int getCurrentIndexRow() throws GException {
		try {
			if(m_intIndexRow > -1) {
				return m_intIndexRow;
			} else {
				throw new GException("Row Index: -1");
			}
		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public int getRowCount() throws GException {
		return m_intRowCount;
	}

	public boolean next() throws GException {
		try {
		  m_intIndexRow++;

			if(m_intIndexRow < getRowCount()) {
				return true;
			}

			m_intIndexRow--;

			return false;

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public void beforeFirst() {
		m_intIndexRow = -1;
	}

	private static String getStringValue(Vector<Object> p_vector, int p_intIndex) {
		Object objData = p_vector.get(p_intIndex);

		if(objData == null) {
			return "";
		}

		return objData.toString();
	}
}
