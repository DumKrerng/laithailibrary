package com.laithailibrary.clientlibrary.ui.swing;

import com.laithailibrary.clientlibrary.ui.support.*;
import com.laithailibrary.sharelibrary.db.dbobject.*;
import com.laithailibrary.sharelibrary.field.*;
import com.sun.java.swing.plaf.motif.*;
import exc.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by dumkrerng on 3/10/2560.
 */
public class GTextPassword extends JPasswordField {

	private GTextPassword me;
	private DataField m_datafield = new DataField();

	private String m_strFontName = GFontUtilities.FONT_NAME_NORMAL;
	private int m_intFontSize = GFontUtilities.FONT_SIZE_NORMAL;
	private GFontStyle m_fontstyle = GFontStyle.Bold;

	public GTextPassword() {
		init00();
	}

	public GTextPassword(DataField p_datafield) {
		m_datafield = p_datafield;

		init00();
	}

	public GTextPassword(DataField p_datafield, int p_int) {
		super(p_int);

		m_datafield = p_datafield;

		init00();
	}

	public GTextPassword(int p_int) {
		super(p_int);

		init00();
	}

	public GTextPassword(String p_strSting) {
		super(p_strSting);

		init00();
	}

	public GTextPassword(DataField p_datafield, String p_strSting, int p_int) {
		super(p_strSting, p_int);

		m_datafield = p_datafield;

		init00();
	}

	public GTextPassword(String p_strSting, int p_int) {
		super(p_strSting, p_int);

		init00();
	}

	public GTextPassword(DBObject p_dbObject) {
		super(p_dbObject.getString());

		init00();
	}

	public GTextPassword(DBObject p_dbObject, int p_int) {
		super(p_dbObject.getString(), p_int);

		init00();
	}

	public void init00() {
		try {
			me = this;

			setFont();
			setCaret(MotifTextUI.createCaret());

			setDataFieldMandatory();

			registerActionListener();

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
		}
	}

	public void setFontSize(int p_intFontSize) {
		m_intFontSize = p_intFontSize;

		setFont();
	}

	public void setFontStyle(GFontStyle p_fontstyle) {
		m_fontstyle = p_fontstyle;

		setFont();
	}

	private void setFont() {
		if(m_fontstyle == GFontStyle.Normal) {
			setFont(new Font(m_strFontName, Font.PLAIN, m_intFontSize));

		} else if(m_fontstyle == GFontStyle.Bold) {
			setFont(new Font(m_strFontName, Font.BOLD, m_intFontSize));

		} else if(m_fontstyle == GFontStyle.Italic) {
			setFont(new Font(m_strFontName, Font.ITALIC, m_intFontSize));

		} else if(m_fontstyle == GFontStyle.Bold_Italic) {
			setFont(new Font(m_strFontName, Font.BOLD | Font.ITALIC, m_intFontSize));

		} else {
			setFont(new Font(m_strFontName, Font.BOLD, m_intFontSize));
		}
	}

	private void setDataFieldMandatory() throws GException {
		try {
			setBackground(new Color(255, 107, 112));

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public void setText(DBObject p_dbObject) {
		super.setText(p_dbObject.getString());
	}

	public String getText() {
		String strText = super.getText();

		return strText.trim();
	}

	public DBString getDBString() throws GException {
		try {
			String strText = getText();

			return new DBString(strText.trim());

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	private void registerActionListener() throws GException {
		this.addFocusListener(new FocusAdapter() {
			public void focusGained(final FocusEvent p_event) {
				me.selectAll();
			}
		});

		this.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				transferFocus();
			}
		});
	}
}
