package com.laithailibrary.sharelibrary.db.dbobject;

import java.util.*;
import com.laithailibrary.sharelibrary.collection.*;
import com.laithailibrary.sharelibrary.datatableview.*;
import com.laithailibrary.sharelibrary.db.dbutilities.*;
import com.laithailibrary.sharelibrary.service.*;
import exc.*;

/**
 * Created by IntelliJ IDEA.
 * User: dumkrerng
 * Date: 6/4/11
 * Time: 1:36 AM
 * To change this template use File | Settings | File Templates.
 */
public class DBString extends DBObject {

	private static final long serialVersionUID = -6996848812L;

	public DBString() {
		super("");
	}

	public DBString(String p_strString) {
		super(p_strString);
	}

	public DBString(int p_int) {
		super(String.valueOf(p_int));
	}

	public DBString(DBString p_dbstrString) {
		super(p_dbstrString.getString().trim());
	}

	public DBString(DBObject p_dbObject) {
		super(p_dbObject.getString().trim());
	}

	public DBString(GResultSet p_resultSet, String p_strColumnName) throws GException {
		super("");

		try {
			String strResult = p_resultSet.getString(p_strColumnName);

			if(strResult == null) {
				strResult = "";
			}

			super.setStrData(strResult);

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public DBString(DataVectorTable p_dtvView, String p_strColumnName) throws GException {
		super("");

		try {
			DataVectorRow dvrRow = p_dtvView.getDataRow();
			DBString dbstrValue = dvrRow.getDataAtColumnName(DBString.class, p_strColumnName);

			if(dbstrValue.isValid()) {
				super.setStrData(dbstrValue.getString());
			}
		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public DBString(DataVectorRow p_dvrRow, String p_strColumnName) throws GException {
		super("");

		try {
			DBString dbstrValue = p_dvrRow.getDataAtColumnName(DBString.class, p_strColumnName);

			if(dbstrValue.isValid()) {
				super.setStrData(dbstrValue.getString());
			}
		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public DBString concat(DBString p_dbString) {
		DBString dbstrResult = new DBString(this.getString().concat(p_dbString.getString()));

		return dbstrResult;
	}

	public DBString concat(DBObject p_dbObject) {
		DBString dbstrResult = new DBString(this.getString().concat(p_dbObject.getString()));

		return dbstrResult;
	}

	public DBString concat(String p_srtString) {
		DBString dbstrResult = new DBString(this.getString().concat(p_srtString));

		return dbstrResult;
	}

	public DBString replaceAll(String regex, String replacement) throws GException {
		try {
		  String strString = getString();
			strString = strString.replaceAll(regex, replacement);

			return new DBString(strString);

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public DBString replace(CharSequence target, CharSequence replacement) throws GException {
		try {
		  String strString = getString();
			strString = strString.replace(target, replacement);

			return new DBString(strString);

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public DBString replace(char oldChar, char newChar) throws GException {
		try {
		  String strString = getString();
			strString = strString.replace(oldChar, newChar);

			return new DBString(strString);

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public DBString substring(int beginIndex) throws GException {
		try {
		  String strString = getString();
			strString = strString.substring(beginIndex);

			return new DBString(strString);

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public DBString substring(int beginIndex, int endIndex) throws GException {
		try {
		  String strString = getString();
			strString = strString.substring(beginIndex, endIndex);

			return new DBString(strString);

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public boolean startsWith(DBString p_dbstrPrefix) throws GException {
		return startsWith(p_dbstrPrefix.getString());
	}

	public boolean startsWith(DBString p_dbstrPrefix, int p_intOffset) throws GException {
		return startsWith(p_dbstrPrefix.getString(), p_intOffset);
	}

	public boolean startsWith(String p_strPrefix) throws GException {
		try {
		  String strString = getString();

			return strString.startsWith(p_strPrefix);

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public boolean startsWith(String p_strPrefix, int p_intOffset) throws GException {
		try {
		  String strString = getString();

			return strString.startsWith(p_strPrefix, p_intOffset);

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public List<DBString> split(String p_strString) throws GException {
		try {
			List<DBString> lsStrings = new GList<>();
		  String strToSplit = getString();

			if(strToSplit.trim().length() <= 0) {
				return lsStrings;
			}

			String[] strToSplits = strToSplit.split(p_strString);

			for(String strTemp : strToSplits) {
				DBString dbstrTemp = new DBString(strTemp);
				lsStrings.add(dbstrTemp);
			}

			return lsStrings;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public int length() throws GException {
		try {
		  String strString = getString();

			return strString.length();

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public DBString getEncoded(String p_strKey) throws GException {
		try {
			String strEncoded = GStringEncoder.encode(p_strKey, getString());

			return new DBString(strEncoded);

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public boolean isSameEncoded(String p_strKey, String p_strString_NotEncoded) throws GException {
		try {
			String strString = GStringEncoder.encode(p_strKey, p_strString_NotEncoded);

			if(getString().compareTo(strString) == 0) {
				return true;
			}

			return false;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public DBString getEncodeToBase64() throws GException {
		try {
			String strOriginal = getString();
			String strBase64 = Base64.getEncoder().encodeToString(strOriginal.getBytes("utf-8"));

			return new DBString(strBase64);

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public DBString getDecoderFormBase64() throws GException {
		try {
			String strOriginal = getString();

			// Decode
			byte[] base64decodedBytes = Base64.getDecoder().decode(strOriginal);
			String strString_Decoded = new String(base64decodedBytes, "utf-8");

			return new DBString(strString_Decoded);

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	protected String getInvalidDataString() {
		return String.valueOf("");
	}

	public String getString() {
		return super.getDataString();
	}

	public String getStringSQL() throws GException {
		verifySQLInjection();

		String strReturn;

		if(isValid()) {
			strReturn = "'" + super.getString() + "'";

		} else {
			strReturn = "NULL";
		}

		return strReturn;
	}

	public String getStringSQL_IgnoreSQLInjection() {
		String strReturn;

		if(isValid()) {
			strReturn = "'" + super.getString() + "'";

		} else {
			strReturn = "NULL";
		}

		return strReturn;
	}

	public String getCSVString() {
		String strReturn = super.getString();

		return strReturn;
	}

	public String getValueReport() {
		if(isInvalid()) {
			return null;
		}

		return getString();
	}

	public String getSortString() {
		String strSortString = "!$1STR$!" + getString();

		return strSortString;
	}

	public int compareTo(I_DBObject p_dbObject) {
		try {
			if(p_dbObject instanceof DBString) {
				return this.getSortString().compareTo(p_dbObject.getSortString());
			} else {
				throw new GException("Attempting compare " + getClass().getName() + " to " + p_dbObject.getClass().getName());
			}
		} catch(Exception exception) {
			ExceptionHandler.display(exception);
		}

		return -1;
	}

	public DBString clone() {
		try {
			return new DBString(getString());

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
		}

		return new DBString();
	}
}
