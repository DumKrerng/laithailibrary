package com.laithailibrary.clientlibrary.ui.swing.support;

import java.awt.*;
import java.io.*;
import java.util.List;
import java.util.*;
import com.laithailibrary.clientlibrary.ui.swing.*;
import com.laithailibrary.sharelibrary.collection.*;
import com.laithailibrary.sharelibrary.file.*;
import exc.*;

public class DefaultDataInTextFieldManager {

	private static final String PATH = "DefaultData";
	private static final String TEXT_SPLIT = "$#;#$";

	private static Map<String, Properties> s_mapDefaultData_DialogClass = new GMap<>();

	private DefaultDataInTextFieldManager() {}

	public static void loadDefaultData() throws GException {
		try {
			if(GFile.exists(PATH)) {
				List<File> lsFiles = GFile.getFiles(PATH);

				for(File file : lsFiles) {
					String strFileName = file.getName();
					String strPathDialogClass = PATH + '/' + strFileName;

					Properties properties = new Properties();
					properties.load(new FileInputStream(strPathDialogClass));

					s_mapDefaultData_DialogClass.put(strFileName, properties);
				}
			}
		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public static void setDataToDefault(GTextField p_textfield) throws GException {
		try {
			String strText = p_textfield.getText();
			String strObjectName = p_textfield.getName();

			if(strObjectName != null
				&& strObjectName.length() > 0) {

				String strDialogClass = getDialogClass(p_textfield);

				if(strDialogClass.length() > 0) {
					String strPathDialogClass = PATH + '/' + strDialogClass;
					Properties properties;

					if(s_mapDefaultData_DialogClass.containsKey(strDialogClass)) {
						properties = s_mapDefaultData_DialogClass.get(strDialogClass);

					} else {
						if(!GFile.exists(PATH)) {
							GFile.newDirectory(PATH);
						}

						File fileDialogClass;

						if(!GFile.exists(strPathDialogClass)) {
							fileDialogClass = GFile.newFile(strPathDialogClass);

						} else {
							fileDialogClass = GFile.getFile(strPathDialogClass);
						}

						properties = new Properties();
						properties.load(new FileInputStream(fileDialogClass));

						s_mapDefaultData_DialogClass.put(strDialogClass, properties);
					}

					if(strText.length() > 0) {
						properties.setProperty(strObjectName, strText);

					} else {
						properties.remove(strObjectName);
					}

					properties.store(new FileOutputStream(strPathDialogClass), null);
				}
			}
		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public static void setDataToDefault(GTextSelector p_textselector) throws GException {
		try {
			String strSelectedID = p_textselector.getSelectedID().getString();
			String strText = p_textselector.getText();
			String strObjectName = p_textselector.getName();

			if(strObjectName != null
				&& strObjectName.length() > 0) {

				String strDataToDefault = "";

				if(strText.length() > 0) {
					strDataToDefault = strSelectedID + TEXT_SPLIT + strText;
				}

				String strDialogClass = getDialogClass(p_textselector);

				if(strDialogClass.length() > 0) {
					String strPathDialogClass = PATH + '/' + strDialogClass;
					Properties properties;

					if(s_mapDefaultData_DialogClass.containsKey(strDialogClass)) {
						properties = s_mapDefaultData_DialogClass.get(strDialogClass);

					} else {
						if(!GFile.exists(PATH)) {
							GFile.newDirectory(PATH);
						}

						File fileDialogClass;

						if(!GFile.exists(strPathDialogClass)) {
							fileDialogClass = GFile.newFile(strPathDialogClass);

						} else {
							fileDialogClass = GFile.getFile(strPathDialogClass);
						}

						properties = new Properties();
						properties.load(new FileInputStream(fileDialogClass));

						s_mapDefaultData_DialogClass.put(strDialogClass, properties);
					}

					if(strDataToDefault.length() > 0) {
						properties.setProperty(strObjectName, strDataToDefault);

					} else {
						properties.remove(strObjectName);
					}

					properties.store(new FileOutputStream(strPathDialogClass), null);
				}
			}
		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public static String getDefaultData(GTextField p_textfield) throws GException {
		try {
			String strText = "";
			String strObjectName = p_textfield.getName();

			if(strObjectName != null
				&& strObjectName.length() > 0) {

				String strDialogClass = getDialogClass(p_textfield);

				if(strDialogClass.length() > 0) {
					if(s_mapDefaultData_DialogClass.containsKey(strDialogClass)) {
						Properties properties = s_mapDefaultData_DialogClass.get(strDialogClass);
						strText = properties.getProperty(strObjectName, "");
					}
				}
			}

			return strText;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

//	public static String getDefaultData(GTextSelector p_textselector) throws GException {
//		try {
//			String strText = "";
//			String strObjectName = p_textfield.getName();
//
//			if(strObjectName != null
//				&& strObjectName.length() > 0) {
//
//				String strDialogClass = getDialogClass(p_textfield);
//
//				if(strDialogClass.length() > 0) {
//					if(s_mapDefaultData_DialogClass.containsKey(strDialogClass)) {
//						Properties properties = s_mapDefaultData_DialogClass.get(strDialogClass);
//						strText = properties.getProperty(strObjectName, "");
//					}
//				}
//			}
//
//			return strText;
//
//		} catch(Exception exception) {
//			ExceptionHandler.display(exception);
//			throw new GException(exception);
//		}
//	}

	public static void populateDefaultData(GTextField p_textfield) throws GException {
		try {
			String strObjectName = p_textfield.getName();

			if(strObjectName != null
				&& strObjectName.length() > 0) {

				String strDialogClass = getDialogClass(p_textfield);

				if(strDialogClass.length() > 0) {
					if(s_mapDefaultData_DialogClass.containsKey(strDialogClass)) {
						Properties properties = s_mapDefaultData_DialogClass.get(strDialogClass);

						if(properties.containsKey(strObjectName)) {
							String strText = properties.getProperty(strObjectName, "");
							p_textfield.setText(strText);
						}
					}
				}
			}
		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public static void populateDefaultData(GTextSelector p_textselector) throws GException {
		try {
			String strObjectName = p_textselector.getName();

			if(strObjectName != null
				&& strObjectName.length() > 0) {

				String strDialogClass = getDialogClass(p_textselector);

				if(strDialogClass.length() > 0) {
					if(s_mapDefaultData_DialogClass.containsKey(strDialogClass)) {
						Properties properties = s_mapDefaultData_DialogClass.get(strDialogClass);

						if(properties.containsKey(strObjectName)) {
							String strText = properties.getProperty(strObjectName, "");

							if(strText.length() > 0) {
								StringTokenizer tokenizer = new StringTokenizer(strText, TEXT_SPLIT);
								String strID = (String)tokenizer.nextElement();
								String strDisplay = (String)tokenizer.nextElement();

								p_textselector.setSelected(strID, strDisplay);
							}
						}
					}
				}
			}
		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	private static String getDialogClass(GTextField p_textfield) throws GException {
		try {
			Container parent = p_textfield.getParent();
			String strDialogClassName = "";

			while(parent != null
				&& strDialogClassName.length() <= 0) {

				if(parent instanceof GDialog) {
					strDialogClassName = parent.getClass().getCanonicalName();
				}

				parent = parent.getParent();
			}

			return strDialogClassName;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}
}
