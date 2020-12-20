package com.laithailibrary.clientlibrary.ui.dataloading;

import javax.swing.*;
import javax.swing.filechooser.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;
import com.laithailibrary.clientlibrary.ui.*;
import com.laithailibrary.clientlibrary.ui.support.*;
import com.laithailibrary.clientlibrary.ui.swing.*;
import com.laithailibrary.sharelibrary.bean.*;
import com.laithailibrary.sharelibrary.collection.*;
import com.laithailibrary.sharelibrary.datatableview.*;
import com.laithailibrary.sharelibrary.db.dbobject.*;
import com.laithailibrary.sharelibrary.field.*;
import com.laithailibrary.sharelibrary.file.*;
import com.laithailibrary.sharelibrary.sqlstatement.support.*;
import com.laithailibrary.sharelibrary.support.*;
import exc.*;
import msg.*;

/**
 * Created by dumkrerng on 27/2/2559.
 */
public class UIDLMasterDataLoader extends GDialog {

	private UIDLMasterDataLoader me;

	private List<? extends DBDataTable> m_lsDBDataTables = new GList<>();
	private Map<DBString, DBDataTable> m_mapDBDataTable_TableID = new GMap<>();

	private List<String> m_lsFieldNames = new GList<>();
	private Map<String, ColumnDataLoad> m_mapColumnModelDataLoad_FieldName = new GMap<>();

	private GComboBox<DBString> m_cmbDBDataTable;
	private Map<String, DataField<? extends DBObject>> m_mapDataField_FieldName = new GMap<>();

	private GComboBox<String> m_cmbFieldName;
	private GTextField m_txtCSVColumnNumber;
	private GTextField m_txtDefaultData;
	private GButton m_btnAdd;

	private GButton m_btnLoadColumnModelFile;
	private GButton m_btnSaveColumnModelFile;

	private GLabel m_lblTableName;
	private GTextField m_txtFileName;
	private GButton m_btnChooseFile;
	private GButton m_btnRefresh;
	private GButton m_btnLoadData;

	private GTableDisplay m_tbdpnColumnModelSetting;

	private JPanel m_paneDVTDataView = new JPanel(new BorderLayout());
	private GTableDisplay m_dvtDataView;

	public UIDLMasterDataLoader(GFrame p_frame, List<? extends DBDataTable> p_lsDBDataTables) throws GException {
		super(p_frame, "Data Loader", true);

		init(p_lsDBDataTables);
	}

	public UIDLMasterDataLoader(GDialog p_dialog, List<? extends DBDataTable> p_lsDBDataTables) throws GException {
		super(p_dialog, "Data Loader", true);

		init(p_lsDBDataTables);
	}

