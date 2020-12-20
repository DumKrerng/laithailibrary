package com.laithailibrary.sharelibrary.datatableview;

import com.laithailibrary.sharelibrary.collection.GList;
import com.laithailibrary.sharelibrary.db.dbobject.*;
import exc.ExceptionHandler;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: dumkrerng
 * Date: 5/11/12
 * Time: 8:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class PrimaryKeyValue implements Comparable<PrimaryKeyValue>, Externalizable {

	private List<DBObject> m_lsPrimaryKeyValues = new GList<>();

	private static final long serialVersionUID = 6;

	public PrimaryKeyValue() {}

	public PrimaryKeyValue(DBObject p_dbObject) {
		add(p_dbObject);
	}

	public void writeExternal(ObjectOutput out) throws IOException {
		try {
			out.writeObject(m_lsPrimaryKeyValues);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			out.close();

			throw new IOException(exception);
		}
	}

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		try {
			m_lsPrimaryKeyValues = (List<DBObject>)in.readObject();

		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			in.close();

			throw new IOException(exception);
		}
	}

	public void add(DBObject p_dbObject) {
		m_lsPrimaryKeyValues.add(p_dbObject);
	}

	public DBObject get(int p_index) {
		if(m_lsPrimaryKeyValues.size() <= p_index) {
			return new DBString();
		}

		return m_lsPrimaryKeyValues.get(p_index);
	}

	public String getStringValue() {
		String strReturn = "";

		try {
			for(DBObject dboPrimaryKeysValue : m_lsPrimaryKeyValues) {
				if(strReturn.length() > 0) {
					strReturn += ", ";
				}

				strReturn += dboPrimaryKeysValue.getString();
			}
		} catch (Exception exception) {
			ExceptionHandler.display(exception);
		}

		return strReturn;

	}

	public String getString() {
		String strReturn = "";

		try {
			for(DBObject dboPrimaryKeysValue : m_lsPrimaryKeyValues) {
				strReturn += "$#";
				strReturn += dboPrimaryKeysValue.getSortString();
			}
		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		}

		return strReturn;
	}
	
	public String toString() {
		return getString();
	}
	
	public int compareTo(PrimaryKeyValue p_pkValue) {
		return this.toString().compareTo(p_pkValue.toString());
	}
}
