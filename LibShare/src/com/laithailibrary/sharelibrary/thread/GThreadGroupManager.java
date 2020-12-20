package com.laithailibrary.sharelibrary.thread;

public class GThreadGroupManager {

	private static final Object LOCK = new Object();

	private static long s_longGroupID = 0;
	private static final String PREFIX_GROUPNAME = "LAITHAITHREADGROUP-";

	private GThreadGroupManager() {}

	public static ThreadGroup newThreadGroup() {
		return new ThreadGroup(getGroupName_Next());
	}

	public static String getGroupName_Next() {
		synchronized(LOCK) {
			s_longGroupID++;
			String strGroupName = PREFIX_GROUPNAME + s_longGroupID;

			return strGroupName;
		}
	}
}
