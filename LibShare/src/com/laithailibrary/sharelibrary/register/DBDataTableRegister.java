package com.laithailibrary.sharelibrary.register;

import com.laithailibrary.logger.GLog;
import com.laithailibrary.sharelibrary.collection.GMap;
import com.laithailibrary.sharelibrary.bean.DBDataTable;
import exc.ExceptionHandler;
import exc.GException;
import pp.ProgramExit;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: dumkrerng
 * Date: 2/14/12
 * Time: 10:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class DBDataTableRegister {

	private static Map<String, Class<? extends DBDataTable>> sMapDBDataTable_ClassName = new GMap<String, Class<? extends DBDataTable>>();

	private DBDataTableRegister() {}

	public static void addDataTable(Class<? extends DBDataTable> p_classDataTable) throws GException {
		try {
			String strSimpleName = p_classDataTable.getSimpleName();

			if(sMapDBDataTable_ClassName.containsKey(strSimpleName)) {
				GLog.severe("Duplicate Register DBDataTable!");
				GLog.info("Program Shutdown.");

				System.exit(ProgramExit.DuplicateRegisterDBDataTable);
			}

			sMapDBDataTable_ClassName.put(strSimpleName, p_classDataTable);

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);

			GLog.info("Program Shutdown.");

			System.exit(ProgramExit.Other);
		}
	}

	public static Map<String, Class<? extends DBDataTable>> getDataBeanClasses() {
		return sMapDBDataTable_ClassName;
	}
}
