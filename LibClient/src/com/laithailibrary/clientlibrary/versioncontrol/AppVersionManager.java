package com.laithailibrary.clientlibrary.versioncontrol;

import javax.swing.*;
import java.io.*;
import java.math.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import com.laithailibrary.clientlibrary.ui.support.*;
import com.laithailibrary.clientlibrary.ui.swing.*;
import com.laithailibrary.logger.*;
import com.laithailibrary.sharelibrary.file.*;
import com.laithailibrary.sharelibrary.locale.*;
import com.laithailibrary.sharelibrary.servicecall.*;
import com.laithailibrary.sharelibrary.socket.*;
import com.laithailibrary.sharelibrary.support.*;
import exc.*;

public class AppVersionManager {

	private String m_strAppName = "";
	private GSplash m_splash = null;

	private AppVersionManager() {}

	public AppVersionManager(String p_strAppName) {
		m_strAppName = p_strAppName;
	}

	public AppVersionManager(String p_strAppName, GSplash p_splash) {
		m_strAppName = p_strAppName;
		m_splash = p_splash;
	}

	public boolean isUpToDate() throws GException {
		try {
			File file = new File("AppVersion");

			if(!file.exists()) {
				Properties properties = new Properties();
				properties.setProperty("AppName", m_strAppName);
				properties.setProperty("AppVersion", "");

				properties.store(new FileOutputStream("AppVersion"), null);

				return false;
			}

			Properties properties = new Properties();
			properties.load(new FileInputStream(file));

			String strAppName = properties.getProperty("AppName");
			String strAppVersion = properties.getProperty("AppVersion");

			if(strAppVersion == null
				|| strAppVersion.length() <= 0) {

				return false;
			}

			if(strAppName == null
				|| strAppName.length() <= 0) {

				return false;
			}

			setSplashMessage("Application Version: " + strAppVersion);

			if(strAppVersion.compareTo("DEBUG") == 0) {
				return true;
			}

			AppVersion appversion = getAppVersion(strAppName, strAppVersion);

			return appversion.isUpdate();

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);

			System.exit(1);

		  throw new GException(exception);
		}
	}

	public AppVersion getAppVersion(String strAppName, String p_strAppVersion) throws GException {
		try {
			AppVersion appversion = new AppVersion(strAppName, p_strAppVersion);

			setSplashMessage("Verify Application Version . . .");

			Socket socket = ClientSocket.createSocket();

			OutputStream output = socket.getOutputStream();
			output.flush();
			ObjectOutputStream objOutput = new ObjectOutputStream(output);
			objOutput.writeObject(appversion);

			InputStream input = socket.getInputStream();
			ObjectInputStream objInput = new ObjectInputStream(input);

			appversion = (AppVersion)objInput.readObject();

			if(appversion == null) {
				throw new GException("Update Application Error!!!");
			}

			return appversion;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);

			System.exit(1);

		  throw new GException(exception);
		}
	}

	public boolean doUpdateApp() throws GException {
		try {
			File fileProperties = new File("AppVersion");

			if(!fileProperties.exists()) {
				throw new GException("Update Application Error!!!");
			}

			Properties properties = new Properties();
			properties.load(new FileInputStream(fileProperties));

			String strAppName = properties.getProperty("AppName");
			String strAppVersion = properties.getProperty("AppVersion");

			if(strAppVersion == null) {
				strAppVersion = "";
			}

			if(strAppVersion.compareTo("DEBUG") == 0) {
				return true;
			}

			if(strAppName == null
				|| strAppName.length() <= 0) {

				throw new GException("Update Application Error!!!");
			}

			AppManager appmanager = new AppManager(AppClientUtilities.getSessionID(), strAppName);
			appmanager.setRequestString("ClientAppSize");

			Socket socket = ClientSocket.createSocket();

			OutputStream output = socket.getOutputStream();
			output.flush();
			ObjectOutputStream objOutput = new ObjectOutputStream(output);
			objOutput.writeObject(appmanager);

			InputStream input = socket.getInputStream();
			ObjectInputStream objInput = new ObjectInputStream(input);

			double dubFileSize = Double.valueOf(objInput.readLong());

			objOutput.close();
			objInput.close();
			socket.close();

			if(dubFileSize <= 0) {
				throw new GException("Update Application Error!!!");
			}

			String strCaption_Updating = "Application updating . . .";

			if(AppClientUtilities.getLocale() == GLocale.Thai) {
				strCaption_Updating = "กำลังอัปเดดโปรแกรมตัวใหม่ . . .";
			}

			new GDialogWaiting(new GDialog(), strCaption_Updating, new Runnable() {
				@Override
				public void run() {
					try {
						setSplashMessage("Get New Application . . .");

						String strAppVersion_Temp = properties.getProperty("AppVersion");

						AppVersion appversion = getAppVersion(strAppName, strAppVersion_Temp);

						properties.setProperty("AppVersion", appversion.getAppVersion_Server());

						if(strAppName == null
							|| strAppName.length() <= 0) {

							properties.setProperty("AppName", m_strAppName);
							properties.setProperty("AppVersion", "");

							properties.store(new FileOutputStream("AppVersion"), null);

							setSplashMessage("Update Application Error!!!");

							throw new GException("Update Application Error!!!");
						}

						AppManager appmanager = new AppManager(AppClientUtilities.getSessionID(), strAppName);
						appmanager.setRequestString("GetNewApp");

						Socket socket = ClientSocket.createSocket();

						OutputStream output = socket.getOutputStream();
						output.flush();
						ObjectOutputStream objOutput = new ObjectOutputStream(output);
						objOutput.writeObject(appmanager);

						DataInputStream input = new DataInputStream(socket.getInputStream());

						BigDecimal bdFileSize_100 = new BigDecimal(dubFileSize / 100000);
						bdFileSize_100 = bdFileSize_100.setScale(2, RoundingMode.CEILING);

						FileOutputStream fileoutput = new FileOutputStream(strAppName + ".jar.tmp");

						byte[] buffer = new byte[1024];
						int bytesRead;
						double totalBytesRead = 0;

						while((bytesRead = input.read(buffer)) != -1) {
							fileoutput.write(buffer, 0, bytesRead);

							totalBytesRead += bytesRead;

							BigDecimal bdFileRead_100 = new BigDecimal(totalBytesRead / 100000);
							bdFileRead_100 = bdFileRead_100.setScale(2, RoundingMode.CEILING);

							setSplashMessage("Update Application: " + bdFileRead_100 + " / " + bdFileSize_100.toString());
						}

						fileoutput.close();

						objOutput.close();
						input.close();
						socket.close();

						renameAppJAR(strAppName);

						properties.store(new FileOutputStream("AppVersion"), null);

						String strCaption_Updated = "Application up to date.";

						if(AppClientUtilities.getLocale() == GLocale.Thai) {
							strCaption_Updated = "อัปเดดโปรแกรมเสร็จสิ้น เข้าใช้งานใหม่อีกครั้ง";
						}

						JOptionPane.showMessageDialog(null, strCaption_Updated);

						GLog.info("Application is updated.");

					} catch(Exception exception) {
						ExceptionHandler.display(exception);

						System.exit(1);
					}
				}
			});

			return true;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);

			System.exit(1);
		}

		return false;
	}

	private void setSplashMessage(String p_strMessage) {
		if(m_splash != null) {
			m_splash.setMessage(p_strMessage);
		}
	}

	private void renameAppJAR(String p_strAppName) throws GException {
		try {
			// File (or directory) with old name
			File fileTMP = new File(p_strAppName + ".jar.tmp");
//
//			// File (or directory) with new name
//			File fileProduct = new File(p_strAppName + ".jar");
//
//			String strOS = System.getProperty("os.name").toLowerCase();
//
//			if(strOS.startsWith("win")) {
//				GFile.writeToStorage(Files.readAllBytes(fileTMP.toPath()), p_strAppName + ".jar");
//				fileTMP.deleteOnExit();
//
//			} else {
//				Files.move(fileTMP.toPath(), fileProduct.toPath(), StandardCopyOption.REPLACE_EXISTING);
//			}

			GFile.writeToStorage(Files.readAllBytes(fileTMP.toPath()), p_strAppName + ".jar");
			fileTMP.deleteOnExit();

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}
}
