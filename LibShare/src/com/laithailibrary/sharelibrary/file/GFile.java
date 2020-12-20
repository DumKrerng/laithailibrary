package com.laithailibrary.sharelibrary.file;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import com.laithailibrary.sharelibrary.collection.*;
import com.laithailibrary.sharelibrary.file.support.*;
import exc.*;

/**
 * Created by dumkrerng on 31/10/2560.
 */
public class GFile implements Externalizable {

	protected GFilePostFix m_postfix = null;
	private byte[] m_bytevalues = null;

	private static final long serialVersionUID = 3;

	public GFile() {}

	public GFile(GFilePostFix p_filepostfix) {
		m_postfix = p_filepostfix;
	}

	public GFile(String p_strFullName_WithPath) throws GException {
		setByteValues(p_strFullName_WithPath, true);
	}

	public GFile(String p_strFullName_WithPath, boolean p_bolVerifyFileExist) throws GException {
		setByteValues(p_strFullName_WithPath, p_bolVerifyFileExist);
	}

	public GFile(String p_strFullName_WithPath, CreateNewFile_OnNotExist p_createnewfile_onnotexist) throws GException {
		try {
			setByteValues(p_strFullName_WithPath, false);

			if(!exists(p_strFullName_WithPath)) {
				if(p_createnewfile_onnotexist == CreateNewFile_OnNotExist.Yes) {
					File file = newFile(p_strFullName_WithPath);
					setByteValues(file);
				}
			}
		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public GFile(byte[] p_bytevalues) throws GException {
		try {
			m_bytevalues = p_bytevalues;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public void writeExternal(ObjectOutput p_out) throws IOException {
		try {
			p_out.writeObject(m_bytevalues);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			p_out.close();

			throw new IOException(exception);
		}
	}

	public void readExternal(ObjectInput p_in) throws IOException {
		try {
			m_bytevalues = (byte[])p_in.readObject();

		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			p_in.close();

			throw new IOException(exception);
		}
	}

	public byte[] getByteValues() {
		return m_bytevalues;
	}

	public boolean isValid() {
		if(m_bytevalues != null
			&& m_bytevalues.length > 0) {

			return true;
		}

		return false;
	}

	public boolean isInvalid() {
		if(m_bytevalues == null) {
			return true;
		}

		if(m_bytevalues.length <= 0) {
			return true;
		}

		return false;
	}

	public static File toFile(String p_strFullName_FullPath) throws GException {
		try {
			File file = new File(prepareSeparator(p_strFullName_FullPath));

			return file;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public static void delete(String p_strFullName_FullPath) throws GException {
		try {
			File file = new File(prepareSeparator(p_strFullName_FullPath));

			if(file.exists()) {
				file.delete();
			}
		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	protected void setByteValues(String p_strFullName_FullPath, boolean p_bolVerifyFileExist) throws GException {
		try {
			if(p_strFullName_FullPath.length() > 0) {
				File file = new File(p_strFullName_FullPath);

				if(p_bolVerifyFileExist) {
					setByteValues(file);

				} else {
					if(file.exists()) {
						setByteValues(file);
					}
				}
			}
		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public static boolean exists(String p_strFullName_FullPath) throws GException {
		File file = new File(p_strFullName_FullPath);

		return file.exists();
	}

	public void setByteValues(File p_file) throws GException {
		try {
			m_bytevalues = Files.readAllBytes(p_file.toPath());

			if(m_postfix == null) {
				m_postfix = GFilePostFix.getFileType(p_file.getAbsolutePath());

			} else {
				GFilePostFix postfix = GFilePostFix.getFileType(p_file.getAbsolutePath());

				if(postfix != m_postfix) {
					if(p_file.exists()) {
						p_file.delete();
					}

					StringBuilder bufferError = new StringBuilder();
					bufferError.append("Invalid PostFix file name.\n");
					bufferError.append("Request: ").append(m_postfix.getLabel()).append('\n');
					bufferError.append("Current: ").append(postfix.getLabel());

					throw new GException(bufferError.toString());
				}
			}
		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public static String prepareSeparator(String p_strFullName_Full) throws GException {
		try {
			String strFullName_Prepared = p_strFullName_Full;

			String strOS = System.getProperty("os.name").toLowerCase();

			if(strOS.startsWith("win")) {
				strFullName_Prepared = strFullName_Prepared.replace('/', File.separatorChar);
			}

			return strFullName_Prepared;

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public void writeToStorage(String p_strDirectoryPath, String p_strFileName_WithFileType) throws GException {
		writeToStorage(getByteValues(), p_strDirectoryPath, p_strFileName_WithFileType);
	}

	public static void writeToStorage(byte[] p_byteValues, String p_strFileName_WithFileType) throws GException {
		writeToStorage(p_byteValues, "", p_strFileName_WithFileType);
	}

	public static void writeToStorage(byte[] p_byteValues, String p_strDirectoryPath, String p_strFileName_WithFileType) throws GException {
		try {
			String strFileName_Full;

			if(p_strDirectoryPath.length() > 0) {
				p_strDirectoryPath = prepareSeparator(p_strDirectoryPath);

				if(p_strDirectoryPath.endsWith("\\")
					|| p_strDirectoryPath.endsWith("/")) {

					p_strDirectoryPath = p_strDirectoryPath.substring(0, p_strDirectoryPath.length() - 1);
				}

				strFileName_Full = p_strDirectoryPath + File.separatorChar + p_strFileName_WithFileType;

			} else {
				strFileName_Full = p_strFileName_WithFileType;
			}

			File file = new File(strFileName_Full);

			if(!file.exists()) {
				File dir = new File(p_strDirectoryPath);

				if(!dir.exists()) {
					dir.mkdirs();
				}

				file.createNewFile();
			}

			FileOutputStream output = new FileOutputStream(file);
			output.write(p_byteValues);
			output.flush();
			output.close();

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public static File newFile(String p_strDirectoryPath, String p_strFileName_WithFileType) throws GException {
		try {
			p_strDirectoryPath = prepareSeparator(p_strDirectoryPath);

			if(p_strDirectoryPath.endsWith("\\")
				|| p_strDirectoryPath.endsWith("/")) {

				p_strDirectoryPath = p_strDirectoryPath.substring(0, p_strDirectoryPath.length() - 1);
			}

			String strFileName_Full = p_strDirectoryPath + File.separatorChar + p_strFileName_WithFileType;
			File file = new File(strFileName_Full);

			if(!file.exists()) {
				File dir = new File(p_strDirectoryPath);

				if(!dir.exists()) {
					dir.mkdirs();
				}

				file.createNewFile();
			}

			return file;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public static File newFile(String p_strFileName_FullPath) throws GException {
		try {
			p_strFileName_FullPath = prepareSeparator(p_strFileName_FullPath);
			File file = new File(p_strFileName_FullPath);

			if(!file.exists()) {
				File dir = new File(file.getParent());

				if(!dir.exists()) {
					dir.mkdirs();
				}

				file.createNewFile();
			}

			return file;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public static File newDirectory(String p_strDirectory_FullPath) throws GException {
		try {
			p_strDirectory_FullPath = prepareSeparator(p_strDirectory_FullPath);
			File dir = new File(p_strDirectory_FullPath);

			if(!dir.exists()) {
				dir.mkdirs();
			}

			return dir;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public static File getFile(String p_strDirectoryPath, String p_strFileName_WithFileType) throws GException {
		try {
			p_strDirectoryPath = prepareSeparator(p_strDirectoryPath);

			if(p_strDirectoryPath.endsWith("\\")
				|| p_strDirectoryPath.endsWith("/")) {

				p_strDirectoryPath = p_strDirectoryPath.substring(0, p_strDirectoryPath.length() - 1);
			}

			String strFileName_Full = p_strDirectoryPath + File.separatorChar + p_strFileName_WithFileType;
			File file = new File(strFileName_Full);

			if(!file.exists()) {
				throw new GException("File not found!!!");
			}

			return file;

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public static File getFile(String p_strFileName_Full) throws GException {
		try {
			p_strFileName_Full = prepareSeparator(p_strFileName_Full);

			File file = new File(p_strFileName_Full);

			if(!file.exists()) {
				throw new GException("File not found!!!");
			}

			return file;

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public static List<File> getFiles(String p_strDirectoryPath) throws GException {
		return getFiles(new File(p_strDirectoryPath));
	}

	public static List<File> getFiles(File p_directory) throws GException {
		try {
			List<File> lsFiles = new GList<>();
			List<File> lsDirectories = new GList<>();

			if(p_directory.exists()) {
				File[] files = p_directory.listFiles();

				for(File file : files) {
					if(file.isFile()) {
						lsFiles.add(file);

					} else {
						lsDirectories.add(file);
					}
				}

				for(File directory : lsDirectories) {
					List<File> lsFiles_Temp = getFiles(directory);
					lsFiles.addAll(lsFiles_Temp);
				}
			}

			return lsFiles;

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public enum CreateNewFile_OnNotExist {
		Yes,
		No
	}
}
