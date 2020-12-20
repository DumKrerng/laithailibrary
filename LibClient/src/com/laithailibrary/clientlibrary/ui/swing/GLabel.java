package com.laithailibrary.clientlibrary.ui.swing;

import javax.swing.*;
import java.awt.*;
import com.laithailibrary.clientlibrary.ui.support.*;
import com.laithailibrary.sharelibrary.db.dbobject.*;

/**
 * Created with IntelliJ IDEA.
 * User: dumkrerng
 * Date: 5/13/12
 * Time: 9:59 AM
 * To change this template use File | Settings | File Templates.
 */
public class GLabel extends JLabel {

	private String m_strFontName = GFontUtilities.FONT_NAME_NORMAL;
	private int m_intFontSize = GFontUtilities.FONT_SIZE_NORMAL;
	private GFontStyle m_fontstyle = GFontStyle.Bold;

	public GLabel() {
		init();
	}

	public GLabel(String p_string) {
		super(p_string);

		init();
	}

	public GLabel(String p_string, int p_alignment) {
		super(p_string, p_alignment);

		init();
	}

	public GLabel(DBString p_dbstring) {
		super(p_dbstring.getString());

		init();
	}

	public GLabel(DBString p_dbstring, int p_alignment) {
		super(p_dbstring.getString(), p_alignment);

		init();
	}

	private void init() {
		setFont();
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
}
