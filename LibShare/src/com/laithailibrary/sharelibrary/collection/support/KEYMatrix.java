package com.laithailibrary.sharelibrary.collection.support;

import com.laithailibrary.sharelibrary.collection.*;
import com.laithailibrary.sharelibrary.db.dbobject.*;
import exc.*;

/**
 * Created by dumkrerng on 29/5/2559.
 */
public class KEYMatrix extends GKey {

	private static final long serialVersionUID = 1;

	public KEYMatrix(int p_intRowIndex, int p_intColumnIndex) throws GException {
		super(2);

		try {
			addValue(0, new DBInteger(p_intRowIndex));
			addValue(1, new DBInteger(p_intColumnIndex));

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public int getRowIndex() throws GException {
		return ((DBInteger)super.getValue(0)).getIntValue();
	}

	public int getColumnIndex() throws GException {
		return ((DBInteger)super.getValue(1)).getIntValue();
	}
}
