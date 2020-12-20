package com.laithailibrary.sharelibrary.db.dbutilities;

import exc.GException;

/**
 * Created with IntelliJ IDEA.
 * User: dumkrerng
 * Date: 6/16/12
 * Time: 9:45 PM
 * To change this template use File | Settings | File Templates.
 */
public enum DBType {
	SQLite("SQLite"),
	Postgresql("Postgresql"),
	MySQL("MySQL");

	private String m_strLabel = "";

	private DBType(String p_strLabel) {
		m_strLabel = p_strLabel;
	}

	public String getLabel() {
		return m_strLabel;
	}

	public static DBType getDBType(String p_strLabel) throws GException {
		if(SQLite.getLabel().contains(p_strLabel)) {
			return SQLite;
		}

		if(Postgresql.getLabel().contains(p_strLabel)) {
			return Postgresql;
		}

		if(MySQL.getLabel().contains(p_strLabel)) {
			return MySQL;
		}

		throw new GException("Invalid DBType(" + p_strLabel + ")!");
	}
}
