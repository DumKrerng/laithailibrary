package exc;

import com.laithailibrary.logger.GLog;

import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.util.logging.ErrorManager;

/**
 * Created with IntelliJ IDEA.
 * User: dumkrerng
 * Date: 3/13/13
 * Time: 10:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExceptionHandler {

	private ExceptionHandler() {}

	public static void display(Exception p_exception) {
		try {
			String strError = p_exception.toString();

			if(strError.startsWith("java.rmi.ServerException: RemoteException occurred in server thread; nested exception is:")
				|| strError.startsWith("RemoteException occurred in server thread; nested exception is:")) {

				strError = strError.replace("java.rmi.ServerException: ", "");
				strError = strError.replace("RemoteException occurred in server thread; nested exception is: \n\texc.GException: ", "");
				strError = strError.replace(";", "\n");

				if(ExcDialog.getDisplay() == ExcDialog.Display.Yes) {
					GException.setMessageError(strError);

					new ExcDialog(strError, GException.class, ExcDialog.MessageType.Error);
				}

				GLog.severe(strError);

				ErrorManager errorManager = new ErrorManager();
				errorManager.error(strError, new Exception(), ErrorManager.WRITE_FAILURE);

			} else if(!strError.contains("GException")) {
				if(ExcDialog.getDisplay() == ExcDialog.Display.Yes) {
					GException.setMessageError(strError);

					new ExcDialog(p_exception, ExcDialog.MessageType.Error);
				}

				if(p_exception instanceof SQLClientInfoException) {
					String strSQLError = ((SQLClientInfoException) p_exception).getSQLState();
					GLog.severe(strSQLError);

				} else if(p_exception instanceof SQLException) {
					String strSQLError = p_exception.getMessage();
					GLog.severe(strSQLError);

				} else {
					GLog.severe(strError);
				}

				ErrorManager errorManager = new ErrorManager();
				errorManager.error(p_exception.getMessage(), p_exception, ErrorManager.WRITE_FAILURE);
			}
		} catch (Exception exception) {
			GLog.severe(exception.getMessage());
		}
	}

	public static void display(Throwable p_throwable) {
		try {
			String strError = p_throwable.getMessage();

			if(strError.startsWith("java.rmi.ServerException: RemoteException occurred in server thread; nested exception is:")
				|| strError.startsWith("RemoteException occurred in server thread; nested exception is:")) {

				strError = strError.replace("java.rmi.ServerException: ", "");
				strError = strError.replace("RemoteException occurred in server thread; nested exception is: \n\texc.GException: ", "");
				strError = strError.replace(";", "\n");

				if(ExcDialog.getDisplay() == ExcDialog.Display.Yes) {
					GException.setMessageError(strError);

					new ExcDialog(strError, GException.class, ExcDialog.MessageType.Error);
				}

				GLog.severe(strError);

				ErrorManager errorManager = new ErrorManager();
				errorManager.error(strError, new Exception(), ErrorManager.WRITE_FAILURE);

			} else if(!strError.contains("GException")) {
				if(ExcDialog.getDisplay() == ExcDialog.Display.Yes) {
					GException.setMessageError(strError);

					new ExcDialog(strError, ExcDialog.MessageType.Error);

				} else {
					GLog.severe(strError);
				}

				ErrorManager errorManager = new ErrorManager();
				errorManager.error(strError, new GException(p_throwable), ErrorManager.WRITE_FAILURE);
			}
		} catch (Exception exception) {
			GLog.severe(exception.getMessage());
		}
	}
}
