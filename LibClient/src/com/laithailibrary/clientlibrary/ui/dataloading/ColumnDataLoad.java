package com.laithailibrary.clientlibrary.ui.dataloading;

import com.laithailibrary.sharelibrary.db.dbobject.*;
import com.laithailibrary.sharelibrary.field.*;
import exc.*;

/**
 * Created by dumkrerng on 28/2/2559.
 */
public class ColumnDataLoad implements Comparable<ColumnDataLoad> {

	private DataField m_datafield = null;
	private int m_intCSVColumnIndex = -1;
	private String m_strDefaultData = "";

	public ColumnDataLoad(DataField p_datafield, int p_intCSVColumnIndex, String p_strDefaultData) throws GException {
		try {
			m_datafield = p_datafield;
			m_intCSVColumnIndex = p_intCSVColumnIndex;
			m_strDefaultData = p_strDefaultData;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public <T extends DBObject> ColumnDataLoad(String p_strColumnName, Class<T> p_classData, int p_intCSVColumnIndex, String p_strDefaultData) throws GException {
		try {
			m_datafield = new DataField<T>(DVTDataLoad.TN, p_strColumnName, p_classData);
			m_intCSVColumnIndex = p_intCSVColumnIndex;
			m_strDefaultData = p_strDefaultData;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public DataField getDataField() {
		return m_datafield;
	}

	public int getCSVColumnIndex() {
		return m_intCSVColumnIndex;
	}

	public String getDefaultData() {
		return m_strDefaultData;
	}

	public int compareTo(ColumnDataLoad p_columnmodel) {
		return this.getDataField().compareTo(p_columnmodel.getDataField());
	}
}
