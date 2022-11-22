package com.diy.software.system;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
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
	JTextField pin;
	JLabel instructLabel, confirmLabel, amountLabel;
	JButton confirm;
	JButton btnAdd1,btnAdd5;
	DIYSystem station;
	
	private boolean payWasSuccessful = false;
	private JButton btnCloseWindow;
	private long amountInserted = 0;
	

	public PaymentCash(DIYSystem sys) {
		//this is just copy paste from Payment.java for now...
		station = sys;
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

		//create two buttons: one creates a coin and asks DoItYourselfStation's CoinSlot to receive the coin. the coin has denomination long.valueOf(1L)
		//the other one asks DoItYourselfStation's BanknoteSlot to receive a banknote. the banknote has denomination int[] { 1 }
		
		// pinLabel = new JLabel("PIN", SwingConstants.LEFT);
		
		// Add 1 Button
		btnAdd1 = new JButton("Add 1");		
		btnAdd1.addActionListener(e -> { // When Add 1 is pressed, tell the system to start the payment process
			System.out.println("Add 1 button pressed"); 
			amountInserted++;
			updateAmountLabel();
		});
		payPanel.add(btnAdd1);
		
		// Add 5 Button
		btnAdd5 = new JButton("Add 5");		
		btnAdd5.addActionListener(e -> { // When Add 1 is pressed, tell the system to start the payment process
			System.out.println("Add 5button pressed"); 
			amountInserted += 5;
			updateAmountLabel();
		});
		payPanel.add(btnAdd5);
		
		amountLabel = new JLabel("Amount Inserted = $0");
		amountLabel.setHorizontalAlignment(JLabel.CENTER);
		payPanel.add(amountLabel);
		
		//Confirm button
		confirm = new JButton("Confirm Payment Details");		
		confirm.addActionListener(e -> { // When Confirm is pressed, tell the system to start the payment process
			station.payByCash(amountInserted); 
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
	
	private void updateAmountLabel() {
		amountLabel.setText("Amount Inserted = $" + amountInserted);
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
		if(!payWasSuccessful) {
			payFrame.dispose();
			return;
		}
		//Pay was successful
		payFrame.dispose();
		
	}
}
