package com.laithailibrary.sharelibrary.field;

import java.io.*;
import java.util.*;
import com.laithailibrary.sharelibrary.collection.*;
import exc.*;

/**
 * Created with IntelliJ IDEA.
 * User: dumkrerng
 * Date: 6/17/12
 * Time: 9:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class FieldUnique implements Comparable<FieldUnique>, Externalizable {

	private TableName m_tablename;
	private List<? extends DataField> m_lsDataFields = new GList<DataField>();

	private static final long serialVersionUID = 252514589745214753L;

	public FieldUnique() {}

	public <T extends DataField> FieldUnique(TableName p_tablename, T p_datafield) {
		try {
			init(p_tablename, new GList<DataField>(p_datafield));

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		}
	}

	public FieldUnique(TableName p_tablename, List<? extends DataField> p_lsDataFields) {
		try {
			init(p_tablename, p_lsDataFields);

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		}
	}

	public FieldUnique(TableName p_tablename, DataField... p_datafields) {
		try {
			List<DataField> lsDataFields = new GList<>(p_datafields.length);
			lsDataFields.addAll(Arrays.asList(p_datafields));

			init(p_tablename, lsDataFields);

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		}
	}

	public void writeExternal(ObjectOutput out) throws IOException {
		try {
			out.writeObject(m_tablename);
			out.writeObject(m_lsDataFields);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			out.close();

			throw new IOException(exception);
		}
	}

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		try {
			m_tablename = (TableName)in.readObject();
			m_lsDataFields = (List<? extends DataField>)in.readObject();

		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			in.close();

			throw new IOException(exception);
		}
	}

	private void init(TableName p_tablename, List<? extends DataField> p_lsDataFields) throws GException {
		try {
			m_tablename = p_tablename;
			m_lsDataFields = p_lsDataFields;

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public List<? extends DataField> getDataFields() {
		return m_lsDataFields;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();

		try {
			builder.append("Table(").append(m_tablename.getTableName()).append(")->");
			builder.append("FieldUnique[");

			for(DataField datafield : m_lsDataFields) {
				builder.append(datafield.getFieldName()).append(", ");
			}

			builder = new StringBuilder(builder.substring(0, builder.length() - 2));
			builder.append("]");

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		}

		return builder.toString();
	}

	public int compareTo(FieldUnique p_fieldunique) {
		return this.toString().compareTo(p_fieldunique.toString());
	}
}
