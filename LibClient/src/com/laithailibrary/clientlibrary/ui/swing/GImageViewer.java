package com.laithailibrary.clientlibrary.ui.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import com.laithailibrary.sharelibrary.file.*;
import exc.*;

/**
 * Created by dumkrerng on 31/10/2560.
 */
public class GImageViewer extends JPanel {

	private GDialog m_dialog = null;

	private GImage m_image_OriginalSize = null;
//	private BufferedImage m_image_OriginalSize = null;
	private JLabel m_viewer;
	private int m_intMaxWidth = 0;
	private boolean m_bolFixBorder = false;

	public GImageViewer() throws GException {
		init(null, 0, false);
	}

	public GImageViewer(GDialog p_dialog) throws GException {
		init(p_dialog, 0, false);
	}

	public GImageViewer(GDialog p_dialog, int p_intMaxWidth) throws GException {
		init(p_dialog, p_intMaxWidth, false);
	}

	public GImageViewer(GDialog p_dialog, int p_intMaxWidth, boolean p_bolFixBorder) throws GException {
		init(p_dialog, p_intMaxWidth, p_bolFixBorder);
	}

	public GImageViewer(GDialog p_dialog, GImage p_image, int p_intMaxWidth) throws GException {
		init(p_dialog, 0, false);

		setImage(p_image, p_intMaxWidth, false);
	}

	public GImageViewer(GDialog p_dialog, GImage p_image, int p_intMaxWidth, boolean p_bolFixBorder) throws GException {
		init(p_dialog, 0, p_bolFixBorder);

		setImage(p_image, p_intMaxWidth, p_bolFixBorder);
	}

	public GImageViewer(GDialog p_dialog, String p_strFullName, int p_intMaxWidth) throws GException {
		init(p_dialog, p_intMaxWidth, false);

		GImage image = new GImage(p_strFullName, false);
		setImage(image, p_intMaxWidth, false);
	}

	public GImageViewer(GDialog p_dialog, String p_strFullName, int p_intMaxWidth, boolean p_bolFixBorder) throws GException {
		init(p_dialog, p_intMaxWidth, p_bolFixBorder);

		GImage image = new GImage(p_strFullName, p_bolFixBorder);
		setImage(image, p_intMaxWidth, p_bolFixBorder);
	}

//	private void init() throws GException {
//		try {
//			super.setLayout(new BorderLayout());
//
//			m_viewer = new JLabel("Image Viewer", GLabel.CENTER);
//			m_viewer.setBorder(BorderFactory.createLoweredBevelBorder());
//			super.add(m_viewer, BorderLayout.CENTER);
//
//		} catch(Exception exception) {
//		  ExceptionHandler.display(exception);
//		  throw new GException(exception);
//		}
//	}

