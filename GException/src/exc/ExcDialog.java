package exc;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import com.laithailibrary.logger.*;

/**
 * Created by IntelliJ IDEA.
 * User: dumkrerng
 * Date: 6/4/11
 * Time: 11:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExcDialog {

	private static Display s_display = Display.No;

	private String m_strHeader = "Message";
	private String m_strExceptionMSG = "";
	private String m_strLocalizedMessage = "";
	private String m_strClassName = "";
	private String m_strCanonicalName = "";

	private MessageType m_messagetype = MessageType.Info;

	public ExcDialog(Exception p_exception) throws GException {
		m_strExceptionMSG = p_exception.getMessage();
		m_strLocalizedMessage = p_exception.getLocalizedMessage();
		m_strClassName = p_exception.getClass().getName();
		m_strCanonicalName = p_exception.getClass().getCanonicalName();

		init(s_display);
	}

	public ExcDialog(Exception p_exception, MessageType p_messagetype) throws GException {
		m_strExceptionMSG = p_exception.getMessage();
		m_strLocalizedMessage = p_exception.getLocalizedMessage();
		m_strClassName = p_exception.getClass().getName();
		m_strCanonicalName = p_exception.getClass().getCanonicalName();
		m_messagetype = p_messagetype;

		init(s_display);
	}

	public ExcDialog(String p_string) {
		m_strExceptionMSG = p_string;
		m_strLocalizedMessage = "";
		m_strClassName = "";
		m_strCanonicalName = "";

		init(s_display);
	}

	public ExcDialog(String p_string, MessageType p_messagetype) {
		m_strExceptionMSG = p_string;
		m_strLocalizedMessage = "";
		m_strClassName = "";
		m_strCanonicalName = "";
		m_messagetype = p_messagetype;

		init(s_display);
	}

	public ExcDialog(String p_string, Class p_class, MessageType p_messagetype) throws GException {
		m_strExceptionMSG = p_string;
		m_strLocalizedMessage = "";
		m_strClassName = p_class.getName();
		m_strCanonicalName = "";
		m_messagetype = p_messagetype;

		init(s_display);
	}

	public ExcDialog(String p_string, Class p_class, MessageType p_messagetype, Display p_display) throws GException {
		m_strExceptionMSG = p_string;
		m_strLocalizedMessage = "";
		m_strClassName = p_class.getName();
		m_strCanonicalName = "";
		m_messagetype = p_messagetype;

		init(p_display);
	}

	public ExcDialog(String p_strHeader, String p_string) throws GException {
		m_strHeader = p_strHeader;
		m_strExceptionMSG = p_string;
		m_strLocalizedMessage = "";
		m_strClassName = "";
		m_strCanonicalName = "";

		init(s_display);
	}

	public ExcDialog(String p_strHeader, String p_string, MessageType p_messagetype) throws GException {
		m_strHeader = p_strHeader;
		m_strExceptionMSG = p_string;
		m_strLocalizedMessage = "";
		m_strClassName = "";
		m_strCanonicalName = "";
		m_messagetype = p_messagetype;

		init(s_display);
	}

	private void init(Display p_display) {
		try {
			if(p_display == null) {
				p_display = s_display;
			}

			if(m_strExceptionMSG == null) {
				m_strExceptionMSG = "NULL";
			}

			m_strExceptionMSG = m_strExceptionMSG.replace("RemoteException occurred in server thread; nested exception is: \n\texc.GException: ", "");
			m_strExceptionMSG = m_strExceptionMSG.replace("exc.GException:", "");
			m_strExceptionMSG = m_strExceptionMSG.replace(";", "\n");

			StringBuilder builder = new StringBuilder();

			if(m_strClassName.length() > 0) {
				builder.append(m_strClassName).append(" -> ");
			}

			builder.append(m_strExceptionMSG);

			if(m_messagetype == MessageType.Info) {
				GLog.info(builder.toString());

				p_display = Display.No;

			} else if(m_messagetype == MessageType.Message) {
				GLog.info(builder.toString());

			} else if(m_messagetype == MessageType.Error) {
				m_strHeader = "Exception Message";

				GLog.severe(builder.toString());

			} else {
				GLog.info(builder.toString());
			}

			if(p_display == Display.Yes) {
				displayDialog();
			}
		} catch (Exception exception) {
			ExceptionHandler.display(exception);
		}
	}

	private void displayDialog() throws GException {
		try {
			JDialog dialog = new JDialog();
			dialog.setSize(new Dimension(600, 400));

			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setModal(true);

			StringBuilder builder = new StringBuilder();
//			builder.append(m_strClassName).append("\n");
			builder.append(m_strExceptionMSG).append('\n');

			JTextComponent textComponent = new JTextArea(builder.toString());
			JButton btnClose = new JButton("Close");

			buildDialogLayout(dialog, textComponent, btnClose);
			buildLocation(dialog);

			dialog.setLocationRelativeTo(null);

			if(m_messagetype == MessageType.Message) {
				dialog.addWindowListener( new WindowAdapter() {
					public void windowOpened(WindowEvent p_event) {
						textComponent.requestFocus();
					}
				});
			} else {
				dialog.addWindowListener( new WindowAdapter() {
					public void windowOpened(WindowEvent p_event) {
						btnClose.requestFocus();
					}
				});
			}

			dialog.setVisible(true);

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	private void buildDialogLayout(final JDialog p_dialog, JTextComponent p_textComponent, JButton p_btnClose) throws GException {
		try{
			Container pane = p_dialog.getContentPane();
			pane.setLayout(new BorderLayout());

			JLabel label = new JLabel(m_strHeader);
			label.setFont(new Font("TH Sarabun New", Font.BOLD, 26));
			pane.add(label, BorderLayout.NORTH);

			p_textComponent.setEditable(false);
			p_textComponent.setFont(new Font("TH Sarabun New", Font.PLAIN, 22));
			JScrollPane scrollPane = new JScrollPane(p_textComponent);
			pane.add(scrollPane, BorderLayout.CENTER);

			p_btnClose.addActionListener(new ActionListener() {
				public void actionPerformed(final ActionEvent p_event) {
					p_dialog.dispose();
				}
			});

			pane.add(p_btnClose, BorderLayout.SOUTH);

			if(m_messagetype == MessageType.Message) {
				p_textComponent.addKeyListener(new KeyListener() {
					@Override
					public void keyTyped(KeyEvent p_event) {}

					@Override
					public void keyPressed(KeyEvent p_event) {
						if(p_event.getKeyCode() == KeyEvent.VK_ENTER) {
							p_dialog.dispose();
						}
					}

					@Override
					public void keyReleased(KeyEvent p_event) {}
				});
			}

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	private void buildLocation(JDialog p_dialog) {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

		int intX_Parent = new Double(dim.getWidth()).intValue();
		int intY_Parent = new Double(dim.getHeight()).intValue();

		intX_Parent = intX_Parent / 2;
		intY_Parent = intY_Parent / 2;

		int intWidth = p_dialog.getWidth();
		int intHeight = p_dialog.getHeight();

		intWidth = intWidth / 2;
		intHeight = intHeight / 2;

		int intX = intX_Parent - intWidth;
		int intY = intY_Parent - intHeight;

		p_dialog.setLocation(intX, intY);
	}

	public static void setDisplay(Display p_display) {
		s_display = p_display;
	}

	public static Display getDisplay() {
		return s_display;
	}

	public enum MessageType {
		Info,
		Message,
		Error
	}

	public enum Display {
		Yes,
		No
	}
}
