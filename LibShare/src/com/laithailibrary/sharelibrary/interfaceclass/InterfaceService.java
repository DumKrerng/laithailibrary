package com.laithailibrary.sharelibrary.interfaceclass;

import java.io.*;
import com.laithailibrary.sharelibrary.session.*;
import exc.*;

/**
 * Created with IntelliJ IDEA.
 * User: dumkrerng
 * Date: 11/2/12
 * Time: 9:54 PM
 * To change this template use File | Settings | File Templates.
 */
public interface InterfaceService extends Serializable {
	public abstract void setSessionID(ISessionID p_sessionid) throws GException;
	public abstract ISessionID getSessionID() throws GException;
}
