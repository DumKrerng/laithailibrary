package com.laithailibrary.clientlibrary.ui.ucv;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.laithailibrary.clientlibrary.ui.*;
import com.laithailibrary.clientlibrary.ui.swing.*;
import com.laithailibrary.sharelibrary.bean.*;
import com.laithailibrary.sharelibrary.datatableview.*;
import com.laithailibrary.sharelibrary.db.dbobject.*;
import com.laithailibrary.sharelibrary.locale.*;
import com.laithailibrary.sharelibrary.sqlstatement.support.*;
import com.laithailibrary.sharelibrary.support.*;
import exc.*;

/**
 * Created by IntelliJ IDEA.
 * User: dumkrerng
 * Date: 6/11/11
 * Time: 11:41 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class UIControlEditPane<UIC extends UIControlView, DTV extends UIDataTableView<? extends DataTable, ? extends DBDataTable>, DBDT extends DBDataTable> extends GDialog {

	private UIControlEditPane me;

	private UIC m_uiControlView;
	private DTV m_dataTableView;
	private DBDT m_beanDBDataTable_Original;
	private DBDT m_beanDBDataTable_Edited;

	private Class<? extends DBDataTable> m_clsDBDataTable;

	private DBString m_dbstrCurrentPrimaryKey;

	private Container m_Container;

	private UIControlState m_uiControlState;

	private GButton m_btnSave;
	private GButton m_btnCancel;
	private GButton m_btnDelete;
	private GButton m_btnClose;

	protected UIControlEditPane(UIC p_uiControlView, DTV p_dataTableView, String p_strTitle, UIControlState p_uiControlState) throws GException {
		super(p_uiControlView, p_strTitle, true);

		init00(p_uiControlView, p_dataTableView, p_uiControlState);
	}

	private void init00(UIC p_uiControlView, DTV p_dataTableView, UIControlState p_uiControlState) throws GException {
		try {
			me = this;
			m_uiControlView = p_uiControlView;

			super.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

			m_Container = getContentPane();

			m_dataTableView = p_dataTableView;
			m_beanDBDataTable_Original = (DBDT)p_dataTableView.getDBDataTable_Class().newInstance();
			m_clsDBDataTable = p_dataTableView.getDBDataTable_Class();
			m_uiControlState = p_uiControlState;

			m_dbstrCurrentPrimaryKey = p_dataTableView.getCurrentPrimaryKey_StringValue();

			buildLayoutEditPane();
			registerButtonEvenListener();

			init();

			controlDisable();

			if(p_uiControlState == UIControlState.Insert) {
				m_beanDBDataTable_Edited = (DBDT)p_dataTableView.getDBDataTable_Class().newInstance();

				controlResetData();
				controlEnable();

				setTextFieldDefaultData();
				setDefaultData_OnInsert();

				m_btnDelete.setEnabled(false);

			} else if(p_uiControlState == UIControlState.Edit) {
				setDBDataTable();
				populateDataControlFromDataBean(m_beanDBDataTable_Edited);
				controlEnable();

				if(!m_dataTableView.allowedDelete()) {
					m_btnDelete.setEnabled(false);
				}
			} else {
				controlDisable();
			}

			registerDialogListener00();

			addWindowListener( new WindowAdapter() {
				public void windowOpened(WindowEvent p_event) {
					getDefaultFocus().requestFocus();
				}
			});
		} catch (InstantiationException exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);

		} catch (IllegalAccessException exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	protected abstract void init() throws GException;

	private void setDBDataTable() throws GException {
		try {
			DataTable datatable = m_dataTableView.getDataTable_Class().newInstance();
			datatable.setSessionID(AppClientUtilities.getSessionID());
			datatable.setDBDataTable(m_beanDBDataTable_Original.getClass());

			m_beanDBDataTable_Original = (DBDT)datatable.getDBDataTable(m_dbstrCurrentPrimaryKey);
			m_beanDBDataTable_Edited = (DBDT)datatable.getDBDataTable(m_dbstrCurrentPrimaryKey);

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	private void buildLayoutEditPane() throws GException {
		m_Container.setLayout(new BorderLayout());

		m_Container.add(getUIControlStateLabel(), BorderLayout.NORTH);
		m_Container.add(getMainEditPane(), BorderLayout.CENTER);
		m_Container.add(getCommendButtonPanel(), BorderLayout.SOUTH);
	}

	private JPanel getUIControlStateLabel() {
		JPanel jPanel = new JPanel(new BorderLayout());

		String strUIControlStateLabel = m_uiControlState.getUIControlStateLabel();
		GLabel label = new GLabel(strUIControlStateLabel);
		label.setFont(new Font("TH Sarabun New", Font.BOLD, 24));
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setBorder(BorderFactory.createRaisedBevelBorder());

		jPanel.add(label, BorderLayout.NORTH);

		return jPanel;
	}

	protected abstract JComponent getMainEditPane() throws GException;
	protected abstract JComponent getDefaultFocus();
	protected abstract void controlResetData() throws GException;
	protected abstract void controlDisable() throws GException;
	protected abstract void controlEnable() throws GException;

	public void setDefaultData_OnInsert() throws GException {}

	protected abstract void populateDataControlFromDataBean(DBDT p_dbDataTable) throws GException;
	protected abstract void populateDataBeanFromDataControl(DBDT p_dbDataTable) throws GException;

	private JComponent getCommendButtonPanel() throws GException {
		try {
		  JPanel jPanel = new JPanel(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets = new Insets(5, 10, 5, 10);

			gbc.gridx = 0;
			gbc.gridy = 0;
			m_btnSave = new GButton("Save");
			jPanel.add(m_btnSave, gbc);

			gbc.gridx = 1;
			gbc.gridy = 0;
			m_btnCancel = new GButton("Cancel");
			jPanel.add(m_btnCancel, gbc);

			gbc.gridx = 2;
			gbc.gridy = 0;
			m_btnDelete = new GButton("Delete");
			jPanel.add(m_btnDelete, gbc);

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

	public UIC getUIControlView() {
		return m_uiControlView;
	}

	public DBDT getDataTableView() throws GException {
		return m_beanDBDataTable_Original;
	}

	public Dimension getDimension() {
		return new Dimension(getWidth(), getHeight());
	}

	public UIControlState getUIControlState() {
		return m_uiControlState;
	}

	private void registerButtonEvenListener() throws GException {
		m_btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					populateDataBeanFromDataControl(m_beanDBDataTable_Edited);

					if(m_uiControlState == UIControlState.Insert) {
						me.executeProcess_WithDialogWaiting(new OnInsertData());

					} else if(m_uiControlState == UIControlState.Edit) {
						me.executeProcess_WithDialogWaiting(new OnUpdateData());

					} else {
						return;
					}
				} catch (Exception exception) {
				  ExceptionHandler.display(exception);
				}
			}
		});

		m_btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					controlResetData();

					if(m_uiControlState == UIControlState.Edit) {
						populateDataControlFromDataBean(m_beanDBDataTable_Original);
					}
				} catch (Exception exception) {
				  ExceptionHandler.display(exception);
				}
			}
		});

		m_btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					onDeleteData();

				} catch (Exception exception) {
				  ExceptionHandler.display(exception);
				}
			}
		});

		m_btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				close();
			}
		});
	}

	private void onDeleteData() throws GException {
		try {
			String strCaption = "Confirm Delete Data?";

			if(AppClientUtilities.getLocale() == GLocale.Thai) {
				strCaption = "ต้องการลบข้อมูล";
			}

			if(GConfirmDialog.confirmAction(m_uiControlView, "Confirm Action.", strCaption)) {
				OnDeleteData onDeleteData = new OnDeleteData();
				me.executeProcess_WithDialogWaiting(onDeleteData);
			}
		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	private void registerDialogListener00() {
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent p_event) {
				try {
					close();

				} catch (Exception exception) {
					ExceptionHandler.display(exception);
				}
			}
		});
	}

	private class OnInsertData implements Runnable {

		public void run() {
			try {
				DataTable datatable = m_dataTableView.getDataTable_Class().newInstance();
				datatable.setSessionID(AppClientUtilities.getSessionID());
				datatable.setDBDataTable(m_beanDBDataTable_Original.getClass());

				DBString dbstrNewID = datatable.insertData(m_beanDBDataTable_Edited, TransactionBegin.Yes);
				m_dataTableView.reloadDataView();
				m_dataTableView.setSelectedID(new PrimaryKeyValue(dbstrNewID));

				close();

			} catch (Exception exception) {
			  ExceptionHandler.display(exception);
			}
		}
	}

	private class OnUpdateData implements Runnable {

		public void run() {
			try {
				DataTable datatable = m_dataTableView.getDataTable_Class().newInstance();
				datatable.setSessionID(AppClientUtilities.getSessionID());
				datatable.setDBDataTable(m_beanDBDataTable_Original.getClass());

				DBString dbstrID = datatable.updateData(m_beanDBDataTable_Edited, TransactionBegin.Yes);
				m_dataTableView.reloadDataView();
				m_dataTableView.setSelectedID(new PrimaryKeyValue(dbstrID));

				close();

			} catch (Exception exception) {
			  ExceptionHandler.display(exception);
			}
		}
	}

	private class OnDeleteData implements Runnable {

		public void run() {
			try {
				DataTable datatable = m_dataTableView.getDataTable_Class().newInstance();
				datatable.setSessionID(AppClientUtilities.getSessionID());
				datatable.setDBDataTable(m_beanDBDataTable_Original.getClass());

				datatable.deleteData(m_beanDBDataTable_Original, TransactionBegin.Yes);
				m_dataTableView.reloadDataView();

				close();

			} catch (Exception exception) {
			  ExceptionHandler.display(exception);
			}
		}
	}
}
