package com.laithailibrary.sharelibrary.db.dbobject;

import java.io.*;
import com.laithailibrary.sharelibrary.db.dbobject.support.*;
import exc.*;

/**
 * Created by IntelliJ IDEA.
 * User: dumkrerng
 * Date: 6/4/11
 * Time: 12:31 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class DBObject implements I_DBObject, Comparable<I_DBObject>, Serializable, Cloneable {

	public static final String STRING_TRUE = "TRUE";
	public static final String STRING_FALSE = "FALSE";
	public static final String STRING_INVALID = "INVALID";
	public static final String STRING_EMPTY = "";

	protected String m_strData = "";

	protected static int SizeAfterDecimal = 34;

//	public static final String[] STRING_NOT_ALLOWED = {"'", "~", "!", "^", "\\", ";"}; serialVersionUID = 10;
	public static final String[] STRING_NOT_ALLOWED = {"'", "~",  "\\", ";"};
	public static final GNumber[] GNUMBERS = {GNumber._1, GNumber._2, GNumber._3, GNumber._4, GNumber._5, GNumber._6, GNumber._7, GNumber._8, GNumber._9, GNumber._0};

	private static final long serialVersionUID = 11;

	public DBObject() {}

	protected DBObject(String p_strString) {
		setInvalidData();

		if(p_strString != null) {
			if(p_strString.length() > 0) {
				m_strData = p_strString;
			}
		} else {
			resetData();
		}
	}

	protected DBObject(int p_int) {
		setInvalidData();

		m_strData = Integer.toString(p_int);
	}

	protected DBObject(Integer p_integer) {
		setInvalidData();

		m_strData = Integer.toString(p_integer);
	}

	protected DBObject(boolean p_boolean) {
		setInvalidData();

		if(p_boolean) {
			m_strData = STRING_TRUE;

		} else {
			m_strData = STRING_FALSE;
		}
	}

	public void writeExternal(ObjectOutput p_out) throws IOException {
		try {
			p_out.writeUTF(m_strData);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			p_out.close();

			throw new IOException(exception);
		}
	}

	public void readExternal(ObjectInput p_in) throws IOException {
		try {
			m_strData = p_in.readUTF();

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);

			p_in.close();

			throw new IOException(exception);
		}
	}

	protected void setInvalidData() {}

	public static void setDecimalSize(int p_intDecimalSize) {
		SizeAfterDecimal = p_intDecimalSize;
	}

	public void setStringValue(String p_strString) {
		setInvalidData();

		if(p_strString != null) {
			if(p_strString.length() > 0) {
				m_strData = p_strString;
			}
		} else {
			resetData();
		}
	}

	protected void setStrData(String p_strData) {
		if(p_strData != null) {
			m_strData = p_strData;

		} else {
			resetData();
		}
	}

	protected void setIntData(int p_int) {
		m_strData = Integer.toString(p_int);
	}

	protected void setBolData(boolean p_boolean) {
		if(p_boolean) {
			m_strData = STRING_TRUE;

		} else {
			m_strData = STRING_FALSE;
		}
	}

	protected void resetData() {
		m_strData = getInvalidDataString();
	}

	protected String getDataString() {
		return m_strData;
	}

	protected abstract String getInvalidDataString();

	public boolean isInvalid() {
		if(m_strData.length() <= 0) {
			return true;
		}

		if(m_strData.equalsIgnoreCase(getInvalidDataString())) {
			return true;
		}

		return false;
	}

	public boolean isValid() {
		if(m_strData.length() <= 0) {
			return false;
		}

		if(!m_strData.equalsIgnoreCase(getInvalidDataString())) {
			return true;
		}

		return false;
	}

	public String getString() {
		return m_strData;
	}

	public String getStringValue() {
		if(m_strData.length() <= 0) {
			return STRING_INVALID;
		}

		return m_strData;
	}

	public String getString_ThaiNumber() throws GException {
		try {
			if(isInvalid()) {
				return "";
			}

			return getString_ThaiNumber(getString());

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public static String getString_ThaiNumber(String p_strString) throws GException {
		try {
			if(p_strString.length() <= 0) {
				return "";
			}

			String strValue = p_strString;

			for(GNumber number : GNUMBERS) {
				strValue = number.replaceArabicToThai(strValue);
			}

			return strValue;

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public String getStringSQL() throws GException {
		return "NULL";
	}

	public String getStringSQL_StartWith() throws GException {
		verifySQLInjection();

		String strReturn = "'" + getString() + "%'";

		return strReturn;
	}

	public String getStringSQL_EndWith() throws GException {
		verifySQLInjection();

		String strReturn = "'%" + getString() + "'";

		return strReturn;
	}

	public String getStringSQL_LIKE2() throws GException {
		verifySQLInjection();

		String strReturn = "'%" + getString() + "%'";

		return strReturn;
	}

	public String getStringSQL_LowerCase() throws GException {
		verifySQLInjection();

		String strReturn = "'" + getString().toLowerCase() + "'";

		return strReturn;
	}

	public String getStringSQL_LIKE2_LowerCase() throws GException {
		verifySQLInjection();

		String strReturn = "'%" + getString().toLowerCase() + "%'";

		return strReturn;
	}

	public String getStringSQL_StartWith_IgnoreSQLInjection() {
		String strReturn = "'" + getString() + "%'";

		return strReturn;
	}

	public String getStringSQL_EndWith_IgnoreSQLInjection() {
		String strReturn = "'%" + getString() + "'";

		return strReturn;
	}

	public String getStringSQL_LIKE2_IgnoreSQLInjection() {
		String strReturn = "'%" + getString() + "%'";

		return strReturn;
	}

	public void verifySQLInjection() throws GException {
		try {
			if(isSQLInjection()) {
				throw new GException("String Error!");
			}
		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public boolean isSQLInjection() {
		try {
			String strString = getString();

			for(String strTemp : STRING_NOT_ALLOWED) {
				if(strString.contains(strTemp)) {
					return true;
				}
			}
		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		}

		return false;
	}

	public String getHTMLString() {
		return toHTMLString(m_strData);
	}

	public static String toHTMLString(String p_string) {
		StringBuffer sb = new StringBuffer(p_string.length());
		// true if last char was blank
		boolean lastWasBlankChar = false;
		int len = p_string.length();
		char c;

		for(int i = 0; i < len; i++) {
			c = p_string.charAt(i);

			if(c == ' ') {
				// blank gets extra work,
				// this solves the problem you get if you replace all
				// blanks with &nbsp;, if you do that you loss
				// word breaking
				if(lastWasBlankChar) {
					lastWasBlankChar = false;
					sb.append("&nbsp;");

				} else {
					lastWasBlankChar = true;
					sb.append(' ');

				}
			} else {
				lastWasBlankChar = false;
				//
				// HTML Special Chars
				if (c == '"')
					sb.append("&quot;");

				else if (c == '&')
					sb.append("&amp;");

				else if (c == '<')
					sb.append("&lt;");

				else if (c == '>')
					sb.append("&gt;");

				else if (c == '\n')
					// Handle Newline
					sb.append("<br/>");

				else {
					int ci = 0xffff & c;

					if (ci < 160)
						// nothing special only 7 Bit
						sb.append(c);

					else {
						// Not 7 Bit use the unicode system
						sb.append("&#");
						sb.append(new Integer(ci).toString());
						sb.append(';');
					}
				}
			}
		}

		return sb.toString();
	}

	public String toString() {
		return getString();
	}

	public int compareTo(I_DBObject p_dbObject) {
		return this.getSortString().compareTo(p_dbObject.getSortString());
	}
}
