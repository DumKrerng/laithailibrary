package com.laithailibrary.serverlibrary.server.dbcache;

import com.laithailibrary.sharelibrary.collection.GMap;
import exc.ExceptionHandler;
import exc.GException;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: dumkrerng
 * Date: 9/26/12
 * Time: 11:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class GCacheDataTableRegister {

	private static Map<String, GCacheDataTable> s_mapRegister = new GMap<String, GCacheDataTable>();

	private GCacheDataTableRegister() {}

	public static void register(String p_srtClass, GCacheDataTable p_catchaatatable) throws GException {
		try {
			s_mapRegister.put(p_srtClass, p_catchaatatable);

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public static GCacheDataTable getCatchDataTable(String p_srtClass) throws GException {
		try {
		  return s_mapRegister.get(p_srtClass);

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}
}
