package com.laithailibrary.serverlibrary.server.datatable;

import com.laithailibrary.sharelibrary.bean.*;
import com.laithailibrary.sharelibrary.session.*;
import exc.*;

/**
 * Created with IntelliJ IDEA.
 * User: dumkrerng
 * Date: 10/29/12
 * Time: 11:08 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class DataTableLogic<T extends DBDataTable> {

	protected ISessionID m_sessionid;

	public void setSessionID(ISessionID p_sessionid) {
		m_sessionid = p_sessionid;
	}
	
	public abstract void prepareDataOnInsert(T p_dbDataTable, ExtraBean p_extrabean) throws GException;
	public abstract void prepareDataOnUpdate(T p_dbDataTable, T p_dbDataTable_Edit, final ExtraBean p_extrabean) throws GException;
	public abstract void prepareDataOnDelete(T p_dbDataTable, final ExtraBean p_extrabean) throws GException;

	public abstract void verifyDataOnInsert(T p_dbDataTable, final ExtraBean p_extrabean) throws GException;
	public abstract void verifyDataOnUpdate(T p_dbDataTable, T p_dbDataTable_Edit, final ExtraBean p_extrabean) throws GException;
	public abstract void verifyDataOnDelete(T p_dbDataTable, final ExtraBean p_extrabean) throws GException;

	public abstract void beforeOnInsert(T p_dbDataTable, final ExtraBean p_extrabean) throws GException;
	public abstract void beforeOnUpdate(T p_dbDataTable, T p_dbDataTable_Edit, final ExtraBean p_extrabean) throws GException;
	public abstract void beforeOnDelete(T p_dbDataTable, final ExtraBean p_extrabean) throws GException;

	public abstract void afterOnInsert(T p_dbDataTable, final ExtraBean p_extrabean) throws GException;
	public abstract void afterOnUpdate(T p_dbDataTable, T p_dbDataTable_Edit, final ExtraBean p_extrabean) throws GException;
	public abstract void afterOnDelete(T p_dbDataTable, final ExtraBean p_extrabean) throws GException;
}
