package com.laithailibrary.clientlibrary.ui.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.*;
import java.util.stream.*;
import com.laithailibrary.clientlibrary.ui.image.*;
import com.laithailibrary.sharelibrary.collection.*;
import com.laithailibrary.sharelibrary.db.dbobject.*;
import exc.*;

/**
 * Created by dumkrerng on 14/1/2561.
 */
public class GMultipleListSelector<T extends DBObject> extends GPanel {

	private GMultipleListSelector me;

	private GTextField m_txtSearch;
	private JList<String> m_listDisplayValue;
	private JList<String> m_listDisplayValue_Selected;

	private GButton m_btnSelectAll;
	private GButton m_btnSelect;
	private GButton m_btnUnselected;
	private GButton m_btnUnselectedAll;

	private WithSelectAll m_withselectall = WithSelectAll.Yes;

	private Map<String, T> m_mapKey_Display;
	private Map<T, String> m_mapDisplay_Key;

	private Set<String> m_setValues = new GSet<>();
	private Set<String> m_setSearchedValues = null;
	private Set<String> m_setSelectedValues = new GSet<>();

	private int m_intDialogListSize_Width = 200;
	private int m_intDialogListSize_Height = 200;

	public GMultipleListSelector() throws GException {
		super(new GridBagLayout());

		init();
	}

	public GMultipleListSelector(int p_intWidth, int p_intHeight) throws GException {
		super(new GridBagLayout());

		m_intDialogListSize_Width = p_intWidth;
		m_intDialogListSize_Height = p_intHeight;

		init();
	}

	public GMultipleListSelector(WithSelectAll p_withselectall) throws GException {
		super(new GridBagLayout());

		m_withselectall = p_withselectall;

		init();
	}

	public GMultipleListSelector(WithSelectAll p_withselectall, int p_intWidth, int p_intHeight) throws GException {
		super(new GridBagLayout());

		m_withselectall = p_withselectall;
		m_intDialogListSize_Width = p_intWidth;
		m_intDialogListSize_Height = p_intHeight;

		init();
	}

	public GMultipleListSelector(Map<String, T> p_mapKey_Display) throws GException {
		super(new GridBagLayout());

		m_mapKey_Display = p_mapKey_Display;

		init();
	}

	public GMultipleListSelector(Map<String, T> p_mapKey_Display, int p_intWidth, int p_intHeight) throws GException {
		super(new GridBagLayout());

		m_mapKey_Display = p_mapKey_Display;
		m_intDialogListSize_Width = p_intWidth;
		m_intDialogListSize_Height = p_intHeight;

		init();
	}

	public GMultipleListSelector(Map<String, T> p_mapKey_Display, WithSelectAll p_withselectall) throws GException {
		super(new GridBagLayout());

		m_mapKey_Display = p_mapKey_Display;
		m_withselectall = p_withselectall;

		init();
	}

	public GMultipleListSelector(Map<String, T> p_mapKey_Display, int p_intWidth, int p_intHeight, WithSelectAll p_withselectall) throws GException {
		super(new GridBagLayout());

		m_mapKey_Display = p_mapKey_Display;
		m_intDialogListSize_Width = p_intWidth;
		m_intDialogListSize_Height = p_intHeight;
		m_withselectall = p_withselectall;

		init();
	}

