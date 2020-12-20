package com.laithailibrary.sharelibrary.db.dbobject;

import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import com.laithailibrary.logger.*;
import com.laithailibrary.sharelibrary.db.dbobject.support.*;
import com.laithailibrary.sharelibrary.db.dbutilities.*;
import com.laithailibrary.sharelibrary.locale.*;
import com.laithailibrary.sharelibrary.servicecall.*;
import com.laithailibrary.sharelibrary.session.*;
import com.laithailibrary.sharelibrary.socket.*;
import com.laithailibrary.sharelibrary.support.*;
import exc.*;

/**
 * Created by IntelliJ IDEA.
 * User: dumkrerng
 * Date: 4/26/12
 * Time: 11:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class DBDate extends DBObject {

	public static final String INVALID_DATE_DB = "1900-00-00";
	public static final String INVALID_DATE_DISPLAY = "00/00/1900";

	private String m_strDayInMonth = "";
	private String m_strMonthInYear = "";
	private String m_strYear = "";

	public static final String FORMAT_DASH_yyyy_MM_dd = "yyyy-MM-dd";
	public static final String FORMAT_SLASH_dd_MM_yyyy = "dd/MM/yyyy";
	public static final String FORMAT_SLASH_dd_MM_yy = "dd/MM/yy";

	public static final DateFormat DATEFORMAT_ENGLISH_DASH_yyyy_MM_dd = new SimpleDateFormat(FORMAT_DASH_yyyy_MM_dd, GLocale.English.getLocale());
	public static final DateFormat DATEFORMAT_ENGLISH_SLASH_dd_MM_yyyy = new SimpleDateFormat(FORMAT_SLASH_dd_MM_yyyy, GLocale.English.getLocale());

	public static final DateFormat DATEFORMAT_THAI_FULL = new SimpleDateFormat("EEEE ที่ dd เดือน MMMM พ.ศ. yyyy", GLocale.Thai.getLocale());
	public static final DateFormat DATEFORMAT_THAI_D_MMMM_YYYY = new SimpleDateFormat("d เดือน MMMM พ.ศ. yyyy", GLocale.Thai.getLocale());
	public static final DateFormat DATEFORMAT_THAI_DD_MMMM_YYYY = new SimpleDateFormat("dd เดือน MMMM พ.ศ. yyyy", GLocale.Thai.getLocale());
	public static final DateFormat DATEFORMAT_THAI_SLASH_dd_MM_yyyy = new SimpleDateFormat(FORMAT_SLASH_dd_MM_yyyy, GLocale.Thai.getLocale());

	private static final long serialVersionUID = 13;

	public DBDate() {
		super(INVALID_DATE_DB);
	}

	public DBDate(String p_strDayNumberInMonth, String p_strMonthNumberInYear, String p_strYear) throws GException {
		super(p_strDayNumberInMonth + '/' + p_strMonthNumberInYear + '/' + p_strYear);

		init();
	}

	public DBDate(int p_intDayNumberInMonth, int p_intMonthNumberInYear, int p_intYear) throws GException {
		super(Integer.toString(p_intDayNumberInMonth) + '/' + Integer.toString(p_intMonthNumberInYear) + '/' + Integer.toString(p_intYear));

		init();
	}

	public DBDate(String p_strDayNumberInMonth, String p_strMonthNumberInYear, String p_strYear, GLocale p_locale) throws GException {
		super(INVALID_DATE_DB);

		String strFormat = FORMAT_SLASH_dd_MM_yyyy;

		if(p_strYear.length() == 2) {
			strFormat = FORMAT_SLASH_dd_MM_yy;
		}

		convertToEnglishLocale(p_strDayNumberInMonth + '/' + p_strMonthNumberInYear + '/' + p_strYear, strFormat, p_locale);

		init();
	}

	public DBDate(String p_string) throws GException {
		super(p_string);

		init();
	}

	public DBDate(String p_string, GLocale p_locale) throws GException {
		super(INVALID_DATE_DB);

		convertToEnglishLocale(p_string, FORMAT_SLASH_dd_MM_yyyy, p_locale);

		init();
	}

	public DBDate(String p_string, String p_strFormat, GLocale p_locale) throws GException {
		super(INVALID_DATE_DB);

		convertToEnglishLocale(p_string, p_strFormat, p_locale);

		init();
	}

	private void convertToEnglishLocale(String p_string, String p_strFormat, GLocale p_locale) throws GException {
		try {
			DateFormat dateformat = new SimpleDateFormat(p_strFormat, p_locale.getLocale());
			Date date = dateformat.parse(p_string);

			Calendar calendar = Calendar.getInstance(p_locale.getLocale());
			calendar.setTime(date);

			m_strData = DATEFORMAT_ENGLISH_SLASH_dd_MM_yyyy.format(date);

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public DBDate(GResultSet p_resultSet, String p_strColumnName) throws GException {
		super(INVALID_DATE_DB);

		try {
			DBDate dbdResult = p_resultSet.getDate(p_strColumnName);
			m_strData = dbdResult.m_strData;

			init();

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	private void init() throws GException {
		try {
			setData();

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public void writeExternal(ObjectOutput p_out) throws IOException {
		try {
			super.writeExternal(p_out);

			p_out.writeUTF(m_strDayInMonth);
			p_out.writeUTF(m_strMonthInYear);
			p_out.writeUTF(m_strYear);
			p_out.writeUTF(m_strData);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			p_out.close();

			throw new IOException(exception);
		}
	}

	public void readExternal(ObjectInput p_in) throws IOException {
		try {
			super.readExternal(p_in);

			m_strDayInMonth = p_in.readUTF();
			m_strMonthInYear = p_in.readUTF();
			m_strYear = p_in.readUTF();
			m_strData = p_in.readUTF();

		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			p_in.close();

			throw new IOException(exception);
		}
	}

	protected void setInvalidData() {
		m_strData = INVALID_DATE_DB;
	}

	public void setStringValue(String p_strString) {
		super.setStringValue(p_strString);

		try {
			init();

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		}
	}

	public static DBDate getCurrentDate() throws GException {
		try {
			DBDate dbDate;

			if(GUtilities.isServer()) {
				DateFormat dateformat = new SimpleDateFormat(FORMAT_SLASH_dd_MM_yyyy, GLocale.English.getLocale());
				String strCurrentDate = dateformat.format(new Date());

				dbDate = new DBDate(strCurrentDate);

			} else {
				SessionData sessiondata = AppClientUtilities.getSessionData();
				SessionID sessionid = sessiondata.getSessionID();

				ServiceCall_CurrentDate servicecall = new ServiceCall_CurrentDate(sessionid, AppClientUtilities.getServerAddress());
				GLog.info("Call service CurrentDate.");

				Socket socket = ClientSocket.createSocket(AppClientUtilities.getServerAddress(), AppClientUtilities.getServerPort());

				OutputStream output = socket.getOutputStream();
				ObjectOutputStream objOutputStream = new ObjectOutputStream(output);
				objOutputStream.writeObject(servicecall);
				objOutputStream.flush();

				InputStream input = socket.getInputStream();
				ObjectInputStream objInputStream = new ObjectInputStream(input);
				dbDate = (DBDate) objInputStream.readObject();
			}

			return dbDate;

		} catch(SocketTimeoutException p_excTimeout) {
			throw new GException("Connection Timeout!!!");

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

//	public boolean isInvalid() {
//		if(m_strData.equalsIgnoreCase(INVALID_DATE_DB)) {
//			return true;
//		}
//
//		return false;
//	}
//
//	public boolean isValid() {
//		if(!m_strData.equalsIgnoreCase(INVALID_DATE_DB)) {
//			return true;
//		}
//
//		return false;
//	}

	protected String getInvalidDataString() {
		return INVALID_DATE_DB;
	}

	public boolean isValid() {
		if(m_strData.equalsIgnoreCase(INVALID_DATE_DB)) {
			return false;

		} else if(m_strData.equalsIgnoreCase(getInvalidDataString())) {
			return false;

		} else if(m_strData.length() == 0) {
			return false;
		}

		return true;
	}

	public boolean isInvalid() {
		if(isValid()) {
			return false;
		}

		return true;
	}

	public DBDate getDateDiff(int p_intDiff) throws GException {
		try {
			if(isInvalid()) {
				throw new GException("Invalid Date!");
			}

			Date date = getDate();

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.DATE, p_intDiff);

			date = calendar.getTime();

			DBDate dbdtReturn = new DBDate(DATEFORMAT_ENGLISH_SLASH_dd_MM_yyyy.format(date));

			return dbdtReturn;

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public DBDate getMonthDiff(int p_intDiff) throws GException {
		try {
			if(isInvalid()) {
				throw new GException("Invalid Date!");
			}

			Date date = getDate();

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.MONTH, p_intDiff);

			date = calendar.getTime();

			DBDate dbdtReturn = new DBDate(DATEFORMAT_ENGLISH_SLASH_dd_MM_yyyy.format(date));

			return dbdtReturn;

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public DBDate getYearDiff(int p_intDiff) throws GException {
		try {
			if(isInvalid()) {
				throw new GException("Invalid Date!");
			}

			Date date = getDate();

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.YEAR, p_intDiff);

			date = calendar.getTime();

			DBDate dbdtReturn = new DBDate(DATEFORMAT_ENGLISH_SLASH_dd_MM_yyyy.format(date));

			return dbdtReturn;

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public DBDate getFirstDate_InYear() throws GException {
		try {
			if(isInvalid()) {
				throw new GException("Invalid Date!");
			}

			DBDate dbdtReturn = new DBDate(1, 1, getYear().getYear());

			return dbdtReturn;

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public DBDate getLastDate_InYear() throws GException {
		try {
			if(isInvalid()) {
				throw new GException("Invalid Date!");
			}

			DBDate dbdtReturn = new DBDate(31, 12, getYear().getYear());

			return dbdtReturn;

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public DBDate getFirstDate_InMonth() throws GException {
		try {
			if(isInvalid()) {
				throw new GException("Invalid Date!");
			}

			DBDate dbdtReturn = new DBDate(1, getMonthInYear(), getYear().getYear());

			return dbdtReturn;

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public DBDate getLastDate_InMonth() throws GException {
		try {
			if(isInvalid()) {
				throw new GException("Invalid Date!");
			}

			Date date = getDate();

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

			date = calendar.getTime();

			DBDate dbdtReturn = new DBDate(DATEFORMAT_ENGLISH_SLASH_dd_MM_yyyy.format(date));

			return dbdtReturn;

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public int getDaysDiff(DBDate p_dbdtDate) throws GException {
		return getDaysDiff(this, p_dbdtDate);
	}

	public int getMonthDiff(DBDate p_dbdtDate) throws GException {
		return getMonthDiff(this, p_dbdtDate);
	}

	public int getYearDiff(DBDate p_dbdtDate) throws GException {
		return getYearDiff(this, p_dbdtDate);
	}

	public static int getDaysDiff(DBDate p_dbdtDate01, DBDate p_dbdtDate02) throws GException {
		try {
			if(p_dbdtDate01.isInvalid()
				|| p_dbdtDate02.isInvalid()) {

				throw new GException("Invalid Date!");
			}

			Date date_01 = p_dbdtDate01.getDate();
			Date date_02 = p_dbdtDate02.getDate();

			long logTimeDiff = date_01.getTime() - date_02.getTime();
			Long logDaysDiff = TimeUnit.DAYS.convert(logTimeDiff, TimeUnit.MILLISECONDS);

			return logDaysDiff.intValue();

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public static int getMonthDiff(DBDate p_dbdtDate01, DBDate p_dbdtDate02) throws GException {
		try {
			if(p_dbdtDate01.isInvalid()
				|| p_dbdtDate02.isInvalid()) {

				throw new GException("Invalid Date!!!");
			}

			int intYear_01 = p_dbdtDate01.getYear().getYear();
			int intMonth_01 = p_dbdtDate01.getMonthInYear();

			int intYear_02 = p_dbdtDate02.getYear().getYear();
			int intMonth_02 = p_dbdtDate02.getMonthInYear();

			int intTotalMonthNumber_01 = (intYear_01 * 12) + intMonth_01;
			int intTotalMonthNumber_02 = (intYear_02 * 12) + intMonth_02;

			return intTotalMonthNumber_01 - intTotalMonthNumber_02;

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public static int getYearDiff(DBDate p_dbdtDate01, DBDate p_dbdtDate02) throws GException {
		try {
			if(p_dbdtDate01.isInvalid()
				|| p_dbdtDate02.isInvalid()) {

				throw new GException("Invalid Date!!!");
			}

			int intMonthDiff = getMonthDiff(p_dbdtDate01, p_dbdtDate02);
			int intYearDiff = intMonthDiff / 12;

			return intYearDiff;

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public Date getDate() {
		try {
			if(m_strDayInMonth.isEmpty()
				|| m_strMonthInYear.isEmpty()
				|| m_strYear.isEmpty()) {

				return new Date();
			}

			Date date = DATEFORMAT_ENGLISH_DASH_yyyy_MM_dd.parse(getYear_String() + '-' + getMonthInYear_String() + '-' + getDayInMonth_String());

			return date;

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
		}

		return new Date();
	}

	public String getStringSQL() {
		String string = super.getString();
		String strReturn = "NULL";

		if(string.length() > 0) {
			strReturn = "'" + string + "'";

		} else {
			strReturn = INVALID_DATE_DB;
		}

		if(string.compareTo(INVALID_DATE_DB) == 0) {
			strReturn = "NULL";
		}

		return strReturn;
	}

	public String toString() {
		return getString_Display();
	}

	public String getString() {
		return getString_Display();
	}

	public String getString(DateFormat p_dateformat) {
		if(m_strDayInMonth.isEmpty()
			|| m_strMonthInYear.isEmpty()
			|| m_strYear.isEmpty()) {

			return "";
		}

		return p_dateformat.format(getDate());
	}

	public String getString(GLocaleID p_localeid) {
		if(m_strDayInMonth.isEmpty()
			|| m_strMonthInYear.isEmpty()
			|| m_strYear.isEmpty()) {

			return "";
		}

		return getString(p_localeid.getLocale());
	}

	public String getString(GLocale p_locale) {
		if(m_strDayInMonth.isEmpty()
			|| m_strMonthInYear.isEmpty()
			|| m_strYear.isEmpty()) {

			return "";
		}

		return getString(FORMAT_SLASH_dd_MM_yyyy, p_locale);
	}

	public String getString(String p_strFormat, GLocale p_locale) {
		if(m_strDayInMonth.isEmpty()
			|| m_strMonthInYear.isEmpty()
			|| m_strYear.isEmpty()) {

			return "";
		}

		DateFormat dateformat = new SimpleDateFormat(p_strFormat, p_locale.getLocale());

		return getString(dateformat);
	}

	public String getString_DefaultFormat() throws GException {
		if(m_strDayInMonth.isEmpty()
			|| m_strMonthInYear.isEmpty()
			|| m_strYear.isEmpty()) {

			return "";
		}

		return DATEFORMAT_ENGLISH_DASH_yyyy_MM_dd.format(getDate());
	}

	private void setData() throws GException {
		try {
			if(m_strData.compareTo(INVALID_DATE_DB) == 0) {
				resetData();

				return;
			}

			if(m_strData.compareTo(STRING_INVALID) == 0) {
				resetData();

				return;
			}

			String strData_Temp = m_strData;

			StringTokenizer tokenizer = new StringTokenizer(strData_Temp, "/");

			m_strData = "";
			String strTokenizerElement = "";

			int index = 0;

			if(tokenizer.countTokens() != 3) {
				tokenizer = new StringTokenizer(strData_Temp, "-");

				if(tokenizer.countTokens() != 3) {
					throw new GException("Invalid Date");

				} else {
					strTokenizerElement = "-";
				}
			} else {
				strTokenizerElement = "/";
			}

			if(strTokenizerElement.equals("-")) {
				while(tokenizer.hasMoreTokens()) {
					index++;

					if(index == 1) {
						m_strYear = tokenizer.nextToken();
						verifyNumber(m_strYear);

					} else if(index == 2) {
						m_strMonthInYear = tokenizer.nextToken();
						verifyNumber(m_strMonthInYear);

					} else if(index == 3) {
						m_strDayInMonth = tokenizer.nextToken();
						verifyNumber(m_strDayInMonth);

					} else {
						throw new GException("Invalid Date");
					}
				}
			} else if(strTokenizerElement.compareTo("/") == 0) {
				while(tokenizer.hasMoreTokens()) {
					index++;

					if(index == 1) {
						m_strDayInMonth = tokenizer.nextToken();
						verifyNumber(m_strDayInMonth);

					} else if(index == 2) {
						m_strMonthInYear = tokenizer.nextToken();
						verifyNumber(m_strMonthInYear);

					} else if(index == 3) {
						m_strYear = tokenizer.nextToken();
						verifyNumber(m_strYear);

					} else {
						throw new GException("Invalid Date");
					}
				}
			} else {
				throw new GException("Invalid Date");
			}

			if(m_strDayInMonth.isEmpty()
				|| m_strMonthInYear.isEmpty()
				|| m_strYear.isEmpty()) {

				throw new GException("Invalid Date");
			}

			m_strData = getStringDate();

			verifyDateData(m_strData);

		} catch (Exception exception) {
			resetData();

			ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public int getDayInMonth() {
		return Integer.parseInt(m_strDayInMonth);
	}

	public int getMonthInYear() {
		return Integer.parseInt(m_strMonthInYear);
	}

	public DBYear getYear() {
		return new DBYear(m_strYear);
	}

	public String getDayInMonth_String() {
		return m_strDayInMonth;
	}

	public String getMonthInYear_String() {
		return m_strMonthInYear;
	}

	public String getYear_String() {
		return m_strYear;
	}

	protected void resetData() {
		super.resetData();

		m_strDayInMonth = "";
		m_strMonthInYear = "";
		m_strYear = "";
		m_strData = INVALID_DATE_DB;
	}

	private String getStringDate() {
		if(m_strDayInMonth.isEmpty()
			|| m_strMonthInYear.isEmpty()
			|| m_strYear.isEmpty()) {

			return "";
		}

		return m_strYear + "-" + m_strMonthInYear + "-" + m_strDayInMonth;
	}

	private String getString_Display() {
		if(m_strDayInMonth.isEmpty()
			|| m_strMonthInYear.isEmpty()
			|| m_strYear.isEmpty()) {

			return "";
		}
		
		return m_strDayInMonth + "/" + m_strMonthInYear + "/" + m_strYear;
	}

	public String getCSVString() {
		return getString_Display();
	}

	public java.sql.Date getValueReport() {
		if(isInvalid()) {
			return null;
		}

		return java.sql.Date.valueOf(getStringSQL().replace("'", ""));
	}

	public String getString_ThaiNumber(String p_strFormat, GLocale p_locale) throws GException {
		try {
			String strString = getString(p_strFormat, p_locale);

			return DBObject.getString_ThaiNumber(strString);

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public String getString_ThaiNumber(DateFormat p_dateformat) throws GException {
		try {
			String strString = getString(p_dateformat);

			return DBObject.getString_ThaiNumber(strString);

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public DayID getDayID() throws GException {
		try {
			DateFormat dateformat = new SimpleDateFormat("EEEE", GLocale.English.getLocale());
			String strDay = getString(dateformat);

			DayID dayid = DayID.getDayID_ENGDayLabel(strDay);

			return dayid;

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public String getSortString() {
		String strSortString = "!$3DAT$!" + getStringDate();

		return strSortString;
	}

	private void verifyDateData(String p_string) throws GException {
		try {
			if(p_string.compareTo(INVALID_DATE_DB) == 0
				|| p_string.compareTo(INVALID_DATE_DISPLAY) == 0) {

				resetData();

				return;
			}

			java.sql.Date.valueOf(p_string);

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);

			resetData();

		  throw new GException("Invalid Date: " + p_string);
		}
	}

	private void verifyNumber(String p_string) throws GException {
		if(!isNumber(p_string)) {
			resetData();

			throw new GException("Invalid Date!");
		}
	}

	private boolean isNumber(String p_string) {
		try{
			Integer.parseInt(p_string);

		} catch(NumberFormatException nfe) {
			return false;
		}

		return true;
	}

	public int compareTo(I_DBObject p_dbObject) {
		try {
			if(p_dbObject instanceof DBDate) {
				return this.getSortString().compareTo(p_dbObject.getSortString());
			} else {
				throw new GException("Attempting compare " + getClass().getName() + " to " + p_dbObject.getClass().getName());
			}
		} catch(Exception exception) {
			ExceptionHandler.display(exception);
		}

		return -1;
	}

	public DBDate clone() {
		try {
			return new DBDate(getDayInMonth(), getMonthInYear(), getYear().getYear());

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
		}

		return new DBDate();
	}
}
