package com.laithailibrary.sharelibrary.db.dbobject;


import java.io.*;
import java.math.*;
import java.text.*;
import java.util.*;
import com.laithailibrary.sharelibrary.datatableview.*;
import com.laithailibrary.sharelibrary.db.dbobject.support.*;
import com.laithailibrary.sharelibrary.db.dbutilities.*;
import exc.*;

/**
 * Created by IntelliJ IDEA.
 * User: dumkrerng
 * Date: 5/12/12
 * Time: 3:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class DBDecimal extends DBObject {

	public static final String INVALID_DATASTRING = "-999999999999999999999.900000000";

	public static final DBDecimal Zero = new DBDecimal(0.00);
	public static final DBDecimal INVALID_DBDecimal = new DBDecimal(INVALID_DATASTRING);

	private static final BigDecimal DATA_CAL_LESSER_ZERO = new BigDecimal("999999999999999999999.999999999", MathContext.DECIMAL128);

	protected BigDecimal m_bdData = new BigDecimal(INVALID_DATASTRING, MathContext.DECIMAL128);

	private static final long serialVersionUID = 6;

	public DBDecimal() {
		super(INVALID_DATASTRING);

		try {
			prepareData();

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		}
	}

	public DBDecimal(double p_double) {
		super(Double.toString(p_double));

		try {
			prepareData();

			m_strData = m_bdData.toPlainString();

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
		}
	}

	public DBDecimal(BigDecimal p_bdValue) {
		super(p_bdValue.toString());

		try {
			prepareData();

			m_strData = m_bdData.toPlainString();

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
		}
	}

	public DBDecimal(String p_string) {
		super(p_string.replace(",", ""));

		try {
			prepareData();

			m_strData = m_bdData.toPlainString();

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
		}
	}

	public DBDecimal(DBDecimal p_dbDecimal) {
		super(p_dbDecimal.getString_WithoutComma());

		try {
			prepareData();

			m_strData = m_bdData.toPlainString();

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
		}
	}

	public DBDecimal(GResultSet p_resultSet, String p_strColumnName) throws GException {
		try {
			DBDecimal dbdmResult = p_resultSet.getDecimal(p_strColumnName);

			if(dbdmResult.compareTo(INVALID_DBDecimal) != 0) {
				setDecimalData(dbdmResult);
			}
		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public void writeExternal(ObjectOutput p_out) throws IOException {
		try {
			super.writeExternal(p_out);

			p_out.writeObject(m_bdData);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			p_out.close();

			throw new IOException(exception);
		}
	}

	public void readExternal(ObjectInput p_in) throws IOException {
		try {
			super.readExternal(p_in);

			m_bdData = (BigDecimal)p_in.readObject();

		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			p_in.close();

			throw new IOException(exception);
		}
	}

	protected void setInvalidData() {
		m_strData = INVALID_DATASTRING;

		m_bdData = new BigDecimal(INVALID_DATASTRING, MathContext.DECIMAL128);
	}

	protected String getInvalidDataString() {
		return INVALID_DATASTRING;
	}

	public void setStringValue(String p_strString) {
		p_strString = p_strString.replace(",", "");

		super.setStringValue(p_strString);

		if(m_strData.length() > 0) {
			if(m_strData.compareTo(STRING_INVALID) == 0
				|| m_strData.toUpperCase().compareTo("INFINITY") == 0
				|| m_strData.toUpperCase().compareTo("NAN") == 0
				|| m_strData.toUpperCase().compareTo("NULL") == 0) {

				setInvalidData();

			} else {
				try {
					Double.parseDouble(m_strData);

					m_bdData = new BigDecimal(m_strData, MathContext.DECIMAL128);
					m_bdData = m_bdData.setScale(SizeAfterDecimal, BigDecimal.ROUND_HALF_UP);

					super.setStrData(m_bdData.toPlainString());

				} catch(NumberFormatException nfe) {
					setInvalidData();
				}
			}
		}
	}

	public void setDecimalData(DBDecimal p_dbDecimal) throws GException {
		m_bdData = new BigDecimal(p_dbDecimal.getString_WithoutComma());
		m_bdData = m_bdData.setScale(SizeAfterDecimal, BigDecimal.ROUND_HALF_UP);

		super.setStrData(m_bdData.toPlainString());
	}

	public DBDecimal add(DBDecimal p_dbDecimal) throws GException {
		try {
			if(p_dbDecimal.isInvalid()
				|| (getBigDecimal().compareTo(new BigDecimal(INVALID_DATASTRING, MathContext.DECIMAL128)) == 0)) {

				return new DBDecimal();
			}

			BigDecimal bigdecimal = this.getBigDecimal().add(p_dbDecimal.getBigDecimal());

			return new DBDecimal(bigdecimal.doubleValue());

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public DBDecimal add(int p_intValue) throws GException {
		try {
			BigDecimal bigdecimal = this.getBigDecimal().add(new BigDecimal(p_intValue));

			return new DBDecimal(bigdecimal.doubleValue());

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public DBDecimal negate() throws GException {
		try {
			BigDecimal bigdecimal = this.getBigDecimal();
			bigdecimal = bigdecimal.multiply(new BigDecimal(-1));

			DBDecimal dbdReturn = new DBDecimal(bigdecimal.doubleValue());

			return dbdReturn;

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public DBDecimal multiply(DBDecimal p_dbDecimal) throws GException {
		try {
			if(p_dbDecimal.isInvalid()
				|| (getBigDecimal().compareTo(new BigDecimal(INVALID_DATASTRING, MathContext.DECIMAL128)) == 0)) {

				return new DBDecimal();
			}

			BigDecimal bigdecimal = new BigDecimal(this.getBigDecimal().doubleValue());
			bigdecimal = bigdecimal.multiply(p_dbDecimal.getBigDecimal());

			DBDecimal dbdReturn = new DBDecimal(bigdecimal.doubleValue());

			return dbdReturn;

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public DBDecimal divide(DBDecimal p_dbDecimal) throws GException {
		return divide(p_dbDecimal, SizeAfterDecimal, RoundingMode.HALF_UP);
	}

	public DBDecimal divide(DBDecimal p_dbDecimal, int p_intScale, RoundingMode p_roundingmode) throws GException {
		try {
			if(p_dbDecimal.isInvalid()
				|| (getBigDecimal().compareTo(new BigDecimal(INVALID_DATASTRING, MathContext.DECIMAL128)) == 0)) {

				return new DBDecimal();
			}

			BigDecimal bigdecimal = new BigDecimal(this.getBigDecimal().doubleValue());
			bigdecimal = bigdecimal.divide(p_dbDecimal.getBigDecimal(), p_intScale, p_roundingmode);

			DBDecimal dbdReturn = new DBDecimal(bigdecimal.doubleValue());

			return dbdReturn;

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public DBDecimal getSquareRoot() throws GException {
		try {
			DBDecimal dbdcValue = new DBDecimal();

			if(isValid()) {
				dbdcValue = new DBDecimal(MathUtil.sqrt(this.getBigDecimal(), RoundingMode.HALF_UP));
			}

			return dbdcValue;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public DBDecimal abs() throws GException {
		try {
		  BigDecimal bdTemp = getBigDecimal();
			double dblTemp = Math.abs(bdTemp.doubleValue());

			return new DBDecimal(dblTemp);

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public BigDecimal getBigDecimal() throws GException {
		if(isInvalid()) {
			return new BigDecimal(0);
		}

		return m_bdData;
	}

	public DBInteger getDBInteger(RoundingMode p_roundingmode) throws GException {
		try {
			if(isInvalid()
				|| (getBigDecimal().compareTo(new BigDecimal(INVALID_DATASTRING, MathContext.DECIMAL128)) == 0)) {

				return new DBInteger();
			}

			BigDecimal bigdecimal = new BigDecimal(this.getBigDecimal().doubleValue());
			bigdecimal = bigdecimal.setScale(0, p_roundingmode);

			DBInteger dbintReturn = new DBInteger(bigdecimal.intValue());

			return dbintReturn;

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public String getStringSQL() {
		try {
			if(isInvalid()) {
				return "NULL";
			}
		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		}

		return getString_WithoutComma();
	}

	public String getString() {
		if(isInvalid()) {
			return STRING_INVALID;
		}

		return m_strData;
	}

	public String getString_WithoutComma() {
		return getString().replace(",", "");
	}

	public String getString_Scale(int p_scale) {
		return getString_Scale(p_scale, BigDecimal.ROUND_HALF_UP);
	}

	public String getString_Scale(int p_scale, int p_roundingmode) {
		if(isInvalid()
			|| p_scale < 0) {

			return STRING_INVALID;
		}

		String strFormat = "#,##0";

		if(p_scale > 0) {
			strFormat = strFormat + ".";

			for(int i=1; i <= p_scale; i++) {
				strFormat = strFormat + '0';
			}
		}

		BigDecimal bdData_New = new BigDecimal(m_bdData.doubleValue());
		DecimalFormat df = new DecimalFormat(strFormat);

		return df.format(bdData_New.setScale(p_scale, p_roundingmode));
	}

	public String getSortString() {
		return getSortString("!$0INT$!");
	}

	public String getSortString_WithoutTag() {
		return getSortString();
	}

	public String getSortString(String p_strTag) {
		String strSortString = p_strTag;

		try {
			if(isValid()) {
				String strDataToSortString = m_strData;
				this.getBigDecimal();

				if(m_bdData.compareTo(BigDecimal.ZERO) < 0) {
					BigDecimal dbNew = DATA_CAL_LESSER_ZERO.add(m_bdData);
					dbNew = dbNew.setScale(SizeAfterDecimal, BigDecimal.ROUND_HALF_UP);

					strDataToSortString = dbNew.toPlainString();
				}

				String strFill = "0";

				if(this.getBigDecimal().longValue() < 0) {
					strFill = "&";
				}

				StringTokenizer token = new StringTokenizer(strDataToSortString, ".");
				String strData_01 = token.nextToken();
				String strData_02 = token.nextToken();

				while(strData_01.length() < 22) {
					strData_01 = strFill + strData_01;
				}

				while(strData_02.length() < SizeAfterDecimal) {
					strData_02 = strData_02 + '0';
				}

				strSortString = p_strTag + strData_01 + '.' + strData_02;
			}
		} catch (Exception exception) {
			ExceptionHandler.display(exception);
		}

		return strSortString;
	}

	public String getCSVString() {
		if(isInvalid()) {
			return "";
		}

		return getString_WithoutComma();
	}

	public BigDecimal getValueReport() {
		if(isValid()) {
			return m_bdData;
		}

		return null;
	}

	public boolean isInvalid() {
		return (m_bdData.compareTo(new BigDecimal(INVALID_DATASTRING, MathContext.DECIMAL128)) == 0);
	}

	public boolean isValid() {
		return (m_bdData.compareTo(new BigDecimal(INVALID_DATASTRING, MathContext.DECIMAL128)) != 0);
	}

	public DBDecimal getSumValue(List<DataVectorRow> p_lsSelectedDataVectorRows, String p_strColumnName) throws GException {
		try {
		  DBDecimal dbdSumValue = new DBDecimal(0);

		  for(DataVectorRow dvrRow : p_lsSelectedDataVectorRows) {
			  DBDecimal dbdValue = dvrRow.getDataAtColumnName(DBDecimal.class, p_strColumnName);
			  dbdSumValue = dbdSumValue.add(dbdValue);
		  }

		  return dbdSumValue;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public DBDecimal getSumValue(GResultSet p_rst, String p_strColumnName) throws GException {
		try {
			p_rst.beforeFirst();

			DBDecimal dbdSumValue = new DBDecimal(0);

			while(p_rst.next()) {
				DBDecimal dbdValue = new DBDecimal(p_rst, p_strColumnName);
				dbdSumValue = dbdSumValue.add(dbdValue);
			}

			p_rst.beforeFirst();

			return dbdSumValue;

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public DBDecimal getSumValue(DataVectorTable p_dvtTable, String p_strColumnName) throws GException {
		try {
			p_dvtTable.beforeFirst();

			DBDecimal dbdSumValue = new DBDecimal(0);

			while(p_dvtTable.next()) {
				DataVectorRow dvrRow = p_dvtTable.getDataRow();
				DBDecimal dbdValue = dvrRow.getDataAtColumnName(DBDecimal.class, p_strColumnName);
				dbdSumValue = dbdSumValue.add(dbdValue);
			}

			p_dvtTable.beforeFirst();

			return dbdSumValue;

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	private void prepareData() throws GException {
		try {
			m_strData = m_strData.replace(",", "");

			try {
				Double.valueOf(m_strData);

			} catch (Exception exception) {
				resetData();

				return;
			}

			m_bdData = new BigDecimal(m_strData);
			m_bdData = m_bdData.setScale(SizeAfterDecimal, BigDecimal.ROUND_HALF_UP);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			resetData();

			throw new GException(exception);
		}
	}

	protected void resetData() {
		super.resetData();

		m_bdData = new BigDecimal(INVALID_DATASTRING, MathContext.DECIMAL128);
		m_bdData = m_bdData.setScale(SizeAfterDecimal, BigDecimal.ROUND_HALF_UP);
	}

	public String toString() {
		return getString();
	}

	public int compareTo(I_DBObject p_dbObject) {
		try {
			if(p_dbObject instanceof DBDecimal) {
				return this.getSortString().compareTo(p_dbObject.getSortString());

			} else {
				throw new GException("Attempting compare " + getClass().getName() + " to " + p_dbObject.getClass().getName());
			}
		} catch(Exception exception) {
			ExceptionHandler.display(exception);
		}

		return -1;
	}

	public DBDecimal clone() {
		try {
			return new DBDecimal(m_bdData.toString());

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
		}

		return new DBDecimal();
	}
}
