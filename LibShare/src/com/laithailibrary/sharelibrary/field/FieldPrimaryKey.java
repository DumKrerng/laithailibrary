package com.laithailibrary.sharelibrary.field;

import exc.ExceptionHandler;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * Created by IntelliJ IDEA.
 * User: dumkrerng
 * Date: 2/12/12
 * Time: 12:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class FieldPrimaryKey implements Comparable<FieldPrimaryKey>, Externalizable {

	private TableName m_tablename;
	private String m_strPrimaryKeyName = "";
	private DataField m_datafield;

	private static final long serialVersionUID = 2574383589745821453L;

	public FieldPrimaryKey() {}

	public FieldPrimaryKey(DataField p_datafield) {
		m_datafield = p_datafield;

		init(p_datafield.getTableName(), p_datafield.getFieldName());
	}

	public void writeExternal(ObjectOutput out) throws IOException {
		try {
			out.writeObject(m_tablename);
			out.writeUTF(m_strPrimaryKeyName);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			out.close();

			throw new IOException(exception);
		}
	}

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		try {
			m_tablename = (TableName)in.readObject();
			m_strPrimaryKeyName = in.readUTF();

		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			in.close();

			throw new IOException(exception);
		}
	}

	private void init(TableName p_strTableName, String p_strPrimaryKeyName) {
		m_tablename = p_strTableName;
		m_strPrimaryKeyName = p_strPrimaryKeyName;
	}

	public DataField getDataField() {
		return m_datafield;
	}

	public TableName getTableName() {
		return m_tablename;
	}

	public String getPrimaryKeyName() {
		return m_strPrimaryKeyName;
	}

	public String toString() {
		return m_tablename.getTableName().concat(".").concat(m_strPrimaryKeyName);
	}

	public int compareTo(FieldPrimaryKey p_FPK) {
		return this.toString().compareTo(p_FPK.toString());
	}
}
