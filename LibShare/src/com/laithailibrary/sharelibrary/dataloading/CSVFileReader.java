package com.laithailibrary.sharelibrary.dataloading;

import exc.*;

/**
 * Created by dumkrerng on 24/2/2559.
 */
public class CSVFileReader {

	private String m_strFileName_FullPath = "";

	public CSVFileReader(String p_strFileName_FullPath) throws GException {
		try {
		  m_strFileName_FullPath = p_strFileName_FullPath;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}
}
