package com.laithailibrary.clientlibrary.ui;

/**
 * Created by IntelliJ IDEA.
 * User: dumkrerng
 * Date: 6/12/11
 * Time: 12:32 AM
 * To change this template use File | Settings | File Templates.
 */
public enum UIControlState {
	Insert("Insert Data"),
	Edit("Edit Data");

	private String StateLabel;

	UIControlState(String str) {
		StateLabel = str;
	}

	public String getUIControlStateLabel() {
		return StateLabel;
	}
}
