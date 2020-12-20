package com.laithailibrary.clientlibrary.ui.dataloading;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.List;
import java.util.*;
import com.laithailibrary.clientlibrary.ui.*;
import com.laithailibrary.clientlibrary.ui.swing.*;
import com.laithailibrary.sharelibrary.collection.*;
import com.laithailibrary.sharelibrary.dataloading.*;
import com.laithailibrary.sharelibrary.datatableview.*;
import com.laithailibrary.sharelibrary.db.dbobject.*;
import com.laithailibrary.sharelibrary.file.*;
import com.laithailibrary.sharelibrary.file.support.*;
import com.laithailibrary.sharelibrary.locale.*;
import com.laithailibrary.sharelibrary.support.*;
import exc.*;
import msg.*;

public abstract class UIDLDataLoader extends GDialog {

	private UIDLDataLoader me;

	private String m_strMetaFileName;

	private List<String> m_lsFieldNames = new GList<>();
	private Map<String, ColumnDataLoad> m_mapColumnModelDataLoad_FieldName = new GMap<>();

	private GTextField m_txtCSVFileName;
	private String m_strDirectoryPath = "";

	private GEncodingCodeSelector m_cmbEncodingCodeSelector;

	private GButton m_btnSelectCSVFile;
	private GButton m_btnRefresh;
	private GButton m_btnLoadData;

	private JPanel m_paneDVTDataView = new JPanel(new BorderLayout());
	private GTableDisplay m_dvtDataView;

	private static final String NODATA = "@#NODATA#@";

	private UIDLDataLoader() {}

	public UIDLDataLoader(GFrame p_frame, String p_strTitle, String p_strMetaFileName) throws GException {
		super(p_frame, p_strTitle, true);

		init00(p_strMetaFileName, UIDimension.DIM_1000_700);
	}

	public UIDLDataLoader(GFrame p_frame, String p_strTitle, String p_strMetaFileName, Dimension p_dimension) throws GException {
		super(p_frame, p_strTitle, true);

		init00(p_strMetaFileName, p_dimension);
	}

	public UIDLDataLoader(GDialog p_dialog, String p_strTitle, String p_strMetaFileName) throws GException {
		super(p_dialog, p_strTitle, true);

		init00(p_strMetaFileName, UIDimension.DIM_1000_700);
	}

	public UIDLDataLoader(GDialog p_dialog, String p_strTitle, String p_strMetaFileName, Dimension p_dimension) throws GException {
		super(p_dialog, p_strTitle, true);

		init00(p_strMetaFileName, p_dimension);
	}

