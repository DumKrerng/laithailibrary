package com.laithailibrary.sharelibrary.db.dbobject;

import com.laithailibrary.sharelibrary.locale.*;
import exc.*;

/**
 * Created with IntelliJ IDEA.
 * User: dumkrerng
 * Date: 5/13/12
 * Time: 1:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class DBYear extends DBString {

	private static final long serialVersionUID = 3;

	public DBYear(String p_strYear) {
		super(p_strYear);
	}

	protected String getInvalidDataString() {
		return "1900";
	}

	public String getString() {
		return m_strData;
	}

	public int getYear() {
		return Integer.valueOf(m_strData);
	}

	public String getString_TwoDigit() {
		return getString_TwoDigit(GLocale.English);
	}

	public String getString_TwoDigit(GLocale p_locale) {
		String strYear;

		if(p_locale == GLocale.Thai) {
			int intYear_ENG = Integer.parseInt(m_strData);
			int intYear_THA = intYear_ENG + 543;

			strYear = String.valueOf(intYear_THA);

		} else {
			strYear = m_strData;
		}

		return strYear.substring(strYear.length() - 2);
	}

	public String toString() {
		return m_strData;
	}

	public DBYear clone() {
		try {
			return new DBYear(getString());

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
		}

		return new DBYear(null);
	}
}
