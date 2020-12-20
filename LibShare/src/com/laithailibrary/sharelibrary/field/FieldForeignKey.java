package com.laithailibrary.sharelibrary.field;

import com.laithailibrary.sharelibrary.collection.GList;
import com.laithailibrary.sharelibrary.collection.GSet;
import exc.*;
import exc.ExceptionHandler;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: dumkrerng
 * Date: 2/12/12
 * Time: 12:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class FieldForeignKey implements Comparable<FieldForeignKey>, Externalizable {

	private Set<FieldPrimaryKey> m_setFieldReferentPrimaryKey = null;
	private DataField m_dfDataField_ForeignKey = null;
	private TableName m_tablename;
	private String m_strFieldName = "";

	private static final long serialVersionUID = 32583589745214753L;

	public FieldForeignKey() {}

	public FieldForeignKey(DataField p_datafield, FieldPrimaryKey p_fieldReferentPrimaryKey) {
		try {
			init(p_datafield, new GList<>(p_fieldReferentPrimaryKey));

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
		}
	}

	public FieldForeignKey(DataField p_datafield, List<FieldPrimaryKey> p_lsFieldReferentPrimaryKeys) {
		try {
			init(p_datafield, p_lsFieldReferentPrimaryKeys);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
		}
	}

	public FieldForeignKey(DataField p_datafield, FieldPrimaryKey... p_fieldprimarykeys) {
		try {
			List<FieldPrimaryKey> lsFieldReferentPrimaryKeys = new GList<>(p_fieldprimarykeys.length);

			for(FieldPrimaryKey fieldprimarykey : p_fieldprimarykeys) {
				lsFieldReferentPrimaryKeys.add(fieldprimarykey);
			}

			init(p_datafield, lsFieldReferentPrimaryKeys);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
		}
	}

	public void writeExternal(ObjectOutput out) throws IOException {
		try {
			out.writeObject(m_setFieldReferentPrimaryKey);
			out.writeObject(m_dfDataField_ForeignKey);
			out.writeObject(m_tablename);
			out.writeUTF(m_strFieldName);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			out.close();

			throw new IOException(exception);
		}
	}

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		try {
			m_setFieldReferentPrimaryKey = (Set<FieldPrimaryKey>)in.readObject();
			m_dfDataField_ForeignKey = (DataField)in.readObject();
			m_tablename = (TableName)in.readObject();
			m_strFieldName = in.readUTF();

		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			in.close();

			throw new IOException(exception);
		}
	}

	private void init(DataField p_datafield, List<FieldPrimaryKey> p_lsFieldReferentPrimaryKeys) throws GException {
		try {
			m_dfDataField_ForeignKey = p_datafield;
			m_tablename = p_datafield.getTableName();
			m_strFieldName = p_datafield.getFieldName();
			m_setFieldReferentPrimaryKey = new GSet<>();

			for(FieldPrimaryKey fieldPrimaryKey : p_lsFieldReferentPrimaryKeys) {
				m_setFieldReferentPrimaryKey.add(fieldPrimaryKey);
			}

			new ForeignKey(this);

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public DataField getDataField_ForeignKey() {
		return m_dfDataField_ForeignKey;
	}

	public TableName getTableName_ForeignKey() {
		return m_tablename;
	}

	public String getFieldName_ForeignKey() {
		return m_strFieldName;
	}

	public Set<FieldPrimaryKey> getFieldReferentPrimaryKeys() {
		return m_setFieldReferentPrimaryKey;
	}

	public FieldPrimaryKey getFieldReferentPrimaryKey_First() {
		return m_setFieldReferentPrimaryKey.iterator().next();
	}

	public int getSizeReferentPrimaryKey() {
		return m_setFieldReferentPrimaryKey.size();
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(m_tablename.getTableName()).append(".").append(m_strFieldName).append("->[");

		for(FieldPrimaryKey fieldPrimaryKey : m_setFieldReferentPrimaryKey) {
			builder.append(fieldPrimaryKey.toString()).append(", ");
		}

		builder = new StringBuilder(builder.substring(0, builder.length() - 2));
		builder.append("]");

		return builder.toString();
	}

	public int compareTo(FieldForeignKey p_FFK) {
		return this.toString().compareTo(p_FFK.toString());
	}
}
