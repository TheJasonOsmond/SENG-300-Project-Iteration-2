package com.diy.software.system;

import java.io.IOException;
import java.util.NoSuchElementException;
import com.diy.hardware.*;
import com.jimmyselectronics.OverloadException;
import com.jimmyselectronics.disenchantment.TouchScreen;
import com.jimmyselectronics.opeechee.BlockedCardException;
import com.jimmyselectronics.opeechee.Card;
import com.jimmyselectronics.opeechee.ChipFailureException;
import com.jimmyselectronics.opeechee.InvalidPINException;
import com.jimmyselectronics.virgilio.ElectronicScale;

/** ITERATION 1.0
 * The logic of the system. Booted from Start. 
 * Currenty supports adding via scanning an item, and charging the customer via a selected card
 * @authors Brandon Greene (UCID 30157643)
 * 			Daniel Tangtakoune (UCID 30137487),
 * 			Saja AbuFarha (UCID 30163097), 
 * 			Travis Hamilton (UCID 30143567), 
 * 			Sai, 
 * 			Rose
 *
 */

public class DIYSystem {
	
	//Self Checkout unit and the observers we are using
	private DoItYourselfStationAR station;
	private CardReaderObserver cardReaderObs;
	private BarcodeScannerObserver scannerObs;
	private ElectronicScaleObserver scaleObs;
	private	TouchScreenObserver touchObs;
	
	//Customer IO Windows
	private Payment payWindow;
	private PaymentDebit payWindowDebit;
	private DiyInterface mainWindow;
	
	//Customer Data Instance
	private final CustomerData customerData;
	
	//System Variables
	private double amountToBePayed; //TOTAL AMOUNT OWED BY CUSTOMER, INCREMENTED ON SUCCESSFUL ITEM SCAN VIA BARCODESCANNEROBSERVER
	private double baggingAreaCurrentWeight;
	private double baggingAreaExpectedWeight;
	private boolean wasSuccessScan = false;
	private boolean bagItemSuccess = false;
	private boolean wasPaymentPosted = false;
	private TouchScreen touchScreen;
	private ElectronicScale baggingArea;
	private static double scaleMaximumWeightConfiguration = 5000.0;
	private static double scaleSensitivityConfiguration = 0.5;
	private Card debitCardSelected = null;
	private Card creditCardSelected = null;
	
	public DIYSystem(CustomerData c) {
		customerData = c;
		initialize();
	}

