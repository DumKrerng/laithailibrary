package com.laithailibrary.sharelibrary.servicecall;

import com.laithailibrary.sharelibrary.interfaceclass.InterfaceService;
import com.laithailibrary.sharelibrary.session.*;
import exc.GException;

/**
 * Created with IntelliJ IDEA.
 * User: dumkrerng
 * Date: 12/19/12
 * Time: 11:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class ServiceCall_CurrentDate extends BaseServiceCall {

	private static final long serialVersionUID = 2;

	public ServiceCall_CurrentDate() {}

	public ServiceCall_CurrentDate(ISessionID p_sessionid, String p_setServerAddress) throws GException {
		super(p_sessionid, InterfaceService.class, "CurrentDate", p_setServerAddress);
	}
}
