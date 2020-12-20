package com.laithailibrary.clientlibrary.ui.support;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.*;
import com.laithailibrary.clientlibrary.ui.image.*;
import com.laithailibrary.clientlibrary.ui.swing.*;
import com.laithailibrary.logger.*;
import exc.*;

/**
 * Created with IntelliJ IDEA.
 * User: dumkrerng
 * Date: 1/21/13
 * Time: 10:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class GDialogWaiting extends JDialog {

	private GDialogWaiting me;
	private String m_strCaption = "Please Wait . . .";
	private Dimension m_dimension = new Dimension(350, 100);



	private static GDialogWaiting main = null;



	private static Thread m_threadMain = null;
	private Thread m_threadUpdateInfo;
//	private boolean m_cancel = false;
	
	private static boolean s_bolThreadMainRunning = false;

	protected GLabel m_lblInfo;
	private GButton m_btnStopTask;

	public GDialogWaiting(GFrame p_frame, Runnable p_runnable) throws GException {
		super(p_frame, "Please Wait . . .", true);

		if(p_runnable instanceof GRunnable) {
			m_dimension = new Dimension(350, 150);
		}

		init(p_runnable);
	}

	public GDialogWaiting(GFrame p_frame, String p_strCaption, Runnable p_runnable) throws GException {
		super(p_frame, "Please Wait . . .", true);

		if(p_runnable instanceof GRunnable) {
			m_dimension = new Dimension(350, 150);
		}

		m_strCaption = p_strCaption;

		init(p_runnable);
	}

	public GDialogWaiting(GFrame p_frame, GRunnable p_runnable, Dimension p_dimension) throws GException {
		super(p_frame, "Please Wait . . .", true);

		m_dimension = p_dimension;

		init(p_runnable);
	}

	public GDialogWaiting(GDialog p_dialog, Runnable p_runnable) throws GException {
		super(p_dialog, "Please Wait . . .", true);

		if(p_runnable instanceof GRunnable) {
			m_dimension = new Dimension(350, 150);
		}

		init(p_runnable);
	}

	public GDialogWaiting(GDialog p_dialog, String p_strCaption, Runnable p_runnable) throws GException {
		super(p_dialog, "Please Wait . . .", true);

		if(p_runnable instanceof GRunnable) {
			m_dimension = new Dimension(350, 150);
		}

		m_strCaption = p_strCaption;

		init(p_runnable);
	}

	public GDialogWaiting(GDialog p_dialog, GRunnable p_runnable, Dimension p_dimension) throws GException {
		super(p_dialog, "Please Wait . . .", true);

		m_dimension = p_dimension;

		init(p_runnable);
	}

	private void init(Runnable p_runnable) throws GException {
		try {
			me = this;

			setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
			setResizable(false);

			buildLayoutDialog(p_runnable);
			registerListener();

			setSize(m_dimension);
			buildLocation();

			runProcess(p_runnable);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	private void runProcess(final Runnable p_runnable) throws GException {
		try {
			if(!s_bolThreadMainRunning) {
				m_threadMain = new Thread() {
					public void run() {
						s_bolThreadMainRunning = true;

						p_runnable.run();

						dispose();
					}
				};

				main = me;

				m_threadMain.start();

				runThreadUpdateInfo(p_runnable);

				setVisible(true);

			} else {
				p_runnable.run();
			}
		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	private void runThreadUpdateInfo(final Runnable p_runnable) throws GException {
		try {
			if(m_lblInfo != null) {
				m_threadUpdateInfo = new Thread() {
					public void run() {
						try {
							while(true) {
								if(p_runnable instanceof GRunnable) {
									String strText_Old = m_lblInfo.getText();
									String strLabelInfo_New = ((GRunnable)p_runnable).getLabelInfo();

									if(strText_Old.compareTo(strLabelInfo_New) != 0) {
										m_lblInfo.setText(strLabelInfo_New);
									}
								}
							}
						} catch(Exception exception) {
							ExceptionHandler.display(exception);
						}
					}
				};

				m_threadUpdateInfo.start();
			}
		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	private void buildLocation() {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

		int intX_Parent = new Double(dim.getWidth()).intValue();
		int intY_Parent = new Double(dim.getHeight()).intValue();

		intX_Parent = intX_Parent / 2;
		intY_Parent = intY_Parent / 2;

		int intWidth = getWidth();
		int intHeight = getHeight();

		intWidth = intWidth / 2;
		intHeight = intHeight / 2;

		int intX = intX_Parent - intWidth;
		int intY = intY_Parent - intHeight;

		setLocation(intX, intY);
	}

	private void buildLayoutDialog(Runnable p_runnable) throws GException {
		try {
			Container pane = getContentPane();
			pane.setLayout(new BorderLayout());

			JLabel label = new JLabel(m_strCaption);
			label.setHorizontalTextPosition(GLabel.CENTER);
			pane.add(label, BorderLayout.NORTH);

			label = new JLabel(GImageIcon.getIconProcess());
			label.setHorizontalTextPosition(GLabel.CENTER);
			pane.add(label, BorderLayout.CENTER);

			if(p_runnable instanceof GRunnable) {
				JPanel paneSouth = new JPanel(new GridLayout(2, 1, 5, 5));

				m_lblInfo = new GLabel("BEGIN", GLabel.CENTER);
				paneSouth.add(m_lblInfo);

				m_btnStopTask = new GButton("Stop Task");
				paneSouth.add(m_btnStopTask);

				pane.add(paneSouth, BorderLayout.SOUTH);
			}
		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public void dispose() {
		try {
			if(m_lblInfo != null) {
				Method m = Thread.class.getDeclaredMethod("stop0", new Class[]{Object.class});
				m.setAccessible(true);
				m.invoke(m_threadUpdateInfo, new ThreadDeath());
			}

			Method m = Thread.class.getDeclaredMethod("stop0", new Class[]{Object.class});
			m.setAccessible(true);

			if(m_threadMain != null) {
				m.invoke(m_threadMain, new ThreadDeath());
			}

			me.setVisible(false);

		} catch (InvocationTargetException e) {

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);

		} finally {
			m_threadMain = null;
			s_bolThreadMainRunning = false;

			dispose_Main();

			super.dispose();
		}
	}


	public void dispose_SUPER() {
		if(main != null) {
			m_threadMain = null;
			s_bolThreadMainRunning = false;

			super.dispose();
		}
	}


	public static void dispose_Main() {
		if(main != null) {
			main.dispose_SUPER();
			main = null;
		}
	}

	private void registerListener() throws GException {
		if(m_btnStopTask != null) {
			m_btnStopTask.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent p_event) {
					try {
						GLog.info("Stop Task");

						me.dispose();

					} catch(Exception exception) {
						ExceptionHandler.display(exception);
					}
				}
			});
		}
	}
}
