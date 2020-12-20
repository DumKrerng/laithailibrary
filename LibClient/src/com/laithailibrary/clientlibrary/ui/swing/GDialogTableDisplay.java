package com.laithailibrary.clientlibrary.ui.swing;

import java.awt.*;
import java.awt.event.*;
import com.laithailibrary.clientlibrary.ui.*;
import com.laithailibrary.sharelibrary.datatableview.*;
import exc.*;

/**
 * Created by dumkrerng on 22/5/2559.
 */
public class GDialogTableDisplay extends GDialog {

	private GDialogTableDisplay me;

	private GTableDisplay m_tabledisplay;

	public GDialogTableDisplay(GFrame p_frame, String p_strDialogTitle, String p_strTableTitle, DataVectorTable p_datatable,
		Dimension p_dimension) throws GException {

		super(p_frame, p_strDialogTitle);

		init(p_datatable, p_strTableTitle, p_dimension);
	}

	public GDialogTableDisplay(GDialog p_dialog, String p_strDialogTitle, String p_strTableTitle, DataVectorTable p_datatable,
		Dimension p_dimension) throws GException {

		super(p_dialog, p_strDialogTitle);

		init(p_datatable, p_strTableTitle, p_dimension);
	}

	private void init(DataVectorTable p_datatable, String p_strTableTitle, Dimension p_dimension) throws GException {
		try {
			me = this;

			buildDialogLayout(p_datatable, p_strTableTitle);

			setSize(p_dimension);
			setVisible(true);

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	private void buildDialogLayout(DataVectorTable p_datatable, String p_strTableTitle) throws GException {
		try {
			Container pane = getContentPane();
			pane.setLayout(new BorderLayout());

			m_tabledisplay = new GTableDisplay(p_datatable, p_strTableTitle);
			pane.add(m_tabledisplay, BorderLayout.CENTER);

			GButton btnClose = new GButton("Close");
			pane.add(btnClose, BorderLayout.SOUTH);

			btnClose.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent p_event) {
					me.close();
				}
			});
		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}
}
