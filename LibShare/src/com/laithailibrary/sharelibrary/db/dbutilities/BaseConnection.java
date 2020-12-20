package com.laithailibrary.sharelibrary.db.dbutilities;

import java.sql.*;
import com.laithailibrary.sharelibrary.db.dbconn.*;
import exc.*;
import msg.*;
import pp.*;

/**
 * Created with IntelliJ IDEA.
 * User: dumkrerng
 * Date: 5/13/12
 * Time: 12:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class BaseConnection implements IConnection {

	private static String s_strDBConnectionString = null;

	private static DBType s_dbType = DBType.SQLite;

	private Connection m_connection;
	private boolean m_bolTransactionStart = false;

	public BaseConnection(DBType p_dbType) throws GException {
		try {
			s_dbType = p_dbType;

			setDBConnectionString();

			IConnectionSQL driver;

			if(s_dbType == DBType.SQLite) {
				driver = new ConnectionSQLite();

			} else if(s_dbType == DBType.Postgresql) {
				driver = new ConnectionPostgresql();

			} else if(s_dbType == DBType.MySQL) {
				driver = new ConnectionMySQL();

			} else {
				throw new GException("Not support " + s_dbType.name() + "!");
			}

			m_connection = driver.createConnection(s_strDBConnectionString);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	private static void setDBConnectionString() throws GException {
		try {
			s_strDBConnectionString = GProperties.getDBConnectionString();

			if(s_dbType == DBType.SQLite) {
				if(!s_strDBConnectionString.endsWith(".db")) {
					new MsgDialog("Can not connect to database!");

					System.exit(ProgramExit.CanNotConnectToDatabase);
				}
			}
		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public Statement createStatement() throws GException {
		try {
			return m_connection.createStatement();

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public Statement createStatementUpdateData() throws GException {
		return createStatementInsertOrUpdateData();
	}

	public Statement createStatementInsertData() throws GException {
		return createStatementInsertOrUpdateData();
	}

	public Statement createStatementCreateTable() throws GException {
		return createStatementInsertOrUpdateData();
	}

	private Statement createStatementInsertOrUpdateData() throws GException {
		try {
			if(!m_bolTransactionStart
				|| isClosed()) {

				throw new GException("Transaction not ready!");
			}

			if(m_bolTransactionStart) {
				m_connection.setAutoCommit(false);
				m_bolTransactionStart = true;
			}

			return m_connection.createStatement();

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public void transactionBegin() throws GException {
		try {
			if(m_bolTransactionStart) {
				throw new GException("Transaction can not started!\nTransaction is ready.");
			}

			m_connection.setAutoCommit(false);
			m_bolTransactionStart = true;

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public void commit() throws GException {
		try {
			if(!m_bolTransactionStart
				|| isClosed()) {

				throw new GException("Transaction can not commit!\nTransaction is closed.");
			}

			m_connection.commit();
			m_bolTransactionStart = false;

			close();

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public void rollback() throws GException {
		try {
			if(m_bolTransactionStart) {
				m_connection.rollback();
				m_bolTransactionStart = false;
			}

			close();

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public void setReadOnly(boolean p_boolean) throws GException {
		try {
			m_connection.setReadOnly(p_boolean);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public boolean isClosed() throws GException {
		try {
			return m_connection.isClosed();

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public void close() throws GException {
		try {
			if(!m_bolTransactionStart) {
				m_connection.close();
			}
		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public boolean isTransactionStarted() throws GException {
		return m_bolTransactionStart;
	}

	public DBType getDBType() {
		return s_dbType;
	}
}
