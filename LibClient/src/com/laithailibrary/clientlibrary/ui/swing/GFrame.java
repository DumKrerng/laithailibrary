package com.laithailibrary.clientlibrary.ui.swing;

import javax.swing.*;
import java.awt.*;
import com.laithailibrary.clientlibrary.ui.support.*;
import com.laithailibrary.sharelibrary.support.*;
import exc.*;

/**
 * Created with IntelliJ IDEA.
 * User: dumkrerng
 * Date: 5/17/13
 * Time: 11:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class GFrame extends JFrame {

	public GFrame() {}

	public GFrame(String p_strTitle) {
		init00(p_strTitle);
	}

	private void init00(String p_strTitle) {
		String strTitleProgram = GUtilities.getProName() + " :[" + p_strTitle +"]";
		super.setTitle(strTitleProgram);
	}

	public void executeProcess_WithDialogWaiting(Runnable p_runnable) throws GException {
		try {
			new GDialogWaiting(this, p_runnable);

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
