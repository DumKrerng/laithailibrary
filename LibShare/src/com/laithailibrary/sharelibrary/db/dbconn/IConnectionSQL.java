package com.laithailibrary.sharelibrary.db.dbconn;

import exc.GException;

import java.sql.Connection;

/**
 * Created with IntelliJ IDEA.
 * User: dumkrerng
 * Date: 9/2/12
 * Time: 11:50 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IConnectionSQL {
	public Connection createConnection(String p_strDatabasePath) throws GException;
}
