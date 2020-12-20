package com.laithailibrary.sharelibrary.register;

import com.laithailibrary.sharelibrary.field.TableID;
import com.laithailibrary.sharelibrary.field.TableName;
import com.laithailibrary.sharelibrary.collection.GMap;
import com.laithailibrary.sharelibrary.collection.GSet;
import com.laithailibrary.sharelibrary.bean.DBDataTable;
import exc.*;
import exc.ExceptionHandler;

import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: dumkrerng
 * Date: 5/13/12
 * Time: 3:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class TableIDRegister {

	private static Map<TableID, TableName> mapTableName_TableID = new GMap<>();
	private static Set<TableID> setTableID = new GSet<>();

	private TableIDRegister() {}

	public static void addTableID(DBDataTable p_dataTable) throws GException {
		try {
			TableID tableid = p_dataTable.getTableID();
			TableName tablename = p_dataTable.getTableName();

			if(tableid.length() > 0) {
				if(setTableID.contains(tableid)) {
					TableName tablename_Temp = mapTableName_TableID.get(tableid);

					StringBuilder bufferError = new StringBuilder();
					bufferError.append("Duplicate TableID!\n");
					bufferError.append("Table ID: ").append(tableid).append('\n');
					bufferError.append("Table Name: ").append(tablename).append('\n');
					bufferError.append("Table Name: ").append(tablename_Temp).append('\n');

					throw new GException(bufferError.toString());

				} else {
					setTableID.add(tableid);
					registerTableID(tablename, tableid);
				}
			} else {
				throw new GException("Table(" + tablename + ") invalid TableID!");
			}
		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public static void registerTableID(TableName p_tablename, TableID p_tableid) throws GException {
		try {
			if(p_tablename.length() <= 0
				|| p_tableid.length() <= 0) {

				throw new GException("Invalid TableName or TableID!");
			}

			if(mapTableName_TableID.containsKey(p_tableid)) {
				TableName tablename_Temp = mapTableName_TableID.get(p_tableid);

				StringBuilder bufferError = new StringBuilder();
				bufferError.append("Duplicate TableID!\n");
				bufferError.append("Table ID: ").append(p_tableid).append('\n');
				bufferError.append("Table Name: ").append(p_tablename).append('\n');
				bufferError.append("Table Name: ").append(tablename_Temp).append('\n');

				throw new GException(bufferError.toString());

			} else {
				mapTableName_TableID.put(p_tableid, p_tablename);
			}
		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}
}
