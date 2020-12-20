package com.laithailibrary.sharelibrary.dataloading;

import java.io.*;
import java.util.*;
import com.laithailibrary.sharelibrary.collection.*;
import com.laithailibrary.sharelibrary.db.dbobject.*;
import com.laithailibrary.sharelibrary.file.*;
import com.laithailibrary.sharelibrary.locale.*;
import exc.*;

public class BEANDataLoadResult implements Externalizable {

	private Map<DBInteger, String> m_mapLogSting_LineNumber = new GMap<>();
	private String m_strLogFileName = "";

	private static final long serialVersionUID = 1;

	public BEANDataLoadResult() {}

	public void writeExternal(ObjectOutput out) throws IOException {
		try {
			out.writeObject(m_mapLogSting_LineNumber);

		} catch (Exception exception) {
			out.close();

			ExceptionHandler.display(exception);
			throw new IOException(exception);
		}
	}

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		try {
			m_mapLogSting_LineNumber = (Map<DBInteger, String>)in.readObject();

		} catch (Exception exception) {
			in.close();

			ExceptionHandler.display(exception);
			throw new IOException(exception);
		}
	}

	public void addLogMessage(DBInteger p_dbintLineNumber, String p_strLogMessage) throws GException {
		try {
			String strLogSting;

			if(m_mapLogSting_LineNumber.containsKey(p_dbintLineNumber)) {
				strLogSting = m_mapLogSting_LineNumber.get(p_dbintLineNumber);

			} else {
				strLogSting = "";
			}

			strLogSting = strLogSting + p_strLogMessage;
			m_mapLogSting_LineNumber.put(p_dbintLineNumber, strLogSting);

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public Map<DBInteger, String> getMapLogSting_LineNumber() {
		return m_mapLogSting_LineNumber;
	}

	public boolean hasLogMassage() {
		if(m_mapLogSting_LineNumber.size() > 0) {
			return true;
		}

		return false;
	}

	public String getLogFileName() {
		return m_strLogFileName;
	}

	public void writeLog(DBDateTime p_dbdtmDateTimeBegin, String p_strDirectoryPath) throws GException {
		try {
			if(p_strDirectoryPath.length() <= 0) {
				throw new GException("Invalid Directory Path!!!");
			}

			m_strLogFileName = "";

			DBDateTime dbdtmTime = DBDateTime.getCurrentDateTime();
			m_strLogFileName = dbdtmTime.getString("yyyMMdd_HHmm", GLocale.English);
			m_strLogFileName = m_strLogFileName.concat(".log");

			GTextFile fileLog = new GTextFile();
			fileLog.addText_NewLine("Begin: " + p_dbdtmDateTimeBegin);
			fileLog.addText_NewLine("End: " + DBDateTime.getCurrentDateTime());

			if(m_mapLogSting_LineNumber.size() > 0) {
				for(Map.Entry<DBInteger, String> entLogSting : m_mapLogSting_LineNumber.entrySet()) {
					DBInteger dbintLineNumber = entLogSting.getKey();
					String strMessage = entLogSting.getValue();

					fileLog.addText_NewLine("Line#" + dbintLineNumber + ": " + strMessage);
				}
			} else {
				fileLog.addText_NewLine("No Error.");
			}

			fileLog.writeToStorage(p_strDirectoryPath, m_strLogFileName);

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}
}
