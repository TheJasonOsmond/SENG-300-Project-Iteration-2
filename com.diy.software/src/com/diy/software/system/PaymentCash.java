package com.diy.software.system;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Currency;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.CardLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

public class PaymentCash {
	JFrame payFrame;
	JPanel payPanel;
	JTextArea cashDisplay;
	JLabel instructLabel, confirmLabel;
	JButton confirm, insertCoin, insertNote;
	DIYSystem station;
	
	double cashInserted;
	
	private boolean payWasSuccessful = false;
	private JButton btnCloseWindow;

	public PaymentCash(DIYSystem sys) {
		//this is just copy paste from Payment.java for now...
		station = sys;
		Currency curr = Currency.getInstance(Locale.CANADA);
		
		payFrame = new JFrame("***** Pay by Cash *****");
		payFrame.setResizable(true);
		payFrame.setUndecorated(false);
		payFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		payFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				station.reEnableScanner();
			}
		});
		payFrame.getContentPane().setLayout(new BoxLayout(payFrame.getContentPane(), BoxLayout.X_AXIS));
		payPanel = new JPanel();
		payPanel.setLayout(new GridLayout(0, 1));
		payPanel.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));
		payFrame.getContentPane().add(payPanel);
		confirmLabel = new JLabel("");
		confirmLabel.setHorizontalAlignment(SwingConstants.CENTER);

		instructLabel = new JLabel("Choose coins to insert");
		instructLabel.setHorizontalAlignment(JLabel.CENTER);
		payPanel.add(instructLabel);
		//this displays the amount of cash inserted
		cashDisplay = new JTextArea("Cash Inserted: $0.0");
		payPanel.add(cashDisplay);
		//press to insert a coin; this could be done better........
		insertCoin = new JButton("Insert ¢1 Coin");
		insertCoin.addActionListener(e -> {
			station.InsertCoin(curr);
			cashInserted += 0.1;
			cashDisplay.replaceRange(String.valueOf(cashInserted), 16, 18);
		});
		payPanel.add(insertCoin);
		
		insertNote = new JButton("Insert $1 Banknote");
		insertNote.addActionListener(e -> {
			station.InsertBanknote(curr);
			cashInserted += 1.0;
			cashDisplay.replaceRange(String.valueOf(cashInserted), 16, 18);
		});
		
		// pinLabel = new JLabel("PIN", SwingConstants.LEFT);
		confirm = new JButton("Confirm Payment Details");

		// When the Confirm button is pressed, tell the system to start the payment
		// process
		confirm.addActionListener(e -> {
			station.payByCash(cashInserted);
		});

		payPanel.add(confirm);

		btnCloseWindow = new JButton("Exit");
		btnCloseWindow.addActionListener(e -> {
			closeWindow();
		});
		
		payPanel.add(btnCloseWindow);
		payPanel.add(confirmLabel);

		payFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		payFrame.setVisible(true);
		payFrame.pack();
		payFrame.setSize(400, 200);
	}

	/**
	 * Triggered from the system to update the message that the customer can see
	 * 
	 * @param msg
	 * @throws InterruptedException
	 */
	public void setMessage(String msg) {
		confirmLabel.setText(msg);
	}

	public String getMessage() {
		return this.confirmLabel.getText();
	}
	
	public void disablePaying() {
		this.confirm.setEnabled(false);
	}
	
	public void updatePayStatus(boolean status) {
		this.payWasSuccessful = status;
	}

	private void closeWindow() {
		station.enableScanningAndBagging();
		
		if(!payWasSuccessful)
			payFrame.dispose();
		else
			System.exit(0);
	}
}
