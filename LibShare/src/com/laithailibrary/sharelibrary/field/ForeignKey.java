package com.laithailibrary.sharelibrary.field;

import com.laithailibrary.sharelibrary.collection.GMap;
import com.laithailibrary.sharelibrary.collection.GSet;
import exc.*;
import exc.ExceptionHandler;

import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: dumkrerng
 * Date: 2/22/12
 * Time: 12:08 AM
 * To change this template use File | Settings | File Templates.
 */
public class ForeignKey {

	private static Map<FieldPrimaryKey, Set<FieldForeignKey>> sMapFieldForeignKeys_FieldPrimaryKey = new GMap<>();

	public ForeignKey() {}

	public ForeignKey(FieldForeignKey p_fieldForeignKey) {
		try {
			Set<FieldPrimaryKey> setFieldReferentPrimaryKey = p_fieldForeignKey.getFieldReferentPrimaryKeys();

			for(FieldPrimaryKey fieldPrimaryKey : setFieldReferentPrimaryKey) {
				Set<FieldForeignKey> setFieldForeignKey;

				if(sMapFieldForeignKeys_FieldPrimaryKey.containsKey(fieldPrimaryKey)) {
					setFieldForeignKey = sMapFieldForeignKeys_FieldPrimaryKey.get(fieldPrimaryKey);

				} else {
					setFieldForeignKey = new GSet<FieldForeignKey>();
				}

				setFieldForeignKey.add(p_fieldForeignKey);
				sMapFieldForeignKeys_FieldPrimaryKey.put(fieldPrimaryKey, setFieldForeignKey);
			}
		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		}
	}

	public static Set<FieldForeignKey> getFieldForeignKeys(FieldPrimaryKey p_fieldPrimaryKey) throws GException {
		try {
			Set<FieldForeignKey> setFieldForeignKey = new GSet<FieldForeignKey>();

			if(sMapFieldForeignKeys_FieldPrimaryKey.containsKey(p_fieldPrimaryKey)) {
				setFieldForeignKey = sMapFieldForeignKeys_FieldPrimaryKey.get(p_fieldPrimaryKey);
			}

			return setFieldForeignKey;

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public static Map<FieldPrimaryKey, Set<FieldForeignKey>> getMapForeignKeys() {
		return sMapFieldForeignKeys_FieldPrimaryKey;
	}
}
