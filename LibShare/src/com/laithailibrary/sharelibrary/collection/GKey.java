package com.laithailibrary.sharelibrary.collection;

import java.io.*;
import java.util.*;
import com.laithailibrary.sharelibrary.db.dbobject.*;
import exc.*;

/**
 * Created by dumkrerng on 31/3/2557.
 */
public class GKey implements Comparable<GKey>, Externalizable {

	private List<Object> m_lsKeyValues;

	private static final long serialVersionUID = 2;

	private GKey() {}

	public GKey(int p_size) {
		m_lsKeyValues = new GList<>(p_size);
	}

	public void writeExternal(ObjectOutput out) throws IOException {
		try {
			out.writeObject(m_lsKeyValues);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			out.close();

			throw new IOException(exception);
		}
	}

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		try {
			m_lsKeyValues = (List<Object>)in.readObject();

		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			in.close();

			throw new IOException(exception);
		}
	}

	public void addValue(int p_index, Object p_object) throws GException {
		try {
		  m_lsKeyValues.add(p_index, p_object);

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public Object getValue(int p_index) throws GException {
		try {
		  return m_lsKeyValues.get(p_index);

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public String getStringSort() {
		String strStringSort = "";

		try {
			for(Object objKeyValue : m_lsKeyValues) {
				String strValue;

				if(objKeyValue instanceof DBObject) {
					strValue = ((DBObject)objKeyValue).getSortString();

				} else {
					DBString dbstrTemp = new DBString(objKeyValue.toString());
					strValue = dbstrTemp.getSortString();
				}

				strStringSort += strValue;
			}
		} catch(Exception exception) {
			ExceptionHandler.display(exception);
		}

		return strStringSort;
	}

	public String toString() {
		return getStringSort();
	}

	public int compareTo(GKey p_key) {
		return this.getStringSort().compareTo(p_key.getStringSort());
	}
}
