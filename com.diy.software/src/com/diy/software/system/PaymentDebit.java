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
	DIYSystem station;
	JFrame payFrame;
	JPanel payPanel;
	JLabel pinLabel, confirmLabel;
	JTextField pin;
	JButton insertCardButton;
	JButton tapCardButton;
	JButton swipeCard;
	
	private boolean payWasSuccessful = false;
	private JButton btnCloseWindow;

	/**
	 * Constructor and main method of the class. Creates a payment debit instance and shows the Debit Payment Window pop up on the screen.
	 * @param sys The DIYSystem instance to be displayed.
	 */
	public PaymentDebit(DIYSystem sys) {
		station = sys;
		// Create the main payFrame window.
		payFrame = new JFrame("Debit Payment Window");
		payFrame.setResizable(true);
		payFrame.setUndecorated(false);
		payFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		payFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		payFrame.addWindowListener(new WindowAdapter() {
			// Re-enable the scanner once the window is closed.
			public void windowClosing(WindowEvent e) {
				station.reEnableScanner();
			}
		});
		payFrame.getContentPane().setLayout(new BoxLayout(payFrame.getContentPane(), BoxLayout.X_AXIS));
		payFrame.setVisible(true);
		payFrame.pack();
		payFrame.setSize(400, 400);

		//Create Panel to put Labels in
		payPanel = new JPanel();
		payPanel.setLayout(new GridLayout(0, 1));
		payPanel.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));
		payFrame.getContentPane().add(payPanel);

		// Add a Confirm Label
		confirmLabel = new JLabel("");
		confirmLabel.setHorizontalAlignment(SwingConstants.CENTER);
		payPanel.add(confirmLabel);

		// Add a Pin entry Label with a text field
		pinLabel = new JLabel("Enter PIN");
		pinLabel.setHorizontalAlignment(JLabel.CENTER);
		payPanel.add(pinLabel);
		pin = new JTextField();
		pin.setHorizontalAlignment(SwingConstants.CENTER);
		payPanel.add(pin);
		pinLabel = new JLabel("PIN", SwingConstants.LEFT); //TODO: Figure out what this is

		// Create a button to insert card
		insertCardButton = new JButton("Insert Card");
		insertCardButton.addActionListener(e -> station.payByDebit(pin.getText())); // When the Insert Card is pressed, tell the system to start the payment process
		payPanel.add(insertCardButton);

		// Create a button to tap card
		tapCardButton = new JButton("Tap Card"); //no need to insert this, just do transaction
		tapCardButton.addActionListener(e -> station.payByDebitTap());
		payPanel.add(tapCardButton);

		// Create a button to swipe card
		swipeCard = new JButton("Swipe Card");
		swipeCard.addActionListener(e -> station.payByDebitSwipe());
		payPanel.add(swipeCard);

		// Create a button to exit pay window
		btnCloseWindow = new JButton("Exit");
		btnCloseWindow.addActionListener(e -> closeWindow());
		payPanel.add(btnCloseWindow);
	}

	/**
	 * Triggered from the system to update the message that the customer can see.
	 * @param msg The message to be shown.
	 */
	public void setMessage(String msg) {
		confirmLabel.setText(msg);
	}

	/**
	 * Getter method to get the message that was set to the confirmLabel
	 * @return The message from the confirmLabel
	 */
	public String getMessage() {
		return this.confirmLabel.getText();
	}

	/**
	 * Disables the option to pay.
	 */
	public void disablePaying() {
		this.insertCardButton.setEnabled(false);
		this.swipeCard.setEnabled(false);
		this.tapCardButton.setEnabled(false);
	}

	/**
	 * Updates the payWasSuccessful status.
	 * @param status State to be updated to
	 */
	public void updatePayStatus(boolean status) {
		this.payWasSuccessful = status;
	}

	/**
	 * Closes the payment window. NOTE: Entire program exits if payment was successful.
	 */
	private void closeWindow() {
		station.enableScanningAndBagging();
		
		if(!payWasSuccessful)
			payFrame.dispose();
		else
			System.exit(0);
	}
}
