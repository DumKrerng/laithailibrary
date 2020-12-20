package com.laithailibrary.clientlibrary.ui.swing;

import com.laithailibrary.clientlibrary.ui.support.*;
import javax.swing.*;
import java.awt.*;

public class GMenu extends JMenu {

	private GMenu() {}

	public GMenu(String p_strCaption) {
		super(p_strCaption);

		init();
	}

	private void init() {
		setFont(new Font(GFontUtilities.FONT_NAME_NORMAL, Font.PLAIN, 20));
	}
}
