package com.laithailibrary.serverlibrary.server.thread;

import com.laithailibrary.sharelibrary.thread.*;
import exc.*;

/**
 * Created with IntelliJ IDEA.
 * User: dumkrerng
 * Date: 11/10/12
 * Time: 7:50 AM
 * To change this template use File | Settings | File Templates.
 */
public class GThread {

	private ServerThread m_serverthread = null;
//	private Thread m_thread = null;

	public static final String THREADMAIN = "THREADMAIN";

	private GThread() {}

	public GThread(ServerThread p_serverthread) throws GException {
		m_serverthread = p_serverthread;
	}

	public void start() throws GException {
		try {
//			Socket clientsocket = m_serverthread.getClientSocket();
//
//			InputStream input = clientsocket.getInputStream();
//			ObjectInputStream objInput = new ObjectInputStream(input);
//			Object object = objInput.readObject();
//
//			AppVersion appversion = (AppVersion)object;
//			String strAppName = appversion.getAppName();
//
//			if(strAppName == null
//				|| strAppName.length() <= 0) {
//
//				appversion = null;
//
//			} else {
//				String strAppVersion_Server = GProperties.getAppVersion(strAppName);
//
//				if(strAppVersion_Server != null
//					&& strAppVersion_Server.length() > 0) {
//
//					appversion.setAppVersion_Server(strAppVersion_Server);
//
//				} else {
//					appversion = null;
//				}
//			}
//
//			OutputStream output = m_clientsocket.getOutputStream();
//			output.flush();
//			ObjectOutputStream objStream = new ObjectOutputStream(output);
//			objStream.writeObject(appversion);

			GThreadGroup threadgroup = new GThreadGroup();

			Thread thread = new Thread(threadgroup.getThreadGroup(), m_serverthread, THREADMAIN, 80);
			thread.setPriority(Thread.MAX_PRIORITY);

			m_serverthread.setThread(thread, threadgroup);

			thread.start();

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		}
	}

//	public void run() throws GException {
//		try {
//			m_thread.run();
//
//		} catch (Exception exception) {
//		  ExceptionHandler.display(exception);
//		}
//	}
}
