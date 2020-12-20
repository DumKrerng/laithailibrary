package com.laithailibrary.sharelibrary.db.dbconn;

import exc.*;
import java.sql.*;

public class ConnectionPostgresql implements IConnectionSQL {

	public static String USERNAME = "";
	public static String PASSWORD = "";

	public ConnectionPostgresql() {}

	public Connection createConnection(String p_strDatabasePath) throws GException {
		try {
			Class.forName("org.postgresql.Driver");
			Connection connection = DriverManager.getConnection("jdbc:postgresql://" + p_strDatabasePath, USERNAME, PASSWORD);

			return connection;

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}
}
