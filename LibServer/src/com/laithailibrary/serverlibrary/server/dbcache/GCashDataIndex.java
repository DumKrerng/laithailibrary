package com.laithailibrary.serverlibrary.server.dbcache;

import java.util.*;
import com.laithailibrary.sharelibrary.collection.*;
import com.laithailibrary.sharelibrary.datatableview.*;
import com.laithailibrary.sharelibrary.db.dbobject.*;
import com.laithailibrary.sharelibrary.sqlstatement.support.*;
import exc.*;

public class GCashDataIndex extends GMap<String, GMap<DBObject, GSet<PrimaryKeyValue>>> {

	private GCashDataIndex me;

	private static final long serialVersionUID = 2;

	public GCashDataIndex() {
		me = this;
	}

	public void add(String p_strFieldName, DBObject p_dboValue, PrimaryKeyValue p_primarykeyvalue) throws GException {
		try {
			GMap<DBObject, GSet<PrimaryKeyValue>> mapPrimaryKeyValues_DBObject;

			if(me.containsKey(p_strFieldName)) {
				mapPrimaryKeyValues_DBObject = me.get(p_strFieldName);

			} else {
				mapPrimaryKeyValues_DBObject = new GMap<>();
			}

			GSet<PrimaryKeyValue> setPrimaryKeyValues;

			if(mapPrimaryKeyValues_DBObject.containsKey(p_dboValue)) {
				setPrimaryKeyValues = mapPrimaryKeyValues_DBObject.get(p_dboValue);

			} else {
				setPrimaryKeyValues = new GSet<>();
			}

			setPrimaryKeyValues.add(p_primarykeyvalue);
			mapPrimaryKeyValues_DBObject.put(p_dboValue, setPrimaryKeyValues);
			me.put(p_strFieldName, mapPrimaryKeyValues_DBObject);

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public GSet<PrimaryKeyValue> getPrimaryKeyValues(String p_strFieldName, DBObject p_dboValue, SQLOperator p_sqloperator) throws GException {
		try {
			GSet<PrimaryKeyValue> setPrimaryKeyValues = new GSet<>();

			if(me.containsKey(p_strFieldName)) {
				GMap<DBObject, GSet<PrimaryKeyValue>> mapPrimaryKeyValues_DBObject = me.get(p_strFieldName);

				if(p_sqloperator == SQLOperator.Equal) {
					if(mapPrimaryKeyValues_DBObject.containsKey(p_dboValue)) {
						setPrimaryKeyValues = mapPrimaryKeyValues_DBObject.get(p_dboValue);
					}
				} else if(p_sqloperator == SQLOperator.NotEqual) {
					if(!mapPrimaryKeyValues_DBObject.containsKey(p_dboValue)) {
						GSet<DBObject> setValues_NotEqual = new GSet<>(mapPrimaryKeyValues_DBObject.keySet());
						setValues_NotEqual.remove(p_dboValue);

						setPrimaryKeyValues = new GSet<>();

						for(DBObject dboValue : setValues_NotEqual) {
							GSet<PrimaryKeyValue> temp = mapPrimaryKeyValues_DBObject.get(dboValue);
							setPrimaryKeyValues.addAll(temp);
						}
					}
				} else if(p_sqloperator == SQLOperator.GreaterThan) {
					Set<DBObject> setValues = mapPrimaryKeyValues_DBObject.keySet();

					for(DBObject dboValue : setValues) {
						if(dboValue.compareTo(p_dboValue) > 0) {
							GSet<PrimaryKeyValue> temp = mapPrimaryKeyValues_DBObject.get(dboValue);
							setPrimaryKeyValues.addAll(temp);
						}
					}
				} else if(p_sqloperator == SQLOperator.GreaterThanOrEqualTo) {
					Set<DBObject> setValues = mapPrimaryKeyValues_DBObject.keySet();

					for(DBObject dboValue : setValues) {
						if(dboValue.compareTo(p_dboValue) >= 0) {
							GSet<PrimaryKeyValue> temp = mapPrimaryKeyValues_DBObject.get(dboValue);
							setPrimaryKeyValues.addAll(temp);
						}
					}
				} else if(p_sqloperator == SQLOperator.LessThan) {
					Set<DBObject> setValues = mapPrimaryKeyValues_DBObject.keySet();

					for(DBObject dboValue : setValues) {
						if(dboValue.compareTo(p_dboValue) < 0) {
							GSet<PrimaryKeyValue> temp = mapPrimaryKeyValues_DBObject.get(dboValue);
							setPrimaryKeyValues.addAll(temp);
						}
					}
				} else if(p_sqloperator == SQLOperator.LessThanOrEqualTo) {
					Set<DBObject> setValues = mapPrimaryKeyValues_DBObject.keySet();

					for(DBObject dboValue : setValues) {
						if(dboValue.compareTo(p_dboValue) <= 0) {
							GSet<PrimaryKeyValue> temp = mapPrimaryKeyValues_DBObject.get(dboValue);
							setPrimaryKeyValues.addAll(temp);
						}
					}
				} else if(p_sqloperator == SQLOperator.EndWith) {
					Set<DBObject> setValues = mapPrimaryKeyValues_DBObject.keySet();

					for(DBObject dboValue : setValues) {
						if(dboValue.getString().endsWith(p_dboValue.getString())) {
							GSet<PrimaryKeyValue> temp = mapPrimaryKeyValues_DBObject.get(dboValue);
							setPrimaryKeyValues.addAll(temp);
						}
					}
				} else if(p_sqloperator == SQLOperator.StartWith) {
					Set<DBObject> setValues = mapPrimaryKeyValues_DBObject.keySet();

					for(DBObject dboValue : setValues) {
						if(dboValue.getString().startsWith(p_dboValue.getString())) {
							GSet<PrimaryKeyValue> temp = mapPrimaryKeyValues_DBObject.get(dboValue);
							setPrimaryKeyValues.addAll(temp);
						}
					}
				} else if(p_sqloperator == SQLOperator.Equal_IgnoreCase) {
					Set<DBObject> setValues = mapPrimaryKeyValues_DBObject.keySet();

					for(DBObject dboValue : setValues) {
						if(dboValue.getString().toLowerCase().compareTo(p_dboValue.getString().toLowerCase()) == 0) {
							GSet<PrimaryKeyValue> temp = mapPrimaryKeyValues_DBObject.get(dboValue);
							setPrimaryKeyValues.addAll(temp);
						}
					}
				} else if(p_sqloperator == SQLOperator.Like2) {
					Set<DBObject> setValues = mapPrimaryKeyValues_DBObject.keySet();

					for(DBObject dboValue : setValues) {
						if(dboValue.getString().contains(p_dboValue.getString())) {
							GSet<PrimaryKeyValue> temp = mapPrimaryKeyValues_DBObject.get(dboValue);
							setPrimaryKeyValues.addAll(temp);
						}
					}
				} else if(p_sqloperator == SQLOperator.Like2_IgnoreCase) {
					Set<DBObject> setValues = mapPrimaryKeyValues_DBObject.keySet();

					for(DBObject dboValue : setValues) {
						if(dboValue.getString().toLowerCase().contains(p_dboValue.getString().toLowerCase())) {
							GSet<PrimaryKeyValue> temp = mapPrimaryKeyValues_DBObject.get(dboValue);
							setPrimaryKeyValues.addAll(temp);
						}
					}
				} else if(p_sqloperator == SQLOperator.IsNull) {
					Set<DBObject> setValues = mapPrimaryKeyValues_DBObject.keySet();

					for(DBObject dboValue : setValues) {
						if(dboValue.isInvalid()) {
							GSet<PrimaryKeyValue> temp = mapPrimaryKeyValues_DBObject.get(dboValue);
							setPrimaryKeyValues.addAll(temp);
						}
					}
				} else if(p_sqloperator == SQLOperator.IsNotNull) {
					Set<DBObject> setValues = mapPrimaryKeyValues_DBObject.keySet();

					for(DBObject dboValue : setValues) {
						if(dboValue.isValid()) {
							GSet<PrimaryKeyValue> temp = mapPrimaryKeyValues_DBObject.get(dboValue);
							setPrimaryKeyValues.addAll(temp);
						}
					}
				} else {
					throw new GException("SQLOperator(" + p_sqloperator + ") not implemented!\nStep: 2");
				}
			}

			return setPrimaryKeyValues;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public GSet<PrimaryKeyValue> getPrimaryKeyValues(String p_strFieldName, Set<? extends DBObject> p_setDBObjects, SQLOperator p_sqloperator) throws GException {
		try {
			GSet<PrimaryKeyValue> setPrimaryKeyValues = new GSet<>();

			if(me.containsKey(p_strFieldName)) {
				Map<DBObject, GSet<PrimaryKeyValue>> mapPrimaryKeyValues_DBObject = me.get(p_strFieldName);

				for(Map.Entry<DBObject, GSet<PrimaryKeyValue>> entPrimaryKeyValues : mapPrimaryKeyValues_DBObject.entrySet()) {
					DBObject dboValue = entPrimaryKeyValues.getKey();

					if(p_sqloperator == SQLOperator.In) {
						if(p_setDBObjects.contains(dboValue)) {
							GSet<PrimaryKeyValue> setTemp = entPrimaryKeyValues.getValue();
							setPrimaryKeyValues.addAll(setTemp);
						}
					} else if(p_sqloperator == SQLOperator.NotIn) {
						if(!p_setDBObjects.contains(dboValue)) {
							GSet<PrimaryKeyValue> setTemp = entPrimaryKeyValues.getValue();
							setPrimaryKeyValues.addAll(setTemp);
						}

						/*
					} else if(p_sqloperator == SQLOperator.InWithNull) {
						if(setDBO.contains(dboValue)
							|| dboValue.isInvalid()) {

							dtvReturn.addDataRow_WithoutRunSortData(dvrRow);
						}
					} else if(p_sqloperator == SQLOperator.NotInWithNull) {
						if(!setDBO.contains(dboValue)
							|| dboValue.isInvalid()) {

							dtvReturn.addDataRow_WithoutRunSortData(dvrRow);
						}
						*/
					} else {
						throw new GException("SQLOperator(" + p_sqloperator + ") not implemented!\nStep: 2");
					}
				}
			}

			return setPrimaryKeyValues;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public GSet<DBString> getPrimaryValues(String p_strFieldName, DBObject p_dboValue, SQLOperator p_sqloperator) throws GException {
		try {
			GSet<DBString> setPrimaryValues = new GSet<>();
			GSet<PrimaryKeyValue> setPrimaryKeyValues = getPrimaryKeyValues(p_strFieldName, p_dboValue, p_sqloperator);

			for(PrimaryKeyValue primarykeyvalue : setPrimaryKeyValues) {
				DBString dbstrValue = new DBString(primarykeyvalue.getStringValue());
				setPrimaryValues.add(dbstrValue);
			}

			return setPrimaryValues;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public GSet<DBString> getPrimaryValues(String p_strFieldName, Set<? extends DBObject> p_setDBObjects, SQLOperator p_sqloperator) throws GException {
		try {
			GSet<DBString> setPrimaryValues = new GSet<>();
			GSet<PrimaryKeyValue> setPrimaryKeyValues = getPrimaryKeyValues(p_strFieldName, p_setDBObjects, p_sqloperator);

			for(PrimaryKeyValue primarykeyvalue : setPrimaryKeyValues) {
				DBString dbstrValue = new DBString(primarykeyvalue.getStringValue());
				setPrimaryValues.add(dbstrValue);
			}

			return setPrimaryValues;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}
}
