package pp;


import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.*;
import java.util.*;
import com.laithailibrary.logger.*;
import exc.*;
import msg.*;

/**
 * Created by IntelliJ IDEA.
 * User: dumkrerng
 * Date: 6/4/11
 * Time: 11:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class GProperties {

	private static Properties s_properties = new Properties();

	private static String s_strPropertiesFileName = "";

	public static final String KEY_DBType = "DBType";
	public static final String KEY_DBConnectionString = "DBConnectionString";

	public static final String KEY_ServerAddress = "ServerAddress";
	public static final String KEY_ServerPort = "ServerPort";

	public static final String KEY_AppVersion = "AppVersion";

	public static final String KEY_WebServerAddress = "WebServerAddress";
	public static final String KEY_WebServerPort = "WebServerPort";

	public static final String KEY_PHPJavaPort = "PHPJavaPort";

	private GProperties() {}

	public static void setPropertiesFileName(String p_strPropertiesFileName) throws GException {
		try {
			s_strPropertiesFileName = p_strPropertiesFileName;

			GLog.info("Reading Properties . . .");
			ReadProperties();

			for(String strPropertyName : s_properties.stringPropertyNames()) {
				String strPropertyValue = s_properties.getProperty(strPropertyName);
				GLog.info(strPropertyName.concat(" = ").concat(strPropertyValue));
			}

			GLog.info("Read Properties complete.");

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);

			new MsgDialog("Reading Properties Error!", ExcDialog.MessageType.Error);

			System.exit(ProgramExit.ReadingPropertiesError);
		}
	}

	public static void ReadProperties() throws GException {
		try {
			s_properties = new Properties();
			s_properties.load(new FileInputStream(s_strPropertiesFileName));

		} catch (IOException exception) {
			clearProperty();

			ReadProperties();

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public static void WritePropertiesFile() throws GException {
		try {
			if(s_strPropertiesFileName.length() > 0) {
				s_properties.store(new FileOutputStream(s_strPropertiesFileName), null);
			}
		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public static void setPPProperty(String p_strKey, String p_strValue) {
		s_properties.put(p_strKey, p_strValue);
	}

	public static String getPPProperty(String p_strKey) {
		return (String)s_properties.get(p_strKey);
	}

	public static String getDBType() throws GException {
		return (String)s_properties.get(KEY_DBType);
	}

	public static String getDBConnectionString() throws GException {
		return (String)s_properties.get(KEY_DBConnectionString);
	}

	public static String getServerAddress() throws GException {
		return (String)s_properties.get(KEY_ServerAddress);
	}

	public static int getServerPort() throws GException {
		return Integer.valueOf(s_properties.get(KEY_ServerPort).toString());
	}

	public static String getAppVersion(String p_strAppName) throws GException {
		return (String)s_properties.get(getExtraKEY(KEY_AppVersion, p_strAppName));
	}

	public static String getWebServerAddress() throws GException {
		return (String)s_properties.get(KEY_WebServerAddress);
	}

	public static int getWebServerPort() throws GException {
		return Integer.valueOf(s_properties.get(KEY_WebServerPort).toString());
	}

	public static int getPHPJavaPort() throws GException {
		return Integer.valueOf(s_properties.get(KEY_PHPJavaPort).toString());
	}

	public static Set<String> getKeys_All() throws GException {
		try {
			Set<Object> setObjectKey = s_properties.keySet();
			Set<String> setKey_Return = new TreeSet<>();

			for(Object object : setObjectKey) {
				setKey_Return.add(object.toString());
			}

			return setKey_Return;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public static Set<String> getKeys_StartsWith(String p_strPrefix) throws GException {
		try {
			Set<Object> setObjectKey = s_properties.keySet();
			Set<String> setKey_Return = new TreeSet<>();

			for(Object object : setObjectKey) {
				String strKey = object.toString();

				if(strKey.startsWith(p_strPrefix)) {
					setKey_Return.add(strKey);
				}
			}

			return setKey_Return;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public static void clearProperty() throws GException {
		s_properties = new Properties();
		WritePropertiesFile();
	}

//	private static void createProperties() throws GException {
//		try {
//			String strDBPath = getDBPath();
//
//			m_Properties.put(m_DBConnectionString, strDBPath);
//
//			m_Properties.store(new FileOutputStream(m_PropertiesFileName), null);
//
//		} catch (Exception exception) {
//		  ExceptionHandler.display(exception);
//		  throw new GException(exception);
//		}
//	}

	private static String getExtraKEY(String p_strKEY, String p_strExtraString) throws GException {
		return p_strKEY + '.' + p_strExtraString;
	}

	public static String getDBPath() throws GException {
		try {
			JFileChooser jFileChooser = new JFileChooser();

			jFileChooser.setAcceptAllFileFilterUsed(false);

			jFileChooser.setFileFilter(new FileFilter() {
				@Override
				public boolean accept(final File p_file) {
					String strFileName = p_file.getName();

					if(strFileName.toLowerCase().endsWith(".db")
						|| p_file.isDirectory()) {

						return true;

					} else {
						return false;
					}
				}

				@Override
				public String getDescription() {
					return "SQLite Database";
				}
			});

			int r = jFileChooser.showOpenDialog(new JDialog());

			if(r == JFileChooser.APPROVE_OPTION) {
				String strPath = jFileChooser.getSelectedFile().getPath();
				System.out.println(strPath);

				return strPath;

			} else {
				new MsgDialog("Error!!!", "Error!!!\nCan not connect to database!");

				System.exit(ProgramExit.CanNotConnectToDatabase);
			}
		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}

		return "";
	}
}
