package com.laithailibrary.sharelibrary.file;

import com.laithailibrary.sharelibrary.collection.*;
import com.laithailibrary.sharelibrary.file.support.*;
import exc.*;
import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;

/**
 * Created by dumkrerng on 30/4/2561.
 */
public class GTextFile extends GFile {

	private List<String> m_lsTexts = new GList<>();

	private static final long serialVersionUID = 1;

	public GTextFile() {
		super(GFilePostFix.TEXT);
	}

	public GTextFile(String p_strFullName_WithPath) throws GException {
		m_postfix = GFilePostFix.TEXT;

		setByteValues(p_strFullName_WithPath, true);
	}

	public GTextFile(String p_strFullName_WithPath, boolean p_bolVerifyFileExist) throws GException {
		m_postfix = GFilePostFix.TEXT;

		setByteValues(p_strFullName_WithPath, p_bolVerifyFileExist);
	}

	public GTextFile(String p_strFullName_WithPath, CreateNewFile_OnNotExist p_createnewfile_onnotexist) throws GException {
		try {
			m_postfix = GFilePostFix.TEXT;

			File file = null;

			if(!exists(p_strFullName_WithPath)) {
				if(p_createnewfile_onnotexist == CreateNewFile_OnNotExist.Yes) {
					String strFullName_WithPath = prepareSeparator(p_strFullName_WithPath);
					file = new File(strFullName_WithPath);

					if(!file.exists()) {
						File dir = new File(file.getParent());

						if(!dir.exists()) {
							dir.mkdirs();
						}

						file.createNewFile();
					}
				}
			} else {
				file = new File(p_strFullName_WithPath);
			}

			setByteValues(file);
			read(file);

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public void read(File p_file) throws GException {
		try {
			m_lsTexts = readFromFile(p_file);

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public static List<String> readFromFile(File p_file) throws GException {
		return readFromFile(p_file, StandardCharsets.UTF_8);
	}

	public static List<String> readFromFile(File p_file, GEncodingCode p_gencodingcode) throws GException {
		return readFromFile(p_file, Charset.forName(p_gencodingcode.getCode()));
	}

	public static List<String> readFromFile(File p_file, String p_strEncodingCode) throws GException {
		return readFromFile(p_file, Charset.forName(p_strEncodingCode));
	}

	public static List<String> readFromFile_Windows_874(File p_file) throws GException {
		return readFromFile(p_file, Charset.forName("x-windows-874"));
	}

	public static List<String> readFromFile_TIS_620(File p_file) throws GException {
		return readFromFile(p_file, Charset.forName("TIS-620"));
	}

	public static List<String> readFromFile(File p_file, Charset p_charsets) throws GException {
		try {
			List<String> lsTests = Files.readAllLines(p_file.toPath(), p_charsets);

			return lsTests;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public void addText_NewLine(String p_strText) throws GException {
		m_lsTexts.add(p_strText);
	}

	public void appendText(int p_intLineIndex, String p_strText) throws GException {
		try {
			String strString = m_lsTexts.get(p_intLineIndex);
			strString += p_strText;

			m_lsTexts.set(p_intLineIndex, strString);

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public void appendText_LastLine(String p_strText) throws GException {
		try {
			if(m_lsTexts.size() <= 0) {
				addText_NewLine(p_strText);

			} else {
				int intLineIndex = m_lsTexts.size() - 1;
				appendText(intLineIndex, p_strText);
			}
		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public List<String> getTexts() {
		return m_lsTexts;
	}

	public StringBuilder getStringBuffer() {
		StringBuilder buffer = new StringBuilder();
		boolean bolFirstLine = true;

		for(String strString: m_lsTexts) {
			if(!bolFirstLine) {
				strString = '\n' + strString;
			}

			buffer.append(strString);

			bolFirstLine = false;
		}

		return buffer;
	}

	public String getStringBuffer_ToString() {
		return getStringBuffer().toString();
	}

	public void writeToStorage(String p_strDirectoryPath, String p_strFileName_WithFileType) throws GException {
		writeToStorage(p_strDirectoryPath + File.separator + p_strFileName_WithFileType);
	}

	public void writeToStorage(String p_strFileName_FullPath) throws GException {
		try {
			ReadWriteTextFile rwTextFile = new ReadWriteTextFile(p_strFileName_FullPath);
			rwTextFile.write(getStringBuffer());

//			File file = GFile.newFile(p_strFileName_FullPath);
//
//			if(m_lsTexts.size() > 0) {
//				Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF8"));
//				out.append();
//				out.flush();
//				out.close();
//
//			} else {
//				if(isValid()) {
//					writeToStorage(getByteValues(), file.getParent(), file.getName());
//				}
//			}
		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}
}
