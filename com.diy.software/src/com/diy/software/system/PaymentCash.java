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
	private DIYSystem station;
	private JFrame payFrame;
	private JPanel payPanel;
	private JLabel remainingLabel, confirmLabel, cashDisplay;
	
	private JButton confirm, btncollectChange, btnCloseWindow;
	private JButton	btnCoin1, btnCoin2; 
	private JButton insertNote;
	
	private double cashReceived = 0, amountRemaining, changeDue = 0;
	private boolean payWasSuccessful = false;

	public PaymentCash(DIYSystem sys) {
		//this is just copy paste from Payment.java for now...
		station = sys;
		amountRemaining = station.getReceiptPrice();
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

		remainingLabel = new JLabel("Amount Remaining: $" +  amountRemaining);
		remainingLabel.setHorizontalAlignment(JLabel.CENTER);
		payPanel.add(remainingLabel);
		
		//this displays the amount of cash inserted
		cashDisplay = new JLabel("Cash Inserted: $0.00");
		cashDisplay.setHorizontalAlignment(SwingConstants.CENTER);
		payPanel.add(cashDisplay);
		
		//press to insert a coin; this could be done better........
		btnCoin1 = new JButton("Insert $1 Coin");
		btnCoin1.addActionListener(e -> {
			station.InsertCoin(curr, 1l);
		});
		payPanel.add(btnCoin1);
		
		//Insert $2 Coin
		btnCoin2 = new JButton("Insert $2 Coin");
		btnCoin2.addActionListener(e -> {
			station.InsertCoin(curr, 2l);
		});
		payPanel.add(btnCoin2);
		
		insertNote = new JButton("Insert $1 Banknote");
		insertNote.addActionListener(e -> {
			station.InsertBanknote(curr, 1l);
		});
		payPanel.add(insertNote);
		
		// pinLabel = new JLabel("PIN", SwingConstants.LEFT);
		
		// When the Confirm button is pressed, tell the system to start the payment
		// process
		confirm = new JButton("Confirm Payment Details");
		confirm.addActionListener(e -> {
			station.payByCash(cashReceived);
			checkIfChangeisDue();
		});
		payPanel.add(confirm);
		
		//Collect Change
		btncollectChange = new JButton("Collect Change");
		btncollectChange.addActionListener(e -> {
			station.collectChange();
			System.out.println("Collect Change Pressed");
			//station.payByCash(cashReceived);
		});
		payPanel.add(btncollectChange);

		btnCloseWindow = new JButton("Exit");
		btnCloseWindow.addActionListener(e -> {
			if (cashReceived > 0);
				station.updateGUIItemListPayment(cashReceived);
			closeWindow();
		});
		
		payPanel.add(btnCloseWindow);
		payPanel.add(confirmLabel);
		
		disableCollectChange();

		payFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		payFrame.setVisible(true);
		payFrame.pack();
		payFrame.setSize(400, 600);
	}

	public void updateCashDisplay() {
		cashDisplay.setText("Cash Inserted: $" + 
				(double) Math.ceil(cashReceived * 100) / 100);
		remainingLabel.setText("Amount Remaining: $" +  amountRemaining);
		blockIfCashExceedTotal();
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
	
	public void addChangeDue(double amount) {
		if(amount > 0) { //change must be non-negative
			changeDue += amount;			
		}
		
	}
	
	public void deductChangeDue(double amount) {
		if(amount >= changeDue) { //change must be non-negative
			changeDue = 0;	
			return;
		}
		changeDue -= amount;	
	}
	
	
	public void collectChange(double amountCollected) {
		deductChangeDue(amountCollected);
		if (changeDue == 0) {
			closeWindow();			
		}
	}
	
	public void checkIfChangeisDue() {
		if(changeDue > 0) {
			enableCollectChange();
			disableCloseWindow();
			disablePaying();
		}		
	}
	
	private void disableCollectChange() {
		btncollectChange.setEnabled(false);
	}
	private void enableCollectChange() {
		btncollectChange.setEnabled(true);
	}
	
	
	public void disableCloseWindow() {
		btnCloseWindow.setEnabled(false);
	}
		
	
	public void disablePaying() {
		confirm.setEnabled(false);
		blockCashInsertion();
		
	}
	
	public void updatePayStatus(boolean status) {
		this.payWasSuccessful = status;
	}

	public void addCashReceived(long amountAdded) {
		cashReceived += amountAdded;
		deductRemaining(amountAdded);
	}
	
	private void deductRemaining(long amount) {
		if( amountRemaining > amount){
			amountRemaining -= amount;
			return;
		}
		amountRemaining = 0;
		return;
	}
	
	
	private void blockIfCashExceedTotal() {
		if (station.getReceiptPrice() <= 0)
			blockCashInsertion();
		
	}
	
	private void blockCashInsertion() {
		btnCoin1.setEnabled(false);
		btnCoin2.setEnabled(false);
		insertNote.setEnabled(false);
	}
	
	private void unblockCashInsertion(){
		btnCoin1.setEnabled(true);
		btnCoin2.setEnabled(true);
		insertNote.setEnabled(true);
	}
	
	private void closeWindow() {
		station.enableScanningAndBagging();
		
		if(!payWasSuccessful)
			payFrame.dispose();
		else
			System.exit(0);
	}
}
