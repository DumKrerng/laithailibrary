package com.laithailibrary.sharelibrary.servicecall;

import com.laithailibrary.sharelibrary.interfaceclass.InterfaceService;
import com.laithailibrary.sharelibrary.session.*;
import exc.GException;

/**
 * Created with IntelliJ IDEA.
 * User: dumkrerng
 * Date: 12/20/12
 * Time: 12:02 AM
 * To change this template use File | Settings | File Templates.
 */
public class ServiceCall_CurrentDateTime extends BaseServiceCall {

	private static final long serialVersionUID = 2;

	public ServiceCall_CurrentDateTime() {}

	public ServiceCall_CurrentDateTime(ISessionID p_sessionid, String p_setServerAddress) throws GException {
		super(p_sessionid, InterfaceService.class, "CurrentDateTime", p_setServerAddress);
	}
}
