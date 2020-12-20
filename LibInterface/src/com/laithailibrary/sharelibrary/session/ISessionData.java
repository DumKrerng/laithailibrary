package com.laithailibrary.sharelibrary.session;

public interface ISessionData {
	public ISessionID getSessionID();
	public IClientInfo getClientInfo();
	public String getProName();
	public String getProVer();
}
