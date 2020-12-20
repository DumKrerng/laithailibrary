package com.laithailibrary.serverlibrary.server.dbcache;


import java.util.*;
import com.laithailibrary.logger.*;
import com.laithailibrary.serverlibrary.server.search.*;
import com.laithailibrary.sharelibrary.bean.*;
import com.laithailibrary.sharelibrary.collection.*;
import com.laithailibrary.sharelibrary.datatableview.*;
import com.laithailibrary.sharelibrary.db.dbobject.*;
import com.laithailibrary.sharelibrary.db.dbutilities.*;
import com.laithailibrary.sharelibrary.field.*;
import com.laithailibrary.sharelibrary.session.*;
import com.laithailibrary.sharelibrary.sqlstatement.*;
import exc.*;

/**
 * Created with IntelliJ IDEA.
 * User: dumkrerng
 * Date: 9/6/12
 * Time: 10:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class GCacheDataTable<DT extends DBDataTable> {

	private DataVectorTable m_datatable;
	private Class<DT> m_clsDBDataTable;
	private DBDataTable m_dbDataTable;
	private FieldPrimaryKey m_primarykey;
	private DataTableColumnModel m_tableColumnModel;

	private GCashDataIndex m_dataindex;

	public GCacheDataTable(Class<DT> p_clsDBDataTable) throws GException {
		try {
			m_clsDBDataTable = p_clsDBDataTable;
			m_dbDataTable = p_clsDBDataTable.newInstance();
			m_primarykey = m_dbDataTable.getFieldPrimaryKey();
			m_dataindex = new GCashDataIndex();

			GCacheDataTableRegister.register(p_clsDBDataTable.getName(), this);

			createDataTable();

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	private void createDataTable() throws GException {
		try {
			List<DataField<? extends DBObject>> lsDataField = m_dbDataTable.getDataFields();
			m_tableColumnModel = new DataTableColumnModel();

			for(DataField<? extends DBObject> datafield : lsDataField) {
				m_tableColumnModel.addColumn(datafield);
			}

			List<String> lsPrimaryKeys = new GList<>();
			lsPrimaryKeys.add(m_dbDataTable.getFieldPrimaryKeyName());

			m_datatable = new DataVectorTable(m_tableColumnModel, lsPrimaryKeys);

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public int readData(ISessionID p_sessionid) throws GException {
		try {
			m_datatable = null;

			readData(new DBString(), p_sessionid);

			return m_datatable.size();

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public void readData(DBString p_dbstrPrimaryKeyValue, ISessionID p_sessionid) throws GException {
		if(p_dbstrPrimaryKeyValue.isValid()) {
			readData(new GList<>(p_dbstrPrimaryKeyValue), p_sessionid);

		} else {
			readData(new GList<>(), p_sessionid);
		}
	}

	public void readData(List<DBString> p_lsPrimaryKeyValues, ISessionID p_sessionid) throws GException {
		try {
			String strFieldPrimaryKeyName = m_dbDataTable.getFieldPrimaryKeyName();
			DataField datafield_PK = m_dbDataTable.getDataField(strFieldPrimaryKeyName);

			SQLStatement sqlStatement = SQLStatement.EmptyStatement;

			if(p_lsPrimaryKeyValues.size() > 0) {
				sqlStatement = Where.in(datafield_PK, p_lsPrimaryKeyValues);
			}

			String strSQLString_Select = m_dbDataTable.getSQLString_Select(sqlStatement, p_sessionid);

			GStatement stm = new GStatement(DBUtilities.getConnection(p_sessionid));
			GResultSet rst = stm.executeQuery(strSQLString_Select);

			if(m_datatable == null) {
				List<String> lsPrimaryKeys = new GList<>(strFieldPrimaryKeyName);

				m_datatable = new DataVectorTable(m_tableColumnModel, lsPrimaryKeys);
			}

			List<DataField<? extends DBObject>> lsDataField = m_dbDataTable.getDataFields();

			while(rst.next()) {
				DataVectorRow dvrRow = new DataVectorRow(m_tableColumnModel);

				for(DataField<? extends DBObject> datafield : lsDataField) {
					String strFieldName = datafield.getFieldName();
					Class<? extends DBObject> aClass = datafield.getClassData();
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

					dvrRow.setData(strFieldName, dboNewInstance);

					if(datafield.isIndex()) {
						m_dataindex.add(strFieldName, dboNewInstance, dvrRow.getPrimaryKeyValue(m_datatable));
					}
				}

				m_datatable.addDataRow(dvrRow);
			}

			StringBuilder builder = new StringBuilder();
			builder.append("Read ").append(m_dbDataTable.getTableName()).append(" have ").append(m_datatable.size()).append(" Rows");

			GLog.info(builder.toString());

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public void updateData(DBString p_dbstrPrimaryKeyValue, ISessionID p_sessionid) throws GException {
		try {
			m_datatable.deleteRow_NotVerify(p_dbstrPrimaryKeyValue);

			String strFieldPrimaryKeyName = m_dbDataTable.getFieldPrimaryKeyName();
			DataField datafield_PK = m_dbDataTable.getDataField(strFieldPrimaryKeyName);

			SQLStatement sqlStatement = Where.equal(datafield_PK, p_dbstrPrimaryKeyValue);

			String strSQLString_Select = m_dbDataTable.getSQLString_Select(sqlStatement, p_sessionid);

			GStatement stm = new GStatement(DBUtilities.getConnection(p_sessionid));
			GResultSet rst = stm.executeQuery(strSQLString_Select);

			if(m_datatable == null) {
				List<String> lsPrimaryKeys = new GList<>(strFieldPrimaryKeyName);

				m_datatable = new DataVectorTable(m_tableColumnModel, lsPrimaryKeys);
			}

			List<DataField<? extends DBObject>> lsDataField = m_dbDataTable.getDataFields();

			while(rst.next()) {
				DataVectorRow dvrRow = new DataVectorRow(m_tableColumnModel);

				for(DataField<? extends DBObject> datafield : lsDataField) {
					String strFieldName = datafield.getFieldName();
					Class<? extends DBObject> aClass = datafield.getClassData();
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

					dvrRow.setData(strFieldName, dboNewInstance);

					if(datafield.isIndex()) {
						m_dataindex.add(strFieldName, dboNewInstance, dvrRow.getPrimaryKeyValue(m_datatable));
					}
				}

				m_datatable.addDataRow(dvrRow);
			}

			StringBuilder builder = new StringBuilder();
			builder.append("Read ").append(m_dbDataTable.getTableName()).append(" have ").append(m_datatable.size()).append(" Rows");

			GLog.info(builder.toString());

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public boolean existing(DBString p_dbstrSourceID) throws GException {
		return m_datatable.hasRow(p_dbstrSourceID);
	}

	public DataVectorRow getDVRRow(DBString p_dbstrPrimaryKeyValue) throws GException {
		try {
			DataVectorRow dvrRow = m_datatable.getDataVectorRow(p_dbstrPrimaryKeyValue);

			return dvrRow;

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public DT getDBDataTable(DBString p_dbstrPrimaryKeyValue) throws GException {
		try {
			DataVectorRow dvrRow = getDVRRow(p_dbstrPrimaryKeyValue);
			DT datatable = m_clsDBDataTable.newInstance();
			datatable.setData(dvrRow);

			return datatable;

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public Set<DBString> getPrimaryKeyValues() throws GException {
		try {
			Set<DBString> setPrimaryKeyValues = m_datatable.getPrimaryKeyValues();

			return setPrimaryKeyValues;

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public<T extends DBObject> T getDataOnPrimaryKey(DBString p_dbstrPrimaryKeyValue, DataField<T> p_datafield) throws GException {
		return getDataOnPrimaryKey(p_dbstrPrimaryKeyValue, p_datafield.getClassData(), p_datafield.getFieldName());
	}

	public<T extends DBObject> T getDataOnPrimaryKey(DBString p_dbstrPrimaryKeyValue, Class<T> p_class, String p_strFieldName) throws GException {
		try {
			DataVectorRow dvrRow = getDVRRow(p_dbstrPrimaryKeyValue);

			return dvrRow.getDataAtColumnName(p_class, p_strFieldName);

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public void reloadData(ISessionID p_sessionid) throws GException {
		try {
			readData(p_sessionid);

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public void reloadData(ISessionID p_sessionid, DBString p_dbstrPrimaryKeyValue) throws GException {
		try {
			deleteData(p_dbstrPrimaryKeyValue);
			readData(new GList<>(p_dbstrPrimaryKeyValue), p_sessionid);

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public void deleteData(DBString p_dbstrPrimaryKeyValue) throws GException {
		try {
		  m_datatable.deleteRow_NotVerify(p_dbstrPrimaryKeyValue);

			StringBuilder builder = new StringBuilder();
			builder.append("Read ").append(m_dbDataTable.getTableName()).append(" have ").append(m_datatable.size()).append(" Rows");

			GLog.info(builder.toString());

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public void deleteData(List<DBString> p_lsPrimaryKeyValues) throws GException {
		try {
			for(DBString dbstrPrimaryKeyValue : p_lsPrimaryKeyValues) {
				m_datatable.deleteRow_NotVerify(dbstrPrimaryKeyValue);
			}

			StringBuilder builder = new StringBuilder();
			builder.append("Read ").append(m_dbDataTable.getTableName()).append(" have ").append(m_datatable.size()).append(" Rows");

			GLog.info(builder.toString());

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public Set<DBString> search(SQLStatement p_sqlStatement) throws GException {
		return DataVectorSearch.search(m_datatable, m_dataindex, p_sqlStatement);
	}

//	private DataVectorTable getDTV(Set<DBString> p_setPrimaryKeyValues) throws GException {
//		return getDTV(p_setPrimaryKeyValues, true);
//	}
//
//	private DataVectorTable getDTV(Set<DBString> p_setPrimaryKeyValues, boolean p_bolDoSortData) throws GException {
//		return getDTV(m_datatable, p_setPrimaryKeyValues, p_bolDoSortData);
//	}
//
//	private static DataVectorTable getDTV(DataVectorTable p_dvtDVT_ToGet, Set<DBString> p_setPrimaryKeyValues, boolean p_bolDoSortData) throws GException {
//		try {
//			DataVectorTable dvtReturn = new DataVectorTable(p_dvtDVT_ToGet.getDataTableColumnModel(), p_dvtDVT_ToGet.getColumnNamePrimaryKeys(),
//				p_dvtDVT_ToGet.getColumnNameSortKeys());
//
//			p_dvtDVT_ToGet.beforeFirst();
//
//			if(p_dvtDVT_ToGet.size() < p_setPrimaryKeyValues.size()) {
//				while(p_dvtDVT_ToGet.next()) {
//					PrimaryKeyValue primarykeyvalue = p_dvtDVT_ToGet.getCurrentPrimaryKeyValue();
//					DBString dbstrStringValue = new DBString(primarykeyvalue.getStringValue());
//
//					if(p_setPrimaryKeyValues.contains(dbstrStringValue)) {
//						DataVectorRow dvrRow = p_dvtDVT_ToGet.getDataRow(primarykeyvalue);
//						dvtReturn.addDataRow(dvrRow);
//					}
//				}
//			} else {
//				for(DBString dbstrPrimaryKeyValue : p_setPrimaryKeyValues) {
//					if(p_dvtDVT_ToGet.hasRow(dbstrPrimaryKeyValue)) {
//						DataVectorRow dvrRow = p_dvtDVT_ToGet.getDataRow(dbstrPrimaryKeyValue);
//						dvtReturn.addDataRow(dvrRow);
//					}
//				}
//			}
//
//			if(p_bolDoSortData) {
//				dvtReturn.sortData();
//			}
//
//			p_dvtDVT_ToGet.beforeFirst();
//
//			return dvtReturn;
//
//		} catch (Exception exception) {
//		  ExceptionHandler.display(exception);
//		  throw new GException(exception);
//		}
//	}
}
