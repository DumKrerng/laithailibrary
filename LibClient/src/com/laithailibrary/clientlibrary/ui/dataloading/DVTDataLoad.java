package com.laithailibrary.clientlibrary.ui.dataloading;

import java.util.*;
import com.laithailibrary.sharelibrary.collection.*;
import com.laithailibrary.sharelibrary.datatableview.*;
import com.laithailibrary.sharelibrary.db.dbobject.*;
import com.laithailibrary.sharelibrary.field.*;
import com.laithailibrary.sharelibrary.support.*;
import exc.*;

/**
 * Created by dumkrerng on 29/2/2559.
 */
public class DVTDataLoad extends DataVectorTable {

	public static final TableName TN = new TableName("DVTDataLoad");

	private static final long serialVersionUID = 1;

	public DVTDataLoad(List<ColumnDataLoad> p_lsColumnDataLoads) throws GException {
		super(new DataLoadColumnModel(p_lsColumnDataLoads), new GList<>("LineNumber"), new GList<>("LineNumber"));
	}

	public static class DataLoadColumnModel extends DataTableColumnModel {

		public static final DataField<DBInteger> LineNumber = new DataField<>(TN, "LineNumber", DBInteger.class);

		private static final long serialVersionUID = 1;

		public DataLoadColumnModel(List<ColumnDataLoad> p_lsColumnDataLoads) throws GException {
			try {
				addColumn(LineNumber, "Line #", 60, Alignment.Center);

				for(ColumnDataLoad columnmodel : p_lsColumnDataLoads) {
					String strFieldName = columnmodel.getDataField().getFieldName();
					String strFieldLabel = columnmodel.getDataField().getFieldLabel();

					addColumn(strFieldName, strFieldLabel, DBString.class, 150);
				}
			} catch(Exception exception) {
			  ExceptionHandler.display(exception);
			  throw new GException(exception);
			}
		}
	}
}
