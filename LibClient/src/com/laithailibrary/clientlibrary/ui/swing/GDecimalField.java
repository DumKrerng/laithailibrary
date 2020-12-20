package com.laithailibrary.clientlibrary.ui.swing;

import javax.swing.*;
import java.awt.event.*;
import com.laithailibrary.clientlibrary.ui.support.*;
import com.laithailibrary.sharelibrary.db.dbobject.*;
import com.laithailibrary.sharelibrary.field.*;
import exc.*;

/**
 * Created by dumkrerng on 26/5/2558.
 */
public class GDecimalField extends GTextField {

	private GDecimalField me;

	public GDecimalField() {
		init();
	}

	public GDecimalField(int p_int) {
		super(p_int);

		init();
	}

	public GDecimalField(int p_int, Mandatory p_mandatory) {
		super(p_int, p_mandatory);

		init();
	}

	public GDecimalField(String p_strText) {
		super(p_strText);

		setText(p_strText);

		init();
	}

	public GDecimalField(String p_strObjectName, String p_strText) {
		super(p_strObjectName, p_strText);

		setText(p_strText);

		init();
	}

	public GDecimalField(String p_strText, Mandatory p_mandatory) {
		super(p_strText, p_mandatory);

		setText(p_strText);

		init();
	}

	public GDecimalField(String p_strObjectName, String p_strText, Mandatory p_mandatory) {
		super(p_strObjectName, p_strText, p_mandatory);

		setText(p_strText);

		init();
	}

	public GDecimalField(String p_strText, int p_int) {
		super(p_int);

		setText(p_strText);

		init();
	}

	public GDecimalField(String p_strObjectName, String p_strText, int p_int) {
		super(p_strObjectName, p_strText, p_int);

		setText(p_strText);

		init();
	}

	public GDecimalField(String p_strText, int p_int, Mandatory p_mandatory) {
		super(p_int, p_mandatory);

		setText(p_strText);

		init();
	}

	public GDecimalField(String p_strObjectName, String p_strText, int p_int, Mandatory p_mandatory) {
		super(p_strObjectName, p_strText, p_int, p_mandatory);

		setText(p_strText);

		init();
	}

	public GDecimalField(DataField p_datafield) {
		super(p_datafield);

		init();
	}

	public GDecimalField(DataField p_datafield, int p_int) {
		super(p_datafield, p_int);

		init();
	}

	public void setText(DBDecimal p_dbDecimal) {
		super.setText(p_dbDecimal.getString());
	}

	private void setTextOnMouseClick() {
		super.setText("");
	}

	public DBDecimal getDBDecimal() {
		return new DBDecimal(getText());
	}

	private void init() {
		try {
			me = this;

			setHorizontalAlignment(JTextField.RIGHT);

			registerActionListener();

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
		}
	}

	private void registerActionListener() {
		me.addMouseListener(new MouseListener() {
			public void mouseClicked(final MouseEvent p_event) {
				try {
					int intClickCount = p_event.getClickCount();

					if(intClickCount == 1) {
						String strText = getText().trim();

						if(strText.compareTo("INVALID") == 0) {
							setTextOnMouseClick();
						}
					} else {
						GCalcDialog calcdialog = new GCalcDialog();
						calcdialog.showDialog();

						if(calcdialog.isClickOK()) {
							String strResult = calcdialog.getResult();

							me.setText(strResult);
						}

						calcdialog.dispose();
					}
				} catch(Exception exception) {
					ExceptionHandler.display(exception);
				}
			}

			public void mousePressed(final MouseEvent p_event) {}
			public void mouseReleased(final MouseEvent p_event) {}
			public void mouseEntered(final MouseEvent p_event) {}
			public void mouseExited(final MouseEvent p_event) {}
		});

		me.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent p_event) {}

			public void keyPressed(KeyEvent p_event) {
				try {
					char charKey = p_event.getKeyChar();

					if(charKey == '\n') {
						String strStatement = me.getText();

						String strResult = GCalculator.calculate(strStatement);

						Double.parseDouble(strResult);

						me.setText(strResult);
					}
				} catch(Exception exception) {
					ExceptionHandler.display(exception);
				}
			}

			public void keyReleased(KeyEvent p_event) {}
		});
	}
}
