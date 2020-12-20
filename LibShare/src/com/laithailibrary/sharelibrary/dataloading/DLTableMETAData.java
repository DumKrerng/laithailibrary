package com.laithailibrary.sharelibrary.dataloading;

import java.util.*;
import com.laithailibrary.sharelibrary.bean.*;
import com.laithailibrary.sharelibrary.collection.*;
import exc.*;

/**
 * Created by dumkrerng on 24/2/2559.
 */
public class DLTableMETAData {

	private DBDataTable m_dbDataTable = null;
//	private GMap<Integer, DLColumnMETAData> m_mapDLColumnMETAData_CSVColumnIndex = new GMap<Integer, DLColumnMETAData>();
	private List<DLColumnMETAData> m_lsDLColumnMETADatas = new GList<DLColumnMETAData>();

	public DLTableMETAData(DBDataTable p_dbDataTable) throws GException {
		try {
		  m_dbDataTable = p_dbDataTable;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public void addColumn(DLColumnMETAData p_dlColumnMETAData) throws GException {
		try {
			m_lsDLColumnMETADatas.add(p_dlColumnMETAData);

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}
}
