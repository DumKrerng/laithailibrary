package com.laithailibrary.clientlibrary.ui;

import com.laithailibrary.clientlibrary.ui.swing.*;
import exc.GException;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: dumkrerng
 * Date: 2/12/12
 * Time: 2:26 AM
 * To change this template use File | Settings | File Templates.
 */
public class EvenSelectionMasterData extends AWTEvent {

	public EvenSelectionMasterData(Object p_objectSource, int p_intID) throws GException {
		super(p_objectSource, p_intID);
	}

	public GTextSelector getTextSelectorField() {
		return (GTextSelector)getSource();
	}
}
