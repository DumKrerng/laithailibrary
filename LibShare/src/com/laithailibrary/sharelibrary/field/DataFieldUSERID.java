package com.laithailibrary.sharelibrary.field;

import com.laithailibrary.sharelibrary.db.dbobject.DBString;

/**
 * Created by IntelliJ IDEA.
 * User: dumkrerng
 * Date: 2/24/12
 * Time: 1:21 AM
 * To change this template use File | Settings | File Templates.
 */
public class DataFieldUSERID extends DataField<DBString> {

	private static final long serialVersionUID = 6541383258745821453L;

	public DataFieldUSERID() {}

	public DataFieldUSERID(TableName p_tablename, String p_strFieldName) {
		super(p_tablename, p_strFieldName, DBString.class);
	}
}
