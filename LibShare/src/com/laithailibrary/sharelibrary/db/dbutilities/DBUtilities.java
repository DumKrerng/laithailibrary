package com.laithailibrary.sharelibrary.db.dbutilities;

import com.laithailibrary.logger.*;
import com.laithailibrary.sharelibrary.collection.GMap;
import com.laithailibrary.sharelibrary.session.*;
import com.laithailibrary.sharelibrary.db.dbobject.DBString;
import com.laithailibrary.sharelibrary.thread.*;
import exc.*;
import exc.ExceptionHandler;
import msg.MsgDialog;
import pp.GProperties;
import pp.ProgramExit;
import com.laithailibrary.sharelibrary.support.GUtilities;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: dumkrerng
 * Date: 6/4/11
 * Time: 12:24 AM
 * To change this template use File | Settings | File Templates.
 */
public class DBUtilities {

//	private static final java.lang.String PREFIX_SQLite = "jdbc:sqlite:";
//	private static JDBC s_jdbcSQLite = null;
//	private static String s_strDatabasePath = null;
//
//
//	private static BaseConnection s_baseconnection = null;
	public static DBType s_dbType = null;

	private static Map<ISessionID, BaseConnection> m_mapBaseConnection_SessionID = new GMap<>();

	private DBUtilities() {}

	public DBUtilities(SessionID p_sessionid) throws GException {
		try {
			createConnectionFirst(p_sessionid);

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public DBUtilities(DBType p_dbType, SessionID p_sessionid) throws GException {
		try {
			s_dbType = p_dbType;

			createConnectionFirst(p_sessionid);

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	private static void createConnectionFirst(ISessionID p_sessionid) throws GException {
		try {
			String strDBType = GProperties.getDBType();

			if(strDBType == null
				|| strDBType.length() <= 0) {

				GProperties.setPPProperty(GProperties.KEY_DBType, "");
				GProperties.WritePropertiesFile();

				new MsgDialog("Error!", "Invalid DBType!");
				System.exit(ProgramExit.InvalidDBType);
			}

			s_dbType = DBType.getDBType(strDBType);

			String strDBConnectionString = GProperties.getDBConnectionString();

			if(strDBConnectionString == null
				|| strDBConnectionString.length() <= 0) {

				if(s_dbType == DBType.SQLite) {
					String strDBPath_SQLite = GProperties.getDBPath();

					if(strDBPath_SQLite.length() <= 0) {
						throw new GException("Invalid DBPath!");

					} else {
						GProperties.setPPProperty(GProperties.KEY_DBConnectionString, strDBPath_SQLite);
						GProperties.WritePropertiesFile();
					}
				} else {

					new MsgDialog("Error!", "Invalid DBConnectionString!");
					System.exit(ProgramExit.InvalidDBType);
				}
			}

			BaseConnection baseconnection = new BaseConnection(s_dbType);
			m_mapBaseConnection_SessionID.put(p_sessionid, baseconnection);

			checkDatabase(p_sessionid);

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public static void createConnection() throws GException {
		try {
			ISessionID sessionid = ClientSessionID.getSessionID(new GThreadGroup(Thread.currentThread()));
			createConnection(sessionid);

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public static void createConnection(ISessionID p_sessionid) throws GException {
		try {
			BaseConnection baseconnection = new BaseConnection(s_dbType);
			m_mapBaseConnection_SessionID.put(p_sessionid, baseconnection);

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public static BaseConnection getNewConnection() throws GException {
		try {
			return new BaseConnection(s_dbType);

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	private static void checkDatabase(ISessionID p_sessionid) throws GException {
		try {
		  StringBuilder builder = new StringBuilder();
			builder.append("SELECT ProName, ProVer\n");
			builder.append("FROM ProInfo\n");

			GStatement stm = new GStatement(DBUtilities.getConnection(p_sessionid));
			GResultSet rst = stm.executeQuery(builder.toString());

			if(rst.next()) {
				DBString dbstrProName = new DBString(rst, "ProName");
				DBString dbstrProVer = new DBString(rst, "ProVer");

				if(GUtilities.getProName().length() <= 0) {
					GUtilities.setProName(dbstrProName.getString());
					GUtilities.setProVersion(dbstrProVer.getString());
				}
			}

			m_mapBaseConnection_SessionID.get(p_sessionid).close();
			m_mapBaseConnection_SessionID.remove(p_sessionid);

		} catch (Exception exception) {
			if(ExcDialog.getDisplay() == ExcDialog.Display.Yes) {
				new ExcDialog("Database not compatible Application!!!");
			}

			GLog.severe("Database not compatible Application!!!");

			GProperties.clearProperty();
		  ExceptionHandler.display(exception);

			try {
				m_mapBaseConnection_SessionID.get(p_sessionid).close();
				m_mapBaseConnection_SessionID.remove(p_sessionid);

			} catch (Exception excep) {
			  excep.printStackTrace();
			  throw new GException(excep);
			}

			GProperties.clearProperty();

			if(exception.getMessage().contains("no such table: ProInfo")) {
				if(ExcDialog.getDisplay() == ExcDialog.Display.Yes) {
					new ExcDialog("Database not compatible Application!!!");
				}

				GLog.severe("Database not compatible Application!!!");

				System.exit(ProgramExit.DatabaseNotCompatibleApplication);

			} else {
				if(ExcDialog.getDisplay() == ExcDialog.Display.Yes) {
					new ExcDialog(exception.getMessage());
				}

				GLog.severe(exception.getMessage());

				System.exit(ProgramExit.Other);
			}
		}
	}

	public static BaseConnection getConnection() throws GException {
		return getConnection(null);
	}

	public static BaseConnection getConnection(ISessionID p_sessionid) throws GException {
		try {
			if(p_sessionid == null) {
				p_sessionid = ClientSessionID.getSessionID(new GThreadGroup(Thread.currentThread()));
			}

			BaseConnection baseconnection = m_mapBaseConnection_SessionID.get(p_sessionid);

			if(baseconnection == null) {
				createConnection(p_sessionid);

				return getConnection(p_sessionid);

			} else if(baseconnection.isClosed()) {
				createConnection(p_sessionid);

				return getConnection(p_sessionid);

			} else {
				return baseconnection;
			}
		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public static void closeConnection(ISessionID p_sessionid) throws GException {
		try {
			BaseConnection baseconnection = m_mapBaseConnection_SessionID.get(p_sessionid);

			if(baseconnection != null) {
				if(!baseconnection.isClosed()) {
					if(!baseconnection.isTransactionStarted()) {
						baseconnection.close();
					}
				}
			}
		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public static void setDBType(DBType p_dbtype) {
		s_dbType = p_dbtype;
	}

	public static DBType getDBType() {
		return s_dbType;
	}
}
