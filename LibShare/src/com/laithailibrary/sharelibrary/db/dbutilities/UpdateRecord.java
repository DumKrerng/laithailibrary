package com.laithailibrary.sharelibrary.db.dbutilities;

import com.laithailibrary.sharelibrary.db.dbobject.DBDateTime;
import com.laithailibrary.sharelibrary.db.dbobject.DBString;
import com.laithailibrary.sharelibrary.field.FieldPrimaryKey;
import com.laithailibrary.sharelibrary.field.TableName;
import com.laithailibrary.sharelibrary.support.GUtilities;
import com.laithailibrary.sharelibrary.support.SingleClient;
import exc.*;
import exc.ExceptionHandler;

import java.sql.ResultSet;

/**
 * Created with IntelliJ IDEA.
 * User: dumkrerng
 * Date: 11/10/12
 * Time: 9:07 AM
 * To change this template use File | Settings | File Templates.
 */
public class UpdateRecord {

	FieldPrimaryKey m_fieldprimarykey;
	DBString m_dbstrValue;

	DBDateTime m_dbdtRecordUpdatedOnDB = null;
	DBDateTime m_dbdtRecordUpdatedOnSession = null;

	public UpdateRecord(FieldPrimaryKey p_fieldprimarykey, DBString p_dbstrValue) throws GException {
		m_fieldprimarykey = p_fieldprimarykey;
		m_dbstrValue = p_dbstrValue;
	}

	public FieldPrimaryKey getFieldPrimaryKey() {
		return m_fieldprimarykey;
	}

	public DBString getPrimaryKeyValue() {
		return m_dbstrValue;
	}

	public void verifyRecordUpdated(BaseConnection p_baseconnection) throws GException {
		try {
			if(GUtilities.getSingleClient() == SingleClient.Yes) {
				return;
			}

			if(m_dbstrValue.isInvalid()) {
				throw new GException("Invalid PrimaryKeyValue!");
			}

			TableName tablename = m_fieldprimarykey.getTableName();
			String strPrimaryKeyName = m_fieldprimarykey.getPrimaryKeyName();

			StringBuilder builder = new StringBuilder();
			builder.append("SELECT RecordUpdated\n");
			builder.append("FROM ").append(tablename).append('\n');
			builder.append("WHERE ").append(strPrimaryKeyName).append(" = ").append(m_dbstrValue.getStringSQL());

			ResultSet rst_Temp = p_baseconnection.createStatement().executeQuery(builder.toString());
			GResultSet rst = new GResultSet(rst_Temp);

			rst_Temp.close();

			if(rst.getRowCount() != 1) {
				throw new GException("Invalid ID(" + m_dbstrValue + ") on Table(" + tablename + ")!");

			} else {
				m_dbdtRecordUpdatedOnSession = new DBDateTime();

				if(rst.next()) {
					m_dbdtRecordUpdatedOnSession = new DBDateTime(rst, "RecordUpdated");
				}
			}

			GStatement stmt_New = new GStatement(DBUtilities.getNewConnection());
			rst = stmt_New.executeQuery(builder);

			if(rst.getRowCount() > 1) {
				throw new GException("Invalid ID(" + m_dbstrValue + ") on Table(" + tablename + ")!");

			} else {
				m_dbdtRecordUpdatedOnDB = new DBDateTime();

				if(rst.next()) {
					m_dbdtRecordUpdatedOnDB = new DBDateTime(rst, "RecordUpdated");
				}
			}

			if(m_dbdtRecordUpdatedOnDB == null
				|| m_dbdtRecordUpdatedOnSession == null) {

				throw new GException("Invalid RecordUpdated!");
			}

			if(m_dbdtRecordUpdatedOnDB.isValid()) {
				if(m_dbdtRecordUpdatedOnDB.compareTo(m_dbdtRecordUpdatedOnSession) != 0) {
					throw new GException("Record ID(" + m_dbstrValue + ") is not update on Table(" + tablename + ")!");
				}
			}
		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}
}
