package com.laithailibrary.sharelibrary.session;

/**
 * Created with IntelliJ IDEA.
 * User: dumkrerng
 * Date: 11/9/12
 * Time: 9:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class MainThread implements Comparable<MainThread> {

	private long m_lThreadID;

	public MainThread(Thread p_thread) {
		m_lThreadID = p_thread.getId();
	}

	public long getThreadID() {
		return m_lThreadID;
	}

	public String toString() {
		return String.valueOf(m_lThreadID);
	}

	public int compareTo(MainThread p_threadsession) {
		return this.toString().compareTo(p_threadsession.toString());
	}
}
