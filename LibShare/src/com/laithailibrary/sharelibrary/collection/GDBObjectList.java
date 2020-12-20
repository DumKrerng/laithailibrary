package com.laithailibrary.sharelibrary.collection;

import com.laithailibrary.sharelibrary.db.dbobject.*;
import com.laithailibrary.sharelibrary.db.dbutilities.*;
import exc.*;
import java.io.*;
import java.util.*;

public class GDBObjectList<T extends DBObject> implements Externalizable {

	private GList<T> m_list = new GList<>();
	private GSet<T> m_set = new GSet<>();

	private static final long serialVersionUID = 4;

	public GDBObjectList() {}

	public GDBObjectList(GList<T> p_list) throws GException {
		try {
			for(T value : p_list) {
				add(value);
			}
		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public void writeExternal(ObjectOutput out) throws IOException {
		try {
			out.writeObject(m_list);
			out.writeObject(m_set);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			out.close();

			throw new IOException(exception);
		}
	}

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		try {
			m_list = (GList<T>)in.readObject();
			m_set = (GSet<T>)in.readObject();

		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			in.close();

			throw new IOException(exception);
		}
	}

	public void add(T p_value) throws GException {
		try {
			if(!m_set.contains(p_value)) {
				m_set.add(p_value);
				m_list.add(p_value);
			}
		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public void remove(T p_value) throws GException {
		try {
			if(m_set.contains(p_value)) {
				m_set.remove(p_value);

				int intSize = m_list.size();

				for(int index = 0; index < intSize; index++) {
					T value = m_list.get(index);

					if(value.toString().compareTo(p_value.toString()) == 0) {
						m_list.remove(index);

						return;
					}
				}
			}
		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public void removeAll() throws GException {
		try {
			m_set = new GSet<>();
			m_list = new GList<>();

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public T getValue(int index) {
		return m_list.get(index);
	}

	public List<T> getList() {
		return m_list;
	}

	public boolean contains(T p_value) {
		return m_set.contains(p_value);
	}

	public int size() {
		return m_list.size();
	}
	
	public String getSQLString_IN() throws GException {
		return getSQLString("IN");
	}

	public String getSQLString_NOT_IN() throws GException {
		return getSQLString("NOT IN");
	}

	private String getSQLString(String p_strOperator) throws GException {
		String strSQLString = "";

		try {
			DBType dbtype = DBUtilities.getDBType();
			int intListLimit = 1000;

			if(dbtype != DBType.SQLite) {
				intListLimit = 2000;
			}

			if(m_list.size() <= intListLimit) {
				if(m_list.size() > 0) {
					StringBuilder buffer = new StringBuilder();

					if(p_strOperator.length() > 0) {
						buffer.append(p_strOperator).append(' ');
					}

					buffer.append('(');

					int intCount = 0;

					for(DBObject dbObject : m_list) {
						if(intCount > 0
							&& intCount % 10 == 0) {

							buffer.append('\n');
						}

						buffer.append(dbObject.getStringSQL()).append(", ");
						intCount++;
					}

					buffer = new StringBuilder(buffer.substring(0, buffer.length() - 2));
					buffer.append(')');

					strSQLString = buffer.toString();
				}
			} else {
				throw new GException("Not support List size over " + intListLimit + "!!!");
			}
		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
		
		return strSQLString;
	}
}
