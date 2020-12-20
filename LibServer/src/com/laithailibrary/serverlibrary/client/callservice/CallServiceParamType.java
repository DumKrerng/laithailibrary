package com.laithailibrary.serverlibrary.client.callservice;

import com.laithailibrary.sharelibrary.bean.*;
import com.laithailibrary.sharelibrary.collection.*;
import com.laithailibrary.sharelibrary.db.dbobject.*;
import com.laithailibrary.sharelibrary.sqlstatement.*;
import exc.*;
import java.util.*;

public class CallServiceParamType {

	private CallServiceParamType() {}

	public static Class<?>[] getParamTypes(Object[] p_parameters) throws GException {
		return getParamTypes(p_parameters, false);
	}

	public static Class<?>[] getParamTypes_DataTableQuery(Object[] p_parameters) throws GException {
		return getParamTypes(p_parameters, true);
	}

	private static Class<?>[] getParamTypes(Object[] p_parameters, boolean p_bolDataTableQuery) throws GException {
		try {
			Class<?>[] paramtypes = new Class<?>[p_parameters.length];

			int index = -1;

			for(Object objParam : p_parameters) {
				index++;

				Class clsParam = objParam.getClass();

				if(!clsParam.isEnum()
					&& !clsParam.isArray()) {

					if(p_bolDataTableQuery) {
						if(objParam instanceof DBObject) {
							clsParam = DBObject.class;

						} else if(objParam instanceof DBDataTable) {
							clsParam = DBDataTable.class;
						}
					}

					if(objParam instanceof ExtraBean) {
						clsParam = ExtraBean.class;

					} else if(objParam instanceof SQLStatement) {
						clsParam = SQLStatement.class;

					} else if(objParam instanceof GList) {
						clsParam = List.class;

					} else if(objParam instanceof GSet) {
						clsParam = Set.class;

					} else if(objParam instanceof GMap) {
						clsParam = Map.class;

					} else if(objParam instanceof Integer) {
						clsParam = int.class;

					} else if(objParam instanceof String) {
						clsParam = String.class;
					}
				}

				paramtypes[index] = clsParam;
			}

			return paramtypes;

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}
}
