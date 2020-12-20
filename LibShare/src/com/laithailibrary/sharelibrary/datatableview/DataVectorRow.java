package com.laithailibrary.sharelibrary.datatableview;

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
 * Date: 6/8/11
 * Time: 12:30 AM
 * To change this template use File | Settings | File Templates.
 */
public class DataVectorRow implements Externalizable {

	private DataTableColumnModel m_dtcColumnModel = null;
	private int m_intColumnIndex = -1;

	private CompressData m_compressdata = new CompressData();
	private Map<Integer, Integer> m_mapCompressKey_ColumnIndex = new GMap<>();

	private static final long serialVersionUID = 12;

	public DataVectorRow() {}

	public DataVectorRow(DataTableColumnModel p_dataTableColumnModel) throws GException {
		m_dtcColumnModel = p_dataTableColumnModel;
		m_intColumnIndex = 0;
		m_mapCompressKey_ColumnIndex = new GMap<>();
		m_compressdata = new CompressData();
	}

	public void writeExternal(ObjectOutput out) throws IOException {
		try {
			out.writeObject(m_dtcColumnModel);
			out.writeInt(m_intColumnIndex);
			out.writeObject(m_mapCompressKey_ColumnIndex);
			out.writeObject(m_compressdata);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			out.close();

			throw new IOException(exception);
		}
	}

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		try {
			m_dtcColumnModel = (DataTableColumnModel)in.readObject();
			m_intColumnIndex = in.readInt();
			m_mapCompressKey_ColumnIndex = (Map<Integer, Integer>)in.readObject();
			m_compressdata = (CompressData)in.readObject();

		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			in.close();

			throw new IOException(exception);
		}
	}

	public CompressData getCompressData() throws GException {
		return m_compressdata;
	}

	public void doCombineCompressData(CompressData p_compressdata) throws GException {
		try {
		  for(Integer intColumnIndex : m_mapCompressKey_ColumnIndex.keySet()) {
			  Integer intCompressKey = m_mapCompressKey_ColumnIndex.get(intColumnIndex);
			  String strValue = m_compressdata.getValue(intCompressKey);

			  p_compressdata.addValue(strValue);
			  Integer intCompressKey_New = p_compressdata.getCompressKey(strValue);

			  m_mapCompressKey_ColumnIndex.put(intColumnIndex, intCompressKey_New);
		  }

			m_compressdata = p_compressdata;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public void addData(DBObject p_dbObject) throws GException{
		try {
			if(m_intColumnIndex >= getColumnModelCount()) {
				throw new GException("Out off index\nIndex is ".concat(Integer.toString(m_intColumnIndex + 1)).concat("\n").concat("Size is ")
					.concat(Integer.toString(getColumnModelCount())));
			}

			Class<? extends DBObject> aClass = m_dtcColumnModel.getColumnClass(m_intColumnIndex);

			if(!aClass.isInstance(p_dbObject)) {
				throw new GException("Attempting cast ".concat(p_dbObject.getClass().getSimpleName()).concat(" to ").concat(aClass.getSimpleName()));
			}

			String strValue = p_dbObject.getString();
			Integer intCompressKey;

			if(m_compressdata.containsValue(strValue)) {
				intCompressKey = m_compressdata.getCompressKey(strValue);

			} else {
				m_compressdata.addValue(strValue);
				intCompressKey = m_compressdata.getCompressKey(strValue);
			}

			m_mapCompressKey_ColumnIndex.put(m_intColumnIndex, intCompressKey);
			m_intColumnIndex++;

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public void setData(String strColumnName, DBObject p_dbObject) throws GException{
		try {
			int intColumnIndex = m_dtcColumnModel.getColumnIndex(strColumnName);

			setData(intColumnIndex, p_dbObject);

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public void setData(int p_intColumnIndex, DBObject p_dbObject) throws GException{
		try {
			if(m_mapCompressKey_ColumnIndex.size() <= 0) {
				int intColumnModelCount = getColumnModelCount();

				for(int intColumnIndex = 0; intColumnIndex < intColumnModelCount; intColumnIndex++) {
					m_compressdata.addValue("");

					Integer intCompressKey = m_compressdata.getCompressKey("");
					m_mapCompressKey_ColumnIndex.put(intColumnIndex, intCompressKey);
				}
			}

			m_mapCompressKey_ColumnIndex.remove(p_intColumnIndex);

			int intColumnModelCount = getColumnModelCount();

			if(p_intColumnIndex >= intColumnModelCount) {
				throw new GException("Out off index\nIndex is ".concat(Integer.toString(p_intColumnIndex + 1)).concat("\n").concat("Size is ")
					.concat(Integer.toString(intColumnModelCount)));
			}

			Class aClass = m_dtcColumnModel.getColumnClass(p_intColumnIndex);

			if(!aClass.isInstance(p_dbObject)) {
				throw new GException("Attempting cast ".concat(p_dbObject.getClass().getSimpleName()).concat(" to ").concat(aClass.getSimpleName()));
			}

			String strValue = p_dbObject.getString();
			Integer intCompressKey = m_compressdata.addValue(strValue);

			m_mapCompressKey_ColumnIndex.put(p_intColumnIndex, intCompressKey);

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public DataTableColumnModel getDataTableColumnModel() {
		return m_dtcColumnModel;
	}

	public int getColumnModelCount() throws GException {
		try {
			if(m_dtcColumnModel == null) {
				return 0;
			}

			return m_dtcColumnModel.getColumnCount();

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public int getColumnDataCount() {
		return m_mapCompressKey_ColumnIndex.size();
	}

	public void setMapCompressKey_ColumnIndex(Map<Integer, Integer> p_mapCompressKey_ColumnIndex) {
		m_mapCompressKey_ColumnIndex = null;
		m_mapCompressKey_ColumnIndex = p_mapCompressKey_ColumnIndex;
	}

	public void setCompressData(CompressData p_compressdata) {
		m_compressdata = p_compressdata;
	}

	public int size() {
		return getColumnDataCount();
	}

	public Vector<DBObject> getDataVector() throws GException {
		try {
			List<DBObject> lsDBObjectValue = new GList<>();

			for(Integer intColumnIndex : m_mapCompressKey_ColumnIndex.keySet()) {
				Integer intCompressKey = m_mapCompressKey_ColumnIndex.get(intColumnIndex);
				String strValue = m_compressdata.getValue(intCompressKey);

				DBObject dbObject = m_dtcColumnModel.getColumnClass(intColumnIndex).newInstance();
				dbObject.setStringValue(strValue);

				lsDBObjectValue.add(dbObject);
			}

			Vector<DBObject> vector = new Vector<>(lsDBObjectValue);

			return vector;

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public DBObject getDataAtColumnName(String p_strColumnName) throws GException {
		try {
			int intColumnIndex = m_dtcColumnModel.getColumnIndex(p_strColumnName);

			return getDataAtColumnIndex(intColumnIndex);

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public<T extends DBObject> T getDataAtColumnName(Class<T> p_class, int p_intColumnIndex) throws GException {
		try {
			Integer intCompressKey = m_mapCompressKey_ColumnIndex.get(p_intColumnIndex);
			String strValue = m_compressdata.getValue(intCompressKey);

			T dbObject = p_class.newInstance();
			dbObject.setStringValue(strValue);

			return dbObject;

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public DBObject getDataAtColumnIndex(int p_intColumnIndex) throws GException {
		try {
			Integer intCompressKey = m_mapCompressKey_ColumnIndex.get(p_intColumnIndex);
			String strValue = m_compressdata.getValue(intCompressKey);

			DBObject dbObject = m_dtcColumnModel.getColumnClass(p_intColumnIndex).newInstance();
			dbObject.setStringValue(strValue);

			return dbObject;

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public<T extends DBObject> T getDataAtColumnName(DataField<T> p_datafield) throws GException {
		return getDataAtColumnName(p_datafield.getClassData(), p_datafield.getFieldName());
	}

	public<T extends DBObject> T getDataAtColumnName(Class<T> p_class, String p_strColumnName) throws GException {
		try {
			int intColumnIndex = m_dtcColumnModel.getColumnIndex(p_strColumnName);

			return getDataAtColumnName(p_class, intColumnIndex);

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public PrimaryKeyValue getPrimaryKeyValue(DataVectorTable p_datavectortable) throws GException {
		List<String> lsColumnNamePrimaryKeys = p_datavectortable.getColumnNamePrimaryKeys();

		return getPrimaryKeyValue(lsColumnNamePrimaryKeys);
	}

	public PrimaryKeyValue getPrimaryKeyValue(List<String> p_lsColumnNamePrimaryKeys) throws GException {
		try {
			if(p_lsColumnNamePrimaryKeys.size() <= 0) {
				throw new GException("Not-Existing Column PrimaryKey!");
			}

			PrimaryKeyValue primarykeyvalue = new PrimaryKeyValue();

			for(String strColumnNamePrimaryKey : p_lsColumnNamePrimaryKeys) {
				DBObject dboValue = getDataAtColumnName(strColumnNamePrimaryKey);
				primarykeyvalue.add(dboValue);
			}

			return primarykeyvalue;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public Set<Integer> getCompressKeys() throws GException {
		try {
			Set<Integer> setCompressKeys = new GSet<>();

			for(Map.Entry<Integer, Integer> entCompressKey : m_mapCompressKey_ColumnIndex.entrySet()) {
				Integer intCompressKey = entCompressKey.getValue();
				setCompressKeys.add(intCompressKey);
			}

			return setCompressKeys;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public String getCSVData() throws GException {
		try {
			DataTableColumnModel dtcColumn = getDataTableColumnModel();

			int intColumnCount = dtcColumn.getColumnCount();
			int intColumnIndex;

			StringBuilder builder = new StringBuilder();

			for(intColumnIndex = 0; intColumnIndex < intColumnCount; intColumnIndex++) {
				DataTableColumnModel.ShowInCSV showincsv = dtcColumn.getShowInCSV(intColumnIndex);

				if(showincsv == DataTableColumnModel.ShowInCSV.Yes) {
					DBObject dbObject = getDataAtColumnIndex(intColumnIndex);

					if(dbObject != null) {
						builder.append(dbObject.getCSVString());
					}

					builder.append(";");
				}
			}

			return builder.toString();

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public void setData_FromJSONString(String p_strJSON) throws GException {
		try {
			setData(new JSONObject(p_strJSON));

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public void setData(JSONObject p_json) throws GException {
		try {
			Iterator iterator = p_json.keys();
			Map<String, Object> mapValue_Key = new GMap<>();

			while(iterator.hasNext()) {
				String strKey = (String)iterator.next();
				Object objValue = p_json.get(strKey);

				mapValue_Key.put(strKey.toLowerCase(), objValue);
			}

			int intColumnCount = m_dtcColumnModel.getColumnCount();

			for(int index=0; index < intColumnCount; index++) {
				String strColumnName = m_dtcColumnModel.getColumnName(index).toLowerCase();

				if(mapValue_Key.containsKey(strColumnName)) {
					Object objValue = mapValue_Key.get(strColumnName);

					Class<? extends DBObject> clsColumn = m_dtcColumnModel.getColumnClass(index);
					DBObject dboValue = clsColumn.newInstance();
					dboValue.setStringValue(objValue.toString());

					setData(strColumnName, dboValue);
				}
			}
		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public JSONObject toJSON() throws GException {
		return toJSON_Base(null);
	}

	public JSONObject toJSON(List<? extends DataField> p_setDataFields) throws GException {
		Set<String> setColumnNames = new GSet<>();

		for(DataField datafield : p_setDataFields) {
			setColumnNames.add(datafield.getFieldName());
		}

		return toJSON_Base(setColumnNames);
	}

	public JSONObject toJSON(Set<String> p_setColumnNames) throws GException {
		return toJSON_Base(p_setColumnNames);
	}

	private JSONObject toJSON_Base(Set<String> p_setColumnNames) throws GException {
		try {
			if(p_setColumnNames != null) {
				if(p_setColumnNames.size() <= 0) {
					return new JSONObject();
				}
			}

			Set<String> setColumnNames_LowerCase = new GSet<>();

			if(p_setColumnNames != null) {
				for(String strColumnName : p_setColumnNames) {
					setColumnNames_LowerCase.add(strColumnName.toLowerCase());
				}
			}

			DataTableColumnModel dtcColumn = getDataTableColumnModel();

			int intColumnCount = dtcColumn.getColumnCount();
			int intColumnIndex;

			JSONObject jsonReturn = new JSONObject();

			for(intColumnIndex = 0; intColumnIndex < intColumnCount; intColumnIndex++) {
				String strColumnName = dtcColumn.getColumnName(intColumnIndex);
				DBObject dbObject = getDataAtColumnIndex(intColumnIndex);

				if(p_setColumnNames == null) {
					jsonReturn.put(strColumnName, dbObject.getValueReport());

				} else {
					if(setColumnNames_LowerCase.contains(strColumnName.toLowerCase()) == true) {
						jsonReturn.put(strColumnName, dbObject.getValueReport());
					}
				}
			}

			return jsonReturn;

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public String toJSONString() throws GException {
		try {
			JSONObject jsonReturn = toJSON();

			return jsonReturn.toString();

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public String toJSONString(List<? extends DataField> p_setDataFields) throws GException {
		try {
			JSONObject jsonReturn = toJSON(p_setDataFields);

			return jsonReturn.toString();

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public String toJSONString(Set<String> p_setColumnNames) throws GException {
		try {
			JSONObject jsonReturn = toJSON(p_setColumnNames);

			return jsonReturn.toString();

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public DataVectorRow clone() throws CloneNotSupportedException {
		try {
			DataTableColumnModel dcmColumns = getDataTableColumnModel();
			Map<String, Integer> mapColumnIndex_ColumnName = dcmColumns.getMapColumnIndex_ColumnName();

			DataVectorRow dvrNew = new DataVectorRow(dcmColumns);

			for(Map.Entry<String, Integer> entColumnNumber : mapColumnIndex_ColumnName.entrySet()) {
				Integer intColumnIndex = entColumnNumber.getValue();
				DBObject dbobjData = getDataAtColumnIndex(intColumnIndex);

				dvrNew.setData(intColumnIndex, dbobjData);
			}

			return dvrNew;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new CloneNotSupportedException(exception.toString());
		}
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();

		try {
			if(m_dtcColumnModel != null
				&& m_mapCompressKey_ColumnIndex != null
				&& m_compressdata != null) {

				int intColumnCount = getColumnModelCount();

				if(m_mapCompressKey_ColumnIndex.size() > 0) {
					for(int intColumnIndex = 0; intColumnIndex < intColumnCount; intColumnIndex++) {
						DBObject dbObject = getDataAtColumnIndex(intColumnIndex);

						if(dbObject != null) {
							builder.append(dbObject.getSortString());
						}

						builder.append(";");
					}
				}
			}
		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		}

		return builder.toString();
	}

	public String toString(Set<String> p_setFieldName) {
		StringBuilder builder = new StringBuilder();

		try {
			if(p_setFieldName.size() > 0) {
				if(m_dtcColumnModel != null
					&& m_mapCompressKey_ColumnIndex != null
					&& m_compressdata != null) {

					DataTableColumnModel dtcColumn = getDataTableColumnModel();
					int intColumnCount = dtcColumn.getColumnCount();

					if(m_mapCompressKey_ColumnIndex.size() > 0) {
						for(int intColumnIndex = 0; intColumnIndex < intColumnCount; intColumnIndex++) {
							String strColumnName = dtcColumn.getColumnName(intColumnIndex);

							if(p_setFieldName.contains(strColumnName)) {
								DBObject dbObject = getDataAtColumnIndex(intColumnIndex);

								if(dbObject != null) {
									builder.append(dbObject.getSortString());
								}

								builder.append(";");
							}
						}
					}
				}
			}
		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		}

		return builder.toString();
	}
}
