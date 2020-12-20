package com.laithailibrary.sharelibrary.thread;

public class GThreadGroup implements Comparable<GThreadGroup> {

	private String m_strGroupName = "";
	private ThreadGroup m_threadgroup = null;

	public GThreadGroup() {
		m_strGroupName = GThreadGroupManager.getGroupName_Next();
		m_threadgroup = new ThreadGroup(m_strGroupName);
	}

	public GThreadGroup(Thread p_thread) {
		m_threadgroup = p_thread.getThreadGroup();
		m_strGroupName = m_threadgroup.getName();

		if(m_strGroupName.compareTo("main") == 0) {
			m_strGroupName = String.valueOf(p_thread.getId());
		}
	}

	public void first(Thread p_thread) {
		m_threadgroup = p_thread.getThreadGroup();
		m_strGroupName = p_thread.getThreadGroup().getName();

		if(m_strGroupName.compareTo("main") == 0) {
			m_strGroupName = String.valueOf(p_thread.getId());
		}
	}

	public String getName() {
		return m_strGroupName;
	}

	public ThreadGroup getThreadGroup() {
		return m_threadgroup;
	}

	public String toString() {
		return getName();
	}

	public int compareTo(GThreadGroup p_threadgroup) {
		return this.toString().compareTo(p_threadgroup.toString());
	}
}
