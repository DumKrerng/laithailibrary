package com.laithailibrary.sharelibrary.util.support;

import com.laithailibrary.sharelibrary.collection.*;
import exc.*;

public class KEYTagPrimaryKey extends GKey {

	private static final long serialVersionUID = 1;

	public KEYTagPrimaryKey(String p_strTableName, String p_strTagPrimaryKey) throws GException {
		super(2);

		try {
			addValue(0, p_strTableName);
			addValue(1, p_strTagPrimaryKey);

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}
}
