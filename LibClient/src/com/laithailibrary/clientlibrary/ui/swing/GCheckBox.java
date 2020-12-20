package com.laithailibrary.clientlibrary.ui.swing;

import javax.swing.*;
import java.awt.*;
import com.laithailibrary.sharelibrary.db.dbobject.*;

public class GCheckBox extends JCheckBox {

	public GCheckBox (String p_strCaption) {
		super(p_strCaption);

		init();
	}

	public GCheckBox(Icon p_icon) {
		super(p_icon);

		init();
	}

	public GCheckBox(String p_strCaption, boolean p_selected) {
		super(p_strCaption);

		init();

		setSelected(p_selected);
	}

	public GCheckBox(Icon p_icon, boolean p_selected) {
		super(p_icon, p_selected);
	}

	private void init() {
		setFont(new Font("TH Sarabun New", Font.BOLD, 20));
	}

	public void setSelected(DBBoolean p_dbBoolean) {
		setSelected(p_dbBoolean.getBoolean());
	}

	public DBBoolean getDBBoolean() {
		if(isSelected()) {
			return new DBBoolean(true);
		}

		return new DBBoolean(false);
	}
}
