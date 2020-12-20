package com.laithailibrary.sharelibrary.bean;

import com.laithailibrary.sharelibrary.db.dbobject.*;
import com.laithailibrary.sharelibrary.field.*;
import com.laithailibrary.sharelibrary.support.*;
import exc.*;

/**
 * Created by dumkrerng on 17/5/2558.
 */
public class FieldRecordUpdated extends DataField<DBDateTime> {

	public static final String FieldName = "RecordUpdated";
	public static final Class<DBDateTime> DataClass = DBDateTime.class;

	private static final long serialVersionUID = 1;

	public FieldRecordUpdated() {}

	public FieldRecordUpdated(TableName p_tablename) {
		super(p_tablename, FieldName, DBDateTime.class);

		try {
			setFieldLabel(FieldName);
			setDataSize(50);
			setMandatory(true);
			setUpdateable(false);
			setColumnAlignment(Alignment.Center);
			setIndex(true);

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		}
	}
}
