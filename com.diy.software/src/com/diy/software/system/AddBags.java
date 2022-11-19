package com.diy.software.system;

import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

public class AddBags {
	JFrame bagFrame;
	JPanel payPanel;
	JDialog addOwnBagDialog;
	JLabel bagLabel, confirmLabel;
	JButton purchaseBag;
	DIYSystem station;
	
	private boolean payWasSuccessful = false;
	private JButton btnCloseWindow;
	private JButton addOwnBags;

	public AddBags(DIYSystem sys) {
		station = sys;
		bagFrame = new JFrame("*****Add Bags*****");
		bagFrame.setResizable(true);
		bagFrame.setUndecorated(false);
		bagFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		bagFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				station.reEnableScanner();
			}
		});
		bagFrame.getContentPane().setLayout(new BoxLayout(bagFrame.getContentPane(), BoxLayout.X_AXIS));
		payPanel = new JPanel();
		payPanel.setLayout(new GridLayout(0, 1));
		payPanel.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));
		bagFrame.getContentPane().add(payPanel);
		confirmLabel = new JLabel("");
		confirmLabel.setHorizontalAlignment(SwingConstants.CENTER);

		bagLabel = new JLabel("Please choose an option:");
		bagLabel.setHorizontalAlignment(JLabel.CENTER);
		payPanel.add(bagLabel);

		purchaseBag = new JButton("Purchase Store Bags");
			purchaseBag.addActionListener(e -> {
				//TODO Popup in Attendant station
				
				
			});

		payPanel.add(purchaseBag);
		
		addOwnBags = new JButton("Add Own Bags");
			addOwnBags.addActionListener(e -> {
				
				JOptionPane pane = new JOptionPane("Please press \"OK\" to confirm you have placed your own bags in the bagging area.", 
						JOptionPane.INFORMATION_MESSAGE, 
						JOptionPane.OK_CANCEL_OPTION);
				JDialog dialog = pane.createDialog(null, "Confirm");
				dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
				dialog.setVisible(true);
				
				// If OK pressed, close dialog and block
				

					int value = ((Integer)pane.getValue()).intValue();
					if (value == JOptionPane.OK_OPTION) {
					    System.out.println("Yes");
					} else if (value == JOptionPane.CANCEL_OPTION) {
					    System.out.println("No");
					}
				
				
				
			});
		payPanel.add(addOwnBags);
		
		
		btnCloseWindow = new JButton("Exit");
			btnCloseWindow.addActionListener(e -> {
					closeWindow();
			});
				
				payPanel.add(btnCloseWindow);
		payPanel.add(confirmLabel);

		bagFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		bagFrame.setVisible(true);
		bagFrame.pack();
		bagFrame.setSize(400, 200);
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
	
	public void disableButtons() {
		this.purchaseBag.setEnabled(false);
	}
	
	public void updatePayStatus(boolean status) {
		this.payWasSuccessful = status;
	}
	
	public void ownBagBlockMessage() {
		
	}

	private void closeWindow() {
		station.enableScanningAndBagging();
		
		if(!payWasSuccessful)
			bagFrame.dispose();
		else
			System.exit(0);
	}
}
