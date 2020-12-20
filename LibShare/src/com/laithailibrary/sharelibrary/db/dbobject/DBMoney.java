package com.laithailibrary.sharelibrary.db.dbobject;

import com.laithailibrary.sharelibrary.datatableview.*;
import com.laithailibrary.sharelibrary.db.dbutilities.*;
import exc.*;
import java.math.*;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: dumkrerng
 * Date: 5/12/12
 * Time: 3:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class DBMoney extends DBDecimal {

	public static final DBMoney Zero = new DBMoney(0.00);

	private static final long serialVersionUID = 4;

	public DBMoney() {}

	public DBMoney(int p_integer) {
		super(p_integer);
	}

	public DBMoney(double p_double) {
		super(p_double);
	}

	public DBMoney(String p_string) {
		super(p_string);
	}

	public DBMoney(DBDecimal p_dbDecimal) {
		super(p_dbDecimal);
	}

	public DBMoney(GResultSet p_resultSet, String p_strColumnName) throws GException {
		try {
			DBDecimal dbdmResult = p_resultSet.getDecimal(p_strColumnName);

			if(dbdmResult.compareTo(INVALID_DBDecimal) != 0) {
				super.setDecimalData(dbdmResult);
			}
		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public DBMoney(DataVectorTable p_dtvView, String p_strColumnName) throws GException {
		try {
			DataVectorRow dvrRow = p_dtvView.getDataRow();
			DBMoney dbmValue = (DBMoney)dvrRow.getDataAtColumnName(p_strColumnName);

			if(dbmValue.isValid()) {
				if(dbmValue.compareTo(INVALID_DBDecimal) != 0) {
					super.setDecimalData(dbmValue);
				}
			}
		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public DBMoney(DataVectorRow p_dvrRow, String p_strColumnName) throws GException {
		try {
			DBMoney dbmValue = (DBMoney)p_dvrRow.getDataAtColumnName(p_strColumnName);

			if(dbmValue.compareTo(INVALID_DBDecimal) != 0) {
				super.setDecimalData(dbmValue);
			}
		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public DBMoney add(DBMoney p_dbMoney) throws GException {
		try {
			DBDecimal dbDecimal = super.add(p_dbMoney);

			if(dbDecimal.isInvalid()
				|| p_dbMoney.isInvalid()) {

				return new DBMoney();
			}

			return new DBMoney(dbDecimal);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public DBMoney add(int p_intValue) throws GException {
		try {
			DBDecimal dbDecimal = super.add(p_intValue);

			return new DBMoney(dbDecimal);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public DBMoney negate() throws GException {
		return new DBMoney(super.negate());
	}

	public DBMoney multiply(DBMoney p_dbMoney) throws GException {
		return new DBMoney(super.multiply(p_dbMoney));
	}

	public DBMoney divide(DBMoney p_dbMoney) throws GException {
		return divide(p_dbMoney, SizeAfterDecimal, RoundingMode.HALF_UP);
	}

	public DBMoney divide(DBMoney p_dbMoney, int p_intScale, RoundingMode p_roundingmode) throws GException {
		return new DBMoney(super.divide(p_dbMoney, p_intScale ,p_roundingmode));
	}

	public DBMoney abs() throws GException {
		return new DBMoney(super.abs());
	}

	public DBMoney getSumValue(List<DataVectorRow> p_lsSelectedDataVectorRows, String p_strColumnName) throws GException {
		try {
			DBMoney dbmSumValue = new DBMoney(0);

			for(DataVectorRow dvrRow : p_lsSelectedDataVectorRows) {
				DBMoney dbmValue = dvrRow.getDataAtColumnName(DBMoney.class, p_strColumnName);
				dbmSumValue = dbmSumValue.add(dbmValue);
			}

			return dbmSumValue;

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public DBMoney getSumValue(GResultSet p_rst, String p_strColumnName) throws GException {
		try {
			p_rst.beforeFirst();

			DBMoney dbmSumValue = new DBMoney(0);

			while(p_rst.next()) {
				DBMoney dbmValue = new DBMoney(p_rst, p_strColumnName);
				dbmSumValue = dbmSumValue.add(dbmValue);
			}

			p_rst.beforeFirst();

			return dbmSumValue;

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public DBMoney getSumValue(DataVectorTable p_dvtTable, String p_strColumnName) throws GException {
		try {
			p_dvtTable.beforeFirst();

			DBMoney dbmSumValue = new DBMoney(0);

			while(p_dvtTable.next()) {
				DataVectorRow dvrRow = p_dvtTable.getDataRow();
				DBMoney dbmValue = dvrRow.getDataAtColumnName(DBMoney.class, p_strColumnName);
				dbmSumValue = dbmSumValue.add(dbmValue);
			}

			p_dvtTable.beforeFirst();

			return dbmSumValue;

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public String getString() {
		if(isInvalid()) {
			return STRING_INVALID;
		}

		return getString_Scale(2);
	}

	public String getCSVString() {
		if(isInvalid()) {
			return "";
		}

		return getString_Scale(2).replace(",", "");
	}

	public int compareTo(I_DBObject p_dbObject) {
		try {
			if(p_dbObject instanceof DBDecimal
				|| p_dbObject instanceof DBMoney) {

				return this.getSortString().compareTo(p_dbObject.getSortString());

			} else {
				throw new GException("Attempting compare " + getClass().getName() + " to " + p_dbObject.getClass().getName());
			}
		} catch(Exception exception) {
			ExceptionHandler.display(exception);
		}

		return -1;
	}

	public DBMoney clone() {
		try {
			return new DBMoney(m_bdData.toString());

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
		}

		return new DBMoney();
	}
}
