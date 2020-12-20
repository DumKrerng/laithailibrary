package com.laithailibrary.sharelibrary.file;

import java.io.*;
import java.util.*;
import com.laithailibrary.sharelibrary.collection.*;
import com.laithailibrary.sharelibrary.file.support.*;
import exc.*;

/**
 * Created with IntelliJ IDEA.
 * User: dumkrerng
 * Date: 6/26/12
 * Time: 11:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReadWriteTextFile {

	private String m_setFileName;
	private StringBuilder m_builder = new StringBuilder();

	public ReadWriteTextFile(String p_setFileName) {
		m_setFileName = p_setFileName;
	}

	public StringBuilder read() throws GException {
		return read(GEncodingCode.UTF_8);
	}

	public StringBuilder read(GEncodingCode p_gencodingcode) throws GException {
		try {
			String NL = System.getProperty("line.separator");
			Scanner scanner = new Scanner(new FileInputStream(m_setFileName), p_gencodingcode.getCode());

			try {
				while(scanner.hasNextLine()) {
					if(m_builder.length() > 0) {
						m_builder.append(NL);
					}

					m_builder.append(scanner.nextLine());
				}
			} finally {
				scanner.close();
			}

			return m_builder;

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public List<String> readToStringLine() throws GException {
		return readToStringLine(GEncodingCode.UTF_8);
	}

	public List<String> readToStringLine(GEncodingCode p_gencodingcode) throws GException {
		try {
			Scanner scanner = new Scanner(new FileInputStream(m_setFileName), p_gencodingcode.getCode());
			List<String> lsStringLines = new GList<>();

			try {
				while(scanner.hasNextLine()) {
					lsStringLines.add(scanner.nextLine());
				}
			} finally {
				scanner.close();
			}

			return lsStringLines;

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public StringBuilder readNewFile(String p_setFileName) throws GException {
		try {
			m_setFileName = p_setFileName;

			return read();

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public void write(StringBuilder p_builder) throws GException {
		try {
			m_builder = p_builder;

			write();

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public void write() throws GException {
		write(GEncodingCode.UTF_8);
	}

	public void write(GEncodingCode p_gencodingcode) throws GException {
		try {
			Writer out = new OutputStreamWriter(new FileOutputStream(m_setFileName), p_gencodingcode.getCode());

			try {
				out.write(m_builder.toString());

			} finally {
				out.close();
			}
		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}
}
