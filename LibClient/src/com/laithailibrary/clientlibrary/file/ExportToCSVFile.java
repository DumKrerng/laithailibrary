package com.laithailibrary.clientlibrary.file;

import com.laithailibrary.clientlibrary.ui.swing.*;
import com.laithailibrary.sharelibrary.datatableview.*;
import com.laithailibrary.sharelibrary.datatableview.DataVectorTable;
import exc.ExceptionHandler;
import exc.GException;
import com.laithailibrary.sharelibrary.file.ReadWriteTextFile;
import msg.MsgDialog;

import javax.swing.*;
import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: dumkrerng
 * Date: 6/27/12
 * Time: 10:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExportToCSVFile {

	private ExportToCSVFile() throws GException {}

	public static void toCSVFile(GFrame p_frame, DataVectorTable p_dtvTable) throws GException {
		try {
			String strPathFolder = getPathFolder(p_frame);

			if(strPathFolder.length() > 0) {
				String strFileName = "";

				while(strFileName.length() <= 0) {
					strFileName = JOptionPane.showInputDialog(p_frame.getFocusOwner(), "Directory: " + strPathFolder + "\nEnter file name.");

					if(strFileName == null) {
						strFileName = "";
					}

					if(strFileName.length() <= 0) {
						strFileName = "";
						new MsgDialog("You must enter file name.");
					}
				}

				strFileName = strFileName + ".csv";
				strFileName = strPathFolder + File.separatorChar + strFileName;

				createFile(strFileName, getStringCSV(p_dtvTable));

				new MsgDialog("Export Successful.", "Export To: " + strFileName);
			}
		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public static void toCSVFile(GDialog p_dialog, DataVectorTable p_dtvTable) throws GException {
		try {
			String strPathFolder = getPathFolder(p_dialog);

			if(strPathFolder.length() > 0) {
				String strFileName = "";

				while(strFileName.length() <= 0) {
					strFileName = JOptionPane.showInputDialog(p_dialog.getFocusOwner(), "Directory: " + strPathFolder + "\nEnter file name.");

					if(strFileName == null) {
						strFileName = "";
					}

					if(strFileName.length() <= 0) {
						strFileName = "";
						new MsgDialog("You must enter file name.");
					}
				}

				strFileName = strFileName + ".csv";
				strFileName = strPathFolder + File.separatorChar + strFileName;

				createFile(strFileName, getStringCSV(p_dtvTable));

				new MsgDialog("Export Successful.", "Export To: " + strFileName);
			}
		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	private static String getPathFolder(GDialog p_dialog) throws GException {
		try {
			JFileChooser chooser = new JFileChooser();
			chooser.setDialogTitle("Select folder to save CSV file.");
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			chooser.setAcceptAllFileFilterUsed(false);

			String strPathFolder = "";

			if (chooser.showOpenDialog(p_dialog) == JFileChooser.APPROVE_OPTION) {
				strPathFolder = chooser.getSelectedFile().getPath();
			}

			return strPathFolder;

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	private static String getPathFolder(GFrame p_frame) throws GException {
		try {
			JFileChooser chooser = new JFileChooser();
			chooser.setDialogTitle("Select folder to save CSV file.");
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			chooser.setAcceptAllFileFilterUsed(false);

			String strPathFolder = "";

			if (chooser.showOpenDialog(p_frame) == JFileChooser.APPROVE_OPTION) {
				strPathFolder = chooser.getSelectedFile().getPath();
			}

			return strPathFolder;

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	private static void createFile(String p_strFileName, StringBuilder p_builder) throws GException {
		try {
			ReadWriteTextFile file = new ReadWriteTextFile(p_strFileName);

			file.write(p_builder);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	private static StringBuilder getStringCSV(DataVectorTable p_dtvTable) throws GException {
		try {
			StringBuilder builderReturn = new StringBuilder();

			String strCSVHeader = p_dtvTable.getCSVHeader();
			builderReturn.append(strCSVHeader).append("\n");

			while(p_dtvTable.next()) {
				DataVectorRow dvrRow = p_dtvTable.getDataRow();
				String strCSVData = dvrRow.getCSVData();

				builderReturn.append(strCSVData).append("\n");
			}

			return builderReturn;

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}
}
