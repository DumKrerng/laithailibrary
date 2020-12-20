package com.laithailibrary.sharelibrary.datatableview;

import com.laithailibrary.sharelibrary.collection.GList;
import com.laithailibrary.sharelibrary.db.dbobject.DBObject;
import exc.ExceptionHandler;
import exc.GException;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.text.Collator;
import java.util.List;
import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: dumkrerng
 * Date: 10/3/12
 * Time: 7:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class SortKeyValue implements Comparable<SortKeyValue>, Externalizable {

	public List<String> m_lsSortKeyValue = new GList<String>();

	private static final long serialVersionUID = 7529856155621476147L;

	public SortKeyValue() {}

	public SortKeyValue(DBObject p_dboSortKeyValue) throws GException {
		m_lsSortKeyValue.add(p_dboSortKeyValue.getSortString());
	}

	public void writeExternal(ObjectOutput out) throws IOException {
		try {
			out.writeObject(m_lsSortKeyValue);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			out.close();

			throw new IOException(exception);
		}
	}

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		try {
			m_lsSortKeyValue = (List<String>)in.readObject();

		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			in.close();

			throw new IOException(exception);
		}
	}

	public void add(DBObject p_dboSortKeyValue) throws GException {
		m_lsSortKeyValue.add(p_dboSortKeyValue.getSortString());
	}

	public String get(int p_index) throws GException {
		try {
		  return m_lsSortKeyValue.get(p_index);

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public String getStringValue() {
		String strReturn = "";

		try {
			for(String dboSortKeyValue : m_lsSortKeyValue) {
				if(strReturn.length() > 0) {
					strReturn += ", ";
				}

				strReturn += dboSortKeyValue;
			}
		} catch (Exception exception) {
			ExceptionHandler.display(exception);
		}

		return strReturn;
	}

	public String getString() {
		String strReturn = "";

		try {
			for(String dboSortKeyValue : m_lsSortKeyValue) {
				strReturn += "!#";
				strReturn += dboSortKeyValue;
			}
		} catch (Exception exception) {
			ExceptionHandler.display(exception);
		}

		return strReturn;
	}

	public String toString() {
		return getString();
	}

	public int compareTo(SortKeyValue p_skValue) {
		Locale locale = new Locale("th", "TH");
//		Locale.setDefault(locale);
		Collator collator = Collator.getInstance(locale);
//		collator.setStrength(Collator.SECONDARY);

		return collator.compare(this.getString(), p_skValue.getString());
//		return this.toString().compareTo(p_skValue.toString());
	}
}
