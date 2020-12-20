package com.laithailibrary.sharelibrary.db.dbobject;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.text.*;
import java.util.Date;
import java.util.*;
import java.util.concurrent.*;
import com.laithailibrary.logger.*;
import com.laithailibrary.sharelibrary.db.dbutilities.*;
import com.laithailibrary.sharelibrary.locale.*;
import com.laithailibrary.sharelibrary.servicecall.*;
import com.laithailibrary.sharelibrary.session.*;
import com.laithailibrary.sharelibrary.socket.*;
import com.laithailibrary.sharelibrary.support.*;
import exc.*;

/**
 * Created with IntelliJ IDEA.
 * User: dumkrerng
 * Date: 11/8/12
 * Time: 7:02 AM
 * To change this template use File | Settings | File Templates.
 */
public class DBDateTime extends DBObject {

	private static final String INVALID_DATETIME = "1900-00-00 00:00:00";

	private DBDate m_dbdDate = new DBDate();

	private String m_strHour = "";
	private String m_strMinute = "";
	private String m_strSecond = "00";

	private static final String FORMAT_DASH_yyyy_MM_dd = "yyyy-MM-dd HH:mm:ss";
	private static final String FORMAT_SLASH_dd_MM_yyyy = "dd/MM/yyyy HH:mm:ss";

	private static final DateFormat DATETIMEFORMAT_ENGLISH_DASH_yyyy_MM_dd_HH_mm_ss = new SimpleDateFormat(FORMAT_DASH_yyyy_MM_dd, GLocale.English.getLocale());
	private static final DateFormat DATETIMEFORMAT_ENGLISH_SLASH_dd_MM_yyyy = new SimpleDateFormat(FORMAT_SLASH_dd_MM_yyyy, GLocale.English.getLocale());

	private static final DateFormat DATEFORMAT_THAI_FULL = new SimpleDateFormat("EEEE ที่ dd เดือน MMMM พ.ศ. yyyy HH:mm:ss", GLocale.Thai.getLocale());

	private static final long serialVersionUID = 13;

	public DBDateTime() {
		super(INVALID_DATETIME);
	}

