package com.laithailibrary.sharelibrary.datatableview;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.*;
import javax.swing.table.*;
import java.awt.*;
import java.io.*;
import java.util.List;
import java.util.*;
import com.laithailibrary.sharelibrary.collection.*;
import com.laithailibrary.sharelibrary.db.dbobject.*;
import com.laithailibrary.sharelibrary.field.*;
import com.laithailibrary.sharelibrary.support.*;
import exc.*;
import sun.swing.table.*;

/**
 * Created by IntelliJ IDEA.
 * User: dumkrerng
 * Date: 6/7/11
 * Time: 1:02 AM
 * To change this template use File | Settings | File Templates.
 */
public class DataTableColumnModel extends DefaultTableColumnModel implements Cloneable, Comparable<DataTableColumnModel>, Externalizable {

	private Map<String, Integer> m_mapColumnIndex_ColumnName;
	private Map<Integer, String> m_mapColumnName_ColumnIndex;
	private Map<Integer, Class<? extends DBObject>> m_mapClass_ColumnIndex;
	private Map<Integer, TableColumn> m_mapTableColumn_ColumnIndex;
	private Map<Integer, ShowInCSV> m_mapShowInCSV_ColumnIndex;
	private Map<Integer, Alignment> m_mapAlignment_ColumnIndex;

	private List<String> m_lsColumnNameData;

	private Integer m_intColumnIndex;
	private Integer m_intColumnCount;

	private static final long serialVersionUID = 3;

	public DataTableColumnModel() {
		m_mapColumnIndex_ColumnName = new GMap<>();
		m_mapColumnName_ColumnIndex = new GMap<>();
		m_mapClass_ColumnIndex = new GMap<>();
		m_mapTableColumn_ColumnIndex = new GMap<>();
		m_mapShowInCSV_ColumnIndex = new GMap<>();
		m_mapAlignment_ColumnIndex = new GMap<>();
		m_intColumnIndex = -1;
		m_intColumnCount = 0;
		m_lsColumnNameData = new GList<>();
	}

