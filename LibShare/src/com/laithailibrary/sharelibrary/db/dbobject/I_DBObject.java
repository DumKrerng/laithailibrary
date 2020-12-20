package com.laithailibrary.sharelibrary.db.dbobject;

import java.io.*;
import exc.*;

/**
 * Created by IntelliJ IDEA.
 * User: dumkrerng
 * Date: 6/4/11
 * Time: 12:32 AM
 * To change this template use File | Settings | File Templates.
 */
public interface I_DBObject extends Externalizable {
	public abstract void setStringValue(String p_strString);
	public abstract String getStringSQL() throws GException;
	public abstract String getStringSQL_StartWith() throws GException;
	public abstract String getStringSQL_EndWith() throws GException;
	public abstract String getStringSQL_LIKE2() throws GException;
	public abstract String getStringSQL_LowerCase() throws GException;
	public abstract String getStringSQL_LIKE2_LowerCase() throws GException;
	public abstract String getString();
	public abstract String getSortString();
	public abstract String getCSVString();
	public abstract Object getValueReport();
	public abstract String toString();
	public abstract boolean isInvalid();
	public abstract boolean isValid();
}
