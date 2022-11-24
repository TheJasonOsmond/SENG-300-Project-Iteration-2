package com.diy.software.system;

import com.jimmyselectronics.OverloadException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AddBags {
	JFrame bagFrame;
	JPanel bagPanel;
	JDialog addOwnBagDialog;
	JLabel bagLabel, confirmLabel;
	JButton purchaseBag;
	DIYSystem station;
	AttendantStation aStation;
	BagDispenser bagDispenser;
	
	private JButton btnCloseWindow;
	private JButton addOwnBags;
	int bag_purchased;
	private CustomerBag shopping_bag;
	int value;
	boolean bagPurchasedSuccessful;
	boolean bagAddedSuccessful;

	/**
	 * GUI for adding bags. User has a choice of adding their own bags or store bought bags.
	 * @param sys Main DIYSystem
	 * @param asys Main Attendant Station
	 */
	public AddBags(DIYSystem sys, AttendantStation asys) {
		shopping_bag = new CustomerBag(10);
		station = sys;
		aStation = asys;
		bag_purchased = 0;
		bagDispenser = sys.getBagDispenserData();
		bagFrame = new JFrame("*****Add Bags*****");
		bagFrame.setResizable(true);
		bagFrame.setUndecorated(false);
		bagFrame.setLocationRelativeTo(null);
		bagFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		bagFrame.getContentPane().setLayout(new BoxLayout(bagFrame.getContentPane(), BoxLayout.X_AXIS));
		bagPanel = new JPanel();
		bagPanel.setLayout(new GridLayout(0, 1));
		bagPanel.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));
		bagFrame.getContentPane().add(bagPanel);
		confirmLabel = new JLabel("");
		confirmLabel.setHorizontalAlignment(SwingConstants.CENTER);
		//Button bag_quantity;

		bagLabel = new JLabel("Please choose an option:");
		bagLabel.setHorizontalAlignment(JLabel.CENTER);
		bagPanel.add(bagLabel);

		purchaseBag = new JButton("Purchase Store Bags");
			purchaseBag.addActionListener(e -> {
				bagPurchasedSuccessful = false;
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
								if (bag_purchased < 0){
									System.out.println("Positive number only");
									custInput = "";
								}
							} catch (NumberFormatException exception) {
								System.out.println("Not an integer");
								custInput = "";
							}

						}

				}

				// This is called if the operation is not canceled.
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
					
						bagDispenser.dispense(bag_purchased);
						
						if (bagDispenser.isDispenserEmpty()) {
							// Disable the system if the bag dispenser is empty.
							// Notify attendant as well.
							sys.systemDisable();
							aStation.noBags(bagDispenser.getAmountOfBags());
							sys.outOfBags();
							updateSystem();
							bagFrame.dispose();
							bagPurchasedSuccessful = true;
						} else {
							bagPurchasedSuccessful = true;
							updateSystem();
							closeWindow();
						}
						
					} else if (value1 == JOptionPane.CANCEL_OPTION) {
						System.out.println("Operation Canceled.");
						closeWindow();
					}
				}	
			});

		bagPanel.add(purchaseBag);
		
		addOwnBags = new JButton("Add Own Bags");
			addOwnBags.addActionListener(e -> {
				bagAddedSuccessful = false;
				JOptionPane pane = new JOptionPane("Please press \"OK\" to confirm you have placed your own bags in the bagging area.", 
						JOptionPane.INFORMATION_MESSAGE, 
						JOptionPane.OK_CANCEL_OPTION);
				JDialog dialog = pane.createDialog(null, "Confirm");
				dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
				dialog.setVisible(true);
				
				// If OK pressed, close dialog and block station. Otherwise, just go back to the dialog.
					int value = ((Integer)pane.getValue()).intValue();
					if (value == JOptionPane.OK_OPTION) {
						bagAddedSuccessful = true;
						// Simulates customer adding their shopping bag to the bagging area.
						// This will trigger a weight change, blocking the system.
						station.getBaggingAreaRef().add(shopping_bag);
						
						bagFrame.dispose();
					} else if (value == JOptionPane.CANCEL_OPTION) {
					    System.out.println("Operation Canceled.");
					    closeWindow();
					}

			});
			
		bagPanel.add(addOwnBags);
		// Exit button
		btnCloseWindow = new JButton("Exit");
			btnCloseWindow.addActionListener(e -> {
					closeWindow();
			});
				
				bagPanel.add(btnCloseWindow);
		bagPanel.add(confirmLabel);

		bagFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		bagFrame.setVisible(true);
		bagFrame.pack();
		bagFrame.setSize(400, 200);
	}
	
	/**
	 * Closes the window, and enables the system again. Used on exit calls.
	 */
	private void closeWindow() {
		station.enableScanningAndBagging();
		bagFrame.dispose();
	}
	
	/**
	 * Gets the amount of bags purchased
	 * @return Amount of bags purchased by customer
	 */
	public int getBag_purchased(){
		return bag_purchased;
	}
	
	/**
	 * Has a bag been purchased successfully?
	 * @return If the bags were purchased successfully.
	 */
	public boolean bagPurchasedSuccessful() {
		return bagPurchasedSuccessful;
	}
	
	/**
	 * Has a bag been added successfully?
	 * @return If the bags were added successfully.
	 */
	public boolean bagAddedSuccessful() {
		return bagAddedSuccessful;
	}
	
	/**
	 * Gets the bag dispenser reference.
	 * @return The bag dispenser used when adding a bag
	 */
	public BagDispenser getBagDispenserRef() {
		return bagDispenser;
	}
	/**
	 * Updates the system's price, weight, and item list.
	 */
	public void updateSystem() {
		station.updateExpectedWeight(bagDispenser.calcTotalBagWeight(bag_purchased));
		station.updateGUIItemList("Store bag", bagDispenser.calcTotalBagPrice(bag_purchased), bagDispenser.calcTotalBagWeight(bag_purchased));
		station.changeReceiptPrice(bagDispenser.calcTotalBagPrice(bag_purchased));
		System.out.println("Bags have been added by customer");
	}

	/**
	 * Triggers Store Bought Bags button, for testing purposes.
	 */
	public void triggerStoreBagButton() {
		// Referenced from https://stackoverflow.com/questions/3079524/how-do-i-manually-invoke-an-action-in-swing
		for(ActionListener a: purchaseBag.getActionListeners()) {
		    a.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null) {
		    });
		}
	}
	
	/**
	 * Triggers Own Bags button, for testing purposes.
	 */
	public void triggerOwnBagButton() {
		// Referenced from https://stackoverflow.com/questions/3079524/how-do-i-manually-invoke-an-action-in-swing
		for(ActionListener a: addOwnBags.getActionListeners()) {
		    a.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null) {
		    });
		}
	}
	
	/**
	 * Triggers Exit button, for testing purposes.
	 */
	public void triggerExitButton() {
		// Referenced from https://stackoverflow.com/questions/3079524/how-do-i-manually-invoke-an-action-in-swing
		for(ActionListener a: btnCloseWindow.getActionListeners()) {
		    a.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null) {
		    });
		}
	}
}