	private void init00(String p_strMetaFileName, Dimension p_dimension) throws GException {
		try {
			me = this;
			m_strMetaFileName = p_strMetaFileName;

			buildDialogLayout();
			registerActionListener();

			init();

			setSize(p_dimension);

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	protected void init() throws GException {}

	private void buildDialogLayout() throws GException {
		try {
			Container pane = getContentPane();
			pane.setLayout(new BorderLayout());

			pane.add(getDataLoadingPane(), BorderLayout.CENTER);

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	private JPanel getDataLoadingPane() throws GException {
		try {
			JPanel pane = new JPanel(new BorderLayout());

			JPanel paneDL = new JPanel(new BorderLayout());
			paneDL.add(getInputFilePane(), BorderLayout.NORTH);
			paneDL.add(getDataLoadViewPane(), BorderLayout.CENTER);

			JPanel paneCustom = getCustomPane();

			if(paneCustom != null) {
				pane.add(paneCustom, BorderLayout.NORTH);
			}

			pane.add(paneDL, BorderLayout.CENTER);

			return pane;

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	protected JPanel getCustomPane() throws GException {
		return null;
	}

	private JPanel getInputFilePane() throws GException {
		try {
			JPanel pane = new JPanel(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets = new Insets(5, 5, 5, 5);

			gbc.gridx = 1;
			gbc.gridy = 1;
			gbc.gridheight = 1;
			gbc.gridwidth = 1;
			gbc.anchor = GridBagConstraints.WEST;
			m_txtCSVFileName = new GTextField(30);
			m_txtCSVFileName.setEditable(false);
			pane.add(m_txtCSVFileName, gbc);

			gbc.gridx = 2;
			gbc.gridy = 1;
			gbc.gridheight = 1;
			gbc.gridwidth = 1;
			gbc.anchor = GridBagConstraints.WEST;
			m_btnSelectCSVFile = new GButton("Select CSV File");
			pane.add(m_btnSelectCSVFile, gbc);

			gbc.gridx = 1;
			gbc.gridy = 2;
			gbc.gridheight = 1;
			gbc.gridwidth = 1;
			gbc.anchor = GridBagConstraints.EAST;
			GLabel label = new GLabel("Text Encoding");
			pane.add(label, gbc);

			gbc.gridx = 2;
			gbc.gridy = 2;
			gbc.gridheight = 1;
			gbc.gridwidth = 1;
			gbc.anchor = GridBagConstraints.WEST;
			m_cmbEncodingCodeSelector = new GEncodingCodeSelector();
			pane.add(m_cmbEncodingCodeSelector, gbc);

			gbc.gridx = 1;
			gbc.gridy = 3;
			gbc.gridheight = 1;
			gbc.gridwidth = 1;
			gbc.anchor = GridBagConstraints.EAST;
			m_btnRefresh = new GButton("Refresh");
			pane.add(m_btnRefresh, gbc);

			gbc.gridx = 2;
			gbc.gridy = 3;
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

	protected abstract BEANDataLoadResult onLoadData(DataVectorTable p_dvtDataLoad) throws GException;

	private void registerActionListener() throws GException {

		m_btnSelectCSVFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent p_event) {
				try {
					JFileChooser fileChooser = new JFileChooser();
					fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

					if(m_strDirectoryPath.length() > 0) {
						fileChooser.setCurrentDirectory(new File(m_strDirectoryPath));

					} else {
						fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
					}

					int result = fileChooser.showOpenDialog(me);

					if(result == JFileChooser.APPROVE_OPTION) {
						File selectedFile = fileChooser.getSelectedFile();
						String strCSVFileName_Full = selectedFile.getAbsolutePath();

						if(!strCSVFileName_Full.endsWith(".csv")) {
							throw new GException("File is not support.\n" + selectedFile.getAbsoluteFile());
						}

						m_txtCSVFileName.setText(strCSVFileName_Full);
						m_strDirectoryPath = selectedFile.getParent();

						me.executeProcess_WithDialogWaiting(new OnRefreshDataView());
					}
				} catch(Exception exception) {
					ExceptionHandler.display(exception);
				}
			}
		});

		m_btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent p_event) {
				try {
					me.executeProcess_WithDialogWaiting(new OnRefreshDataView());

				} catch(Exception exception) {
					ExceptionHandler.display(exception);
				}
			}
		});

		m_btnLoadData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent p_event) {
				try {
					if(m_dvtDataView.getDVT().size() <= 0) {
						throw new GException("No Data!!!");
					}

					if(GConfirmDialog.confirmAction(me, "", "Load Data")) {
						me.executeProcess_WithDialogWaiting(new DoDataLoading());
					}
				} catch(Exception exception) {
					ExceptionHandler.display(exception);
				}
			}
		});
	}

	private class DoDataLoading implements Runnable {

		public void run() {
			try {
				DBDateTime dbdtmDateTimeBegin = DBDateTime.getCurrentDateTime();

				BEANDataLoadResult beanDataLoadResult = onLoadData(m_dvtDataView.getDVT().cloneDataVectorTable());
				beanDataLoadResult.writeLog(dbdtmDateTimeBegin, m_strDirectoryPath);

				if(beanDataLoadResult.hasLogMassage()) {
					String strCaption = "Data Load Error!\nError detail ";

					if(AppClientUtilities.getLocale() == GLocale.Thai) {
						strCaption = "พบข้อผิดพลาดในการโหลดข้อมูล\nอ่านข้อผิดพลาดใน ";
					}

					new MsgDialog("Data Load", strCaption + beanDataLoadResult.getLogFileName(), ExcDialog.MessageType.Error);

				} else {
					String strCaption = "SUCCESSFUL.";

					if(AppClientUtilities.getLocale() == GLocale.Thai) {
						strCaption = "เสร็จสมบูรณ์";
					}

					new MsgDialog("Data Load", strCaption, ExcDialog.MessageType.Message);
				}
			} catch(Exception exception) {
			  ExceptionHandler.display(exception);
			}
		}
	}

