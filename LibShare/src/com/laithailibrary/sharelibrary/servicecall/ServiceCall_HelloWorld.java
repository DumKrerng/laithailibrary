package com.laithailibrary.sharelibrary.servicecall;

import com.laithailibrary.sharelibrary.interfaceclass.*;
import com.laithailibrary.sharelibrary.session.*;
import exc.*;

/**
 * Created by dumkrerng on 3/8/2559.
 */
public class ServiceCall_HelloWorld extends BaseServiceCall {

	private static final long serialVersionUID = 2;

	public ServiceCall_HelloWorld() {}

	public ServiceCall_HelloWorld(ISessionID p_sessionid, String p_setServerAddress) throws GException {
		super(p_sessionid, InterfaceService.class, "HelloWorld", p_setServerAddress);
	}
}
