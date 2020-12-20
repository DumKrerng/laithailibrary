package com.laithailibrary.clientlibrary.ui.swing;

import com.laithailibrary.clientlibrary.ui.support.*;
import javax.swing.*;
import java.awt.*;

/**
 * Created by dumkrerng on 13/10/2560.
 */
public class GMenuItem extends JMenuItem {

	public GMenuItem() {
		init();
	}

	public GMenuItem(Icon p_icon) {
		super(p_icon);

		init();
	}

	public GMenuItem(String p_text) {
		super(p_text);

		init();
	}

	public GMenuItem(String p_text, Icon p_icon) {
		super(p_text, p_icon);

		init();
	}

	public GMenuItem(String p_text, int p_mnemonic) {
		super(p_text, p_mnemonic);

		init();
	}

	private void init() {
		setFont(new Font(GFontUtilities.FONT_NAME_NORMAL, Font.PLAIN, 20));
	}
}
