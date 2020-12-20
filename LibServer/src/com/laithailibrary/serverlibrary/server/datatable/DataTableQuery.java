package com.laithailibrary.serverlibrary.server.datatable;

import java.lang.reflect.*;
import java.util.*;
import com.laithailibrary.logger.*;
import com.laithailibrary.serverlibrary.server.dbcache.*;
import com.laithailibrary.serverlibrary.server.mock.*;
import com.laithailibrary.sharelibrary.bean.*;
import com.laithailibrary.sharelibrary.collection.*;
import com.laithailibrary.sharelibrary.datatableview.*;
import com.laithailibrary.sharelibrary.db.dbobject.*;
import com.laithailibrary.sharelibrary.db.dbutilities.*;
import com.laithailibrary.sharelibrary.field.*;
import com.laithailibrary.sharelibrary.interfaceclass.*;
import com.laithailibrary.sharelibrary.session.*;
import com.laithailibrary.sharelibrary.sqlstatement.*;
import com.laithailibrary.sharelibrary.sqlstatement.support.*;
import com.laithailibrary.sharelibrary.support.*;
import com.laithailibrary.sharelibrary.thread.*;
import com.laithailibrary.sharelibrary.util.*;
import exc.*;
import org.json.*;

/**
 * Created by IntelliJ IDEA.
 * User: dumkrerng
 * Date: 6/12/11
 * Time: 6:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class DataTableQuery<T extends DBDataTable> implements IDataTableQuery<T> {

	private Class<T> m_classDBDataTable = null;

	private T m_beanDBDataTable = null;
	private T m_beanDBDataTable_Original = null;
	private T m_beanDBDataTable_Edited = null;

	private DataTableLogic<T> m_datatablelogic = new XDataTableLogic();

	private MockDataTableQuery<T, DataTableQuery<T>> m_mockdata = null;

	private ISessionID m_sessionid;

	private static final long serialVersionUID = 3;

	public DataTableQuery() {
		try {
			m_sessionid = ClientSessionID.getSessionID(new GThreadGroup(Thread.currentThread()));

		} catch(Exception exception) {
			ExceptionHandler.display(exception);

			m_sessionid = null;
		}
	}

	public DataTableQuery(ISessionID p_sessionid, Class p_classDBDataTable) throws GException {
		m_sessionid = p_sessionid;

		init(p_classDBDataTable);
	}

	public void setSessionID(ISessionID p_sessionid) {
		m_sessionid = p_sessionid;
	}

	public ISessionID getSessionID() {
		return m_sessionid;
	}

	public void setDBDataTableClass(Class p_classDBDataTable) throws GException {
		init(p_classDBDataTable);
	}

	private void init(Class<T> p_class) throws GException {
		try {
			m_beanDBDataTable = p_class.newInstance();
			m_classDBDataTable = p_class;

			DataTableLogic datatablelogic = DataTableLogicRegister.getDataTableLogic(m_beanDBDataTable.getClass());

			if(datatablelogic != null) {
				m_datatablelogic = datatablelogic;
			}

			m_datatablelogic.setSessionID(m_sessionid);

		} catch (InstantiationException exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);

		} catch (IllegalAccessException exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public void useMockData(String p_strJSONArray) throws GException {
		try {
			useMockData(new JSONArray(p_strJSONArray));

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public void useMockData(JSONArray p_jsonarray) throws GException {
		try {
			m_mockdata = new MockDataTableQuery<>(m_beanDBDataTable, this, m_datatablelogic);
			m_mockdata.setMockData(p_jsonarray);

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public List<T> getDBDataTables(SQLStatement p_statement) throws GException {
		try {
			if(m_mockdata != null) {
				return m_mockdata.getDBDataTables(p_statement);
			}

			TableName tablename = m_beanDBDataTable.getTableName();
			List<String> lsFieldName = m_beanDBDataTable.getFieldNames();

			StringBuilder builder = new StringBuilder();
			builder.append("SELECT ");

			StringBuilder builderFieldName = new StringBuilder();

			for(String strFieldName : lsFieldName) {
				if(builderFieldName.length() > 0) {
					builderFieldName.append(",\n");
				}

				builderFieldName.append(strFieldName);
			}

			builder.append(builderFieldName).append("\n");
			builder.append("FROM ").append(tablename.getTableDB()).append("\n");

			if(p_statement.isSet()) {
				builder.append("WHERE ").append(p_statement.getSQLString(DBUtilities.getConnection()));
			}

			GStatement stm = new GStatement(DBUtilities.getConnection(getSessionID()));
			GResultSet rst = stm.executeQuery(builder.toString());

			List<T> lsDBDataTables = new GList<>();

			while(rst.next()) {
				T beanDBDataTable = m_classDBDataTable.newInstance();

				for(String strFieldName : lsFieldName) {
					Class<? extends DBObject> aClass = m_beanDBDataTable.getClass_FieldName(strFieldName);
					DBObject dboNewInstance = aClass.newInstance();

					if(aClass.newInstance() instanceof DBString) {
						String strResult = rst.getString(strFieldName);
						dboNewInstance.setStringValue(strResult);

					} else if(aClass.newInstance() instanceof DBInteger) {
						int intResult = rst.getInt(strFieldName);
						dboNewInstance.setStringValue(Integer.toString(intResult));

					} else if(aClass.newInstance() instanceof DBBoolean) {
						Boolean bolResult = rst.getBoolean(strFieldName);
						dboNewInstance.setStringValue(bolResult.toString());

					} else if(aClass.newInstance() instanceof DBDate) {
						DBDate dbdResult = rst.getDate(strFieldName);
						dboNewInstance.setStringValue(dbdResult.getString());

					} else if(aClass.newInstance() instanceof DBDateTime) {
						DBDateTime dbdtResult = rst.getDateTime(strFieldName);
						dboNewInstance.setStringValue(dbdtResult.getString());

					} else if(aClass.newInstance() instanceof DBDecimal) {
						DBDecimal dbdmResult = rst.getDecimal(strFieldName);
						dboNewInstance.setStringValue(dbdmResult.getString());

					} else if(aClass.newInstance() instanceof DBMoney) {
						DBDecimal dbdmResult = rst.getDecimal(strFieldName);
						dboNewInstance.setStringValue(dbdmResult.getString());

					} else if(aClass.newInstance() instanceof DBQuantity) {
						DBDecimal dbdmResult = rst.getDecimal(strFieldName);
						dboNewInstance.setStringValue(dbdmResult.getString());

					} else {
						throw new GException("Not-support Class: " + aClass.getName());
					}

					beanDBDataTable.setData(strFieldName, dboNewInstance);
				}

				lsDBDataTables.add(beanDBDataTable);
			}

			return lsDBDataTables;

		} catch(Exception exception) {
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
			if(m_mockdata != null) {
				return m_mockdata.getDataVectorTable(p_statement, p_lsSortKeys, p_intRowLimit, p_sorting);
			}

			TableName tablename = m_beanDBDataTable.getTableName();
			List<String> lsFieldName = m_beanDBDataTable.getFieldNames();

			StringBuilder builder = new StringBuilder();
			builder.append("SELECT ");

			StringBuilder builderFieldName = new StringBuilder();

			for(String strFieldName : lsFieldName) {
				if(builderFieldName.length() > 0) {
					builderFieldName.append(",\n");
				}

				builderFieldName.append(strFieldName);
			}

			builder.append(builderFieldName).append("\n");
			builder.append("FROM ").append(tablename.getTableDB()).append("\n");

			if(p_statement.isSet()) {
				builder.append("WHERE ").append(p_statement.getSQLString(DBUtilities.getConnection()));
			}

			if(p_lsSortKeys.size() > 0) {
				StringBuilder builderFieldName_OrderBy = new StringBuilder();

				for(String strSortKey : p_lsSortKeys) {
					if(builderFieldName_OrderBy.length() > 0) {
						builderFieldName_OrderBy.append(", ");
					}

					builderFieldName_OrderBy.append(strSortKey);
				}

				builder.append("\nORDER BY ").append(builderFieldName_OrderBy);

				if(p_sorting == Sorting.Descending) {
					builder.append(" DESC");
				}
			}

			GStatement stm = new GStatement(DBUtilities.getConnection(getSessionID()));
			GResultSet rst = stm.executeQuery(builder.toString());

			DataTableColumnModel columnmodel = m_beanDBDataTable.getDataVectorRow().getDataTableColumnModel();

			List<String> lsPrimaryKeys = new GList<>();
			lsPrimaryKeys.add(m_beanDBDataTable.getFieldPrimaryKeyName());

			DataVectorTable dvtReturn = new DataVectorTable(columnmodel, lsPrimaryKeys, p_lsSortKeys, p_sorting);

			int intRowCount = 0;

			while(rst.next()) {
				T dbDataTable = m_classDBDataTable.newInstance();

				for(String strFieldName : lsFieldName) {
					Class<? extends DBObject> aClass = m_beanDBDataTable.getClass_FieldName(strFieldName);
					DBObject dboNewInstance = aClass.newInstance();

					if(aClass.newInstance() instanceof DBString) {
						String strResult = rst.getString(strFieldName);
						dboNewInstance.setStringValue(strResult);

					} else if(aClass.newInstance() instanceof DBInteger) {
						int intResult = rst.getInt(strFieldName);
						dboNewInstance.setStringValue(Integer.toString(intResult));

					} else if(aClass.newInstance() instanceof DBBoolean) {
						Boolean bolResult = rst.getBoolean(strFieldName);
						dboNewInstance.setStringValue(bolResult.toString());

					} else if(aClass.newInstance() instanceof DBDate) {
						DBDate dbdResult = rst.getDate(strFieldName);
						dboNewInstance.setStringValue(dbdResult.getString());

					} else if(aClass.newInstance() instanceof DBDateTime) {
						DBDateTime dbdtResult = rst.getDateTime(strFieldName);
						dboNewInstance.setStringValue(dbdtResult.getString());

					} else if(aClass.newInstance() instanceof DBDecimal) {
						DBDecimal dbdmResult = rst.getDecimal(strFieldName);
						dboNewInstance.setStringValue(dbdmResult.getString());

					} else if(aClass.newInstance() instanceof DBMoney) {
						DBDecimal dbdmResult = rst.getDecimal(strFieldName);
						dboNewInstance.setStringValue(dbdmResult.getString());

					} else if(aClass.newInstance() instanceof DBQuantity) {
						DBDecimal dbdmResult = rst.getDecimal(strFieldName);
						dboNewInstance.setStringValue(dbdmResult.getString());

					} else {
						throw new GException("Not-support Class: " + aClass.getName());
					}

					dbDataTable.setData(strFieldName, dboNewInstance);
				}

				dvtReturn.addDataRow(dbDataTable.getDataVectorRow());

				intRowCount++;

				if(p_intRowLimit > 0) {
					if(intRowCount >= p_intRowLimit) {
						break;
					}
				}
			}

			return dvtReturn;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public<DBO extends DBObject> T getDBDataTable(DBO p_PrimaryKeyID) throws GException {
		try {
			if(m_mockdata != null) {
				return m_mockdata.getDBDataTable(p_PrimaryKeyID);
			}

			TableName tablename = m_beanDBDataTable.getTableName();
			List<String>  lsFieldName = m_beanDBDataTable.getFieldNames();
			String strFieldPrimaryKey = m_beanDBDataTable.getFieldPrimaryKeyName();

			T beanDBDataTable = m_classDBDataTable.newInstance();

			if(getCatchDataTable() != null) {
				GCacheDataTable<T> catchdatatable = getCatchDataTable();
				beanDBDataTable = catchdatatable.getDBDataTable(new DBString(p_PrimaryKeyID));

			} else {
				StringBuilder builder = new StringBuilder();
				builder.append("SELECT ");

				StringBuilder builderFieldName = new StringBuilder();

				for(String strFieldName : lsFieldName) {
					if(builderFieldName.length() > 0) {
						builderFieldName.append(",\n");
					}

					builderFieldName.append(strFieldName);
				}

				builder.append(builderFieldName).append("\n");
				builder.append("FROM ").append(tablename.getTableDB()).append("\n");
				builder.append("WHERE ").append(strFieldPrimaryKey).append(" = ").append(p_PrimaryKeyID.getStringSQL());

				GStatement stm = new GStatement(DBUtilities.getConnection(getSessionID()));
				GResultSet rst = stm.executeQuery(builder.toString());

				if(rst.next()) {
					for(String strFieldName : lsFieldName) {
						Class<? extends DBObject> aClass = m_beanDBDataTable.getClass_FieldName(strFieldName);
						DBObject dboNewInstance = aClass.newInstance();

						if(aClass.newInstance() instanceof DBString) {
							String strResult = rst.getString(strFieldName);
							dboNewInstance.setStringValue(strResult);

						} else if(aClass.newInstance() instanceof DBInteger) {
							int intResult = rst.getInt(strFieldName);
							dboNewInstance.setStringValue(Integer.toString(intResult));

						} else if(aClass.newInstance() instanceof DBBoolean) {
							Boolean bolResult = rst.getBoolean(strFieldName);
							dboNewInstance.setStringValue(bolResult.toString());

						} else if(aClass.newInstance() instanceof DBDate) {
							DBDate dbdResult = rst.getDate(strFieldName);
							dboNewInstance.setStringValue(dbdResult.getString());

						} else if(aClass.newInstance() instanceof DBDateTime) {
							DBDateTime dbdtResult = rst.getDateTime(strFieldName);
							dboNewInstance.setStringValue(dbdtResult.getString());

						} else if(aClass.newInstance() instanceof DBDecimal) {
							DBDecimal dbdmResult = rst.getDecimal(strFieldName);
							dboNewInstance.setStringValue(dbdmResult.getString());

						} else if(aClass.newInstance() instanceof DBMoney) {
							DBDecimal dbdmResult = rst.getDecimal(strFieldName);
							dboNewInstance.setStringValue(dbdmResult.getString());

						} else if(aClass.newInstance() instanceof DBQuantity) {
							DBDecimal dbdmResult = rst.getDecimal(strFieldName);
							dboNewInstance.setStringValue(dbdmResult.getString());

						} else {
							throw new GException("Not-support Class: " + aClass.getName());
						}

						beanDBDataTable.setData(strFieldName, dboNewInstance);
					}
				}
			}

			return beanDBDataTable;

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	private <DBO extends DBObject> DBDataTable getDBDataTable_OnAfterInserted(DBO p_PrimaryKeyID) throws GException {
		try {
			if(m_mockdata != null) {
				return m_mockdata.getDBDataTable(p_PrimaryKeyID);
			}

			TableName tablename = m_beanDBDataTable.getTableName();
			List<String>  lsFieldName = m_beanDBDataTable.getFieldNames();
			String strFieldPrimaryKey = m_beanDBDataTable.getFieldPrimaryKeyName();
			DBDataTable beanDBDataTable = m_classDBDataTable.newInstance();

			if(getCatchDataTable() != null) {
				GCacheDataTable<T> catchdatatable = getCatchDataTable();
				beanDBDataTable = catchdatatable.getDBDataTable(new DBString(p_PrimaryKeyID));

			} else {
				StringBuilder builder = new StringBuilder();
				builder.append("SELECT ");

				StringBuilder builderFieldName = new StringBuilder();

				for(String strFieldName : lsFieldName) {
					if(builderFieldName.length() > 0) {
						builderFieldName.append(",\n");
					}

					builderFieldName.append(strFieldName);
				}

				builder.append(builderFieldName).append("\n");
				builder.append("FROM ").append(tablename.getTableDB()).append("\n");
				builder.append("WHERE ").append(strFieldPrimaryKey).append(" = ").append(p_PrimaryKeyID.getStringSQL());

				GStatement stm = new GStatement(DBUtilities.getConnection(getSessionID()));
				GResultSet rst = stm.executeQuery(builder.toString());

				if(rst.next()) {
					for(String strFieldName : lsFieldName) {
						Class<? extends DBObject> aClass = m_beanDBDataTable.getClass_FieldName(strFieldName);
						DBObject dboNewInstance = aClass.newInstance();

						if(aClass.newInstance() instanceof DBString) {
							String strResult = rst.getString(strFieldName);
							dboNewInstance.setStringValue(strResult);

						} else if(aClass.newInstance() instanceof DBInteger) {
							int intResult = rst.getInt(strFieldName);
							dboNewInstance.setStringValue(Integer.toString(intResult));

						} else if(aClass.newInstance() instanceof DBBoolean) {
							Boolean bolResult = rst.getBoolean(strFieldName);
							dboNewInstance.setStringValue(bolResult.toString());

						} else if(aClass.newInstance() instanceof DBDate) {
							DBDate dbdResult = rst.getDate(strFieldName);
							dboNewInstance.setStringValue(dbdResult.getString());

						} else if(aClass.newInstance() instanceof DBDateTime) {
							DBDateTime dbdtResult = rst.getDateTime(strFieldName);
							dboNewInstance.setStringValue(dbdtResult.getString());

						} else if(aClass.newInstance() instanceof DBDecimal) {
							DBDecimal dbdmResult = rst.getDecimal(strFieldName);
							dboNewInstance.setStringValue(dbdmResult.getString());

						} else if(aClass.newInstance() instanceof DBMoney) {
							DBDecimal dbdmResult = rst.getDecimal(strFieldName);
							dboNewInstance.setStringValue(dbdmResult.getString());

						} else if(aClass.newInstance() instanceof DBQuantity) {
							DBDecimal dbdmResult = rst.getDecimal(strFieldName);
							dboNewInstance.setStringValue(dbdmResult.getString());

						} else {
							throw new GException("Not-support Class: " + aClass.getName());
						}
						beanDBDataTable.setData(strFieldName, dboNewInstance);
					}
				}
			}

			return beanDBDataTable;

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public List<DBString> insertDatas(List<T> p_lsDataTables, TransactionBegin p_transactionBegin) throws GException {
		return insertDatas(p_lsDataTables, new ExtraBean(), p_transactionBegin);
	}

	public List<DBString> insertDatas(List<T> p_lsDataTables, ExtraBean p_extrabean, TransactionBegin p_transactionBegin) throws GException {
		try {
			if(p_lsDataTables.size() <= 0) {
				throw new GException("Invalid DataTable!");
			}

			if(p_transactionBegin == TransactionBegin.Yes) {
				DBUtilities.getConnection(getSessionID()).transactionBegin();
			}

			List<DBString> lsNewIDs = new GList<>();

			for(T dbdatatable : p_lsDataTables) {
				DBString dbstrNewID = insertData(dbdatatable, p_extrabean, DoUpdateCatchDataTable.No, TransactionBegin.No);

				lsNewIDs.add(dbstrNewID);
			}

			if(getCatchDataTable() != null) {
				List<DBString> lsNewIDs_ToUpdate = new GList<>();

				for(DBString dbstrNewID : lsNewIDs) {
					lsNewIDs_ToUpdate.add(dbstrNewID);
				}

				getCatchDataTable().readData(lsNewIDs_ToUpdate, getSessionID());
			}

			if(p_transactionBegin == TransactionBegin.Yes) {
				DBUtilities.getConnection(getSessionID()).commit();
			}

			return lsNewIDs;

		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			try {
				DBUtilities.getConnection(getSessionID()).rollback();

				GLog.severe("Insert Data error!!!  ".concat(exception.getMessage()));

			} catch (Exception e) {
				e.printStackTrace();
			}

			throw new GException(exception);
		}
	}

	public DBString insertData(T p_beanDBDataTable, TransactionBegin p_transactionBegin) throws GException {
		return insertData(p_beanDBDataTable, new ExtraBean(), p_transactionBegin);
	}

	public DBString insertData(T p_beanDBDataTable, DoUpdateCatchDataTable p_doupdatecatchdatatable, TransactionBegin p_transactionBegin) throws GException {
		return insertData(p_beanDBDataTable, new ExtraBean(), p_doupdatecatchdatatable, p_transactionBegin);
	}
	
	public DBString insertData(T p_beanDBDataTable, ExtraBean p_extrabean, TransactionBegin p_transactionBegin) throws GException {
		return baseInsertData(p_beanDBDataTable, p_extrabean, DoUpdateCatchDataTable.Yes, p_transactionBegin, DoVerifyData.Yes,
			DoRunBefore.Yes, DoRunAfter.Yes);
	}

	public DBString insertData(T p_beanDBDataTable, ExtraBean p_extrabean, DoUpdateCatchDataTable p_doupdatecatchdatatable, TransactionBegin p_transactionBegin) throws GException {
		return baseInsertData(p_beanDBDataTable, p_extrabean, p_doupdatecatchdatatable, p_transactionBegin, DoVerifyData.Yes,
			DoRunBefore.Yes, DoRunAfter.Yes);
	}

	public DBString insertData(T p_beanDBDataTable, TransactionBegin p_transactionBegin, DoVerifyData p_doverifydata, DoRunBefore p_dorunbefore,
		DoRunAfter p_dorunafter) throws GException {

		return insertData(p_beanDBDataTable, new ExtraBean(), p_transactionBegin, p_doverifydata, p_dorunbefore, p_dorunafter);
	}

	public DBString insertData(T p_beanDBDataTable, DoUpdateCatchDataTable p_doupdatecatchdatatable, TransactionBegin p_transactionBegin,
		DoVerifyData p_doverifydata, DoRunBefore p_dorunbefore, DoRunAfter p_dorunafter) throws GException {

		return insertData(p_beanDBDataTable, new ExtraBean(), p_doupdatecatchdatatable, p_transactionBegin, p_doverifydata, p_dorunbefore,
			p_dorunafter);
	}

	public DBString insertData(T p_beanDBDataTable, ExtraBean p_extrabean, TransactionBegin p_transactionBegin, DoVerifyData p_doverifydata,
		DoRunBefore p_dorunbefore, DoRunAfter p_dorunafter) throws GException {

		return baseInsertData(p_beanDBDataTable, p_extrabean, DoUpdateCatchDataTable.Yes, p_transactionBegin, p_doverifydata,
			p_dorunbefore, p_dorunafter);
	}

	public DBString insertData(T p_beanDBDataTable, ExtraBean p_extrabean, DoUpdateCatchDataTable p_doupdatecatchdatatable, TransactionBegin p_transactionBegin,
		DoVerifyData p_doverifydata, DoRunBefore p_dorunbefore, DoRunAfter p_dorunafter) throws GException {

		return baseInsertData(p_beanDBDataTable, p_extrabean, p_doupdatecatchdatatable, p_transactionBegin, p_doverifydata, p_dorunbefore,
			p_dorunafter);
	}

	private DBString baseInsertData(T p_beanDBDataTable, ExtraBean p_extrabean, DoUpdateCatchDataTable p_doupdatecatchdatatable,
		TransactionBegin p_transactionBegin, DoVerifyData p_doverifydata, DoRunBefore p_dorunbefore, DoRunAfter p_dorunafter) throws GException {

		try {
			if(m_mockdata != null) {
				return m_mockdata.baseInsertData(p_beanDBDataTable, p_extrabean, p_doverifydata, p_dorunbefore, p_dorunafter);
			}

			if(p_transactionBegin == TransactionBegin.Yes) {
				DBUtilities.getConnection(getSessionID()).transactionBegin();
			}

		  prepareDataOnInsert(p_beanDBDataTable, p_extrabean);

			if(p_doverifydata == DoVerifyData.Yes) {
				verifyDataOnInsert(p_beanDBDataTable, p_extrabean);
			}

			if(p_dorunbefore == DoRunBefore.Yes) {
				beforeOnInsert(p_beanDBDataTable, p_extrabean);
			}

			p_beanDBDataTable.setRecordUpdated(DBDateTime.getCurrentDateTime());

			verifyFieldIsMandatory(p_beanDBDataTable);
			verifyReferentForeignKey_OnInsertOnUpdate(p_beanDBDataTable);
			verifyUniqueConstrain(p_beanDBDataTable);

			DBString dbstrNewID = onDBInsert(p_beanDBDataTable);

			if(p_dorunafter == DoRunAfter.Yes) {
				afterOnInsert(p_beanDBDataTable, p_extrabean);
			}

			if(p_transactionBegin == TransactionBegin.Yes) {
				DBUtilities.getConnection(getSessionID()).commit();
			}

			if(p_doupdatecatchdatatable == DoUpdateCatchDataTable.Yes) {
				if(getCatchDataTable() != null) {
					getCatchDataTable().readData(dbstrNewID, getSessionID());
				}
			}

			return dbstrNewID;

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);

			try {
				DBUtilities.getConnection(getSessionID()).rollback();

				if(getCatchDataTable() != null) {
					getCatchDataTable().reloadData(getSessionID());
				}

				GLog.severe("Insert Data error!!!  ".concat(exception.getMessage()));

			} catch (Exception e) {
			  e.printStackTrace();
			}

		  throw new GException(exception);
		}
	}

	private DBString onDBInsert(T p_beanDBDataTable) throws GException {
		try {
			TableName tablename = m_beanDBDataTable.getTableName();
			List<String>  lsFieldName = m_beanDBDataTable.getFieldNames();
			String strFieldPrimaryKey = m_beanDBDataTable.getFieldPrimaryKeyName();

			StringBuilder builder = new StringBuilder();
			builder.append("INSERT INTO ").append(tablename.getTableName());

			StringBuilder builderFieldName = new StringBuilder();
			StringBuilder builderFieldData = new StringBuilder();

			DBString dbstrPrimaryKeyID_New = new DBString();

			for(String strFieldName : lsFieldName) {
				if(!strFieldName.equalsIgnoreCase(strFieldPrimaryKey)) {
					if(builderFieldName.length() > 0) {
						builderFieldName.append(", ");
					}

					builderFieldName.append(strFieldName);

					DBObject dbObject = p_beanDBDataTable.getData(strFieldName);

					if(builderFieldData.length() > 0) {
						builderFieldData.append(", ");
					}

					if(dbObject == null
						|| dbObject.isInvalid()) {

						builderFieldData.append("NULL");

					} else {
						builderFieldData.append(dbObject.getStringSQL());
					}
				} else {
					if(builderFieldName.length() > 0) {
						builderFieldName.append(", ");
					}

					builderFieldName.append(strFieldName);

					dbstrPrimaryKeyID_New = TableIDUtilities.getNextPrimaryKeyID(m_beanDBDataTable, DBDate.getCurrentDate(), getSessionID());

					if(builderFieldData.length() > 0) {
						builderFieldData.append(", ");
					}

					if(dbstrPrimaryKeyID_New.isInvalid()) {
						builderFieldData.append("NULL");

					} else {
						builderFieldData.append(dbstrPrimaryKeyID_New.getStringSQL());
					}
				}
			}

			builder.append("(").append(builderFieldName).append(")\nVALUES");
			builder.append("(").append(builderFieldData).append(")\n");

			GStatement stm = new GStatement(DBUtilities.getConnection(getSessionID()));
			stm.executeInsert(builder.toString());

			p_beanDBDataTable.setData(strFieldPrimaryKey, dbstrPrimaryKeyID_New);

			return dbstrPrimaryKeyID_New;

		} catch (RuntimeException exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public DBString updateData(T p_dbDataTable_Edited, TransactionBegin p_transactionBegin) throws GException {
		return updateData(p_dbDataTable_Edited, new ExtraBean(), p_transactionBegin);
	}

	public DBString updateData(T p_dbDataTable_Edited, TransactionBegin p_transactionBegin, DoVerifyData p_doverifydata, DoRunBefore p_dorunbefore,
		DoRunAfter p_dorunafter) throws GException {

		return updateData(p_dbDataTable_Edited, new ExtraBean(), p_transactionBegin, p_doverifydata, p_dorunbefore, p_dorunafter);
	}
	
	public DBString updateData(T p_dbDataTable_Edited, ExtraBean p_extrabean, TransactionBegin p_transactionBegin) throws GException {
		return updateData(p_dbDataTable_Edited, p_extrabean, p_transactionBegin, DoVerifyData.Yes, DoRunBefore.Yes, DoRunAfter.Yes);
	}

	public DBString updateData(T p_dbDataTable_Edited, ExtraBean p_extrabean, TransactionBegin p_transactionBegin, DoVerifyData p_doverifydata, DoRunBefore p_dorunbefore,
		DoRunAfter p_dorunafter) throws GException {

		try {
			String strPrimaryKeyName = p_dbDataTable_Edited.getFieldPrimaryKeyName();
			DBObject dboPrimaryKeyValue = p_dbDataTable_Edited.getData(strPrimaryKeyName);

			if(dboPrimaryKeyValue.isInvalid()) {
				throw new GException("Invalid PrimaryKey value!");
			}

			T dbDataTable_Original = getDBDataTable(dboPrimaryKeyValue);

			return updateData(dbDataTable_Original, p_dbDataTable_Edited, p_extrabean, p_transactionBegin);

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	private DBString updateData(T p_dbDataTable_Original, T p_dbDataTable_Edited, ExtraBean p_extrabean, TransactionBegin p_transactionBegin) throws GException {
		return updateData(p_dbDataTable_Original, p_dbDataTable_Edited, p_extrabean, p_transactionBegin, DoVerifyData.Yes, DoRunBefore.Yes,
			DoRunAfter.Yes);
	}

	private DBString updateData(T p_dbDataTable_Original, T p_dbDataTable_Edited, ExtraBean p_extrabean, TransactionBegin p_transactionBegin,
		DoVerifyData p_doverifydata, DoRunBefore p_dorunbefore, DoRunAfter p_dorunafter) throws GException {

		try {
			if(m_mockdata != null) {
				return m_mockdata.updateData(p_dbDataTable_Original, p_dbDataTable_Edited, p_extrabean, p_doverifydata, p_dorunbefore,
					p_dorunafter);
			}

			m_beanDBDataTable_Original = p_dbDataTable_Original;
			m_beanDBDataTable_Edited = p_dbDataTable_Edited;

			if(p_transactionBegin == TransactionBegin.Yes) {
				DBUtilities.getConnection(getSessionID()).transactionBegin();
			}

		  prepareDataOnUpdate(m_beanDBDataTable_Original, m_beanDBDataTable_Edited, p_extrabean);

			if(p_doverifydata == DoVerifyData.Yes) {
				verifyDataOnUpdate(m_beanDBDataTable_Original, m_beanDBDataTable_Edited, p_extrabean);
			}

			if(p_dorunbefore == DoRunBefore.Yes) {
				beforeOnUpdate(m_beanDBDataTable_Original, m_beanDBDataTable_Edited, p_extrabean);
			}

			verifyFieldIsMandatory(m_beanDBDataTable_Edited);
			verifyReferentForeignKey_OnInsertOnUpdate(m_beanDBDataTable_Edited);
			verifyUniqueConstrain(m_beanDBDataTable_Edited);
			verifyRecordUpdated(m_beanDBDataTable_Edited);

			DBString dbstrPrimaryKeyValue = onDBUpdate();

			if(p_dorunafter == DoRunAfter.Yes) {
				afterOnUpdate(m_beanDBDataTable_Original, m_beanDBDataTable_Edited, p_extrabean);
			}

			if(p_transactionBegin == TransactionBegin.Yes) {
				DBUtilities.getConnection(getSessionID()).commit();
			}

			if(getCatchDataTable() != null) {
				getCatchDataTable().updateData(dbstrPrimaryKeyValue, getSessionID());
			}

			return dbstrPrimaryKeyValue;

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);

			try {
				DBUtilities.getConnection(getSessionID()).rollback();

				if(getCatchDataTable() != null) {
					getCatchDataTable().reloadData(getSessionID());
				}

				GLog.severe("Update Data error!!!  ".concat(exception.getMessage()));

			} catch (Exception e) {
			  e.printStackTrace();
			}

		  throw new GException(exception);
		}
	}

	private DBString onDBUpdate() throws GException {
		try {
			TableName tablename = m_beanDBDataTable.getTableName();
			List<String>  lsFieldName = m_beanDBDataTable.getFieldNames();
			String strFieldPrimaryKey = m_beanDBDataTable.getFieldPrimaryKeyName();

			StringBuilder builder = new StringBuilder();
			builder.append("UPDATE ").append(tablename.getTableName()).append("\nSET ");

			StringBuilder builderFieldName_Data = new StringBuilder();
			DBObject dboPrimaryKeyValue = m_beanDBDataTable_Edited.getData(strFieldPrimaryKey);

			if(dboPrimaryKeyValue.isInvalid()) {
				throw new GException("Primary Key value is invalid!");
			}

			for(String strFieldName : lsFieldName) {
				DataField datafield = m_beanDBDataTable.getDataField(strFieldName);

				if(datafield.isUpdateable()) {
					if(builderFieldName_Data.length() > 0) {
						builderFieldName_Data.append(",\n");
					}

					DBObject dbObject = m_beanDBDataTable_Edited.getData(strFieldName);
					builderFieldName_Data.append(strFieldName).append(" = ").append(dbObject.getStringSQL());
				}
			}

			builder.append(builderFieldName_Data).append("\n");
			builder.append("WHERE ").append(strFieldPrimaryKey).append(" = ").append(dboPrimaryKeyValue.getStringSQL());

			GStatement stm = new GStatement(DBUtilities.getConnection(getSessionID()));
			stm.executeUpdate(builder.toString(), new UpdateRecord(m_beanDBDataTable.getFieldPrimaryKey(), (DBString)dboPrimaryKeyValue));

			return (DBString)dboPrimaryKeyValue;

		} catch (RuntimeException exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public void deleteAllData(SQLStatement p_statement, TransactionBegin p_transactionBegin) throws GException {
		try {
			List<T> lsDBDataTables = getDBDataTables(p_statement);

			if(p_transactionBegin == TransactionBegin.Yes) {
				DBUtilities.getConnection(getSessionID()).transactionBegin();
			}

			deleteDatas(lsDBDataTables, TransactionBegin.No);

			if(p_transactionBegin == TransactionBegin.Yes) {
				DBUtilities.getConnection(getSessionID()).commit();
			}
		} catch(Exception exception) {
			ExceptionHandler.display(exception);

			try {
				DBUtilities.getConnection(getSessionID()).rollback();

				GLog.severe("Delete Data error!!!  ".concat(exception.getMessage()));

			} catch (Exception e) {
				e.printStackTrace();
			}

			throw new GException(exception);
		}
	}

	public void deleteDatas(List<T> p_lsDataTables, TransactionBegin p_transactionBegin) throws GException {
		deleteDatas(p_lsDataTables, new ExtraBean(), p_transactionBegin);
	}

	public void deleteDatas(List<T> p_lsDataTables, ExtraBean p_extrabean, TransactionBegin p_transactionBegin) throws GException {
		try {
			if(p_lsDataTables.size() <= 0) {
				throw new GException("Invalid DataTable!");
			}

			if(p_transactionBegin == TransactionBegin.Yes) {
				DBUtilities.getConnection(getSessionID()).transactionBegin();
			}

			List<DBString> lsDataPrimaryKeys_ToUpdate = new GList<>();

			String strFieldPrimaryKey = m_beanDBDataTable.getFieldPrimaryKeyName();

			for(T dbdatatable : p_lsDataTables) {
				DBObject dbobjDataPrimaryKey = m_beanDBDataTable.getData(strFieldPrimaryKey);
				baseDeleteData(dbdatatable, p_extrabean, DoUpdateCatchDataTable.No, TransactionBegin.No, DoVerifyData.Yes, DoRunBefore.Yes,
					DoRunAfter.Yes);

				lsDataPrimaryKeys_ToUpdate.add(new DBString(dbobjDataPrimaryKey));
			}

			if(getCatchDataTable() != null) {
				getCatchDataTable().deleteData(lsDataPrimaryKeys_ToUpdate);
			}

			if(p_transactionBegin == TransactionBegin.Yes) {
				DBUtilities.getConnection(getSessionID()).commit();
			}
		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			try {
				DBUtilities.getConnection(getSessionID()).rollback();

				GLog.severe("Delete Data error!!!  ".concat(exception.getMessage()));

			} catch (Exception e) {
				e.printStackTrace();
			}

			throw new GException(exception);
		}
	}

	public void deleteData(T p_beanDBDataTable, TransactionBegin p_transactionBegin) throws GException {
		deleteData(p_beanDBDataTable, new ExtraBean(), p_transactionBegin);
	}

	public void deleteData(T p_beanDBDataTable, ExtraBean p_extrabean, TransactionBegin p_transactionBegin) throws GException {
		baseDeleteData(p_beanDBDataTable, p_extrabean, DoUpdateCatchDataTable.Yes, p_transactionBegin, DoVerifyData.Yes, DoRunBefore.Yes,
			DoRunAfter.Yes);
	}

	public void deleteData(T p_beanDBDataTable, TransactionBegin p_transactionBegin, DoVerifyData p_doverifydata, DoRunBefore p_dorunbefore,
		DoRunAfter p_dorunafter) throws GException {

		deleteData(p_beanDBDataTable, new ExtraBean(), p_transactionBegin, p_doverifydata, p_dorunbefore, p_dorunafter);
	}

	public void deleteData(T p_beanDBDataTable, ExtraBean p_extrabean, TransactionBegin p_transactionBegin, DoVerifyData p_doverifydata,
		DoRunBefore p_dorunbefore, DoRunAfter p_dorunafter) throws GException {

		baseDeleteData(p_beanDBDataTable, p_extrabean, DoUpdateCatchDataTable.Yes, p_transactionBegin, p_doverifydata, p_dorunbefore,
			p_dorunafter);
	}

	private void baseDeleteData(T p_beanDBDataTable, ExtraBean p_extrabean, DoUpdateCatchDataTable p_doupdatecatchdatatable,
		TransactionBegin p_transactionBegin, DoVerifyData p_doverifydata, DoRunBefore p_dorunbefore, DoRunAfter p_dorunafter) throws GException {

		try {
			if(m_mockdata != null) {
				m_mockdata.baseDeleteData(p_beanDBDataTable, p_extrabean, p_doverifydata, p_dorunbefore, p_dorunafter);

				return;
			}

			GLog.info("Deleting Data at ".concat(m_beanDBDataTable.getTableName().getTableName()).concat(" . . ."));

			if(p_transactionBegin == TransactionBegin.Yes) {
				DBUtilities.getConnection(getSessionID()).transactionBegin();
			}

		  prepareDataOnDelete(p_beanDBDataTable, p_extrabean);

			if(p_doverifydata == DoVerifyData.Yes) {
				verifyDataOnDelete(p_beanDBDataTable, p_extrabean);
			}

			if(p_dorunbefore == DoRunBefore.Yes) {
				beforeOnDelete(p_beanDBDataTable, p_extrabean);
			}

			verifyReferentForeignKey_OnDelete(p_beanDBDataTable);

			String strFieldPrimaryKey = m_beanDBDataTable.getFieldPrimaryKeyName();
			DBObject dboPrimaryKeyValue = p_beanDBDataTable.getData(strFieldPrimaryKey);

			onDBDelete(dboPrimaryKeyValue);

			if(p_dorunafter == DoRunAfter.Yes) {
				afterOnDelete(p_beanDBDataTable, p_extrabean);
			}

			if(p_transactionBegin == TransactionBegin.Yes) {
				DBUtilities.getConnection(getSessionID()).commit();
			}

			if(p_doupdatecatchdatatable == DoUpdateCatchDataTable.Yes) {
				if(getCatchDataTable() != null) {
					getCatchDataTable().deleteData(new DBString(dboPrimaryKeyValue));
				}
			}

			GLog.info("Delete Data at ".concat(m_beanDBDataTable.getTableName().getTableName()).concat("(ID: ").concat(dboPrimaryKeyValue.getString()).concat(") completed."));

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);

			try {
				DBUtilities.getConnection(getSessionID()).rollback();

				if(getCatchDataTable() != null) {
					getCatchDataTable().reloadData(getSessionID());
				}

				GLog.severe("Deleting Data error!!!  ".concat(exception.getMessage()));

			} catch (Exception e) {
			  e.printStackTrace();
			}

		  throw new GException(exception);
		}
	}

	private void verifyReferentForeignKey_OnDelete(T p_beanDBDataTable) throws GException {
		try {
		  FieldPrimaryKey fieldPrimaryKey = m_beanDBDataTable.getFieldPrimaryKey();
			Set<FieldForeignKey> setFieldForeignKeys = ForeignKey.getFieldForeignKeys(fieldPrimaryKey);

			for(FieldForeignKey fieldForeignKey : setFieldForeignKeys) {
				TableName tablename = fieldForeignKey.getTableName_ForeignKey();
				String strFieldName = fieldForeignKey.getFieldName_ForeignKey();

				String strFieldPrimaryKey = m_beanDBDataTable.getFieldPrimaryKeyName();
				DBObject dbObjectDataPrimaryKey = p_beanDBDataTable.getData(strFieldPrimaryKey);

				StringBuilder builder = new StringBuilder();
				builder.append("SELECT COUNT(").append(strFieldName).append(") AS CountData\n");
				builder.append("FROM ").append(tablename.getTableName()).append("\n");
				builder.append("WHERE ").append(strFieldName).append(" = ").append(dbObjectDataPrimaryKey.getStringSQL()).append("\n");

				GStatement stm = new GStatement(DBUtilities.getConnection(getSessionID()));
				GResultSet rst = stm.executeQuery(builder.toString());

				if(rst.next()) {
					DBInteger dbintCountData = new DBInteger(rst, "CountData");

					if(dbintCountData.isInvalid()) {
						dbintCountData = new DBInteger(0);
					}

					if(dbintCountData.compareTo(DBInteger.Zero) > 0) {
						throw new GException("Delete Error!\nRecord Has Referent to " + tablename.getTableName());
					}
				}
			}
		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	private void onDBDelete(DBObject p_dboPrimaryKeyValue) throws GException {
		try {
			TableName tablename = m_beanDBDataTable.getTableName();
			String strFieldPrimaryKey = m_beanDBDataTable.getFieldPrimaryKeyName();

			StringBuilder builder = new StringBuilder();
			builder.append("DELETE FROM ").append(tablename.getTableName()).append("\n");
			builder.append("WHERE ").append(strFieldPrimaryKey).append(" = ").append(p_dboPrimaryKeyValue.getStringSQL()).append("\n");

			GStatement stm = new GStatement(DBUtilities.getConnection(getSessionID()));
			stm.executeUpdate(builder.toString(), new UpdateRecord(m_beanDBDataTable.getFieldPrimaryKey(), (DBString)p_dboPrimaryKeyValue));

		} catch (RuntimeException exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	private void verifyFieldIsMandatory(DBDataTable p_dbDataTable) throws GException {
		try {
			String strFieldPrimaryKeyName = p_dbDataTable.getFieldPrimaryKeyName();

		  for(String strFieldName : p_dbDataTable.getFieldNames()) {
				if(strFieldPrimaryKeyName.compareTo(strFieldName) != 0) {
					DataField datafield = p_dbDataTable.getDataField(strFieldName);

					if(datafield.isMandatory()) {
						DBObject dbObject = p_dbDataTable.getData(datafield);

						if(dbObject == null
							|| dbObject.isInvalid()) {

							BeanErrorMessage.errorFieldNotInvalid(datafield);
						}
					}
				}
			}
		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	private void verifyReferentForeignKey_OnInsertOnUpdate(DBDataTable p_dbDataTable) throws GException {
		try {
			for(Field field : m_beanDBDataTable.getClass().getDeclaredFields()) {
				if(field.getType().isAssignableFrom(FieldForeignKey.class)) {
					FieldForeignKey fieldforeignkey = (FieldForeignKey)field.get(new ForeignKey());
					DataField datafield = fieldforeignkey.getDataField_ForeignKey();
					DBObject dbObjectValue = p_dbDataTable.getData(datafield);

					if(dbObjectValue.isValid()) {
						boolean bolExisting = false;

						Set<FieldPrimaryKey> setFieldPrimaryKey = fieldforeignkey.getFieldReferentPrimaryKeys();

						for(FieldPrimaryKey fieldprimarykey : setFieldPrimaryKey) {
							StringBuilder builder = new StringBuilder();
							builder.append("SELECT ").append(fieldprimarykey.getPrimaryKeyName()).append('\n');
							builder.append("FROM ").append(fieldprimarykey.getTableName()).append('\n');
							builder.append("WHERE ").append(fieldprimarykey.getPrimaryKeyName()).append(" = ").append(dbObjectValue.getStringSQL());

							GStatement stm = new GStatement(DBUtilities.getConnection(getSessionID()));
							GResultSet rst = stm.executeQuery(builder.toString());

							int intRowCount = rst.getRowCount();

							if(intRowCount > 0) {
								bolExisting = true;

								break;
							}
						}

						if(!bolExisting) {
							BeanErrorMessage.errorMultipleForeignKeyNonDataExisting(dbObjectValue);
						}
					}
				}
			}
		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	private void verifyUniqueConstrain(DBDataTable p_dbDataTable) throws GException {
		try {
			Map<String, FieldUnique> mapFieldUnique_Name = p_dbDataTable.getMapFieldUnique_Name();

			for(String strName : mapFieldUnique_Name.keySet()) {
				FieldUnique fieldUnique = mapFieldUnique_Name.get(strName);
				List<? extends DataField> lsDataFields = fieldUnique.getDataFields();

				BeanUtilities.verifyNotDuplicateFields(DBUtilities.getConnection(getSessionID()), p_dbDataTable.getTableName(), p_dbDataTable,
					lsDataFields, getSessionID());
			}
		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	private void verifyRecordUpdated(DBDataTable p_dbDataTable) throws GException {
		try {
			if(GUtilities.getSingleClient() == SingleClient.Yes) {
				return;
			}

			DBDateTime dbdtRecordUpdated_Edited = p_dbDataTable.getRecordUpdated();
			DBDateTime dbdtRecordUpdated_OnDB = new DBDateTime();

			TableName tablename = p_dbDataTable.getTableName();
			String strPrimaryKeyName = p_dbDataTable.getFieldPrimaryKeyName();

			DBObject dboPrimaryKeyValue = p_dbDataTable.getData(strPrimaryKeyName);

			StringBuilder builder = new StringBuilder();
			builder.append("SELECT RecordUpdated\n");
			builder.append("FROM ").append(tablename.getTableName()).append('\n');
			builder.append("WHERE ").append(strPrimaryKeyName).append(" = ").append(dboPrimaryKeyValue.getStringSQL());

			GStatement stm_NewConnection = new GStatement(DBUtilities.getNewConnection());
			GResultSet rst_NewConnection = stm_NewConnection.executeQuery(builder.toString());

			GResultSet rst_Temp;

			if(rst_NewConnection.getRowCount() != 1) {
				GStatement stm_CurrentConnection = new GStatement(DBUtilities.getConnection());
				GResultSet rst_CurrentConnection = stm_CurrentConnection.executeQuery(builder.toString());

				if(rst_CurrentConnection.getRowCount() != 1) {
					throw new GException("Invalid ID(" + dboPrimaryKeyValue.getString() + ") on Table(" + tablename.getTableName() + ")!");
				}

				rst_Temp = rst_CurrentConnection;

			} else {
				rst_Temp = rst_NewConnection;
			}

			if(rst_Temp.next()) {
				dbdtRecordUpdated_OnDB = new DBDateTime(rst_Temp, "RecordUpdated");
			}

			if(dbdtRecordUpdated_Edited.compareTo(dbdtRecordUpdated_OnDB) != 0) {
				throw new GException("Record ID(" + dboPrimaryKeyValue.getString() + ") is not update on Table(" + tablename.getTableName() + ")!");
			}
		} catch (Exception exception) {

			GLog.severe("NewConnection: " + DBUtilities.getNewConnection().toString());
			GLog.severe("OldConnection: " + DBUtilities.getConnection().toString());

		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	private GCacheDataTable<T> getCatchDataTable() throws GException {
		try {
			GCacheDataTable<T> catchdatatable = GCacheDataTableRegister.getCatchDataTable(m_classDBDataTable.getName());

			return catchdatatable;

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	private void prepareDataOnInsert(T p_dbDataTable, final ExtraBean p_extrabean) throws GException {
		try {
			if(m_datatablelogic != null) {
				m_datatablelogic.prepareDataOnInsert(p_dbDataTable, p_extrabean);
			}
		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	private void prepareDataOnUpdate(T p_dbDataTable, T p_dbDataTable_Edit, final ExtraBean p_extrabean) throws GException {
		try {
			if(m_datatablelogic != null) {
				m_datatablelogic.prepareDataOnUpdate(p_dbDataTable, p_dbDataTable_Edit, p_extrabean);
			}
		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	private void prepareDataOnDelete(T p_dbDataTable, final ExtraBean p_extrabean) throws GException {
		try {
			if(m_datatablelogic != null) {
				m_datatablelogic.prepareDataOnDelete(p_dbDataTable, p_extrabean);
			}
		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	private void verifyDataOnInsert(T p_dbDataTable, final ExtraBean p_extrabean) throws GException {
		try {
			if(m_datatablelogic != null) {
				m_datatablelogic.verifyDataOnInsert(p_dbDataTable, p_extrabean);
			}
		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	private void verifyDataOnUpdate(T p_dbDataTable, T p_dbDataTable_Edit, final ExtraBean p_extrabean) throws GException {
		try {
			if(m_datatablelogic != null) {
				m_datatablelogic.verifyDataOnUpdate(p_dbDataTable, p_dbDataTable_Edit, p_extrabean);
			}
		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	private void verifyDataOnDelete(T p_dbDataTable, final ExtraBean p_extrabean) throws GException {
		try {
			if(m_datatablelogic != null) {
				m_datatablelogic.verifyDataOnDelete(p_dbDataTable, p_extrabean);
			}
		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	private void beforeOnInsert(T p_dbDataTable, final ExtraBean p_extrabean) throws GException {
		try {
			if(m_datatablelogic != null) {
				m_datatablelogic.beforeOnInsert(p_dbDataTable, p_extrabean);
			}
		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	private void beforeOnUpdate(T p_dbDataTable, T p_dbDataTable_Edit, final ExtraBean p_extrabean) throws GException {
		try {
			if(m_datatablelogic != null) {
				m_datatablelogic.beforeOnUpdate(p_dbDataTable, p_dbDataTable_Edit, p_extrabean);
			}
		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	private void beforeOnDelete(T p_dbDataTable, final ExtraBean p_extrabean) throws GException {
		try {
			if(m_datatablelogic != null) {
				m_datatablelogic.beforeOnDelete(p_dbDataTable, p_extrabean);
			}
		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	private void afterOnInsert(T p_dbDataTable, final ExtraBean p_extrabean) throws GException {
		try {
			if(m_datatablelogic != null) {
				m_datatablelogic.afterOnInsert(p_dbDataTable, p_extrabean);
			}
		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	private void afterOnUpdate(T p_dbDataTable, T p_dbDataTable_Edit, final ExtraBean p_extrabean) throws GException {
		try {
			if(m_datatablelogic != null) {
				m_datatablelogic.afterOnUpdate(p_dbDataTable, p_dbDataTable_Edit, p_extrabean);
			}
		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	private void afterOnDelete(T p_dbDataTable, final ExtraBean p_extrabean) throws GException {
		try {
			if(m_datatablelogic != null) {
				m_datatablelogic.afterOnDelete(p_dbDataTable, p_extrabean);
			}
		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	private class XDataTableLogic extends DataTableLogic<T> {
		@Override
		public void prepareDataOnInsert(T p_dbDataTable, ExtraBean p_extrabean) throws GException {}
		@Override
		public void prepareDataOnUpdate(T p_dbDataTable, T p_dbDataTable_Edit, ExtraBean p_extrabean) throws GException {}
		@Override
		public void prepareDataOnDelete(T p_dbDataTable, ExtraBean p_extrabean) throws GException {}
		@Override
		public void verifyDataOnInsert(T p_dbDataTable, ExtraBean p_extrabean) throws GException {}
		@Override
		public void verifyDataOnUpdate(T p_dbDataTable, T p_dbDataTable_Edit, ExtraBean p_extrabean) throws GException {}
		@Override
		public void verifyDataOnDelete(T p_dbDataTable, ExtraBean p_extrabean) throws GException {}
		@Override
		public void beforeOnInsert(T p_dbDataTable, ExtraBean p_extrabean) throws GException {}
		@Override
		public void beforeOnUpdate(T p_dbDataTable, T p_dbDataTable_Edit, ExtraBean p_extrabean) throws GException {}
		@Override
		public void beforeOnDelete(T p_dbDataTable, ExtraBean p_extrabean) throws GException {}
		@Override
		public void afterOnInsert(T p_dbDataTable, ExtraBean p_extrabean) throws GException {}
		@Override
		public void afterOnUpdate(T p_dbDataTable, T p_dbDataTable_Edit, ExtraBean p_extrabean) throws GException {}
		@Override
		public void afterOnDelete(T p_dbDataTable, ExtraBean p_extrabean) throws GException {}
	}

	private enum DoUpdateCatchDataTable {
		Yes,
		No
	}
}
