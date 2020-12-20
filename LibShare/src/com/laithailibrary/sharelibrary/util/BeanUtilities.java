package com.laithailibrary.sharelibrary.util;

import com.laithailibrary.sharelibrary.field.DataField;
import com.laithailibrary.sharelibrary.field.FieldPrimaryKey;
import com.laithailibrary.sharelibrary.field.TableName;
import com.laithailibrary.sharelibrary.collection.GList;
import com.laithailibrary.sharelibrary.bean.DBDataTable;
import com.laithailibrary.sharelibrary.db.dbutilities.BaseConnection;
import com.laithailibrary.sharelibrary.db.dbutilities.DBUtilities;
import com.laithailibrary.sharelibrary.db.dbutilities.GResultSet;
import com.laithailibrary.sharelibrary.db.dbutilities.GStatement;
import com.laithailibrary.sharelibrary.db.dbobject.DBObject;
import com.laithailibrary.sharelibrary.session.*;
import exc.ExceptionHandler;
import exc.GException;
import com.laithailibrary.sharelibrary.sqlstatement.SQLStatement;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dumkrerng
 * Date: 6/18/12
 * Time: 10:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class BeanUtilities {

	private BeanUtilities() {}

	public static void verifyNotDuplicateField(BaseConnection p_baseConnection, TableName p_tablename, DBDataTable p_dbDataTable,
		DataField p_datafield, ISessionID p_sessionid) throws GException {

		verifyNotDuplicateField(p_baseConnection, p_tablename, p_dbDataTable, p_datafield, SQLStatement.EmptyStatement, p_sessionid);
	}

	public static void verifyNotDuplicateField(BaseConnection p_baseConnection, TableName p_tablename, DBDataTable p_dbDataTable,
		DataField p_datafield, SQLStatement p_sqlStatement, ISessionID p_sessionid) throws GException {

		verifyNotDuplicateField(p_baseConnection, p_tablename, p_dbDataTable, p_datafield, p_sqlStatement, "", p_sessionid);
	}

	public static void verifyNotDuplicateField(BaseConnection p_baseConnection, TableName p_tablename, DBDataTable p_dbDataTable,
		DataField p_datafield, SQLStatement p_sqlStatement, String strMessageDisplay, ISessionID p_sessionid) throws GException {

		verifyNotDuplicateFields(p_baseConnection, p_tablename, p_dbDataTable, new GList<DataField>(p_datafield), p_sqlStatement,
			strMessageDisplay, p_sessionid);
	}

	public static void verifyNotDuplicateFields(BaseConnection p_baseConnection, TableName p_tablename, DBDataTable p_dbDataTable,
		List<? extends DataField> p_lsDataFields_Exception, ISessionID p_sessionid) throws GException {

		verifyNotDuplicateFields(p_baseConnection, p_tablename, p_dbDataTable, p_lsDataFields_Exception, SQLStatement.EmptyStatement,
			p_sessionid);
	}

	public static void verifyNotDuplicateFields(BaseConnection p_baseConnection, TableName p_tablename, DBDataTable p_dbDataTable,
		List<? extends DataField> p_lsDataFields_Exception, SQLStatement p_sqlStatement, ISessionID p_sessionid) throws GException {

		verifyNotDuplicateFields(p_baseConnection, p_tablename, p_dbDataTable, p_lsDataFields_Exception, p_sqlStatement, "",
			p_sessionid);
	}

	public static void verifyNotDuplicateFields(BaseConnection p_baseConnection, TableName p_tablename, DBDataTable p_dbDataTable,
		List<? extends DataField> p_lsDataFields_Exception, SQLStatement p_sqlStatement, String strMessageDisplay, ISessionID p_sessionid) throws GException {

		try {
			FieldPrimaryKey fieldPrimaryKey = p_dbDataTable.getFieldPrimaryKey();
			String strPrimaryKeyName = fieldPrimaryKey.getPrimaryKeyName();

			StringBuilder builder = new StringBuilder();
			builder.append("SELECT *\n");
			builder.append("FROM ").append(p_tablename.getTableName()).append("\n");
			builder.append("WHERE (");

			StringBuilder builderFilter = new StringBuilder();

			for(DataField datafield : p_lsDataFields_Exception) {
				String strFieldName = datafield.getFieldName();
				DBObject dboValue = p_dbDataTable.getData(datafield);

				builderFilter.append(strFieldName).append(" = ").append(dboValue.getStringSQL()).append("\nAND ");
			}

			builderFilter = new StringBuilder(builderFilter.substring(0, builderFilter.length() - 4));
			builderFilter.append(")");

			builder.append(builderFilter).append("\n");

			if(p_sqlStatement.isSet()) {
				builder.append("AND ").append(p_sqlStatement.getSQLString(DBUtilities.getConnection(p_sessionid))).append("\n");
			}

			DBObject dbObject = p_dbDataTable.getData(strPrimaryKeyName);

			if(dbObject.isValid()) {
				builder.append("AND ").append(strPrimaryKeyName).append(" != ").append(dbObject.getStringSQL());
			}

			GStatement stm = new GStatement(p_baseConnection);
			GResultSet rst = stm.executeQuery(builder);

			if(rst.next()) {
				StringBuilder builderError = new StringBuilder();

				if(strMessageDisplay.length() > 0) {
					builderError.append(strMessageDisplay);

				} else {
					builderError.append("Field[");

					for(DataField datafield : p_lsDataFields_Exception) {
						String strFieldName = datafield.getFieldName();

						builderError.append(strFieldName).append(",");
					}

					builderError = new StringBuilder(builderError.substring(0, builderError.length() - 1));
					builderError.append("] ");

					if(p_lsDataFields_Exception.size() == 1) {
						builderError.append("is ");

					} else {
						builderError.append("are ");
					}

					builderError.append("duplicate on Table(").append(p_tablename.getTableName()).append(")\n");
					builderError.append("Value[");

					for(DataField datafield : p_lsDataFields_Exception) {
						DBObject dboValue = p_dbDataTable.getData(datafield);

						builderError.append(dboValue.getString()).append(",");
					}

					builderError = new StringBuilder(builderError.substring(0, builderError.length() - 1));
					builderError.append("] ");
				}

				throw new GException(builderError.toString());
			}
		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}
}
