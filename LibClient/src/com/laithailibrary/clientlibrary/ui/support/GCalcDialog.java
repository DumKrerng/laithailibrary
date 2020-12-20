package com.laithailibrary.clientlibrary.ui.support;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.laithailibrary.clientlibrary.ui.swing.*;
import exc.*;

/**
 * Created by dumkrerng on 1/6/2558.
 */
public class GCalcDialog extends GDialog {

	private GCalcDialog me;

	private static final Font BIGGER_FONT = new Font("monspaced", Font.PLAIN, 20);
	private static int SCALE = 6;

	private JTextField m_txtResult;

	private boolean m_bolCalculated = false;
	private boolean m_bolClickOK = false;

	public GCalcDialog() throws GException {
		init();
	}

	public GCalcDialog(int p_intScale) throws GException {
		SCALE = p_intScale;

		init();
	}

	private void init() throws GException {
		try {
			me = this;

			buildDialogLayout();
			registerActionListener();

			me.setTitle("Calc");
			me.setResizable(false);
			me.setLocationRelativeTo(null);

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	private void buildDialogLayout() throws GException {
		try {
		  Container pane = getContentPane();

			pane.setLayout(new BorderLayout());
			pane.add(getMainPane(), BorderLayout.CENTER);

			me.pack();

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	private JPanel getMainPane() throws GException {
		try {
		  JPanel pane = new JPanel(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets = new Insets(5, 5, 5, 5);
			gbc.fill = GridBagConstraints.HORIZONTAL;

			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.gridheight = 1;
			gbc.gridwidth = 4;
			m_txtResult = new JTextField("", 10);
			m_txtResult.setFont(BIGGER_FONT);
			m_txtResult.setHorizontalAlignment(JTextField.RIGHT);
			pane.add(m_txtResult, gbc);

			gbc.gridx = 0;
			gbc.gridy = 1;
			gbc.gridheight = 1;
			gbc.gridwidth = 3;
			pane.add(getNumberPane(), gbc);

			gbc.gridx = 3;
			gbc.gridy = 1;
			gbc.gridheight = 1;
			gbc.gridwidth = 1;
			gbc.anchor = GridBagConstraints.NORTHWEST;
			pane.add(getOperatorPane(), gbc);

			gbc.gridx = 0;
			gbc.gridy = 2;
			gbc.gridheight = 1;
			gbc.gridwidth = 4;
			JButton b = new JButton("=");
			b.addActionListener(new OperatorListener());
			b.setFont(BIGGER_FONT);
			pane.add(b, gbc);

			gbc.gridx = 0;
			gbc.gridy = 3;
			gbc.gridheight = 1;
			gbc.gridwidth = 2;
			JButton btnClear = new JButton("Clear");
			btnClear.setFont(BIGGER_FONT);
			btnClear.addActionListener(new ClearListener());
			pane.add(btnClear, gbc);

			gbc.gridx = 2;
			gbc.gridy = 3;
			gbc.gridheight = 1;
			gbc.gridwidth = 2;
			JButton btnOK = new JButton("OK");
			btnOK.setFont(BIGGER_FONT);
			btnOK.addActionListener(new OKListener());
			pane.add(btnOK, gbc);

			return pane;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	private JPanel getNumberPane() throws GException {
		try {
			ActionListener numListener = new NumListener();

			String strCaption_Number = "789456123 0.";
			JPanel paneNumber = new JPanel();
			paneNumber.setLayout(new GridLayout(4, 3, 2, 2));

			for (int i = 0; i < strCaption_Number.length(); i++) {
				String strNumber = strCaption_Number.substring(i, i+1);
				JButton b = new JButton(strNumber);

				if(strNumber.equals(" ")) {
					b.setEnabled(false);

				} else {
					b.addActionListener(numListener);
					b.setFont(BIGGER_FONT);
				}

				paneNumber.add(b);
			}

			return paneNumber;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	private JPanel getOperatorPane() throws GException {
		try {
			ActionListener opListener = new OperatorListener();

			JPanel pane = new JPanel();
			pane.setLayout(new GridLayout(4, 1, 2, 2));

			String[] opOrder = {"+", "-", "*", "/"};

			for (int i = 0; i < opOrder.length; i++) {
				JButton b = new JButton(opOrder[i]);
				b.addActionListener(opListener);
				b.setFont(BIGGER_FONT);
				pane.add(b);
			}

			return pane;

		} catch(Exception exception) {
		  ExceptionHandler.display(exception);
		  throw new GException(exception);
		}
	}

	public void showDialog() {
		super.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		super.setModal(true);

		addWindowListener( new WindowAdapter() {
			public void windowOpened(WindowEvent p_event) {
				m_txtResult.requestFocus();
			}
		});

		setLocationRelativeTo(null);
		setVisible(true);
	}

	public boolean isClickOK() {
		return m_bolClickOK;
	}

	public String getResult() {
		if(!m_bolCalculated) {
			m_txtResult.setText("");
		}

		return m_txtResult.getText();
	}

	private void displayText(String p_strCommand, boolean p_bolIsOperator) {
		m_bolCalculated = false;

		if(p_bolIsOperator) {
			if(p_strCommand.compareTo("=") == 0) {
				String strResult = GCalculator.calculate(m_txtResult.getText(), SCALE);

				m_txtResult.setText(strResult);
				m_bolCalculated = true;

			} else {
				m_txtResult.setText(m_txtResult.getText() + p_strCommand);
			}
		} else {
			m_txtResult.setText(m_txtResult.getText() + p_strCommand);
		}

		m_txtResult.requestFocus();
	}

	private void registerActionListener() {
		m_txtResult.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent p_event) {
				char charKey = p_event.getKeyChar();

				if(charKey == '\n') {
					displayText("=", true);

				} else if(!GCalculator.OPERATOR.contains(String.valueOf(charKey))
					&& !(charKey >= '0' && charKey <= '9')
					&& (charKey != '.')
					&& (charKey != ' ')
					&& (charKey != GCalculator.BRACKET_BEGIN)
					&& (charKey != GCalculator.BRACKET_END)) {

					p_event.consume();
				}
			}

			public void keyPressed(KeyEvent p_event) {}
			public void keyReleased(KeyEvent p_event) {}
		});
	}

	private class OperatorListener implements ActionListener {

		public void actionPerformed(ActionEvent p_event) {
			String strCommand = p_event.getActionCommand();

			displayText(strCommand, true);
		}
	}

	private class NumListener implements ActionListener {

		public void actionPerformed(ActionEvent p_event) {
			String strNumber = p_event.getActionCommand();

			displayText(strNumber, false);
		}
	}

	class OKListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			try {
				Double.parseDouble(m_txtResult.getText());

			} catch(NumberFormatException p_exception) {
				m_txtResult.setText("");
			}

			m_bolClickOK = true;
			me.setVisible(false);
		}
	}

	class ClearListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			m_txtResult.setText("");
			m_txtResult.requestFocus();
		}
	}
}
