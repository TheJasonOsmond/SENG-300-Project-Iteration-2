package com.diy.software.system;


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
import javax.swing.BoxLayout;

public class PaymentDebit {
	JFrame payFrame;
	JPanel payPanel;
	JTextField pin, amountToPay;
	JLabel pinLabel, confirmLabel, amountToPayLabel;
	JButton confirm, btnCloseWindow, btnPayFull;
	DIYSystem station;
	
	JButton tapCard;
	JButton swipeCard;
	JButton insertCard;

	double partialPaymentAmount;
	private boolean payWasSuccessful = false;

	public PaymentDebit(DIYSystem sys) {
		station = sys;
		payFrame = new JFrame("***** Pay by Card (Debit)*****");
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

		pinLabel = new JLabel("Enter PIN");
		pinLabel.setHorizontalAlignment(JLabel.CENTER);
		payPanel.add(pinLabel);
		pin = new JTextField();
		pin.setHorizontalAlignment(SwingConstants.CENTER);
		payPanel.add(pin);

		pinLabel = new JLabel("PIN", SwingConstants.LEFT);
		confirm = new JButton("Insert Card");

			// When the Confrim button is pressed, tell the system to start the payment
		// process
		confirm.addActionListener(e -> {
			if(getPartialPayment())//gets the partial payment amount
				station.payByDebit(pin.getText(),partialPaymentAmount);
			});

		payPanel.add(confirm);
		
		/**
		 * Adding 'Insert' Button
		 */
		//insertCard = new JButton("Insert Card");
		//insertCard.addActionListener(e -> station.payByDebit(pin.getText()));
		//payPanel.add(insertCard);
		//Working

	/**
		 * Adding 'Tap' Button
		 */
		tapCard = new JButton("Tap Card");
		//no need to insert this, just do transaction 
		tapCard.addActionListener(e ->
		{
			if(getPartialPayment())
				station.payByDebitTap(partialPaymentAmount);
				
		});
		payPanel.add(tapCard);
	
		
		/**
		 * Adding 'Swipe' Button
		 */
		swipeCard = new JButton("Swipe Card");
		swipeCard.addActionListener(e ->{
			if(getPartialPayment())
				station.payByDebitSwipe(partialPaymentAmount);
		});
		payPanel.add(swipeCard);


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

	private boolean getPartialPayment() 
	{
		double amount;
		try 
		{ //Convert to double
			amount = Double.parseDouble(amountToPay.getText());
			amount = (double) Math.round(amount * 100) / 100; //rounds to 100th
			System.out.println("partial payment amount "+amount);
			System.out.println("total amount to pay "+station.getReceiptPrice());
			if (amount > station.getReceiptPrice())
			{
				setMessage("Cannot pay for more than total ");
				return false;
			}
			if(amount <= 0) {
				setMessage("Invalid Amount. Amount must be greater than 0.");
				return false;
			}
			
			partialPaymentAmount = amount;
			return true;
			//this is amount that is to be paid
			//station.payByCredit(pin.getText(), amount);
			
		} catch (NumberFormatException e){
			setMessage("Invalid Amount");
			return false;
		}
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
	
	private void closeWindow() {
		station.enableScanningAndBagging();
		
		if(!payWasSuccessful)
		{
			payFrame.dispose();
			return;
		}
		//Pay was successful
				payFrame.dispose();	
		//else
			//System.exit(0);
	}
}