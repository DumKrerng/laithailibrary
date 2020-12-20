package com.laithailibrary.serverlibrary.server.mock;

import java.util.*;
import com.laithailibrary.logger.*;
import com.laithailibrary.serverlibrary.server.datatable.*;
import com.laithailibrary.serverlibrary.server.dbcache.*;
import com.laithailibrary.serverlibrary.server.search.*;
import com.laithailibrary.sharelibrary.bean.*;
import com.laithailibrary.sharelibrary.collection.*;
import com.laithailibrary.sharelibrary.datatableview.*;
import com.laithailibrary.sharelibrary.db.dbobject.*;
import com.laithailibrary.sharelibrary.db.dbutilities.*;
import com.laithailibrary.sharelibrary.field.*;
import com.laithailibrary.sharelibrary.sqlstatement.*;
import com.laithailibrary.sharelibrary.util.*;
import exc.*;
import org.json.*;

public class MockDataTableQuery<T extends DBDataTable, U extends DataTableQuery<T>> {

	private DataVectorTable m_mockdata = null;
	private GCashDataIndex m_dataindex;

	private String m_strPrimaryKey_MAX = "";

	private T m_beanDBDataTable = null;
	private U m_datatablequery = null;
	private DataTableLogic<T> m_datatablelogic = null;

	private static final long serialVersionUID = 1;

