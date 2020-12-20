package com.laithailibrary.sharelibrary.bean;

import java.lang.reflect.*;
import java.util.*;
import com.laithailibrary.logger.*;
import com.laithailibrary.sharelibrary.collection.*;
import com.laithailibrary.sharelibrary.datatableview.*;
import com.laithailibrary.sharelibrary.db.dbobject.*;
import com.laithailibrary.sharelibrary.db.dbutilities.*;
import com.laithailibrary.sharelibrary.interfaceclass.*;
import com.laithailibrary.sharelibrary.servicecall.invocation.*;
import com.laithailibrary.sharelibrary.session.*;
import com.laithailibrary.sharelibrary.sqlstatement.*;
import com.laithailibrary.sharelibrary.sqlstatement.support.*;
import com.laithailibrary.sharelibrary.support.*;
import exc.*;
import msg.*;
import org.json.*;
import pp.*;

/**
 * Created with IntelliJ IDEA.
 * User: dumkrerng
 * Date: 10/31/12
 * Time: 12:24 AM
 * To change this template use File | Settings | File Templates.
 */
public class DataTable<T extends DBDataTable> {

	private Class<T> m_clsDBDataTable = null;
	private IDataTableQuery<T> m_datatablequery = null;
	private ISessionID m_sessionid = null;

	private boolean m_bolIsMock = false;

	private static final long serialVersionUID = 21;

	public DataTable() {}

	public DataTable(ISessionID p_sessionid, Class<T> p_clsDBDataTable) {
		try {
			m_sessionid = p_sessionid;
			m_clsDBDataTable = p_clsDBDataTable;

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
		}
	}

	public void setSessionID(ISessionID p_sessionid) {
		m_sessionid = p_sessionid;
	}

	public void setDBDataTable(Class<T> p_clsDBDataTable) {
		m_clsDBDataTable = p_clsDBDataTable;
	}

