package com.laithailibrary.sharelibrary.datatableview;

import javax.swing.table.*;
import java.io.*;
import java.util.*;
import com.laithailibrary.sharelibrary.collection.*;
import com.laithailibrary.sharelibrary.compressdata.*;
import com.laithailibrary.sharelibrary.db.dbobject.*;
import com.laithailibrary.sharelibrary.field.*;
import exc.*;
import org.json.*;

/**
 * Created by IntelliJ IDEA.
 * User: dumkrerng
 * Date: 6/6/11
 * Time: 11:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class DataVectorTable implements Externalizable {

	private DataTableColumnModel m_dataTableColumnModel;
	private Map<PrimaryKeyValue, DataVectorRow> m_mapDataVectorTableRow_PrimaryKeyValue = new GMap<>();
	private Set<PrimaryKeyValue> m_setPrimaryKeyValues = new GSet<>();
	private Map<SortKeyValue, PrimaryKeyValue> m_mapPrimaryKeyValue_SortKeyValue = new GMap<>();
	private Map<PrimaryKeyValue, SortKeyValue> m_mapSortKeyValue_PrimaryKeyValue = new GMap<>();
	private List<SortKeyValue> m_lsSortKeyValues = new GList<>();

	private List<String> m_lsColumnNamePrimaryKeys;
	private List<String> m_lsColumnNameSortKeys;
	private List<String> m_lsReturnColumnPrimaryKeys = new GList<>();

	private String m_ColumnSelectionDisplayValue = "";

	private CompressData m_compressdata = null;

	private Sorting m_sorting = Sorting.Ascending;
	private int m_intIndexRow = -1;

	private boolean m_bolSorted = false;

	private static final long serialVersionUID = 14;

	public DataVectorTable() {}

	public DataVectorTable(DataTableColumnModel p_dataTableColumnModel) throws GException {
		init(p_dataTableColumnModel, new GList<>(), new GList<>(), Sorting.Ascending);
	}

	public DataVectorTable(DataTableColumnModel p_dataTableColumnModel, List<String> p_lsColumnNamePrimaryKeys) throws GException {
		init(p_dataTableColumnModel, p_lsColumnNamePrimaryKeys, new GList<>(), Sorting.Ascending);
	}

	public DataVectorTable(DataTableColumnModel p_dataTableColumnModel, List<String> p_lsColumnNamePrimaryKeys, List<String> p_lsColumnNameSortKeys) throws GException {
		init(p_dataTableColumnModel, p_lsColumnNamePrimaryKeys, p_lsColumnNameSortKeys, Sorting.Ascending);
	}

	public DataVectorTable(DataTableColumnModel p_dataTableColumnModel, Sorting p_sorting) throws GException {
		init(p_dataTableColumnModel, new GList<>(), new GList<>(), p_sorting);
	}

	public DataVectorTable(DataTableColumnModel p_dataTableColumnModel, List<String> p_lsColumnNamePrimaryKeys, List<String> p_lsColumnNameSortKeys,
		Sorting p_sorting) throws GException {

		init(p_dataTableColumnModel, p_lsColumnNamePrimaryKeys, p_lsColumnNameSortKeys, p_sorting);
	}

	public void init(DataTableColumnModel p_dataTableColumnModel, List<String> p_lsColumnNamePrimaryKey, List<String> p_lsColumnNameSortKey,
		Sorting p_sorting) throws GException {

		try {
			m_dataTableColumnModel = p_dataTableColumnModel;
			m_mapDataVectorTableRow_PrimaryKeyValue = new GMap<>();
			m_setPrimaryKeyValues = new GSet<>();
			m_mapPrimaryKeyValue_SortKeyValue = new GMap<>();
			m_mapSortKeyValue_PrimaryKeyValue = new GMap<>();
			m_lsSortKeyValues = new GList<>();
			m_sorting = p_sorting;

			m_lsColumnNamePrimaryKeys = p_lsColumnNamePrimaryKey;
			m_lsColumnNameSortKeys = p_lsColumnNameSortKey;
			m_lsReturnColumnPrimaryKeys = new GList<>();

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public void writeExternal(ObjectOutput out) throws IOException {
		try {
			for(PrimaryKeyValue primarykeyvalue : m_mapDataVectorTableRow_PrimaryKeyValue.keySet()) {
				m_mapDataVectorTableRow_PrimaryKeyValue.get(primarykeyvalue).setCompressData(null);
			}

			if(!m_bolSorted) {
				sortData();
			}

			out.writeObject(m_dataTableColumnModel);
			out.writeObject(m_mapDataVectorTableRow_PrimaryKeyValue);
			out.writeObject(m_setPrimaryKeyValues);
			out.writeObject(m_mapPrimaryKeyValue_SortKeyValue);
			out.writeObject(m_mapSortKeyValue_PrimaryKeyValue);
			out.writeObject(m_lsSortKeyValues);
			out.writeObject(m_sorting);

			out.writeObject(m_lsColumnNamePrimaryKeys);
			out.writeObject(m_lsColumnNameSortKeys);
			out.writeObject(m_lsReturnColumnPrimaryKeys);

			out.writeObject(m_compressdata);

			out.writeObject(m_ColumnSelectionDisplayValue);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			out.close();

			throw new IOException(exception);
		}
	}

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		try {
			m_dataTableColumnModel = (DataTableColumnModel)in.readObject();
			m_mapDataVectorTableRow_PrimaryKeyValue = (Map<PrimaryKeyValue, DataVectorRow>)in.readObject();
			m_setPrimaryKeyValues = (Set<PrimaryKeyValue>)in.readObject();
			m_mapPrimaryKeyValue_SortKeyValue = (Map<SortKeyValue, PrimaryKeyValue>)in.readObject();
			m_mapSortKeyValue_PrimaryKeyValue = (Map<PrimaryKeyValue, SortKeyValue>)in.readObject();
			m_lsSortKeyValues = (List<SortKeyValue>)in.readObject();
			m_sorting = (Sorting)in.readObject();

			m_lsColumnNamePrimaryKeys = (List<String>)in.readObject();
			m_lsColumnNameSortKeys = (List<String>)in.readObject();
			m_lsReturnColumnPrimaryKeys = (List<String>)in.readObject();

			m_compressdata = (CompressData)in.readObject();

			m_ColumnSelectionDisplayValue = (String)in.readObject();

			for(PrimaryKeyValue primarykeyvalue : m_mapDataVectorTableRow_PrimaryKeyValue.keySet()) {
				m_mapDataVectorTableRow_PrimaryKeyValue.get(primarykeyvalue).setCompressData(m_compressdata);
			}

			m_bolSorted = true;

		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			in.close();

			throw new IOException(exception);
		}
	}

	public void addDataRow(DataVectorRow p_dvrRow) throws GException {
		try {
			m_bolSorted = false;

			if(m_compressdata == null) {
				m_compressdata = p_dvrRow.getCompressData();

			} else {
				p_dvrRow.doCombineCompressData(m_compressdata);
			}

			DataTableColumnModel dtcColumnModel = p_dvrRow.getDataTableColumnModel();

			if(m_dataTableColumnModel.compareTo(dtcColumnModel) != 0) {
				throw new GException("DataTableColumnModel not same!!!");
			}

			int intColumnModelCount = m_dataTableColumnModel.getColumnCount();
			int intColumnDataCount = p_dvrRow.getColumnDataCount();

			if(intColumnModelCount != intColumnDataCount) {
				throw new GException("ColumnModelCount: " + intColumnModelCount + "\nColumnDataCount: " + intColumnDataCount);
			}

			PrimaryKeyValue primarykeyvalue = new PrimaryKeyValue();

			if(m_lsColumnNamePrimaryKeys.size() > 0) {
				for(String strColumnNamePrimaryKey : m_lsColumnNamePrimaryKeys) {
					DBObject dboPrimaryKeyValue = p_dvrRow.getDataAtColumnName(strColumnNamePrimaryKey);
					primarykeyvalue.add(dboPrimaryKeyValue);
				}
			} else {
				int intRowCount = getRowCount();

				primarykeyvalue.add(new DBInteger(intRowCount));
			}

			if(m_setPrimaryKeyValues.contains(primarykeyvalue)) {
				throw new GException("Duplicate PrimaryKeyValue!\nPrimaryValue: " + primarykeyvalue.getString());

			} else {
				m_setPrimaryKeyValues.add(primarykeyvalue);
			}

			m_mapDataVectorTableRow_PrimaryKeyValue.put(primarykeyvalue, p_dvrRow);

			SortKeyValue sortkeyvalue = new SortKeyValue();

			if(m_lsColumnNameSortKeys.size() > 0) {
				for(String strColumnNameSortKey : m_lsColumnNameSortKeys) {
					DBObject dboSortKeyValue = p_dvrRow.getDataAtColumnName(strColumnNameSortKey);
					sortkeyvalue.add(dboSortKeyValue);
				}
			}

			if(m_lsColumnNamePrimaryKeys.size() > 0) {
				sortkeyvalue.add(new DBString(primarykeyvalue.getString()));

				if(m_mapPrimaryKeyValue_SortKeyValue.containsKey(sortkeyvalue)) {
					ErrorDuplicateSortKey(m_lsColumnNameSortKeys, sortkeyvalue);
				}

				m_mapPrimaryKeyValue_SortKeyValue.put(sortkeyvalue, primarykeyvalue);
				m_mapSortKeyValue_PrimaryKeyValue.put(primarykeyvalue, sortkeyvalue);
				m_lsSortKeyValues.add(sortkeyvalue);
			}
		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public void setData_FromJSONString(String p_strJSONArray) throws GException {
		try {
			setData(new JSONArray(p_strJSONArray));

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public void setData(JSONArray p_json) throws GException {
		try {
			m_mapDataVectorTableRow_PrimaryKeyValue = new GMap<>();
			m_setPrimaryKeyValues = new GSet<>();
			m_mapPrimaryKeyValue_SortKeyValue = new GMap<>();
			m_mapSortKeyValue_PrimaryKeyValue = new GMap<>();
			m_lsSortKeyValues = new GList<>();

			m_compressdata = null;

			int intLength = p_json.length();

			for(int index=0; index < intLength; index++) {
				JSONObject json = p_json.getJSONObject(index);

				DataVectorRow dvrRow = new DataVectorRow(m_dataTableColumnModel);
				dvrRow.setData(json);

				addDataRow(dvrRow);
			}
		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public void resetCompressData() throws GException {
		try {
			if(m_compressdata != null) {
				CompressData compressdata_New = new CompressData();

				int intColumnCount = m_dataTableColumnModel.getColumnCount();

				this.beforeFirst();

				while(this.next()) {
					DataVectorRow dvrRow =  getDataRow();
					Map<Integer, Integer> mapCompressKey_ColumnIndex = new GMap<>();

					for(int index=0; index < intColumnCount; index++) {
						String strValue = dvrRow.getDataAtColumnIndex(index).getString();
						int intCompressKey = compressdata_New.addValue(strValue);

						mapCompressKey_ColumnIndex.put(index, intCompressKey);
					}

					dvrRow.setMapCompressKey_ColumnIndex(mapCompressKey_ColumnIndex);
					dvrRow.setCompressData(compressdata_New);
				}

				m_compressdata = null;
				m_compressdata = compressdata_New;

				this.beforeFirst();
			}
		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public void setColumnSelectionDisplayValue(String p_strColumnName) {
		m_ColumnSelectionDisplayValue = p_strColumnName;
	}

	public String getColumnSelectionDisplayValue() {
		return m_ColumnSelectionDisplayValue;
	}

	public void setColumnPrimaryKeys(List<String> p_lsColumnNames) throws GException {
		try {
			if(p_lsColumnNames.size() > 0) {
				m_lsColumnNamePrimaryKeys = new GList<>();
				m_lsColumnNameSortKeys = new GList<>();

				for(String strColumnName : p_lsColumnNames) {
					addColumnPrimaryKey(strColumnName);
				}
			}
		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	private void addColumnPrimaryKey(String p_strColumnName) throws GException {
		m_dataTableColumnModel.verifyContainsColumn(p_strColumnName);

		if(m_lsColumnNamePrimaryKeys.contains(p_strColumnName)) {
			throw new GException("Duplicate column PrimaryKey: " + p_strColumnName);
		}

		m_lsColumnNamePrimaryKeys.add(p_strColumnName);
		addColumnSortKey(p_strColumnName);
	}

	public List<String> getColumnNamePrimaryKeys() {
		return m_lsColumnNamePrimaryKeys;
	}

	public void setColumnSortKeys(List<String> p_lsColumnNames) throws GException {
		try {
			if(p_lsColumnNames.size() > 0) {
				m_lsColumnNameSortKeys = new GList<>();

				for(String strColumnName : p_lsColumnNames) {
					addColumnSortKey(strColumnName);
				}

				for(String strColumnName : m_lsColumnNamePrimaryKeys) {
					if(!m_lsColumnNameSortKeys.contains(strColumnName)) {
						addColumnSortKey(strColumnName);
					}
				}

				resortData();
			}
		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	private void addColumnSortKey(String p_strColumnName) throws GException {
		m_dataTableColumnModel.verifyContainsColumn(p_strColumnName);

		if(m_lsColumnNameSortKeys.contains(p_strColumnName)) {
			throw new GException("Duplicate column SortKey: " + p_strColumnName);
		}

		m_lsColumnNameSortKeys.add(p_strColumnName);
	}

	public List<String> getColumnNameSortKeys() {
		return m_lsColumnNameSortKeys;
	}

	public void addReturnColumnPrimaryKey(DataField p_datafield) {
		addReturnColumnPrimaryKey(p_datafield.getFieldName());
	}

	public void addReturnColumnPrimaryKey(String p_strColumnName) {
		m_lsReturnColumnPrimaryKeys.add(p_strColumnName);
	}

	public List<String> getReturnColumnPrimaryKeys() {
		return m_lsReturnColumnPrimaryKeys;
	}

	public void beforeFirst() {
		m_intIndexRow = -1;
	}

	public boolean next() {
		int intRowCount = getRowCount();

		m_intIndexRow++;

		if(m_intIndexRow < intRowCount) {
			return true;

		} else {
			return false;
		}
	}

	public DataVectorRow getDataRow() throws GException {
		try {
			if(!m_bolSorted) {
				sortData();
			}

			DataVectorRow dvrResult;

			int intRowCount = getRowCount();

			if(m_intIndexRow == -1) {
				throw new GException("Row index -1!");

			} else if(m_intIndexRow >= intRowCount) {
				throw new GException("Out of index\nIndex: " + m_intIndexRow + "\nSize: " + intRowCount);

			} else {
				dvrResult = getDataRow(m_intIndexRow);

				return dvrResult;
			}
		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public PrimaryKeyValue getCurrentPrimaryKeyValue() throws GException {
		try {
			if(m_lsColumnNamePrimaryKeys.size() <= 0) {
				throw new GException("Not-Existing Column PrimaryKey!");
			}

			DataVectorRow dvtRow = getDataRow();

			PrimaryKeyValue primarykeyvalue = new PrimaryKeyValue();

			for(String strColumnNamePrimaryKey : m_lsColumnNamePrimaryKeys) {
				DBObject dboValue = dvtRow.getDataAtColumnName(strColumnNamePrimaryKey);
				primarykeyvalue.add(dboValue);
			}

			return primarykeyvalue;

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public DataVectorRow getDataRow(DBString p_dbstrPrimaryKeyValue) throws GException {
		try {
			PrimaryKeyValue primarykeyvalue = new PrimaryKeyValue(p_dbstrPrimaryKeyValue);

			return getDataRow(primarykeyvalue);

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public DataVectorRow getDataRow(PrimaryKeyValue p_primarykeyvalue) throws GException {
		try {
			if(m_lsColumnNamePrimaryKeys.size() <= 0) {
				throw new GException("Not-Existing Column PrimaryKey!");
			}

			if(!m_mapDataVectorTableRow_PrimaryKeyValue.containsKey(p_primarykeyvalue)) {
				throw new GException("Not-Existing PrimaryKeyValue: " + p_primarykeyvalue.getStringValue());
			}

			return m_mapDataVectorTableRow_PrimaryKeyValue.get(p_primarykeyvalue);

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public DataVectorRow getDataRow(int p_intRowIndex) throws GException {
		try {
			int intRowCount = getRowCount();

			if(p_intRowIndex >= intRowCount) {
				throw new GException("Out of index\nIndex: " + p_intRowIndex + "\nSize: " + intRowCount);
			}

			DataVectorRow dvtRowResult;

			if(m_lsSortKeyValues.size() > 0) {
				SortKeyValue sortkeyvalue = m_lsSortKeyValues.get(p_intRowIndex);
				PrimaryKeyValue primarykeyview = m_mapPrimaryKeyValue_SortKeyValue.get(sortkeyvalue);
				dvtRowResult = m_mapDataVectorTableRow_PrimaryKeyValue.get(primarykeyview);

			} else {
				dvtRowResult = (DataVectorRow)m_mapDataVectorTableRow_PrimaryKeyValue.values().toArray()[p_intRowIndex];
			}

			if(dvtRowResult == null) {
				throw new GException("DataVectorRow is null!");
			}

			return dvtRowResult;

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public boolean hasRow(DBString p_dbstrPrimaryKeyValue) throws GException {
		return hasRow(new PrimaryKeyValue(p_dbstrPrimaryKeyValue));
	}

	public boolean hasRow(PrimaryKeyValue p_primarykeyvalue) throws GException {
		try {
			if(m_mapDataVectorTableRow_PrimaryKeyValue.containsKey(p_primarykeyvalue)) {
				return true;
			}

			return false;

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public void verifyPrimaryKey(DBObject p_dboPrimaryKeyValue) throws GException {
		verifyPrimaryKey(new PrimaryKeyValue(p_dboPrimaryKeyValue));
	}

	public void verifyPrimaryKey(PrimaryKeyValue p_primarykeyvalue) throws GException {
		if(!hasRow(p_primarykeyvalue)) {
			throw new GException("Non-existing PrimaryKeyValue: " + p_primarykeyvalue.getStringValue() + "!");
		}
	}

	public Set<DBString> getPrimaryKeyValues() throws GException {
		try {
		  Set<PrimaryKeyValue> setPrimaryKeyValues = m_mapDataVectorTableRow_PrimaryKeyValue.keySet();
			Set<DBString> setPrimaryKeyValues_Return = new GSet<>();

			for(PrimaryKeyValue primarykeyview : setPrimaryKeyValues) {
				DBObject dboValue = primarykeyview.get(0);

				setPrimaryKeyValues_Return.add(new DBString(dboValue));
			}

			return setPrimaryKeyValues_Return;

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public List<String> getSortKeyValues() throws GException {
		try {
			List<String> lsSortKeyValues = new GList<>();

		  for(SortKeyValue sortkeyvalue : m_lsSortKeyValues) {
				String strValue = sortkeyvalue.getString();

				lsSortKeyValues.add(strValue);
			}

			return lsSortKeyValues;

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public DataVectorRow getDataVectorRow(DBObject p_dboPrimaryKeyValue) throws GException {
		try {
			verifyPrimaryKey(p_dboPrimaryKeyValue);

			DataVectorRow datavectorrow = new DataVectorRow(m_dataTableColumnModel);
			PrimaryKeyValue primarykeyview = new PrimaryKeyValue(p_dboPrimaryKeyValue);

			if(m_mapDataVectorTableRow_PrimaryKeyValue.containsKey(primarykeyview)) {
				datavectorrow = m_mapDataVectorTableRow_PrimaryKeyValue.get(primarykeyview);
			}

			return datavectorrow;

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public DataVectorRow getDataVectorRow(PrimaryKeyValue p_primarykeyvalue) throws GException {
		try {
			verifyPrimaryKey(p_primarykeyvalue);

			DataVectorRow datavectorrow = new DataVectorRow(m_dataTableColumnModel);

			if(m_mapDataVectorTableRow_PrimaryKeyValue.containsKey(p_primarykeyvalue)) {
				datavectorrow = m_mapDataVectorTableRow_PrimaryKeyValue.get(p_primarykeyvalue);
			}

			return datavectorrow;

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public void deleteAllRow() throws GException {
		try {
			m_mapDataVectorTableRow_PrimaryKeyValue = new GMap<>();
			m_setPrimaryKeyValues = new GSet<>();
			m_mapPrimaryKeyValue_SortKeyValue = new GMap<>();
			m_lsSortKeyValues = new GList<>();

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public void deleteRow(DBString p_dbstrPrimaryKeyValue) throws GException {
		deleteRow(p_dbstrPrimaryKeyValue, true);
	}

	public void deleteRow_NotVerify(DBString p_dbstrPrimaryKeyValue) throws GException {
		deleteRow(p_dbstrPrimaryKeyValue, false);
	}

	private void deleteRow(DBString p_dbstrPrimaryKeyValue, boolean p_bolVerifyPrimaryKey) throws GException {
		try {
			if(p_bolVerifyPrimaryKey) {
				verifyPrimaryKey(p_dbstrPrimaryKeyValue);
			}

			PrimaryKeyValue primarykeyview = new PrimaryKeyValue(p_dbstrPrimaryKeyValue);

			if(m_mapSortKeyValue_PrimaryKeyValue.containsKey(primarykeyview)) {
				SortKeyValue sortkeyvalue = m_mapSortKeyValue_PrimaryKeyValue.get(primarykeyview);
				PrimaryKeyValue primarykeyview_Temp = m_mapPrimaryKeyValue_SortKeyValue.get(sortkeyvalue);

				m_mapPrimaryKeyValue_SortKeyValue.remove(sortkeyvalue);
				m_lsSortKeyValues.remove(sortkeyvalue);

				m_mapDataVectorTableRow_PrimaryKeyValue.remove(primarykeyview_Temp);
				m_mapSortKeyValue_PrimaryKeyValue.remove(primarykeyview_Temp);
				m_setPrimaryKeyValues.remove(primarykeyview_Temp);

			} else {
				throw new GException("Invalid PrimaryKey!");
			}
		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public DataTableColumnModel getDataTableColumnModel() throws GException {
		return m_dataTableColumnModel;
	}
	
	public Integer getRowNumber(PrimaryKeyValue p_pkView) throws GException {
		try {
			if(p_pkView.getStringValue().length() > 0) {
				SortKeyValue sortkeyvalue = m_mapSortKeyValue_PrimaryKeyValue.get(p_pkView);
				int intRowNumber = m_lsSortKeyValues.indexOf(sortkeyvalue);

				return Integer.valueOf(intRowNumber);
			}

			return -1;

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public int size() {
		return getRowCount();
	}

	public int getRowCount() {
		return m_setPrimaryKeyValues.size();
	}

	public GTableModel getTableModel() throws GException {
		try {
			GTableModel tablemodel = new GTableModel(m_dataTableColumnModel);

			List<TableColumn> lsTableColumn = m_dataTableColumnModel.getTableColumns();

			for(TableColumn tableColumn : lsTableColumn) {
				tablemodel.addColumn(tableColumn.getHeaderValue());
			}

			beforeFirst();

			while(next()) {
				DataVectorRow dvrRow = getDataRow();
				tablemodel.addRow(dvrRow.getDataVector());
			}

			beforeFirst();

			return tablemodel;

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public String getCSVHeader() throws GException {
		try {
			DataTableColumnModel dtcColumn = getDataTableColumnModel();

			int intColumnCount = dtcColumn.getColumnCount();
			int intColumnIndex;

			StringBuilder builder = new StringBuilder();

			for(intColumnIndex = 0; intColumnIndex < intColumnCount; intColumnIndex++) {
				DataTableColumnModel.ShowInCSV showincsv = dtcColumn.getShowInCSV(intColumnIndex);
				TableColumn tablecolumn = dtcColumn.getTableColumn(intColumnIndex);

				if(showincsv == DataTableColumnModel.ShowInCSV.Yes) {
					builder.append(tablecolumn.getHeaderValue().toString().trim()).append(";");
				}
			}

			return builder.toString();

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public JSONArray toJSONArray(List<? extends DataField> p_lsDataFields) throws GException {
		try {
			JSONArray jsonArray = new JSONArray();
			beforeFirst();

			while(next()) {
				DataVectorRow dvrRow = getDataRow();
				JSONObject jsonObject = dvrRow.toJSON(p_lsDataFields);
				jsonArray.put(jsonObject);
			}

			beforeFirst();

			return jsonArray;

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public JSONArray toJSONArray(Set<String> p_lsColumnNames) throws GException {
		try {
			JSONArray jsonArray = new JSONArray();
			beforeFirst();

			while(next()) {
				DataVectorRow dvrRow = getDataRow();
				JSONObject jsonObject = dvrRow.toJSON(p_lsColumnNames);
				jsonArray.put(jsonObject);
			}

			beforeFirst();

			return jsonArray;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public JSONArray toJSONArray() throws GException {
		try {
			JSONArray jsonArray = new JSONArray();
			beforeFirst();

			while(next()) {
				DataVectorRow dvrRow = getDataRow();
				JSONObject jsonObject = dvrRow.toJSON();
				jsonArray.put(jsonObject);
			}

			beforeFirst();

			return jsonArray;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public String toJSONArrayString(List<? extends DataField> p_lsDataFields) throws GException {
		try {
			JSONArray jsonArray = toJSONArray(p_lsDataFields);

			return jsonArray.toString();

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public String toJSONArrayString(Set<String> p_lsColumnNames) throws GException {
		try {
			JSONArray jsonArray = toJSONArray(p_lsColumnNames);

			return jsonArray.toString();

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public String toJSONArrayString() throws GException {
		try {
			JSONArray jsonArray = toJSONArray();

			return jsonArray.toString();

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public DataVectorTable cloneDataVectorTable()throws GException {
		try {
			DataVectorTable dtvReturn = new DataVectorTable(m_dataTableColumnModel, m_lsColumnNamePrimaryKeys, m_lsColumnNameSortKeys);

			this.beforeFirst();

			while(this.next()) {
				DataVectorRow dvrRow = this.getDataRow();
				dtvReturn.addDataRow(dvrRow);
			}

			dtvReturn.sortData();
			beforeFirst();

			return dtvReturn;

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public DataVectorTable subDataVectorTable(DataTableColumnModel p_columnmodel, List<Integer> p_lsRowIndex, List<String> p_lsColumnNameSortKeys,
		Sorting p_sorting) throws GException {

		try {
			for(String strColumnNamePrimaryKey : m_lsColumnNamePrimaryKeys) {
				if(!p_columnmodel.containsColumnName(strColumnNamePrimaryKey)) {
					int intColumnIndex = getDataTableColumnModel().getColumnIndex(strColumnNamePrimaryKey);
					Class clsColumnClass = getDataTableColumnModel().getColumnClass(intColumnIndex);

					p_columnmodel.addColumn(strColumnNamePrimaryKey, clsColumnClass);
				}
			}

			DataVectorTable dvtReturn = new DataVectorTable(p_columnmodel, m_lsColumnNamePrimaryKeys, p_lsColumnNameSortKeys, p_sorting);

			Map<String, Integer> mapColumnIndex_ColumnName = p_columnmodel.getMapColumnIndex_ColumnName();
			Set<Map.Entry<String, Integer>> setColumnIndex = mapColumnIndex_ColumnName.entrySet();

			for(int intRowIndex : p_lsRowIndex) {
				DataVectorRow dvrRow_Original = getDataRow(intRowIndex);
				DataVectorRow dvrRow = new DataVectorRow(p_columnmodel);

				for(Map.Entry<String, Integer> entColumnIndex : setColumnIndex) {
					int intColumnIndex = entColumnIndex.getValue();

					dvrRow.setData(intColumnIndex, dvrRow_Original.getDataAtColumnIndex(intColumnIndex));
				}

				dvtReturn.addDataRow(dvrRow);
			}

			dvtReturn.resortData();

			return dvtReturn;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public DataVectorTable subDataVectorTable(Set<PrimaryKeyValue> p_setPrimaryKeyValue, List<String> p_lsColumnNameSortKeys,
		Sorting p_sorting) throws GException {

		try {
			DataVectorTable dvtReturn = new DataVectorTable(m_dataTableColumnModel, m_lsColumnNamePrimaryKeys, p_lsColumnNameSortKeys, p_sorting);

			Map<String, Integer> mapColumnIndex_ColumnName = m_dataTableColumnModel.getMapColumnIndex_ColumnName();
			Set<Map.Entry<String, Integer>> setColumnIndex = mapColumnIndex_ColumnName.entrySet();

			for(PrimaryKeyValue primarykeyvalue : p_setPrimaryKeyValue) {
				DataVectorRow dvrRow_Original = getDataRow(primarykeyvalue);
				DataVectorRow dvrRow = new DataVectorRow(m_dataTableColumnModel);

				for(Map.Entry<String, Integer> entColumnIndex : setColumnIndex) {
					int intColumnIndex = entColumnIndex.getValue();

					dvrRow.setData(intColumnIndex, dvrRow_Original.getDataAtColumnIndex(intColumnIndex));
				}

				dvtReturn.addDataRow(dvrRow);
			}

			dvtReturn.resortData();

			return dvtReturn;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public Sorting getSorting() {
		return m_sorting;
	}

	private static void ErrorDuplicateSortKey(List<String> p_lsColumnNameSortKeys, SortKeyValue p_sortkeyvalue) throws GException {
		try {
			StringBuilder builder = new StringBuilder();
			builder.append("Duplicate SortKey!\n");
			builder.append("Column: ").append(p_lsColumnNameSortKeys.toString()).append('\n');
			builder.append("Value: ").append(p_sortkeyvalue.getStringValue()).append('\n');

			throw new GException(builder.toString());

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public void sortData() throws GException {
		try {
			if(m_sorting == Sorting.Descending) {
				m_lsSortKeyValues.sort(Collections.reverseOrder());

			} else {
				Collections.sort(m_lsSortKeyValues);
			}

			m_bolSorted = true;

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	private void resortData() throws GException {
		try {
			if(m_setPrimaryKeyValues.size() > 0) {
				m_mapPrimaryKeyValue_SortKeyValue = new GMap<>();
				m_mapSortKeyValue_PrimaryKeyValue = new GMap<>();
				m_lsSortKeyValues = new GList<>();

				for(PrimaryKeyValue primarykeyvalue : m_setPrimaryKeyValues) {
					DataVectorRow dvrRow = getDataRow(primarykeyvalue);

					SortKeyValue sortkeyvalue = new SortKeyValue();

					if(m_lsColumnNameSortKeys.size() > 0) {
						for(String strColumnNameSortKey : m_lsColumnNameSortKeys) {
							DBObject dboSortKeyValue = dvrRow.getDataAtColumnName(strColumnNameSortKey);
							sortkeyvalue.add(dboSortKeyValue);
						}
					} else {
						int intRowCount = getRowCount();

						sortkeyvalue.add(new DBString(intRowCount));
					}

					sortkeyvalue.add(new DBString(primarykeyvalue.getString()));

					if(m_mapPrimaryKeyValue_SortKeyValue.containsKey(sortkeyvalue)) {
						ErrorDuplicateSortKey(m_lsColumnNameSortKeys, sortkeyvalue);
					}

					m_mapPrimaryKeyValue_SortKeyValue.put(sortkeyvalue, primarykeyvalue);
					m_mapSortKeyValue_PrimaryKeyValue.put(primarykeyvalue, sortkeyvalue);
					m_lsSortKeyValues.add(sortkeyvalue);
				}

				sortData();
			}

			beforeFirst();

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}
}
