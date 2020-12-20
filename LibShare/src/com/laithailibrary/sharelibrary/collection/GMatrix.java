package com.laithailibrary.sharelibrary.collection;

import java.io.*;
import java.util.*;
import com.laithailibrary.sharelibrary.collection.support.*;
import com.laithailibrary.sharelibrary.datatableview.*;
import com.laithailibrary.sharelibrary.db.dbobject.*;
import exc.*;
import org.com.matrix.*;

/**
 * Created by dumkrerng on 30/4/2559.
 */
public class GMatrix<T extends DBObject> implements Externalizable {

	private GMatrix<T> me;

	private Class<T> m_clsClassOfValue = null;
	private GMap<KEYMatrix, T> m_mapValue_KEYMatrix = null;
	private List<String> m_lsColumnNames = null;
	private GMap<Integer, String> m_mapColumnName_ColumnIndex = null;
	private GMap<String, Integer> m_mapColumnIndex_ColumnName = null;

	private static final long serialVersionUID = 1;

	public GMatrix(Class<T> p_clsClassOfValue, List<String> p_lsColumnNames) throws GException {
		try {
			me = this;

			m_clsClassOfValue = p_clsClassOfValue;
			m_mapValue_KEYMatrix = new GMap<>();

			m_mapColumnName_ColumnIndex = new GMap<>();
			m_mapColumnIndex_ColumnName = new GMap<>();

			int intSize_ColumnName = p_lsColumnNames.size();

			for(int index = 0; index < intSize_ColumnName; index++) {
				String strColumnName = p_lsColumnNames.get(index);

				if(m_mapColumnIndex_ColumnName.containsKey(strColumnName)) {
					throw new GException("Duplicate ColumnName: " + strColumnName);
				}

				m_mapColumnIndex_ColumnName.put(strColumnName, index);
				m_mapColumnName_ColumnIndex.put(index, strColumnName);
			}

			m_lsColumnNames = p_lsColumnNames;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public void writeExternal(ObjectOutput out) throws IOException {
		try {
			out.writeObject(m_clsClassOfValue);
			out.writeObject(m_mapValue_KEYMatrix);
			out.writeObject(m_lsColumnNames);
			out.writeObject(m_mapColumnName_ColumnIndex);
			out.writeObject(m_mapColumnIndex_ColumnName);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			out.close();

			throw new IOException(exception);
		}
	}

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		try {
			m_clsClassOfValue = (Class<T>)in.readObject();
			m_mapValue_KEYMatrix = (GMap<KEYMatrix, T>)in.readObject();
			m_lsColumnNames = (List<String>)in.readObject();
			m_mapColumnName_ColumnIndex = (GMap<Integer, String>)in.readObject();
			m_mapColumnIndex_ColumnName = (GMap<String, Integer>)in.readObject();

		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			in.close();

			throw new IOException(exception);
		}
	}

	public void setValue(int p_intRowIndex, String p_strColumnName, T p_value) throws GException {
		try {
			Integer intColumnIndex = m_mapColumnIndex_ColumnName.get(p_strColumnName);

			if(intColumnIndex == null) {
				throw new GException("ColumnName(" + p_strColumnName + ") not found!!!");
			}

			setValue(p_intRowIndex, intColumnIndex, p_value);

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public void setValue(int p_intRowIndex, int p_intColumnIndex, T p_value) throws GException {
		try {
			KEYMatrix keyMatrix = new KEYMatrix(p_intRowIndex, p_intColumnIndex);

			setValue(keyMatrix, p_value);

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public void setValue(KEYMatrix p_keyMatrix, T p_value) throws GException {
		try {
			m_mapValue_KEYMatrix.put(p_keyMatrix, p_value);

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public T getValue(int p_intRowIndex, String p_strColumnName) throws GException {
		try {
			Integer intColumnIndex = m_mapColumnIndex_ColumnName.get(p_strColumnName);

			if(intColumnIndex == null) {
				throw new GException("ColumnName(" + p_strColumnName + ") not found!!!");
			}

			return getValue(p_intRowIndex, intColumnIndex);

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public T getValue(int p_intRowIndex, int p_intColumnIndex) throws GException {
		try {
			KEYMatrix keyMatrix = new KEYMatrix(p_intRowIndex, p_intColumnIndex);

			return getValue(keyMatrix);

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public T getValue(KEYMatrix p_keyMatrix) throws GException {
		try {
			T value;

			if(m_mapValue_KEYMatrix.containsKey(p_keyMatrix)) {
				value = m_mapValue_KEYMatrix.get(p_keyMatrix);

			} else {
				value = m_clsClassOfValue.newInstance();
			}

			return value;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public Set<KEYMatrix> getKEYMatrixs() throws GException {
		try {
		  return m_mapValue_KEYMatrix.keySet();

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public DataVectorTable getDVT() throws GException {
		return getDVT(150, new GList<String>());
	}

	public DataVectorTable getDVT(int p_intColumnWidth) throws GException {
		return getDVT(p_intColumnWidth, new GList<String>());
	}

	public DataVectorTable getDVT(List<String> p_lsSortKeys) throws GException {
		return getDVT(150, p_lsSortKeys);
	}

	public DataVectorTable getDVT(int p_intColumnWidth, List<String> p_lsSortKeys) throws GException {
		try {
			if(m_lsColumnNames == null) {
				throw new GException("Invalid Column Name!!!");
			}

			if(m_lsColumnNames.size() <= 0) {
				throw new GException("Invalid Column Name!!!");
			}

			DataTableColumnModel dcmColumn = new DataTableColumnModel();
			dcmColumn.addColumn("RowID", DBInteger.class);

			for(String strColumnName : m_lsColumnNames) {
				dcmColumn.addColumn(strColumnName, strColumnName, m_clsClassOfValue, p_intColumnWidth);
			}

			List<String> lsPrimaryKeys = new GList<>();
			lsPrimaryKeys.add("RowID");

			DataVectorTable dvtReturn = new DataVectorTable(dcmColumn, lsPrimaryKeys, p_lsSortKeys);

			List<KEYMatrix> lsKEYMatrixs = new GList<>(m_mapValue_KEYMatrix.keySet());
			KEYMatrix keyMatrix_Last = lsKEYMatrixs.get(lsKEYMatrixs.size() - 1);

			int intRowIndex_Max = keyMatrix_Last.getRowIndex();

			for(int index = 0; index <= intRowIndex_Max; index++) {
				DataVectorRow dvrRow = new DataVectorRow(dcmColumn);
				dvrRow.setData("RowID", new DBInteger(index));

				for(String strColumnName : m_lsColumnNames) {
					Integer intColumnIndex = m_mapColumnIndex_ColumnName.get(strColumnName);

					KEYMatrix keyMatrix = new KEYMatrix(index, intColumnIndex);
					T dboValue = m_clsClassOfValue.newInstance();

					if(m_mapValue_KEYMatrix.containsKey(keyMatrix)) {
						dboValue = m_mapValue_KEYMatrix.get(keyMatrix);
					}

					dvrRow.setData(strColumnName, dboValue);
				}

				dvtReturn.addDataRow(dvrRow);
			}

			return dvtReturn;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public int getNumberOfColumn() throws GException {
		try {
			int intSize = m_lsColumnNames.size();

			return intSize;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public int getNumberOfRow() throws GException {
		try {
			List<KEYMatrix> lsKEYMatrixs = new GList<>(m_mapValue_KEYMatrix.keySet());
			KEYMatrix keyMatrix_Last = lsKEYMatrixs.get(lsKEYMatrixs.size() - 1);

			int intRowIndex_Max = keyMatrix_Last.getRowIndex();
			intRowIndex_Max++;

			return intRowIndex_Max;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public GMatrix<T> getSubMatrix(int p_intRowIndex_Begin, int p_intRowIndex_End, List<String> p_lsColumnName) throws GException {
		try {
			if(p_intRowIndex_End < p_intRowIndex_Begin) {
				throw new GException("Matrix out of index!!!");
			}

			GMatrix<T> matrix_Return = new GMatrix<>(m_clsClassOfValue, p_lsColumnName);

			List<Integer> lsRowIndex = new GList<>();

			int index = p_intRowIndex_Begin;

			do{
				lsRowIndex.add(index);

				index++;

			} while(index <= p_intRowIndex_End);

			for(int i=0; i < lsRowIndex.size(); i++) {
				int intRowIndex = lsRowIndex.get(i);

				for(String strColumnName : p_lsColumnName) {
					if(m_mapColumnIndex_ColumnName.containsKey(strColumnName)) {
						int intColumnIndex = m_mapColumnIndex_ColumnName.get(strColumnName);
						T dbobjValue = getValue(intRowIndex, intColumnIndex);

						matrix_Return.setValue(i, intColumnIndex, dbobjValue);

					} else {
						throw new GException("Column Name(" + strColumnName + ") not found!!!");
					}
				}
			}

			return matrix_Return;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public GMatrix<T> inverse() throws GException {
		try {
			int intSize = m_lsColumnNames.size();

			double dubValue[][] = new double[intSize][intSize];

			for(int rowindex=0; rowindex < intSize; rowindex++) {
				for(int colindex=0; colindex < intSize; colindex++) {
					String strValue = getValue(rowindex, colindex).getString();

					dubValue[rowindex][colindex] = Double.valueOf(strValue);
				}
			}

			dubValue = Inverse.invert(dubValue);

			GMatrix<T> matrix = new GMatrix<>(m_clsClassOfValue, m_lsColumnNames);

			for(int rowindex=0; rowindex < intSize; rowindex++) {
				for(int colindex=0; colindex < intSize; colindex++) {
					Double dubTemp = dubValue[rowindex][colindex];

					T dboValue = m_clsClassOfValue.newInstance();
					dboValue.setStringValue(dubTemp.toString());

					matrix.setValue(rowindex, colindex, dboValue);
				}
			}

			return matrix;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public GMatrix<T> multiply(GMatrix<T> p_matrix) throws GException {
		try {
			verifyOnMultiply(p_matrix);

			GMatrix<T> matrix_C = MatrixMultiplication.multiply(m_clsClassOfValue, me, p_matrix);

			return matrix_C;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	private void verifyOnMultiply(GMatrix<T> p_matrix) throws GException {
		try {
		  int intNumberOfColumn_A = me.getNumberOfColumn();
			int intNumberOfRow_B = p_matrix.getNumberOfRow();

			if(intNumberOfColumn_A != intNumberOfRow_B) {
				throw new GException("Matrix Multiplication Format Error!!!");
			}
		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}
}
