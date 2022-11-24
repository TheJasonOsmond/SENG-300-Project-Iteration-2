package com.diy.software.system;


import java.awt.BorderLayout;
import java.awt.Dimension;
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
	JPanel bottomPanel;
	
	JLabel memberNumber,instructionLabel, alertLabel;
	JButton confirm;
	JButton clear;
	JPanel numberPanel;
	JButton[] numberButtons;
	
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
		
		
		memberFrame.setMinimumSize(new Dimension(400, 200));
		memberFrame.getContentPane().setLayout(new BorderLayout());
		
		instructionLabel = new JLabel("Enter membership number");
		instructionLabel.setHorizontalAlignment(JLabel.CENTER);
		memberFrame.add(instructionLabel, BorderLayout.NORTH);
		
		
		memberPanel = new JPanel();
		memberPanel.setLayout(new GridLayout(2,1));
		memberFrame.add(memberPanel, BorderLayout.CENTER);
		
		memberNumber = new JLabel();
		memberNumber.setHorizontalAlignment(SwingConstants.CENTER);
		memberPanel.add(memberNumber);
		
		numberPanel = new JPanel();
		//numberPanel.setMinimumSize(new Dimension(150,200));
		numberPanel.setLayout(new GridLayout(4,3));
		
		
		numberButtons = new JButton[10];
		//create and add number s
		for (int i = 9; i>0;i-- ) {
			numberButtons[i]= new JButton(String.valueOf(i));
			numberButtons[i].addActionListener(e->{
				memberNumber.setText(memberNumber.getText()+((JButton)e.getSource()).getText());
			});
			numberPanel.add(numberButtons[i]);
		
		}
		
		clear = new JButton("Clear");
		clear.addActionListener(e -> {
			memberNumber.setText("");
		});

		numberPanel.add(clear);

		numberButtons[0]= new JButton("0");
		numberButtons[0].addActionListener(e->{
			memberNumber.setText(memberNumber.getText()+((JButton)e.getSource()).getText());
		});
		numberPanel.add(numberButtons[0]);
		
		
		// When the Confirm button is pressed, tell the system to start 
		confirm = new JButton("Confirm ");
		confirm.addActionListener(e -> {
			station.enterMembership(memberNumber.getText());
		});
		numberPanel.add(confirm);
				
		
		memberPanel.add(numberPanel);

		btnCloseWindow = new JButton("Exit");
		btnCloseWindow.addActionListener(e -> {
			closeWindow();
		});
		
		bottomPanel = new JPanel(new GridLayout(1,2));
		
		alertLabel = new JLabel("");
		alertLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		bottomPanel.add(alertLabel);
		bottomPanel.add(btnCloseWindow);
		
		memberFrame.add(bottomPanel, BorderLayout.SOUTH);
		
		memberFrame.pack();
		memberFrame.setVisible(true);
	}

	/**
	 * Triggered from the system to update the message that the customer can see
	 * 
	 * @param msg
	 * @throws InterruptedException
	 */
	public void setMessage(String msg) {
		alertLabel.setText(msg);
	}

	public String getMessage() {
		return this.alertLabel.getText();
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