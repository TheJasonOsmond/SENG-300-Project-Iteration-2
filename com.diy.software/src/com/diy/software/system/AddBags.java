package com.diy.software.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AddBags implements ActionListener {
	JFrame bagFrame;
	JPanel payPanel;
	JDialog addOwnBagDialog;
	JLabel bagLabel, confirmLabel;
	JButton purchaseBag;
	DIYSystem station;
	
	private boolean payWasSuccessful = false;
	private JButton btnCloseWindow;
	private JButton addOwnBags;
	int bag_purchased;
	double bag_weight = 0.10;
	double bag_price = 0.10;

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
		//Button bag_quantity;

		bagLabel = new JLabel("Please choose an option:");
		bagLabel.setHorizontalAlignment(JLabel.CENTER);
		payPanel.add(bagLabel);

		purchaseBag = new JButton("Purchase Store Bags");
			purchaseBag.addActionListener(e -> {
				JOptionPane select_bag_amount = new JOptionPane();
				bag_purchased = Integer.parseInt(JOptionPane.showInputDialog(select_bag_amount,"Enter amount"));
				//bag_purchased = amount;
				//JOptionPane bag_amount = new JOptionPane();
				//select_bag_amount.setVisible(true);
				//Button bag_quantity = new Button("Select the amount");
				//bag_amount.add(bag_quantity);
				//bag_quantity.addActionListener(this);
				JOptionPane confirm_bag_amount = new JOptionPane("Please press \"OK\" to confirm the amount of bags " +
						"purchased.",
				//JOptionPane pane = new JOptionPane("Please press \"OK\" to confirm you have grabbed the store bag
				// in the bagging area.",
						JOptionPane.INFORMATION_MESSAGE,
						JOptionPane.OK_CANCEL_OPTION);
				JDialog dialog = confirm_bag_amount.createDialog(null, "Confirm");
				dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
				dialog.setVisible(true);

				int value = ((Integer)confirm_bag_amount.getValue()).intValue();
				if (value == JOptionPane.OK_OPTION) {
					station.notifyBagWeightChange("Bags have been added by customer");
					System.out.println("Bags have been added by customer");
				} else if (value == JOptionPane.CANCEL_OPTION) {
					System.out.println("Operation Canceled.");
				}

				//Update expected weight and price
				if (bag_purchased > 0){
					double new_item_weight;
					new_item_weight = bag_purchased * bag_weight;
					double total_bag_price;
					total_bag_price = bag_purchased * bag_price;
					station.updateExpectedWeight(new_item_weight);
					station.updateGUIItemList("Store bags purchased", total_bag_price, new_item_weight);
				}
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
				
				// If OK pressed, close dialog and block station. Otherwise, just go back to the dialog.
					int value = ((Integer)pane.getValue()).intValue();
					if (value == JOptionPane.OK_OPTION) {
					    station.notifyBagWeightChange("Bags have been added by customer");
					} else if (value == JOptionPane.CANCEL_OPTION) {
					    System.out.println("Operation Canceled.");
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
	
	
	public void ownBagBlockMessage() {
		
	}

	private void closeWindow() {
		station.enableScanningAndBagging();
		
		if(!payWasSuccessful)
			bagFrame.dispose();
		else
			System.exit(0);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//bag_purchased = 0;
		bag_purchased++;
		purchaseBag.setText("The number of bags purchased is" + bag_purchased);

	}
}
