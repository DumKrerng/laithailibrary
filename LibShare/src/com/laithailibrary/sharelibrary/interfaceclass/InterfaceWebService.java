package com.laithailibrary.sharelibrary.interfaceclass;

import java.io.*;
import com.laithailibrary.sharelibrary.session.*;
import exc.*;

/**
 * Created by dumkrerng on 8/8/2559.
 */
public interface InterfaceWebService extends Serializable {
	public abstract void setSessionID(SessionID p_sessionid) throws GException;
	public abstract SessionID getSessionID() throws GException;
}
