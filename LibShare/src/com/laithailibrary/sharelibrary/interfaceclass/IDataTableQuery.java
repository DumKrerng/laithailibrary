package com.laithailibrary.sharelibrary.interfaceclass;

import java.util.*;
import com.laithailibrary.sharelibrary.bean.*;
import com.laithailibrary.sharelibrary.datatableview.*;
import com.laithailibrary.sharelibrary.db.dbobject.*;
import com.laithailibrary.sharelibrary.db.dbutilities.*;
import com.laithailibrary.sharelibrary.sqlstatement.*;
import com.laithailibrary.sharelibrary.sqlstatement.support.*;
import exc.*;
import org.json.*;

/**
 * Created with IntelliJ IDEA.
 * User: dumkrerng
 * Date: 9/26/12
 * Time: 10:50 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IDataTableQuery<T extends DBDataTable> extends InterfaceService {
	public void setDBDataTableClass(Class p_classDBDataTable) throws GException;
	public <DBO extends DBObject> T getDBDataTable(DBO p_PrimaryKeyID) throws GException;
	public List<T> getDBDataTables(SQLStatement p_statement) throws GException;
	public DataVectorTable getDataVectorTable(SQLStatement p_statement) throws GException;
	public DataVectorTable getDataVectorTable(SQLStatement p_statement, List<String> p_lsSortKeys) throws GException;
	public DataVectorTable getDataVectorTable(SQLStatement p_statement, List<String> p_lsSortKeys, Sorting p_sorting) throws GException;
	public DataVectorTable getDataVectorTable(SQLStatement p_statement, List<String> p_lsSortKeys, int p_intRowLimit, Sorting p_sorting) throws GException;
	public List<DBString> insertDatas(List<T> p_lsDataTables, TransactionBegin p_transactionBegin) throws GException;
	public List<DBString> insertDatas(List<T> p_lsDataTables, ExtraBean p_extrabean, TransactionBegin p_transactionBegin) throws GException;
	public DBString insertData(T p_dbDataTable, TransactionBegin p_transactionBegin) throws GException;
	public DBString insertData(T p_dbDataTable, ExtraBean p_extrabean, TransactionBegin p_transactionBegin) throws GException;
	public DBString insertData(T p_dbDataTable, TransactionBegin p_transactionBegin, DoVerifyData p_doverifydata, DoRunBefore p_dorunbefore,
		DoRunAfter p_dorunafter) throws GException;
	public DBString insertData(T p_dbDataTable, ExtraBean p_extrabean, TransactionBegin p_transactionBegin, DoVerifyData p_doverifydata,
		DoRunBefore p_dorunbefore, DoRunAfter p_dorunafter) throws GException;
	public DBString updateData(T p_dbDataTable_Edited, TransactionBegin p_transactionBegin) throws GException;
	public DBString updateData(T p_dbDataTable_Edited, ExtraBean p_extrabean, TransactionBegin p_transactionBegin) throws GException;
	public DBString updateData(T p_dbDataTable_Edited, TransactionBegin p_transactionBegin, DoVerifyData p_doverifydata, DoRunBefore p_dorunbefore,
		DoRunAfter p_dorunafter) throws GException;
	public DBString updateData(T p_dbDataTable_Edited, ExtraBean p_extrabean, TransactionBegin p_transactionBegin, DoVerifyData p_doverifydata,
		DoRunBefore p_dorunbefore, DoRunAfter p_dorunafter) throws GException;
	public void deleteAllData(SQLStatement p_statement, TransactionBegin p_transactionBegin) throws GException;
	public void deleteDatas(List<T> p_lsDataTables, TransactionBegin p_transactionBegin) throws GException;
	public void deleteDatas(List<T> p_lsDataTables, ExtraBean p_extrabean, TransactionBegin p_transactionBegin) throws GException;
	public void deleteData(T p_dbDataTable, TransactionBegin p_transactionBegin) throws GException;
	public void deleteData(T p_dbDataTable, ExtraBean p_extrabean, TransactionBegin p_transactionBegin) throws GException;
	public void deleteData(T p_dbDataTable, TransactionBegin p_transactionBegin, DoVerifyData p_doverifydata, DoRunBefore p_dorunbefore,
		DoRunAfter p_dorunafter) throws GException;
	public void deleteData(T p_dbDataTable, ExtraBean p_extrabean, TransactionBegin p_transactionBegin, DoVerifyData p_doverifydata,
		DoRunBefore p_dorunbefore, DoRunAfter p_dorunafter) throws GException;

	public void useMockData(String p_strJSONArray) throws GException;
	public void useMockData(JSONArray p_jsonarray) throws GException;
}
