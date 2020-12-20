package com.laithailibrary.clientlibrary.ui.ucv;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import com.laithailibrary.clientlibrary.ui.*;
import com.laithailibrary.clientlibrary.ui.swing.*;
import com.laithailibrary.sharelibrary.collection.*;
import exc.*;

/**
 * Created by IntelliJ IDEA.
 * User: dumkrerng
 * Date: 6/8/11
 * Time: 10:15 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class UIControlView extends GDialog {

	private List<I_UIDataView> m_lsDataTableView_Child;

	public UIControlView(JFrame p_frame, String p_strTitle, boolean p_bolModal) {
		super(p_frame, p_strTitle, p_bolModal);

		try {
			initUIControlView();

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		}
	}

	public UIControlView(GDialog p_dialog, String p_strTitle, boolean p_bolModal) {
		super(p_dialog, p_strTitle, p_bolModal);

		try {
		  initUIControlView();

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		}
	}

	private void initUIControlView() throws GException {
		try {
			m_lsDataTableView_Child = new GList<>();

			buildMenuBar();

			this.setSize(UIDimension.DIM_800_600);

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	private void buildMenuBar() {
		JMenuBar menubar = new JMenuBar();

		JMenu menu = new JMenu("File");
//		menu.setFont(new Font("TH Sarabun New", Font.BOLD, 20));

		JMenuItem mi = new JMenuItem("Close");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		menu.add(mi);

		menubar.add(menu);
		super.setJMenuBar(menubar);
	}

	public Dimension getDimension() {
		return new Dimension(getWidth(), getHeight());
	}

	public<T extends I_UIDataView> void registerChildView(T p_t) {
		m_lsDataTableView_Child.add(p_t);
	}

	public void startNewSection() throws GException {
		reloadDataChildView();
	}

	public void reloadDataChildView() throws GException {
		if(!m_lsDataTableView_Child.isEmpty()) {
			for(I_UIDataView i_dataTableView : m_lsDataTableView_Child) {
				i_dataTableView.reloadDataView();
			}
		}
	}

	public void reloadDataChildView_NotReloadImageView() throws GException {
		if(!m_lsDataTableView_Child.isEmpty()) {
			for(I_UIDataView i_dataTableView : m_lsDataTableView_Child) {
				if(!(i_dataTableView instanceof ControlImageViewerPane)) {
					i_dataTableView.reloadDataView();
				}
			}
		}
	}
}
