package com.laithailibrary.sharelibrary.servicecall;

import com.laithailibrary.sharelibrary.interfaceclass.*;
import com.laithailibrary.sharelibrary.session.*;

/**
 * Created with IntelliJ IDEA.
 * User: dumkrerng
 * Date: 3/31/13
 * Time: 1:02 AM
 * To change this template use File | Settings | File Templates.
 */
public interface IServiceCell {
	public abstract Class<? extends InterfaceService> getClassService();
	public abstract String getServiceName();
	public abstract String getServerAddress();
	public abstract ISessionID getSessionID();
	public abstract String getMethodName();
//	public abstract GServerSocketFactory getGServerSocketFactory();
//	public abstract GClientSocketFactory getGClientSocketFactory();
}
