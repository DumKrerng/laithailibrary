package com.laithailibrary.clientlibrary.ui.ucv;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import com.laithailibrary.clientlibrary.ui.swing.*;
import com.laithailibrary.sharelibrary.file.*;
import exc.*;

/**
 * Created by dumkrerng on 1/11/2560.
 */
public abstract class ControlImageViewerPane extends JPanel implements I_UIDataView {

	protected UIControlView m_uiControlView;
	protected String m_strHeader;
	protected int m_intMaxWidth = 0;

	protected GImageViewer m_imageviewer;

	public ControlImageViewerPane(UIControlView p_uiControlView, String p_strHeader) throws GException {
		m_uiControlView = p_uiControlView;
		m_strHeader = p_strHeader;

		buildPanelLayout();
	}

	public ControlImageViewerPane(UIControlView p_uiControlView, String p_strHeader, int p_intMaxWidth) throws GException {
		m_uiControlView = p_uiControlView;
		m_strHeader = p_strHeader;
		m_intMaxWidth = p_intMaxWidth;

		buildPanelLayout();
	}

	private void buildPanelLayout() throws GException {
		try {
			super.setLayout(new BorderLayout());

			GLabel lblTableHeader = new GLabel(m_strHeader);
			lblTableHeader.setHorizontalAlignment(JLabel.CENTER);
			lblTableHeader.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			super.add(lblTableHeader, BorderLayout.NORTH);

			m_imageviewer = new GImageViewer(m_uiControlView, m_intMaxWidth);
			super.add(m_imageviewer, BorderLayout.CENTER);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public void reloadDataView() throws GException {
//		m_uiControlView.executeProcess_WithDialogWaiting(new OnReloadImage());
		new OnReloadImage().run();
	}

	public void reloadDataChildView() throws GException {}
	public void reloadDataChildView_NotReloadImageView() throws GException {}

	public void clearDataView() throws GException {
		resetImage();
	}

	protected abstract void reloadImage() throws GException;

	public void setImage(GImage p_image, int p_intMaxWidth) throws GException {
		try {
			m_imageviewer.setImage(p_image, p_intMaxWidth, true);

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public void setImage(GImage p_image) throws GException {
		try {
			m_imageviewer.setImage(p_image, m_intMaxWidth, true);

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public void setImage_ByURL(String p_strURL) throws GException {
		try {
			m_imageviewer.setURLImage(p_strURL, m_intMaxWidth, true);

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public void setImage(String p_strFullName, int p_intMaxWidth) throws GException {
		try {
			GImage image = new GImage(p_strFullName, false);
			setImage(image, p_intMaxWidth);

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public void setImage(String p_strFullName) throws GException {
		try {
			GImage image = new GImage(p_strFullName, false);
			setImage(image, m_intMaxWidth);

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public GImage getImage() throws GException {
		return m_imageviewer.getImage();
	}

	public void resetImage() throws GException {
		try {
			m_imageviewer.resetImage();

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	private class OnReloadImage implements Runnable {

		@Override
		public void run() {
			try {
				reloadImage();

			} catch(Exception exception) {
			  ExceptionHandler.display(exception);
			}
		}
	}
}
