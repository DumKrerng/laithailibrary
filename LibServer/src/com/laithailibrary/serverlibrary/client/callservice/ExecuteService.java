package com.laithailibrary.serverlibrary.client.callservice;

import java.io.*;
import java.net.*;
import com.laithailibrary.logger.*;
import com.laithailibrary.serverlibrary.client.callservice.android.*;
import com.laithailibrary.serverlibrary.client.clientsession.*;
import com.laithailibrary.sharelibrary.bean.request.*;
import com.laithailibrary.sharelibrary.db.dbobject.*;
import com.laithailibrary.sharelibrary.service.*;
import com.laithailibrary.sharelibrary.servicecall.*;
import com.laithailibrary.sharelibrary.session.*;
import com.laithailibrary.sharelibrary.thread.*;
import exc.*;
import pp.*;

public class ExecuteService {

	private Socket m_clientsocket = null;

	private static final long serialVersionUID = 1;

	private ExecuteService() {}

	public ExecuteService(Socket p_clientsocket) throws GException {
		m_clientsocket = p_clientsocket;
	}

	public void execute(GThreadGroup p_threadgroup) throws GException {
		try {
			InputStream input = m_clientsocket.getInputStream();

			ObjectInputStream objInput = new ObjectInputStream(input);
			Object object = objInput.readObject();

			if(object instanceof AppVersion) {
				AppVersion appversion = (AppVersion)object;
				String strAppName = appversion.getAppName();

				if(strAppName == null
					|| strAppName.length() <= 0) {

					appversion = null;

				} else {
					String strAppVersion_Server = GProperties.getAppVersion(strAppName);

					if(strAppVersion_Server != null
						&& strAppVersion_Server.length() > 0) {

						appversion.setAppVersion_Server(strAppVersion_Server);

					} else {
						appversion = null;
					}
				}

				OutputStream output = m_clientsocket.getOutputStream();
				output.flush();
				ObjectOutputStream objStream = new ObjectOutputStream(output);
				objStream.writeObject(appversion);

			} else if(object instanceof AppManager) {
				AppManager appmanager = (AppManager)object;

				ISessionID sessionid = appmanager.getSessionID();
				String strRequestString = appmanager.getRequestString();

				if(strRequestString.compareTo("ClientAppSize") == 0) {
					GLog.info("Client SessionID: " + sessionid.toString() + " get ClientAppSize.");

					String strAppName = appmanager.getAppName();
					File file = new File("ClientApp" + File.separatorChar + strAppName + ".jar");

					OutputStream output = m_clientsocket.getOutputStream();
					output.flush();
					ObjectOutputStream objOutputStream = new ObjectOutputStream(output);

					long longClientAppSize = 0;

					if(!file.exists()) {
						GLog.severe("File not found. \"" + file.getAbsolutePath() + '\"');

					} else {
						longClientAppSize = file.length();
					}

					objOutputStream.writeLong(longClientAppSize);

					objOutputStream.close();

				} else if(strRequestString.compareTo("GetNewApp") == 0) {
					GLog.info("Client SessionID: " + sessionid.toString() + " get new ClientApp.");

					String strAppName = appmanager.getAppName();
					File file = new File("ClientApp" + File.separatorChar + strAppName + ".jar");

					OutputStream output = m_clientsocket.getOutputStream();
					output.flush();
					DataOutputStream objOutputStream = new DataOutputStream(output);

					if(!file.exists()) {
						GLog.severe("File not found. \"" + file.getAbsolutePath() + '\"');
						output.write(null);

					} else {
						FileInputStream inputStream = new FileInputStream(file);
						byte[] buffer = new byte[1024];
						int bytesRead;

						while((bytesRead = inputStream.read(buffer)) != -1) {
							objOutputStream.write(buffer, 0, bytesRead);
						}

						inputStream.close();
						output.close();
					}
				}
			} else if(object instanceof FirstConnect) {
				FirstConnect firstconnect = (FirstConnect)object;
				SessionID sessionid = SessionManager.createSessionID(firstconnect);

				SessionData sessiondata = (SessionData)SessionManager.getSessionData(sessionid);

				OutputStream output = m_clientsocket.getOutputStream();
				output.flush();
				ObjectOutputStream objStream = new ObjectOutputStream(output);
				objStream.writeObject(sessiondata);

				GLog.info("Client SessionID: " + sessionid.toString() + " is connected.");

			} else if(object instanceof ServiceCall_DataTableQuery) {
				ServiceCall_DataTableQuery servicecall = (ServiceCall_DataTableQuery)object;

				callMethod_DataTableQuery(p_threadgroup, m_clientsocket, servicecall);

			} else if(object instanceof ServiceCall_WebShortLink) {
				ServiceCall_WebShortLink servicecall = (ServiceCall_WebShortLink)object;

				callWebShortLink(p_threadgroup, m_clientsocket, servicecall);

			} else if(object instanceof ServiceCall_CurrentDate) {
				ServiceCall_CurrentDate servicecall = (ServiceCall_CurrentDate)object;

				callCurrentDate(p_threadgroup, m_clientsocket, servicecall);

			} else if(object instanceof ServiceCall_CurrentDateTime) {
				ServiceCall_CurrentDateTime servicecall = (ServiceCall_CurrentDateTime)object;

				callCurrentDateTime(p_threadgroup, m_clientsocket, servicecall);

			} else if(object instanceof ServiceCall_HelloWorld) {
				ServiceCall_HelloWorld servicecall = (ServiceCall_HelloWorld)object;

				callHelloWorld(p_threadgroup, m_clientsocket, servicecall);

			} else if(object instanceof ServiceCall_Method) {
				ServiceCall_Method servicecall = (ServiceCall_Method)object;

				callMethod(p_threadgroup, m_clientsocket, servicecall);

			} else if(object instanceof ClientSayGoodbye) {
				ClientSayGoodbye clientsaygoodbye = (ClientSayGoodbye)object;
				SessionData sessiondata = clientsaygoodbye.getSessionData();

				SessionManager.deleteSessionID(sessiondata.getSessionID());

				GLog.info("Client SessionID: " + sessiondata.getSessionID().toString() + " is disconnected.");

			} else if(object instanceof SessionData) {
				SessionData sessiondata = (SessionData)object;
				SessionManager.deleteSessionID(sessiondata.getSessionID());

				GLog.info("Client SessionID: " + sessiondata.getSessionID().toString() + " is disconnected.");

			} else if(object instanceof String) {
				String strRequest = (String)object;

				if(strRequest.compareTo(ServiceStringConstant.REQUEST_StopServerService) == 0) {
					callStopServerService(p_threadgroup, m_clientsocket);

				} else if(strRequest.compareTo(ServiceStringConstant.REQUEST_SessionActive) == 0) {
					int intNumberOfValidSession = SessionManager.getNumberOfValidSession();

					OutputStream output = m_clientsocket.getOutputStream();
					output.flush();
					ObjectOutputStream objStream = new ObjectOutputStream(output);
					objStream.writeUTF("Session is valid #" + intNumberOfValidSession);

				} else {
					ObjectOutputStream objOutput = new ObjectOutputStream(m_clientsocket.getOutputStream());
					objOutput.writeObject(ServiceStringConstant.RESPOND_NotSupportServiceString);

					throw new GException("Not support service string: " + strRequest + "!!!");
				}
			} else if(object instanceof AndroidRequest) {
				AndroidRequest request = (AndroidRequest)object;

				callFromAndroid(p_threadgroup, m_clientsocket, request);

			} else {
				throw new GException("Not support object: " + object.getClass().getName() + "!!!");
			}
		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	private void callWebShortLink(GThreadGroup p_threadgroup, Socket p_clientsocket, ServiceCall_WebShortLink p_servicecall) {
		try {
			ISessionID sessionid = p_servicecall.getSessionID();
			ClientSessionID.addClientCallService(p_threadgroup, sessionid);

			DBString dbstrWebFullLinkValue = GWebShortLink.getWebFullLinkValue(sessionid, p_servicecall.getWebShortLinkID());

			ObjectOutputStream objOutput = new ObjectOutputStream(p_clientsocket.getOutputStream());
			objOutput.writeObject(dbstrWebFullLinkValue);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
		}

		SessionManager.finish(p_servicecall.getSessionID());
	}

	private void callCurrentDate(GThreadGroup p_threadgroup, Socket p_clientsocket, ServiceCall_CurrentDate p_servicecall) {
		try {
			ISessionID sessionid = p_servicecall.getSessionID();
			ClientSessionID.addClientCallService(p_threadgroup, sessionid);

			GLog.info("Client call CurrentDate on SessionID " + sessionid);

			DBDate dbDate = DBDate.getCurrentDate();

			ObjectOutputStream objOutput = new ObjectOutputStream(p_clientsocket.getOutputStream());
			objOutput.writeObject(dbDate);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
		}

		SessionManager.finish(p_servicecall.getSessionID());
	}

	private void callCurrentDateTime(GThreadGroup p_threadgroup, Socket p_clientsocket, ServiceCall_CurrentDateTime p_servicecall) {
		try {
			ISessionID sessionid = p_servicecall.getSessionID();
			ClientSessionID.addClientCallService(p_threadgroup, sessionid);

			GLog.info("Client call CurrentDateTime on SessionID " + sessionid);

			DBDateTime dbDateTime = DBDateTime.getCurrentDateTime();

			ObjectOutputStream objOutput = new ObjectOutputStream(p_clientsocket.getOutputStream());
			objOutput.writeObject(dbDateTime);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
		}

		SessionManager.finish(p_servicecall.getSessionID());
	}

	private void callHelloWorld(GThreadGroup p_threadgroup, Socket p_clientsocket, ServiceCall_HelloWorld p_servicecall) {
		try {
			ISessionID sessionid = p_servicecall.getSessionID();
			ClientSessionID.addClientCallService(p_threadgroup, sessionid);

			GLog.info("Client call HelloWorld on SessionID " + sessionid);

			ObjectOutputStream objOutput = new ObjectOutputStream(p_clientsocket.getOutputStream());
			objOutput.writeObject("Hello World!!!");

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
		}

		SessionManager.finish(p_servicecall.getSessionID());
	}

	private void callMethod(GThreadGroup p_threadgroup, Socket p_clientsocket, ServiceCall_Method p_servicecall) {
		try {
			ISessionID sessionid = p_servicecall.getSessionID();
			ClientSessionID.addClientCallService(p_threadgroup, sessionid);

			new CallServiceMethod(p_clientsocket, p_servicecall);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
		}

		SessionManager.finish(p_servicecall.getSessionID());
	}

	private void callMethod_DataTableQuery(GThreadGroup p_threadgroup, Socket p_clientsocket, ServiceCall_DataTableQuery p_servicecall) {
		try {
			ISessionID sessionid = p_servicecall.getSessionID();
			ClientSessionID.addClientCallService(p_threadgroup, sessionid);

			new CallServiceMethod_DataTableQuery(p_clientsocket, p_servicecall);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
		}

		SessionManager.finish(p_servicecall.getSessionID());
	}

	private void callFromAndroid(GThreadGroup p_threadgroup, Socket p_clientsocket, AndroidRequest p_request) {
		try {
			ClientSessionID.addClientCallService(p_threadgroup, p_request.getANDSessionData().getSessionID());

			new AndroidRequestManager(p_clientsocket, p_request);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
		}

		try {
			SessionManager.finish(p_request.getANDSessionData().getSessionID());

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
		}
	}

	private void callStopServerService(GThreadGroup p_threadgroup, Socket p_clientsocket) {
		try {
			GLog.info("Client call Stop Server Service.");

			if(ClientSessionID.isEmptyCallService()) {
				ObjectOutputStream objOutput = new ObjectOutputStream(p_clientsocket.getOutputStream());
				objOutput.writeObject(ServiceStringConstant.RESPOND_ServerServiceIsStopping);

				p_clientsocket.close(); // Create Runtime object

//				r.exec("shutdown -s -t 10");
				Runtime.getRuntime().exec("shutdown -h 1");

				GLog.info("Server Service Stopped By Client.");

				System.exit(0);
			}

			GLog.severe("Session Service is not empty!!!");

			ObjectOutputStream objOutput = new ObjectOutputStream(p_clientsocket.getOutputStream());
			objOutput.writeObject(ServiceStringConstant.RESPOND_SessionServiceExecuteIsNotEmpty);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
		}
	}
}
