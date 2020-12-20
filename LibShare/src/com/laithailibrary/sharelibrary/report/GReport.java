package com.laithailibrary.sharelibrary.report;

import java.io.*;
import java.util.*;
import com.laithailibrary.sharelibrary.collection.*;
import com.laithailibrary.sharelibrary.datatableview.*;
import com.laithailibrary.sharelibrary.db.dbobject.*;
import com.laithailibrary.sharelibrary.file.*;
import exc.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.*;

/**
 * Created by dumkrerng on 20/1/2558.
 */
public abstract class GReport implements Externalizable, Serializable {

	private String m_strPath = "";
	private String m_strJASPERName = "";

	private JasperPrint m_report = null;

	private static final long serialVersionUID = 2;

	public GReport() {}

	protected GReport(String p_strPath, String p_strJASPERName) throws GException {
		try {
			m_strPath = "report" + File.separator + p_strPath;
			m_strJASPERName = p_strJASPERName;

			if(!m_strJASPERName.endsWith(".jasper")) {
				throw new GException("Not support file " + m_strJASPERName + "!!!");
			}

			String strJASPERName_FullPath = m_strPath + File.separator + m_strJASPERName;

			if(!GFile.exists(strJASPERName_FullPath)) {
				throw new GException("Jasper not found!!!\n" + strJASPERName_FullPath);
			}
		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public void writeExternal(ObjectOutput p_out) throws IOException {
		try {
			p_out.writeObject(m_report);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			p_out.close();

			throw new IOException(exception);
		}
	}

	public void readExternal(ObjectInput p_in) throws IOException {
		try {
			m_report = (JasperPrint)p_in.readObject();

		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			p_in.close();

			throw new IOException(exception);
		}
	}

	public void buildReport() throws GException {
		try {
			String strJASPERName_FullPath = m_strPath + File.separator + m_strJASPERName;

			Collection<Map<String, ?>> colMapData = getCollectionMapData(getDVTReportDisplay());
			Map<String, Object> mapParameter = getMapParameter();

			if(colMapData.size() > 0) {
				JRDataSource datasource = new JRBeanCollectionDataSource(colMapData, true);
				m_report = JasperFillManager.fillReport(strJASPERName_FullPath, mapParameter, datasource);

			} else {
				m_report = JasperFillManager.fillReport(strJASPERName_FullPath, mapParameter);
			}
		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public JasperPrint getReport() {
		return m_report;
	}

	protected abstract DataVectorTable getDVTReportDisplay() throws GException;

	public Map<String, Object> getMapParameter() throws GException {
		return new GMap<>();
	}

	private Collection<Map<String, ? extends Object>> getCollectionMapData(DataVectorTable p_datavectortable) throws GException {
		try {
			Collection<Map<String, ? extends Object>> colMapData = new GList<>();

			DataTableColumnModel dcmColumnModel = p_datavectortable.getDataTableColumnModel();

			if(dcmColumnModel != null) {
				Map<String, Integer> mapColumnIndex_ColumnName = dcmColumnModel.getMapColumnIndex_ColumnName();

				p_datavectortable.beforeFirst();

				while(p_datavectortable.next()) {
					DataVectorRow dvrRow = p_datavectortable.getDataRow();

					Map<String, Object> mapData = new GMap<>();

					for(Map.Entry<String, Integer> entColumnIndex : mapColumnIndex_ColumnName.entrySet()) {
						String strColumnName = entColumnIndex.getKey();

						DBObject dboData = dvrRow.getDataAtColumnName(strColumnName);
						mapData.put(strColumnName, dboData.getValueReport());
					}

					colMapData.add(mapData);
				}
			}

			return colMapData;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}
}
