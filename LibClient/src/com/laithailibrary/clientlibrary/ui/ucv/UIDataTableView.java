package com.laithailibrary.clientlibrary.ui.ucv;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.*;
import com.laithailibrary.clientlibrary.ui.*;
import com.laithailibrary.clientlibrary.ui.image.*;
import com.laithailibrary.clientlibrary.ui.support.*;
import com.laithailibrary.clientlibrary.ui.swing.*;
import com.laithailibrary.sharelibrary.bean.*;
import com.laithailibrary.sharelibrary.collection.*;
import com.laithailibrary.sharelibrary.datatableview.*;
import com.laithailibrary.sharelibrary.db.dbobject.*;
import com.laithailibrary.sharelibrary.locale.*;
import com.laithailibrary.sharelibrary.sqlstatement.support.*;
import com.laithailibrary.sharelibrary.support.*;
import com.laithailibrary.sharelibrary.thread.*;
import exc.*;

/**
 * Created by IntelliJ IDEA.
 * User: dumkrerng
 * Date: 6/8/11
 * Time: 10:04 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class UIDataTableView<DT extends DataTable, DBDT extends DBDataTable> extends JPanel implements I_UIDataView {

	private UIDataTableView me;

	private List<I_UIDataView> m_lsDataTableChildView;

	private String m_strTableHeader;
	private GTableDisplay m_tabledisplay = null;

	private Class<DT> m_clsDataTable;
	private DataTable<DBDT> m_objDataTable;
	private UIControlView m_uiControlView;
	private Class<DBDT> m_clsDBDataTable;
	private DataTableColumnModel m_dataTableColumnModel;
	private DataVectorTable m_datavectortable;

	private WithButtonControl m_withbuttoncontrol = WithButtonControl.No;

	protected JPopupMenu m_popupmenu;

	protected UIDataTableView(Class<DT> p_clsDataTable, UIControlView p_uiControlView, Class<DBDT> p_clsDBDataTable, String p_strTableHeader) throws GException {

		init00(p_clsDataTable, p_uiControlView, p_clsDBDataTable, p_strTableHeader, WithButtonControl.No);
	}

	protected UIDataTableView(Class<DT> p_clsDataTable, UIControlView p_uiControlView, Class<DBDT> p_clsDBDataTable, String p_strTableHeader,
		WithButtonControl p_withbuttoncontrol) throws GException {

		init00(p_clsDataTable, p_uiControlView, p_clsDBDataTable, p_strTableHeader, p_withbuttoncontrol);
	}

	protected UIDataTableView(UIControlView p_uiControlView, String p_strTableHeader) throws GException {
		init00(null, p_uiControlView, null, p_strTableHeader, WithButtonControl.No);
	}

	private void init00(Class<DT> p_clsDataTable, UIControlView p_uiControlView, Class<DBDT> p_clsDBDataTable, String p_strTableHeader,
		WithButtonControl p_withbuttoncontrol) throws GException {

		try {
			me = this;
			m_lsDataTableChildView = new GList<>();

			m_strTableHeader = p_strTableHeader;
			m_withbuttoncontrol = p_withbuttoncontrol;

			m_uiControlView = p_uiControlView;

			if(p_clsDataTable != null) {
				m_clsDataTable = p_clsDataTable;
				m_clsDBDataTable = p_clsDBDataTable;

				m_objDataTable = (DT)m_clsDataTable.newInstance();
				m_objDataTable.setDBDataTable(m_clsDBDataTable);
				m_objDataTable.setSessionID(AppClientUtilities.getSessionData().getSessionID());

				m_clsDBDataTable.newInstance();
			}

			init();

			m_dataTableColumnModel = getTableColumnModel();

			buildPanelLayout();
			registerActionListener();

			updateUI();
			setVisible(true);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public abstract void init() throws GException;
	public abstract DataTableColumnModel getTableColumnModel() throws GException;
	public abstract List<String> getPrimaryKeys() throws GException;
	public abstract List<String> getSortKeys() throws GException;
	public abstract DataVectorTable getTableView() throws GException;

	protected Sorting getSorting() {
		return null;
	}

	public<T extends I_UIDataView> void registerChildView(T p_t) {
		m_lsDataTableChildView.add(p_t);
	}

	private void buildPanelLayout() throws GException {
		try {
			setLayout(new BorderLayout());

			add(getButtonControlPane(), BorderLayout.NORTH);

			if(m_datavectortable != null) {
				m_tabledisplay = new GTableDisplay(m_datavectortable, m_strTableHeader);
				add(m_tabledisplay, BorderLayout.CENTER);

			} else if(m_dataTableColumnModel != null) {
				m_tabledisplay = new GTableDisplay(m_dataTableColumnModel, m_strTableHeader);
				add(m_tabledisplay, BorderLayout.CENTER);
			}
		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	private JPanel getButtonControlPane() throws GException {
		try {
			JPanel pane = new JPanel();
			pane.setLayout(new GridLayout(1, 1, 1, 1));

			JPanel paneButton = new JPanel(new BorderLayout());
			paneButton.add(getActionPane(), BorderLayout.LINE_START);

			pane.add(paneButton);

			return pane;

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	private JPanel getActionPane() throws GException {
		try {
			JPanel pane = new JPanel(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets = new Insets(2, 2, 2, 2);

			if(m_withbuttoncontrol == WithButtonControl.Yes) {
				if(allowedInsert()) {
					gbc.gridx = 0;
					gbc.gridy = 0;
					JLabel label = new JLabel(GImageIcon.getIconNew());
					label.setToolTipText("New");
					pane.add(label, gbc);

					label.addMouseListener(new GMouseAdapter() {
						public void mouseClicked(final MouseEvent p_event) {
							try {
								if(p_event.getButton() == MouseEvent.BUTTON1) {
									m_uiControlView.executeProcess_WithDialogWaiting("Open Edit Pane", new OnOpenEditPane(UIControlState.Insert));
								}
							} catch (Exception exception) {
								ExceptionHandler.display(exception);
							}
						}
					});
				}

				if(allowedEdit()) {
					gbc.gridx = 1;
					gbc.gridy = 0;
					JLabel label = new JLabel(GImageIcon.getIconEdit());
					label.setToolTipText("Edit");
					pane.add(label, gbc);

					label.addMouseListener(new GMouseAdapter() {
						public void mouseClicked(final MouseEvent p_event) {
							try {
								if(p_event.getButton() == MouseEvent.BUTTON1) {
									m_uiControlView.executeProcess_WithDialogWaiting("Open Edit Pane", new OnOpenEditPane(UIControlState.Edit));
								}
							} catch(Exception exception) {
								ExceptionHandler.display(exception);
							}
						}
					});
				}

				if(allowedDelete()) {
					gbc.gridx = 2;
					gbc.gridy = 0;
					JLabel label = new JLabel(GImageIcon.getIconDelete());
					label.setToolTipText("Delete");
					pane.add(label, gbc);

					label.addMouseListener(new GMouseAdapter() {
						public void mouseClicked(final MouseEvent p_event) {
							try {
								if(p_event.getButton() == MouseEvent.BUTTON1) {
									DBString dbstrCurrentPrimaryKey = m_tabledisplay.getDataTableView().getCurrentPrimaryKey_StringValue();

									if(dbstrCurrentPrimaryKey.isInvalid()) {
										JOptionPane.showMessageDialog(m_uiControlView, "Not selected row data.", "Error!!!", JOptionPane.ERROR_MESSAGE);

									} else {
										String strCaption = "Confirm Delete Data?";

										if(AppClientUtilities.getLocale() == GLocale.Thai) {
											strCaption = "ต้องการลบข้อมูล";
										}

										if(GConfirmDialog.confirmAction(m_uiControlView, "Confirm Action.", strCaption)) {
											onDeleteData();
										}
									}
								}
							} catch(Exception exception) {
								ExceptionHandler.display(exception);
							}
						}
					});
				}
			}

			gbc.gridx = 3;
			gbc.gridy = 0;
			JLabel label = new JLabel(GImageIcon.getIconReload());
			label.setToolTipText("Reload Data");
			pane.add(label, gbc);

			label.addMouseListener(new GMouseAdapter() {
				public void mouseClicked(final MouseEvent p_event) {
					try {
						if(p_event.getButton() == MouseEvent.BUTTON1) {
							m_uiControlView.executeProcess_WithDialogWaiting(new OnReloadData());
						}
					} catch(Exception exception) {
						ExceptionHandler.display(exception);
					}
				}
			});

			return pane;

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public void reloadDataView_NotReloadChildImageView() throws GException {
		reloadDataView();
	}

	public void reloadDataView() throws GException {
		try {
			PrimaryKeyValue primarykeyvalue_OLD = getCurrentPrimaryKeyValue();

			DataVectorTable dvtOriginal = getTableView();

			Sorting sorting = getSorting();

			if(sorting == null) {
				sorting = dvtOriginal.getSorting();
			}

			m_datavectortable = new DataVectorTable(m_dataTableColumnModel, getPrimaryKeys(), getSortKeys(), sorting);
			Map<String, Integer> mapColumnIndex_ColumnName = m_dataTableColumnModel.getMapColumnIndex_ColumnName();

			while(dvtOriginal.next()) {
				DataVectorRow dvrRow_Original = dvtOriginal.getDataRow();
				DataVectorRow dvrRow = new DataVectorRow(m_dataTableColumnModel);

				for(String strColumnName : mapColumnIndex_ColumnName.keySet()) {
					Integer intColumnIndex = mapColumnIndex_ColumnName.get(strColumnName);
					Class<? extends DBObject> aClass = m_dataTableColumnModel.getColumnClass(intColumnIndex);
					DBObject dbObject = dvrRow_Original.getDataAtColumnName(aClass, strColumnName);

					dvrRow.setData(strColumnName, dbObject);
				}

				m_datavectortable.addDataRow(dvrRow);
			}

			if(m_tabledisplay != null) {
				m_tabledisplay.setDataVectorTable(m_datavectortable);
			}

			if(primarykeyvalue_OLD.getStringValue().length() > 0) {
				setSelectedID(primarykeyvalue_OLD);

			} else {
				setSelectedRowNumber(0);
			}

			int intSelectedRow = m_tabledisplay.getDataTableView().getSelectedRow();

			if(intSelectedRow == -1) {
				clearChildDataView();
			}

			updateUI();
			validate();

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public void clearDataView() throws GException {
		try {
			m_datavectortable = new DataVectorTable(m_dataTableColumnModel, getPrimaryKeys(), getSortKeys());

			if(m_tabledisplay != null) {
				m_tabledisplay.setDataVectorTable(m_datavectortable);
			}

			if(m_lsDataTableChildView.size() > 0) {
				for(I_UIDataView i_uidataview : m_lsDataTableChildView) {
					i_uidataview.clearDataView();
				}
			}

			updateUI();
			validate();

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public void clearChildDataView() throws GException {
		try {
			if(m_lsDataTableChildView.size() > 0) {
				for(I_UIDataView i_uidataview : m_lsDataTableChildView) {
					i_uidataview.clearDataView();
				}
			}
		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public void setSelectedID(PrimaryKeyValue p_primarykeyvalue) throws GException {
		if(m_tabledisplay != null) {
			m_tabledisplay.getDataTableView().setSelectedRow_AllowNotExisting(p_primarykeyvalue);
		}
	}

	public void setSelectedRowNumber(int p_intRowNumber) throws GException {
		if(m_tabledisplay != null) {
			m_tabledisplay.getDataTableView().setSelectedRow(p_intRowNumber);
		}
	}

	public DBObject getCurrentCellData(String p_strColumnName) throws GException {
		try {
			DataVectorRow dvrRow = m_tabledisplay.getDataTableView().getCurrentDataVectorRow();

			return dvrRow.getDataAtColumnName(p_strColumnName);

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	private void buildPopupMenu() throws GException {
		try {
			if(m_tabledisplay != null) {
				if(m_clsDataTable != null) {
					if(allowedInsert()) {
						GMenuItem mi = new GMenuItem("Insert");
						addMenuItem(mi);

						mi.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								try {
									m_uiControlView.executeProcess_WithDialogWaiting("Open Edit Pane", new OnOpenEditPane(UIControlState.Insert));

								} catch(Exception exception) {
									ExceptionHandler.display(exception);
								}
							}
						});
					}

					if(allowedEdit()) {
						GMenuItem mi = new GMenuItem("Edit");
						addMenuItem(mi);

						mi.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								try {
									DBString dbstrCurrentPrimaryKey = m_tabledisplay.getDataTableView().getCurrentPrimaryKey_StringValue();

									if(dbstrCurrentPrimaryKey.isInvalid()) {
										JOptionPane.showMessageDialog(m_uiControlView, "Not selected row data.", "Error!!!", JOptionPane.ERROR_MESSAGE);

									} else {
										m_uiControlView.executeProcess_WithDialogWaiting("Open Edit Pane", new OnOpenEditPane(UIControlState.Edit));
									}
								} catch(Exception exception) {
									ExceptionHandler.display(exception);
								}
							}
						});
					}

					if(allowedDelete()) {
						GMenuItem mi = new GMenuItem("Delete");
						addMenuItem(mi);

						mi.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								try {
									DBString dbstrCurrentPrimaryKey = m_tabledisplay.getDataTableView().getCurrentPrimaryKey_StringValue();

									if(dbstrCurrentPrimaryKey.isInvalid()) {
										JOptionPane.showMessageDialog(m_uiControlView, "Not selected row data.", "Error!!!", JOptionPane.ERROR_MESSAGE);

									} else {
										String strCaption = "Confirm Delete Data?";

										if(AppClientUtilities.getLocale() == GLocale.Thai) {
											strCaption = "ต้องการลบข้อมูล";
										}

										if(GConfirmDialog.confirmAction(m_uiControlView, "Confirm Action.", strCaption)) {
											onDeleteData();
										}
									}
								} catch(Exception exception) {
									ExceptionHandler.display(exception);
								}
							}
						});
					}

					addSeparatorMenuItem();
				}

				setPopupMenu();
			}
		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public void addMenuItem(GMenuItem p_menuitem) {
		m_popupmenu.add(p_menuitem);
	}

	public void addSeparatorMenuItem() {
		m_popupmenu.addSeparator();
	}

	protected boolean allowedInsert() {
		return true;
	}

	protected boolean allowedEdit() {
		return true;
	}

	protected boolean allowedDelete() {
		return false;
	}

	public abstract void setPopupMenu();

	public abstract void openEditPane(UIControlState p_uicontrolstate) throws GException;

//	private void openEditPane_InsetData() throws GException {}
//	private void openEditPane_EditData() throws GException {}

	public DBString getCurrentPrimaryKey_StringValue() throws GException {
		if(m_tabledisplay == null) {
			return new DBString();
		}

		return m_tabledisplay.getDataTableView().getCurrentPrimaryKey_StringValue();
	}

	public PrimaryKeyValue getCurrentPrimaryKeyValue() throws GException {
		if(m_tabledisplay == null) {
			return new PrimaryKeyValue();
		}

		return m_tabledisplay.getDataTableView().getCurrentPrimaryKeyValue();
	}

	public List<DBString> getSelectedPrimaryKeys_StringValue() throws GException {
		if(m_tabledisplay == null) {
			return new GList<>();
		}

		return m_tabledisplay.getDataTableView().getSelectedValues_ByColumnName(DBString.class, getPrimaryKeys().get(0));
	}

	public void addActionSelectedListener(TDActionEventSelectedRow p_eventselectedrow) throws GException {
		m_tabledisplay.addActionSelectedListener(p_eventselectedrow);
	}

	private void onDeleteData() throws GException {
		try {
			if(allowedDelete()) {
				DBDT dbDataTable = m_objDataTable.getDBDataTable(m_tabledisplay.getDataTableView().getCurrentPrimaryKey_StringValue());
				m_objDataTable.deleteData(dbDataTable, TransactionBegin.Yes);

				me.reloadDataView();

				setSelectedRowNumber(0);

			} else {
				throw new GException("Not allowed delete data!!!");
			}
		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public Class<DT> getDataTable_Class() {
		return m_clsDataTable;
	}

	public Class<DBDT> getDBDataTable_Class() {
		return m_clsDBDataTable;
	}

	public UIControlView getUIControlView() {
		return m_uiControlView;
	}

	public void reloadView() throws GException {
		me.reloadDataView();
	}

	public void reloadView_NotReloadChildImageView() throws GException {
		me.reloadDataView();
	}

	public void reloadDataChildView() throws GException {
		m_uiControlView.executeProcess_WithDialogWaiting(new OnReloadDataChildView(true));
	}

	public void reloadDataChildView_NotReloadImageView() throws GException {
		m_uiControlView.executeProcess_WithDialogWaiting(new OnReloadDataChildView(false));
	}

	private void registerActionListener() {
		m_tabledisplay.getDataTableView().addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent p_event) {
				showPopupMenu(p_event);
			}

			public void mouseReleased(MouseEvent p_event) {
				showPopupMenu(p_event);
			}
		});

		m_tabledisplay.getDataTableView().getTableHeader().addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent p_event) {
				showPopupMenu(p_event);
			}

			@Override
			public void mouseReleased(MouseEvent p_event) {
				showPopupMenu(p_event);
			}
		});

		m_tabledisplay.getDataTableView().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(final ListSelectionEvent p_event) {
				try {
					boolean bolReloadImageView = true;

					if(GThreadManager.hasStackTrace("com.laithailibrary.clientlibrary.ui.ucv.UIDataTableView", "reloadView_NotReloadChildImageView")) {
						bolReloadImageView = false;
					}

					if(GThreadManager.hasStackTrace("com.laithailibrary.clientlibrary.ui.ucv.UIDataTableView", "reloadDataView_NotReloadChildImageView")) {
						bolReloadImageView = false;
					}

					StackTraceElement[] traceelements = Thread.currentThread().getStackTrace();

					for(StackTraceElement trace : traceelements) {
						String strClassName = trace.getClassName();
						String strMethodName = trace.getMethodName();

						if(strClassName.compareTo("javax.swing.DefaultListSelectionModel") == 0
							&& strMethodName.compareTo("setSelectionInterval") == 0) {

							if(bolReloadImageView) {
								reloadDataChildView();

							} else {
								reloadDataChildView_NotReloadImageView();
							}

							break;
						}
					}
				} catch(Exception exception) {
					ExceptionHandler.display(exception);
				}
			}
		});
	}

	private void showPopupMenu(MouseEvent p_event) {
		try {
			if(p_event.isPopupTrigger()) {
				m_popupmenu = new JPopupMenu();

				buildPopupMenu();

				m_popupmenu.show(p_event.getComponent(), p_event.getX(), p_event.getY());
			}
		} catch(Exception exception) {
			ExceptionHandler.display(exception);
		}
	}

	private class GMouseAdapter extends MouseAdapter {
		public void mouseEntered(MouseEvent p_event) {
			try {
				setMouseCursor_Entered();

			} catch (Exception exception) {
				ExceptionHandler.display(exception);
			}
		}

		public void mouseExited(MouseEvent p_event) {
			try {
				setMouseCursor_Exited();

			} catch (Exception exception) {
				ExceptionHandler.display(exception);
			}
		}

		private void setMouseCursor_Entered() throws GException {
			Cursor normalCursor = new Cursor(Cursor.HAND_CURSOR);
			setCursor(normalCursor);
		}

		private void setMouseCursor_Exited() throws GException {
			Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
			setCursor(normalCursor);
		}
	}

	private class OnReloadData implements Runnable {

		public void run() {
			try {
			  reloadDataView();

			} catch(Exception exception) {
			  ExceptionHandler.display(exception);
			}
		}
	}

	private class OnReloadDataChildView implements Runnable {

		private boolean i_bolReloadImageView = true;

		private OnReloadDataChildView(boolean p_bolReloadImageView) {
			i_bolReloadImageView = p_bolReloadImageView;
		}

		public void run() {
			try {
				if(!m_lsDataTableChildView.isEmpty()) {
					int intSelectedRow = m_tabledisplay.getDataTableView().getSelectedRow();

					if(intSelectedRow > -1) {
						for(I_UIDataView i_uidataview : m_lsDataTableChildView) {
							if(i_bolReloadImageView) {
								i_uidataview.reloadDataView();

							} else {
								if(!(i_uidataview instanceof ControlImageViewerPane)) {
									i_uidataview.reloadDataView();
								}
							}
						}
					} else {
						for(I_UIDataView i_uidataview : m_lsDataTableChildView) {
							if(i_bolReloadImageView) {
								i_uidataview.clearDataView();

							} else {
								if(!(i_uidataview instanceof ControlImageViewerPane)) {
									i_uidataview.clearDataView();
								}
							}
						}
					}
				}
			} catch(Exception exception) {
			  ExceptionHandler.display(exception);
			}
		}
	}

	private class OnOpenEditPane implements Runnable {

		private UIControlState i_state;

		private OnOpenEditPane(UIControlState p_state) {
			i_state = p_state;
		}

		public void run() {
			try {
				me.openEditPane(i_state);

				GDialogWaiting.dispose_Main();

			} catch(Exception exception) {
			  ExceptionHandler.display(exception);
			}
		}
	}

	public enum WithButtonControl {
		Yes,
		No
	}
}