	private void init(List<? extends DBDataTable> p_lsDBDataTables) throws GException {
		try {
			me = this;
			m_lsDBDataTables = p_lsDBDataTables;

			for(DBDataTable dbdatatable : p_lsDBDataTables) {
				m_mapDBDataTable_TableID.put(new DBString(dbdatatable.getTableID().getID()), dbdatatable);
			}

			buildDialogLayout();
			registerActionListener();

			setListSelectorFieldName();
			setTableLabel();

			setSize(UIDimension.DIM_800_600);
			setVisible(true);

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	private void buildDialogLayout() throws GException {
		try {
			Container pane = getContentPane();
			pane.setLayout(new BorderLayout());

			pane.add(getTabbedPane(), BorderLayout.CENTER);

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	private JTabbedPane getTabbedPane() throws GException {
		try {
			JTabbedPane tabbedpane = new JTabbedPane();

			tabbedpane.addTab("Setting", getMetaTablePane());
			tabbedpane.addTab("Data Loading", getDataLoadingPane());

			return tabbedpane;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	private JPanel getMetaTablePane() throws GException {
		try {
			JPanel pane = new JPanel(new BorderLayout());

			pane.add(getSettingPane(), BorderLayout.NORTH);
			pane.add(getColumnModelSettingViewPane(), BorderLayout.CENTER);

			return pane;

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	private JPanel getSettingPane() throws GException {
		try {
			JPanel pane = new JPanel(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets = new Insets(5, 5, 5, 5);

			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.gridheight = 1;
			gbc.gridwidth = 1;
			gbc.anchor = GridBagConstraints.EAST;
			GLabel label = new GLabel("Table");
			pane.add(label, gbc);

			gbc.gridx = 1;
			gbc.gridy = 0;
			gbc.gridheight = 1;
			gbc.gridwidth = 1;
			gbc.anchor = GridBagConstraints.WEST;
			m_cmbDBDataTable = new GComboBox<>(getBEANComboBoxs_DBDataTable());
			pane.add(m_cmbDBDataTable, gbc);

			gbc.gridx = 0;
			gbc.gridy = 1;
			gbc.gridheight = 1;
			gbc.gridwidth = 1;
			gbc.anchor = GridBagConstraints.EAST;
			label = new GLabel("Column");
			pane.add(label, gbc);

			gbc.gridx = 1;
			gbc.gridy = 1;
			gbc.gridheight = 1;
			gbc.gridwidth = 1;
			gbc.anchor = GridBagConstraints.WEST;
			m_cmbFieldName = new GComboBox<>(new GList<>());
			pane.add(m_cmbFieldName, gbc);

			gbc.gridx = 0;
			gbc.gridy = 2;
			gbc.gridheight = 1;
			gbc.gridwidth = 1;
			gbc.anchor = GridBagConstraints.EAST;
			label = new GLabel("CSV Column Number");
			pane.add(label, gbc);

			gbc.gridx = 1;
			gbc.gridy = 2;
			gbc.gridheight = 1;
			gbc.gridwidth = 1;
			gbc.anchor = GridBagConstraints.WEST;
			m_txtCSVColumnNumber = new GTextField(20);
			pane.add(m_txtCSVColumnNumber, gbc);

			gbc.gridx = 0;
			gbc.gridy = 3;
			gbc.gridheight = 1;
			gbc.gridwidth = 1;
			gbc.anchor = GridBagConstraints.EAST;
			label = new GLabel("Default Data");
			pane.add(label, gbc);

			gbc.gridx = 1;
			gbc.gridy = 3;
			gbc.gridheight = 1;
			gbc.gridwidth = 1;
			gbc.anchor = GridBagConstraints.WEST;
			m_txtDefaultData = new GTextField(20);
			pane.add(m_txtDefaultData, gbc);

			gbc.gridx = 1;
			gbc.gridy = 4;
			gbc.gridheight = 1;
			gbc.gridwidth = 1;
			gbc.anchor = GridBagConstraints.WEST;
			m_btnAdd = new GButton("Add");
			pane.add(m_btnAdd, gbc);

			gbc.gridx = 1;
			gbc.gridy = 5;
			gbc.gridheight = 1;
			gbc.gridwidth = 1;
			gbc.anchor = GridBagConstraints.WEST;
			m_btnLoadColumnModelFile = new GButton("Load Column Model File");
			pane.add(m_btnLoadColumnModelFile, gbc);

			gbc.gridx = 2;
			gbc.gridy = 5;
			gbc.gridheight = 1;
			gbc.gridwidth = 1;
			gbc.anchor = GridBagConstraints.WEST;
			m_btnSaveColumnModelFile = new GButton("Save Column Model File");
			pane.add(m_btnSaveColumnModelFile, gbc);

			return pane;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	private JPanel getColumnModelSettingViewPane() throws GException {
		try {
			JPanel pane = new JPanel(new BorderLayout());

			m_tbdpnColumnModelSetting = new GTableDisplay(getDVTColumnModelSetting(), "Column Setting");
			pane.add(m_tbdpnColumnModelSetting, BorderLayout.CENTER);

			return pane;

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	private JPanel getDataLoadingPane() throws GException {
		try {
			JPanel pane = new JPanel(new BorderLayout());

			pane.add(getInputFilePane(), BorderLayout.NORTH);
			pane.add(getDataLoadViewPane(), BorderLayout.CENTER);

			return pane;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	private JPanel getInputFilePane() throws GException {
		try {
			JPanel pane = new JPanel(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets = new Insets(5, 5, 5, 5);

			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.gridheight = 1;
			gbc.gridwidth = 3;
			gbc.anchor = GridBagConstraints.CENTER;
			m_lblTableName = new GLabel("");
			pane.add(m_lblTableName, gbc);

			gbc.gridx = 0;
			gbc.gridy = 1;
			gbc.gridheight = 1;
			gbc.gridwidth = 1;
			gbc.anchor = GridBagConstraints.EAST;
			GLabel label = new GLabel("File(CSV)");
			pane.add(label, gbc);

			gbc.gridx = 1;
			gbc.gridy = 1;
			gbc.gridheight = 1;
			gbc.gridwidth = 1;
			gbc.anchor = GridBagConstraints.WEST;
			m_txtFileName = new GTextField(20);
			pane.add(m_txtFileName, gbc);

			gbc.gridx = 2;
			gbc.gridy = 1;
			gbc.gridheight = 1;
			gbc.gridwidth = 1;
			gbc.anchor = GridBagConstraints.WEST;
			m_btnChooseFile = new GButton("Choose File");
			pane.add(m_btnChooseFile, gbc);

			gbc.gridx = 1;
			gbc.gridy = 2;
			gbc.gridheight = 1;
			gbc.gridwidth = 1;
			gbc.anchor = GridBagConstraints.EAST;
			m_btnRefresh = new GButton("Refresh");
			pane.add(m_btnRefresh, gbc);

			gbc.gridx = 2;
			gbc.gridy = 2;
			gbc.gridheight = 1;
			gbc.gridwidth = 1;
			gbc.anchor = GridBagConstraints.WEST;
			m_btnLoadData = new GButton("Load Data");
			pane.add(m_btnLoadData, gbc);

			return pane;

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	private JPanel getDataLoadViewPane() throws GException {
		try {
			m_dvtDataView = new GTableDisplay(new DVTDataLoad(getColumnModelDataLoads()), "Data Loading");
			m_paneDVTDataView.add(m_dvtDataView, BorderLayout.CENTER);

			return m_paneDVTDataView;

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	private List<BEANComboBox<DBString>> getBEANComboBoxs_DBDataTable() throws GException {
		try {
			List<BEANComboBox<DBString>> lsBEANComboBoxs = new GList<>();

			for(DBDataTable dbdatatable : m_lsDBDataTables) {
				BEANComboBox<DBString> bean = new BEANComboBox<>(dbdatatable.getTableName().getTableLabel(), new DBString(dbdatatable.getTableID().getID()));
				lsBEANComboBoxs.add(bean);
			}

			return lsBEANComboBoxs;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	private DataVectorTable getDVTColumnModelSetting() throws GException {
		try {
			List<String> lsPrimaryKeys = new GList<>();
			lsPrimaryKeys.add(ColumnModelSetting.FieldName.getFieldName());

			List<String> lsSortKeys = new GList<>();
			lsSortKeys.add(ColumnModelSetting.CSVColumnNumber.getFieldName());
			lsSortKeys.add(ColumnModelSetting.FieldName.getFieldName());

			DataVectorTable dvtColumnModelSetting = new DataVectorTable(new ColumnModelSetting(), lsPrimaryKeys, lsSortKeys);

			for(String strFieldName : m_lsFieldNames) {
				ColumnDataLoad columnmodel = m_mapColumnModelDataLoad_FieldName.get(strFieldName);

				String strFieldLabel = columnmodel.getDataField().getFieldLabel();
				int intCSVColumnIndex = columnmodel.getCSVColumnIndex();
				String strDefaultData = columnmodel.getDefaultData();

				DataVectorRow dvrRow = new DataVectorRow(new ColumnModelSetting());
				dvrRow.addData(new DBInteger(intCSVColumnIndex + 1));
				dvrRow.addData(new DBInteger(intCSVColumnIndex));
				dvrRow.addData(new DBString(strFieldName));
				dvrRow.addData(new DBString(strFieldLabel));
				dvrRow.addData(new DBString(strDefaultData));

				dvtColumnModelSetting.addDataRow(dvrRow);
			}

			return dvtColumnModelSetting;

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public DBDataTable getSelectedDBDataTable() throws GException {
		try {
			DBString dbstrTableID = m_cmbDBDataTable.getSelectedID();

			if(m_mapDBDataTable_TableID.containsKey(dbstrTableID)) {
				DBDataTable dbdatatable = m_mapDBDataTable_TableID.get(dbstrTableID);

				return dbdatatable;

			} else {
				throw new GException("Invalid TableID("+ dbstrTableID + ")!");
			}
		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	private void setListSelectorFieldName() throws GException {
		try {
			m_mapDataField_FieldName = getSelectedDBDataTable().getMapDataField_FieldName();

			List<BEANComboBox<String>> lsBEANComboBoxs = getBEANComboBoxs_FieldName();
			m_cmbFieldName.setLSBEANComboBoxs(lsBEANComboBoxs);

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	private void setTableLabel() throws GException {
		try {
			String strTableName = "Table: ";
			String strTableLabel = getSelectedDBDataTable().getTableName().getTableLabel();

			strTableName = strTableName + strTableLabel;

			m_lblTableName.setText(strTableName);

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	private List<BEANComboBox<String>> getBEANComboBoxs_FieldName() throws GException {
		try {
			DBDataTable dbdatatable = getSelectedDBDataTable();

			List<BEANComboBox<String>> lsBEANComboBoxs = new GList<>();
			FieldPrimaryKey fieldPrimaryKey = dbdatatable.getFieldPrimaryKey();
			DataField dfPrimaryKey = fieldPrimaryKey.getDataField();
			DataField dfRecordUpdated = dbdatatable.getDataField(FieldRecordUpdated.FieldName);

			for(Map.Entry<String, DataField<? extends DBObject>> entDataField : m_mapDataField_FieldName.entrySet()) {
				DataField<? extends DBObject> datafield = entDataField.getValue();

				if(datafield.compareTo(dfPrimaryKey) == 0) {
					continue;
				}

				if(datafield.compareTo(dfRecordUpdated) == 0) {
					continue;
				}

				BEANComboBox<String> bean = new BEANComboBox<>(datafield.getFieldLabel(), datafield.getFieldName());
				lsBEANComboBoxs.add(bean);
			}

			return lsBEANComboBoxs;

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	private List<ColumnDataLoad> getColumnModelDataLoads() throws GException {
		try {
			List<ColumnDataLoad> lsColumnDataLoads = new GList<>();

			for(String strFieldName : m_lsFieldNames) {
				ColumnDataLoad columnmodel = m_mapColumnModelDataLoad_FieldName.get(strFieldName);

				lsColumnDataLoads.add(columnmodel);
			}

			return lsColumnDataLoads;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	private void addColumnModel(String p_strFieldName, int p_intCSVColumnIndex, String p_strDefaultData) throws GException {
		try {
			p_strDefaultData = p_strDefaultData.trim();

			if(m_mapDataField_FieldName.containsKey(p_strFieldName)) {
				if(p_intCSVColumnIndex == -1) {
					if(p_strDefaultData.length() == 0) {
						throw new GException("Invalid CSV Column Number.");
					}
				}

				DataField datafield = m_mapDataField_FieldName.get(p_strFieldName);
				ColumnDataLoad columnmodel = new ColumnDataLoad(datafield, p_intCSVColumnIndex, p_strDefaultData);

				if(m_mapColumnModelDataLoad_FieldName.containsKey(p_strFieldName)) {
					throw new GException("Duplicate Field(" + datafield.getFieldLabel() + ")!!!");
				}

				m_mapColumnModelDataLoad_FieldName.put(p_strFieldName, columnmodel);
				m_lsFieldNames.add(p_strFieldName);

				m_tbdpnColumnModelSetting.setDataVectorTable(getDVTColumnModelSetting());

				m_txtCSVColumnNumber.setText("");
				m_txtDefaultData.setText("");

			} else {
				String strSelectedItem = (String)m_cmbFieldName.getSelectedItem();

				throw new GException("Invalid Field(" + strSelectedItem + ")!");
			}
		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	private void registerActionListener() throws GException {
		m_cmbDBDataTable.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent p_event) {
				try {
					m_mapColumnModelDataLoad_FieldName = new GMap<>();
					m_lsFieldNames = new GList<>();

					m_paneDVTDataView.remove(m_dvtDataView);

					m_dvtDataView = new GTableDisplay(new DVTDataLoad(getColumnModelDataLoads()), "Data Loading");
					m_paneDVTDataView.add(m_dvtDataView, BorderLayout.CENTER);

					m_paneDVTDataView.updateUI();

					setListSelectorFieldName();
					setTableLabel();

					m_tbdpnColumnModelSetting.setDataVectorTable(getDVTColumnModelSetting());

				} catch(Exception exception) {
				  ExceptionHandler.display(exception);
				}
			}
		});

		m_btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent p_event) {
				try {
					OnAddColumnModel onAddColumnModel = new OnAddColumnModel();
					me.executeProcess_WithDialogWaiting(onAddColumnModel);

				} catch(Exception exception) {
					ExceptionHandler.display(exception);
				}
			}
		});

		m_btnLoadColumnModelFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent p_event) {
				try {
					OnLoadColumnModelFile onLoadColumnModelFile = new OnLoadColumnModelFile();
					me.executeProcess(onLoadColumnModelFile);

				} catch(Exception exception) {
					ExceptionHandler.display(exception);
				}
			}
		});

		m_btnSaveColumnModelFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent p_event) {
				try {
					OnSaveColumnModelFile onSaveColumnModelFile = new OnSaveColumnModelFile();
					me.executeProcess_WithDialogWaiting(onSaveColumnModelFile);

				} catch(Exception exception) {
					ExceptionHandler.display(exception);
				}
			}
		});

		m_btnChooseFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent p_event) {
				try {
					JFileChooser fileChooser = new JFileChooser();
					fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
					fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));

					int result = fileChooser.showOpenDialog(me);

					if (result == JFileChooser.APPROVE_OPTION) {
						File selectedFile = fileChooser.getSelectedFile();
						m_txtFileName.setText(selectedFile.getAbsolutePath());
					}
				} catch(Exception exception) {
				  ExceptionHandler.display(exception);
				}
			}
		});

		m_btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent p_event) {
				try {
					OnRefreshDataView onRefreshDataView = new OnRefreshDataView();
					me.executeProcess_WithDialogWaiting(onRefreshDataView);

				} catch(Exception exception) {
				  ExceptionHandler.display(exception);
				}
			}
		});

		m_btnLoadData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent p_event) {
				try {
					OnDataLoading onDataLoading = new OnDataLoading();
					me.executeProcess_WithDialogWaiting(onDataLoading);

				} catch(Exception exception) {
				  ExceptionHandler.display(exception);
				}
			}
		});

		m_tbdpnColumnModelSetting.addMenuItems(new GList<GMenuItem>() {{
			GMenuItem mi = new GMenuItem("Delete");
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent p_event) {
					try {
						int intConfirm = JOptionPane.showConfirmDialog(me, "Confirm Delete?", "Confirm Action.", JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE);

						if(intConfirm == JOptionPane.YES_OPTION) {
							DBString dbstrFieldName = m_tbdpnColumnModelSetting.getDVRCurrentRow().getDataAtColumnName(ColumnModelSetting.FieldName);

							OnDeleteColumnModel onDeleteColumnModel = new OnDeleteColumnModel(dbstrFieldName.getString());
							me.executeProcess_WithDialogWaiting(onDeleteColumnModel);
						}
					} catch(Exception exception) {
					  ExceptionHandler.display(exception);
					}
				}
			});

			add(mi);
		}});
	}

	private class OnAddColumnModel implements Runnable {

		public void run() {
			try {
				DBInteger dbintCSVColumnNumber = m_txtCSVColumnNumber.getDBInteger();
				DBString dbstrDefaultData = m_txtDefaultData.getDBString();

				if(dbintCSVColumnNumber.isInvalid()) {
					if(dbstrDefaultData.isInvalid()) {
						throw new GException("You must enter CSV Column Index to continue.");
					}

					dbintCSVColumnNumber = new DBInteger(0);

				} else {
					if(dbintCSVColumnNumber.compareTo(DBInteger.Zero) < 1) {
						throw new GException("You must enter CSV Column Index >= 1.");
					}
				}

				int intCSVColumnIndex = dbintCSVColumnNumber.add(-1).getIntValue();
				String strFieldName = m_cmbFieldName.getSelectedID();

				addColumnModel(strFieldName, intCSVColumnIndex, dbstrDefaultData.getString());

			} catch(Exception exception) {
				ExceptionHandler.display(exception);
			}
		}
	}

	private class OnLoadColumnModelFile implements Runnable {

		public void run() {
			try {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));

				fileChooser.setAcceptAllFileFilterUsed(false);
				fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Column Model", "meta"));

				int result = fileChooser.showOpenDialog(me);

				if (result == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();

					Properties properties = new Properties();
					properties.load(new FileInputStream(selectedFile.getCanonicalPath()));

					for(String strFieldName : properties.stringPropertyNames()) {
						String strDataElement = properties.getProperty(strFieldName);
						String strColumnNumber = "0";
						String strDefaultData = "";

						if(strDataElement.startsWith("$")
							&& strDataElement.endsWith("$")) {

							strDefaultData = strDataElement.replace("$", "");

						} else {
							if(!DBInteger.isInteger(strDataElement)) {
								throw new GException("Field Name: " + strFieldName + " is invalid value.");

							} else {
								strColumnNumber = strDataElement;
							}
						}

						addColumnModel(strFieldName, Integer.parseInt(strColumnNumber) - 1, strDefaultData);
					}
				}
			} catch(Exception exception) {
			  ExceptionHandler.display(exception);
			}
		}
	}

	private class OnSaveColumnModelFile implements Runnable {

		public void run() {
			try {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));

				int result = fileChooser.showOpenDialog(me);

				if(result == JFileChooser.APPROVE_OPTION) {
					DBDataTable datatable = getSelectedDBDataTable();

					File selectedDirectory = fileChooser.getSelectedFile();
					File selectedFile = new File(selectedDirectory, datatable.getTableName().getTableName() + ".meta");

					if(!selectedFile.exists()) {
						selectedFile.createNewFile();
					}

					Properties properties = new Properties();
					DataVectorTable dvtColumnModelSetting = m_tbdpnColumnModelSetting.getDVT();
					dvtColumnModelSetting.beforeFirst();

					while(dvtColumnModelSetting.next()) {
						DataVectorRow dvrRow = dvtColumnModelSetting.getDataRow();
						DBString dbstrFieldName = dvrRow.getDataAtColumnName(ColumnModelSetting.FieldName);
						DBInteger dbintCSVColumnIndex = dvrRow.getDataAtColumnName(ColumnModelSetting.CSVColumnIndex);
						DBString dbstrDefaultData = dvrRow.getDataAtColumnName(ColumnModelSetting.DefaultData);
						String strDataElement;

						if(dbintCSVColumnIndex.compareTo(new DBInteger(-1)) == 0) {
							strDataElement = '$' + dbstrDefaultData.getString() + '$';

						} else {
							strDataElement = dbintCSVColumnIndex.add(1).getString();
						}

						properties.setProperty(dbstrFieldName.getString(), strDataElement);
					}

					properties.store(new FileOutputStream(selectedFile), null);
				}
			} catch(Exception exception) {
			  ExceptionHandler.display(exception);
			}
		}
	}

	private class OnDeleteColumnModel implements Runnable {

		private String i_strFieldName;

		private OnDeleteColumnModel(String p_strFieldName) {
			i_strFieldName = p_strFieldName;
		}

		public void run() {
			try {
				if(m_mapDataField_FieldName.containsKey(i_strFieldName)) {
					DataField datafield = m_mapDataField_FieldName.get(i_strFieldName);

					if(!m_mapColumnModelDataLoad_FieldName.containsKey(i_strFieldName)) {
						throw new GException("Not-exsit Field(" + datafield.getFieldLabel() + ")!");
					}

					m_mapColumnModelDataLoad_FieldName.remove(i_strFieldName);
					m_lsFieldNames.remove(i_strFieldName);

					m_tbdpnColumnModelSetting.setDataVectorTable(getDVTColumnModelSetting());
					m_txtCSVColumnNumber.setText("");

				} else {
					throw new GException("Invalid Field(" + i_strFieldName + ")!");
				}
			} catch(Exception exception) {
				ExceptionHandler.display(exception);
			}
		}
	}

	private class OnRefreshDataView implements Runnable {

		public void run() {
			try {
				String strFileName = m_txtFileName.getText();

				if(strFileName.length() <= 0) {
					throw new GException("You must select file to continue.");
				}

				DVTDataLoad dvtDataLoad = new DVTDataLoad(getColumnModelDataLoads());

				ReadWriteTextFile file = new ReadWriteTextFile(strFileName);
				List<String> lsDataLoads = file.readToStringLine();

				int intLineNumber = 0;

				for(String strDataLoad : lsDataLoads) {
					intLineNumber++;

					String[] strElements = strDataLoad.split("\t");
					List<String> lsElements = Arrays.asList(strElements);

					DataVectorRow dvrRow = new DataVectorRow(new DVTDataLoad.DataLoadColumnModel(getColumnModelDataLoads()));
					dvrRow.setData(DVTDataLoad.DataLoadColumnModel.LineNumber.getFieldName(), new DBInteger(intLineNumber));

					for(Map.Entry<String, ColumnDataLoad> entColumnModelDataLoad : m_mapColumnModelDataLoad_FieldName.entrySet()) {
						ColumnDataLoad columnmodel = entColumnModelDataLoad.getValue();

						String strFieldName = columnmodel.getDataField().getFieldName();
						int intCSVColumnIndex = columnmodel.getCSVColumnIndex();
						String strDefaultData = columnmodel.getDefaultData();
						String strValue = "";

						if(intCSVColumnIndex > -1) {
							if(intCSVColumnIndex < lsElements.size()) {
								strValue = lsElements.get(intCSVColumnIndex);
							}
						}

						if(strValue.length() <= 0) {
							strValue = strDefaultData;
						}

						dvrRow.setData(strFieldName, new DBString(strValue));
					}

					dvtDataLoad.addDataRow(dvrRow);
				}

				m_paneDVTDataView.remove(m_dvtDataView);

				m_dvtDataView = new GTableDisplay(dvtDataLoad, "Data Loading");
				m_paneDVTDataView.add(m_dvtDataView, BorderLayout.CENTER);

				m_paneDVTDataView.updateUI();

			} catch(Exception exception) {
			  ExceptionHandler.display(exception);
			}
		}
	}

	private class OnDataLoading implements Runnable {

		public void run() {
			try {
				DBDataTable dbdatatable = getSelectedDBDataTable();
				DataTable datatable = new DataTable(AppClientUtilities.getSessionData().getSessionID(), dbdatatable.getClass());
				List<DBDataTable> lsDataLoads = getDataLoads(dbdatatable);

				datatable.insertDatas(lsDataLoads, TransactionBegin.Yes);

				new MsgDialog("Data Loading", "SUCCESSFUL", ExcDialog.MessageType.Message);

			} catch(Exception exception) {
			  ExceptionHandler.display(exception);
			}
		}

		public List<DBDataTable> getDataLoads(DBDataTable p_dbdatatable) throws GException {
			try {
				List<DBDataTable> lsDataLoads = new GList<>();
				DataVectorTable dvtDataLoadView = m_dvtDataView.getDVT();

				dvtDataLoadView.beforeFirst();

				while(dvtDataLoadView.next()) {
					DataVectorRow dvrRow = dvtDataLoadView.getDataRow();
					DBDataTable datatable = p_dbdatatable.getClass().newInstance();

					for(Map.Entry<String, ColumnDataLoad> entColumnModelDataLoad : m_mapColumnModelDataLoad_FieldName.entrySet()) {
						ColumnDataLoad columnmodel = entColumnModelDataLoad.getValue();

						DataField datafield = columnmodel.getDataField();
						DBObject dbobjValue = dvrRow.getDataAtColumnName(datafield);

						datatable.setData(datafield, dbobjValue.getString());
					}

					lsDataLoads.add(datatable);
				}

				return lsDataLoads;

			} catch(Exception exception) {
			  ExceptionHandler.display(exception);
			  throw new GException(exception);
			}
		}
	}

	private static class ColumnModelSetting extends DataTableColumnModel {

		private static final TableName TNColumnModelSetting = new TableName("ColumnModelSetting");

		private static final DataField<DBString> FieldName = new DataField<>(TNColumnModelSetting, "FieldName", DBString.class);
		private static final DataField<DBString> FieldLabel = new DataField<>(TNColumnModelSetting, "FieldLabel", DBString.class);
		private static final DataField<DBInteger> CSVColumnIndex = new DataField<>(TNColumnModelSetting, "CSVColumnIndex", DBInteger.class);
		private static final DataField<DBInteger> CSVColumnNumber = new DataField<>(TNColumnModelSetting, "CSVColumnNumber", DBInteger.class);
		private static final DataField<DBString> DefaultData = new DataField<>(TNColumnModelSetting, "DefaultData", DBString.class);

		private static final long serialVersionUID = 1;

		private ColumnModelSetting() throws GException {
			try {
				addColumn(CSVColumnNumber, "CSV Number", 100, Alignment.Center);
				addColumn(CSVColumnIndex);
				addColumn(FieldName);
				addColumn(FieldLabel, "Field", 200);
				addColumn(DefaultData, "Default Data", 200);

			} catch(Exception exception) {
				ExceptionHandler.display(exception);
				throw new GException(exception);
			}
		}
	}
}