	private void init() throws GException {
		try {
			me = this;

			buildLayout();
			registerEventListener();

			populateListDisplayValue();

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	private void buildLayout() throws GException {
		try {
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets = new Insets(5, 5, 5, 5);

			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.gridwidth = 1;
			gbc.gridheight = 1;
			gbc.anchor = GridBagConstraints.SOUTH;
			m_txtSearch = new GTextField(15);
			me.add(m_txtSearch, gbc);

			gbc.gridx = 1;
			gbc.gridy = 0;
			gbc.gridwidth = 1;
			gbc.gridheight = 2;
			gbc.anchor = GridBagConstraints.CENTER;
			me.add(getButtonPane(), gbc);

			gbc.gridx = 2;
			gbc.gridy = 0;
			gbc.gridwidth = 1;
			gbc.gridheight = 1;
			gbc.anchor = GridBagConstraints.SOUTH;
			GLabel label = new GLabel("Select");
			me.add(label, gbc);

			gbc.gridx = 0;
			gbc.gridy = 1;
			gbc.gridwidth = 1;
			gbc.gridheight = 1;
			gbc.anchor = GridBagConstraints.CENTER;
			m_listDisplayValue = new JList<>();
			JScrollPane scroller = new JScrollPane(m_listDisplayValue);
			scroller.setPreferredSize(new Dimension(m_intDialogListSize_Width, m_intDialogListSize_Height));
			me.add(scroller, gbc);

			gbc.gridx = 2;
			gbc.gridy = 1;
			gbc.gridwidth = 1;
			gbc.gridheight = 1;
			gbc.anchor = GridBagConstraints.CENTER;
			m_listDisplayValue_Selected = new JList<>();
			JScrollPane scroller_Selected = new JScrollPane(m_listDisplayValue_Selected);
			scroller_Selected.setPreferredSize(new Dimension(m_intDialogListSize_Width, m_intDialogListSize_Height));
			me.add(scroller_Selected, gbc);

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	private GPanel getButtonPane() throws GException {
		try {
			GPanel pane = new GPanel(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets = new Insets(5, 5, 5, 5);

			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.anchor = GridBagConstraints.WEST;
			m_btnSelectAll = new GButton("", GImageIcon.getIconSelectAll());
			m_btnSelectAll.setPreferredSize(new Dimension(60, 30));

			if(m_withselectall == WithSelectAll.Yes) {
				pane.add(m_btnSelectAll, gbc);
			}

			gbc.gridx = 0;
			gbc.gridy = 1;
			gbc.anchor = GridBagConstraints.WEST;
			m_btnSelect = new GButton("", GImageIcon.getIconSelect());
			m_btnSelect.setPreferredSize(new Dimension(60, 40));
			pane.add(m_btnSelect, gbc);

			gbc.gridx = 0;
			gbc.gridy = 2;
			gbc.anchor = GridBagConstraints.WEST;
			m_btnUnselected = new GButton("", GImageIcon.getIconUnselect());
			m_btnUnselected.setPreferredSize(new Dimension(60, 40));
			pane.add(m_btnUnselected, gbc);

			gbc.gridx = 0;
			gbc.gridy = 3;
			gbc.anchor = GridBagConstraints.WEST;
			m_btnUnselectedAll = new GButton("", GImageIcon.getIconUnselectAll());
			m_btnUnselectedAll.setPreferredSize(new Dimension(60, 30));

			if(m_withselectall == WithSelectAll.Yes) {
				pane.add(m_btnUnselectedAll, gbc);
			}

			return pane;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	private void doSearch() throws GException {
		try {
		  String strSearch = m_txtSearch.getText();
			m_setSearchedValues = null;

			if(strSearch.length() > 0) {
				Stream<String> stmValues_Searched = m_setValues.stream().filter(it -> it.contains(strSearch));
				m_setSearchedValues = new GSet<>(stmValues_Searched.collect(Collectors.toSet()));

				m_listDisplayValue.setListData(m_setSearchedValues.toArray(new String[]{""}));

			} else {
				m_listDisplayValue.setListData(m_setValues.toArray(new String[]{""}));
			}

			if(m_listDisplayValue.getModel().getSize() > 0) {
				m_listDisplayValue.setSelectedIndex(0);

				m_listDisplayValue.requestFocus();
			}
		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public void setListDisplayValue(Map<String, T> p_mapKey_Display) throws GException {
		m_mapKey_Display = p_mapKey_Display;

		populateListDisplayValue();
	}

	public void setEnabled(boolean p_bolEnabled) {
		super.setEnabled(p_bolEnabled);

		m_txtSearch.setEnabled(p_bolEnabled);
		m_listDisplayValue.setEnabled(p_bolEnabled);
		m_listDisplayValue_Selected.setEnabled(p_bolEnabled);

		m_btnSelectAll.setEnabled(p_bolEnabled);
		m_btnSelect.setEnabled(p_bolEnabled);
		m_btnUnselected.setEnabled(p_bolEnabled);
		m_btnUnselectedAll.setEnabled(p_bolEnabled);
	}

	public List<T> getSelectedIDs() throws GException {
		try {
			List<T> lsSelectedIDs = new GList<>();

			if(m_setSelectedValues.size() > 0) {
				lsSelectedIDs = getActualSelectedIDs();

			} else {
				for(String strSelectedValue : m_setValues) {
					if(m_mapKey_Display.containsKey(strSelectedValue)) {
						T id = m_mapKey_Display.get(strSelectedValue);
						lsSelectedIDs.add(id);

					} else {
						throw new GException("Not found ID for " + strSelectedValue + "!!!");
					}
				}
			}

			return lsSelectedIDs;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public List<T> getActualSelectedIDs() throws GException {
		try {
			List<T> lsSelectedIDs = new GList<>();

			if(m_setSelectedValues.size() > 0) {
				for(String strSelectedValue : m_setSelectedValues) {
					if(m_mapKey_Display.containsKey(strSelectedValue)) {
						T id = m_mapKey_Display.get(strSelectedValue);
						lsSelectedIDs.add(id);

					} else {
						throw new GException("Not found ID for " + strSelectedValue + "!!!");
					}
				}
			}

			return lsSelectedIDs;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public void clearAllData() throws GException {
		try {
			m_txtSearch.setText("");

			m_mapDisplay_Key = new GMap<>();
			m_mapKey_Display = new GMap<>();

			m_setValues = new GSet<>();
			m_setSearchedValues = null;
			m_setSelectedValues = new GSet<>();

			m_listDisplayValue.setListData(m_setValues.toArray(new String[]{""}));
			m_listDisplayValue_Selected.setListData(m_setSelectedValues.toArray(new String[]{""}));

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	private void populateListDisplayValue() throws GException {
		try {
			if(m_mapKey_Display == null) {
				m_mapKey_Display = new GMap<>();
			}

			m_mapDisplay_Key = new GMap<>();

			for(Map.Entry<String, T> entKey : m_mapKey_Display.entrySet()) {
				String strValue = entKey.getKey();
				T key = entKey.getValue();

				m_mapDisplay_Key.put(key, strValue);
				m_setValues.add(strValue);
			}

			m_listDisplayValue.setListData(m_setValues.toArray(new String[]{""}));

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	private void doSelectValue() throws GException {
		try {
			m_setSelectedValues.addAll(m_listDisplayValue.getSelectedValuesList());
			m_setValues.removeAll(m_listDisplayValue.getSelectedValuesList());

			if(m_setSearchedValues != null) {
				m_setSearchedValues.removeAll(m_setSelectedValues);
				m_listDisplayValue.setListData(m_setSearchedValues.toArray(new String[]{""}));

			} else {
				m_listDisplayValue.setListData(m_setValues.toArray(new String[]{""}));
			}

			m_listDisplayValue_Selected.setListData(m_setSelectedValues.toArray(new String[]{""}));

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	private void doUnselectedValue() throws GException {
		try {
			m_setValues.addAll(m_listDisplayValue_Selected.getSelectedValuesList());
			m_setSelectedValues.removeAll(m_listDisplayValue_Selected.getSelectedValuesList());

			m_listDisplayValue.setListData(m_setValues.toArray(new String[]{""}));
			m_listDisplayValue_Selected.setListData(m_setSelectedValues.toArray(new String[]{""}));

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	private void registerEventListener() throws GException {
		m_txtSearch.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent p_event) {}

			public void keyPressed(KeyEvent p_event) {
				try {
					int intKeyCode = p_event.getKeyCode();

					switch(intKeyCode) {
						case KeyEvent.VK_UP:
							break;

						case KeyEvent.VK_DOWN:
							break;

						case KeyEvent.VK_LEFT:
							break;

						case KeyEvent.VK_RIGHT :
							break;

						case KeyEvent.VK_ENTER :
							doSearch();

							break;
					}
				} catch(Exception exception) {
					ExceptionHandler.display(exception);
				}
			}

			public void keyReleased(KeyEvent p_event) {}
		});

		m_btnSelectAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent p_event) {
				try {
					if(m_setSearchedValues == null) {
						m_listDisplayValue.setSelectionInterval(0, m_setValues.size() - 1);

					} else {
						m_listDisplayValue.setSelectionInterval(0, m_setSearchedValues.size() - 1);
					}

					doSelectValue();

				} catch(Exception exception) {
				  ExceptionHandler.display(exception);
				}
			}
		});

		m_btnSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent p_event) {
				try {
					doSelectValue();

				} catch(Exception exception) {
				  ExceptionHandler.display(exception);
				}
			}
		});

		m_btnUnselectedAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent p_event) {
				try {
					m_listDisplayValue_Selected.setSelectionInterval(0, m_setSelectedValues.size() - 1);

					doUnselectedValue();

				} catch(Exception exception) {
				  ExceptionHandler.display(exception);
				}
			}
		});

		m_btnUnselected.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent p_event) {
				try {
					doUnselectedValue();

				} catch(Exception exception) {
				  ExceptionHandler.display(exception);
				}
			}
		});

		m_listDisplayValue.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent p_event) {}

			public void keyPressed(KeyEvent p_event) {
				try {
					int intKeyCode = p_event.getKeyCode();

					switch(intKeyCode) {
						case KeyEvent.VK_UP:
							break;

						case KeyEvent.VK_DOWN:
							break;

						case KeyEvent.VK_LEFT:
							break;

						case KeyEvent.VK_RIGHT :
							doSelectValue();

							break;

						case KeyEvent.VK_ENTER :
							doSelectValue();

							break;
					}
				} catch(Exception exception) {
					ExceptionHandler.display(exception);
				}
			}

			public void keyReleased(KeyEvent p_event) {}
		});

		m_listDisplayValue_Selected.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent p_event) {}

			public void keyPressed(KeyEvent p_event) {
				try {
					int intKeyCode = p_event.getKeyCode();

					switch(intKeyCode) {
						case KeyEvent.VK_UP:
							break;

						case KeyEvent.VK_DOWN:
							break;

						case KeyEvent.VK_LEFT:
							doUnselectedValue();

							break;

						case KeyEvent.VK_RIGHT :
							break;

						case KeyEvent.VK_ENTER :
							doUnselectedValue();

							break;
					}
				} catch(Exception exception) {
					ExceptionHandler.display(exception);
				}
			}

			public void keyReleased(KeyEvent p_event) {}
		});

		m_listDisplayValue.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent p_event) {
				try {
				  int intClickCount = p_event.getClickCount();

					if(intClickCount == 2) {
						doSelectValue();
					}
				} catch(Exception exception) {
				  ExceptionHandler.display(exception);
				}
			}
		});

		m_listDisplayValue_Selected.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent p_event) {
				try {
				  int intClickCount = p_event.getClickCount();

					if(intClickCount == 2) {
						doUnselectedValue();
					}
				} catch(Exception exception) {
				  ExceptionHandler.display(exception);
				}
			}
		});
	}

	public enum WithSelectAll {
		Yes,
		No
	}
}
