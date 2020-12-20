package com.laithailibrary.sharelibrary.bean;

import com.laithailibrary.sharelibrary.collection.GMap;
import exc.*;
import exc.ExceptionHandler;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: dumkrerng
 * Date: 10/6/12
 * Time: 9:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExtraBean implements Externalizable {

	private Map<String, Object> m_mapObject_ObjectName = new GMap<>();

	private static final long serialVersionUID = 10;

	public ExtraBean() {}

	public void writeExternal(ObjectOutput p_out) throws IOException {
		try {
			p_out.writeObject(m_mapObject_ObjectName);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			p_out.close();

			throw new IOException(exception);
		}
	}

	public void readExternal(ObjectInput p_in) throws IOException {
		try {
			m_mapObject_ObjectName = (Map<String, Object>)p_in.readObject();

		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			p_in.close();

			throw new IOException(exception);
		}
	}

	protected void addObject(String p_strObjectName, Object p_object) throws GException {
		try {
			m_mapObject_ObjectName.put(p_strObjectName, p_object);

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	protected Object getObject(String p_strObjectName) throws GException {
		try {
		  return m_mapObject_ObjectName.get(p_strObjectName);

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}
}
