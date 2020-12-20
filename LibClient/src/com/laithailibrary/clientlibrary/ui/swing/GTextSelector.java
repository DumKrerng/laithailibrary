package com.laithailibrary.clientlibrary.ui.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.laithailibrary.clientlibrary.ui.*;
import com.laithailibrary.clientlibrary.ui.support.*;
import com.laithailibrary.clientlibrary.ui.swing.support.*;
import com.laithailibrary.clientlibrary.ui.ucv.*;
import com.laithailibrary.sharelibrary.datatableview.*;
import com.laithailibrary.sharelibrary.db.dbobject.*;
import com.laithailibrary.sharelibrary.field.*;
import com.laithailibrary.sharelibrary.locale.*;
import com.laithailibrary.sharelibrary.support.*;
import exc.*;

/**
 * Created by IntelliJ IDEA.
 * User: dumkrerng
 * Date: 6/16/11
 * Time: 7:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class GTextSelector<T extends DBObject> extends GTextField {

	private GTextSelector me;

	private Class<T> m_clsPrimaryKeyType;
	private T m_dboPrimaryKeyValue;
	private DBString m_dbstrDisplayValue;
	private DBString m_dbstrDisplayValue_Old = new DBString();

	private DataVectorRow m_dvrSelectedRow = null;

	public GTextSelector(Class<T> p_clsPrimaryKey, String p_strString, int p_intWidth) {
		super(p_strString, p_intWidth);

		init(p_clsPrimaryKey);
	}

	public GTextSelector(Class<T> p_clsPrimaryKey, String p_strObjectName, String p_strString, int p_intWidth) {
		super(p_strObjectName, p_strString, p_intWidth);

		init(p_clsPrimaryKey);
	}

	public GTextSelector(Class<T> p_clsPrimaryKey, int p_intWidth) {
		super("", p_intWidth);

		init(p_clsPrimaryKey);
	}

	public GTextSelector(Class<T> p_clsPrimaryKey, String p_strString, int p_intWidth, Mandatory p_mandatory) {
		super(p_strString, p_intWidth, p_mandatory);

		init(p_clsPrimaryKey);
	}

	public GTextSelector(Class<T> p_clsPrimaryKey, String p_strObjectName, String p_strString, int p_intWidth, Mandatory p_mandatory) {
		super(p_strObjectName, p_strString, p_intWidth, p_mandatory);

		init(p_clsPrimaryKey);
	}

	public GTextSelector(DataField<T> p_datafield, String p_strString, int p_intWidth) throws GException {
		super(p_datafield, p_strString, p_intWidth);

		Class<T> classdata = p_datafield.getClassData();

		init(classdata);
	}

	public GTextSelector(DataField<T> p_datafield, int p_intWidth) throws GException {
		super(p_datafield, p_intWidth);

		Class<T> classData = p_datafield.getClassData();

		init(classData);
	}

	private void init(Class<T> p_clsPrimaryKey) {
		try {
			me = this;

			m_clsPrimaryKeyType = p_clsPrimaryKey;
			m_dboPrimaryKeyValue = p_clsPrimaryKey.newInstance();
			m_dbstrDisplayValue = new DBString();

			JLabel label = new JLabel("Select");

			if(AppClientUtilities.getLocale() == GLocale.Thai) {
				label = new JLabel("เลือก");
			}

			setLayout(new GridLayout(2, 1));
			label.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 12));
			label.setForeground(Color.BLUE);
			label.setHorizontalAlignment(RIGHT);
			add(label);

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		}
	}

	public void setSelected(T p_dboPrimaryKeyValue, DBString p_dbstrDisplayValue) throws GException {
		setSelected(p_dboPrimaryKeyValue, p_dbstrDisplayValue.getString());
	}

	public void setSelected(T p_dboPrimaryKeyValue, String p_strDisplayValue) throws GException {
		m_dboPrimaryKeyValue = p_dboPrimaryKeyValue;

		setDisplayValue(p_strDisplayValue);
		setText(p_strDisplayValue);
	}

	public void setSelected(String p_strPrimaryKeyValue, String p_strDisplayValue) throws GException {
		try {
			m_dboPrimaryKeyValue = m_clsPrimaryKeyType.newInstance();
			m_dboPrimaryKeyValue.setStringValue(p_strPrimaryKeyValue);

			setDisplayValue(p_strDisplayValue);
			setText(p_strDisplayValue);

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	private void setDisplayValue(String p_strDisplayValue) throws GException {
		m_dbstrDisplayValue = new DBString(p_strDisplayValue);
		m_dbstrDisplayValue_Old = new DBString(p_strDisplayValue);
	}

	public T getSelectedID() throws GException {
		try {
			String strDisplayText = getText();

			if(m_dbstrDisplayValue_Old.compareTo(new DBString(strDisplayText)) == 0) {
				return m_dboPrimaryKeyValue;
			}

			return m_clsPrimaryKeyType.newInstance();

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public DataVectorRow getSelectedRow() throws GException {
		return m_dvrSelectedRow;
	}

	public DBString getTextDisplay() {
		return m_dbstrDisplayValue;
	}

	public void resetData() {
		try {
			setSelected(m_clsPrimaryKeyType.newInstance(), "");

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		}
	}

	protected void setDataToDefault() throws GException {
		try {
			DefaultDataInTextFieldManager.setDataToDefault(me);

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public void addActionEvenSelectionMasterData(final ActionEventSelectionMasterData p_event) {
		addMouseListener(new MouseListener() {
			public void mouseClicked(final MouseEvent event) {
				try {
					if(event.getClickCount() == 2) {
						setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

						p_event.onSelectorMasterData(new EvenSelectionMasterData(event.getSource(), event.getID()));

					} else if(event.getButton() == MouseEvent.BUTTON3) {
						JPopupMenu popupMenu = new JPopupMenu();

						String strCaption_Select = "Select";
						String strCaption_GoTo = "Go To";
						String strCaption_SetToDefault = "Set to Default";

						if(AppClientUtilities.getLocale() == GLocale.Thai) {
							strCaption_Select = "เลือก";
							strCaption_GoTo = "ไปที่หน้าข้อมูล";
							strCaption_SetToDefault = "เป็นค่าเริ่มต้น";
						}

						JMenuItem mi = new JMenuItem(strCaption_Select);
						mi.addActionListener(new ActionListener() {
							public void actionPerformed(final ActionEvent e) {
								try {
									setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

									p_event.onSelectorMasterData(new EvenSelectionMasterData(event.getSource(), event.getID()));

								} catch (Exception exception) {
								  ExceptionHandler.display(exception);
								}
							}
						});

						popupMenu.add(mi);

						mi = new JMenuItem(strCaption_GoTo);
						mi.addActionListener(new ActionListener() {
							public void actionPerformed(final ActionEvent e) {
								try {
									p_event.onGoToMasterData(new EvenSelectionMasterData(event.getSource(), event.getID()));

									resetData();

								} catch (Exception exception) {
								  ExceptionHandler.display(exception);
								}
							}
						});

						popupMenu.add(mi);

						popupMenu.addSeparator();

						mi = new JMenuItem(strCaption_SetToDefault);
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

	public void displaySelector(Dialog p_dialog, DataVectorTable p_datavectortable, String p_strTableHeader) throws GException {
		displaySelector(p_dialog, p_datavectortable, p_strTableHeader, UIDimension.DIM_900_650);
	}

	public void displaySelector(Dialog p_dialog, DataVectorTable p_datavectortable, String p_strTableHeader, Dimension p_dimension) throws GException {
		try {
			String strFilter = "";

			T dboSelectedID = getSelectedID();

			if(dboSelectedID.isInvalid()) {
				strFilter = getText();
			}

			UISelectorMasterData<T> uiSelectorMasterData = new UISelectorMasterData<>(p_dialog, p_datavectortable, p_strTableHeader,
				strFilter, p_dimension);

			if(uiSelectorMasterData.isOnClickSelected()) {
				T dboPrimaryKey = uiSelectorMasterData.getSelectedPrimaryKey();
				DBString dbstrDisplayValue = (DBString)uiSelectorMasterData.getSelectedDisplayValue();

				setSelected(dboPrimaryKey, dbstrDisplayValue);
				m_dvrSelectedRow = uiSelectorMasterData.getSelectedRow();
			}

			setCursor(Cursor.getDefaultCursor());

		} catch (Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}
}
