package com.laithailibrary.clientlibrary.ui;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.*;
import com.laithailibrary.clientlibrary.ui.support.*;
import com.laithailibrary.clientlibrary.ui.swing.*;
import com.laithailibrary.clientlibrary.ui.ucv.*;
import com.laithailibrary.sharelibrary.collection.*;
import com.laithailibrary.sharelibrary.datatableview.*;
import com.laithailibrary.sharelibrary.db.dbobject.*;
import exc.*;

/**
 * Created by dumkrerng on 4/10/2557.
 */
public class GTableDisplay extends JPanel {

	private GTableDisplay me;

	private String m_strTableHeader;
	private GTable m_table;

	private DataTableColumnModel m_dataTableColumnModel = null;
	private DataVectorTable m_datavectortable = null;

	private UIDataTableView m_uiDataTableView = null;

	private GLabel m_lblTableHeader;
	private GPanel m_paneSearch;
	private GLabel m_lblTableFooter;

	private GComboBox<String> m_cmbFieldName;
	private GTextField m_txtValueSearch;

	private List<GMenuItem> m_lsMenuItems;
	protected JPopupMenu m_popupmenu;

	public GTableDisplay(DataVectorTable p_datavectortable, String p_strTableHeader) throws GException {
		try {
			m_datavectortable = p_datavectortable;
			m_dataTableColumnModel = p_datavectortable.getDataTableColumnModel().clone();
			m_strTableHeader = p_strTableHeader;

			init();

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public GTableDisplay(DataTableColumnModel p_dataTableColumnModel, String p_strTableHeader) throws GException {
		try {
			m_datavectortable = new DataVectorTable(p_dataTableColumnModel);
			m_dataTableColumnModel = p_dataTableColumnModel;
			m_strTableHeader = p_strTableHeader;

			init();

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public GTableDisplay(UIDataTableView p_uiDataTableView, String p_strTableHeader) throws GException {
		try {
			m_uiDataTableView = p_uiDataTableView;

			if(p_uiDataTableView.getTableColumnModel() != null) {
				m_datavectortable = new DataVectorTable(p_uiDataTableView.getTableColumnModel());
				m_dataTableColumnModel = p_uiDataTableView.getTableColumnModel();
			}

			m_strTableHeader = p_strTableHeader;

			init();

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	private void init() throws GException {
		try {
			me = this;

			buildPaneLayout();
			registerActionListener();

			updateDisplayRowNumberSelected();

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	private void buildPaneLayout() throws GException {
		try {
			setLayout(new BorderLayout());

			add(getHeaderPane(), BorderLayout.NORTH);

			if(m_datavectortable != null) {
				if(m_table == null) {
					m_table = new GTable(m_datavectortable);

					JScrollPane scrollPane = new JScrollPane(m_table);
					add(scrollPane, BorderLayout.CENTER);
				}
			}

			m_lblTableFooter = new GLabel("N/N Rows");
			m_lblTableFooter.setHorizontalAlignment(JLabel.CENTER);
			m_lblTableFooter.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			add(m_lblTableFooter, BorderLayout.SOUTH);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	private JPanel getHeaderPane() throws GException {
		try {
			JPanel pane = new JPanel(new BorderLayout());

			m_lblTableHeader = new GLabel(m_strTableHeader);
			m_lblTableHeader.setHorizontalAlignment(JLabel.CENTER);
			m_lblTableHeader.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			pane.add(m_lblTableHeader, BorderLayout.NORTH);

			pane.add(getSearchPane(), BorderLayout.CENTER);

			return pane;

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	private JPanel getSearchPane() throws GException {
		try {
			if(m_paneSearch == null) {
				m_paneSearch = new GPanel(new GridBagLayout());
				GridBagConstraints gbc = new GridBagConstraints();
				gbc.insets = new Insets(5, 5, 5, 5);

				gbc.gridx = 0;
				gbc.gridy = 0;
				gbc.gridwidth = 1;
				gbc.gridheight = 1;
				gbc.anchor = GridBagConstraints.EAST;
				m_cmbFieldName = new GComboBox<>(getBEANComboBoxFieldNames());
				m_paneSearch.add(m_cmbFieldName, gbc);

				gbc.gridx = 1;
				gbc.gridy = 0;
				gbc.gridwidth = 1;
				gbc.gridheight = 1;
				gbc.anchor = GridBagConstraints.WEST;
				m_txtValueSearch = new GTextField(16);
				m_txtValueSearch.setPreferredSize(new Dimension(10, 25));
				m_paneSearch.add(m_txtValueSearch, gbc);
			}

			m_paneSearch.setVisible(false);

			return m_paneSearch;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	private List<BEANComboBox<String>> getBEANComboBoxFieldNames() throws GException {
		try {
			List<TableColumn> lsTableColumns = m_dataTableColumnModel.getTableColumns();

			List<BEANComboBox<String>> lsBEANComboBoxes = new GList<>();

			for(TableColumn tablecolumn : lsTableColumns) {
				int intPreferredWidth = tablecolumn.getPreferredWidth();
				String strIdentifier = (String)tablecolumn.getIdentifier();
				String strColumnLabel = (String)tablecolumn.getHeaderValue();

				if(intPreferredWidth > 0) {
					BEANComboBox<String> beanComboBox = new BEANComboBox<>(strColumnLabel, strIdentifier);

					lsBEANComboBoxes.add(beanComboBox);
				}
			}

			return lsBEANComboBoxes;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public void reloadDataView() throws GException {
		try {
			if(m_table != null) {
				m_table.setColumnModel(m_dataTableColumnModel);
				m_table.setDataVectorTable(m_datavectortable);
			}

			updateDisplayRowNumberSelected();

			updateUI();
			validate();

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	private void buildDataTablePopupMenu(MouseEvent p_event) throws GException {
		try {
			JPopupMenu popupMenu = new JPopupMenu();

			String strMenuLabel;

			if(m_paneSearch.isVisible()) {
				strMenuLabel = "Hide Search Pane";

			} else {
				strMenuLabel = "Display Search Pane";
			}

			GMenuItem mi = new GMenuItem(strMenuLabel);
			popupMenu.add(mi);

			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						if(m_paneSearch.isVisible()) {
							m_paneSearch.setVisible(false);

						} else {
							m_paneSearch.setVisible(true);
						}
					} catch(Exception exception) {
						ExceptionHandler.display(exception);
					}
				}
			});

			popupMenu.show(p_event.getComponent(), p_event.getX(), p_event.getY());

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public void setDataVectorTable(DataVectorTable p_datavectortable) throws GException {
		try {
			if(m_dataTableColumnModel == null) {
				m_dataTableColumnModel = p_datavectortable.getDataTableColumnModel().clone();
			}

			m_datavectortable = new DataVectorTable(m_dataTableColumnModel, p_datavectortable.getColumnNamePrimaryKeys(), p_datavectortable.getColumnNameSortKeys(),
				p_datavectortable.getSorting());
			Map<String, Integer> mapColumnIndex_ColumnName = m_dataTableColumnModel.getMapColumnIndex_ColumnName();

			while(p_datavectortable.next()) {
				DataVectorRow dvrRow_Original = p_datavectortable.getDataRow();
				DataVectorRow dvrRow = new DataVectorRow(m_dataTableColumnModel);

				for(String strColumnName : mapColumnIndex_ColumnName.keySet()) {
					Integer intColumnIndex = mapColumnIndex_ColumnName.get(strColumnName);
					Class<? extends DBObject> aClass = m_dataTableColumnModel.getColumnClass(intColumnIndex);
					DBObject dbObject = dvrRow_Original.getDataAtColumnName(aClass, strColumnName);

					dvrRow.setData(strColumnName, dbObject);
				}

				m_datavectortable.addDataRow(dvrRow);
			}

			reloadDataView();

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public void addActionSelectedListener(TDActionEventSelectedRow p_eventselectedrow) throws GException {
		m_table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent p_event) {
				try {
					List<DataVectorRow> p_lsSelectedDataVectorRows = getSelectedDataVectorRows();

					p_eventselectedrow.selectedRow(p_lsSelectedDataVectorRows);

				} catch(Exception exception) {
				  ExceptionHandler.display(exception);
				}
			}
		});
	}

	public DataVectorTable getDVT() {
		return m_table.getDVT();
	}

	public PrimaryKeyValue getCurrentPrimaryKeyValue() throws GException {
		return m_table.getCurrentPrimaryKeyValue();
	}

	public DataVectorRow getDVRCurrentRow() throws GException {
		return m_table.getCurrentDataVectorRow();
	}

	public List<DataVectorRow> getSelectedDataVectorRows() throws GException {
		return m_table.getSelectedDataVectorRows();
	}

	public <T extends DBObject> List<T> getSelectedValues_ByColumnName(Class<T> p_class, String p_strColumnName) throws GException {
		return m_table.getSelectedValues_ByColumnName(p_class, p_strColumnName);
	}

	public <T extends DBObject> List<T> getSelectedPrimaryKeyValues(Class<T> p_class) throws GException {
		return m_table.getSelectedPrimaryKeyValues(p_class);
	}

	public void searchData(String p_strSearch) throws GException {
		try {
			m_table.search(p_strSearch, 1);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public GTable getDataTableView() {
		return m_table;
	}

	public void setPopupMenu(JPopupMenu p_popupmenu) {}

	public void addMenuItems(GList<GMenuItem> p_menuitems) {
		m_lsMenuItems.addAll(p_menuitems);
	}

	private void buildPopupMenu(JPopupMenu p_popupmenu) throws GException {
		try {
			setPopupMenu(p_popupmenu);

			for(GMenuItem mi : m_lsMenuItems) {
				p_popupmenu.add(mi);
			}
		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	private void showPopupMenu(MouseEvent p_event) {
		try {
			if(p_event.isPopupTrigger()) {
				m_popupmenu = new JPopupMenu();
				m_lsMenuItems = new GList<>();

				buildPopupMenu(m_popupmenu);

				m_popupmenu.show(p_event.getComponent(), p_event.getX(), p_event.getY());
			}
		} catch(Exception exception) {
			ExceptionHandler.display(exception);
		}
	}

	private void registerActionListener() {
		me.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent p_event) {
				try {
					if(p_event.getButton() == MouseEvent.BUTTON3) {
						buildDataTablePopupMenu(p_event);
					}
				} catch(Exception exception) {
				  ExceptionHandler.display(exception);
				}
			}

			@Override
			public void mouseReleased(MouseEvent p_event) {
				try {
					if(p_event.getButton() == MouseEvent.BUTTON3) {
						buildDataTablePopupMenu(p_event);
					}
				} catch(Exception exception) {
				  ExceptionHandler.display(exception);
				}
			}
		});

		m_txtValueSearch.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent p_event) {
				try {
					if(p_event.getKeyCode() == KeyEvent.VK_ENTER) {
						String strID = m_cmbFieldName.getSelectedID();
						int intColumnIndex = m_dataTableColumnModel.getColumnIndex(strID);
						String strValueSearch = m_txtValueSearch.getText();

						if(strValueSearch.length() > 0) {
							m_table.search(strValueSearch, intColumnIndex);

						} else {
							reloadDataView();

							m_table.setSelectedRow(0);
						}

						m_txtValueSearch.requestFocus();

						updateDisplayRowNumberSelected();
					}
				} catch(Exception exception) {
				  ExceptionHandler.display(exception);
				}
			}
		});

		m_table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent p_event) {
				updateDisplayRowNumberSelected();
			}
		});

		m_table.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent p_event) {
				showPopupMenu(p_event);
			}

			public void mouseReleased(MouseEvent p_event) {
				showPopupMenu(p_event);
			}
		});
	}

	private void updateDisplayRowNumberSelected() {
		try {
			int intRowNumber = m_table.getSelectedRow() + 1;
			int intRowCount = m_table.getRowCount();

			m_lblTableFooter.setText(Integer.toString(intRowNumber) + "/" + Integer.toString(intRowCount) + " Rows");

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
		}
	}
}