	public MockDataTableQuery(T p_beanDBDataTable, U p_datatablequery, DataTableLogic<T> p_datatablelogic) throws GException {
		try {
			m_dataindex = new GCashDataIndex();

			m_beanDBDataTable = p_beanDBDataTable;
			m_datatablequery = p_datatablequery;
			m_datatablelogic = p_datatablelogic;

			List<DataField<? extends DBObject>> lsDataField = m_beanDBDataTable.getDataFields();
			DataTableColumnModel column = new DataTableColumnModel();

			for(DataField<? extends DBObject> datafield : lsDataField) {
				column.addColumn(datafield);
			}

			String strPrimaryKeyName = m_beanDBDataTable.getFieldPrimaryKey().getDataField().getFieldName();
			List<String> lsPrimaryKeyNames = new GList<>();
			lsPrimaryKeyNames.add(strPrimaryKeyName);

			m_mockdata = new DataVectorTable(column, lsPrimaryKeyNames);

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public void setMockData(DataVectorTable p_dvtMockData) throws GException {
		try {
			m_mockdata = new DataVectorTable(m_mockdata.getDataTableColumnModel(), m_mockdata.getColumnNamePrimaryKeys());

			p_dvtMockData.beforeFirst();

			while(p_dvtMockData.next()) {
				DataVectorRow dvrRow = p_dvtMockData.getDataRow();
				m_mockdata.addDataRow(dvrRow);
			}

			setIndexValue();

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public void setMockData(JSONArray p_jsonArray) throws GException {
		try {
			m_mockdata = new DataVectorTable(m_mockdata.getDataTableColumnModel(), m_mockdata.getColumnNamePrimaryKeys());
			m_mockdata.setData(p_jsonArray);

			setIndexValue();

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public void setMockData(String p_strJSONArray) throws GException {
		try {
			m_mockdata = new DataVectorTable(m_mockdata.getDataTableColumnModel(), m_mockdata.getColumnNamePrimaryKeys());
			m_mockdata.setData_FromJSONString(p_strJSONArray);

			setIndexValue();

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	private void setIndexValue() throws GException {
		try {
			List<DataField<? extends DBObject>> lsDataField = m_beanDBDataTable.getDataFields();
			Set<DataField<? extends DBObject>> setFieldIndex = new GSet<>();

			for(DataField<? extends DBObject> datafield : lsDataField) {
				if(datafield.isIndex()) {
					setFieldIndex.add(datafield);
				}
			}

			while(m_mockdata.next()) {
				DataVectorRow dvrRow = m_mockdata.getDataRow();
				PrimaryKeyValue primarykeyvalue = dvrRow.getPrimaryKeyValue(m_mockdata);

				if(m_strPrimaryKey_MAX.length() <= 0) {
					m_strPrimaryKey_MAX = primarykeyvalue.getStringValue();

				} else {
					if(m_strPrimaryKey_MAX.compareTo(primarykeyvalue.getStringValue()) < 0) {
						m_strPrimaryKey_MAX = primarykeyvalue.getStringValue();
					}
				}

				for(DataField<? extends DBObject> datafield : setFieldIndex) {
					m_dataindex.add(datafield.getFieldName(), dvrRow.getDataAtColumnName(datafield), primarykeyvalue);
				}
			}

			m_mockdata.beforeFirst();

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public <DBO extends DBObject> T getDBDataTable(DBO p_PrimaryKeyID) throws GException {
		try {
			PrimaryKeyValue primarykey = new PrimaryKeyValue(p_PrimaryKeyID);

			DataVectorRow dvrRow = m_mockdata.getDataRow(primarykey);
			T data = (T)m_beanDBDataTable.getClass().newInstance();
			data.setData(dvrRow);

			return data;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public List<T> getDBDataTables(SQLStatement p_statement) throws GException {
		try {
			Set<DBString> setPrimaryKeyValues = DataVectorSearch.search(m_mockdata, m_dataindex, p_statement);
			List<T> lsDatas = new GList<>();

			for(DBString dbstrValue : setPrimaryKeyValues) {
				DataVectorRow dvrRow = m_mockdata.getDataRow(dbstrValue);
				T data = (T)m_beanDBDataTable.getClass().newInstance();
				data.setData(dvrRow);

				lsDatas.add(data);
			}

			return lsDatas;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public DataVectorTable getDataVectorTable(SQLStatement p_statement, List<String> p_lsSortKeys, int p_intRowLimit, Sorting p_sorting) throws GException {
		try {
			Set<DBString> setValues = DataVectorSearch.search(m_mockdata, m_dataindex, p_statement);
			Set<PrimaryKeyValue> setPrimaryKeyValues = new GSet<>();
			int intRowCount = 0;

			for(DBString dbstrValue : setValues) {
				PrimaryKeyValue primarykeyvalue = new PrimaryKeyValue(dbstrValue);
				setPrimaryKeyValues.add(primarykeyvalue);

				intRowCount++;

				if(p_intRowLimit > 0) {
					if(intRowCount >= p_intRowLimit) {
						break;
					}
				}
			}

			return m_mockdata.subDataVectorTable(setPrimaryKeyValues, p_lsSortKeys, p_sorting);

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public DBString baseInsertData(T p_beanDBDataTable, ExtraBean p_extrabean, DoVerifyData p_doverifydata, DoRunBefore p_dorunbefore,
		DoRunAfter p_dorunafter) throws GException {

		try {
			prepareDataOnInsert(p_beanDBDataTable, p_extrabean);

			if(p_doverifydata == DoVerifyData.Yes) {
				verifyDataOnInsert(p_beanDBDataTable, p_extrabean);
			}

			if(p_dorunbefore == DoRunBefore.Yes) {
				beforeOnInsert(p_beanDBDataTable, p_extrabean);
			}

			p_beanDBDataTable.setRecordUpdated(DBDateTime.getCurrentDateTime());

			verifyFieldIsMandatory(p_beanDBDataTable);
			verifyUniqueConstrain(p_beanDBDataTable);

			m_strPrimaryKey_MAX = m_strPrimaryKey_MAX.concat("_MC");

			if(p_dorunafter == DoRunAfter.Yes) {
				afterOnInsert(p_beanDBDataTable, p_extrabean);
			}

			return new DBString(m_strPrimaryKey_MAX);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public DBString updateData(T p_dbDataTable_Original, T p_dbDataTable_Edited, ExtraBean p_extrabean, DoVerifyData p_doverifydata,
		DoRunBefore p_dorunbefore, DoRunAfter p_dorunafter) throws GException {

		try {
			prepareDataOnUpdate(p_dbDataTable_Original, p_dbDataTable_Edited, p_extrabean);

			if(p_doverifydata == DoVerifyData.Yes) {
				verifyDataOnUpdate(p_dbDataTable_Original, p_dbDataTable_Edited, p_extrabean);
			}

			if(p_dorunbefore == DoRunBefore.Yes) {
				beforeOnUpdate(p_dbDataTable_Original, p_dbDataTable_Edited, p_extrabean);
			}

			verifyFieldIsMandatory(p_dbDataTable_Edited);
			verifyUniqueConstrain_OnUpdate(p_dbDataTable_Edited);

			DataField dfdPrimaryKey = p_dbDataTable_Original.getFieldPrimaryKey().getDataField();

			DBString dbstrPrimaryKeyValue = (DBString)p_dbDataTable_Original.getData(dfdPrimaryKey);

			List<DataField<? extends DBObject>> lsDataFields = m_beanDBDataTable.getDataFields();
			DataVectorRow dvrRow = m_mockdata.getDataRow(dbstrPrimaryKeyValue);

			for(DataField<? extends DBObject> datafield : lsDataFields) {
				if(datafield.getFieldName().compareTo(dfdPrimaryKey.getFieldName()) != 0) {
					dvrRow.setData(datafield.getFieldName(), p_dbDataTable_Edited.getData(datafield.getFieldName()));
				}
			}

			if(p_dorunafter == DoRunAfter.Yes) {
				afterOnUpdate(p_dbDataTable_Original, p_dbDataTable_Edited, p_extrabean);
			}

			return dbstrPrimaryKeyValue;

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public void baseDeleteData(T p_beanDBDataTable, ExtraBean p_extrabean, DoVerifyData p_doverifydata, DoRunBefore p_dorunbefore,
		DoRunAfter p_dorunafter) throws GException {

		try {
			GLog.info("Deleting Data at ".concat(m_beanDBDataTable.getTableName().getTableName()).concat(" . . ."));

			prepareDataOnDelete(p_beanDBDataTable, p_extrabean);

			if(p_doverifydata == DoVerifyData.Yes) {
				verifyDataOnDelete(p_beanDBDataTable, p_extrabean);
			}

			if(p_dorunbefore == DoRunBefore.Yes) {
				beforeOnDelete(p_beanDBDataTable, p_extrabean);
			}

			String strFieldPrimaryKey = m_beanDBDataTable.getFieldPrimaryKeyName();
			DBObject dboPrimaryKeyValue = p_beanDBDataTable.getData(strFieldPrimaryKey);

			m_mockdata.deleteRow(new DBString(dboPrimaryKeyValue));

			if(p_dorunafter == DoRunAfter.Yes) {
				afterOnDelete(p_beanDBDataTable, p_extrabean);
			}

			GLog.info("Delete Data at ".concat(m_beanDBDataTable.getTableName().getTableName()).concat("(ID: ").concat(dboPrimaryKeyValue.getString()).concat(") completed."));

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

	private void verifyUniqueConstrain(DBDataTable p_dbDataTable) throws GException {
		try {
			Map<String, FieldUnique> mapFieldUnique_Name = p_dbDataTable.getMapFieldUnique_Name();

			for(String strName : mapFieldUnique_Name.keySet()) {
				FieldUnique fieldUnique = mapFieldUnique_Name.get(strName);
				List<? extends DataField> lsDataFields = fieldUnique.getDataFields();

				SQLStatement sqlStatement = SQLStatement.EmptyStatement;

				for(DataField datafield : lsDataFields) {
					DBObject dboValue = p_dbDataTable.getData(datafield);

					sqlStatement = sqlStatement.and(Where.equal(datafield, dboValue));
				}

				Set<DBString> setPrimaryKeyValues = DataVectorSearch.search(m_mockdata, m_dataindex, sqlStatement);

				if(setPrimaryKeyValues.size() > 0) {
					throw new GException("Data is duplicate!!!");
				}
			}
		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	private void verifyUniqueConstrain_OnUpdate(DBDataTable p_dbDataTable) throws GException {
		try {
			Map<String, FieldUnique> mapFieldUnique_Name = p_dbDataTable.getMapFieldUnique_Name();

			for(String strName : mapFieldUnique_Name.keySet()) {
				FieldUnique fieldUnique = mapFieldUnique_Name.get(strName);
				List<? extends DataField> lsDataFields = fieldUnique.getDataFields();

				SQLStatement sqlStatement = SQLStatement.EmptyStatement;

				for(DataField datafield : lsDataFields) {
					DBObject dboValue = p_dbDataTable.getData(datafield);

					sqlStatement = sqlStatement.and(Where.equal(datafield, dboValue));
				}

				Set<DBString> setPrimaryKeyValues = DataVectorSearch.search(m_mockdata, m_dataindex, sqlStatement);

				if(setPrimaryKeyValues.size() > 1) {
					throw new GException("Data is duplicate!!!");
				}
			}
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
}
