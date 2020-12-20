package com.laithailibrary.sharelibrary.db.dbobject.support;

import exc.*;

public enum GNumber {
	_1("1", "๑"),
	_2("2", "๒"),
	_3("3", "๓"),
	_4("4", "๔"),
	_5("5", "๕"),
	_6("6", "๖"),
	_7("7", "๗"),
	_8("8", "๘"),
	_9("9", "๙"),
	_0("0", "๐");

	private String m_strArabic = "";
	private String m_strThai = "";

	private GNumber(String p_strArabic, String p_strThai) {
		m_strArabic = p_strArabic;
		m_strThai = p_strThai;
	}

	public String replaceArabicToThai(String p_strString) throws GException {
		try {
			String strReplaced = p_strString;
			strReplaced = strReplaced.replace(m_strArabic, m_strThai);

			return strReplaced;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}
}
