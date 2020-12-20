package com.laithailibrary.clientlibrary.ui.swing;

import com.laithailibrary.clientlibrary.ui.support.GDialogWaiting;
import com.laithailibrary.clientlibrary.ui.swing.support.*;
import com.laithailibrary.sharelibrary.collection.*;
import com.laithailibrary.sharelibrary.support.*;
import exc.ExceptionHandler;
import exc.GException;
import com.laithailibrary.clientlibrary.ui.UIDimension;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dumkrerng
 * Date: 5/13/12
 * Time: 9:44 AM
 * To change this template use File | Settings | File Templates.
 */
public class GDialog extends JDialog {

	public GDialog() {
		setSize(UIDimension.DIM_800_600);
	}

	public GDialog(Frame p_frame, String p_strTitle, boolean modal) {
		super(p_frame, "", modal);

		init00(p_strTitle);

		setSize(UIDimension.DIM_800_600);
	}

	public GDialog(Frame p_frame, String p_strTitle) {
		super(p_frame, "", true);

		init00(p_strTitle);

		setSize(UIDimension.DIM_800_600);
	}

	public GDialog(Frame p_frame, String p_strTitle, Dimension p_dimension) {
		super(p_frame, "", true);

		init00(p_strTitle);

		setSize(p_dimension);
	}

	public GDialog(Frame p_frame, String p_strTitle, Dimension p_dimension, boolean modal) {
		super(p_frame, "", modal);

		init00(p_strTitle);

		setSize(p_dimension);
	}

	public GDialog(Dialog p_dialog, String p_strTitle) {
		super(p_dialog, "", true);

		init00(p_strTitle);

		setSize(UIDimension.DIM_800_600);
	}

	public GDialog(GDialog p_dialog, String p_strTitle) {
		super(p_dialog, "", true);

		init00(p_strTitle);

		setSize(UIDimension.DIM_800_600);
	}

	public GDialog(GDialog p_dialog, String p_strTitle, Dimension p_dimension) {
		super(p_dialog, "", true);

		init00(p_strTitle);

		setSize(p_dimension);
	}

	public GDialog(Dialog p_dialog, String p_strTitle, boolean modal) {
		super(p_dialog, "", modal);

		init00(p_strTitle);

		setSize(UIDimension.DIM_800_600);
	}

	public GDialog(Dialog p_dialog, String p_strTitle, Dimension p_dimension, boolean modal) {
		super(p_dialog, "", modal);

		init00(p_strTitle);

		setSize(p_dimension);
	}

	private void init00(String p_strTitle) {
		String strTitleProgram = GUtilities.getProName() + " :[" + p_strTitle +"]";
		super.setTitle(strTitleProgram);

		super.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}

	public void setSize(Dimension p_dimension) {
		super.setSize(p_dimension);

		buildLocation();
	}

	public void setSize(int width, int height) {
		super.setSize(width, height);

		buildLocation();
	}

	public void setVisible(boolean p_visible) {
		if(p_visible) {
			GDialogWaiting.dispose_Main();
		}

		super.setVisible(p_visible);
	}

	public void setVisible(boolean p_visible, boolean p_bolDoPopulateTestFieldDefaultData) {
		if(p_visible) {
			GDialogWaiting.dispose_Main();
		}

		if(p_bolDoPopulateTestFieldDefaultData) {
			setTextFieldDefaultData();
		}

		super.setVisible(p_visible);
	}

	protected void buildLocation() {
		int intX_Parent = getParent().getX();
		int intY_Parent = getParent().getY();

		int intWidth_Parent = getParent().getWidth();
		int intHeight_Parent = getParent().getHeight();

		intWidth_Parent = intWidth_Parent / 2;
		intHeight_Parent = intHeight_Parent / 2;

		int intWidth_CenterParent = intX_Parent + intWidth_Parent;
		int intHeight_CenterParent = intY_Parent + intHeight_Parent;

		int intWidth = getWidth();
		int intHeight = getHeight();

		intWidth = intWidth / 2;
		intHeight = intHeight / 2;

		int intX = intWidth_CenterParent - intWidth;
		int intY = intHeight_CenterParent - intHeight;

		setLocation(intX, intY);
	}

	public void setLocation(int p_intX, int p_intY) {
		if(p_intX <= 0
			|| p_intY <= 0) {

			p_intX = 70;
			p_intY = 70;
		}

		super.setLocation(p_intX, p_intY);
	}

	protected void setTextFieldDefaultData() {
		try {
			List<GTextField> lsGTextFields = getGTextFields();

			for(GTextField textfield : lsGTextFields) {
				if(textfield instanceof GTextSelector) {
					DefaultDataInTextFieldManager.populateDefaultData((GTextSelector)textfield);

				} else {
					DefaultDataInTextFieldManager.populateDefaultData(textfield);
				}
			}
		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		}
	}

	private List<GTextField> getGTextFields() throws GException {
		try {
			List<GTextField> lsGTextFields = new GList<>();
			Container container = getContentPane();
			Component[] components = container.getComponents();

			for(Component compo : components) {
				if(compo instanceof JPanel) {
					lsGTextFields = getGTextFields((JPanel)compo, lsGTextFields);
				}
			}

			return lsGTextFields;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	private List<GTextField> getGTextFields(JPanel p_panel, List<GTextField> p_lsGTextFields) throws GException {
		try {
			Component[] components = p_panel.getComponents();

			for(Component component : components) {
				if(component instanceof JPanel) {
					p_lsGTextFields = getGTextFields((JPanel)component, p_lsGTextFields);

				} else if(component instanceof JTabbedPane) {
					p_lsGTextFields = getGTextFields((JTabbedPane)component, p_lsGTextFields);

				} else if(component instanceof GTextSelector) {
					p_lsGTextFields.add((GTextSelector)component);

				} else if(component instanceof GTextField) {
					p_lsGTextFields.add((GTextField)component);
				}
			}

			return p_lsGTextFields;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	private List<GTextField> getGTextFields(JTabbedPane p_tabbed, List<GTextField> p_lsGTextFields) throws GException {
		try {
			Component[] components = p_tabbed.getComponents();

			for(Component component : components) {
				if(component instanceof JPanel) {
					p_lsGTextFields = getGTextFields((JPanel)component, p_lsGTextFields);

				} else if(component instanceof JTabbedPane) {
					p_lsGTextFields = getGTextFields((JTabbedPane)component, p_lsGTextFields);

				} else if(component instanceof GTextSelector) {
					p_lsGTextFields.add((GTextSelector)component);

				} else if(component instanceof GTextField) {
					p_lsGTextFields.add((GTextField)component);
				}
			}

			return p_lsGTextFields;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public void close() {
		onCloseDialog();

		dispose();
	}

	protected void onCloseDialog() {}

	public void executeProcess_WithDialogWaiting(Runnable p_runnable) throws GException {
		executeProcess_WithDialogWaiting("", p_runnable);
	}

	public void executeProcess_WithDialogWaiting(String p_strCaption, Runnable p_runnable) throws GException {
		try {
			new GDialogWaiting(this, p_strCaption, p_runnable);

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public void executeProcess(Runnable p_runnable) throws GException {
		try {
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

			p_runnable.run();

			setCursor(Cursor.getDefaultCursor());

		} catch (Exception exception) {
			setCursor(Cursor.getDefaultCursor());

		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}
}
