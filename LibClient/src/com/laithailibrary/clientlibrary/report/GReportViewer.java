package com.laithailibrary.clientlibrary.report;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.laithailibrary.clientlibrary.ui.swing.*;
import com.laithailibrary.sharelibrary.report.*;
import exc.*;
import net.sf.jasperreports.view.*;

/**
 * Created by dumkrerng on 20/1/2558.
 */
public class GReportViewer {

	private GReportViewer() throws GException {}

	public static void viewReport(GFrame p_frame, String p_strTitle, GReport p_report) throws GException {
		viewReport(p_frame, p_strTitle, p_report, new Dimension(950, 700), false);
	}

	public static void viewReport(GFrame p_frame, String p_strTitle, GReport p_report, Dimension p_dimension) throws GException {
		viewReport(p_frame, p_strTitle, p_report, p_dimension, false);
	}

	public static void viewReport(GFrame p_frame, String p_strTitle, GReport p_report, boolean p_toplevel) throws GException {
		viewReport(p_frame, p_strTitle, p_report, new Dimension(950, 700), p_toplevel);
	}

	public static void viewReport(GFrame p_frame, String p_strTitle, GReport p_report, Dimension p_dimension, boolean p_toplevel) throws GException {
		try {
			final JasperViewer jasperviewer = new JasperViewer(p_report.getReport(), false);
			jasperviewer.setSize(p_dimension);
			jasperviewer.setFitPageZoomRatio();

			JDialog dialog = new JDialog(p_frame, p_toplevel);
			dialog.setTitle(p_strTitle);
			dialog.getContentPane().add(jasperviewer.getContentPane());

			dialog.addWindowListener(new WindowListener() {
				public void windowOpened(WindowEvent p_event) {}

				public void windowClosing(WindowEvent p_event) {
					try {
						jasperviewer.dispose();

					} catch(Exception exception) {
					  ExceptionHandler.display(exception);
					}
				}

				public void windowClosed(WindowEvent p_event) {}
				public void windowIconified(WindowEvent p_event) {}
				public void windowDeiconified(WindowEvent p_event) {}
				public void windowActivated(WindowEvent p_event) {}
				public void windowDeactivated(WindowEvent p_event) {}
			});

			dialog.setSize(jasperviewer.getSize());
			dialog.setVisible(true);

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public static void viewReport(GDialog p_dialog, String p_strTitle, GReport p_report) throws GException {
		viewReport(p_dialog, p_strTitle, p_report, new Dimension(950, 700), false);
	}

	public static void viewReport(GDialog p_dialog, String p_strTitle, GReport p_report, Dimension p_dimension) throws GException {
		viewReport(p_dialog, p_strTitle, p_report, p_dimension, false);
	}

	public static void viewReport(GDialog p_dialog, String p_strTitle, GReport p_report, boolean p_toplevel) throws GException {
		viewReport(p_dialog, p_strTitle, p_report, new Dimension(950, 700), p_toplevel);
	}

	public static void viewReport(GDialog p_dialog, String p_strTitle, GReport p_report, Dimension p_dimension, boolean p_toplevel) throws GException {
		try {
			final JasperViewer jasperviewer = new JasperViewer(p_report.getReport(), false);
			jasperviewer.setSize(p_dimension);
			jasperviewer.setFitPageZoomRatio();

			JDialog dialog = new JDialog(p_dialog, p_toplevel);
			dialog.setTitle(p_strTitle);
			dialog.getContentPane().add(jasperviewer.getContentPane());

			dialog.addWindowListener(new WindowListener() {
				public void windowOpened(WindowEvent p_event) {}

				public void windowClosing(WindowEvent p_event) {
					try {
						jasperviewer.dispose();

					} catch(Exception exception) {
					  ExceptionHandler.display(exception);
					}
				}

				public void windowClosed(WindowEvent p_event) {}
				public void windowIconified(WindowEvent p_event) {}
				public void windowDeiconified(WindowEvent p_event) {}
				public void windowActivated(WindowEvent p_event) {}
				public void windowDeactivated(WindowEvent p_event) {}
			});

			dialog.setSize(jasperviewer.getSize());
			dialog.setVisible(true);

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}
}
