package com.laithailibrary.sharelibrary.field;

import exc.ExceptionHandler;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: dumkrerng
 * Date: 6/17/12
 * Time: 9:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class TableName implements Comparable<TableName>, Externalizable, Serializable {

	private String m_strTableName;
	private String m_strTableLabel;
	private String m_strTableView = "";

	private static final long serialVersionUID = 2514383589745214753L;

	public TableName() {}

	public TableName(String p_strTableName) {
		m_strTableName = p_strTableName;
		m_strTableLabel = p_strTableName;
	}

	public TableName(String p_strTableName, String p_strTableLabel) {
		m_strTableName = p_strTableName;
		m_strTableLabel = p_strTableLabel;
	}

	public TableName(String p_strTableName, String p_strTableLabel, String p_strTableView) {
		m_strTableName = p_strTableName;
		m_strTableLabel = p_strTableLabel;
		m_strTableView = p_strTableView;
	}

	public void writeExternal(ObjectOutput out) throws IOException {
		try {
			out.writeObject(m_strTableName);
			out.writeObject(m_strTableLabel);
			out.writeObject(m_strTableView);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			out.close();

			throw new IOException(exception);
		}
	}

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		try {
			m_strTableName = (String)in.readObject();
			m_strTableLabel = (String)in.readObject();
			m_strTableView = (String)in.readObject();

		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			in.close();

			throw new IOException(exception);
		}
	}

	public String getTableName() {
		return m_strTableName;
	}

	public String getTableDB() {
		if(m_strTableView.length() > 0) {
			return m_strTableView;
		}

		return m_strTableName;
	}

	public void setTableLabel(String p_strTableLabel) {
		m_strTableLabel = p_strTableLabel;
	}

	public String getTableLabel() {
		return m_strTableLabel;
	}

	public void setTableView(String p_strTableView) {
		m_strTableView = p_strTableView;
	}

	public String getTableView() {
		return m_strTableView;
	}

	public int length() {
		return m_strTableName.length();
	}

	public String toString() {
		return m_strTableName;
	}

	public int compareTo(TableName p_tablename) {
		return this.toString().compareTo(p_tablename.toString());
	}
}
