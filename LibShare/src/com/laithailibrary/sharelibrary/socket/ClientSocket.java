package com.laithailibrary.sharelibrary.socket;

import javax.net.ssl.*;
import java.net.*;
import com.laithailibrary.logger.*;
import com.laithailibrary.sharelibrary.support.*;
import exc.*;

public class ClientSocket {

	public static final int SOCKETTIOMOUT = 5000; //5 seconds
	public static final int TRYLIMIT = 10;
	public static final int SLEEP = 3000; //3 seconds

	private static String m_strTrustStore_FilePath = "";

	private ClientSocket() {}

	public static void setSSL(String p_strFile) {
		m_strTrustStore_FilePath = p_strFile;

		System.setProperty("javax.net.ssl.trustStore", m_strTrustStore_FilePath);
	}

	public static Socket createSocket() throws GException {
		return createSocket(AppClientUtilities.getServerAddress(), AppClientUtilities.getServerPort());
	}

	public static Socket createSocket(String p_strServerAddress, int p_intServerPort) throws GException {
		try {
			Socket socket = null;

			boolean bolTry = true;
			int intTryCount = 0;

			do {
				intTryCount++;

				try {
					if(intTryCount > 1) {
						GLog.info("Connect to server: " + p_strServerAddress + ':' + p_intServerPort + ".(Try=" + intTryCount + ')');
					}

					if(m_strTrustStore_FilePath.length() > 0) {
						SSLSocketFactory ssf = (SSLSocketFactory)SSLSocketFactory.getDefault();

						socket = ssf.createSocket();
						socket.connect(new InetSocketAddress(p_strServerAddress, p_intServerPort), SOCKETTIOMOUT);

					} else {
						socket = new Socket();
						socket.connect(new InetSocketAddress(p_strServerAddress, p_intServerPort), SOCKETTIOMOUT);
					}

					bolTry = false;

				} catch(SocketTimeoutException p_excTimeout) {
					if(intTryCount >= ClientSocket.TRYLIMIT) {
						throw new GException("Connection Timeout!!!");
					}

					Thread.sleep(ClientSocket.SLEEP);

				} catch(ConnectException p_excTimeout) {
					if(intTryCount >= ClientSocket.TRYLIMIT) {
						throw new GException("Connection Timeout!!!");
					}

					Thread.sleep(ClientSocket.SLEEP);

				} catch(Exception exception) {
					ExceptionHandler.display(exception);
					throw new GException(exception);
				}
			} while(bolTry);

			return socket;

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}
}
