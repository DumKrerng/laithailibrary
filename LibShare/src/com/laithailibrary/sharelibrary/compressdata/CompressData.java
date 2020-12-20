package com.laithailibrary.sharelibrary.compressdata;

import java.io.*;
import java.util.*;
import com.laithailibrary.sharelibrary.collection.*;
import exc.*;

/**
 * Created by dumkrerng on 23/4/2559.
 */
public class CompressData implements Externalizable, Cloneable {

	private Map<Integer, String> m_mapValue_CompressKey = new GMap<>();
	private Map<String, Integer> m_mapCompressKey_Value = new GMap<>();

	private static final long serialVersionUID = 1;

	public CompressData() {
		m_mapValue_CompressKey = new GMap<>();
		m_mapCompressKey_Value = new GMap<>();
	}

	public void writeExternal(ObjectOutput out) throws IOException {
		try {
			out.writeObject(m_mapValue_CompressKey);
			out.writeObject(m_mapCompressKey_Value);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			out.close();

			throw new IOException(exception);
		}
	}

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		try {
			m_mapValue_CompressKey = (Map<Integer, String>)in.readObject();
			m_mapCompressKey_Value = (Map<String, Integer>)in.readObject();

		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			in.close();

			throw new IOException(exception);
		}
	}

	public boolean containsValue(String p_strValue) throws GException {
		return m_mapCompressKey_Value.containsKey(p_strValue);
	}

	public Map<Integer, String> getMapValue_CompressKey() {
		return m_mapValue_CompressKey;
	}

	public Integer addValue(String p_strValue) throws GException {
		try {
			Integer intCompressKey;

			if(!m_mapCompressKey_Value.containsKey(p_strValue)) {
				intCompressKey = m_mapCompressKey_Value.size();

				m_mapCompressKey_Value.put(p_strValue, intCompressKey);
				m_mapValue_CompressKey.put(intCompressKey, p_strValue);

			} else {
				intCompressKey = m_mapCompressKey_Value.get(p_strValue);
			}

			return intCompressKey;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public String getValue(Integer p_intCompressKey) throws GException {
		try {
		  String strValue = "";

			if(p_intCompressKey != null) {
				if(m_mapValue_CompressKey.containsKey(p_intCompressKey)) {
					strValue = m_mapValue_CompressKey.get(p_intCompressKey);
				}
			}

			return strValue;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public Integer getCompressKey(String p_strValue) throws GException {
		try {
			Integer intCompressKey = -1;

			if(p_strValue != null) {
				if(m_mapCompressKey_Value.containsKey(p_strValue)) {
					intCompressKey = m_mapCompressKey_Value.get(p_strValue);
				}
			}

			return intCompressKey;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public void removeCompressKeys(Set<Integer> p_intCompressKeys) throws GException {
		try {
		  for(Integer intCompressKey : p_intCompressKeys) {
			  removeCompressKey(intCompressKey);
		  }
		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public void removeCompressKey(Integer p_intCompressKey) throws GException {
		String strValue = m_mapValue_CompressKey.remove(p_intCompressKey);
		Integer intCompressKey = m_mapCompressKey_Value.remove(strValue);

		if(!p_intCompressKey.equals(intCompressKey)) {
			throw new GException("Compress Data Error!!!");
		}
	}

	public CompressData clone() throws CloneNotSupportedException {
		try {
			CompressData compressdata_New = new CompressData();

			for(Map.Entry<Integer, String> entValue : m_mapValue_CompressKey.entrySet()) {
				String strValue = entValue.getValue();
				compressdata_New.addValue(strValue);
			}

			return compressdata_New;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
			throw new CloneNotSupportedException(exception.getMessage());
		}
	}
}
