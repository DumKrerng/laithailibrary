package com.laithailibrary.clientlibrary.ui.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.laithailibrary.clientlibrary.ui.support.*;
import com.laithailibrary.clientlibrary.ui.swing.support.*;
import com.laithailibrary.sharelibrary.db.dbobject.*;
import com.laithailibrary.sharelibrary.field.*;
import com.laithailibrary.sharelibrary.locale.*;
import com.laithailibrary.sharelibrary.support.*;
import com.sun.java.swing.plaf.motif.*;
import exc.*;

/**
 * Created by IntelliJ IDEA.
 * User: dumkrerng
 * Date: 5/6/12
 * Time: 1:19 AM
 * To change this template use File | Settings | File Templates.
 */
public class GTextField extends JTextField {

	private GTextField me;
	private DataField m_datafield = new DataField();

	private Mandatory m_mandatory = null;

	private String m_strFontName = GFontUtilities.FONT_NAME_NORMAL;
	private int m_intFontSize = GFontUtilities.FONT_SIZE_NORMAL;
	private GFontStyle m_fontstyle = GFontStyle.Bold;

	private boolean m_bolDoTransferFocus = true;

	public GTextField() {
		init00("");
	}

	public GTextField(DataField p_datafield) {
		m_datafield = p_datafield;

		init00(p_datafield.getFieldName());
	}

	public GTextField(DataField p_datafield, int p_int) {
		super(p_int);

		m_datafield = p_datafield;

		init00(p_datafield.getFieldName());
	}

	public GTextField(int p_int) {
		super(p_int);

		init00("");
	}

	public GTextField(int p_int, Mandatory p_mandatory) {
		super(p_int);

		m_mandatory = p_mandatory;

		init00("");
	}

	public GTextField(String p_strText) {
		super(p_strText);

		init00("");
	}

	public GTextField(String p_strObjectName, String p_strText) {
		super(p_strText);

		init00(p_strObjectName);
	}

	public GTextField(String p_strText, Mandatory p_mandatory) {
		super(p_strText);

		m_mandatory = p_mandatory;

		init00("");
	}

	public GTextField(String p_strObjectName, String p_strText, Mandatory p_mandatory) {
		super(p_strText);

		m_mandatory = p_mandatory;

		init00(p_strObjectName);
	}

	public GTextField(DataField p_datafield, String p_strText, int p_int) {
		super(p_strText, p_int);

		m_datafield = p_datafield;

		init00(p_datafield.getFieldName());
	}

	public GTextField(String p_strText, int p_int) {
		super(p_strText, p_int);

		init00("");
	}

	public GTextField(String p_strObjectName, String p_strText, int p_int) {
		super(p_strText, p_int);

		init00(p_strObjectName);
	}

	public GTextField(String p_strText, int p_int, Mandatory p_mandatory) {
		super(p_strText, p_int);

		m_mandatory = p_mandatory;

		init00("");
	}

	public GTextField(String p_strObjectName, String p_strText, int p_int, Mandatory p_mandatory) {
		super(p_strText, p_int);

		m_mandatory = p_mandatory;

		init00(p_strObjectName);
	}

	public GTextField(DBObject p_dbObject) {
		super(p_dbObject.getString());

		init00("");
	}

	public GTextField(DBObject p_dbObject, int p_int) {
		super(p_dbObject.getString(), p_int);

		init00("");
	}

	private void init00(String p_strObjectName) {
		try {
			me = this;

			if(p_strObjectName.length() > 0) {
				setName(p_strObjectName);
			}

			setFont();
			setDataFieldMandatory();

			setCaret(MotifTextUI.createCaret());

			registerActionListener();

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
		}
	}

	public void setFontSize(int p_intFontSize) {
		m_intFontSize = p_intFontSize;

		setFont();
	}

	public void setFontStyle(GFontStyle p_fontstyle) {
		m_fontstyle = p_fontstyle;

		setFont();
	}

	private void setFont() {
		if(m_fontstyle == GFontStyle.Normal) {
			setFont(new Font(m_strFontName, Font.PLAIN, m_intFontSize));

		} else if(m_fontstyle == GFontStyle.Bold) {
			setFont(new Font(m_strFontName, Font.BOLD, m_intFontSize));

		} else if(m_fontstyle == GFontStyle.Italic) {
			setFont(new Font(m_strFontName, Font.ITALIC, m_intFontSize));

		} else if(m_fontstyle == GFontStyle.Bold_Italic) {
			setFont(new Font(m_strFontName, Font.BOLD | Font.ITALIC, m_intFontSize));

		} else {
			setFont(new Font(m_strFontName, Font.BOLD, m_intFontSize));
		}
	}

	private void setDataFieldMandatory() throws GException {
		try {
		  boolean bolMandatory = m_datafield.isMandatory();

			if(bolMandatory
				|| (m_mandatory != null
					&& m_mandatory == Mandatory.Yes)) {

				setBackground(new Color(255, 107, 112));
			}
		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public void setText(DBObject p_dbObject) {
		if(p_dbObject.isValid()) {
			super.setText(p_dbObject.getString());

		} else {
			super.setText("");
		}
	}

	public String getText() {
		String strText = super.getText();

		return strText.trim();
	}

	public DBString getDBString() {
		try {
		  String strText = getText();

			return new DBString(strText.trim());

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		}

		return new DBString();
	}

	public DBInteger getDBInteger() {
		try {
		  String strText = getText();

			return new DBInteger(strText.trim());

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		}

		return new DBInteger();
	}

	public void addEventKeyEnterListener(final ActionEventKeyEnterListener p_eventKeyEnterListener) {
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent p_event) {
				try {
					if(p_event.getKeyCode() == KeyEvent.VK_ENTER) {
						m_bolDoTransferFocus = false;

						p_eventKeyEnterListener.doAction(new EventKeyEnterListener(p_event.getSource(), p_event.getID()));
					}
				} catch(Exception exception) {
					ExceptionHandler.display(exception);
				}
			}
		});
	}

	protected void setDataToDefault() throws GException {
		try {
			DefaultDataInTextFieldManager.setDataToDefault(me);

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	private void registerActionListener() throws GException {
		me.addFocusListener(new FocusAdapter() {
			public void focusGained(final FocusEvent p_event) {
				me.selectAll();
			}
		});

		me.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				if(m_bolDoTransferFocus) {
					transferFocus();
				}
			}
		});

		me.addMouseListener(new MouseListener() {
			public void mouseClicked(final MouseEvent event) {
				try {
					if(event.getButton() == MouseEvent.BUTTON3) {
						JPopupMenu popupMenu = new JPopupMenu();

						String strCaption_SetToDefault = "Set to Default";

						if(AppClientUtilities.getLocale() == GLocale.Thai) {
							strCaption_SetToDefault = "เป็นค่าเริ่มต้น";
						}

						JMenuItem mi = new JMenuItem(strCaption_SetToDefault);
						mi.addActionListener(new ActionListener() {
							public void actionPerformed(final ActionEvent e) {
								try {
									setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

									setDataToDefault();

								} catch (Exception exception) {
									ExceptionHandler.display(exception);

								} finally {
									setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
								}
							}
						});

						popupMenu.add(mi);

						popupMenu.show(event.getComponent(), event.getX(), event.getY());
					}
				} catch (Exception exception) {
					ExceptionHandler.display(exception);
				}
			}

			public void mousePressed(final MouseEvent event) {}
			public void mouseReleased(final MouseEvent event) {}
			public void mouseEntered(final MouseEvent event) {}
			public void mouseExited(final MouseEvent event) {}
		});
	}
}
