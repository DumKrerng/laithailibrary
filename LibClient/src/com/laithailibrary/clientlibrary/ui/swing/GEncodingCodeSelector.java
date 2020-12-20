package com.laithailibrary.clientlibrary.ui.swing;

import com.laithailibrary.clientlibrary.ui.support.*;
import com.laithailibrary.sharelibrary.collection.*;
import com.laithailibrary.sharelibrary.file.support.*;
import exc.*;
import java.util.*;

public class GEncodingCodeSelector extends GComboBox<GEncodingCode> {

	public GEncodingCodeSelector() throws GException {
		try {
			BEANComboBox<GEncodingCode> beanUTF_8 = new BEANComboBox<>(GEncodingCode.UTF_8.getLabel(), GEncodingCode.UTF_8);
			BEANComboBox<GEncodingCode> beanUTF_16 = new BEANComboBox<>(GEncodingCode.UTF_16.getLabel(), GEncodingCode.UTF_16);
			BEANComboBox<GEncodingCode> beanTIS_620 = new BEANComboBox<>(GEncodingCode.TIS_620.getLabel(), GEncodingCode.TIS_620);
			BEANComboBox<GEncodingCode> beanWindows_874 = new BEANComboBox<>(GEncodingCode.Windows_874.getLabel(), GEncodingCode.Windows_874);
			BEANComboBox<GEncodingCode> beanISO_8859_1 = new BEANComboBox<>(GEncodingCode.ISO_8859_1.getLabel(), GEncodingCode.ISO_8859_1);
			BEANComboBox<GEncodingCode> beanISO_8859_11 = new BEANComboBox<>(GEncodingCode.ISO_8859_11.getLabel(), GEncodingCode.ISO_8859_11);
			BEANComboBox<GEncodingCode> beanIBM_874 = new BEANComboBox<>(GEncodingCode.IBM_874.getLabel(), GEncodingCode.IBM_874);
			BEANComboBox<GEncodingCode> beanUS_ASCII = new BEANComboBox<>(GEncodingCode.US_ASCII.getLabel(), GEncodingCode.US_ASCII);

			List<BEANComboBox<GEncodingCode>> lsBEANComboBox = new GList<>();
			lsBEANComboBox.add(beanTIS_620);
			lsBEANComboBox.add(beanISO_8859_1);
			lsBEANComboBox.add(beanISO_8859_11);
			lsBEANComboBox.add(beanIBM_874);
			lsBEANComboBox.add(beanUS_ASCII);
			lsBEANComboBox.add(beanWindows_874);
			lsBEANComboBox.add(beanUTF_8);
			lsBEANComboBox.add(beanUTF_16);

			setLSBEANComboBoxs(lsBEANComboBox);

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}
}
