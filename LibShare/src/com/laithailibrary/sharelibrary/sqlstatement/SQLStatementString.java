package com.laithailibrary.sharelibrary.sqlstatement;

import java.io.*;
import java.util.*;
import com.laithailibrary.sharelibrary.collection.*;
import com.laithailibrary.sharelibrary.db.dbobject.*;
import com.laithailibrary.sharelibrary.db.dbutilities.*;
import com.laithailibrary.sharelibrary.field.*;
import com.laithailibrary.sharelibrary.sqlstatement.support.*;
import exc.*;

/**
 * Created with IntelliJ IDEA.
 * User: dumkrerng
 * Date: 6/19/12
 * Time: 11:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class SQLStatementString<T extends DBObject, U extends DataField<T>> implements Externalizable, Comparable<SQLStatementString> {

	private SQLOperator m_sqlOperator = SQLOperator.NullOperator;

	private Collection<? extends DBObject> m_collection = new GList<>();
	private String m_strTag = "";

	private U m_datafield;
	private T m_dbObjectValue;

	private static final long serialVersionUID = 11;

	public SQLStatementString() {}

	public void writeExternal(ObjectOutput out) throws IOException {
		try {
			out.writeObject(m_sqlOperator);
			out.writeObject(m_collection);
			out.writeUTF(m_strTag);
			out.writeObject(m_datafield);
			out.writeObject(m_dbObjectValue);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			out.close();

			throw new IOException(exception);
		}
	}

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		try {
			m_sqlOperator = (SQLOperator)in.readObject();
			m_collection = (Collection<? extends DBObject>)in.readObject();
			m_strTag = in.readUTF();
			m_datafield = (U)in.readObject();
			m_dbObjectValue = (T)in.readObject();

		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			in.close();

			throw new IOException(exception);
		}
	}

	public SQLStatementString(SQLOperator p_sqlOperator, U p_datafield, T p_dbObjectValue, String p_strTag) {
		m_strTag = p_strTag;

		init(p_sqlOperator, p_datafield, p_dbObjectValue);
	}

	public SQLStatementString(SQLOperator p_sqlOperator, U p_datafield, T p_dbObjectValue) {
		init(p_sqlOperator, p_datafield, p_dbObjectValue);
	}

	public SQLStatementString(SQLOperator p_sqlOperator, U p_datafield, Collection<T> p_collection, String p_strTag) {
		try {
			if(p_datafield == null) {
				throw new GException("Invalid DataField!");
			}

			init(p_sqlOperator, p_datafield, null);

			m_collection = p_collection;
			m_strTag = p_strTag;

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
		}
	}

	private void init(SQLOperator p_sqlOperator, U p_datafield, T p_dbObjectValue) {
		try {
			m_sqlOperator = p_sqlOperator;
			m_datafield = p_datafield;

			if(p_dbObjectValue == null) {
				p_dbObjectValue = m_datafield.getClassData().newInstance();
			}

			m_dbObjectValue = p_dbObjectValue;

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		}
	}

	public U getDataField() {
		return m_datafield;
	}

	public T getDBObjectValue() {
		return m_dbObjectValue;
	}

	public Collection<? extends DBObject> getDBObjectValues() {
		return m_collection;
	}

	public String getSQLString(IConnection p_iConnection, String p_strTag) throws GException {
		try {
			if(p_strTag.length() > 0
				&& m_strTag.length() <= 0) {

				m_strTag = p_strTag;
			}

			if(m_sqlOperator == SQLOperator.In
				|| m_sqlOperator == SQLOperator.InWithNull
				|| m_sqlOperator == SQLOperator.NotIn
				|| m_sqlOperator == SQLOperator.NotInWithNull) {

				if(m_collection != null) {
					return getSQL_CollectionFilter(p_iConnection);
				}

				return "";

			} else {
				StringBuilder builder = new StringBuilder();
				builder.append("(");

				if(m_sqlOperator == SQLOperator.Equal) {
					if(m_strTag.length() > 0) {
						builder.append(m_strTag).append(".");
					}

					builder.append(m_datafield.getFieldName()).append(" = ").append(m_dbObjectValue.getStringSQL()).append(')');

				} else if(m_sqlOperator == SQLOperator.NotEqual) {
					builder.append("(");

					if(m_strTag.length() > 0) {
						builder.append(m_strTag).append(".");
					}

					builder.append(m_datafield.getFieldName()).append(" <> ").append(m_dbObjectValue.getStringSQL()).append(") OR (");

					if(m_strTag.length() > 0) {
						builder.append(m_strTag).append(".");
					}

					builder.append(m_datafield.getFieldName()).append(" IS NULL))");

				} else if(m_sqlOperator == SQLOperator.IsNotNull) {
					if(m_strTag.length() > 0) {
						builder.append(m_strTag).append(".");
					}

					builder.append(m_datafield.getFieldName()).append(" IS NOT NULL)");

				} else if(m_sqlOperator == SQLOperator.IsNull) {
					if(m_strTag.length() > 0) {
						builder.append(m_strTag).append(".");
					}

					builder.append(m_datafield.getFieldName()).append(" IS NULL)");

				} else if(m_sqlOperator == SQLOperator.GreaterThan) {
					if(m_strTag.length() > 0) {
						builder.append(m_strTag).append(".");
					}

					builder.append(m_datafield.getFieldName()).append(" > ").append(m_dbObjectValue.getStringSQL()).append(')');

				} else if(m_sqlOperator == SQLOperator.GreaterThanOrEqualTo) {
					if(m_strTag.length() > 0) {
						builder.append(m_strTag).append(".");
					}

					builder.append(m_datafield.getFieldName()).append(" >= ").append(m_dbObjectValue.getStringSQL()).append(')');

				} else if(m_sqlOperator == SQLOperator.LessThan) {
					if(m_strTag.length() > 0) {
						builder.append(m_strTag).append(".");
					}

					builder.append(m_datafield.getFieldName()).append(" < ").append(m_dbObjectValue.getStringSQL()).append(')');

				} else if(m_sqlOperator == SQLOperator.LessThanOrEqualTo) {
					if(m_strTag.length() > 0) {
						builder.append(m_strTag).append(".");
					}

					builder.append(m_datafield.getFieldName()).append(" <= ").append(m_dbObjectValue.getStringSQL()).append(')');

				} else if(m_sqlOperator == SQLOperator.Like2) {
					if(m_strTag.length() > 0) {
						builder.append(m_strTag).append(".");
					}

					builder.append(m_datafield.getFieldName()).append(" LIKE ").append(m_dbObjectValue.getStringSQL_LIKE2()).append(')');

				} else if(m_sqlOperator == SQLOperator.StartWith) {
					if(m_strTag.length() > 0) {
						builder.append(m_strTag).append(".");
					}

					builder.append(m_datafield.getFieldName()).append(" LIKE ").append(m_dbObjectValue.getStringSQL_StartWith()).append(')');

				} else if(m_sqlOperator == SQLOperator.EndWith) {
					if(m_strTag.length() > 0) {
						builder.append(m_strTag).append(".");
					}

					builder.append(m_datafield.getFieldName()).append(" LIKE ").append(m_dbObjectValue.getStringSQL_EndWith()).append(')');

				} else if(m_sqlOperator == SQLOperator.Equal_IgnoreCase) {
					builder.append("LOWER(");

					if(m_strTag.length() > 0) {
						builder.append(m_strTag).append(".");
					}

					builder.append(m_datafield.getFieldName()).append(") LIKE ").append(m_dbObjectValue.getStringSQL_LowerCase()).append(')');

				} else if(m_sqlOperator == SQLOperator.Like2_IgnoreCase) {
					builder.append("LOWER(");

					if(m_strTag.length() > 0) {
						builder.append(m_strTag).append(".");
					}

					builder.append(m_datafield.getFieldName()).append(") LIKE ").append(m_dbObjectValue.getStringSQL_LIKE2_LowerCase()).append(')');

				} else {
					throw new GException("Not support SQLOperator " + m_sqlOperator + "!!!");
				}

				return builder.toString();
			}
		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	private String getSQL_CollectionFilter(IConnection p_iConnection) throws GException {
		try {
			if(m_collection == null) {
				throw new GException("getSQLString invalid Collection!");
			}

			if(p_iConnection == null) {
				StringBuilder builder = new StringBuilder();
				builder.append("(");

				if(m_strTag.length() > 0) {
					builder.append(m_strTag).append(".");
				}

				builder.append(m_datafield.getFieldName()).append(" ");
				builder.append("IN (Collection Size ").append(m_collection.size()).append("))");

				return builder.toString();
			}

			DBType dbtype = DBUtilities.getDBType();
			int intListLimit = 1000;

			if(dbtype != DBType.SQLite) {
				intListLimit = 2000;
			}

			StringBuilder builder = new StringBuilder();
			builder.append("(");

			if(m_collection.size() <= intListLimit) {
				if(m_collection.size() > 0) {
					if(m_strTag.length() > 0) {
						builder.append(m_strTag).append(".");
					}

					builder.append(m_datafield.getFieldName()).append(" ");

					if(m_sqlOperator == SQLOperator.In
						|| m_sqlOperator == SQLOperator.InWithNull) {

						builder.append("IN (");

					} else if(m_sqlOperator == SQLOperator.NotIn
						|| m_sqlOperator == SQLOperator.NotInWithNull) {

						builder.append("NOT IN (");
					}

					for(DBObject dbObject : m_collection) {
						builder.append(dbObject.getStringSQL()).append(", ");
					}

					builder = new StringBuilder(builder.substring(0, builder.length() - 2));
					builder.append(")");

					if(m_sqlOperator == SQLOperator.InWithNull
						|| m_sqlOperator == SQLOperator.NotInWithNull) {

						builder.append(" OR ");

						if(m_strTag.length() > 0) {
							builder.append(m_strTag).append(".");
						}

						builder.append(m_datafield.getFieldName()).append(" IS NULL");
					}

					builder.append(")");
				}
			} else {
				throw new GException("Not support List size over " + intListLimit + "!!!");
			}

			return builder.toString();

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public SQLOperator getSQLOperator() {
		return m_sqlOperator;
	}

	public String toString() {
		try {
			return getSQLString(null, "");

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
		}

		return "";
	}

	public int compareTo(SQLStatementString p_sqlStatementString) {
		return this.toString().compareTo(p_sqlStatementString.toString());
	}
}
