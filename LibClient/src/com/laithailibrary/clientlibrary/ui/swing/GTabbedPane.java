package com.laithailibrary.clientlibrary.ui.swing;

import javax.swing.*;
import java.util.*;
import com.laithailibrary.sharelibrary.collection.*;
import exc.*;

public class GTabbedPane extends JTabbedPane {

	private String m_strName = "";

	private Map<String, JComponent> m_mapComponent_ID = new GMap<>();
	private Map<String, Integer> m_mapIndex_ID = new GMap<>();
	private Map<String, String> m_mapCaption_ID = new GMap<>();
	private Map<String, String> m_mapID_Caption = new GMap<>();

	public GTabbedPane(String p_strName) {
		m_strName = p_strName;
	}

	public void add(String p_strCaption, JComponent p_component) throws GException {
		add(Integer.toString(m_mapComponent_ID.size()), p_strCaption, p_component);
	}

	public void add(String p_strID, String p_strCaption, JComponent p_component) throws GException {
		try {
		  if(m_mapID_Caption.containsKey(p_strCaption)) {
		  	throw new GException("Duplicate Caption!!!");
		  }

			super.add(p_strCaption, p_component);

			m_mapComponent_ID.put(p_strID, p_component);
			m_mapIndex_ID.put(p_strID, m_mapIndex_ID.size());
			m_mapCaption_ID.put(p_strID, p_strCaption);

			m_mapID_Caption.put(p_strCaption, p_strID);

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public void setSelected(String p_strID) throws GException {
		try {
			if(!m_mapComponent_ID.containsKey(p_strID)) {
				throw new GException("Invalid Caption!!!");
			}

			JComponent component = m_mapComponent_ID.get(p_strID);
			setSelectedComponent(component);

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public void setSelected_ByCaption(String p_strCaption) throws GException {
		try {
			if(!m_mapID_Caption.containsKey(p_strCaption)) {
				throw new GException("Invalid Caption!!!");
			}

			String strID = m_mapID_Caption.get(p_strCaption);

			setSelected(strID);

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public void setEnableAt(String p_strID, boolean p_bolEnable) throws GException {
		try {
			if(!m_mapComponent_ID.containsKey(p_strID)) {
				throw new GException("Invalid Caption!!!");
			}

			Integer intIndex = m_mapIndex_ID.get(p_strID);
			setEnabledAt(intIndex, p_bolEnable);

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public void setEnableAt_ByCaption(String p_strCaption, boolean p_bolEnable) throws GException {
		try {
			if(!m_mapID_Caption.containsKey(p_strCaption)) {
				throw new GException("Invalid Caption!!!");
			}

			String strID = m_mapID_Caption.get(p_strCaption);

			setEnableAt(strID, p_bolEnable);

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public void setEnable_All(boolean p_bolEnable) throws GException {
		try {
			for(Map.Entry<String, Integer> entIndex : m_mapIndex_ID.entrySet()) {
				Integer intIndex = entIndex.getValue();
				setEnabledAt(intIndex, p_bolEnable);
			}
		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}
}
