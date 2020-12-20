package com.laithailibrary.sharelibrary.util;

import com.laithailibrary.sharelibrary.field.DataField;
import com.laithailibrary.sharelibrary.field.FieldPrimaryKey;
import com.laithailibrary.sharelibrary.db.dbobject.DBObject;
import exc.GException;

/**
 * Created by IntelliJ IDEA.
 * User: dumkrerng
 * Date: 6/19/11
 * Time: 9:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class BeanErrorMessage {
	private BeanErrorMessage() {}

	public static void errorFieldNotInvalid(DataField p_datafield) throws GException {
		errorFieldNotInvalid(p_datafield.getFieldLabel());
	}

	public static void errorFieldNotInvalid(String p_strFieldName) throws GException {
		String strError = "กรุณากรอกข้อมูล ".concat(p_strFieldName);

		throw new GException(strError);
	}

	public static void errorFieldDuplicate(DataField p_datafield, DBObject p_dboData) throws GException {
		errorFieldDuplicate(p_datafield.getFieldName(), p_dboData);
	}

	public static void errorFieldDuplicate(String p_strFieldName, DBObject p_dboData) throws GException {
		String strError = "ข้อมูลของ ".concat(p_strFieldName).concat("(").concat(p_dboData.getString()).concat(")").concat(" ซ้ำ");
		throw new GException(strError);
	}

	public static void errorForeignKeyNonDataExisting(FieldPrimaryKey p_fieldprimarykey, DBObject p_dboData) throws GException {
		String strError = "ID(" + p_dboData.getString() + ") not existing in " + p_fieldprimarykey.toString();
		throw new GException(strError);
	}

	public static void errorMultipleForeignKeyNonDataExisting(DBObject p_dboData) throws GException {
		String strError = "MultipleForeignKeyID(" + p_dboData.getString() + ") not existing.";
		throw new GException(strError);
	}
}