	/*
	 * Set up the system to a default state, with an attending customer and DIY station
	 */
	private void initialize() {
		//Set up the DIY Station
		station = new DoItYourselfStationAR();
		station.plugIn();
		//station.turnOn();
		touchScreen = new TouchScreen();
		baggingArea = new ElectronicScale(scaleMaximumWeightConfiguration, scaleSensitivityConfiguration);
		baggingArea.plugIn();
		//baggingArea.turnOn();
		touchScreen.plugIn();
		//touchScreen.turnOn();
		//These turnOn can result in power failure
		boolean goodPower = false;
		while (!goodPower){
			try{
				station.turnOn();
				baggingArea.turnOn();
				touchScreen.turnOn();
				goodPower = true;
			}
			catch (Exception e){
				goodPower = false;
			}
		}
		//Shouldn't catching the no power exception be necessary in the implementation? - Eusa

		//Set default weight for the system to reference
		try {
			//baggingAreaExpectedWeight = station.baggingArea.getCurrentWeight();
			baggingAreaExpectedWeight = baggingArea.getCurrentWeight();
		} catch (OverloadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Set up the required observers
		cardReaderObs = new CardReaderObserver(this);
		scannerObs = new BarcodeScannerObserver(this);
		scaleObs = new ElectronicScaleObserver(this);
		touchObs = new TouchScreenObserver();
			
		//Register the observer to the CardReader on the DIY Station
		station.cardReader.register(cardReaderObs);
		//station.baggingArea.register(scaleObs);
		baggingArea.register(scaleObs);
		station.scanner.register(scannerObs);
		//station.touchScreen.register(touchObs);
		touchScreen.register(touchObs);
		
		//Setup the Customer and start using the DIY station
		customerData.customer.useStation(station);
		
		//TODO: Launch the GUI for the customer to see with a completed GUI from GUI team
		mainWindow = new DiyInterface(this);
		/*
		station.touchScreen.getFrame().getContentPane().add(mainWindow);
		station.touchScreen.getFrame().pack();
		station.touchScreen.getFrame().setSize(600, 600);
		station.touchScreen.getFrame().setLocationRelativeTo(null);
		station.touchScreen.setVisible(true);
		*/
		touchScreen.getFrame().getContentPane().add(mainWindow);
		touchScreen.getFrame().pack();
		touchScreen.getFrame().setSize(700, 600);
		touchScreen.getFrame().setLocationRelativeTo(null);
		touchScreen.setVisible(true);
		
		sendMsgToGui("Begin Scanning");
	}
	


	/**
	 * Scans an item.
	 */
	public void systemStartScan() {
		//START SCAN BY SELECTING AN ITEM IF WE CAN
		try {
			customerData.customer.selectNextItem();
		} catch(NoSuchElementException e) {
			sendMsgToGui("No more items in the cart...");
			return;
		}
		customerData.customer.scanItem();
		systemDisable();
		//ON FAILED SCAN, DO NOT CYCLE TO NEXT ITEM, RESCAN THE SAME ONE
		if(!wasSuccessScan) {
			customerData.customer.deselectCurrentItem();
			systemEnable();
			sendMsgToGui("Scan Failed! Try Item Again!");
		} else {
			systemEnable();
			//PROCEED WITH BAGGING AN ITEM
			disableScanning();
			mainWindow.enableBagging();
		}
	}
	
	/**
	 * Bags the Item for the user.
	 */
	public void StartBagging() {
		//THE CUSTOMER BAGS THEIR ITEM (SIMULATED VIA BUTTON ON GUI)
		customerData.customer.placeItemInBaggingArea();
		bagItemSuccess = true;
		//cheat code
		if(bagItemSuccess) {
			reEnableScanning();
		}
	}



	public double updateExpectedWeight(ElectronicScale baggingArea, double itemExpectedWeight) throws OverloadException {
		baggingAreaCurrentWeight = baggingArea.getCurrentWeight();
		baggingAreaExpectedWeight = baggingAreaCurrentWeight + itemExpectedWeight;
		return baggingAreaExpectedWeight;
	}
	

	
	/** COME BACK TO THIS WHEN MULTIPLE PAYMENTS ARE OPTIONALS?
	 * The customer has requested to pay via CREDIT CARD via the GUI interface (when implemented)
	 * @param pin, the supplied pin from the GUI
	 * @param type, the supplied type via the GUI
	 * @throws Exception
	 *
	//public void startPayment() {
	//	//Disable scanner from further input
	//	amountToBePayed = 50;//TESTING!!
	//	station.scanner.disable();
	//	//This will support the other options for paying, but only calls pay by credit for now
	//	if(amountToBePayed <= 0) {
	//		//DO NOT START PAYMENT, NOTHING SCANNED IN
	//		//TODO:Display error to the main gui and restart the scanner
	//		reEnableScanner();
	//	} else {
	//		//Card To Use
	//		customerData.customer.selectCard("Visa");
	//		payWindow = new Payment(this);
	//	}
	//}
	 */

	 /**
	 * Starts the pay by credit process from the main window.
	 * @param data The card type passed from the main window as String.
	 */
	public void payByCreditStart(String data) {
		if(amountToBePayed <= 0 ) {
			mainWindow.setMsg("Please Scan at least one item");
			return; //TODO: display error that cant make payment on no money
		} 
		if(data == null) {
			mainWindow.setMsg("Please select a valid Card");
			return; // display error that cant make payment on no money
		}

		String[] arrOfStr = data.split(",");
		String type = arrOfStr[1].substring(1);//removing the leading space

		for(Card card : this.getUserData().customer.wallet.cards)
			if(card.kind.equals(type)) {
				creditCardSelected = card;//to be used in next methods (in tap method)
				break;
			}
		customerData.customer.selectCard(type);
		disableScanningAndBagging();
		payWindow = new Payment(this); //Boot up the payment window
	}

	/**
	 * Starts the pay by credit process from the main window.
	 * @param data The card type passed from the main window as String.
	 * @author simrat_benipal
	 */
	public void payByDebitStart(String data) {
		if(amountToBePayed <= 0 ) {
			mainWindow.setMsg("Please Scan at least one item");
			return;
		}
		if(data == null) {
			mainWindow.setMsg("Please select a valid Card");
			return; // display error that cant make payment on no money
		}

		String[] arrOfStr = data.split(",");
		String type = arrOfStr[1].substring(1);//remove the leading zero
		
		for(Card card : this.getUserData().customer.wallet.cards)
			if(card.kind.equals(type)) {
				debitCardSelected = card;//to be used in next methods (in tap method)
				break;
			}
		//System.out.println(debitCardSelected.isTapEnabled);
		customerData.customer.selectCard(type);
		disableScanningAndBagging();
		payWindowDebit = new PaymentDebit(this); //Boot up the payment window
	}
	
	/**
	 * Finalizes the pay by credit sequence.
	 * @param pin The pin from customer input
	 */
	public void payByCredit(String pin) {
		try { //Try and Catch here because a bunch of exceptions can be thrown before hitting the CardReaderListener
			customerData.customer.insertCard(pin.intern());
		} catch(BlockedCardException e) {
			payWindow.setMessage("Payment Unsuccessful: Blocked Card, Please Try Again.");
			return;
		} catch(ChipFailureException e) {
			payWindow.setMessage("Payment Unsuccessful: Random Chip Failure, Please Try Again.");
			return;
		} catch(InvalidPINException e) {
			payWindow.setMessage("Payment Unsuccessful: Invalid, Please Try Again.");
			return;
		} catch(IllegalStateException | IOException e) {
			payWindow.setMessage(e.getMessage());
			return;
		} finally {
			station.cardReader.remove();
		}
		disablePayOnGui(); //WE GET HERE, THE PAYMENT WAS PROCESSED
	}
	
	/**
	 * Finalizes the pay by Debit sequence.
	 * @param pin The pin from customer input.
	 */
	public void payByDebit(String pin) {
		try { //Try and Catch here because a bunch of exceptions can be thrown before hitting the CardReaderListener
			customerData.customer.insertCard(pin.intern());
		} catch(BlockedCardException e) {
			payWindowDebit.setMessage("Payment Unsuccessful: Blocked Card, Please Try Again.");
			return;
		} catch(ChipFailureException e) {
			payWindowDebit.setMessage("Payment Unsuccessful: Random Chip Failure, Please Try Again.");
			return;
		} catch(InvalidPINException e) {
			payWindowDebit.setMessage("Payment Unsuccessful: Invalid, Please Try Again.");
			return;
		} catch(IllegalStateException | IOException e) {
			payWindowDebit.setMessage(e.getMessage());
			return;
		} finally {
			station.cardReader.remove();
		}
		disablePayOnGui(); //WE GET HERE, THE PAYMENT WAS PROCESSED
	}
	
	/**
	 * Finalizes the pay by Debit sequence using TAP.
	 * @author simrat_benipal
	 */
	public void payByDebitTap() {
		if(!debitCardSelected.isTapEnabled) { //check if the card has tap enabled or not
			payWindowDebit.setMessage("Payment Unsuccessful: Tap function not available.");
			return;
		} else { //Try and Catch here because a bunch of exceptions can be thrown before hitting the CardReaderListener
			try {
				station.cardReader.tap(debitCardSelected);
			} catch(BlockedCardException e) {
				payWindowDebit.setMessage("Payment Unsuccessful: Blocked Card, Please Try Again.");
				return;
			} catch(ChipFailureException e) {
				payWindowDebit.setMessage("Payment Unsuccessful: Random Tap Failure, Please Try Again.");
				return;
			} catch(IllegalStateException | IOException e) {
				payWindowDebit.setMessage(e.getMessage());
				return;
			}
		}
		disablePayOnGui(); //WE GET HERE, THE PAYMENT WAS PROCESSED
	}
	
	/**
	 * Finalizes the pay by Debit sequence using swipe.
	 * @author simrat_benipal
	 */
	public void payByDebitSwipe() {
		try { //Try and Catch here because a bunch of exceptions can be thrown before hitting the CardReaderListener
			station.cardReader.swipe(debitCardSelected);
		} catch(BlockedCardException e) {
			payWindowDebit.setMessage("Payment Unsuccessful: Blocked Card, Please Try Again.");
			return;
		} catch(ChipFailureException e) {
			payWindowDebit.setMessage("Payment Unsuccessful: Random Swipe Failure, Please Try Again.");
			return;
		} catch(IllegalStateException | IOException e) {
			payWindowDebit.setMessage(e.getMessage());
			return;
		}
		disablePayOnGui(); //WE GET HERE, THE PAYMENT WAS PROCESSED
	}

	/**
	 * Finalizes the pay by Credit sequence using TAP.
	 * @author simrat_benipal
	 */
	public void payByCreditTap() {
		if(!creditCardSelected.isTapEnabled) { //check if the card has tap enabled or not
			payWindowDebit.setMessage("Payment Unsuccessful: Tap function not available.");
			return;
		} else {
			try { //Try and Catch here because a bunch of exceptions can be thrown before hitting the CardReaderListener
				station.cardReader.tap(creditCardSelected);
			} catch(BlockedCardException e) {
				payWindowDebit.setMessage("Payment Unsuccessful: Blocked Card, Please Try Again.");
				return;
			} catch(ChipFailureException e) {
				payWindowDebit.setMessage("Payment Unsuccessful: Random Tap Failure, Please Try Again.");
				return;
			} catch(IllegalStateException | IOException e) {
				payWindowDebit.setMessage(e.getMessage());
				return;
			}
		}
		disablePayOnGui(); //WE GET HERE, THE PAYMENT WAS PROCESSED
	}

	/**
	 * Finalizes the pay by Credit sequence using swipe.
	 * @author simrat_benipal
	 */
	public void payByCreditSwipe() {
		try { //Try and Catch here because a bunch of exceptions can be thrown before hitting the CardReaderListener
			station.cardReader.swipe(creditCardSelected);
		} catch(BlockedCardException e) {
			payWindowDebit.setMessage("Payment Unsuccessful: Blocked Card, Please Try Again.");
			return;
		} catch(ChipFailureException e) {
			payWindowDebit.setMessage("Payment Unsuccessful: Random Swipe Failure, Please Try Again.");
			return;
		} catch(IllegalStateException | IOException e) {
			payWindowDebit.setMessage(e.getMessage());
			return;
		}
		disablePayOnGui(); //WE GET HERE, THE PAYMENT WAS PROCESSED
	}
	

	public void disablePayOnGui() {
		if(payWindow != null)
			payWindow.disablePaying();
		else if (payWindowDebit != null)
			payWindowDebit.disablePaying();
			
	}

	/**
	 * Sends a message to the pay window to show to the customer.
	 * @param msg The message to be displayed.
	 */
	public void payWindowMessage(String msg) {
		if(payWindow != null)
			payWindow.setMessage(msg);
		else if (payWindowDebit != null)
			payWindowDebit.setMessage(msg);
	}

	public void updateExpectedWeight(double newItemWeight) {
		baggingAreaExpectedWeight += newItemWeight;
	}

	public void setScanStatus(boolean status) {
		this.wasSuccessScan = status;
	}

	public void bagItemSuccess(boolean status) {
		this.bagItemSuccess = status;
	}
	
	/**
	 * retrieve the current customer using the station
	 * @return
	 */
	public CustomerData getUserData() {
		return this.customerData;
	}
	
	/**
	 * get the price of the current receipt
	 * @return
	 */
	public double getReceiptPrice() {
		return amountToBePayed;
	}

	/**
	 * Adds the specified amount to the current receipt total.
	 * @param price The amount to be added to the price.
	 */
	public void changeReceiptPrice(double price) {
		amountToBePayed += price;
		setPriceOnGui();
	}
	

	/**
	 * Resets the receipt price (total) to 0.
	 */
	public void resetReceiptPrice() {
		amountToBePayed = 0.0;
	}
	
	public void setPriceOnGui() {
		mainWindow.setAmountToBePayedLabel(amountToBePayed);
	}

	public void reEnableScanner() {
		station.scanner.enable();
	}

	public void updateGUIItemList(String desc, double weight, double price) {
		mainWindow.addProductDetails(desc, price, weight);
	}
	
	public void updateWeightOnGUI(double weight) {
		mainWindow.updateWeightLabel(weight);
	}
	
	public void updatePayStatusGUI() {
		if(payWindow != null)
			payWindow.updatePayStatus(this.wasPaymentPosted);
		else if (payWindowDebit != null)
			payWindowDebit.updatePayStatus(this.wasPaymentPosted);
	}

	public void systemDisable() {
		//station.baggingArea.disable();
		baggingArea.disable();
		station.cardReader.disable();
		station.scanner.disable();
		//station.touchScreen.disable();
		touchScreen.disable();

	}

	public void systemEnable() {
		baggingArea.enable();
		//station.baggingArea.enable();
		station.cardReader.enable();
		station.scanner.enable();
		//station.touchScreen.enable();
		touchScreen.enable();
	}

	public void disableScanning() {
		mainWindow.disableScanning();
		mainWindow.disablePaying();
		mainWindow.enableBagging();
		mainWindow.setMsg("Bag Your Item:");
	}

	public void reEnableScanning() {
		systemEnable();
		sendMsgToGui("Scan Next Item:");
		mainWindow.enablePaying();
		mainWindow.enableScanning();
		mainWindow.disableBagging();
	}

	public void disableScanningAndBagging() {
		mainWindow.disableBagging();
		mainWindow.disableScanning();
		mainWindow.disablePaying();
	}

	public void enableScanningAndBagging() {
		mainWindow.enableScanning();
		mainWindow.enablePaying();
	}

	public boolean getWasPaymentPosted() {
		return wasPaymentPosted;
	}

	public void setWasPaymentPosted(boolean state) {
		this.wasPaymentPosted = state;
	}

	private void sendMsgToGui(String string) {
		// TODO Auto-generated method stub
		mainWindow.setMsg(string);
	}

	public double getCurrentExpectedWeight() {
		return baggingAreaExpectedWeight;
	}


}