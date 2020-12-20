package com.laithailibrary.clientlibrary.ui.swing;

import javax.swing.*;
import java.util.*;
import com.laithailibrary.clientlibrary.ui.support.*;
import com.laithailibrary.sharelibrary.collection.*;
import exc.*;

/**
 * Created by dumkrerng on 29/10/2557.
 */
public class GComboBox<T> extends JComboBox {

	private GComboBox me;

	private Map<String, T> m_mapID_Label;

	protected GComboBox() {
		init();
	}

	public GComboBox(List<BEANComboBox<T>> p_lsBEANComboBox) throws GException {
		try {
			init();

			setLSBEANComboBoxs(p_lsBEANComboBox);

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	private void init() {
		me = this;

//		setFont(new Font("TH Sarabun New", Font.BOLD, 20));
	}

	public void setLSBEANComboBoxs(List<BEANComboBox<T>> p_lsBEANComboBox) throws GException {
		try {
			removeAllItems();

			m_mapID_Label = new GMap<>();

			for(BEANComboBox<T> beanComboBox : p_lsBEANComboBox) {
				m_mapID_Label.put(beanComboBox.getLabel(), beanComboBox.getValue());

				addItem(beanComboBox.getLabel());
			}
		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public T getSelectedID() throws GException {
		try {
		  String strSelectedItem = (String)getSelectedItem();

			if(m_mapID_Label.containsKey(strSelectedItem)) {
				return m_mapID_Label.get(strSelectedItem);
			}

			throw new GException("Invalid ID.");

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}
}