	private void init(GDialog p_dialog, int p_intMaxWidth, boolean p_bolFixBorder) throws GException {
		try {
			m_dialog = p_dialog;
			m_intMaxWidth = p_intMaxWidth;
			m_bolFixBorder = p_bolFixBorder;

			super.setLayout(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();

			gbc.gridx = 0;
			gbc.gridy = 0;
			m_viewer = new JLabel("Image Viewer", GLabel.CENTER);

			if(p_intMaxWidth > 0) {
				int intWidth = p_intMaxWidth;
				int intHeight = p_intMaxWidth;

				m_viewer.setPreferredSize(new Dimension(intWidth, intHeight));
			}

			m_viewer.setBorder(BorderFactory.createLoweredBevelBorder());
			super.add(m_viewer, gbc);

			registerEventListener();

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public void setImage(String p_strFullName, int p_intMaxWidth) throws GException {
		try {
			GImage image = new GImage(p_strFullName, m_bolFixBorder);
			setImage(image, p_intMaxWidth, m_bolFixBorder);

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public void setImage(String p_strFullName) throws GException {
		try {
			GImage image = new GImage(p_strFullName, m_bolFixBorder);
			setImage(image, m_intMaxWidth, m_bolFixBorder);

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public void setImage(GImage p_image, int p_intMaxWidth, boolean p_bolFixBorder) throws GException {
		try {
			if(p_image.isValid()) {
				m_viewer.setText("");
				m_image_OriginalSize = p_image;
				m_bolFixBorder = p_bolFixBorder;

				ImageIcon imageicon = new ImageIcon(p_image.toBufferedImage(p_intMaxWidth, m_bolFixBorder));
				m_viewer.setIcon(imageicon);

			} else {
				resetImage();
			}
		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public GImage getImage() {
		if(m_image_OriginalSize == null) {
			return new GImage();
		}

		return m_image_OriginalSize;
	}

	public void setURLImage(String p_strURL, int p_intMaxWidth, boolean p_bolFixBorder) throws GException {
		try {
			if(p_strURL.length() > 0) {
				m_viewer.setText("");
				m_bolFixBorder = p_bolFixBorder;

				ImageIcon imageicon = new ImageIcon(GImage.toBufferedImage(p_strURL, p_intMaxWidth, m_bolFixBorder));
				m_viewer.setIcon(imageicon);

				m_image_OriginalSize = new GImage();
				m_image_OriginalSize.setImage_ByURL(p_strURL);

			} else {
				resetImage();
			}
		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public void resetImage() throws GException {
		try {
			m_viewer.setIcon(null);
			m_viewer.setText("Image Viewer");

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	private void registerEventListener() throws GException {
		m_viewer.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent p_event) {
				try {
					super.mouseClicked(p_event);
					int intClickCount = p_event.getClickCount();

					if(intClickCount == 2) {
						if(m_image_OriginalSize != null) {
							if(m_dialog != null) {
								m_dialog.executeProcess(new OnDialogViewer());
							}
						}
					}
				} catch(Exception exception) {
					ExceptionHandler.display(exception);
				}
			}
		});
	}

	private class OnDialogViewer implements Runnable {

		public void run() {
			try {
				new DialogViewer(m_image_OriginalSize.toBufferedImage(-1, false));

			} catch(Exception exception) {
			  ExceptionHandler.display(exception);
			}
		}
	}

	private class DialogViewer extends GDialog {

		private DialogViewer i_dialogviewer;
		private ImageIcon i_imageicon_Original = null;

		private DialogViewer(BufferedImage p_image) throws GException {
			super(m_dialog, "Image Viewer", new Dimension(900, 650), true);

			try {
				i_dialogviewer = this;
				i_imageicon_Original = new ImageIcon(p_image);

				super.setLayout(new BorderLayout());
				JLabel viewer = new JLabel("");
				viewer.setHorizontalAlignment(JLabel.CENTER);
				JScrollPane scroller = new JScrollPane(viewer);
				scroller.setPreferredSize(new Dimension(870, 640));

				viewer.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent p_event) {
						try {
							super.mouseClicked(p_event);
							int intClickCount = p_event.getClickCount();

							if(intClickCount == 2) {
								i_dialogviewer.executeProcess(new OnResetSizePresent(viewer, i_dialogviewer.getWidth() - 10));
							}
						} catch(Exception exception) {
						  ExceptionHandler.display(exception);
						}
					}
				});

				super.add(scroller, BorderLayout.CENTER);

				i_dialogviewer.executeProcess(new OnResetSizePresent(viewer, i_dialogviewer.getWidth() - 10));

				super.setVisible(true);

			} catch(Exception exception) {
				ExceptionHandler.display(exception);
				throw new GException(exception);
			}
		}

		private class OnResetSizePresent implements Runnable {

			private JLabel i_viewer;
			private int i_intWidth;

			private OnResetSizePresent(JLabel p_viewer, int p_intWidth) {
				i_viewer = p_viewer;
				i_intWidth = p_intWidth;
			}

			public void run() {
				try {
					Dimension dimension = GImage.prepareDimensionPresent(i_imageicon_Original.getImage(), i_intWidth, false);

					Image temp = i_imageicon_Original.getImage().getScaledInstance(dimension.width, dimension.height, Image.SCALE_SMOOTH);
					BufferedImage resized = GImage.toBufferedImage(temp, i_intWidth, false);

					ImageIcon imageicon = new ImageIcon(resized);
					i_viewer.setIcon(imageicon);

				} catch(Exception exception) {
				  ExceptionHandler.display(exception);
				}
			}
		}
	}
}
