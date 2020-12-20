package com.laithailibrary.clientlibrary.ui.ucv;

import com.laithailibrary.clientlibrary.ui.swing.support.*;
import com.laithailibrary.sharelibrary.datatableview.*;
import com.laithailibrary.sharelibrary.datatableview.DataVectorTable;
import com.laithailibrary.sharelibrary.db.dbobject.DBObject;
import exc.*;
import exc.ExceptionHandler;
import com.laithailibrary.sharelibrary.support.GUtilities;
import com.laithailibrary.clientlibrary.ui.DataTableViewSelector;
import com.laithailibrary.clientlibrary.ui.UIDimension;
import com.laithailibrary.clientlibrary.ui.swing.GButton;
import com.laithailibrary.clientlibrary.ui.swing.GDialog;
import com.laithailibrary.clientlibrary.ui.swing.GTextField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by IntelliJ IDEA.
 * User: dumkrerng
 * Date: 6/16/11
 * Time: 10:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class UISelectorMasterData<DBO extends DBObject> extends GDialog {

//	private UISelectorMasterData me;
//	private UIControlView m_uiControlView;

	private String m_strTableHeader;

	private DataVectorTable m_datavectortable;
	private DataTableViewSelector m_dataTableViewSelector;

	private DBO m_dboPrimaryKey;
	private DBO m_dboDisplayValue;
	private DataVectorRow m_dvrSelectedRow;

	private GTextField m_txtSearch;

	private GButton m_btnSelected;
	private GButton m_btnClose;

	private Boolean isOnClickSelected = false;

	public UISelectorMasterData(Dialog p_dialog, DataVectorTable p_datavectortable, String p_strTableHeader) throws GException {
		super(p_dialog, "Selector Master Data", true);

		init(p_datavectortable, p_strTableHeader, "", UIDimension.DIM_900_650);
	}

	public UISelectorMasterData(Dialog p_dialog, DataVectorTable p_datavectortable, String p_strTableHeader, String p_strFilter) throws GException {
		super(p_dialog, "Selector Master Data", true);

		init(p_datavectortable, p_strTableHeader, p_strFilter, UIDimension.DIM_900_650);
	}

	public UISelectorMasterData(Dialog p_dialog, DataVectorTable p_datavectortable, String p_strTableHeader, Dimension p_dimension) throws GException {
		super(p_dialog, "Selector Master Data", true);

		init(p_datavectortable, p_strTableHeader, "", p_dimension);
	}

	public UISelectorMasterData(Dialog p_dialog, DataVectorTable p_datavectortable, String p_strTableHeader, String p_strFilter, Dimension p_dimension) throws GException {
		super(p_dialog, "Selector Master Data", true);

		init(p_datavectortable, p_strTableHeader, p_strFilter, p_dimension);
	}

	public void init(DataVectorTable p_datavectortable, String p_strTableHeader, String p_strFilter, Dimension p_dimension) throws GException {
		String strTitleProgram = GUtilities.getProName() + " :[" + super.getTitle() +"]";
		super.setTitle(strTitleProgram);

//		me = this;
		m_datavectortable = p_datavectortable;
		m_strTableHeader = p_strTableHeader;

		buildDialogLayout();
		registerEvenListener();

		if(p_strFilter.length() > 0) {
			m_txtSearch.setText(p_strFilter);
			m_dataTableViewSelector.searchData(p_strFilter);
		}

		setSize(p_dimension);

		setCursor(Cursor.getDefaultCursor());

		setVisible(true);
	}

	private void buildDialogLayout() throws GException {
		try {
		  JPanel paneCenter = new JPanel();
			paneCenter.setLayout(new BorderLayout());

			m_dataTableViewSelector = new DataTableViewSelector(m_datavectortable, "Selector Master Data");

			paneCenter.add(getSearchPane(), BorderLayout.NORTH);
			paneCenter.add(m_dataTableViewSelector, BorderLayout.CENTER);

			JTabbedPane tabbedpane = new JTabbedPane();
			tabbedpane.add(m_strTableHeader, paneCenter);

		  Container pane = getContentPane();
			pane.setLayout(new BorderLayout());

			pane.add(tabbedpane, BorderLayout.CENTER);
			pane.add(getPanelButton(), BorderLayout.SOUTH);

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	private JPanel getSearchPane() throws GException {
		try {
			JPanel paneSearch = new JPanel(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets = new Insets(10, 5, 10, 5);

			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.gridwidth = 1;
			gbc.anchor = GridBagConstraints.EAST;
			JLabel label = new JLabel("Search: ");
			paneSearch.add(label, gbc);

			gbc.gridx = 1;
			gbc.gridy = 0;
			gbc.gridwidth = 1;
			gbc.anchor = GridBagConstraints.WEST;
			m_txtSearch = new GTextField(20);
			paneSearch.add(m_txtSearch, gbc);

			return paneSearch;

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	private JPanel getPanelButton() throws GException {
		try {
		  JPanel jPanel = new JPanel(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets = new Insets(5, 10, 5, 10);

			gbc.gridx = 0;
			gbc.gridy = 0;
			m_btnSelected = new GButton("Selected");
			jPanel.add(m_btnSelected, gbc);

			gbc.gridx = 3;
			gbc.gridy = 0;
			m_btnClose = new GButton("Close");
			jPanel.add(m_btnClose, gbc);

			return jPanel;

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	private void onActionSelected() throws GException {
		try {
			DataVectorRow dvtRow = m_dataTableViewSelector.getDVTCurrentRow();

			if(dvtRow.size() <= 0) {
				throw new GException("Not selected data!!!");
			}

			String strReturnColumnPrimaryKey  = m_datavectortable.getReturnColumnPrimaryKeys().get(0);
			String strColumnSelectionDisplayValue  = m_datavectortable.getColumnSelectionDisplayValue();

			m_dboPrimaryKey = (DBO)dvtRow.getDataAtColumnName(strReturnColumnPrimaryKey);
			m_dboDisplayValue = (DBO)dvtRow.getDataAtColumnName(strColumnSelectionDisplayValue);
			m_dvrSelectedRow = dvtRow;

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public DBO getSelectedPrimaryKey() throws GException {
		return m_dboPrimaryKey;
	}

	public DBO getSelectedDisplayValue() throws GException {
		return m_dboDisplayValue;
	}

	public DataVectorRow getSelectedRow() throws GException {
		return m_dvrSelectedRow;
	}

	public boolean isOnClickSelected() {
		return isOnClickSelected;
	}

	private void registerEvenListener() throws GException {
		m_txtSearch.addEventKeyEnterListener(new ActionEventKeyEnterListener() {
			@Override
			public void doAction(EventKeyEnterListener p_event) {
				try {
					String strSearch = m_txtSearch.getText();

					m_dataTableViewSelector.searchData(strSearch);

				} catch(Exception exception) {
				  ExceptionHandler.display(exception);
				}
			}
		});

		m_btnSelected.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent p_event) {
				try {
					isOnClickSelected = true;

					onActionSelected();
					dispose();

				} catch (Exception exception) {
				  ExceptionHandler.display(exception);
				}
			}
		});

		m_btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent p_event) {
				isOnClickSelected = false;

				dispose();
			}
		});

		m_dataTableViewSelector.getDataTable().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(final MouseEvent p_event) {
				try {
				  if(p_event.getClickCount() == 2) {
						isOnClickSelected = true;

						onActionSelected();
						dispose();
					}
				} catch (Exception exception) {
				  ExceptionHandler.display(exception);
				}
			}
		});
	}
}
