package com.laithailibrary.sharelibrary.sqlstatement;

import com.laithailibrary.sharelibrary.db.dbutilities.IConnection;
import exc.GException;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dumkrerng
 * Date: 6/19/12
 * Time: 12:10 AM
 * To change this template use File | Settings | File Templates.
 */
public interface SQLStatement extends Comparable<SQLStatement>  {

	String SQLTempWhere = "1 = 1";
	SQLStatement EmptyStatement = SQLStatementData.NewStatementEmpty();

	public abstract SQLStatement and(SQLStatement p_sqlStatement) throws GException;
	public abstract SQLStatement or(SQLStatement p_sqlStatement) throws GException;

	public abstract void setSQLStatementString(SQLStatementString p_sqlStatementString);
	public abstract SQLStatementString getSQLStatementString();
	public List<BEANSQLStatement> getBEANSQLStatements();
	public abstract String getSQLString(IConnection p_iConnection) throws GException;
	public abstract String getSQLString(IConnection p_iConnection, String p_strTag) throws GException;
	public abstract boolean isSet() throws GException;
	public abstract boolean isNotSet() throws GException;
	public abstract String toString();
}
