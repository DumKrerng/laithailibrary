package com.laithailibrary.sharelibrary.sqlstatement;

import java.io.*;
import java.util.*;
import com.laithailibrary.sharelibrary.collection.*;
import com.laithailibrary.sharelibrary.db.dbutilities.*;
import com.laithailibrary.sharelibrary.sqlstatement.support.*;
import exc.*;

/**
 * Created with IntelliJ IDEA.
 * User: dumkrerng
 * Date: 6/18/12
 * Time: 10:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class SQLStatementData implements SQLStatement, Externalizable {

	private SQLStatementString m_sqlStatementString = null;

	private List<BEANSQLStatement> m_lsBEANSQLStatements = new GList<>();

	private static final long serialVersionUID = 11;

	public SQLStatementData() {}

	public void writeExternal(ObjectOutput out) throws IOException {
		try {
			out.writeObject(m_sqlStatementString);
			out.writeObject(m_lsBEANSQLStatements);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			out.close();

			throw new IOException(exception);
		}
	}

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		try {
			m_sqlStatementString = (SQLStatementString)in.readObject();
			m_lsBEANSQLStatements = (List<BEANSQLStatement>)in.readObject();

		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			in.close();

			throw new IOException(exception);
		}
	}

	public SQLStatementData(SQLStatementString p_sqlStatementString) {
		m_sqlStatementString = p_sqlStatementString;
	}

	public static SQLStatement NewStatementEmpty() {
		return new SQLStatementData();
	}

	public SQLStatement and(SQLStatement p_sqlStatement) throws GException {
		return createSQLStatement(SQLOperator.AND, p_sqlStatement);
	}

	public SQLStatement or(SQLStatement p_sqlStatement) throws GException {
		return createSQLStatement(SQLOperator.OR, p_sqlStatement);
	}

	private SQLStatement createSQLStatement(SQLOperator p_sqlOperator, SQLStatement p_sqlStatement) throws GException {
		try {
			if(isNotSet()) {
				return new SQLStatementData(p_sqlStatement.getSQLStatementString());

			} else {
				BEANSQLStatement beanSQLStatement = new BEANSQLStatement(p_sqlOperator, p_sqlStatement);
				m_lsBEANSQLStatements.add(beanSQLStatement);
			}

			return this;

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public void setSQLStatementString(SQLStatementString p_sqlStatementString) {
		m_sqlStatementString = p_sqlStatementString;
	}

	public SQLStatementString getSQLStatementString() {
		return m_sqlStatementString;
	}

	public boolean isSet() throws GException {
		try {
			if(m_sqlStatementString == null) {
				return false;
			}

			return true;

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public boolean isNotSet() throws GException {
		try {
			if(isSet()) {
				return false;
			}

			return true;

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public String getSQLString(IConnection p_iConnection) throws GException {
		return getSQLString(p_iConnection, "");
	}

	public String getSQLString(IConnection p_iConnection, String p_strTag) throws GException {
		try {
			StringBuilder builder = new StringBuilder();

			if(isNotSet()) {
				return builder.toString();
			}

			if(m_lsBEANSQLStatements.size() > 0) {
				builder.append("(");
			}

			builder.append(m_sqlStatementString.getSQLString(p_iConnection, p_strTag));

			if(m_lsBEANSQLStatements.size() > 0) {
				for(BEANSQLStatement beanSQLStatement : m_lsBEANSQLStatements) {
					SQLOperator sqlOperator = beanSQLStatement.getSQLOperator();
					SQLStatement sqlStatement = beanSQLStatement.getSQLStatement();

					builder.append("\n").append(sqlOperator.toString()).append(" ");
					builder.append(sqlStatement.getSQLString(p_iConnection, p_strTag));
				}
			}

			if(m_lsBEANSQLStatements.size() > 0) {
				builder.append(")");
			}

			return builder.toString();

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public List<BEANSQLStatement> getBEANSQLStatements() {
		return m_lsBEANSQLStatements;
	}

	public String toString() {
		String strReturn = "";

		try {
			String strSQLStatementString = "";

			if(m_sqlStatementString != null) {
				strSQLStatementString = m_sqlStatementString.toString();
			}

			String strStatement2 = "";

			if(m_lsBEANSQLStatements.size() > 0) {
				StringBuilder builder = new StringBuilder();

				for(BEANSQLStatement beanSQLStatement : m_lsBEANSQLStatements) {
					SQLOperator sqlOperator = beanSQLStatement.getSQLOperator();
					SQLStatement sqlStatement = beanSQLStatement.getSQLStatement();

					if(sqlOperator != null) {
						builder.append(" ").append(sqlOperator.toString()).append(" ");
						builder.append(sqlStatement.toString());
					}
				}

				strStatement2 = builder.toString();
			}

			strReturn = strSQLStatementString + strStatement2;

			return strReturn;

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
		}

		return strReturn;
	}

	public int compareTo(SQLStatement p_sqlStatement) {
		return this.toString().compareTo(p_sqlStatement.toString());
	}
}
