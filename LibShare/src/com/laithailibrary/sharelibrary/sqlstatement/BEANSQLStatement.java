package com.laithailibrary.sharelibrary.sqlstatement;


import java.io.*;
import com.laithailibrary.sharelibrary.sqlstatement.support.*;
import exc.*;

/**
 * Created with IntelliJ IDEA.
 * User: dumkrerng
 * Date: 9/29/12
 * Time: 12:36 AM
 * To change this template use File | Settings | File Templates.
 */
public class BEANSQLStatement implements Externalizable {

	private SQLOperator m_sqlOperator;
	private SQLStatement m_sqlStatement;

	private static final long serialVersionUID = 10;

	public BEANSQLStatement() {}

	public void writeExternal(ObjectOutput out) throws IOException {
		try {
			out.writeObject(m_sqlOperator);
			out.writeObject(m_sqlStatement);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			out.close();

			throw new IOException(exception);
		}
	}

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		try {
			m_sqlOperator = (SQLOperator)in.readObject();
			m_sqlStatement = (SQLStatement)in.readObject();

		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			in.close();

			throw new IOException(exception);
		}
	}

	public BEANSQLStatement(SQLOperator p_sqlOperator, SQLStatement p_sqlStatement) {
		m_sqlOperator = p_sqlOperator;
		m_sqlStatement = p_sqlStatement;
	}

	public SQLOperator getSQLOperator() {
		return m_sqlOperator;
	}

	public SQLStatement getSQLStatement() {
		return m_sqlStatement;
	}
}
