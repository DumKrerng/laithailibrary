package com.laithailibrary.sharelibrary.locale;

import java.util.*;

public enum GLocale {
	English("ENG", "English", Locale.ENGLISH),
	Thai("THA", "Thai", new Locale("th", "TH"));

	private String m_strID = "";
	private String m_strName = "";
	private Locale m_locale = null;

	private GLocale(String p_strID, String p_strName, Locale p_locale) {
		m_strID = p_strID;
		m_strName = p_strName;
		m_locale = p_locale;
	}

	public String getID() {
		return m_strID;
	}

	public String getName() {
		return m_strName;
	}

	public Locale getLocale() {
		return m_locale;
	}
}
