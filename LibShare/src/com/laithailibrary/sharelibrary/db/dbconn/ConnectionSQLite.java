package com.laithailibrary.sharelibrary.db.dbconn;

import java.sql.*;
import java.util.*;
import exc.*;

/**
 * Created with IntelliJ IDEA.
 * User: dumkrerng
 * Date: 9/11/12
 * Time: 11:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class ConnectionSQLite implements IConnectionSQL {

	public ConnectionSQLite() {}

	public Connection createConnection(String p_strDatabasePath) throws GException {
		try {
			Class.forName("org.sqlite.JDBC");
			Connection connection = DriverManager.getConnection("jdbc:sqlite:" + p_strDatabasePath, new Properties());

			return connection;

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}
}