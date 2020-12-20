package com.laithailibrary.clientlibrary.ui.swing;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: dumkrerng
 * Date: 6/23/12
 * Time: 8:38 AM
 * To change this template use File | Settings | File Templates.
 */
public class GButton extends JButton {

	public GButton() {
		super();

		init();
	}

	public GButton(String strDisplay) {
		super(strDisplay);

		init();
	}

	public GButton(String strDisplay, Icon p_icon) {
		super(strDisplay, p_icon);

		init();
	}

	private void init() {
	}
}
