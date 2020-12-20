package com.laithailibrary.sharelibrary.interfaceclass;

import com.laithailibrary.sharelibrary.bean.DBDataTable;
import com.laithailibrary.sharelibrary.bean.ExtraBean;
import exc.GException;

/**
 * Created with IntelliJ IDEA.
 * User: dumkrerng
 * Date: 10/31/12
 * Time: 9:34 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IDataTableLogic {
	public abstract void prepareDataOnInsert(DBDataTable p_dbDataTable, ExtraBean p_extrabean) throws GException;
	public abstract void prepareDataOnUpdate(DBDataTable p_dbDataTable, DBDataTable p_dbDataTable_Edit, ExtraBean p_extrabean) throws GException;
	public abstract void prepareDataOnDelete(DBDataTable p_dbDataTable, ExtraBean p_extrabean) throws GException;

	public abstract void verifyDataOnInsert(DBDataTable p_dbDataTable, ExtraBean p_extrabean) throws GException;
	public abstract void verifyDataOnUpdate(DBDataTable p_dbDataTable, DBDataTable p_dbDataTable_Edit, ExtraBean p_extrabean) throws GException;
	public abstract void verifyDataOnDelete(DBDataTable p_dbDataTable, ExtraBean p_extrabean) throws GException;

	public abstract void beforeOnInsert(DBDataTable p_dbDataTable, ExtraBean p_extrabean) throws GException;
	public abstract void beforeOnUpdate(DBDataTable p_dbDataTable, DBDataTable p_dbDataTable_Edit, ExtraBean p_extrabean) throws GException;
	public abstract void beforeOnDelete(DBDataTable p_dbDataTable, ExtraBean p_extrabean) throws GException;

	public abstract void afterOnInsert(DBDataTable p_dbDataTable, ExtraBean p_extrabean) throws GException;
	public abstract void afterOnUpdate(DBDataTable p_dbDataTable, DBDataTable p_dbDataTable_Edit, ExtraBean p_extrabean) throws GException;
	public abstract void afterOnDelete(DBDataTable p_dbDataTable, ExtraBean p_extrabean) throws GException;
}