	public <DBO extends DBObject> T getDBDataTable(DBO p_PrimaryKeyID) throws GException {
		try {
			T dbdatatable = getDataTableQuery().getDBDataTable(p_PrimaryKeyID);

			return dbdatatable;

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public List<T> getDBDataTables(SQLStatement p_statement) throws GException {
		try {
			List<T> lsdbdatatables = getDataTableQuery().getDBDataTables(p_statement);

			return lsdbdatatables;

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public DataVectorTable getDataVectorTable(SQLStatement p_statement) throws GException {
		return getDataVectorTable(p_statement, new GList<>());
	}

	public DataVectorTable getDataVectorTable(SQLStatement p_statement, List<String> p_lsSortKeys) throws GException {
		return getDataVectorTable(p_statement, p_lsSortKeys, Sorting.Ascending);
	}

	public DataVectorTable getDataVectorTable(SQLStatement p_statement, List<String> p_lsSortKeys, Sorting p_sorting) throws GException {
		return getDataVectorTable(p_statement, p_lsSortKeys, -1, p_sorting);
	}

	public DataVectorTable getDataVectorTable(SQLStatement p_statement, List<String> p_lsSortKeys, int p_intRowLimit, Sorting p_sorting) throws GException {
		try {
			DataVectorTable dvtReturn = getDataTableQuery().getDataVectorTable(p_statement, p_lsSortKeys, p_intRowLimit, p_sorting);

			return dvtReturn;

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public List<DBString> insertDatas(List<T> p_lsDataTables, TransactionBegin p_transactionBegin) throws GException {
		try {
			if(p_lsDataTables.size() <= 0) {
				throw new GException("Invalid DataTable!");
			}

			List<DBString> lsNewIDs = getDataTableQuery().insertDatas(p_lsDataTables, p_transactionBegin);

			return lsNewIDs;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public DBString insertData(T p_dbDataTable, TransactionBegin p_transactionBegin) throws GException {
		return insertData(p_dbDataTable, new ExtraBean(), p_transactionBegin);
	}

	public DBString insertData(T p_dbDataTable, ExtraBean p_extrabean, TransactionBegin p_transactionBegin) throws GException {
		try {
			DBString dbstrResult = getDataTableQuery().insertData(p_dbDataTable, p_extrabean, p_transactionBegin);

			return dbstrResult;

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public DBString insertData(T p_dbDataTable, TransactionBegin p_transactionBegin, DoVerifyData p_doverifydata, DoRunBefore p_dorunbefore,
		DoRunAfter p_dorunafter) throws GException {

		return insertData(p_dbDataTable, new ExtraBean(), p_transactionBegin, p_doverifydata, p_dorunbefore, p_dorunafter);
	}

	public DBString insertData(T p_dbDataTable, ExtraBean p_extrabean, TransactionBegin p_transactionBegin, DoVerifyData p_doverifydata,
		DoRunBefore p_dorunbefore, DoRunAfter p_dorunafter) throws GException {

		try {
			DBString dbstrResult = getDataTableQuery().insertData(p_dbDataTable, p_extrabean, p_transactionBegin, p_doverifydata, p_dorunbefore,
				p_dorunafter);

			return dbstrResult;

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public DBString updateData(T p_dbDataTable_Edited, TransactionBegin p_transactionBegin) throws GException {
		return updateData(p_dbDataTable_Edited, new ExtraBean(), p_transactionBegin);
	}

	public DBString updateData(T p_dbDataTable_Edited, ExtraBean p_extrabean, TransactionBegin p_transactionBegin) throws GException {
		try {
			DBString dbstrResult = getDataTableQuery().updateData(p_dbDataTable_Edited, p_extrabean, p_transactionBegin);

			return dbstrResult;

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public DBString updateData(T p_dbDataTable_Edited, TransactionBegin p_transactionBegin, DoVerifyData p_doverifydata, DoRunBefore p_dorunbefore,
		DoRunAfter p_dorunafter) throws GException {

		return updateData(p_dbDataTable_Edited, new ExtraBean(), p_transactionBegin, p_doverifydata, p_dorunbefore, p_dorunafter);
	}

	public DBString updateData(T p_dbDataTable_Edited, ExtraBean p_extrabean, TransactionBegin p_transactionBegin, DoVerifyData p_doverifydata,
		DoRunBefore p_dorunbefore, DoRunAfter p_dorunafter) throws GException {

		try {
			DBString dbstrResult = getDataTableQuery().updateData(p_dbDataTable_Edited, p_extrabean, p_transactionBegin, p_doverifydata,
				p_dorunbefore, p_dorunafter);

			return dbstrResult;

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public void deleteAllData(SQLStatement p_statement, TransactionBegin p_transactionBegin) throws GException {
		try {
			getDataTableQuery().deleteAllData(p_statement, p_transactionBegin);

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public void deleteData(List<T> p_lsDBDataTables, TransactionBegin p_transactionBegin) throws GException {
		try {
			getDataTableQuery().deleteDatas(p_lsDBDataTables, p_transactionBegin);

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public void deleteData(T p_dbDataTable, TransactionBegin p_transactionBegin) throws GException {
		deleteData(p_dbDataTable, new ExtraBean(), p_transactionBegin);
	}

	public void deleteData(T p_dbDataTable, ExtraBean p_extrabean, TransactionBegin p_transactionBegin) throws GException {
		try {
			getDataTableQuery().deleteData(p_dbDataTable, p_extrabean, p_transactionBegin);

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public void deleteData(T p_dbDataTable, TransactionBegin p_transactionBegin, DoVerifyData p_doverifydata, DoRunBefore p_dorunbefore,
		DoRunAfter p_dorunafter) throws GException {

		deleteData(p_dbDataTable, new ExtraBean(), p_transactionBegin, p_doverifydata, p_dorunbefore, p_dorunafter);
	}

	public void deleteData(T p_dbDataTable, ExtraBean p_extrabean, TransactionBegin p_transactionBegin, DoVerifyData p_doverifydata, DoRunBefore p_dorunbefore,
		DoRunAfter p_dorunafter) throws GException {
		try {
			getDataTableQuery().deleteData(p_dbDataTable, p_extrabean, p_transactionBegin, p_doverifydata, p_dorunbefore, p_dorunafter);

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public void useMockData(String p_strJSONArray) throws GException {
		getDataTableQuery().useMockData(p_strJSONArray);
	}

	public void useMockData(JSONArray p_jsonarray) throws GException {
		getDataTableQuery().useMockData(p_jsonarray);
	}

	private IDataTableQuery<T> getDataTableQuery() throws GException {
		try {
			if(m_datatablequery == null ) {
				if(GUtilities.isServer()) {
					m_datatablequery = (IDataTableQuery<T>)Class.forName("com.laithailibrary.serverlibrary.server.datatable.DataTableQuery").newInstance();

					m_datatablequery.setSessionID(m_sessionid);
					m_datatablequery.setDBDataTableClass(m_clsDBDataTable);

				} else {
					if(m_sessionid == null) {
						m_sessionid = SessionClientUtility.getClientSessionID();
					}

					if(m_sessionid == null) {
						GLog.severe("Invalid SessionID!!!");

						if(ExcDialog.getDisplay() == ExcDialog.Display.Yes) {
							new ExcDialog("Invalid SessionID!!!");
							new MsgDialog("Program is close.");
						}

						System.exit(ProgramExit.InvalidSessionID);
					}

					m_datatablequery = (IDataTableQuery<T>)Proxy.newProxyInstance(IDataTableQuery.class.getClassLoader(), new Class<?>[] {IDataTableQuery.class},
						new DataTableQueryInvocationHandler(m_sessionid, this.getClass()));
				}
			}

			return m_datatablequery;

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}
}
