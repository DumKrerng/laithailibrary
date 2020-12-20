package com.laithailibrary.sharelibrary.datatableview;

import com.laithailibrary.sharelibrary.db.dbobject.*;
import exc.*;
import javax.swing.table.*;

/**
 * Created by dumkrerng on 9/9/2557.
 */
public class GTableModel extends DefaultTableModel {

	private DataTableColumnModel m_dataTableColumnModel;
	private SelectedMultiple m_selectedmultiple = SelectedMultiple.No;

	private static final long serialVersionUID = 10;

	public GTableModel(DataTableColumnModel p_dataTableColumnModel) {
		m_dataTableColumnModel = p_dataTableColumnModel;
	}

	public GTableModel(DataTableColumnModel p_dataTableColumnModel, SelectedMultiple p_selectedmultiple) {
		m_dataTableColumnModel = p_dataTableColumnModel;
		m_selectedmultiple = p_selectedmultiple;
	}

	public Class getColumnClass(int p_column) {
		try {
			Class aClass = m_dataTableColumnModel.getColumnClass(p_column);

			if(aClass.getCanonicalName().compareTo(DBString.class.getCanonicalName()) == 0) {
				return String.class;

			} else if(aClass.getCanonicalName().compareTo(DBInteger.class.getCanonicalName()) == 0) {
				return Integer.class;

			} else if(aClass.getCanonicalName().compareTo(DBBoolean.class.getCanonicalName()) == 0) {
				return Boolean.class;

			} else if(aClass.getCanonicalName().compareTo(DBDate.class.getCanonicalName()) == 0) {
				return String.class;

			} else if(aClass.getCanonicalName().compareTo(DBDateTime.class.getCanonicalName()) == 0) {
				return String.class;

			} else if(aClass.getCanonicalName().compareTo(DBDecimal.class.getCanonicalName()) == 0) {
				return Double.class;

			} else if(aClass.getCanonicalName().compareTo(DBMoney.class.getCanonicalName()) == 0) {
				return Double.class;

			} else if(aClass.getCanonicalName().compareTo(DBQuantity.class.getCanonicalName()) == 0) {
				return Double.class;

			} else {
				throw new GException("Not-support Class: " + aClass.getSimpleName());
			}
		} catch (Exception exception) {
			ExceptionHandler.display(exception);
		}

		return String.class;
	}

	public boolean isCellEditable(int p_intRow, int p_intColumn) {
		try {
			if(m_selectedmultiple == SelectedMultiple.Yes) {
				if(p_intColumn == 0) {
					String strColumnName = m_dataTableColumnModel.getColumnName(p_intColumn);

					if(strColumnName.compareTo("SELECTED") == 0) {
						Class aClass = m_dataTableColumnModel.getColumnClass(p_intColumn);

						if(aClass.getCanonicalName().compareTo(DBBoolean.class.getCanonicalName()) == 0) {
							return true;
						}
					}
				}
			}
		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		}

		return false;
	}

	public DataTableColumnModel getDataTableColumnModel() {
		return m_dataTableColumnModel;
	}

	public enum SelectedMultiple {
		Yes,
		No
	}
}
