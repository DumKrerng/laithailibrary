package com.laithailibrary.clientlibrary.ui;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;
import com.laithailibrary.clientlibrary.ui.support.*;
import com.laithailibrary.sharelibrary.collection.*;
import com.laithailibrary.sharelibrary.datatableview.*;
import com.laithailibrary.sharelibrary.db.dbobject.*;
import com.laithailibrary.sharelibrary.support.*;
import exc.*;

public class GTable extends JTable {

	private GTable me;

	private DataTableColumnModel m_dataTableColumnModel = null;
	private DataVectorTable m_datavectortable = null;
	private DataVectorTable m_datavectortable_Original = null;

	private JTableHeader m_tableheader = null;

	public GTable(DataTableColumnModel p_dataTableColumnModel) throws GException {
		try {
			init(p_dataTableColumnModel, null);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public GTable(DataVectorTable p_datavectortable) throws GException {
		try {
			init(p_datavectortable.getDataTableColumnModel(), p_datavectortable);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public GTable(DataTableColumnModel p_dataTableColumnModel, DataVectorTable p_datavectortable) throws GException {
		try {
			init(p_dataTableColumnModel, p_datavectortable);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	private void init(DataTableColumnModel p_dataTableColumnModel, DataVectorTable p_datavectortable) throws GException {
		try {
			me = this;

			setAutoCreateColumnsFromModel(false);
			setTableColumnModel(p_dataTableColumnModel);

			if(p_datavectortable != null) {
				setDataVectorTable(p_datavectortable);
			}

			setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	private void setTableColumnModel(DataTableColumnModel p_dataTableColumnModel) throws GException {
		m_dataTableColumnModel = p_dataTableColumnModel;

		super.setColumnModel(m_dataTableColumnModel);
	}

	public void setDataVectorTable(DataVectorTable p_datavectortable) throws GException {
		m_datavectortable = p_datavectortable;
		m_datavectortable_Original = p_datavectortable;

		super.setModel(m_datavectortable.getTableModel());
	}

	public void resetStructureDataTable(int p_intSelectedRowNumber) throws GException {
		try {
			setSelectedRow(p_intSelectedRowNumber);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	protected void initializeLocalVars() {
		try {
			setUpdateSelectionOnSort(true);
			setOpaque(true);

			createDefaultRenderers();
			createDefaultEditors();

			if(m_tableheader == null) {
				m_tableheader = new JTableHeader(m_dataTableColumnModel);
				m_tableheader.setResizingAllowed(true);
				m_tableheader.setReorderingAllowed(false);
				m_tableheader.setFont(new Font(GFontUtilities.FONT_NAME_NORMAL, Font.BOLD, 20));

				setTableHeader(m_tableheader);
			}

			setFont(new Font(GFontUtilities.FONT_NAME_NORMAL, Font.PLAIN, 20));

			setShowGrid(true);
			setSelectionBackground(Color.lightGray);
			setAutoResizeMode(AUTO_RESIZE_OFF);
			setAutoscrolls(true);
			setRowHeight(22);
			setRowMargin(1);
			setRowSelectionAllowed(true);
			setCellEditor(null);
//			setEditingColumn(-1);
//			setEditingRow(-1);
			setSurrendersFocusOnKeystroke(false);

			ToolTipManager toolTipManager = ToolTipManager.sharedInstance();
			toolTipManager.registerComponent(this);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
		}
	}

	public TableColumnModel getColumnModel() {
		if(m_dataTableColumnModel != null) {
			columnModel = m_dataTableColumnModel;

			return columnModel;
		}

		return columnModel;
	}

	protected TableModel createDefaultDataModel() {
		if(m_dataTableColumnModel != null) {
			return new GTableModel(m_dataTableColumnModel);
		}

		return new DefaultTableModel();
	}

	protected TableColumnModel createDefaultColumnModel() {
		if(m_dataTableColumnModel != null) {
			return m_dataTableColumnModel;
		}

		return new DataTableColumnModel();
	}

	public boolean isCellEditable(int row, int column) {
		return false;
	}

	public Object getValueAt(int p_row, int p_column) {
		try {
			Class aClass = m_dataTableColumnModel.getColumnClass(p_column);

			if(aClass.newInstance() instanceof DBString) {
				DBString dbstrString = (DBString)getModel().getValueAt(p_row, p_column);

				return dbstrString.getString();

			} else if(aClass.newInstance() instanceof DBInteger) {
				DBInteger dbintInteger = (DBInteger)getModel().getValueAt(p_row, p_column);

				return dbintInteger.getIntValue();

			} else if(aClass.newInstance() instanceof DBBoolean) {
				DBBoolean dbbolBoolean = (DBBoolean)getModel().getValueAt(p_row, p_column);

				return dbbolBoolean.getBoolean();

			} else if(aClass.newInstance() instanceof DBDate) {
				DBDate dbdtDBDate = (DBDate)getModel().getValueAt(p_row, p_column);

				return dbdtDBDate.getString(AppClientUtilities.getLocaleID());

			} else if(aClass.newInstance() instanceof DBDateTime) {
				DBDateTime dbdtmDBDateTime = (DBDateTime)getModel().getValueAt(p_row, p_column);

				return dbdtmDBDateTime.getString(AppClientUtilities.getLocaleID());

			} else if(aClass.newInstance() instanceof DBDecimal) {
				DBDecimal dbdcMoney = (DBDecimal)getModel().getValueAt(p_row, p_column);

				return dbdcMoney.getString();

			} else if(aClass.newInstance() instanceof DBMoney) {
				DBMoney dbmMoney = (DBMoney)getModel().getValueAt(p_row, p_column);

				return dbmMoney.getString();

			} else if(aClass.newInstance() instanceof DBQuantity) {
				DBQuantity dbqQuantity = (DBQuantity)getModel().getValueAt(p_row, p_column);

				return dbqQuantity.getString();

			} else {
				throw new GException("Not-support Class: " + aClass.getSimpleName());
			}
		} catch (Exception exception) {
			ExceptionHandler.display(exception);
		}

		return null;
	}

	public TableCellRenderer getCellRenderer(int row, int p_column) {
		try {
			TableCellRenderer tableCellRenderer = m_dataTableColumnModel.getTableColumn(p_column).getCellRenderer();

			return tableCellRenderer;

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
		}

		return getDefaultRenderer(getColumnClass(p_column));
	}

	public DBString getCurrentPrimaryKey_StringValue() throws GException {
		return new DBString(getCurrentPrimaryKeyValue().getStringValue());
	}

	public PrimaryKeyValue getCurrentPrimaryKeyValue() throws GException {
		try {
			PrimaryKeyValue primarykeyvalue = new PrimaryKeyValue();

			int intRowIndex = getSelectedRow();

			if(intRowIndex == -1) {
				return primarykeyvalue;
			}

			List<String> lsColumnPrimaryKeys = m_datavectortable_Original.getColumnNamePrimaryKeys();

			if(lsColumnPrimaryKeys.size() <= 0) {
				throw new GException("Not-Existing Column PrimaryKey!");
			}

			if(getModel().getRowCount() > intRowIndex) {
				for(String strColumnName : lsColumnPrimaryKeys) {
					int intColumnIndex = m_dataTableColumnModel.getColumnIndex(strColumnName);
					DBObject dboValue = (DBObject)getModel().getValueAt(intRowIndex, intColumnIndex);

					primarykeyvalue.add(dboValue);
				}
			}

			return primarykeyvalue;

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public DataVectorTable getDVT() {
		return m_datavectortable_Original;
	}

	public DataVectorRow getDataVectorRow(DBObject p_dboPrimaryKey) throws GException {
		try {
			DataVectorRow dvtRow = m_datavectortable.getDataVectorRow(p_dboPrimaryKey);

			return dvtRow;

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public DataVectorRow getCurrentDataVectorRow() throws GException {
		try {
			DBString dbstrCurrentPrimaryKey_StringValue = getCurrentPrimaryKey_StringValue();

			if(dbstrCurrentPrimaryKey_StringValue.isInvalid()) {
				return new DataVectorRow(m_dataTableColumnModel);
			}

			PrimaryKeyValue primarykey = getCurrentPrimaryKeyValue();
			DataVectorRow dvtRow = m_datavectortable.getDataVectorRow(primarykey);

			return dvtRow;

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public DataVectorRow getCurrentDataVectorRow_Backup() throws GException {
		try {
			DBString dbstrCurrentPrimaryKey_StringValue = getCurrentPrimaryKey_StringValue();

			if(dbstrCurrentPrimaryKey_StringValue.isInvalid()) {
				return new DataVectorRow(m_dataTableColumnModel);
			}

			DataVectorRow dvtRow = m_datavectortable.getDataVectorRow(dbstrCurrentPrimaryKey_StringValue);

			return dvtRow;

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public List<DataVectorRow> getSelectedDataVectorRows() throws GException {
		try {
			List<DataVectorRow> lsDataVectorRows = new GList<>();
			int[] intIndexRows = getSelectedRows();

			if(intIndexRows.length > 0) {
				for(int Index : intIndexRows) {
					DataVectorRow dvrRow = m_datavectortable_Original.getDataRow(Index);
					lsDataVectorRows.add(dvrRow);
				}
			}

			return lsDataVectorRows;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public <T extends DBObject> List<T> getSelectedValues_ByColumnName(Class<T> p_class, String p_strColumnName) throws GException {
		try {
			List<T> lsValues = new GList<>();
			int[] intIndexRows = getSelectedRows();

			if(intIndexRows.length > 0) {
				for(int Index : intIndexRows) {
					DataVectorRow dvrRow = m_datavectortable_Original.getDataRow(Index);
					T value = dvrRow.getDataAtColumnName(p_class, p_strColumnName);
					lsValues.add(value);
				}
			}

			return lsValues;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public <T extends DBObject> List<T> getSelectedPrimaryKeyValues(Class<T> p_class) throws GException {
		try {
			List<String> lsColumnPrimaryKeys = m_datavectortable_Original.getColumnNamePrimaryKeys();

			if(lsColumnPrimaryKeys.size() <= 0) {
				throw new GException("Not-Existing Column PrimaryKey!!!");

			} else if(lsColumnPrimaryKeys.size() > 1) {
				throw new GException("Not supported multiple PrimaryKey!!!");
			}

			String strColumnName = lsColumnPrimaryKeys.get(0);

			List<T> lsValues = new GList<>();
			int[] intIndexRows = getSelectedRows();

			if(intIndexRows.length > 0) {
				for(int Index : intIndexRows) {
					DataVectorRow dvrRow = m_datavectortable_Original.getDataRow(Index);
					T value = dvrRow.getDataAtColumnName(p_class, strColumnName);
					lsValues.add(value);
				}
			}

			return lsValues;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public void search(String p_strSearch) throws GException {
		search(p_strSearch, -1);
	}

	public void search(String p_strSearch, int p_intColumnNumber) throws GException {
		try {
			super.setModel(m_datavectortable_Original.getTableModel());

			if(p_strSearch.length() > 0) {
				DataVectorTable datavectortable_Search = new DataVectorTable(m_datavectortable_Original.getDataTableColumnModel(),
					m_datavectortable_Original.getColumnNamePrimaryKeys(), m_datavectortable_Original.getColumnNameSortKeys(), m_datavectortable_Original.getSorting());

				if(p_intColumnNumber == -1) {
					p_intColumnNumber = getSelectedColumn();
				}

				if(p_intColumnNumber < 0) {
					throw new GException("Not found selected column.");
				}

				for(int row_Index = 0; row_Index < me.getRowCount(); row_Index++) {
					String strValue = me.getValueAt(row_Index, p_intColumnNumber).toString();
					if(strValue.contains(p_strSearch)) {
						DataVectorRow datarow = m_datavectortable_Original.getDataRow(row_Index);
						datavectortable_Search.addDataRow(datarow);
					}
				}

				super.setModel(datavectortable_Search.getTableModel());

				if(datavectortable_Search.getRowCount() <= 0) {
					setRowSelectionInterval(0, 0);

					me.repaint();
					me.updateUI();

					return;
				}
			} else {
				setDataVectorTable(m_datavectortable_Original);
			}

			me.repaint();
			me.updateUI();

			resetStructureDataTable(0);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public int getSelectedColumn() {
		return columnModel.getSelectionModel().getMinSelectionIndex();
	}

	public int getSelectedColumnIndex() {
		return columnModel.getSelectionModel().getMinSelectionIndex();
	}

	public String getSelectedColumnName() throws GException {
		try {
			int intSelectedColumnIndex = getSelectedColumnIndex();

			TableColumn tablecolumn = columnModel.getColumn(intSelectedColumnIndex);
			String strHeaderName = (String)tablecolumn.getHeaderValue();

			return strHeaderName;

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public void setSelectedRow_AllowNotExisting(PrimaryKeyValue p_pkView) throws GException {
		try {
			int intRowNumber = m_datavectortable.getRowNumber(p_pkView);

			if(intRowNumber <= -1) {
				intRowNumber = 0;
			}

			resetStructureDataTable(intRowNumber);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public void setSelectedRow(int p_intRowNumber) throws GException {
		try {
			if(p_intRowNumber >= 0) {
				int intRowCount = getRowCount();

				if(intRowCount > 0
					&& p_intRowNumber < intRowCount) {

					setRowSelectionInterval(p_intRowNumber, p_intRowNumber);

				} else {
					setRowSelectionInterval(-1, -1);
				}
			}
		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public void setRowSelectionInterval(int index0, int index1) {
		scrollRectToVisible(getCellRect(index0, 1, true));

		super.selectionModel.setSelectionInterval(index0, index1);
	}
}
