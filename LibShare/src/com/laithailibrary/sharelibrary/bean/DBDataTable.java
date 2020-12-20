package com.laithailibrary.sharelibrary.bean;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import com.laithailibrary.sharelibrary.collection.*;
import com.laithailibrary.sharelibrary.datatableview.*;
import com.laithailibrary.sharelibrary.db.dbobject.*;
import com.laithailibrary.sharelibrary.db.dbutilities.*;
import com.laithailibrary.sharelibrary.field.*;
import com.laithailibrary.sharelibrary.field.pfield.*;
import com.laithailibrary.sharelibrary.session.*;
import com.laithailibrary.sharelibrary.sqlstatement.*;
import exc.*;

/**
 * Created by IntelliJ IDEA.
 * User: dumkrerng
 * Date: 6/6/11
 * Time: 11:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class DBDataTable extends GBaseBean implements Cloneable, Externalizable, Serializable {

	private Map<String, Class<? extends DBObject>> m_mapClass_FieldName = new GMap<>();
	private Map<String, DataField<? extends DBObject>> m_mapDataField_FieldName = new GMap<>();
	private List<String> m_lsFieldName = new GList<>();
	private String m_strFieldPrimaryKeyName;

	private FieldPrimaryKey m_fieldPrimaryKey;

	private Map<String, FieldUnique> m_mapFieldUnique_Name;

	private TableName m_tablename;
	private TableID m_tableid;
	private String m_strUSERIDTag = "";

	private static final long serialVersionUID = 11;

	public DBDataTable() {}

	public DBDataTable(TableID p_tableid, FieldPrimaryKey p_fieldprimarykey) {
		try {
			init(p_tableid, p_fieldprimarykey, "");

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		}
	}

	public DBDataTable(TableID p_tableid, FieldPrimaryKey p_fieldprimarykey, String p_strUSERIDTag) {
		try {
			init(p_tableid, p_fieldprimarykey, p_strUSERIDTag);

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		}
	}

	private void init(TableID p_tableid, FieldPrimaryKey p_fieldprimarykey, String p_strUSERIDTag) throws GException {
		try {
			m_tablename = p_fieldprimarykey.getTableName();
			m_strFieldPrimaryKeyName = p_fieldprimarykey.getPrimaryKeyName();
			m_mapClass_FieldName = new GMap<>();
			m_mapDataField_FieldName = new GMap<>();
			m_lsFieldName = new GList<>();

			m_tableid = p_tableid;
			m_fieldPrimaryKey = p_fieldprimarykey;
			m_strUSERIDTag = p_strUSERIDTag;

			setDataField();
			setUniqueField();

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public void writeExternal(ObjectOutput out) throws IOException {
		try {
			super.writeExternal(out);

			out.writeObject(m_mapClass_FieldName);
			out.writeObject(m_mapDataField_FieldName);
			out.writeObject(m_lsFieldName);
			out.writeObject(m_strFieldPrimaryKeyName);
			out.writeObject(m_fieldPrimaryKey);
			out.writeObject(m_mapFieldUnique_Name);
			out.writeObject(m_tablename);
			out.writeObject(m_tableid);
			out.writeObject(m_strUSERIDTag);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			out.close();

			throw new IOException(exception);
		}
	}

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		try {
			super.readExternal(in);

			m_mapClass_FieldName = (Map<String, Class<? extends DBObject>>)in.readObject();
			m_mapDataField_FieldName = (Map<String, DataField<? extends DBObject>>)in.readObject();
			m_lsFieldName = (List<String>)in.readObject();
			m_strFieldPrimaryKeyName = (String)in.readObject();
			m_fieldPrimaryKey = (FieldPrimaryKey)in.readObject();
			m_mapFieldUnique_Name = (Map<String, FieldUnique>)in.readObject();
			m_tablename = (TableName)in.readObject();
			m_tableid = (TableID)in.readObject();
			m_strUSERIDTag = (String)in.readObject();

		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			in.close();

			throw new IOException(exception);
		}
	}

	private void setDataField() throws GException {
		try {
		  for(Field field : this.getClass().getDeclaredFields()) {
				if(field.getType().isAssignableFrom(DataField.class)) {
					DataField<? extends DBObject> dbDataField = (DataField<? extends DBObject>)field.get(DataField.class.newInstance());
					PDataField pdatafield = field.getAnnotation(PDataField.class);

					if(pdatafield != null) {
						String strFieldLabel = pdatafield.FieldLabel();

						if(strFieldLabel.length() <= 0) {
							strFieldLabel = pdatafield.FieldName();
						}

						if(dbDataField.getClassData().getName().compareTo(DBBoolean.class.getName()) == 0) {
							boolean bolMandatory = pdatafield.Mandatory();

							if(!bolMandatory) {
								StringBuilder bufferError = new StringBuilder();
								bufferError.append("DBBoolean must be set Mandatory = true.\n");
								bufferError.append(dbDataField.toString());

								throw new GException(bufferError.toString());
							}
						}

						dbDataField.setFieldLabel(strFieldLabel);
						dbDataField.setColumnWidth(pdatafield.ColumnWidth());
						dbDataField.setColumnAlignment(pdatafield.ColumnAlignment());
						dbDataField.setDataSize(pdatafield.DataSize());
						dbDataField.setMandatory(pdatafield.Mandatory());
						dbDataField.setUpdateable(pdatafield.Updateable());
						dbDataField.setIndex(pdatafield.Index());

					} else {
						throw new GException("Invalid PDataField!");
					}

					addField(dbDataField);

				} else if(field.getType().isAssignableFrom(DataFieldUSERID.class)) {
					DataFieldUSERID dbDataField = (DataFieldUSERID)field.get(DataFieldUSERID.class.newInstance());
					PDataFieldUSERID pdatafield = field.getAnnotation(PDataFieldUSERID.class);

					if(pdatafield != null) {
						dbDataField.setFieldLabel(pdatafield.FieldLabel());
						dbDataField.setColumnWidth(pdatafield.ColumnWidth());
						dbDataField.setDataSize(pdatafield.DataSize());
						dbDataField.setMandatory(pdatafield.Mandatory());
						dbDataField.setUpdateable(pdatafield.Updateable());
						dbDataField.setIndex(pdatafield.Index());

					} else {
						throw new GException("Invalid PDataFieldUSERID!");
					}

					addField(dbDataField);

				} else if(field.getType().isAssignableFrom(DataFieldDocumentUSERID.class)) {
					DataFieldDocumentUSERID dbDataField = (DataFieldDocumentUSERID)field.get(DataFieldDocumentUSERID.class.newInstance());
					PDataFieldDocumentUSERID pdatafield = field.getAnnotation(PDataFieldDocumentUSERID.class);

					if(pdatafield != null) {
						dbDataField.setFieldLabel(pdatafield.FieldLabel());
						dbDataField.setColumnWidth(pdatafield.ColumnWidth());
						dbDataField.setDataSize(pdatafield.DataSize());
						dbDataField.setMandatory(true);
						dbDataField.setUpdateable(false);
						dbDataField.setIndex(true);

					} else {
						throw new GException("Invalid DataFieldDocumentUSERID!");
					}

					addField(dbDataField);

				} else if(field.getType().isAssignableFrom(DataFieldPrimaryKeyID.class)) {
					DataFieldPrimaryKeyID dbDataField = (DataFieldPrimaryKeyID)field.get(DataFieldPrimaryKeyID.class.newInstance());
					PDataFieldPrimaryKeyID pdatafield = field.getAnnotation(PDataFieldPrimaryKeyID.class);

					if(pdatafield != null) {
						dbDataField.setDataSize(DataField.DataSize_PrimaryKey);
						dbDataField.setMandatory(true);
						dbDataField.setUpdateable(false);
						dbDataField.setIndex(true);

					} else {
						throw new GException("Invalid PDataFieldPrimaryKeyID!");
					}

					addField(dbDataField);
				}
			}

			addField(new FieldRecordUpdated(m_tablename));

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	private void setUniqueField() throws GException {
		try {
			m_mapFieldUnique_Name = new GMap<>();

		  for(Field field : this.getClass().getDeclaredFields()) {
				if(field.getType().isAssignableFrom(FieldUnique.class)) {
					FieldUnique fieldunique = (FieldUnique)field.get(new FieldUnique());

					m_mapFieldUnique_Name.put(field.getName(), fieldunique);
				}
			}
		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public TableName getTableName() {
		return m_tablename;
	}

	public String getUSERIDTag() {
		return m_strUSERIDTag;
	}

	public Map<String, FieldUnique> getMapFieldUnique_Name() {
		return m_mapFieldUnique_Name;
	}

	protected void addField(DataField<? extends DBObject> p_datafield) throws GException {
		try {
			String strFieldName = p_datafield.getFieldName();
			Class<? extends DBObject> clsDBObject = p_datafield.getClassData();

			if(!m_mapClass_FieldName.containsKey(strFieldName)) {
				m_mapDataField_FieldName.put(strFieldName, p_datafield);
				m_mapClass_FieldName.put(strFieldName, clsDBObject);
				m_lsFieldName.add(strFieldName);

				addFieldData(strFieldName, clsDBObject);

			} else {
				throw new GException("Duplicate filed name ".concat(strFieldName));
			}
		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public void setData(DataVectorRow p_dvrRow) throws GException {
		try {
		  List<DataField<? extends DBObject>> lsDataFields = getDataFields();

			for(DataField<? extends DBObject> datafield : lsDataFields) {
				setData(datafield.getFieldName(), p_dvrRow.getDataAtColumnName(datafield));
			}
		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public DataField getDataField(DataField p_datafielde) {
		return getDataField(p_datafielde.getFieldName());
	}

	public DataField getDataField(String p_strFieldName) {
		return m_mapDataField_FieldName.get(p_strFieldName);
	}

	public Class<? extends DBObject> getClass_FieldName(String p_strFieldName) {
		return m_mapClass_FieldName.get(p_strFieldName);
	}

	public List<String> getFieldNames() {
		return m_lsFieldName;
	}

	public List<DataField<? extends DBObject>> getDataFields() {
		List<DataField<? extends DBObject>> lsDataFields = new GList<>();

		for(String strFieldName : m_lsFieldName) {
			DataField<? extends DBObject> datafield = m_mapDataField_FieldName.get(strFieldName);

			lsDataFields.add(datafield);
		}

		return lsDataFields;
	}

	public Map<String, DataField<? extends DBObject>> getMapDataField_FieldName() {
		return m_mapDataField_FieldName;
	}

	public List<FieldForeignKey> getFieldForeignKeys() throws GException {
		try {
			List<FieldForeignKey> lsFieldForeignKeys = new GList<>();

		  for(Field field : this.getClass().getDeclaredFields()) {
				if(field.getType().isAssignableFrom(FieldForeignKey.class)) {
					FieldForeignKey fForeignKey = (FieldForeignKey)field.get(DataField.class.newInstance());

					lsFieldForeignKeys.add(fForeignKey);
				}
			}

			return lsFieldForeignKeys;

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public List<DataField> getFieldIndexs() throws GException {
		try {
			List<DataField> lsFieldIndexs = new GList<>();

		  for(Field field : this.getClass().getDeclaredFields()) {
				if(field.getType().isAssignableFrom(DataField.class)
					|| field.getType().isAssignableFrom(DataFieldUSERID.class)) {

					DataField datafield = (DataField)field.get(DataField.class.newInstance());

					if(datafield.isIndex()) {
						lsFieldIndexs.add(datafield);
					}
				}
			}

			return lsFieldIndexs;

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public TableID getTableID() {
		return m_tableid;
	}

	public String getFieldPrimaryKeyName() {
		return m_strFieldPrimaryKeyName;
	}

	public FieldPrimaryKey getFieldPrimaryKey() {
		return m_fieldPrimaryKey;
	}

	public DBDataTable cloneDBDataTable() {
		try {
			return (DBDataTable)this.clone();

		} catch(Exception e) {
			e.printStackTrace();
			return this;
		}
	}

	public String getSQLString_Select(ISessionID p_sessionid) throws GException {
		return getSQLString_Select(SQLStatement.EmptyStatement, p_sessionid);
	}

	public String getSQLString_Select(SQLStatement p_sqlStatement, ISessionID p_sessionid) throws GException {
		try {
			TableName tablename = getTableName();
			List<String>  lsFieldName = getFieldNames();

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
			builder.append("FROM ").append(tablename.getTableDB()).append('\n');

			if(p_sqlStatement.isSet()) {
				builder.append("WHERE ").append(p_sqlStatement.getSQLString(DBUtilities.getConnection(p_sessionid)));
			}

			return builder.toString();

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public void setRecordUpdated(DBDateTime p_dbdtRecordUpdated) throws GException {
		setData(FieldRecordUpdated.FieldName, p_dbdtRecordUpdated);
	}

	public DBDateTime getRecordUpdated() throws GException {
		return (DBDateTime)getData(FieldRecordUpdated.FieldName);
	}

	public String toString() {
		return m_tablename.getTableName();
	}
}
