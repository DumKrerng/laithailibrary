package com.laithailibrary.clientlibrary.ui.swing;

import com.laithailibrary.clientlibrary.ui.*;
import com.laithailibrary.clientlibrary.ui.support.*;
import com.laithailibrary.sharelibrary.datatableview.*;
import com.laithailibrary.sharelibrary.db.dbobject.*;
import com.laithailibrary.sharelibrary.locale.*;
import com.laithailibrary.sharelibrary.support.*;
import exc.*;
import msg.*;
import java.awt.*;
import java.awt.event.*;

public class GTextSelectorDialog<T extends DBObject> extends GDialog {

	private GTextSelectorDialog me;
	private GTextSelector<T> m_textselector;
	private Class<T> m_clsTextSelector;
	private DataVectorTable m_dvtSelector;

	private String m_strCaption = "Selector";

	private boolean m_bolClickOK = false;
	private AllowSelectedInvalid m_allowSelectedInvalid = AllowSelectedInvalid.Yes;

	private GButton m_btnOK;
	private GButton m_btnCancel;

	public GTextSelectorDialog(GFrame p_frame, Class<T> p_clsTextSelector, String p_strTitle, String p_strCaption, DataVectorTable p_dvtSelector) throws GException {
		super(p_frame, p_strTitle, UIDimension.DIM_450_200, true);

		m_clsTextSelector = p_clsTextSelector;
		m_strCaption = p_strCaption;
		m_dvtSelector = p_dvtSelector;

		init();
	}

	public GTextSelectorDialog(GFrame p_frame, Class<T> p_clsTextSelector, String p_strTitle, String p_strCaption, DataVectorTable p_dvtSelector,
		AllowSelectedInvalid p_allowSelectedInvalid) throws GException {

		super(p_frame, p_strTitle, UIDimension.DIM_450_200, true);

		m_clsTextSelector = p_clsTextSelector;
		m_strCaption = p_strCaption;
		m_dvtSelector = p_dvtSelector;
		m_allowSelectedInvalid = p_allowSelectedInvalid;

		init();
	}

	public GTextSelectorDialog(GFrame p_frame, Class<T> p_clsTextSelector, String p_strTitle, String p_strCaption, DataVectorTable p_dvtSelector,
		Dimension p_dimension) throws GException {

		super(p_frame, p_strTitle, p_dimension, true);

		m_clsTextSelector = p_clsTextSelector;
		m_strCaption = p_strCaption;
		m_dvtSelector = p_dvtSelector;

		init();
	}

	public GTextSelectorDialog(GDialog p_dialog, Class<T> p_clsTextSelector, String p_strTitle, String p_strCaption, DataVectorTable p_dvtSelector) throws GException {
		super(p_dialog, p_strTitle, UIDimension.DIM_450_200, true);

		m_clsTextSelector = p_clsTextSelector;
		m_strCaption = p_strCaption;
		m_dvtSelector = p_dvtSelector;

		init();
	}

	public GTextSelectorDialog(GDialog p_dialog, Class<T> p_clsTextSelector, String p_strTitle, String p_strCaption, DataVectorTable p_dvtSelector,
		AllowSelectedInvalid p_allowSelectedInvalid) throws GException {

		super(p_dialog, p_strTitle, UIDimension.DIM_450_200, true);

		m_clsTextSelector = p_clsTextSelector;
		m_strCaption = p_strCaption;
		m_dvtSelector = p_dvtSelector;
		m_allowSelectedInvalid = p_allowSelectedInvalid;

		init();
	}

	public GTextSelectorDialog(GDialog p_dialog, Class<T> p_clsTextSelector, String p_strTitle, String p_strCaption, DataVectorTable p_dvtSelector,
		Dimension p_dimension) throws GException {

		super(p_dialog, p_strTitle, p_dimension, true);

		m_clsTextSelector = p_clsTextSelector;
		m_strCaption = p_strCaption;
		m_dvtSelector = p_dvtSelector;

		init();
	}

	public GTextSelectorDialog(GDialog p_dialog, Class<T> p_clsTextSelector, String p_strTitle, String p_strCaption, DataVectorTable p_dvtSelector,
		Dimension p_dimension, AllowSelectedInvalid p_allowSelectedInvalid) throws GException {

		super(p_dialog, p_strTitle, p_dimension, true);

		m_clsTextSelector = p_clsTextSelector;
		m_strCaption = p_strCaption;
		m_dvtSelector = p_dvtSelector;
		m_allowSelectedInvalid = p_allowSelectedInvalid;

		init();
	}

	private void init() throws GException {
		try {
			me = this;

			buildDialogLayout();

			registerListener();

			setVisible(true);

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	private void buildDialogLayout() throws GException {
		try {
			Container pane = getContentPane();
			pane.setLayout(new BorderLayout());

			pane.add(getMainPane(), BorderLayout.CENTER);
			pane.add(getButtonPane(), BorderLayout.SOUTH);

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	private GPanel getMainPane() throws GException {
		try {
			Mandatory mandatory = Mandatory.No;

			if(m_allowSelectedInvalid == AllowSelectedInvalid.Yes) {
				mandatory = Mandatory.Yes;
			}

			GPanel pane = new GPanel(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets = new Insets(5, 10, 5, 10);

			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.anchor = GridBagConstraints.EAST;
			GLabel label = new GLabel(m_strCaption);
			pane.add(label, gbc);

			gbc.gridx = 1;
			gbc.gridy = 0;
			gbc.anchor = GridBagConstraints.EAST;
			m_textselector = new GTextSelector<>(m_clsTextSelector, "", 20, mandatory);
			pane.add(m_textselector, gbc);

			return pane;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	private GPanel getButtonPane() throws GException {
		try {
			GPanel pane = new GPanel(new GridLayout());

			m_btnOK = new GButton("OK");
			pane.add(m_btnOK);

			m_btnCancel = new GButton("Cancel");
			pane.add(m_btnCancel);

			return pane;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public boolean isClickOK() {
		return m_bolClickOK;
	}

	public T getSelectedID() throws GException {
		return m_textselector.getSelectedID();
	}

	private void registerListener() throws GException {
		m_textselector.addActionEvenSelectionMasterData(new ActionEventSelectionMasterData() {
			@Override
			public void onSelectorMasterData(EvenSelectionMasterData p_event) {
				try {
					m_textselector.displaySelector(me, m_dvtSelector, "Selector");

				} catch(Exception exception) {
				  ExceptionHandler.display(exception);
				}
			}

			@Override
			public void onGoToMasterData(EvenSelectionMasterData p_event) {
				new MsgDialog("Not support!!!");
			}
		});

		m_btnOK.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent p_event) {
				try {
					if(m_allowSelectedInvalid == AllowSelectedInvalid.Yes) {
						T temp = getSelectedID();

						if(temp.isInvalid()) {
							String strError = "You must be select to continue.";

							if(AppClientUtilities.getLocale() == GLocale.Thai) {
								strError = "คุณต้องเลือกข้อมูลก่อน";
							}

							throw new GException(strError);
						}
					}

					m_bolClickOK = true;

					me.close();

				} catch(Exception exception) {
				  ExceptionHandler.display(exception);
				}
			}
		});

		m_btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent p_event) {
				try {
				  m_bolClickOK = false;

				  me.close();

				} catch(Exception exception) {
				  ExceptionHandler.display(exception);
				}
			}
		});
	}

	public enum AllowSelectedInvalid {
		Yes,
		No
	}
}
