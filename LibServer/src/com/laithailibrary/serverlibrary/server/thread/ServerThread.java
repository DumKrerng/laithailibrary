package com.laithailibrary.serverlibrary.server.thread;

import java.net.*;
import com.laithailibrary.serverlibrary.client.callservice.*;
import com.laithailibrary.sharelibrary.thread.*;
import exc.*;

public class ServerThread implements Runnable {

	private Socket m_clientsocket;

	private GThreadGroup m_threadgroup;
	private Thread m_thread;


	public ServerThread(Socket p_clientsocket) throws GException {
		m_clientsocket = p_clientsocket;
	}

	public void setThread(Thread p_thread, GThreadGroup p_threadgroup) {
		m_thread = p_thread;
		m_threadgroup = p_threadgroup;
	}

	public Socket getClientSocket() {
		return m_clientsocket;
	}

	public void run() {
		try {
			ExecuteService service = new ExecuteService(m_clientsocket);
			service.execute(m_threadgroup);

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);

		} finally {
			try {
				m_clientsocket.close();

			} catch(Exception exception) {
				ExceptionHandler.display(exception);
			}
		}
	}
}
