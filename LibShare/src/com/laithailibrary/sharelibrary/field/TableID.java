package com.laithailibrary.sharelibrary.field;

import exc.ExceptionHandler;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * Created with IntelliJ IDEA.
 * User: dumkrerng
 * Date: 6/17/12
 * Time: 9:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class TableID implements Comparable<TableID>, Externalizable {

	private String m_strTableID;

	private static final long serialVersionUID = 2514383236545821453L;

	public TableID() {}

	public TableID(String p_strTableID) {
		m_strTableID = p_strTableID;
	}

	public void writeExternal(ObjectOutput out) throws IOException {
		try {
			out.writeObject(m_strTableID);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			out.close();

			throw new IOException(exception);
		}
	}

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		try {
			m_strTableID = (String)in.readObject();

		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			in.close();

			throw new IOException(exception);
		}
	}

	public int length() {
		return m_strTableID.length();
	}

	public String getID() {
		return m_strTableID;
	}

	public String toString() {
		return m_strTableID;
	}

	public int compareTo(String strSourceID) {
		String strSource = strSourceID.substring(0, 2);

		return compareTo(new TableID(strSource));
	}

	public int compareTo(TableID p_tableid) {
		return this.toString().compareTo(p_tableid.toString());
	}
}
