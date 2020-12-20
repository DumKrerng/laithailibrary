package com.laithailibrary.serverlibrary.client.callservice.android.handler;

import com.laithailibrary.sharelibrary.bean.request.*;
import com.laithailibrary.sharelibrary.bean.result.*;
import exc.*;

public interface ANDRequestHandler {
	public String getRequestName();
	public AndroidResult doHandle(AndroidRequest p_request) throws GException;
}
