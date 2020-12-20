package com.laithailibrary.clientlibrary.ui.swing;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;
import com.laithailibrary.clientlibrary.ui.support.*;
import com.laithailibrary.sharelibrary.collection.*;
import com.laithailibrary.sharelibrary.locale.*;
import com.laithailibrary.sharelibrary.support.*;

public class GConfirmDialog {

	private GConfirmDialog() {}

	public static boolean confirmAction(String p_strTitle, String p_strMessage, GLocale p_locale) {
		return baseConfirmAction(null, p_strTitle ,p_strMessage, p_locale);
	}

	public static boolean confirmAction(JFrame p_frame, String p_strTitle, String p_strMessage, GLocale p_locale) {
		return baseConfirmAction(p_frame, p_strTitle ,p_strMessage, p_locale);
	}

	public static boolean confirmAction(GFrame p_frame, String p_strTitle, String p_strMessage) {
		return baseConfirmAction(p_frame, p_strTitle ,p_strMessage, AppClientUtilities.getLocale());
	}

	public static boolean confirmAction(JDialog p_dialog, String p_strTitle, String p_strMessage, GLocale p_locale) {
		return baseConfirmAction(p_dialog, p_strTitle ,p_strMessage, p_locale);
	}

	public static boolean confirmAction(GDialog p_dialog, String p_strTitle, String p_strMessage) {
		return baseConfirmAction(p_dialog, p_strTitle ,p_strMessage, AppClientUtilities.getLocale());
	}

	private static boolean baseConfirmAction(Component p_component_parent, String p_strTitle, String p_strMessage, GLocale p_locale) {
		StringTokenizer tokenizer = new StringTokenizer(p_strMessage, "\n");

		List<String> lsMessages = new GList<>();

		while(tokenizer.hasMoreTokens()) {
			String strMessage = tokenizer.nextToken();
			lsMessages.add(strMessage);
		}

		GPanel pane = new GPanel(new GridLayout(lsMessages.size() + 1, 1, 1, 1));

		for(String strMessage : lsMessages) {
			GLabel label = new GLabel(strMessage, GLabel.CENTER);
			label.setFontSize(22);
			label.setFontStyle(GFontStyle.Normal);
			pane.add(label);
		}

		String strCaption = "Yes or No";

		if(p_locale == GLocale.Thai) {
			strCaption = "ใช่ หรือ ไม่";
		}

		GLabel label = new GLabel(strCaption, GLabel.CENTER);
		label.setFontSize(22);
		label.setForeground(Color.BLUE);
		pane.add(label);

		int intConfirmDialog = JOptionPane.showConfirmDialog(p_component_parent, pane,p_strTitle, JOptionPane.YES_NO_OPTION,
			JOptionPane.QUESTION_MESSAGE);

		if(intConfirmDialog == JOptionPane.OK_OPTION) {
			return true;
		}

		return false;
	}
}