	private class OnRefreshDataView implements Runnable {

		public void run() {
			try {
				String strCSVFilePath = m_txtCSVFileName.getText();

				if(strCSVFilePath.length() <= 0) {
					throw new GException("You must select file to continue.");
				}

				GEncodingCode gencodingcode = m_cmbEncodingCodeSelector.getSelectedID();

				onLoadColumnModelFile();
				DVTDataLoad dvtDataLoad = new DVTDataLoad(getColumnModelDataLoads());

				ReadWriteTextFile file = new ReadWriteTextFile(strCSVFilePath);
				List<String> lsDataLoads = file.readToStringLine(gencodingcode);

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

						strValue = strValue.trim();

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

		private void onLoadColumnModelFile() {
			try {
				m_lsFieldNames = new GList<>();
				m_mapColumnModelDataLoad_FieldName = new GMap<>();

				File fileMeta = null;

				try {
					fileMeta = GFile.getFile(m_strDirectoryPath, m_strMetaFileName);

				} catch(Exception exception) {
					String strError = exception.getMessage();

					if(strError.contains("File not found")) {
						throw new GException("Meta File not found!!!");
					}
				}

				List<String> lsTexts = GTextFile.readFromFile(fileMeta);

				for(String strText : lsTexts) {
					DBString dbstrText  = new DBString(strText);

					if(dbstrText.toString().contains("=")) {
						if(!dbstrText.startsWith("#")) {
							List<DBString> lsDataColumns = dbstrText.split("=");
							String strColumnName = lsDataColumns.get(0).getString();
							String strColumnData = lsDataColumns.get(1).getString();

							String strColumnNumber = "0";
							DBString dbstrDefaultData = new DBString();

							if(strColumnData.startsWith("$")
								&& strColumnData.endsWith("$")) {

								String strTemp = strColumnData.replace("$", "");

								if(strTemp.length() <= 0) {
									strTemp = NODATA;
								}

								dbstrDefaultData = new DBString(strTemp);

							} else {
								if(!DBInteger.isInteger(strColumnData)) {
									throw new GException("Field Name: " + strColumnName + " is invalid value.");

								} else {
									strColumnNumber = strColumnData;
								}
							}

							addColumnModel(strColumnName, Integer.parseInt(strColumnNumber) - 1, dbstrDefaultData);
						}
					}
				}

				m_paneDVTDataView.remove(m_dvtDataView);

				m_dvtDataView = new GTableDisplay(new DVTDataLoad(getColumnModelDataLoads()), "Data Loading");
				m_paneDVTDataView.add(m_dvtDataView, BorderLayout.CENTER);

				m_paneDVTDataView.updateUI();

			} catch(Exception exception) {
				ExceptionHandler.display(exception);
			}
		}

		private void addColumnModel(String p_strFieldName, int p_intCSVColumnIndex, DBString p_dbstrDefaultData) throws GException {
			try {
				if(m_mapColumnModelDataLoad_FieldName.containsKey(p_strFieldName)) {
					throw new GException("Duplicate Field(" + p_strFieldName + ")!!!");
				}

				String strDefaultData = "";

				if(p_intCSVColumnIndex == -1) {
					if(p_dbstrDefaultData.isInvalid()) {
						throw new GException("Field: " + p_strFieldName + ", Invalid Column Number!!!");

					} else {
						if(p_dbstrDefaultData.compareTo(new DBString(NODATA)) != 0) {
							strDefaultData = p_dbstrDefaultData.getString();
						}
					}
				}

				m_lsFieldNames.add(p_strFieldName);

				ColumnDataLoad columnmodel = new ColumnDataLoad(p_strFieldName, DBString.class, p_intCSVColumnIndex, strDefaultData);
				m_mapColumnModelDataLoad_FieldName.put(p_strFieldName, columnmodel);

			} catch(Exception exception) {
				ExceptionHandler.display(exception);
				throw new GException(exception);
			}
		}
	}
}
