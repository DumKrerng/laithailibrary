package com.laithailibrary.sharelibrary.db.dbutilities;

import com.laithailibrary.logger.*;
import com.laithailibrary.sharelibrary.db.dbobject.DBDateTime;
import com.laithailibrary.sharelibrary.field.TableName;
import exc.ExceptionHandler;
import exc.GException;

import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by IntelliJ IDEA.
 * User: dumkrerng
 * Date: 3/1/12
 * Time: 10:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class GStatement {

	private BaseConnection m_baseconnection;

	private GStatement() {}

	public GStatement(BaseConnection p_baseconnection) throws GException {
		m_baseconnection = p_baseconnection;
	}

	public GResultSet executeQuery(StringBuilder p_builder) throws GException {
		return executeQuery(p_builder.toString());
	}

	public GResultSet executeQuery(String p_strSQL) throws GException {
		try {
//			m_baseconnection.setReadOnly(true);

			Statement stmt = m_baseconnection.createStatement();
			ResultSet rst = stmt.executeQuery(p_strSQL);

			GResultSet resultset = new GResultSet(rst);

			rst.close();
			stmt.close();

			return resultset;

		} catch (Exception exception) {
			GLog.severe("SQL Statement: " + p_strSQL);

		  ExceptionHandler.display(exception);
		  throw new GException(exception);

		} finally {
			m_baseconnection.close();
		}
	}

	public void executeUpdate(StringBuilder p_builder, UpdateRecord p_updaterecord) throws GException {
		executeUpdate(p_builder.toString(), p_updaterecord);
	}

	public void executeUpdate(String p_strSQL, UpdateRecord p_updaterecord) throws GException {
		try {
			Statement stmt = m_baseconnection.createStatementUpdateData();

			p_updaterecord.verifyRecordUpdated(m_baseconnection);
			stmt.executeUpdate(p_strSQL);
			updateRecord(stmt, p_updaterecord);

			stmt.close();

		} catch (Exception exception) {
			GLog.severe("SQL Statement: " + p_strSQL);

		  ExceptionHandler.display(exception);
		  throw new GException(exception);

		} finally {
			m_baseconnection.close();
		}
	}

	public void executeUpdate(String p_strSQL) throws GException {
		try {
			Statement stmt = m_baseconnection.createStatementUpdateData();

			stmt.executeUpdate(p_strSQL);

			stmt.close();

		} catch (Exception exception) {
			GLog.severe("SQL Statement: " + p_strSQL);

		  ExceptionHandler.display(exception);
		  throw new GException(exception);

		} finally {
			m_baseconnection.close();
		}
	}

	public void executeInsert(String p_strSQL) throws GException {
		try {
			Statement stmt = m_baseconnection.createStatementInsertData();
			stmt.executeUpdate(p_strSQL);
//			ResultSet rst = stmt.getGeneratedKeys();
//
//			int newId = -1;
//
//			if (rst.next()) {
//				newId = rst.getInt(1);
//			}
//
//			rst.close();
			stmt.close();

//			return String.valueOf(newId);

		} catch (Exception exception) {
			GLog.severe("SQL Statement: " + p_strSQL);

		  ExceptionHandler.display(exception);
		  throw new GException(exception);

		} finally {
			m_baseconnection.close();
		}
	}

	public void executeCreateTable(String p_strSQL) throws GException {
		try {
			Statement stmt = m_baseconnection.createStatementCreateTable();
			stmt.execute(p_strSQL);

			stmt.close();

		} catch (Exception exception) {
			GLog.severe("SQL Statement: " + p_strSQL);

		  ExceptionHandler.display(exception);
		  throw new GException(exception);

		} finally {
			m_baseconnection.close();
		}
	}

	private void updateRecord(Statement p_stmt, UpdateRecord p_updaterecord) throws GException {
		try {
			TableName tablename =  p_updaterecord.getFieldPrimaryKey().getTableName();
			String strPrimaryKeyName = p_updaterecord.getFieldPrimaryKey().getPrimaryKeyName();

			StringBuilder builder = new StringBuilder();
			builder.append("UPDATE ").append(tablename).append('\n');
			builder.append("SET RecordUpdated = ").append(DBDateTime.getCurrentDateTime().getStringSQL()).append('\n');
			builder.append("WHERE ").append(strPrimaryKeyName).append(" = ").append(p_updaterecord.getPrimaryKeyValue().getStringSQL());

			p_stmt.executeUpdate(builder.toString());

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}
}