	public DBDateTime(String p_string) throws GException {
		super(p_string);

		try {
			setData();

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public DBDateTime(DBDate p_dbDate) throws GException {
		super(p_dbDate.getString() + " 00:00:00");

		try {
			setData();

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public DBDateTime(DBDate p_dbDate, DBDateTime p_dbdtmTime) throws GException {
		super(p_dbDate.getString() + " " + p_dbdtmTime.getStringTime());

		try {
			setData();

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public DBDateTime(String p_string, GLocale p_locale) throws GException {
		super(INVALID_DATETIME);

		convertToEnglishLocale(p_string, FORMAT_SLASH_dd_MM_yyyy, p_locale);
		setData();
	}

	public DBDateTime(String p_string, String p_strFormat, GLocale p_locale) throws GException {
		super(INVALID_DATETIME);

		convertToEnglishLocale(p_string, p_strFormat, p_locale);
		setData();
	}

	private void convertToEnglishLocale(String p_string, String p_strFormat, GLocale p_locale) throws GException {
		try {
			DateFormat dateformat = new SimpleDateFormat(p_strFormat, p_locale.getLocale());
			Date date = dateformat.parse(p_string);

			Calendar calendar = Calendar.getInstance(p_locale.getLocale());
			calendar.setTime(date);

			m_strData = DATETIMEFORMAT_ENGLISH_DASH_yyyy_MM_dd_HH_mm_ss.format(date);

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public DBDateTime(GResultSet p_resultSet, String p_strColumnName) throws GException {
		super(INVALID_DATETIME);

		try {
			DBDateTime dbdtResult = p_resultSet.getDateTime(p_strColumnName);
			m_strData = dbdtResult.m_strData;

			setData();

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public void writeExternal(ObjectOutput p_out) throws IOException {
		try {
			super.writeExternal(p_out);

			p_out.writeObject(m_dbdDate);
			p_out.writeUTF(m_strHour);
			p_out.writeUTF(m_strMinute);
			p_out.writeUTF(m_strSecond);
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

			m_dbdDate = (DBDate)p_in.readObject();
			m_strHour = p_in.readUTF();
			m_strMinute = p_in.readUTF();
			m_strSecond = p_in.readUTF();
			m_strData = p_in.readUTF();

		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			p_in.close();

			throw new IOException(exception);
		}
	}

	protected void setInvalidData() {
		m_strData = INVALID_DATETIME;
	}

	public void setStringValue(String p_strString) {
		super.setStringValue(p_strString);

		try {
			setData();

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
		}
	}

	public static DBDateTime getCurrentDateTime() throws GException {
		try {
			DBDateTime dbDateTime;

			if(GUtilities.isServer()) {
				DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
				String strCurrentDate = df.format(new java.util.Date());

				dbDateTime = new DBDateTime(strCurrentDate);

			} else {
				SessionData sessiondata = AppClientUtilities.getSessionData();
				SessionID sessionid = sessiondata.getSessionID();

				ServiceCall_CurrentDateTime servicecall = new ServiceCall_CurrentDateTime(sessionid, AppClientUtilities.getServerAddress());
				GLog.info("Call service CurrentDateTime.");

				Socket socket = ClientSocket.createSocket(AppClientUtilities.getServerAddress(), AppClientUtilities.getServerPort());

				OutputStream output = socket.getOutputStream();
				ObjectOutputStream objOutputStream = new ObjectOutputStream(output);
				objOutputStream.writeObject(servicecall);
				objOutputStream.flush();

				InputStream input = socket.getInputStream();
				ObjectInputStream objInputStream = new ObjectInputStream(input);
				dbDateTime = (DBDateTime) objInputStream.readObject();
			}

			return dbDateTime;

		} catch(SocketTimeoutException p_excTimeout) {
			throw new GException("Connection Timeout!!!");

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public DBDate getDBDate() throws GException {
		return m_dbdDate;
	}

	public DBDateTime getDateTimeDiff(int p_intMinutesDiff) throws GException {
		try {
			if(isInvalid()) {
				throw new GException("Invalid Date!");
			}

			Date date = getDate();

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.MINUTE, p_intMinutesDiff);

			date = calendar.getTime();

			DBDateTime dbdtmReturn = new DBDateTime(DATETIMEFORMAT_ENGLISH_DASH_yyyy_MM_dd_HH_mm_ss.format(date));

			return dbdtmReturn;

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public DBDateTime getDateDiff(int p_intDiff) throws GException {
		try {
			if(isInvalid()) {
				throw new GException("Invalid Date!");
			}

			DBDate dbdDate = getDBDate();
			dbdDate = dbdDate.getDateDiff(p_intDiff);

			DBDateTime dbdtmReturn = new DBDateTime(dbdDate, this);

			return dbdtmReturn;

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public DBDate getFirstDate_InYear() throws GException {
		return getDBDate().getFirstDate_InYear();
	}

	public DBDate getLastDate_InYear() throws GException {
		return getDBDate().getLastDate_InYear();
	}

	public DBDate getFirstDate_InMonth() throws GException {
		return getDBDate().getFirstDate_InMonth();
	}

	public DBDate getLastDate_InMonth() throws GException {
		return getDBDate().getLastDate_InMonth();
	}

	public int getDiff_Minutes(DBDateTime p_dbdtDateTime) throws GException {
		try {
			if(isInvalid()
				|| p_dbdtDateTime.isInvalid()) {

				throw new GException("Invalid Date!");
			}

			Date date_This = this.getDate();
			Date date_02 = p_dbdtDateTime.getDate();

			long logTimeDiff = date_This.getTime() - date_02.getTime();
			Long logMinutesDiff = TimeUnit.MINUTES.convert(logTimeDiff, TimeUnit.MILLISECONDS);

			return logMinutesDiff.intValue();

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public int getDaysDiff(DBDateTime p_dbdtDateTime) throws GException {
		return getDaysDiff(this, p_dbdtDateTime);
	}

	public int getMonthDiff(DBDateTime p_dbdtDateTime) throws GException {
		return getMonthDiff(this, p_dbdtDateTime);
	}

	public int getYearDiff(DBDateTime p_dbdtDateTime) throws GException {
		return getYearDiff(this, p_dbdtDateTime);
	}

	public static int getDaysDiff(DBDateTime p_dbdtDateTime01, DBDateTime p_dbdtDateTime02) throws GException {
		return DBDate.getDaysDiff(p_dbdtDateTime01.getDBDate(), p_dbdtDateTime02.getDBDate());
	}

	public static int getMonthDiff(DBDateTime p_dbdtDateTime01, DBDateTime p_dbdtDateTime02) throws GException {
		return DBDate.getMonthDiff(p_dbdtDateTime01.getDBDate(), p_dbdtDateTime02.getDBDate());
	}

	public static int getYearDiff(DBDateTime p_dbdtDateTime01, DBDateTime p_dbdtDateTime02) throws GException {
		return DBDate.getYearDiff(p_dbdtDateTime01.getDBDate(), p_dbdtDateTime02.getDBDate());
	}

	public static DBDateTime newTime_InvalidDate(String p_strTime) throws GException {
		return new DBDateTime("1900-01-01 " + p_strTime);
	}

	public Date getDate() throws GException {
		try {
			Date date = DATETIMEFORMAT_ENGLISH_DASH_yyyy_MM_dd_HH_mm_ss.parse(m_dbdDate.getYear_String() + '-' + m_dbdDate.getMonthInYear_String()
				+ '-' + m_dbdDate.getDayInMonth_String() + ' ' + m_strHour + ':' + m_strMinute + ':' + m_strSecond);

			return date;

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public String getStringSQL() {
		String string = super.getString();
		String strReturn;

		if(string.length() > 0) {
			strReturn = "'" + string + "'";

		} else {
			strReturn = INVALID_DATETIME;
		}

		if(string.compareTo(INVALID_DATETIME) == 0) {
			strReturn = "NULL";
		}

		return strReturn;
	}

	public String getStringSQL_InvalidDateTime() {
		String string = super.getString();
		String strReturn;

		if(string.length() > 0) {
			strReturn = "'" + string + "'";

		} else {
			strReturn = "'" + INVALID_DATETIME + "'";
		}

		return strReturn;
	}

	public String toString() {
		return getStringDateData_Display();
	}

	public String getString() {
		return getStringDateData_Display();
	}

	public String getString(DateFormat p_dateformat) throws GException {
		if(isInvalid()) {
			return "";
		}

		return p_dateformat.format(getDate());
	}

	public String getString(GLocaleID p_localeid) throws GException {
		return getString(FORMAT_SLASH_dd_MM_yyyy, p_localeid.getLocale());
	}

	public String getString(GLocale p_locale) throws GException {
		return getString(FORMAT_SLASH_dd_MM_yyyy, p_locale);
	}

	public String getString(String p_strFormat, GLocale p_locale) throws GException {
		DateFormat dateformat = new SimpleDateFormat(p_strFormat, p_locale.getLocale());

		return getString(dateformat);
	}

	public String getString_DefaultFormat() throws GException {
		return DATETIMEFORMAT_ENGLISH_DASH_yyyy_MM_dd_HH_mm_ss.format(getDate());
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

	public String getCSVString() {
		return getStringDateData_Display();
	}

	public Timestamp getValueReport() {
		if(isInvalid()) {
			return null;
		}

		return Timestamp.valueOf(getStringSQL().replace("'", ""));
	}

	protected String getInvalidDataString() {
		return INVALID_DATETIME;
	}

	public boolean isValid() {
		if(m_strData.equalsIgnoreCase(INVALID_DATETIME)) {
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

	private void setData() throws GException {
		try {
			if(m_strData.contains(INVALID_DATETIME)) {
				resetData();

				return;
			}

			if(m_strData.compareTo(STRING_INVALID) == 0) {
				resetData();

				return;
			}

			StringTokenizer tokenizer = new StringTokenizer(m_strData, " ");

			if(tokenizer.countTokens() == 1) {
				m_strData = m_strData.concat(" 00:00");
				tokenizer = new StringTokenizer(m_strData, " ");
			}

			m_strData = "";

			int index = 0;

			if(tokenizer.countTokens() != 2) {
				throw new GException("Invalid Date Time!");

			} else {
				String strDate = tokenizer.nextToken();
				String strTime = tokenizer.nextToken();

				StringTokenizer tokenizerTime = new StringTokenizer(strTime, ".");

				if(tokenizerTime.countTokens() == 2) {
					strTime = tokenizerTime.nextToken();
				}

				m_dbdDate = new DBDate(strDate);

				StringTokenizer tokenizer_Time = new StringTokenizer(strTime, ":");

				while(tokenizer_Time.hasMoreTokens()) {
					index++;

					if(index == 1) {
						m_strHour = tokenizer_Time.nextToken();
						verifyNumber(m_strHour);

					} else if(index == 2) {
						m_strMinute = tokenizer_Time.nextToken();
						verifyNumber(m_strMinute);

					} else if(index == 3) {
						m_strSecond = tokenizer_Time.nextToken();
						verifyNumber(m_strSecond);

					} else {
						throw new GException("Invalid Date Time!");
					}
				}
			}

			if(m_strHour.isEmpty() || m_strMinute.isEmpty()) {

				throw new GException("Invalid Date Time!");
			}

			m_strData = getStringDateTime();

			verifyDateDataTime(m_strData);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			resetData();
			throw new GException(exception);
		}
	}

	protected void resetData() {
		super.resetData();

		m_dbdDate = new DBDate();

		m_strHour = "";
		m_strMinute = "";
		m_strSecond = "00";

		m_strData = INVALID_DATETIME;
	}

	private String getStringDateTime() {
		String strReturn = "";

		if(m_strHour.isEmpty()
			|| m_strMinute.isEmpty()) {

			return strReturn;
		}

		if(m_dbdDate.isInvalid()) {
			strReturn = DBDate.INVALID_DATE_DB;

		} else {
			strReturn = m_dbdDate.getYear_String() + "-" + m_dbdDate.getMonthInYear_String() + "-" + m_dbdDate.getDayInMonth_String();
		}

		strReturn = strReturn + " " + m_strHour + ":" + m_strMinute + ":" + m_strSecond;

		return strReturn;
	}

	private String getStringTime() {
		String strReturn = "";

		if(m_strHour.isEmpty()
			|| m_strMinute.isEmpty()) {

			return strReturn;
		}

		strReturn = m_strHour + ":" + m_strMinute + ":" + m_strSecond;

		return strReturn;
	}

	private String getStringDateData_Display() {
		String strReturn = "";

		if(m_strHour.isEmpty()
			|| m_strMinute.isEmpty()) {

			return strReturn;
		}

		if(m_dbdDate.isInvalid()) {
			strReturn = DBDate.INVALID_DATE_DISPLAY;

		} else {
			strReturn = m_dbdDate.getDayInMonth_String() + "/" + m_dbdDate.getMonthInYear_String() + "/" + m_dbdDate.getYear_String();
		}

		strReturn = strReturn + " " + m_strHour + ":" + m_strMinute + ":" + m_strSecond;

		return strReturn;
	}

	public String getSortString() {
		String strSortString = "!$3DAT$!" + getStringDateTime();

		return strSortString;
	}

	private void verifyDateDataTime(String p_string) throws GException {
		try {
			StringTokenizer tokenizer = new StringTokenizer(p_string, " ");
			String strDate = tokenizer.nextToken();
			String strTime = tokenizer.nextToken();

			if(DBDate.INVALID_DATE_DB.compareTo(strDate) != 0) {
				java.sql.Date.valueOf(strDate);
			}

			Time.valueOf(strTime);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			resetData();

			throw new GException(exception);
		}
	}

	private void verifyNumber(String p_string) throws GException {
		if(!isNumber(p_string)) {
			resetData();

			throw new GException("Invalid Date");
		}
	}

	private boolean isNumber(String p_string) {
		try {
			Integer.parseInt(p_string);

		} catch(NumberFormatException nfe) {
			return false;
		}

		return true;
	}

	public int compareTo(I_DBObject p_dbObject) {
		try {
			if(p_dbObject instanceof DBDateTime) {
				return this.getSortString().compareTo(p_dbObject.getSortString());
			} else {
				throw new GException("Attempting compare " + getClass().getName() + " to " + p_dbObject.getClass().getName());
			}
		} catch(Exception exception) {
			ExceptionHandler.display(exception);
		}

		return -1;
	}

	public DBDateTime clone() {
		try {
			return new DBDateTime(getString(FORMAT_DASH_yyyy_MM_dd, GLocale.English));

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
		}

		return new DBDateTime();
	}
}
