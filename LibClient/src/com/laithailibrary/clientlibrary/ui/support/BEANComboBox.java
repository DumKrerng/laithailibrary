package com.laithailibrary.clientlibrary.ui.support;

import exc.*;

/**
 * Created by dumkrerng on 29/10/2557.
 */
public class BEANComboBox<T> {

	private String m_strLabel;
	private T m_value;

	private BEANComboBox() {}

	public BEANComboBox(String p_strLabel, T p_value) throws GException {
		try {
		  m_strLabel = p_strLabel;
			m_value = p_value;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public String getLabel() {
		return m_strLabel;
	}

	public T getValue() {
		return m_value;
	}
}
