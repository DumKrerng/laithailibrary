package com.laithailibrary.clientlibrary.ui.swing;

import com.laithailibrary.clientlibrary.ui.support.*;
import com.laithailibrary.sharelibrary.field.DataField;
import com.sun.java.swing.plaf.motif.*;
import exc.ExceptionHandler;
import exc.GException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 * Created with IntelliJ IDEA.
 * User: dumkrerng
 * Date: 6/7/12
 * Time: 10:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class GTextArea extends JTextArea {

	private String m_strFontName = GFontUtilities.FONT_NAME_NORMAL;
	private int m_intFontSize = 18;
	private GFontStyle m_fontstyle = GFontStyle.Normal;

	private GTextArea me;

	public GTextArea() {
		super();

		init();
	}

	public GTextArea(DataField p_datafield, int p_intRow, int p_intColumn) {
		super("", p_intRow, p_intColumn);

		init();
	}

	public GTextArea(String p_strText, int p_intRow, int p_intColumn) {
		super(p_strText, p_intRow, p_intColumn);

		init();
	}

	private void init() {
		try {
			me = this;

			setFont();
			setCaret(MotifTextUI.createCaret());

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

	private void registerActionListener() throws GException {
		this.addFocusListener(new FocusAdapter() {
			public void focusGained(final FocusEvent p_event) {
				me.selectAll();
			}
		});
	}
}
