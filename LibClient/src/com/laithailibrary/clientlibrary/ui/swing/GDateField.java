package com.laithailibrary.clientlibrary.ui.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import com.laithailibrary.clientlibrary.ui.support.*;
import com.laithailibrary.sharelibrary.db.dbobject.*;
import com.laithailibrary.sharelibrary.field.*;
import com.laithailibrary.sharelibrary.locale.*;
import com.laithailibrary.sharelibrary.support.*;
import exc.*;

/**
 * Created by IntelliJ IDEA.
 * User: dumkrerng
 * Date: 5/1/12
 * Time: 11:56 AM
 * To change this template use File | Settings | File Templates.
 */
public class GDateField extends GTextField {

	private GDateField me;
	private String m_DateString_Display;
	private DBDate m_dbtDate = new DBDate();

	public GDateField()  {
		try {
			init("");

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		}
	}

	public GDateField(DataField p_datafield)  {
		super(p_datafield);

		try {
			init("");

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		}
	}

	public GDateField(int p_int) throws GException {
		super(p_int);

		try {
			init("");

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
			displayError();
		}
	}

	public GDateField(DataField p_datafield, int p_int) throws GException {
		super(p_datafield, p_int);

		try {
			init("");

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
			displayError();
		}
	}

	public GDateField(int p_int, Mandatory p_mandatory) throws GException {
		super(p_int, p_mandatory);

		try {
			init("");

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
			displayError();
		}
	}

	public GDateField(String p_strDate, int p_int) throws GException {
		super(p_int);

		try {
			init(p_strDate);

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
			displayError();
		}
	}

	public GDateField(String p_strObjectName, String p_strDate, int p_int) throws GException {
		super(p_strObjectName, "", p_int);

		try {
			init(p_strDate);

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
			displayError();
		}
	}

	public GDateField(String p_strObjectName, String p_strDate, int p_int, Mandatory p_mandatory) throws GException {
		super(p_strObjectName, "", p_int, p_mandatory);

		try {
			init(p_strDate);

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
			displayError();
		}
	}

	public GDateField(String p_strDate, int p_int, Mandatory p_mandatory) throws GException {
		super(p_int, p_mandatory);

		try {
			init(p_strDate);

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
			displayError();
		}
	}

	public GDateField(DBDate p_dbDate, int p_int) throws GException {
		super(p_int);

		try {
			init(p_dbDate.getString(AppClientUtilities.getLocaleID()));

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
			displayError();
		}
	}

	public GDateField(String p_strObjectName, DBDate p_dbDate, int p_int) throws GException {
		super(p_strObjectName, "", p_int);

		try {
			init(p_dbDate.getString(AppClientUtilities.getLocaleID()));

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
			displayError();
		}
	}

	public GDateField(DBDate p_dbDate, int p_int, Mandatory p_mandatory) throws GException {
		super(p_int, p_mandatory);

		try {
			init(p_dbDate.getString(AppClientUtilities.getLocaleID()));

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
			displayError();
		}
	}

	public GDateField(String p_strObjectName, DBDate p_dbDate, int p_int, Mandatory p_mandatory) throws GException {
		super(p_strObjectName, "", p_int, p_mandatory);

		try {
			init(p_dbDate.getString(AppClientUtilities.getLocaleID()));

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
			displayError();
		}
	}

	private void init(String p_string) throws GException {
		try {
			me = this;

			JLabel label = new JLabel("dd/mm/yy");

			if(AppClientUtilities.getLocale() == GLocale.Thai) {
				label = new JLabel("วว/ดด/ปป");
			}

			setLayout(new GridLayout(2, 1));
			label.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 12));
			label.setForeground(Color.BLUE);
			label.setHorizontalAlignment(RIGHT);
			add(label);

