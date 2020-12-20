package com.laithailibrary.serverlibrary.server.search;

import java.util.*;
import com.laithailibrary.serverlibrary.server.dbcache.*;
import com.laithailibrary.sharelibrary.collection.*;
import com.laithailibrary.sharelibrary.datatableview.*;
import com.laithailibrary.sharelibrary.db.dbobject.*;
import com.laithailibrary.sharelibrary.field.*;
import com.laithailibrary.sharelibrary.sqlstatement.*;
import com.laithailibrary.sharelibrary.sqlstatement.support.*;
import exc.*;

public class DataVectorSearch {

	private DataVectorSearch() {}

	public static Set<DBString> search(DataVectorTable p_datatable, GCashDataIndex p_dataindex, SQLStatement p_sqlStatement) throws GException {
		try {
			Set<DBString> setPrimaryKeyValues;

			if(p_sqlStatement.isSet()) {
				SQLOperator sqloperator_First = p_sqlStatement.getSQLStatementString().getSQLOperator();
				SQLStatementString sqlStatementString = p_sqlStatement.getSQLStatementString();
				DataField datafield = sqlStatementString.getDataField();
				DBObject dbObjectValue = sqlStatementString.getDBObjectValue();
				Collection ObjectValues = sqlStatementString.getDBObjectValues();

				DataVectorTable dvtSearched_First;

				if(sqloperator_First == SQLOperator.In
					|| sqloperator_First == SQLOperator.InWithNull
					|| sqloperator_First == SQLOperator.NotIn
					|| sqloperator_First == SQLOperator.NotInWithNull) {

					dvtSearched_First = search(p_datatable, p_dataindex, datafield, new GList(ObjectValues), sqloperator_First);

				} else {
					dvtSearched_First = search(p_datatable, p_dataindex, datafield, dbObjectValue, sqloperator_First);
				}

				List<BEANSQLStatement> lsBEANSQLStatements = p_sqlStatement.getBEANSQLStatements();

				if(lsBEANSQLStatements.size() > 0) {
					DataVectorTable dvtSearched_Other = null;

					for(BEANSQLStatement beanSQLStatement_Other : lsBEANSQLStatements) {
						SQLOperator sqloperator_Other = beanSQLStatement_Other.getSQLOperator();
						SQLStatement sqlStatement_Other = beanSQLStatement_Other.getSQLStatement();

						if(sqloperator_Other == SQLOperator.AND) {
							Set<DBString> setPrimaryKeyValues_Searched;

							if(dvtSearched_Other == null) {
								setPrimaryKeyValues_Searched = search(dvtSearched_First, p_dataindex, sqlStatement_Other);
								dvtSearched_Other = getDTV(dvtSearched_First, setPrimaryKeyValues_Searched, false);

							} else {
								setPrimaryKeyValues_Searched = search(dvtSearched_Other, p_dataindex, sqlStatement_Other);
								dvtSearched_Other = getDTV(dvtSearched_Other, setPrimaryKeyValues_Searched, false);
							}
						} else if(sqloperator_Other == SQLOperator.OR) {
							Set<DBString> setPrimaryKeyValues_Searched = search(p_datatable, p_dataindex, sqlStatement_Other);
							dvtSearched_Other = dvtSearched_First.cloneDataVectorTable();

							for(DBString dbstrPrimaryKeyValue : setPrimaryKeyValues_Searched) {
								if(!dvtSearched_Other.hasRow(dbstrPrimaryKeyValue)) {
									DataVectorRow dvrRow = p_datatable.getDataRow(dbstrPrimaryKeyValue);

									dvtSearched_Other.addDataRow(dvrRow);
								}
							}
						} else {
							throw new GException("SQLOperator(" + sqloperator_Other + ") not implemented!\nStep: 1");
						}
					}

					setPrimaryKeyValues = dvtSearched_Other.getPrimaryKeyValues();

				} else {
					setPrimaryKeyValues = dvtSearched_First.getPrimaryKeyValues();
				}
			} else {
				setPrimaryKeyValues = p_datatable.getPrimaryKeyValues();
			}

			return setPrimaryKeyValues;

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	private static <DBO extends DBObject> DataVectorTable search(DataVectorTable p_datatable, GCashDataIndex p_dataindex, DataField<DBO> p_datafield,
		DBO p_dbobject, SQLOperator p_sqloperator) throws GException {

		try {
			DataVectorTable dvtReturn = new DataVectorTable(p_datatable.getDataTableColumnModel(), p_datatable.getColumnNamePrimaryKeys(),
				p_datatable.getColumnNameSortKeys());

			if(p_datatable.size() > 0) {
				p_datatable.beforeFirst();

				if(p_datafield.isIndex()) {
					GSet<DBString> setPrimaryValues = p_dataindex.getPrimaryValues(p_datafield.getFieldName(), p_dbobject, p_sqloperator);
					dvtReturn = getDTV(p_datatable, setPrimaryValues, false);

				} else {
					while(p_datatable.next()) {
						DataVectorRow dvrRow = p_datatable.getDataRow();
						DBObject dboValue = dvrRow.getDataAtColumnName(p_datafield);

						if(p_sqloperator == SQLOperator.Equal) {
							if(dboValue.compareTo(p_dbobject) == 0) {
								dvtReturn.addDataRow(dvrRow);
							}
						} else if(p_sqloperator == SQLOperator.NotEqual) {
							if(dboValue.compareTo(p_dbobject) != 0) {
								dvtReturn.addDataRow(dvrRow);
							}
						} else if(p_sqloperator == SQLOperator.GreaterThan) {
							if(dboValue.compareTo(p_dbobject) > 0) {
								dvtReturn.addDataRow(dvrRow);
							}
						} else if(p_sqloperator == SQLOperator.GreaterThanOrEqualTo) {
							if(dboValue.compareTo(p_dbobject) >= 0) {
								dvtReturn.addDataRow(dvrRow);
							}
						} else if(p_sqloperator == SQLOperator.LessThan) {
							if(dboValue.compareTo(p_dbobject) < 0) {
								dvtReturn.addDataRow(dvrRow);
							}
						} else if(p_sqloperator == SQLOperator.LessThanOrEqualTo) {
							if(dboValue.compareTo(p_dbobject) <= 0) {
								dvtReturn.addDataRow(dvrRow);
							}
						} else if(p_sqloperator == SQLOperator.EndWith) {
							if(dboValue.getString().endsWith(p_dbobject.getString())) {
								dvtReturn.addDataRow(dvrRow);
							}
						} else if(p_sqloperator == SQLOperator.StartWith) {
							if(dboValue.getString().startsWith(p_dbobject.getString())) {
								dvtReturn.addDataRow(dvrRow);
							}
						} else if(p_sqloperator == SQLOperator.Like2) {
							if(dboValue.getString().contains(p_dbobject.getString())) {
								dvtReturn.addDataRow(dvrRow);
							}
						} else if(p_sqloperator == SQLOperator.Like2_IgnoreCase) {
							if(dboValue.getString().toLowerCase().contains(p_dbobject.getString().toLowerCase())) {
								dvtReturn.addDataRow(dvrRow);
							}
						} else if(p_sqloperator == SQLOperator.Equal_IgnoreCase) {
							if(dboValue.getString().toLowerCase().compareTo(p_dbobject.getString().toLowerCase()) == 0) {
								dvtReturn.addDataRow(dvrRow);
							}
						} else if(p_sqloperator == SQLOperator.IsNull) {
							if(dboValue.isInvalid()) {
								dvtReturn.addDataRow(dvrRow);
							}
						} else if(p_sqloperator == SQLOperator.IsNotNull) {
							if(dboValue.isValid()) {
								dvtReturn.addDataRow(dvrRow);
							}
						} else {
							throw new GException("SQLOperator(" + p_sqloperator + ") not implemented!\nStep: 2");
						}
					}
				}

				dvtReturn.sortData();
			}

			return dvtReturn;

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	private static <DBO extends DBObject> DataVectorTable search(DataVectorTable p_datatable, GCashDataIndex p_dataindex, DataField<DBO> p_datafield,
		List<DBO> lsDBOs, SQLOperator p_sqloperator) throws GException {

		try {
			DataVectorTable dvtReturn = new DataVectorTable(p_datatable.getDataTableColumnModel(), p_datatable.getColumnNamePrimaryKeys(),
				p_datatable.getColumnNameSortKeys());

			if(p_datatable.size() > 0) {
				Set<? extends DBObject> setDBO = new GSet<>(lsDBOs);

				p_datatable.beforeFirst();

				if(p_datafield.isIndex()) {
					GSet<DBString>  setPrimaryValues = p_dataindex.getPrimaryValues(p_datafield.getFieldName(), setDBO, p_sqloperator);
					dvtReturn = getDTV(p_datatable, setPrimaryValues, false);

				} else {
					while(p_datatable.next()) {
						DataVectorRow dvrRow = p_datatable.getDataRow();
						DBObject dboValue = dvrRow.getDataAtColumnName(p_datafield);

						if(p_sqloperator == SQLOperator.In) {
							if(setDBO.contains(dboValue)) {
								dvtReturn.addDataRow(dvrRow);
							}
						} else if(p_sqloperator == SQLOperator.NotIn) {
							if(!setDBO.contains(dboValue)) {
								dvtReturn.addDataRow(dvrRow);
							}
						} else if(p_sqloperator == SQLOperator.InWithNull) {
							if(setDBO.contains(dboValue)
								|| dboValue.isInvalid()) {

								dvtReturn.addDataRow(dvrRow);
							}
						} else if(p_sqloperator == SQLOperator.NotInWithNull) {
							if(!setDBO.contains(dboValue)
								|| dboValue.isInvalid()) {

								dvtReturn.addDataRow(dvrRow);
							}
						} else {
							throw new GException("SQLOperator(" + p_sqloperator + ") not implemented!\nStep: 2");
						}
					}
				}

				dvtReturn.sortData();
			}

			return dvtReturn;

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	private static DataVectorTable getDTV(DataVectorTable p_dvtDVT_ToGet, Set<DBString> p_setPrimaryKeyValues, boolean p_bolDoSortData) throws GException {
		try {
			DataVectorTable dvtReturn = new DataVectorTable(p_dvtDVT_ToGet.getDataTableColumnModel(), p_dvtDVT_ToGet.getColumnNamePrimaryKeys(),
				p_dvtDVT_ToGet.getColumnNameSortKeys());

			p_dvtDVT_ToGet.beforeFirst();

			if(p_dvtDVT_ToGet.size() < p_setPrimaryKeyValues.size()) {
				while(p_dvtDVT_ToGet.next()) {
					PrimaryKeyValue primarykeyvalue = p_dvtDVT_ToGet.getCurrentPrimaryKeyValue();
					DBString dbstrStringValue = new DBString(primarykeyvalue.getStringValue());

					if(p_setPrimaryKeyValues.contains(dbstrStringValue)) {
						DataVectorRow dvrRow = p_dvtDVT_ToGet.getDataRow(primarykeyvalue);
						dvtReturn.addDataRow(dvrRow);
					}
				}
			} else {
				for(DBString dbstrPrimaryKeyValue : p_setPrimaryKeyValues) {
					if(p_dvtDVT_ToGet.hasRow(dbstrPrimaryKeyValue)) {
						DataVectorRow dvrRow = p_dvtDVT_ToGet.getDataRow(dbstrPrimaryKeyValue);
						dvtReturn.addDataRow(dvrRow);
					}
				}
			}

			if(p_bolDoSortData) {
				dvtReturn.sortData();
			}

			p_dvtDVT_ToGet.beforeFirst();

			return dvtReturn;

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}
}
