package com.laithailibrary.clientlibrary.ui.swing;

import javax.swing.*;
import java.awt.*;
import com.laithailibrary.sharelibrary.file.*;
import exc.*;

public class GSplash extends JFrame {

	private JLabel m_lblMessage;

	private int m_intWidth = 700;
	private int m_intHeight = 300;

	public GSplash(GImage p_image) throws GException {
		init(p_image, 700, 300);
	}

	public GSplash(GImage p_image, int p_intWidth, int p_intHeight) throws GException {
		init(p_image, p_intWidth, p_intHeight);
	}

	public void init(GImage p_image, int p_intWidth, int p_intHeight) throws GException {
		try {
			m_intWidth = p_intWidth;
			m_intHeight = p_intHeight + 30;

			setUndecorated(true); // Remove title bar
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setSize(m_intWidth, m_intHeight);

			buildLayout(p_image);

			Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
			int intX_Parent = (new Double(dim.getWidth())).intValue();
			int intY_Parent = (new Double(dim.getHeight())).intValue();
			intX_Parent /= 2;
			intY_Parent /= 2;
			int intWidth = getWidth();
			int intHeight = getHeight();
			intWidth /= 2;
			intHeight /= 2;
			int intX = intX_Parent - intWidth;
			int intY = intY_Parent - intHeight;

			setLocation(intX, intY);
			setVisible(true);

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	private void buildLayout(GImage p_image) throws GException {
		try {
			Container pane = getContentPane();
			pane.setLayout(new BorderLayout());

			GImageViewer imageviewer = new GImageViewer();
			imageviewer.setImage(p_image, m_intWidth, true);

			add(imageviewer, BorderLayout.CENTER);

			m_lblMessage = new JLabel();
			m_lblMessage.setPreferredSize(new Dimension(m_intWidth, 30));
			m_lblMessage.setBorder(BorderFactory.createLoweredBevelBorder());
			m_lblMessage.setText("Loading . . .");
			add(m_lblMessage, BorderLayout.SOUTH);

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public void setMessage(String p_strMessage) {
		m_lblMessage.setText(p_strMessage);
	}
}
