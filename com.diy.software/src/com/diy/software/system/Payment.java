package com.diy.software.system;

/** Updated to add Tap, swipe Credit Card payments */
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;


import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;

public class Payment {
	JFrame payFrame;
	JPanel payPanel;
	JTextField pin, amountToPay;
	JLabel pinLabel, confirmLabel, amountToPayLabel;
	JButton confirm, btnCloseWindow, btnPayFull;
	DIYSystem station;
	
	JButton tapCard;
	JButton swipeCard;
	JButton insertCard;
	
	private boolean payWasSuccessful = false;
	

	public Payment(DIYSystem sys) {
		station = sys;
		payFrame = new JFrame("***** Pay by Card (Credit) *****");
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

		//Amount to pay label 
		amountToPayLabel = new JLabel("Enter the payment amount");
		amountToPayLabel.setHorizontalAlignment(JLabel.CENTER);
		payPanel.add(amountToPayLabel);
		
		//Amount to pay text field 
		amountToPay = new JTextField();
		amountToPay.setHorizontalAlignment(SwingConstants.CENTER);
		payPanel.add(amountToPay);
		
		
		btnPayFull = new JButton("Pay Full Amount");
		btnPayFull.addActionListener(e -> {
			setTextToFullAmount();
		});
		payPanel.add(btnPayFull);
		
		
		
		//payPanel.add(Box.createVerticalStrut(1)); //invisible separator
		
		pinLabel = new JLabel("Enter PIN");
		pinLabel.setHorizontalAlignment(JLabel.CENTER);
		payPanel.add(pinLabel);
		pin = new JTextField();
		pin.setHorizontalAlignment(SwingConstants.CENTER);
		payPanel.add(pin);
		
		//payPanel.add(Box.createVerticalStrut(1)); //invisible separator

		pinLabel = new JLabel("PIN", SwingConstants.LEFT);
		confirm = new JButton("Insert Card");

		// When the Confirm button is pressed, tell the system to start the payment
		// process
		confirm.addActionListener(e -> {
			payAmount();		
			
		});

		payPanel.add(confirm);
		
		/**
		 * Adding 'Tap' Button
		 */
		tapCard = new JButton("Tap Card");
		//no need to insert this, just do transaction 
		tapCard.addActionListener(e -> station.payByCreditTap());
		payPanel.add(tapCard);

	
		
		/**
		 * Adding 'Swipe' Button
		 */
		swipeCard = new JButton("Swipe Card");
		swipeCard.addActionListener(e -> station.payByCreditSwipe());
		payPanel.add(swipeCard);
		


//
//		//Add More Items Button
//		btnAddMoreItems = new JButton("Add More Items");
//		btnAddMoreItems.addActionListener(e -> {
//			ReturnToScanning();
//		});
//		payPanel.add(btnAddMoreItems);
		
		//Exit Button
		btnCloseWindow = new JButton("Exit");
		btnCloseWindow.addActionListener(e -> {
			closeWindow();
		});
		payPanel.add(btnCloseWindow);
		
		payPanel.add(confirmLabel);

		payFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		payFrame.setVisible(true);
		payFrame.pack();
		payFrame.setSize(400, 400);
	}

	private void payAmount() {
		double amount;
		try { //Convert to double
			amount = Double.parseDouble(amountToPay.getText());
			
		} catch (NumberFormatException e){
			setMessage("Invalid Amount");
			return;
		}
		amount = (double) Math.round(amount * 100) / 100; //rounds to 100th
		
		if(amount <= 0) {
			setMessage("Invalid Amount. Amount must be greater than 0.");
			return;
		}
				
		station.payByCredit(pin.getText(), amount);
	
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
		this.swipeCard.setEnabled(false);
		this.tapCard.setEnabled(false);
	}
	
	public void updatePayStatus(boolean status) {
		this.payWasSuccessful = status;
	}
	
	private void setTextToFullAmount() {
		amountToPay.setText("" + station.getReceiptPrice());
	}
	
//	private void ReturnToScanning() {
//		station.enableScanningAndBagging();
//		payFrame.dispose();
//		if(payWasSuccessful) {
//			//Update the text for total amount due
//			//Print the amount paid into the text box 
//		}
//	}
	
	
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
