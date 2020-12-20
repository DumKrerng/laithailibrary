package com.laithailibrary.serverlibrary.server.serviceserver;

import java.io.*;
import java.util.*;
import com.laithailibrary.logger.*;
import com.laithailibrary.sharelibrary.bean.*;
import com.laithailibrary.sharelibrary.collection.*;
import com.laithailibrary.sharelibrary.db.dbobject.*;
import com.laithailibrary.sharelibrary.db.dbutilities.*;
import com.laithailibrary.sharelibrary.field.*;
import com.laithailibrary.sharelibrary.file.*;
import com.laithailibrary.sharelibrary.register.*;
import com.laithailibrary.sharelibrary.session.*;
import com.laithailibrary.sharelibrary.util.*;
import exc.*;
import pp.*;

/**
 * Created by IntelliJ IDEA.
 * User: dumkrerng
 * Date: 4/4/12
 * Time: 10:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class CreateDataBase {

	private CreateDataBase() {}

	public static void createNewDB(SessionID p_sessionid) throws GException {
		BaseConnection connection = DBUtilities.getConnection(p_sessionid);

		try {
			DBType dbtype = DBUtilities.getDBType();
			connection.transactionBegin();

			if(dbtype == DBType.SQLite) {
				createNewDB_SQLite(connection);

			} else if(dbtype == DBType.MySQL) {
				createNewDB_MySQL(connection);

			} else {
				createNewDB_Other(connection);
			}

			GLog.info("Create DataBase completed.");

			List<File> lsSQLScripts = GFile.getFiles("SQLScriptOnNewDB");

			if(lsSQLScripts.size() > 0) {
				GLog.info("Running Default SQLScript . . .");

				for(File scriptsql : lsSQLScripts) {
					if(scriptsql.exists()) {

						List<String> lsTexts = GTextFile.readFromFile(scriptsql);
						GLog.info("Execute SQLScript File: " + scriptsql.getName());

						StringBuilder buffer = new StringBuilder();

						for(String strText : lsTexts) {
							if(strText.length() > 0) {
								buffer.append('\n').append(strText);
							}

							if(buffer.toString().endsWith(";")) {
								GStatement stm = new GStatement(connection);
								stm.executeUpdate(buffer.toString());

								buffer = new StringBuilder();
							}
						}
					}
				}

				GLog.info("Run Default SQLScript Successful.");
			}

			connection.commit();

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);

			connection.rollback();

			GLog.severe("Can not create new DB!");

			System.exit(ProgramExit.CanNotCreateNewDB);
		}
	}

	private static void createNewDB_Other(BaseConnection p_connection) throws GException {
		try {
			Map<String, Class<? extends DBDataTable>> mapDataBean_ClassName = DBDataTableRegister.getDataBeanClasses();
			Map<FieldPrimaryKey, Set<FieldForeignKey>> mapFieldForeignKeys_FieldPrimaryKey = ForeignKey.getMapForeignKeys();
			Set<FieldForeignKey> setFieldForeignKeys = new GSet<>();
			Map<TableName, Map<String, FieldUnique>> mapFieldUniques_TableName = new GMap<>();

			for(String strClassName : mapDataBean_ClassName.keySet()) {
				DBDataTable dbDataTable = mapDataBean_ClassName.get(strClassName).newInstance();
				Map<String, FieldUnique> mapFieldUnique_Name = dbDataTable.getMapFieldUnique_Name();

				mapFieldUniques_TableName.put(dbDataTable.getTableName(), mapFieldUnique_Name);
			}

			for(String strClassName : mapDataBean_ClassName.keySet()) {
				DBDataTable dbDataTable = mapDataBean_ClassName.get(strClassName).newInstance();
				TableName tablename = dbDataTable.getTableName();
				String strFieldPrimaryKeyName = dbDataTable.getFieldPrimaryKeyName();
				DataField datafield_PrimaryKey = dbDataTable.getDataField(strFieldPrimaryKeyName);

				List<FieldForeignKey> lsFieldForeignKeys = dbDataTable.getFieldForeignKeys();
				Set<DataField> setDataField_ForeignKeys = new GSet<>();

				for(FieldForeignKey fieldforeignkey : lsFieldForeignKeys) {
					DataField datafield_Temp = fieldforeignkey.getDataField_ForeignKey();

					if(fieldforeignkey.getSizeReferentPrimaryKey() == 1) {
						setDataField_ForeignKeys.add(datafield_Temp);
						setFieldForeignKeys.add(fieldforeignkey);
					}
				}

				GLog.info("Create Table " + tablename.getTableName() + ". . .");

				Class clsData = datafield_PrimaryKey.getClassData();

				StringBuilder builder = new StringBuilder();
				builder.append("CREATE TABLE ").append(tablename.getTableName()).append("(\n");
				builder.append("\t").append(strFieldPrimaryKeyName).append(" ");

				if(clsData.newInstance() instanceof DBInteger) {
					builder.append(" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n");

				} else if(clsData.newInstance() instanceof DBString) {
					builder.append(" VARCHAR(").append(TableIDUtilities.SIZE_PrimaryKeyID).append(") PRIMARY KEY NOT NULL,\n");

				} else {
					String strSimpleName = datafield_PrimaryKey.getClassData().getSimpleName();

					throw new GException(strSimpleName + " not support Primary Key!");
				}

				for(String strFieldName : dbDataTable.getFieldNames()) {
					if(strFieldName.compareTo(strFieldPrimaryKeyName) != 0) {
						DataField datafield = dbDataTable.getDataField(strFieldName);

						if(!setDataField_ForeignKeys.contains(datafield)) {
							builder.append("\t").append(getSQLString_DataField(datafield)).append(",\n");
						}
					}
				}

				builder = new StringBuilder(builder.substring(0, builder.length() - 2));
				builder.append(')');

				GStatement stm = new GStatement(p_connection);
				stm.executeCreateTable(builder.toString());
			}

		//todo Create ForeignKey
			GLog.info("Create Foreign Key. . .");

			for(FieldPrimaryKey fieldprimarykey : mapFieldForeignKeys_FieldPrimaryKey.keySet()) {
				Set<FieldForeignKey> setFieldForeignKey = mapFieldForeignKeys_FieldPrimaryKey.get(fieldprimarykey);

				for(FieldForeignKey fieldforeignkey : setFieldForeignKey) {
					if(setFieldForeignKeys.contains(fieldforeignkey)) {
						DataField datafield_ForeignKey = fieldforeignkey.getDataField_ForeignKey();
						String strFieldForeignKey_Name = fieldforeignkey.getFieldName_ForeignKey();
						TableName tablenameForeignKey = fieldforeignkey.getTableName_ForeignKey();

						String strPrimaryKeyName = fieldprimarykey.getPrimaryKeyName();
						TableName tablenamePrimaryKey = fieldprimarykey.getTableName();

						StringBuilder builder = new StringBuilder();
						builder.append("ALTER TABLE ").append(tablenameForeignKey.getTableName()).append('\n');
						builder.append("ADD ").append(getSQLString_DataField(datafield_ForeignKey)).append('\n');
						builder.append("CONSTRAINT ").append("FK_").append(tablenameForeignKey.getTableName()).append('_').append(strFieldForeignKey_Name).append('\n');
//					builder.append("FOREIGN KEY(").append(strFieldForeignKey_Name).append(")\n");
						builder.append("REFERENCES ").append(tablenamePrimaryKey.getTableName()).append('(').append(strPrimaryKeyName).append(")\n");
						builder.append("ON UPDATE RESTRICT ON DELETE RESTRICT");

						GStatement stm = new GStatement(p_connection);
						stm.executeCreateTable(builder.toString());
					}
				}
			}

		//todo Create Unique
			for(TableName tablename : mapFieldUniques_TableName.keySet()) {
				GLog.info("Create Unique at " + tablename.getTableName() + ". . .");

				StringBuilder builderTable = new StringBuilder();
				builderTable.append("ALTER TABLE ").append(tablename.getTableName()).append('\n');

				Map<String, FieldUnique> mapFieldUnique_Name = mapFieldUniques_TableName.get(tablename);

				for(String strFieldUnique_Name : mapFieldUnique_Name.keySet()) {
					StringBuilder builder = new StringBuilder();
					builder.append("ADD CONSTRAINT ").append(strFieldUnique_Name).append('_').append(tablename.getTableName()).append('\n');
					builder.append("UNIQUE(");

					FieldUnique fieldunique = mapFieldUnique_Name.get(strFieldUnique_Name);
					List<? extends DataField> lsDataFields = fieldunique.getDataFields();

					for(DataField datafield : lsDataFields) {
						String strFieldName = datafield.getFieldName();
						builder.append(strFieldName).append(", ");
					}

					builder = new StringBuilder(builder.substring(0, builder.length() - 2));
					builder.append(')');

					builder = new StringBuilder(builderTable).append(builder);

					GStatement stm = new GStatement(p_connection);
					stm.executeCreateTable(builder.toString());
				}
			}

		//todo Create Index
			for(String strClassName : mapDataBean_ClassName.keySet()) {
				DBDataTable dbDataTable = mapDataBean_ClassName.get(strClassName).newInstance();
				TableName tablename = dbDataTable.getTableName();

				List<DataField> fieldIndexs = dbDataTable.getFieldIndexs();
				List<String> lsSQLCreateIndexs = new GList<>();

				for(DataField datafield : fieldIndexs) {
					String strFieldName = datafield.getFieldName();

					StringBuilder builder = new StringBuilder();
					builder.append("CREATE INDEX ").append("IX_").append(tablename.getTableName()).append('_').append(strFieldName).append('\n');
					builder.append("ON ").append(tablename.getTableName()).append('(').append(strFieldName).append(')');

					lsSQLCreateIndexs.add(builder.toString());
				}

				if(lsSQLCreateIndexs.size() > 0) {
					GLog.info("Create Index at " + tablename.getTableName() + ". . .");

					for(String strSQLCreateIndex : lsSQLCreateIndexs) {
						GStatement stm = new GStatement(p_connection);
						stm.executeCreateTable(strSQLCreateIndex);
					}
				}
			}
		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	private static void createNewDB_MySQL(BaseConnection p_connection) throws GException {
		try {
			Map<String, Class<? extends DBDataTable>> mapDataBean_ClassName = DBDataTableRegister.getDataBeanClasses();
			Map<FieldPrimaryKey, Set<FieldForeignKey>> mapFieldForeignKeys_FieldPrimaryKey = ForeignKey.getMapForeignKeys();
			Set<FieldForeignKey> setFieldForeignKeys = new GSet<>();
			Map<TableName, Map<String, FieldUnique>> mapFieldUniques_TableName = new GMap<>();

			for(String strClassName : mapDataBean_ClassName.keySet()) {
				DBDataTable dbDataTable = mapDataBean_ClassName.get(strClassName).newInstance();
				Map<String, FieldUnique> mapFieldUnique_Name = dbDataTable.getMapFieldUnique_Name();

				mapFieldUniques_TableName.put(dbDataTable.getTableName(), mapFieldUnique_Name);
			}

			for(String strClassName : mapDataBean_ClassName.keySet()) {
				DBDataTable dbDataTable = mapDataBean_ClassName.get(strClassName).newInstance();
				TableName tablename = dbDataTable.getTableName();
				String strFieldPrimaryKeyName = dbDataTable.getFieldPrimaryKeyName();
				DataField datafield_PrimaryKey = dbDataTable.getDataField(strFieldPrimaryKeyName);

				List<FieldForeignKey> lsFieldForeignKeys = dbDataTable.getFieldForeignKeys();
				Set<DataField> setDataField_ForeignKeys = new GSet<>();

				for(FieldForeignKey fieldforeignkey : lsFieldForeignKeys) {
					DataField datafield_Temp = fieldforeignkey.getDataField_ForeignKey();

					if(fieldforeignkey.getSizeReferentPrimaryKey() == 1) {
						setDataField_ForeignKeys.add(datafield_Temp);
						setFieldForeignKeys.add(fieldforeignkey);
					}
				}

				GLog.info("Create Table " + tablename.getTableName() + ". . .");

				Class clsData = datafield_PrimaryKey.getClassData();

				StringBuilder builder = new StringBuilder();
				builder.append("CREATE TABLE ").append(tablename.getTableName()).append("(\n");
				builder.append("\t").append(strFieldPrimaryKeyName).append(" ");

				if(clsData.newInstance() instanceof DBInteger) {
					builder.append(" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n");

				} else if(clsData.newInstance() instanceof DBString) {
					builder.append(" VARCHAR(").append(TableIDUtilities.SIZE_PrimaryKeyID).append(") PRIMARY KEY NOT NULL,\n");

				} else {
					String strSimpleName = datafield_PrimaryKey.getClassData().getSimpleName();

					throw new GException(strSimpleName + " not support Primary Key!");
				}

				for(String strFieldName : dbDataTable.getFieldNames()) {
					if(strFieldName.compareTo(strFieldPrimaryKeyName) != 0) {
						DataField datafield = dbDataTable.getDataField(strFieldName);

						if(!setDataField_ForeignKeys.contains(datafield)) {
							builder.append("\t").append(getSQLString_DataField(datafield)).append(",\n");
						}
					}
				}

				builder = new StringBuilder(builder.substring(0, builder.length() - 2));
				builder.append(')');

				GStatement stm = new GStatement(p_connection);
				stm.executeCreateTable(builder.toString());
			}

		//todo Create ForeignKey
			GLog.info("Create Foreign Key. . .");

			for(FieldPrimaryKey fieldprimarykey : mapFieldForeignKeys_FieldPrimaryKey.keySet()) {
				Set<FieldForeignKey> setFieldForeignKey = mapFieldForeignKeys_FieldPrimaryKey.get(fieldprimarykey);

				for(FieldForeignKey fieldforeignkey : setFieldForeignKey) {
					if(setFieldForeignKeys.contains(fieldforeignkey)) {
						DataField datafield_ForeignKey = fieldforeignkey.getDataField_ForeignKey();
						String strFieldForeignKey_Name = fieldforeignkey.getFieldName_ForeignKey();
						TableName tablenameForeignKey = fieldforeignkey.getTableName_ForeignKey();

						String strPrimaryKeyName = fieldprimarykey.getPrimaryKeyName();
						TableName tablenamePrimaryKey = fieldprimarykey.getTableName();

						StringBuilder builder = new StringBuilder();
						builder.append("ALTER TABLE ").append(tablenameForeignKey.getTableName()).append('\n');
						builder.append("ADD ").append(getSQLString_DataField(datafield_ForeignKey)).append('\n');

						GStatement stm = new GStatement(p_connection);
						stm.executeCreateTable(builder.toString());

						builder = new StringBuilder();
						builder.append("ALTER TABLE ").append(tablenameForeignKey.getTableName()).append('\n');
						builder.append("ADD CONSTRAINT ").append("FK_").append(tablenameForeignKey.getTableName()).append('_').append(strFieldForeignKey_Name).append('\n');
						builder.append("FOREIGN KEY(").append(strFieldForeignKey_Name).append(")\n");
						builder.append("REFERENCES ").append(tablenamePrimaryKey.getTableName()).append('(').append(strPrimaryKeyName).append(")\n");

						stm.executeCreateTable(builder.toString());
					}
				}
			}

		//todo Create Unique
			for(TableName tablename : mapFieldUniques_TableName.keySet()) {
				GLog.info("Create Unique at " + tablename.getTableName() + ". . .");

				StringBuilder builderTable = new StringBuilder();
				builderTable.append("ALTER TABLE ").append(tablename.getTableName()).append('\n');

				Map<String, FieldUnique> mapFieldUnique_Name = mapFieldUniques_TableName.get(tablename);

				for(String strFieldUnique_Name : mapFieldUnique_Name.keySet()) {
					StringBuilder builder = new StringBuilder();
					builder.append("ADD CONSTRAINT ").append(strFieldUnique_Name).append('_').append(tablename.getTableName()).append('\n');
					builder.append("UNIQUE(");

					FieldUnique fieldunique = mapFieldUnique_Name.get(strFieldUnique_Name);
					List<? extends DataField> lsDataFields = fieldunique.getDataFields();

					for(DataField datafield : lsDataFields) {
						String strFieldName = datafield.getFieldName();
						builder.append(strFieldName).append(", ");
					}

					builder = new StringBuilder(builder.substring(0, builder.length() - 2));
					builder.append(')');

					builder = new StringBuilder(builderTable).append(builder);

					GStatement stm = new GStatement(p_connection);
					stm.executeCreateTable(builder.toString());
				}
			}

		//todo Create Index
			for(String strClassName : mapDataBean_ClassName.keySet()) {
				DBDataTable dbDataTable = mapDataBean_ClassName.get(strClassName).newInstance();
				TableName tablename = dbDataTable.getTableName();

				List<DataField> fieldIndexs = dbDataTable.getFieldIndexs();
				List<String> lsSQLCreateIndexs = new GList<>();

				for(DataField datafield : fieldIndexs) {
					String strFieldName = datafield.getFieldName();

					StringBuilder builder = new StringBuilder();
					builder.append("CREATE INDEX ").append("IX_").append(tablename.getTableName()).append('_').append(strFieldName).append('\n');
					builder.append("ON ").append(tablename.getTableName()).append('(').append(strFieldName).append(')');

					lsSQLCreateIndexs.add(builder.toString());
				}

				if(lsSQLCreateIndexs.size() > 0) {
					GLog.info("Create Index at " + tablename.getTableName() + ". . .");

					for(String strSQLCreateIndex : lsSQLCreateIndexs) {
						GStatement stm = new GStatement(p_connection);
						stm.executeCreateTable(strSQLCreateIndex);
					}
				}
			}
		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	private static void createNewDB_SQLite(BaseConnection p_connection) throws GException {
		try {
			Map<String, Class<? extends DBDataTable>> mapDataBean_ClassName = DBDataTableRegister.getDataBeanClasses();
			Map<String, StringBuilder> mapSQLCreateTable_TableName = new GMap<>();

			for(String strClassName : mapDataBean_ClassName.keySet()) {
				DBDataTable dbDataTable = mapDataBean_ClassName.get(strClassName).newInstance();
				TableName tablename = dbDataTable.getTableName();
				String strFieldPrimaryKeyName = dbDataTable.getFieldPrimaryKeyName();
				DataField datafield_PrimaryKey = dbDataTable.getDataField(strFieldPrimaryKeyName);

				List<FieldForeignKey> lsFieldForeignKeys = dbDataTable.getFieldForeignKeys();
				Set<DataField> setDataField_ForeignKeys = new GSet<>();

				for(FieldForeignKey fieldforeignkey : lsFieldForeignKeys) {
					DataField datafield_Temp = fieldforeignkey.getDataField_ForeignKey();

					if(fieldforeignkey.getSizeReferentPrimaryKey() == 1) {
						setDataField_ForeignKeys.add(datafield_Temp);
					}
				}

				Class claData = datafield_PrimaryKey.getClassData();

				StringBuilder builder = new StringBuilder();
				builder.append("CREATE TABLE ").append(tablename.getTableName()).append("(\n");
				builder.append("\t").append(strFieldPrimaryKeyName).append(" ");

				if(claData.newInstance() instanceof DBInteger) {
					builder.append(" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n");

				} else if(claData.newInstance() instanceof DBString) {
					builder.append(" VARCHAR(").append(TableIDUtilities.SIZE_PrimaryKeyID).append(") PRIMARY KEY NOT NULL,\n");

				} else {
					String strSimpleName = datafield_PrimaryKey.getClassData().getSimpleName();

					throw new GException(strSimpleName + " not support Primary Key!");
				}

				boolean bolHaveForeignKey = false;

				for(String strFieldName : dbDataTable.getFieldNames()) {
					if(strFieldName.compareTo(strFieldPrimaryKeyName) != 0) {
						DataField datafield = dbDataTable.getDataField(strFieldName);

						if(!setDataField_ForeignKeys.contains(datafield)) {
							builder.append("\t").append(getSQLString_DataField(datafield)).append(",\n");

						} else {
							bolHaveForeignKey = true;
						}
					}
				}

				builder = new StringBuilder(builder.substring(0, builder.length() - 2));

				if(bolHaveForeignKey) {
					mapSQLCreateTable_TableName.put(strClassName, new StringBuilder(builder));
				}

				builder.append(')');

				GLog.info("Create Table " + tablename.getTableName() + " . . .");

				GStatement stm = new GStatement(p_connection);
				stm.executeCreateTable(builder.toString());
			}

	//todo Create ForeignKey
			for(String strClassName : mapSQLCreateTable_TableName.keySet()) {
				DBDataTable dbDataTable = mapDataBean_ClassName.get(strClassName).newInstance();
				TableName tablename = dbDataTable.getTableName();

				List<FieldForeignKey> lsFieldForeignKeys = dbDataTable.getFieldForeignKeys();
				Set<FieldForeignKey> setFieldForeignKeys = new GSet<>();

				for(FieldForeignKey fieldforeignkey : lsFieldForeignKeys) {
					if(fieldforeignkey.getSizeReferentPrimaryKey() == 1) {
						setFieldForeignKeys.add(fieldforeignkey);
					}
				}

				StringBuilder builder = new StringBuilder();
				builder.append("DROP TABLE ").append(tablename.getTableName());

				GStatement stm = new GStatement(p_connection);
				stm.executeCreateTable(builder.toString());

				StringBuilder builderSQLCreateTable = mapSQLCreateTable_TableName.get(strClassName);
				builder = new StringBuilder(builderSQLCreateTable).append(",\n");

				for(FieldForeignKey fieldforeignkey : setFieldForeignKeys) {
					DataField datafield_ForeignKey = fieldforeignkey.getDataField_ForeignKey();
					FieldPrimaryKey fieldReferentPrimaryKey = fieldforeignkey.getFieldReferentPrimaryKey_First();
					String strFieldForeignKey_Name = fieldforeignkey.getFieldName_ForeignKey();

					builder.append("\t").append(getSQLString_DataField(datafield_ForeignKey)).append('\n');
					builder.append("CONSTRAINT ").append("FK_").append(datafield_ForeignKey.getTableName().getTableName()).append('_').append(strFieldForeignKey_Name).append('\n');
					builder.append("REFERENCES ").append(fieldReferentPrimaryKey.getTableName()).append('(').append(fieldReferentPrimaryKey.getPrimaryKeyName()).append(")\n");
					builder.append("ON UPDATE RESTRICT ON DELETE RESTRICT,\n");
				}

				builder = new StringBuilder(builder.substring(0, builder.length() - 2));

	//todo Create Unique
				Map<String, FieldUnique> mapFieldUnique_Name = dbDataTable.getMapFieldUnique_Name();

				if(mapFieldUnique_Name.size() > 0) {
					builder.append(",\n");
				}

				for(String strFieldUnique_Name : mapFieldUnique_Name.keySet()) {
					builder.append("\tCONSTRAINT ").append(strFieldUnique_Name).append('_').append(tablename.getTableName()).append('\n');
					builder.append("UNIQUE(");

					FieldUnique fieldunique = mapFieldUnique_Name.get(strFieldUnique_Name);
					List<? extends DataField> lsDataFields = fieldunique.getDataFields();

					for(DataField datafield : lsDataFields) {
						String strFieldName = datafield.getFieldName();
						builder.append(strFieldName).append(", ");
					}

					builder = new StringBuilder(builder.substring(0, builder.length() - 2));
					builder.append(")\n");
				}

				if(mapFieldUnique_Name.size() > 0) {
					builder = new StringBuilder(builder.substring(0, builder.length() - 1));
				}

				builder.append(')');

				GLog.info("Create ForeignKey & Unique " + tablename.getTableName() + " . . .");

				stm = new GStatement(p_connection);
				stm.executeCreateTable(builder.toString());
			}

	//todo Create Index
			for(String strClassName : mapDataBean_ClassName.keySet()) {
				DBDataTable dbDataTable = mapDataBean_ClassName.get(strClassName).newInstance();
				TableName tablename = dbDataTable.getTableName();

				List<DataField> fieldIndexs = dbDataTable.getFieldIndexs();
				List<String> lsSQLCreateIndexs = new GList<>();

				for(DataField datafield : fieldIndexs) {
					String strFieldName = datafield.getFieldName();

					StringBuilder builder = new StringBuilder();
					builder.append("CREATE INDEX ").append("IX_").append(tablename.getTableName()).append('_').append(strFieldName).append('\n');
					builder.append("ON ").append(tablename.getTableName()).append('(').append(strFieldName).append(')');

					lsSQLCreateIndexs.add(builder.toString());
				}

				if(lsSQLCreateIndexs.size() > 0) {
					GLog.info("Create Index " + tablename.getTableName() + " . . .");

					for(String strSQLCreateIndex : lsSQLCreateIndexs) {
						GStatement stm = new GStatement(p_connection);
						stm.executeCreateTable(strSQLCreateIndex);
					}
				}
			}
		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	private static String getSQLString_DataField(DataField p_datafield) throws GException {
		try {
			int intDataSize = p_datafield.getDataSize();
			boolean bolIsMandatory = p_datafield.isMandatory();

			StringBuilder builder = new StringBuilder();
			builder.append(p_datafield.getFieldName()).append(" ");

			Class clsData = p_datafield.getClassData();
			DBObject dboNewInstance = (DBObject)clsData.newInstance();

			if(dboNewInstance instanceof DBInteger) {
				builder.append("INTEGER ");

			} else if(dboNewInstance instanceof DBString) {
				if(intDataSize <= 0) {
					throw new GException("Invalid DataSize!");
				}

				builder.append("VARCHAR(").append(intDataSize).append(") ");

			} else if(dboNewInstance instanceof DBBoolean) {
				builder.append("BOOLEAN ");

			} else if(dboNewInstance instanceof DBDateTime) {
				builder.append("TIMESTAMP ");

			} else if(dboNewInstance instanceof DBDate) {
				builder.append("DATE ");

			} else if(dboNewInstance instanceof DBMoney) {
				builder.append("NUMERIC(28, 8) ");

			} else if(dboNewInstance instanceof DBQuantity) {
				builder.append("NUMERIC(18, 8) ");

			} else if(dboNewInstance instanceof DBDecimal) {
				builder.append("NUMERIC(32, 16) ");

			} else {
				throw new GException("Invalid DataField!");
			}

			if(bolIsMandatory) {
				builder.append("NOT NULL ");

			} else {
				builder.append("NULL ");
			}

			return builder.toString();

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}
}
