package com.laithailibrary.sharelibrary.sqlstatement;

import com.laithailibrary.sharelibrary.collection.*;
import com.laithailibrary.sharelibrary.db.dbobject.*;
import com.laithailibrary.sharelibrary.field.*;
import com.laithailibrary.sharelibrary.sqlstatement.support.*;
import exc.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: dumkrerng
 * Date: 6/19/12
 * Time: 12:04 AM
 * To change this template use File | Settings | File Templates.
 */
public class Where {

	private Where() {}

	public static <T extends DBObject, U extends DataField<T>> SQLStatement equal(U p_datafield, T p_value) throws GException {
		return equal(p_datafield, p_value, "");
	}

	public static <T extends DBObject, U extends DataField<T>> SQLStatement equal(U p_datafield, T p_value, String p_strTag) throws GException {
		try {
			SQLStatement sqlStatement = new SQLStatementData();
			sqlStatement.setSQLStatementString(new SQLStatementString<>(SQLOperator.Equal, p_datafield, p_value, p_strTag));

			return sqlStatement;

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public static <T extends DBObject, U extends DataField<T>> SQLStatement notEqual(U p_datafield, T p_value) throws GException {
		return notEqual(p_datafield, p_value, "");
	}

	public static <T extends DBObject, U extends DataField<T>> SQLStatement notEqual(U p_datafield, T p_value, String p_strTag) throws GException {
		try {
			SQLStatement sqlStatement = new SQLStatementData(new SQLStatementString<>(SQLOperator.NotEqual, p_datafield, p_value,
				p_strTag));

			return sqlStatement;

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public static <T extends DBObject, U extends DataField<T>> SQLStatement isNotNull(U p_datafield) throws GException {
		return isNotNull(p_datafield,  "");
	}

	public static <T extends DBObject, U extends DataField<T>> SQLStatement isNotNull(U p_datafield, String p_strTag) throws GException {
		try {
			SQLStatement sqlStatement = new SQLStatementData(new SQLStatementString<>(SQLOperator.IsNotNull, p_datafield, p_datafield.getClassData().newInstance(),
				p_strTag));

			return sqlStatement;

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public static <T extends DBObject, U extends DataField<T>> SQLStatement isNull(U p_datafield) throws GException {
		return isNull(p_datafield, "");
	}

	public static <T extends DBObject, U extends DataField<T>> SQLStatement isNull(U p_datafield, String p_strTag) throws GException {
		try {
			SQLStatement sqlStatement = new SQLStatementData(new SQLStatementString<>(SQLOperator.IsNull, p_datafield, p_datafield.getClassData().newInstance(),
				p_strTag));

			return sqlStatement;

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public static <T extends DBObject, U extends DataField<T>> SQLStatement greaterThan(U p_datafield, T p_value) throws GException {
		return greaterThan(p_datafield, p_value, "");
	}

	public static <T extends DBObject, U extends DataField<T>> SQLStatement greaterThan(U p_datafield, T p_value, String p_strTag) throws GException {
		try {
			SQLStatement sqlStatement = new SQLStatementData(new SQLStatementString<>(SQLOperator.GreaterThan, p_datafield, p_value,
				p_strTag));

			return sqlStatement;

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public static <T extends DBObject, U extends DataField<T>> SQLStatement greaterThanOrEqualTo(U p_datafield, T p_value) throws GException {
		return greaterThanOrEqualTo(p_datafield, p_value, "");
	}

	public static <T extends DBObject, U extends DataField<T>> SQLStatement greaterThanOrEqualTo(U p_datafield, T p_value, String p_strTag) throws GException {
		try {
			SQLStatement sqlStatement = new SQLStatementData(new SQLStatementString<>(SQLOperator.GreaterThanOrEqualTo, p_datafield,
				p_value, p_strTag));

			return sqlStatement;

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public static <T extends DBObject, U extends DataField<T>> SQLStatement lesserThan(U p_datafield, T p_value) throws GException {
		return lesserThan(p_datafield, p_value, "");
	}

	public static <T extends DBObject, U extends DataField<T>> SQLStatement lesserThan(U p_datafield, T p_value, String p_strTag) throws GException {
		try {
			SQLStatement sqlStatement = new SQLStatementData(new SQLStatementString<>(SQLOperator.LessThan, p_datafield, p_value,
				p_strTag));

			return sqlStatement;

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public static <T extends DBObject, U extends DataField<T>> SQLStatement lesserThanOrEqualTo(U p_datafield, T p_value) throws GException {
		return lesserThanOrEqualTo(p_datafield, p_value, "");
	}

	public static <T extends DBObject, U extends DataField<T>> SQLStatement lesserThanOrEqualTo(U p_datafield, T p_value, String p_strTag) throws GException {
		try {
			SQLStatement sqlStatement = new SQLStatementData(new SQLStatementString<>(SQLOperator.LessThanOrEqualTo, p_datafield,
				p_value, p_strTag));

			return sqlStatement;

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public static <T extends DBObject, U extends DataField<T>> SQLStatement in(U p_datafield, GDBObjectList<T> p_objectlist,
		String p_strTag) throws GException {

		return in(p_datafield, p_objectlist.getList(), p_strTag);
	}

	public static <T extends DBObject, U extends DataField<T>> SQLStatement in(U p_datafield, GDBObjectList<T> p_objectlist) throws GException {
		return in(p_datafield, p_objectlist.getList(), "");
	}

	public static <T extends DBObject, U extends DataField<T>> SQLStatement in(U p_datafield, Collection<T> p_colValues) throws GException {
		return in(p_datafield, p_colValues, "");
	}

	public static <T extends DBObject, U extends DataField<T>> SQLStatement in(U p_datafield, Collection<T> p_colValues, String p_strTag) throws GException {
		try {
			if(p_colValues.size() > 0) {
				SQLStatement sqlStatement = new SQLStatementData(new SQLStatementString<>(SQLOperator.In, p_datafield, p_colValues,
					p_strTag));

				return sqlStatement;
			}

			return SQLStatement.EmptyStatement;

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public static <T extends DBObject, U extends DataField<T>> SQLStatement inWithNull(U p_datafield, GDBObjectList<T> p_objectlist,
		String p_strTag) throws GException {

		return inWithNull(p_datafield, p_objectlist.getList(), p_strTag);
	}

	public static <T extends DBObject, U extends DataField<T>> SQLStatement inWithNull(U p_datafield, GDBObjectList<T> p_objectlist) throws GException {
		return inWithNull(p_datafield, p_objectlist.getList(), "");
	}

	public static <T extends DBObject, U extends DataField<T>> SQLStatement inWithNull(U p_datafield, Collection<T> p_colValues,
		String p_strTag) throws GException {

		try {
			if(p_colValues.size() > 0) {
				SQLStatement sqlStatement = new SQLStatementData(new SQLStatementString<>(SQLOperator.InWithNull, p_datafield, p_colValues,
					p_strTag));

				return sqlStatement;
			}

			return SQLStatement.EmptyStatement;

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public static <T extends DBObject, U extends DataField<T>> SQLStatement notIn(U p_datafield, GDBObjectList<T> p_objectlist,
		String p_strTag) throws GException {

		return notIn(p_datafield, p_objectlist.getList(), p_strTag);
	}

	public static <T extends DBObject, U extends DataField<T>> SQLStatement notIn(U p_datafield, GDBObjectList<T> p_objectlist) throws GException {
		return notIn(p_datafield, p_objectlist.getList(), "");
	}

	public static <T extends DBObject, U extends DataField<T>> SQLStatement notIn(U p_datafield, Collection<T> p_colValues) throws GException {
		return notIn(p_datafield, p_colValues, "");
	}

	public static <T extends DBObject, U extends DataField<T>> SQLStatement notIn(U p_datafield, Collection<T> p_colValues,
		String p_strTag) throws GException {

		try {
			if(p_colValues.size() > 0) {
				SQLStatement sqlStatement = new SQLStatementData(new SQLStatementString<>(SQLOperator.NotIn, p_datafield, p_colValues,
					p_strTag));

				return sqlStatement;
			}

			return SQLStatement.EmptyStatement;

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public static <T extends DBObject, U extends DataField<T>> SQLStatement notInWithNull(U p_datafield, Collection<T> p_colValues) throws GException {
		return notInWithNull(p_datafield, p_colValues, "");
	}

	public static <T extends DBObject, U extends DataField<T>> SQLStatement notInWithNull(U p_datafield, Collection<T> p_colValues,
		String p_strTag) throws GException {

		try {
			if(p_colValues.size() > 0) {
				SQLStatement sqlStatement = new SQLStatementData(new SQLStatementString<>(SQLOperator.NotInWithNull, p_datafield,
					p_colValues, p_strTag));

				return sqlStatement;
			}

			return SQLStatement.EmptyStatement;

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public static <T extends DBObject, U extends DataField<T>> SQLStatement like2(U p_datafield, T p_value) throws GException {
		return like2(p_datafield, p_value, "");
	}

	public static <T extends DBObject, U extends DataField<T>> SQLStatement like2(U p_datafield, T p_value, String p_strTag) throws GException {
		try {
			SQLStatement sqlStatement = new SQLStatementData();
			sqlStatement.setSQLStatementString(new SQLStatementString<>(SQLOperator.Like2, p_datafield, p_value, p_strTag));

			return sqlStatement;

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public static <T extends DBObject, U extends DataField<T>> SQLStatement startWith(U p_datafield, T p_value) throws GException {
		return startWith(p_datafield, p_value, "");
	}

	public static <T extends DBObject, U extends DataField<T>> SQLStatement startWith(U p_datafield, T p_value, String p_strTag) throws GException {
		try {
			SQLStatement sqlStatement = new SQLStatementData();
			sqlStatement.setSQLStatementString(new SQLStatementString<>(SQLOperator.StartWith, p_datafield, p_value, p_strTag));

			return sqlStatement;

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public static <T extends DBObject, U extends DataField<T>> SQLStatement like2_IgnoreCase(U p_datafield, T p_value) throws GException {
		return like2_IgnoreCase(p_datafield, p_value, "");
	}

	public static <T extends DBObject, U extends DataField<T>> SQLStatement like2_IgnoreCase(U p_datafield, T p_value, String p_strTag) throws GException {
		try {
			SQLStatement sqlStatement = new SQLStatementData();
			sqlStatement.setSQLStatementString(new SQLStatementString<>(SQLOperator.Like2_IgnoreCase, p_datafield, p_value, p_strTag));

			return sqlStatement;

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public static <T extends DBObject, U extends DataField<T>> SQLStatement equal_IgnoreCase(U p_datafield, T p_value) throws GException {
		return equal_IgnoreCase(p_datafield, p_value, "");
	}

	public static <T extends DBObject, U extends DataField<T>> SQLStatement equal_IgnoreCase(U p_datafield, T p_value, String p_strTag) throws GException {
		try {
			SQLStatement sqlStatement = new SQLStatementData();
			sqlStatement.setSQLStatementString(new SQLStatementString<>(SQLOperator.Equal_IgnoreCase, p_datafield, p_value, p_strTag));

			return sqlStatement;

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public static <T extends DBObject, U extends DataField<T>> SQLStatement endWith(U p_datafield, T p_value) throws GException {
		return endWith(p_datafield, p_value, "");
	}

	public static <T extends DBObject, U extends DataField<T>> SQLStatement endWith(U p_datafield, T p_value, String p_strTag) throws GException {
		try {
			SQLStatement sqlStatement = new SQLStatementData();
			sqlStatement.setSQLStatementString(new SQLStatementString<>(SQLOperator.EndWith, p_datafield, p_value, p_strTag));

			return sqlStatement;

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}
}
