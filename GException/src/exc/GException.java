package exc;

import java.rmi.RemoteException;
import java.util.logging.ErrorManager;

/**
 * Created by IntelliJ IDEA.
 * User: dumkrerng
 * Date: 6/4/11
 * Time: 11:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class GException extends RemoteException {

	private static String s_strMessageError = "$MessageError_Invalid$";

	private static final long serialVersionUID = 10;

	public GException() {
		initCause(null);  // Disallow subsequent initCause
	}

	/**
	 * Constructs a <code>RemoteException</code> with the specified detail
	 * message and cause.  This constructor sets the {@link #detail}
	 * field to the specified <code>Throwable</code>.
	 *
	 * @param s the detail message
	 * @param cause the cause
	 */
	public GException(String s, Throwable cause) {
		super(s, cause);
	}

	/**
	 * Returns the detail message, including the message from the cause, if
	 * any, of this exception.
	 *
	 * @return the detail message
	 */
	public String getMessage() {
		return super.getMessage();
	}

	/**
	 * Returns the cause of this exception.  This method returns the value
	 * of the {@link #detail} field.
	 *
	 * @return  the cause, which may be <tt>null</tt>.
	 * @since   1.4
	 */
	public Throwable getCause() {
		return super.getCause();
	}

	public GException(String p_strMessage) {
		super(p_strMessage);

		try {
			if(!p_strMessage.contains("GException")) {
				if(ExcDialog.getDisplay() == ExcDialog.Display.Yes) {
					new ExcDialog(p_strMessage, GException.class, ExcDialog.MessageType.Error, ExcDialog.getDisplay());
				}

				ErrorManager errorManager = new ErrorManager();
				errorManager.error(p_strMessage, new Exception(), ErrorManager.WRITE_FAILURE);
			}
		} catch (Exception exception) {
			ExceptionHandler.display(exception);

		} finally {
			s_strMessageError = p_strMessage;
		}
	}

	public GException(StringBuilder p_bufferError) {
		super(p_bufferError.toString());

		String strMessage = p_bufferError.toString();

		try {
			if(!strMessage.contains("GException")) {
				if(ExcDialog.getDisplay() == ExcDialog.Display.Yes) {
					new ExcDialog(strMessage, GException.class, ExcDialog.MessageType.Error, ExcDialog.getDisplay());
				}

				ErrorManager errorManager = new ErrorManager();
				errorManager.error(strMessage, new Exception(), ErrorManager.WRITE_FAILURE);
			}
		} catch (Exception exception) {
			ExceptionHandler.display(exception);

		} finally {
			s_strMessageError = strMessage;
		}
	}

	public GException(Exception p_exception) {
		super(p_exception.getMessage());

		try {
			String strMessage = p_exception.getMessage();

			if(!strMessage.contains("GException")) {
				if(ExcDialog.getDisplay() == ExcDialog.Display.Yes) {
					if(!s_strMessageError.contains(strMessage)) {
						new ExcDialog(strMessage, GException.class, ExcDialog.MessageType.Error, ExcDialog.getDisplay());
					}
				}

				ErrorManager errorManager = new ErrorManager();
				errorManager.error(strMessage, new Exception(), ErrorManager.WRITE_FAILURE);
			}
		} catch (Exception exception) {
			ExceptionHandler.display(exception);
		}
	}

	public GException(Throwable p_throwable) {
		super(p_throwable.getMessage(), p_throwable);

		try {
			String strMessage = p_throwable.getMessage();

			if(!strMessage.contains("GException")) {
				if(ExcDialog.getDisplay() == ExcDialog.Display.Yes) {
					if(!s_strMessageError.contains(strMessage)) {
						new ExcDialog(strMessage, GException.class, ExcDialog.MessageType.Error, ExcDialog.getDisplay());
					}
				}

				ErrorManager errorManager = new ErrorManager();
				errorManager.error(strMessage, new Exception(), ErrorManager.WRITE_FAILURE);
			}
		} catch (Exception exception) {
			ExceptionHandler.display(exception);
		}
	}

	public static void setMessageError(String p_strMessageError) {
		s_strMessageError = p_strMessageError;
	}
}