			setText(p_string);
			registerActionListener();

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
			displayError();
		}
	}

	public void setText(String p_string) {
		try {
			verifyDate(p_string);

			super.setText(p_string);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
		}
	}

	public void setText(DBDate p_dbtDate) {
		try {
			String string = p_dbtDate.getString(AppClientUtilities.getLocaleID());

			verifyDate(string);

			super.setText(string);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
		}
	}

	public void setDBDate(DBDate p_dbdtDate) {
		try {
			String string = p_dbdtDate.getString(AppClientUtilities.getLocaleID());

			verifyDate(string);

			super.setText(string);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
		}
	}

	public DBDate getDBDate() {
		if(getText().length() > 0) {
			return m_dbtDate;
		}

		m_dbtDate = new DBDate();

		return m_dbtDate;
	}

	private void verifyDate(String p_strDate) throws GException {
		try {
			int intLength = p_strDate.length();

			if(intLength > 0) {
				DBDate dbdtDate = new DBDate();

				boolean bolError = false;

				if(intLength == 2) {
					DBDate dbdtCurrentDate = getCurrentDate();

					dbdtDate = new DBDate(p_strDate, dbdtCurrentDate.getMonthInYear_String(), dbdtCurrentDate.getYear_String(), AppClientUtilities.getLocale());

				} else if(intLength == 4) {
					String strDayNumber = p_strDate.substring(0, 2);
					String strMonthNumber = p_strDate.substring(2, 4);

					DBDate dbdtCurrentDate = getCurrentDate();

					dbdtDate = new DBDate(strDayNumber, strMonthNumber, dbdtCurrentDate.getYear_String(), AppClientUtilities.getLocale());

				} else if(intLength == 5) {
					StringTokenizer tokenizer = new StringTokenizer(p_strDate, "/");
					int intCountTokens = tokenizer.countTokens();

					if(intCountTokens != 2) {
						displayError();

						bolError = true;
					}

					DBDate dbdtCurrentDate = getCurrentDate();
					dbdtDate = new DBDate(tokenizer.nextToken(), tokenizer.nextToken(), dbdtCurrentDate.getYear_String(), AppClientUtilities.getLocale());

				} else if(intLength == 6) {
					String strDayNumber = p_strDate.substring(0, 2);
					String strMonthNumber = p_strDate.substring(2, 4);
					String strYear = p_strDate.substring(4, 6);

					dbdtDate = new DBDate(strDayNumber, strMonthNumber, strYear, AppClientUtilities.getLocale());

				} else if(intLength == 8) {
					StringTokenizer tokenizer = new StringTokenizer(p_strDate, "/");
					int intCountTokens = tokenizer.countTokens();

					if(intCountTokens != 3) {
						displayError();

						bolError = true;
					}

					DBDate dbdtCurrentDate = getCurrentDate();
					dbdtDate = new DBDate(tokenizer.nextToken(), tokenizer.nextToken(), dbdtCurrentDate.getYear_String().substring(0, 2) + tokenizer.nextToken(),
						AppClientUtilities.getLocale());

				} else if(intLength == 10) {
					StringTokenizer tokenizer = new StringTokenizer(p_strDate, "/");
					int intCountTokens = tokenizer.countTokens();

					if(intCountTokens != 3) {
						displayError();

						bolError = true;

					} else {
						dbdtDate = new DBDate(p_strDate, AppClientUtilities.getLocale());
					}
				} else {
					displayError();

					bolError = true;
				}

				if(!bolError) {
					m_DateString_Display = dbdtDate.getString(AppClientUtilities.getLocale());
					m_dbtDate = dbdtDate;

					super.setText(m_DateString_Display);
				}
			}
		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
			displayError();
		}
	}

	private DBDate getCurrentDate() throws GException {
		DBDate dbdtCurrentDate = DBDate.getCurrentDate();

		if(AppClientUtilities.getLocale() == GLocale.Thai) {
			dbdtCurrentDate = dbdtCurrentDate.getYearDiff(543);
		}

		return dbdtCurrentDate;
	}

	private void resetData() {
		setText("");

		m_DateString_Display = "";
		m_dbtDate = new DBDate();
	}

	private void displayError() throws GException {
		resetData();

		throw new GException("Invalid Date!");
	}

	private void registerActionListener() {
		me.addFocusListener(new FocusListener() {
			public void focusGained(final FocusEvent p_event) {}

			public void focusLost(final FocusEvent p_event) {
				try {
				  String strText = me.getText();
					verifyDate(strText);

				} catch (Exception exception) {
				  ExceptionHandler.display(exception);
				}
			}
		});
	}
}
