package com.laithailibrary.sharelibrary.bean;

import java.io.*;
import java.util.*;
import com.laithailibrary.sharelibrary.collection.*;
import com.laithailibrary.sharelibrary.datatableview.*;
import com.laithailibrary.sharelibrary.db.dbobject.*;
import com.laithailibrary.sharelibrary.db.dbutilities.*;
import com.laithailibrary.sharelibrary.field.*;
import exc.*;
import org.json.*;

/**
 * Created by dumkrerng on 17/5/2558.
 */
public class GBaseBean implements Cloneable, Comparable<GBaseBean>, Externalizable, Serializable {

	private DataVectorRow m_dataholder = null;

	private static final long serialVersionUID = 2;

	public GBaseBean() {}

	public void writeExternal(ObjectOutput out) throws IOException {
		try {
			out.writeObject(m_dataholder);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			out.close();

			throw new IOException(exception);
		}
	}

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		try {
			m_dataholder = (DataVectorRow)in.readObject();

		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			in.close();

			throw new IOException(exception);
		}
	}

	public void setDataTableColumnModel(DataTableColumnModel p_datatablecolumnmodel) throws GException {
		try {
			m_dataholder = new DataVectorRow(p_datatablecolumnmodel);

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	protected void addFieldData(DataField p_datafield) throws GException {
		addFieldData(p_datafield.getFieldName(), p_datafield.getClassData());
	}

	protected void addFieldData(String p_strColumnName, Class<? extends DBObject> p_class) throws GException {
		try {
			if(m_dataholder == null) {
				m_dataholder = new DataVectorRow();
			}

			DataTableColumnModel datatablecolumnmodel = m_dataholder.getDataTableColumnModel();

			if(datatablecolumnmodel == null) {
				datatablecolumnmodel = new DataTableColumnModel();

				m_dataholder = new DataVectorRow(datatablecolumnmodel);
			}

			datatablecolumnmodel.addColumn(p_strColumnName, p_class);

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public void setData_FromJSONString(String p_strJSON) throws GException {
		try {
			m_dataholder.setData_FromJSONString(p_strJSON);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public void setData(JSONObject p_jsonObject) throws GException {
		try {
			m_dataholder.setData(p_jsonObject);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public<T extends DBObject> void setData(DataField<T> p_datafield, String p_strValue) throws GException {
		try {
			Class<? extends DBObject> clsValue = p_datafield.getClassData();
			DBObject dbobjValue = clsValue.newInstance();
			dbobjValue.setStringValue(p_strValue);

			setData(p_datafield.getFieldName(), dbobjValue);

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public<T extends DBObject> void setData(DataField<T> p_datafield, T p_dbObject) throws GException {
		setData(p_datafield.getFieldName(), p_dbObject);
	}

	public void setData(String p_strColumnName, DBObject p_dbObject) throws GException {
		try {
			m_dataholder.setData(p_strColumnName, p_dbObject);

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public void setData(GResultSet p_rst) throws GException {
		try {
			DataTableColumnModel columnmodel = m_dataholder.getDataTableColumnModel();
			Map<String, Integer> mapColumnIndex_ColumnName = columnmodel.getMapColumnIndex_ColumnName();

			for(Map.Entry<String, Integer> entColumnIndex : mapColumnIndex_ColumnName.entrySet()) {
				String strColumnName = entColumnIndex.getKey();
				int intColumnIndex = entColumnIndex.getValue();

				Class<? extends DBObject> clsColumn = columnmodel.getColumnClass(intColumnIndex);
				DBObject dboValue = clsColumn.newInstance();

				if(clsColumn.newInstance() instanceof DBString) {
					String strResult = p_rst.getString(strColumnName);
					dboValue.setStringValue(strResult);

				} else if(clsColumn.newInstance() instanceof DBInteger) {
					int intResult = p_rst.getInt(strColumnName);
					dboValue.setStringValue(Integer.toString(intResult));

				} else if(clsColumn.newInstance() instanceof DBBoolean) {
					Boolean bolResult = p_rst.getBoolean(strColumnName);
					dboValue.setStringValue(bolResult.toString());

				} else if(clsColumn.newInstance() instanceof DBDate) {
					DBDate dbdResult = p_rst.getDate(strColumnName);
					dboValue.setStringValue(dbdResult.getString());

				} else if(clsColumn.newInstance() instanceof DBDateTime) {
					DBDateTime dbdtResult = p_rst.getDateTime(strColumnName);
					dboValue.setStringValue(dbdtResult.getString());

				} else if(clsColumn.newInstance() instanceof DBDecimal) {
					DBDecimal dbdmResult = p_rst.getDecimal(strColumnName);
					dboValue.setStringValue(dbdmResult.getString());

				} else if(clsColumn.newInstance() instanceof DBMoney) {
					DBDecimal dbdmResult = p_rst.getDecimal(strColumnName);
					dboValue.setStringValue(dbdmResult.getString());

				} else if(clsColumn.newInstance() instanceof DBQuantity) {
					DBDecimal dbdmResult = p_rst.getDecimal(strColumnName);
					dboValue.setStringValue(dbdmResult.getString());

				} else {
					throw new GException("Not-support Class: " + clsColumn.getName());
				}

				setData(strColumnName, dboValue);
			}
		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public DBObject getData(String p_strColumnName) throws GException {
		try {
			return m_dataholder.getDataAtColumnName(p_strColumnName);

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public<T extends DBObject> T getData(Class<T> p_class, String p_strColumnName) throws GException {
		try {
			return m_dataholder.getDataAtColumnName(p_class, p_strColumnName);

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public<T extends DBObject> T getData(DataField<T> p_datafield) throws GException {
		try {
			return m_dataholder.getDataAtColumnName(p_datafield);

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public DataVectorRow getDataVectorRow() throws GException {
		return m_dataholder;
	}

	public JSONObject toJSON() throws GException {
		return m_dataholder.toJSON();
	}

	public String toJSONString() throws GException {
		return m_dataholder.toJSONString();
	}

	public JSONObject toJSON(List<? extends DataField> p_setDataFields) throws GException {
		return m_dataholder.toJSON(p_setDataFields);
	}

	public String toJSONString(List<? extends DataField> p_setDataFields) throws GException {
		return m_dataholder.toJSONString(p_setDataFields);
	}

	public JSONObject toJSON(Set<String> p_setColumnNames) throws GException {
		return m_dataholder.toJSON(p_setColumnNames);
	}

	public String toJSONString(Set<String> p_setColumnNames) throws GException {
		return m_dataholder.toJSONString(p_setColumnNames);
	}

	public String toString() {
		String strReturn = "";

		try {
			strReturn = m_dataholder.toString();

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		}

		return strReturn;
	}

	public String toString(Set<String> p_setFieldNames) {
		String strReturn = "";

		try {
			strReturn = m_dataholder.toString(p_setFieldNames);

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		}

		return strReturn;
	}

	public String toString(Collection<DataField> p_setDataFields) {
		String strReturn = "";

		try {
			Set<String> setFieldNames = new GSet<>();

			for(DataField datafield : p_setDataFields) {
				setFieldNames.add(datafield.getFieldName());
			}

			strReturn = toString(setFieldNames);

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		}

		return strReturn;
	}

	public int compareTo(GBaseBean p_basebean) {
		return this.toString().compareTo(p_basebean.toString());
	}

	public int compareTo(GBaseBean p_basebean, Set<String> p_setFieldName_ToCompare) {
		return this.toString(p_setFieldName_ToCompare).compareTo(p_basebean.toString(p_setFieldName_ToCompare));
	}

	public int compareTo(GBaseBean p_basebean, Collection<DataField> p_setDataFields) {
		return this.toString(p_setDataFields).compareTo(p_basebean.toString(p_setDataFields));
	}
}
