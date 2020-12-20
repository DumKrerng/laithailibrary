package com.laithailibrary.sharelibrary.session;

public interface ISessionID {
	public String getAppType();
	public String getSessionID();
	public String getSessionID_01();
	public String getSessionID_02();
	public void incrementSubNumber();
	public String toString();
	public int compareTo(ISessionID p_sessionid);
}
