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
	JTextField pin;
	JLabel pinLabel, confirmLabel;
	JButton confirm;
	DIYSystem station;
	
	JButton tapCard;
	JButton swipeCard;
	JButton insertCard;
	
	private boolean payWasSuccessful = false;
	private JButton btnCloseWindow;

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
			station.payByDebit(pin.getText());
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
		tapCard.addActionListener(e -> station.payByDebitTap());
		payPanel.add(tapCard);

	
		
		/**
		 * Adding 'Swipe' Button
		 */
		swipeCard = new JButton("Swipe Card");
		swipeCard.addActionListener(e -> station.payByDebitSwipe());
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

	private void closeWindow() {
		station.enableScanningAndBagging();
		
		if(!payWasSuccessful)
			payFrame.dispose();
		else
			System.exit(0);
	}
}
