package com.laithailibrary.sharelibrary.db.dbconn;

import exc.*;
import java.sql.*;

public class ConnectionMySQL implements IConnectionSQL {

	public static String USERNAME = "";
	public static String PASSWORD = "";

	public ConnectionMySQL() {}

	public Connection createConnection(String p_strDatabasePath) throws GException {
		try {
//			Class.forName("com.mysql.jdbc.Driver");
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection connection = DriverManager.getConnection("jdbc:mysql://" + p_strDatabasePath + "?characterEncoding=UTF-8", USERNAME, PASSWORD);

			return connection;

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}
}
