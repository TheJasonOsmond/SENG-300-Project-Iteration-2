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
public class Membership {
	JFrame memberFrame;
	JPanel memberPanel;
	JTextField memberNumber;
	JLabel memberNumberLabel, confirmLabel;
	JButton confirm;
	DIYSystem station;
	
	private boolean enterSuccessful = false;
	private JButton btnCloseWindow;

	public Membership(DIYSystem sys) {
		station = sys;
		memberFrame = new JFrame("***** Enter Membership Number *****");
		memberFrame.setResizable(true);
		memberFrame.setUndecorated(false);
		memberFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		memberFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				station.reEnableScanner();
			}
		});
		memberFrame.getContentPane().setLayout(new BoxLayout(memberFrame.getContentPane(), BoxLayout.X_AXIS));
		memberPanel = new JPanel();
		memberPanel.setLayout(new GridLayout(0, 1));
		memberPanel.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));
		memberFrame.getContentPane().add(memberPanel);
		confirmLabel = new JLabel("");
		confirmLabel.setHorizontalAlignment(SwingConstants.CENTER);

		memberNumberLabel = new JLabel("Enter membership number");
		memberNumberLabel.setHorizontalAlignment(JLabel.CENTER);
		memberPanel.add(memberNumberLabel);
		memberNumber = new JTextField();
		memberNumber.setHorizontalAlignment(SwingConstants.CENTER);
		memberPanel.add(memberNumber);

		// pinLabel = new JLabel("PIN", SwingConstants.LEFT);
		confirm = new JButton("Confirm ");

		// When the Confirm button is pressed, tell the system to start 
		
		/**
		 * Add membership method to station 
		confirm.addActionListener(e -> {
			station.mmbership(memberNumber.getText());
		});**/

		memberPanel.add(confirm);

		btnCloseWindow = new JButton("Exit");
		btnCloseWindow.addActionListener(e -> {
			closeWindow();
		});
		
		memberPanel.add(btnCloseWindow);
		memberPanel.add(confirmLabel);

		memberFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		memberFrame.setVisible(true);
		memberFrame.pack();
		memberFrame.setSize(400, 200);
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
	
	public void disable() {
		this.confirm.setEnabled(false);
	}
	
	public void update(boolean status) {
		this.enterSuccessful = status;
	}

	private void closeWindow() {
		station.enableScanningAndBagging();
		
		if(!enterSuccessful)
			memberFrame.dispose();
		else
			System.exit(0);
	}
}