	public void writeExternal(ObjectOutput out) throws IOException {
		try {
			out.writeObject(m_mapColumnIndex_ColumnName);
			out.writeObject(m_mapColumnName_ColumnIndex);
			out.writeObject(m_mapClass_ColumnIndex);
			out.writeObject(m_mapTableColumn_ColumnIndex);
			out.writeObject(m_mapShowInCSV_ColumnIndex);
			out.writeObject(m_mapAlignment_ColumnIndex);
			out.writeObject(m_lsColumnNameData);
			out.writeObject(m_intColumnIndex);
			out.writeObject(m_intColumnCount);

			for(Map.Entry<Integer, TableColumn> entTableColumn : m_mapTableColumn_ColumnIndex.entrySet()) {
				super.addColumn(entTableColumn.getValue());
			}
		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			out.close();

			throw new IOException(exception);
		}
	}

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		try {
			m_mapColumnIndex_ColumnName = (Map<String, Integer>)in.readObject();
			m_mapColumnName_ColumnIndex = (Map<Integer, String>)in.readObject();
			m_mapClass_ColumnIndex = (Map<Integer, Class<? extends DBObject>>)in.readObject();
			m_mapTableColumn_ColumnIndex = (Map<Integer, TableColumn>)in.readObject();
			m_mapShowInCSV_ColumnIndex = (Map<Integer, ShowInCSV>)in.readObject();
			m_mapAlignment_ColumnIndex = (Map<Integer, Alignment>)in.readObject();
			m_lsColumnNameData = (List<String>)in.readObject();
			m_intColumnIndex = (Integer)in.readObject();
			m_intColumnCount = (Integer)in.readObject();

			for(Map.Entry<Integer, TableColumn> entTableColumn : m_mapTableColumn_ColumnIndex.entrySet()) {
				super.addColumn(entTableColumn.getValue());
			}
		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			in.close();

			throw new IOException(exception);
		}
	}

	public <T extends DataField> void addColumn(T p_datafield) throws GException {
		try {
		  String strFieldName = p_datafield.getFieldName();
		  Class<? extends DBObject> clClassData = p_datafield.getClassData();
			String strFieldLabel = p_datafield.getFieldLabel();
			int intColumnWidth = p_datafield.getColumnWidth();
			Alignment alignment = p_datafield.getColumnAlignment();

			addColumn(strFieldName, strFieldLabel, clClassData, intColumnWidth, ShowInCSV.Yes, alignment);

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public <T extends DataField> void addColumn(T p_datafield, String p_strFieldLabel) throws GException {
		try {
		  String strFieldName = p_datafield.getFieldName();
		  Class<? extends DBObject> clClassData = p_datafield.getClassData();
			int intColumnWidth = p_datafield.getColumnWidth();
			Alignment alignment = p_datafield.getColumnAlignment();

			addColumn(strFieldName, p_strFieldLabel, clClassData, intColumnWidth, ShowInCSV.Yes, alignment);

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public <T extends DataField> void addColumn(T p_datafield, String p_strFieldLabel, int p_intColumnWidth) throws GException {
		try {
		  String strFieldName = p_datafield.getFieldName();
		  Class<? extends DBObject> clClassData = p_datafield.getClassData();
			Alignment alignment = p_datafield.getColumnAlignment();

			addColumn(strFieldName, p_strFieldLabel, clClassData, p_intColumnWidth, ShowInCSV.Yes, alignment);

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public <T extends DataField> void addColumn(T p_datafield, String p_strFieldLabel, int p_intColumnWidth, Alignment p_alignment) throws GException {
		try {
		  String strFieldName = p_datafield.getFieldName();
		  Class<? extends DBObject> clClassData = p_datafield.getClassData();

			addColumn(strFieldName, p_strFieldLabel, clClassData, p_intColumnWidth, ShowInCSV.Yes, p_alignment);

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public <T extends DBObject> void addColumn(String p_strColumnName, Class<T> p_class) throws GException {
		addColumn(p_strColumnName, p_strColumnName, p_class, 0, ShowInCSV.No, Alignment.Left);
	}

	public <T extends DBObject> void addColumn(String p_strColumnName, String p_strColumnLabel, Class<T> p_class) throws GException {
		addColumn(p_strColumnName, p_strColumnLabel, p_class, 0, ShowInCSV.Yes, Alignment.Left);
	}

	public <T extends DBObject> void addColumn(String p_strColumnName, String p_strColumnLabel, Class<T> p_class, int p_intColumnWidth) throws GException {
		if(p_class.getCanonicalName().compareTo(DBDecimal.class.getCanonicalName()) == 0
			|| p_class.getCanonicalName().compareTo(DBMoney.class.getCanonicalName()) == 0
			|| p_class.getCanonicalName().compareTo(DBQuantity.class.getCanonicalName()) == 0) {

			addColumn(p_strColumnName, p_strColumnLabel, p_class, p_intColumnWidth, ShowInCSV.Yes, Alignment.Right);

		} else if(p_class.getCanonicalName().compareTo(DBDate.class.getCanonicalName()) == 0
			|| p_class.getCanonicalName().compareTo(DBDateTime.class.getCanonicalName()) == 0) {

			addColumn(p_strColumnName, p_strColumnLabel, p_class, p_intColumnWidth, ShowInCSV.Yes, Alignment.Center);

		} else if(p_class.getCanonicalName().compareTo(DBBoolean.class.getCanonicalName()) == 0) {
			addColumn(p_strColumnName, p_strColumnLabel, p_class, p_intColumnWidth, ShowInCSV.Yes, Alignment.Center);

		} else {
			addColumn(p_strColumnName, p_strColumnLabel, p_class, p_intColumnWidth, ShowInCSV.Yes, Alignment.Left);
		}
	}

	public <T extends DBObject> void addColumn(String p_strColumnName, String p_strColumnLabel, Class<T> p_class, int p_intColumnWidth,
	 Alignment p_alignment) throws GException {

		addColumn(p_strColumnName, p_strColumnLabel, p_class, p_intColumnWidth, ShowInCSV.Yes, p_alignment);
	}

	public <T extends DBObject> void addColumn(String p_strColumnName, String p_strColumnLabel, Class<T> p_class, int p_intColumnWidth,
		ShowInCSV p_showinCSV, Alignment p_alignment) throws GException {

		try {
			String strColumnName_LowerCase = p_strColumnName.toLowerCase();

			if(m_mapColumnIndex_ColumnName.containsKey(strColumnName_LowerCase)) {
				throw new GException("Duplicate Column Name :" + strColumnName_LowerCase);

			} else {
				if(p_strColumnLabel.length() <= 0) {
					p_strColumnLabel = p_strColumnName;
				}

				TableColumn tableColumn = new TableColumn();
				tableColumn.setHeaderValue(p_strColumnLabel);
				tableColumn.setIdentifier(p_strColumnName);
				tableColumn.setPreferredWidth(p_intColumnWidth);
				tableColumn.setMinWidth(0);
				setCellRenderer(p_class, tableColumn, p_alignment);

				tableColumn.setHeaderRenderer(new HeaderColumnTableCellRenderer());

				if(p_intColumnWidth <= 0) {
					tableColumn.setMaxWidth(0);
				}

				m_intColumnCount = super.getColumnCount();
				m_mapColumnIndex_ColumnName.put(strColumnName_LowerCase, m_intColumnCount);
				m_mapColumnName_ColumnIndex.put(m_intColumnCount, strColumnName_LowerCase);
				m_mapClass_ColumnIndex.put(m_intColumnCount, p_class);
				m_mapTableColumn_ColumnIndex.put(m_intColumnCount, tableColumn);
				m_mapShowInCSV_ColumnIndex.put(m_intColumnCount, p_showinCSV);
				m_mapAlignment_ColumnIndex.put(m_intColumnCount, p_alignment);
				m_lsColumnNameData.add(m_intColumnCount + "->" + strColumnName_LowerCase + ":" + p_class.getSimpleName() + ";");

				super.addColumn(tableColumn);
			}
		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public Map<String, Integer> getMapColumnIndex_ColumnName() throws GException {
		return m_mapColumnIndex_ColumnName;
	}

	public Class<? extends DBObject> getColumnClass(int p_intColumnIndex) throws GException {
		if(m_mapClass_ColumnIndex.containsKey(p_intColumnIndex)) {
			return m_mapClass_ColumnIndex.get(p_intColumnIndex);

		} else {
			throw new GException("Column Index not found!!!");
		}
	}

	public String getColumnName(int p_intColumnIndex) throws GException {
		if(m_mapColumnName_ColumnIndex.containsKey(p_intColumnIndex)) {
			return m_mapColumnName_ColumnIndex.get(p_intColumnIndex);

		} else {
			throw new GException("Column Index not found!!!");
		}
	}

	public int getColumnIndex(String p_strColumnName) throws GException {
		String strColumnName_LowerCase = p_strColumnName.toLowerCase();

		if(m_mapColumnIndex_ColumnName.containsKey(strColumnName_LowerCase)) {
			return m_mapColumnIndex_ColumnName.get(strColumnName_LowerCase);

		} else {
			throw new GException("Column Name(" + p_strColumnName + ") not found!!!");
		}
	}

	public boolean containsColumnName(String p_strColumnName) throws GException {
		String strColumnName_LowerCase = p_strColumnName.toLowerCase();

		if(m_mapColumnIndex_ColumnName.containsKey(strColumnName_LowerCase)) {
			return true;
		}

		return false;
	}

	public TableColumn getTableColumn(int p_intColumnIndex) throws GException {
		return super.getColumn(p_intColumnIndex);
	}

	public List<TableColumn> getTableColumns() throws GException {
		try {
			List<TableColumn> lsTableColumns = new GList<>();

			int intColumnCount = super.getColumnCount();
			int intColumnIndex;

			for(intColumnIndex = 0; intColumnIndex < intColumnCount; intColumnIndex++) {
				TableColumn tableColumn = super.getColumn(intColumnIndex);

				lsTableColumns.add(tableColumn);
			}

			return lsTableColumns;

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public void verifyContainsColumn(String p_strColumnName) throws GException {
		try {
			String strColumnName_LowerCase = p_strColumnName.toLowerCase();

			if(!m_mapColumnIndex_ColumnName.containsKey(strColumnName_LowerCase)) {
				throw new GException("Non-existing column name: " + strColumnName_LowerCase);
			}
		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public DataTableColumnModel clone() throws CloneNotSupportedException {
    return (DataTableColumnModel)super.clone();
  }

	public ShowInCSV getShowInCSV(String p_strColumnName) throws GException {
		try {
			int intColumnIndex = getColumnIndex(p_strColumnName);

			return getShowInCSV(intColumnIndex);

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public ShowInCSV getShowInCSV(int p_intColumnIndex) throws GException {
		try {
			if(m_mapShowInCSV_ColumnIndex.containsKey(p_intColumnIndex)) {
				ShowInCSV showinCSV = m_mapShowInCSV_ColumnIndex.get(p_intColumnIndex);

				return showinCSV;
			}

			throw new GException("Column Number(" + p_intColumnIndex + " not existing!");

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public Alignment getAlignment(int p_intColumnIndex) throws GException {
		try {
			if(m_mapAlignment_ColumnIndex.containsKey(p_intColumnIndex)) {
				Alignment alignment = m_mapAlignment_ColumnIndex.get(p_intColumnIndex);

				return alignment;
			}

			throw new GException("Column Number(" + p_intColumnIndex + " not existing!");

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	private void setCellRenderer(Class<? extends DBObject> p_class, TableColumn p_tableColumn, Alignment p_alignment) throws GException {
		try {
			if(p_alignment == Alignment.Left) {
				p_tableColumn.setCellRenderer(new LeftTableCellRenderer());

			} else if(p_alignment == Alignment.Center) {
				p_tableColumn.setCellRenderer(new CenterTableCellRenderer());

			} else if(p_alignment == Alignment.Right) {
				p_tableColumn.setCellRenderer(new RightTableCellRenderer());
			}

			if(p_class.newInstance() instanceof DBBoolean) {
				p_tableColumn.setCellRenderer(new GBooleanRenderer());
			}
		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public String toString() {
		String strReturn = "";

		for(String strColumnNameData : m_lsColumnNameData) {
			if(strReturn.length() > 0) {
				strReturn += "|";
			}

			strReturn += strColumnNameData;
		}

		return strReturn;
	}

	public int compareTo(DataTableColumnModel p_datatablecolumnmodel) {
		return this.toString().compareTo(p_datatablecolumnmodel.toString());
	}

	private class HeaderColumnTableCellRenderer extends DefaultTableCellHeaderRenderer {
		protected HeaderColumnTableCellRenderer() {
			setHorizontalAlignment(JLabel.CENTER);
		}
	}

	private class LeftTableCellRenderer extends DefaultTableCellRenderer {
		protected LeftTableCellRenderer() {
			setHorizontalAlignment(JLabel.LEFT);
		}
	}

	private class CenterTableCellRenderer extends DefaultTableCellRenderer {
		protected CenterTableCellRenderer() {
			setHorizontalAlignment(JLabel.CENTER);
		}
	}

	private class RightTableCellRenderer extends DefaultTableCellRenderer {
		protected RightTableCellRenderer() {
			setHorizontalAlignment(JLabel.RIGHT);
		}
	}

	private class GBooleanRenderer extends JCheckBox implements TableCellRenderer, UIResource {

		private Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);

		private GBooleanRenderer() {
			setHorizontalAlignment(JLabel.CENTER);
			setBorderPainted(true);
		}

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row,
			int column) {

			if(isSelected) {
				setForeground(table.getSelectionForeground());
				super.setBackground(table.getSelectionBackground());

			} else {
				setForeground(table.getForeground());
				setBackground(table.getBackground());
			}

			setSelected((value != null && ((Boolean)value).booleanValue()));

			if(hasFocus) {
				setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));

			} else {
				setBorder(noFocusBorder);
			}

			return this;
		}
	}

	public enum ShowInCSV {
		Yes,
		No
	}
}
