package com.laithailibrary.sharelibrary.servicecall;

import com.laithailibrary.sharelibrary.bean.*;
import com.laithailibrary.sharelibrary.interfaceclass.*;
import com.laithailibrary.sharelibrary.session.*;
import exc.*;
import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: dumkrerng
 * Date: 11/2/12
 * Time: 11:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class ServiceCall_DataTableQuery extends ServiceCall_Method {

	private Class<? extends DataTable> m_clsDataTable = null;
//	private Class<? extends DBDataTable> m_clsDBDataTable = null;

	private static final long serialVersionUID = 2;

	public ServiceCall_DataTableQuery() {}

//	public ServiceCall_DataTableQuery(ISessionID p_sessionid, Class<? extends DBDataTable> p_clsDBDataTable, String p_strMethodName_Full,
//		Object[] p_parameters, String p_setServerAddress) throws GException {
//
//		super(p_sessionid, IDataTableQuery.class, p_strMethodName_Full, p_parameters, p_setServerAddress);
//
//		m_clsDBDataTable = p_clsDBDataTable;
//	}

	public ServiceCall_DataTableQuery(ISessionID p_sessionid, Class<? extends DataTable> p_clsDataTable, String p_strMethodName_Full,
		Object[] p_parameters, String p_setServerAddress) throws GException {

		super(p_sessionid, IDataTableQuery.class, p_strMethodName_Full, p_parameters, p_setServerAddress);

		m_clsDataTable = p_clsDataTable;
	}

	public void writeExternal(ObjectOutput out) throws IOException {
		try {
			super.writeExternal(out);

			out.writeObject(m_clsDataTable);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			out.close();

			throw new IOException(exception);
		}
	}

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		try {
			super.readExternal(in);

			m_clsDataTable = (Class<? extends DataTable>)in.readObject();

		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			in.close();

			throw new IOException(exception);
		}
	}

//	public Class<? extends DBDataTable> getClassDBDataTable() {
//		return m_clsDBDataTable;
//	}

	public Class<? extends DataTable> getClassDataTable() {
		return m_clsDataTable;
	}
}
