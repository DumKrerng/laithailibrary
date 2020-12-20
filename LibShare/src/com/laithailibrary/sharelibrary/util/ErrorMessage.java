package com.laithailibrary.sharelibrary.util;

import com.laithailibrary.sharelibrary.db.dbobject.DBObject;
import exc.GException;

/**
 * Created by IntelliJ IDEA.
 * User: dumkrerng
 * Date: 6/19/11
 * Time: 9:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class ErrorMessage {
	public static void errorFieldNotInvalid(String p_strFieldName) throws GException {
		String strError = "กรุณากรอกข้อมูล ".concat(p_strFieldName);

		throw new GException(strError);
	}
	public static void errorFieldDuplicate(String p_strFieldName, DBObject p_dboData) throws GException {
		String strError = "ข้อมูลของ ".concat(p_strFieldName).concat("(").concat(p_dboData.getString()).concat(")").concat(" ซ้ำ");
		throw new GException(strError);
	}
}
