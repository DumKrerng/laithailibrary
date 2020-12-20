package com.laithailibrary.sharelibrary.collection.support;

import com.laithailibrary.sharelibrary.collection.*;
import com.laithailibrary.sharelibrary.db.dbobject.*;
import exc.*;
import java.util.*;

/**
 * Created by dumkrerng on 29/5/2559.
 */
public class MatrixMultiplication {

	private MatrixMultiplication() {}

	public static<T extends DBObject> GMatrix<T> multiply(Class<T> p_clsClassOfValue, GMatrix<T> p_matrix_A, GMatrix<T> p_matrix_B) throws GException {
		try {
			int intNumberOfRow_A = p_matrix_A.getNumberOfRow();
			int intNumberOfColumn_A = p_matrix_A.getNumberOfColumn();

			int intNumberOfColumn_B = p_matrix_B.getNumberOfColumn();

			GList<String> m_lsColumnNames = new GList<>(intNumberOfColumn_B);

			for(int i=0; i < intNumberOfColumn_B; i++) {
				m_lsColumnNames.add(Integer.toString(i));
			}

			GMatrix<T> matrix_C = new GMatrix<>(p_clsClassOfValue, m_lsColumnNames);

			for(int rowindex=0; rowindex < intNumberOfRow_A; rowindex++) {
				for(int colindex=0; colindex < intNumberOfColumn_B; colindex++) {
					T dboValue = p_clsClassOfValue.newInstance();
					dboValue.setStringValue("0.0");

					matrix_C.setValue(rowindex, colindex, dboValue);
				}
			}

			Set<KEYMatrix> setKEYMatrixs = matrix_C.getKEYMatrixs();

			for(KEYMatrix keyMatrix : setKEYMatrixs) {
				int i = keyMatrix.getRowIndex();
				int j = keyMatrix.getColumnIndex();

				DBDecimal dbdcTotalValue = new DBDecimal(0.0);

				for(int n=0; n < intNumberOfColumn_A; n++) {
					T dboValue_A = p_matrix_A.getValue(i, n);
					T dboValue_B = p_matrix_B.getValue(n, j);

					DBDecimal dbdcValue_A = new DBDecimal(dboValue_A.getString());
					DBDecimal dbdcValue_B = new DBDecimal(dboValue_B.getString());

					dbdcValue_A = dbdcValue_A.multiply(dbdcValue_B);

					dbdcTotalValue = dbdcTotalValue.add(dbdcValue_A);
				}

				T dboValue = p_clsClassOfValue.newInstance();
				dboValue.setStringValue(dbdcTotalValue.getString());

				matrix_C.setValue(keyMatrix, dboValue);
			}

			return matrix_C;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}
}
