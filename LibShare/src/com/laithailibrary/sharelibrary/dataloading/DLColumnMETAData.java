package com.laithailibrary.sharelibrary.dataloading;

import com.laithailibrary.sharelibrary.field.*;
import exc.*;

/**
 * Created by dumkrerng on 24/2/2559.
 */
public class DLColumnMETAData {

	private DataField m_datafield = null;
	private int m_intCSVColumnIndex = -1;
	private String m_strDataDefault = "";

	public DLColumnMETAData(DataField p_datafield, int p_intCSVColumnIndex) throws GException {
		try {
		  m_datafield = p_datafield;
			m_intCSVColumnIndex = p_intCSVColumnIndex;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public void setDataDefault(String p_strDataDefault) throws GException {
		m_strDataDefault = p_strDataDefault;
	}

	public DataField getDataField() throws GException {
		return m_datafield;
	}

	public int getCSVColumnIndex() throws GException {
		return m_intCSVColumnIndex;
	}

	public String getDataDefault() throws GException {
		return m_strDataDefault;
	}
}
