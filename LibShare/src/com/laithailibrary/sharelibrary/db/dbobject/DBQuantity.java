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
public class DBQuantity extends DBDecimal {

	public static final DBQuantity Zero = new DBQuantity(0.00);

	private static final long serialVersionUID = -3;

	public DBQuantity() {}

	public DBQuantity(double p_double) {
		super(p_double);
	}

	public DBQuantity(DBDecimal p_dbDecimal) {
		super(p_dbDecimal);
	}

	public DBQuantity(String p_string) {
		super(p_string);
	}

	public DBQuantity(GResultSet p_resultSet, String p_strColumnName) throws GException {
		super(p_resultSet, p_strColumnName);
	}

	public DBQuantity add(DBQuantity p_dbQuantity) throws GException {
		try {
			DBDecimal dbDecimal = super.add(p_dbQuantity);

			if(dbDecimal.isInvalid()
				|| p_dbQuantity.isInvalid()) {

				return new DBQuantity();
			}

			return new DBQuantity(dbDecimal);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public DBQuantity add(int p_intValue) throws GException {
		try {
			DBDecimal dbDecimal = super.add(p_intValue);

			return new DBQuantity(dbDecimal);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public DBQuantity negate() throws GException {
		return new DBQuantity(super.negate());
	}

	public DBQuantity multiply(DBQuantity p_dbQuantity) throws GException {
		return new DBQuantity(super.multiply(p_dbQuantity));
	}

	public DBQuantity divide(DBQuantity p_dbQuantity) throws GException {
		return divide(p_dbQuantity, SizeAfterDecimal, RoundingMode.HALF_UP);
	}

	public DBQuantity divide(DBQuantity p_dbQuantity, int p_intScale, RoundingMode p_roundingmode) throws GException {
		return new DBQuantity(super.divide(p_dbQuantity, p_intScale ,p_roundingmode));
	}

	public DBQuantity abs() throws GException {
		return new DBQuantity(super.abs());
	}

	public DBQuantity getSumValue(List<DataVectorRow> p_lsSelectedDataVectorRows, String p_strColumnName) throws GException {
		try {
			DBQuantity dbqSumValue = new DBQuantity(0);

			for(DataVectorRow dvrRow : p_lsSelectedDataVectorRows) {
				DBQuantity dbqValue = dvrRow.getDataAtColumnName(DBQuantity.class, p_strColumnName);
				dbqSumValue = dbqSumValue.add(dbqValue);
			}

			return dbqSumValue;

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public DBQuantity getSumValue(GResultSet p_rst, String p_strColumnName) throws GException {
		try {
			p_rst.beforeFirst();

			DBQuantity dbqSumValue = new DBQuantity(0);

			while(p_rst.next()) {
				DBQuantity dbqValue = new DBQuantity(p_rst, p_strColumnName);
				dbqSumValue = dbqSumValue.add(dbqValue);
			}

			p_rst.beforeFirst();

			return dbqSumValue;

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public DBQuantity getSumValue(DataVectorTable p_dvtTable, String p_strColumnName) throws GException {
		try {
			p_dvtTable.beforeFirst();

			DBQuantity dbqSumValue = new DBQuantity(0);

			while(p_dvtTable.next()) {
				DataVectorRow dvrRow = p_dvtTable.getDataRow();
				DBQuantity dbqValue = dvrRow.getDataAtColumnName(DBQuantity.class, p_strColumnName);
				dbqSumValue = dbqSumValue.add(dbqValue);
			}

			p_dvtTable.beforeFirst();

			return dbqSumValue;

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public String getString() {
		if(isInvalid()) {
			return STRING_INVALID;
		}

		return getString_Scale(6);
	}

	public String getCSVString() {
		if(isInvalid()) {
			return "";
		}

		return getString_Scale(6).replace(",", "");
	}

	public int compareTo(I_DBObject p_dbObject) {
		try {
			if(p_dbObject instanceof DBQuantity) {
				return this.getSortString().compareTo(p_dbObject.getSortString());

			} else {
				throw new GException("Attempting compare " + getClass().getName() + " to " + p_dbObject.getClass().getName());
			}
		} catch(Exception exception) {
			ExceptionHandler.display(exception);
		}

		return -1;
	}

	public DBQuantity clone() {
		try {
			return new DBQuantity(m_bdData.toString());

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
		}

		return new DBQuantity();
	}
}
