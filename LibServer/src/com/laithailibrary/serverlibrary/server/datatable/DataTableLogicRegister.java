package com.laithailibrary.serverlibrary.server.datatable;

import com.laithailibrary.sharelibrary.bean.DBDataTable;
import com.laithailibrary.sharelibrary.collection.GMap;
import exc.ExceptionHandler;
import exc.GException;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: dumkrerng
 * Date: 10/31/12
 * Time: 10:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class DataTableLogicRegister {

	private static Map<String, DataTableLogic> m_mapDataTableLogic_DBDataTable = new GMap<>();

	private DataTableLogicRegister() {}

	public static void registerDataTableLogic(Class<? extends DBDataTable> p_class, DataTableLogic p_datatablelogic) throws GException {
		try {
			String strClassName = p_class.getName();

			if(!m_mapDataTableLogic_DBDataTable.containsKey(strClassName)) {
				m_mapDataTableLogic_DBDataTable.put(strClassName, p_datatablelogic);

			} else {
				throw new GException("DataTableLogic duplicate \"" + strClassName + "!");
			}
		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public static DataTableLogic getDataTableLogic(Class<? extends DBDataTable> p_class) throws GException {
		try {
			String strClassName = p_class.getName();

			return m_mapDataTableLogic_DBDataTable.get(strClassName);

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}
}
