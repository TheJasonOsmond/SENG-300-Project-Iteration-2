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
				String custInput = "";
				boolean canceled = false;
				
				// Keep looping if there is no input or if it equals 0, or not canceled.
				while (custInput.length() == 0 || custInput.equals("0") || canceled) {
						canceled = false;
						custInput = JOptionPane.showInputDialog(select_bag_amount, "Enter amount of bags:");
						if (custInput == null) {
							// If we are here, the operation was canceled.
							System.out.println("Operation Canceled.");
							closeWindow();
							canceled = true;
							// Breaking out of loop
							break;
						} else {
							try {
								bag_purchased = Integer.parseInt(custInput);
							} catch (NumberFormatException exception) {
								System.out.println("Not an integer");
								custInput = "";
							}
						}
				}
				
				if (!canceled) {
					JOptionPane confirm_bag_amount = new JOptionPane("Please press \"OK\" to confirm the amount of bag(s) purchased is " +
							custInput,
					//JOptionPane pane = new JOptionPane("Please press \"OK\" to confirm you have grabbed the store bag
					// in the bagging area.",
							JOptionPane.INFORMATION_MESSAGE,
							JOptionPane.OK_CANCEL_OPTION);
					JDialog dialog = confirm_bag_amount.createDialog(null, "Confirm");
					dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
					dialog.setVisible(true);

					int value1 = ((Integer)confirm_bag_amount.getValue()).intValue();
					if (value1 == JOptionPane.OK_OPTION) {
						//Update expected weight and price
						double new_item_weight;
						new_item_weight = bag_purchased * bag_weight;
						double total_bag_price;
						total_bag_price = bag_purchased * bag_price;
						station.updateExpectedWeight(new_item_weight);
						//TODO Should we update item list in the main system? Because we need the attendant station block to appear.
						station.updateGUIItemList("Store bag", total_bag_price, new_item_weight);
						station.notifyBagWeightChange("Bags have been added by customer");
						System.out.println("Bags have been added by customer");
						closeWindow();
					} else if (value1 == JOptionPane.CANCEL_OPTION) {
						System.out.println("Operation Canceled.");
						closeWindow();
					}
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
					    closeWindow();
					} else if (value == JOptionPane.CANCEL_OPTION) {
					    System.out.println("Operation Canceled.");
					    closeWindow();
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
	
	private void closeWindow() {
		station.enableScanningAndBagging();
		bagFrame.dispose();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//bag_purchased = 0;
		bag_purchased++;
		purchaseBag.setText("The number of bags purchased is" + bag_purchased);

	}
	public int getBag_purchased(){
		return bag_purchased;
	}
}
