package com.laithailibrary.sharelibrary.support;

import com.laithailibrary.sharelibrary.db.dbobject.DBString;
import exc.ExceptionHandler;
import exc.GException;

/**
 * Created with IntelliJ IDEA.
 * User: dumkrerng
 * Date: 9/27/12
 * Time: 12:35 AM
 * To change this template use File | Settings | File Templates.
 */
public class LabelGenerator {

	private LabelGenerator() {}

	public static DBString generate(DBString p_dbstrUSERID, DBString p_dbstrName) throws GException {
		try {
		  DBString dbstrReturn = new DBString();

			if(p_dbstrUSERID.isValid()) {
				dbstrReturn = p_dbstrUSERID;

				if(p_dbstrName.isValid()) {
					dbstrReturn = dbstrReturn.concat(": ").concat(p_dbstrName);
				}
			}

			return dbstrReturn;

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}
}
