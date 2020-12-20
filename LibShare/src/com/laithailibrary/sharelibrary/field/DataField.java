package com.laithailibrary.sharelibrary.field;

import java.io.*;
import com.laithailibrary.logger.*;
import com.laithailibrary.sharelibrary.db.dbobject.*;
import com.laithailibrary.sharelibrary.support.*;
import com.laithailibrary.sharelibrary.util.*;
import exc.*;
import pp.*;

/**
 * Created by IntelliJ IDEA.
 * User: dumkrerng
 * Date: 2/24/12
 * Time: 1:21 AM
 * To change this template use File | Settings | File Templates.
 */

public class DataField<T extends DBObject> implements Comparable<DataField>, Externalizable, Serializable {

	private TableName m_tablename;
	private String m_strFieldName = "";
	private String m_classname;

	private String m_strFieldLabel = "";
	private int m_columnwidth = 0;
	private Alignment m_columnalignment = Alignment.Left;
	private int m_intDataSize = 0;
	private boolean m_bolMandatory = false;
	private boolean m_bolUpdateable = true;
	private boolean m_bolIndex = false;

	public static final int DataSize_PrimaryKey = TableIDUtilities.SIZE_PrimaryKeyID;
	public static final int DataSize_DocumentUSERID = 50;
	public static final int DataSize_USERID = 50;
	public static final int DataSize_Name = 150;
	public static final int DataSize_Note = 1024;

	private static final long serialVersionUID = 10;

	public DataField() {}

	public DataField(TableName p_tablename, String p_strFieldName, Class<T> p_classData) {
		init(p_tablename, p_strFieldName, p_classData);
	}

	private void init(TableName p_tablename, String p_strFieldName, Class<T> p_classData) {
		try {
			if(p_tablename.length() <= 0) {
				GLog.severe("Invalid TableName!");

				System.exit(ProgramExit.InvalidTableName);
			}

			if(p_strFieldName.length() <= 0) {
				GLog.severe("Invalid FieldName!");

				System.exit(ProgramExit.InvalidFieldName);
			}

			m_tablename = p_tablename;
			m_strFieldName = p_strFieldName;
			m_strFieldLabel = p_strFieldName;
			m_classname = p_classData.getName();

			if(p_classData.getCanonicalName().compareTo(DBDecimal.class.getCanonicalName()) == 0
				|| p_classData.getCanonicalName().compareTo(DBMoney.class.getCanonicalName()) == 0
				|| p_classData.getCanonicalName().compareTo(DBQuantity.class.getCanonicalName()) == 0) {

				m_columnalignment = Alignment.Right;
			}
		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		}
	}

	public void writeExternal(ObjectOutput out) throws IOException {
		try {
			out.writeObject(m_tablename);
			out.writeUTF(m_strFieldName);
			out.writeUTF(m_classname);
			out.writeUTF(m_strFieldLabel);
			out.writeInt(m_columnwidth);
			out.writeObject(m_columnalignment);
			out.writeInt(m_intDataSize);
			out.writeBoolean(m_bolMandatory);
			out.writeBoolean(m_bolUpdateable);
			out.writeBoolean(m_bolIndex);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			out.close();

			throw new IOException(exception);
		}
	}

	public void readExternal(ObjectInput in) throws IOException {
		try {
			m_tablename = (TableName)in.readObject();
			m_strFieldName = in.readUTF();
			m_classname = in.readUTF();
			m_strFieldLabel = in.readUTF();
			m_columnwidth = in.readInt();
			m_columnalignment = (Alignment)in.readObject();
			m_intDataSize = in.readInt();
			m_bolMandatory = in.readBoolean();
			m_bolUpdateable = in.readBoolean();
			m_bolIndex = in.readBoolean();

		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			in.close();

			throw new IOException(exception);
		}
	}

	public TableName getTableName() {
		return m_tablename;
	}

	public String getFieldName() {
		return m_strFieldName;
	}

	public void setFieldLabel(String p_strFieldLabel) {
		m_strFieldLabel = p_strFieldLabel;
	}

	public String getFieldLabel() {
		return m_strFieldLabel;
	}

	public void setColumnWidth(int p_intColumnWidth) {
		m_columnwidth = p_intColumnWidth;
	}

	public int getColumnWidth() {
		return m_columnwidth;
	}

	public void setColumnAlignment(Alignment p_columnalignment) {
		m_columnalignment = p_columnalignment;
	}

	public Alignment getColumnAlignment() {
		return m_columnalignment;
	}

	public void setDataSize(int p_intDataSize) {
		m_intDataSize = p_intDataSize;
	}

	public int getDataSize() {
		return m_intDataSize;
	}

	public void setMandatory(boolean p_bolMandatory) {
		m_bolMandatory = p_bolMandatory;
	}

	public boolean isMandatory() {
		return m_bolMandatory;
	}

	public void setUpdateable(boolean p_bolUpdateable) {
		m_bolUpdateable = p_bolUpdateable;
	}

	public boolean isUpdateable() {
		return m_bolUpdateable;
	}

	public void setIndex(boolean p_bolIndex) {
		m_bolIndex = p_bolIndex;
	}

	public boolean isIndex() {
		return m_bolIndex;
	}

	public Class<T> getClassData() throws GException {
		try {
			return (Class<T>)Class.forName(m_classname);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public String toString() {
		return m_tablename.getTableName().concat(".").concat(m_strFieldName).concat(":").concat(m_classname);
	}

	public int compareTo(DataField p_datafield) {
		return this.toString().compareTo(p_datafield.toString());
	}



	/*
	*
	*
	String FieldName();
	String FieldLabel() default "";
	int ColumnWidth() default 0;
	Alignment ColumnAlignment() default Alignment.Left;
	int DataSize() default  0;
	boolean Mandatory() default false;
	boolean Updateable() default true;
	boolean Index() default false;v
	*
	* */

}
