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
/**
 * 
 * @author Jason Osmond
 */
public class PaymentCash {
	private DIYSystem station;
	private JFrame payFrame;
	private JPanel payPanel;
	private JLabel remainingLabel, confirmLabel, cashDisplay;
	
	private JButton confirm, btncollectChange, btnCloseWindow;
	private JButton	btnCoin1, btnCoin2; 
	private JButton btnNote5, btnNote100;
	
	private double cashReceived = 0, changeCollected = 0;
	private boolean payWasSuccessful = false;

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

		remainingLabel = new JLabel("Amount Remaining: $" +  station.getReceiptPrice());
		remainingLabel.setHorizontalAlignment(JLabel.CENTER);
		payPanel.add(remainingLabel);
		
		//this displays the amount of cash inserted
		cashDisplay = new JLabel("Cash Inserted: $0.00");
		cashDisplay.setHorizontalAlignment(SwingConstants.CENTER);
		payPanel.add(cashDisplay);
		
		//press to insert a coin; this could be done better........
		btnCoin1 = new JButton("Insert $1 Coin");
		btnCoin1.addActionListener(e -> {
			station.InsertCoin(curr, Long.valueOf(1L));
		});
		payPanel.add(btnCoin1);
		
		//Insert $2 Coin
		btnCoin2 = new JButton("Insert $2 Coin");
		btnCoin2.addActionListener(e -> {
			station.InsertCoin(curr, 2l);
		});
		payPanel.add(btnCoin2);
		
		btnNote5 = new JButton("Insert $5 Banknote");
		btnNote5.addActionListener(e -> {
			station.InsertBanknote(station.getCurrency(), 5);
		});
		payPanel.add(btnNote5);
		
		btnNote100 = new JButton("Insert $100 Banknote");
		btnNote100.addActionListener(e -> {
			station.InsertBanknote(station.getCurrency(), 100);
		});
		payPanel.add(btnNote100);
		
		// pinLabel = new JLabel("PIN", SwingConstants.LEFT);
		

		
		//Collect Change
		btncollectChange = new JButton("Collect Change");
		btncollectChange.addActionListener(e -> {
			changeCollected = station.collectChange();
//			station.updateGUIItemListCollectCash(changeCollected);
//			closeWindow();
		});
		payPanel.add(btncollectChange);

//		btnCloseWindow = new JButton("Exit");
//		btnCloseWindow.addActionListener(e -> {
//			if (cashReceived > 0);
//				station.updateGUIItemListPayment(cashReceived);
//			closeWindow();
//		});
		
//		payPanel.add(btnCloseWindow);
		
		// When the Confirm button is pressed, tell the system to start the payment
		// process
		confirm = new JButton("Confirm Payment & Exit");
		confirm.addActionListener(e -> {
			station.payByCash(cashReceived);
			if(!checkIfChangeisDue())
				closeWindow();
		});
		payPanel.add(confirm);
		
		confirmLabel = new JLabel("");
		confirmLabel.setHorizontalAlignment(SwingConstants.CENTER);
		payPanel.add(confirmLabel);
		
		disableCollectChange();

		payFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		payFrame.setVisible(true);
		payFrame.pack();
		payFrame.setSize(400, 600);
	}
	
	public void cashReceived(long cashAmount) {
		cashReceived += cashAmount;
		updateCashDisplay();
	}
	
	public void updateCashDisplay() {
		cashDisplay.setText("Cash Inserted: $" + 
				(double) Math.ceil(cashReceived * 100) / 100);
		remainingLabel.setText("Amount Remaining: $" +  station.getReceiptPrice());
		blockIfCashExceedTotal();
	}
	
	public void changeCollected() {
		closeWindow();
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
	
	public boolean checkIfChangeisDue() { //TODO Should be done in the station
		if(station.getChangeDue() > 0) {
			enableCollectChange();
//			disableCloseWindow();
			disablePaying();
			station.dispenseChangeDue();
			setMessage("Please Collect Change Before Proceeding");
			return true;
		}	
		return false;
	}
	
	private void disableCollectChange() {
		btncollectChange.setEnabled(false);
	}
	private void enableCollectChange() {
		btncollectChange.setEnabled(true);
	}
	
	
//	public void disableCloseWindow() {
//		btnCloseWindow.setEnabled(false);
//	}
		
	
	public void disablePaying() {
		confirm.setEnabled(false);
		blockCashInsertion();
		
	}
	
	public void updatePayStatus(boolean status) {
		this.payWasSuccessful = status;
	}
	
	
	private void blockIfCashExceedTotal() {
		if (station.getReceiptPrice() <= 0)
			blockCashInsertion();
		
	}
	
	private void blockCashInsertion() {
		btnCoin1.setEnabled(false);
		btnCoin2.setEnabled(false);
		btnNote5.setEnabled(false);
		btnNote100.setEnabled(false);
		
	}
	
//	private void unblockCashInsertion(){
//		btnCoin1.setEnabled(true);
//		btnCoin2.setEnabled(true);
//		insertNote.setEnabled(true);
//	}
	
	private void closeWindow() {
		station.enableScanningAndBagging();
		
		if(!payWasSuccessful)
			payFrame.dispose();
		else
			System.exit(0);
	}
}
