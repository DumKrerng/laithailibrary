package com.laithailibrary.sharelibrary.servicecall;

import com.laithailibrary.sharelibrary.interfaceclass.*;
import com.laithailibrary.sharelibrary.session.*;
import exc.*;

/**
 * Created by dumkrerng on 28/2/2559.
 */
public class ServiceCall_DBDataTableRegister extends BaseServiceCall {

	private static final long serialVersionUID = 2;

	public ServiceCall_DBDataTableRegister() {}

	public ServiceCall_DBDataTableRegister(ISessionID p_sessionid, String p_strMethodName_Full, String p_setServerAddress) throws GException {
		super(p_sessionid, InterfaceService.class, p_strMethodName_Full, p_setServerAddress);
	}
}
