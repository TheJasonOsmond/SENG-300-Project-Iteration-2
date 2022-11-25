package com.diy.software.system;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import com.unitedbankingservices.banknote.Banknote;
import com.unitedbankingservices.banknote.BanknoteDispenserAR;
import com.unitedbankingservices.coin.Coin;

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
 * @author Jason Osmond & Jesse Dirks
 */
public class PaymentCash {
	private DIYSystem station;
	private JFrame payFrame;
	private JPanel payPanel;
	private JLabel remainingLabel, confirmLabel, cashDisplay;
	
	private JButton confirm, btncollectChange;
	private JButton	btnCoin5, btnCoin10, btnCoin25, btnCoin100, btnCoin200; 
	private JButton btnNote5, btnNote10, btnNote20, btnNote50, btnNote100;
	
	private JButton[] noteButtons = {btnNote100, btnNote50, btnNote20, btnNote10, btnNote5}; //Sorted Decreasing Order, Same as denominations in DIYSTATION
	private JButton[] coinButtons = {btnCoin200, btnCoin100, btnCoin25, btnCoin10, btnCoin5}; //Sorted Decreasing Order
//	private Map<Long, JButton> coinButtonMap;
//	private Map<Integer, JButton> noteButtonMap;
	
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
		
		//Add All Bank Note Buttons
		for (int i = 0; i < noteButtons.length; i++) {
			int denomination = DIYSystem.acceptedNoteDenominations[i];
			noteButtons[i] = new JButton("Insert $" + denomination +" Bank Note");
			noteButtons[i].addActionListener(e -> {
				Banknote banknote = new Banknote(curr, denomination);
				station.InsertBanknote(banknote);
			});
			payPanel.add(noteButtons[i]);
		}
		
		//Add All CoinButtons
		for (int i = 0; i < coinButtons.length; i++) {
			long denomination = DIYSystem.acceptedCoinDenominations[i];
			coinButtons[i] = new JButton("Insert $" + DIYSystem.convertCentsToDollars(denomination) +" Coin");
			coinButtons[i].addActionListener(e -> {
				Coin coin = new Coin(curr, denomination);
				station.InsertCoin(coin);
			});
			payPanel.add(coinButtons[i]);
		}
//		
//		btnCoin200 = new JButton("Insert $" + DIYSystem.convertCentsToDollars(2) +" Coin");
//		btnCoin200.addActionListener(e -> {
//			station.InsertCoin(curr, 200l);
//		});
//		payPanel.add(btnCoin200);
		
		//Collect Change (starts disabled)
		btncollectChange = new JButton("Collect Change");
		btncollectChange.addActionListener(e -> {
			changeCollected = station.collectChange();
//			station.updateGUIItemListCollectCash(changeCollected);
//			closeWindow();
		});
		payPanel.add(btncollectChange);
		disableCollectChange();
		
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
		

		payFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		payFrame.setVisible(true);
		payFrame.pack();
		payFrame.setSize(400, 600);
	}
	
	public void cashReceived(double cashAmount) {
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
		for(JButton coinButton : coinButtons)
			coinButton.setEnabled(false);
		for(JButton noteButton : noteButtons)
			noteButton.setEnabled(false);
	}
	
	
	private void closeWindow() {
		station.enableScanningAndBagging();
		
		if(!payWasSuccessful)
			payFrame.dispose();
		else
			System.exit(0);
	}
}
