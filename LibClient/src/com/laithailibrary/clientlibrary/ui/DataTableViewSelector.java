package com.laithailibrary.clientlibrary.ui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import com.laithailibrary.sharelibrary.datatableview.*;
import exc.*;

/**
 * Created by IntelliJ IDEA.
 * User: dumkrerng
 * Date: 6/8/11
 * Time: 10:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class DataTableViewSelector extends JPanel {

	private String m_strTableHeader;
	private GTableDisplay m_tabledisplay;

	private DataVectorTable m_datavectortable = null;

	public DataTableViewSelector(DataVectorTable p_datavectortable, String p_strTableHeader) throws GException {
		try {
			m_datavectortable = p_datavectortable;
			m_strTableHeader = p_strTableHeader;

			buildPanelLayout();

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	private void buildPanelLayout() throws GException {
		try {
			setLayout(new BorderLayout());

			if(m_datavectortable != null) {
				m_tabledisplay = new GTableDisplay(m_datavectortable, m_strTableHeader);
				add(m_tabledisplay, BorderLayout.CENTER);

			} else {
				JLabel label = new JLabel(m_strTableHeader);
				label.setHorizontalAlignment(JLabel.CENTER);
				label.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
				add(label, BorderLayout.NORTH);
			}
		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public void reloadDataView() throws GException {
		try {
			if(m_tabledisplay != null) {
				m_tabledisplay.reloadDataView();
			}

			updateUI();
			validate();

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public DataVectorRow getDVTCurrentRow() throws GException {
		return m_tabledisplay.getDVRCurrentRow();
	}

	public void searchData(String p_strSearch) throws GException {
		try {
			m_tabledisplay.searchData(p_strSearch);

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public GTable getDataTable() {
		return m_tabledisplay.getDataTableView();
	}
}
