package com.diy.software.system;

<<<<<<< HEAD
import com.diy.hardware.DoItYourselfStationAR;
=======
import java.io.IOException;
import java.util.NoSuchElementException;
import com.diy.hardware.*;
import com.diy.hardware.external.ProductDatabases;
import com.jimmyselectronics.EmptyException;
>>>>>>> refs/remotes/origin/print_receipt
import com.jimmyselectronics.OverloadException;
import com.jimmyselectronics.disenchantment.TouchScreen;
import com.jimmyselectronics.opeechee.BlockedCardException;
import com.jimmyselectronics.opeechee.ChipFailureException;
import com.jimmyselectronics.opeechee.InvalidPINException;
import com.jimmyselectronics.virgilio.ElectronicScale;
import com.jimmyselectronics.abagnale.ReceiptPrinterD;
import com.jimmyselectronics.abagnale.ReceiptPrinterListener;

import java.io.IOException;
import java.util.NoSuchElementException;


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
	
	public AttendantStation attendant;
	private DoItYourselfStationAR station;
	
	private CardReaderObserver cardReaderObs;
	private BarcodeScannerObserver scannerObs;
	private ElectronicScaleObserver scaleObs;
	private	TouchScreenObserver touchObs;
	private ReceiptPrinterObserver printerObs;

	
	//Cusomter IO Windows
	private Payment payWindow;
	private PaymentDebit payWindowDebit;
	private DiyInterface mainWindow;
	private AddBags bagWindow;
	
	//Hold an instance of the customer
	private CustomerData customerData;
	
	//System Variables
	private double amountToBePayed; //TOTAL AMOUNT OWED BY CUSTOMER, INCREMENTED ON SUCCESSFULL ITEM SCAN VIA BARCODESCANNEROBSERVER
	private double baggingAreaCurrentWeight;
	private double baggingAreaExpectedWeight;
	private boolean wasSuccessScan = false;
	private boolean bagItemSuccess = false;
	private boolean wasPaymentPosted = false;
<<<<<<< HEAD
	private boolean requestAttendant = true;

=======
	private boolean systemEnabled = true;
>>>>>>> refs/remotes/origin/print_receipt
	
	//added
	private TouchScreen touchScreen;
	private ElectronicScale baggingArea;
	private BagDispenser bagDispenser;
	private static double scaleMaximumWeightConfiguration = 5000.0;
	private static double scaleSensitivityConfiguration = 0.5;
	
	
	public DIYSystem(CustomerData c, AttendantStation a) {
		customerData = c;
		attendant = a;
		initialize();
	}
	
	/*
	 * Setup the system to a default state, with an attending customer and DIY station
	 */
	private void initialize() {
		//Setup the DIY Station
		//station = new DoItYourselfStation();
		station = new DoItYourselfStationAR();
<<<<<<< HEAD
		station.plugIn();
		station.turnOn();
		
		//Initialize a bag dispenser with 50 bags.
		bagDispenser = new BagDispenser(50);
=======
>>>>>>> refs/remotes/origin/print_receipt
		
		touchScreen = new TouchScreen();
		baggingArea = new ElectronicScale(scaleMaximumWeightConfiguration, scaleSensitivityConfiguration);
		
		station.plugIn();
		baggingArea.plugIn();
		touchScreen.plugIn();
		
		station.turnOn();
		baggingArea.turnOn();
		touchScreen.turnOn();
		
		try {
			station.printer.addPaper(100);
			station.printer.addInk(50);
		} catch (OverloadException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//Set default weight for the system to reference
		try {
			//baggingAreaExpectedWeight = station.baggingArea.getCurrentWeight();
			baggingAreaExpectedWeight = baggingArea.getCurrentWeight();
		} catch (OverloadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Setup the required observers
		cardReaderObs = new CardReaderObserver(this);
		scannerObs = new BarcodeScannerObserver(this);
		scaleObs = new ElectronicScaleObserver(this);
		touchObs = new TouchScreenObserver();
		printerObs = new ReceiptPrinterObserver(this);

		
		//Register the observer to the CardReader on the DIY Station
		station.cardReader.register(cardReaderObs);
		//station.baggingArea.register(scaleObs);
		baggingArea.register(scaleObs);
		station.scanner.register(scannerObs);
		//station.touchScreen.register(touchObs);
		touchScreen.register(touchObs);
		station.printer.register(printerObs);
		
		//Setup the Customer and start using the DIY station
		customerData.customer.useStation(station);
		
		//TODO: Launch the GUI for the customer to see with a completed GUI from GUI team
		mainWindow = new DiyInterface(this);
		
		touchScreen.getFrame().getContentPane().add(mainWindow);
		touchScreen.getFrame().pack();
		touchScreen.getFrame().setSize(700, 600);
		touchScreen.getFrame().setLocationRelativeTo(null);
		touchScreen.setVisible(true);
		
		sendMsgToGui("Begin Scanning");
	}
	
	public void systemDisable() {
		
		systemEnabled = false;
		
		//station.baggingArea.disable();
		baggingArea.disable();
		station.cardReader.disable();
		station.scanner.disable();
		//station.touchScreen.disable();
		touchScreen.disable();
		station.printer.disable();
		
		mainWindow.disableAddBagging();
		mainWindow.disableBagging();
		mainWindow.disablePaying();
		mainWindow.disableScanning();
	}
	
	public void systemEnable() {
		
		systemEnabled = true;
		
		baggingArea.enable();
		//station.baggingArea.enable();
		station.cardReader.enable();	
		station.scanner.enable();	
		//station.touchScreen.enable();
		touchScreen.enable();
		station.printer.enable();
		
		mainWindow.enableAddBagging();
		mainWindow.enableBagging();
		mainWindow.enablePaying();
		mainWindow.enableScanning();
	}
	
	public boolean isEnabled() {
		return systemEnabled;
	}
	
	/**
	 * Add Item By Scanning
	 *
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
		
		//ON FAILED SCAN, DO NOT CYLCE TO NEXT ITEM, RESCAN THE SAME ONE
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
	 * Start the bagging proccess for the user
	 */
	public void StartBagging() {
		//THE CUSTOMER BAGS THEIR ITEM (SIMULATED VIA BUTTON ON GUI)
		//customerData.customer.placeItemInBaggingArea();
		customerData.customer.placeItemInBaggingArea();
		bagItemSuccess = true;
		//cheat code
		if(bagItemSuccess) {
			reEnableScanning();
		}
	}

	public void disableScanning() {
		mainWindow.disableScanning();
		mainWindow.disablePaying();
		mainWindow.enableBagging();
		mainWindow.disableAddBagging();
		mainWindow.setMsg("Bag Your Item:");
	}
	
	public void reEnableScanning() {
		systemEnable();
		sendMsgToGui("Scan Next Item:");
		mainWindow.enablePaying();
		mainWindow.enableScanning();
		mainWindow.enableAddBagging();
		mainWindow.disableBagging();
	}
	
	public void disableScanningAndBagging() {
		mainWindow.disableBagging();
		mainWindow.disableScanning();
		mainWindow.disablePaying();
		mainWindow.disableAddBagging();
	}
	
	public void enableScanningAndBagging() {
		mainWindow.enableScanning();
		mainWindow.enablePaying();
		mainWindow.enableAddBagging();
	}
	
	public boolean getWasPaymentPosted() {
		return wasPaymentPosted;
	}
	
	public void setwasPaymentPosted(boolean state) {
		this.wasPaymentPosted = state;
	}
	
	private void sendMsgToGui(String string) {
		// TODO Auto-generated method stub
		mainWindow.setMsg(string);
	}

	public double getCurrentExpectedWeight() {
		return baggingAreaExpectedWeight;
	}
	
	public double updateExpectedWeight(ElectronicScale baggingArea, double itemExpectedWeight) throws OverloadException {
		baggingAreaCurrentWeight = baggingArea.getCurrentWeight();
		baggingAreaExpectedWeight = baggingAreaCurrentWeight + itemExpectedWeight;
		return baggingAreaExpectedWeight;
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
	
	/** COME BACK TO THIS WHEN MULTIPLE PAYMENTS ARE OPTIONALS?
	 * The customer has requested to pay via CREDIT CARD via the GUI interface (when implemented)
	 * @param //pin, the supplied pin from the GUI
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
	
	/****************PAY BY CREDIT CARD BELOW****************************************************
	/**
	 * Start the pay by credit process from the main window
	 * @param type, passed from the maing window
	 */
	public void payByCreditStart(String type) {		
		//Select the card given by type from the main window
		
		if(amountToBePayed <= 0 ) {
			return; //TODO: display error that cant make payment on no money
		} 
		//else we start the selection process
		customerData.customer.selectCard(type);
		//Boot up the pin window
		disableScanningAndBagging();
		payWindow = new Payment(this);
	}
	
	/**
	 * @author simrat_benipal
	 * Start the pay by debit process from the main window
	 */
	public void payByDebitStart(String type) {		
		//Select the card given by type from the main window
		
		if(amountToBePayed <= 0 ) {
			return; //TODO: display error that cant make payment on no money
		} 
		//else we start the selection process
		customerData.customer.selectCard(type);
		//Boot up the pin window
		disableScanningAndBagging();
		payWindowDebit = new PaymentDebit(this);
	}
	
	/**
	 * @author brandonn38
	 * Start the process of adding a bag
	 */
	public void addBag() {
		disableScanningAndBagging();
		bagWindow = new AddBags(this);
	}
	
	public BagDispenser getBagDispenserData() {
		return this.bagDispenser;
	}
	
	public void notifyBagWeightChange(String message) {
		//TODO What kind of item do we add here?
		//baggingArea.add(null);
	}
	
	/**
	 * Finalizes the pay by credit sequenece
	 * @param pin, the pin from customer input
	 */
	public void payByCredit(String pin) {
		
		//Try and Catch here because a bunch of exceptions can be thrown before hitting the CardReaderListener
		try {
			customerData.customer.insertCard(pin.intern());
		} catch(BlockedCardException e) {
			payWindow.setMessage("The card has been blocked!");
			return;
		} catch(IllegalStateException e) {
			payWindow.setMessage(e.getMessage());
			return;
		} catch(ChipFailureException e) {
			payWindow.setMessage("Random Chip Failure! Try Again!!");
			return;
		} catch(InvalidPINException e) {
			payWindow.setMessage("Invalid Pin!");
			return;
		} catch(IOException e) {
			payWindow.setMessage(e.getMessage());
			return;
		} finally {
			station.cardReader.remove();
		}

		printReceipt();
		
		//WE GET HERE, THE PAYMENT WAS PROCESSED
		disablePayOnGui();
	}
	
	/**
	 * Finalizes the pay by credit sequenece
	 * @param pin, the pin from customer input
	 */
	public void payByDebit(String pin) {
		
		//Try and Catch here because a bunch of exceptions can be thrown before hitting the CardReaderListener
		try {
			customerData.customer.insertCard(pin.intern());
		} catch(BlockedCardException e) {
			payWindowDebit.setMessage("The card has been blocked!");
			return;
		} catch(IllegalStateException e) {
			payWindowDebit.setMessage(e.getMessage());
			return;
		} catch(ChipFailureException e) {
			payWindowDebit.setMessage("Random Chip Failure! Try Again!!");
			return;
		} catch(InvalidPINException e) {
			payWindowDebit.setMessage("Invalid Pin!");
			return;
		} catch(IOException e) {
			payWindowDebit.setMessage(e.getMessage());
			return;
		} finally {
			station.cardReader.remove();
		}

		printReceipt();
		
		//WE GET HERE, THE PAYMENT WAS PROCESSED
		disablePayOnGui();
	}
	
	/**
	 * send a message to the pay window for showing to the customer
	 */
	
	public void disablePayOnGui() {
		if(payWindow != null)
			payWindow.disablePaying();
		else if (payWindowDebit != null)
			payWindowDebit.disablePaying();
			
	}
	
	public void payWindowMessage(String msg) {
		if(payWindow != null)
			payWindow.setMessage(msg);
		else if (payWindowDebit != null)
			payWindowDebit.setMessage(msg);
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
	 * update the price of the current receipt
	 * @param price
	 */
	public void changeReceiptPrice(double price) {
		amountToBePayed += price;
		setPriceOnGui();
	}
	

	/**
	 * reset receipt after payment done?
	 */
	public void resetReceiptPrice() {
		amountToBePayed = 0;
	}
	
	
	public void printReceipt() {

		char[] receipt = (mainWindow.getProductDetails()+mainWindow.getTotalAmount()).toCharArray();
		
		for (int c = 0; c<receipt.length-1;c++) {
			try {
				station.printer.print(receipt[c]);
			} catch (EmptyException e) {
				
				//out of paper or ink
				//stop printing- display message to customer
				
				//suspend station
				systemDisable();
				payWindowMessage("printer error- please wait for attendant");
				
				//notify attendant->via ReceiptPrinterObserver

				//send duplicate receipt to print at attendant station?
				
			//	e.printStackTrace();
				
			} catch (OverloadException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		station.printer.cutPaper();
		System.out.println(station.printer.removeReceipt());

	}
	
	//method to allow attendant class to updated based on changes on customer end
	public DiyInterface getMainWindow() {
		return mainWindow;
	}
	
	public ReceiptPrinterD getPrinter() {
		return station.printer;
	}
	
	
	public void setPriceOnGui() {
		mainWindow.setamountToBePayedLabel(amountToBePayed);
	}

	/**
	 * Used to just re enable scanning
	 */
	public void reEnableScanner() {
		//Re enable the scanner
		station.scanner.enable();
	}

	public void updateGUIItemList(String desc, double price, double weight) {
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
	
	public void weightDiscrepancy(ElectronicScale baggingArea, double currentWeight) throws OverloadException {
		//Compare current weight vs previous weight
		double expected_weight = getCurrentExpectedWeight();
		//double current_weight = baggingArea.getCurrentWeight();

		if (expected_weight < currentWeight){
			//Station to disabled scanning
			station.scanner.disable();
			//GUI to disable scanning and bagging
			disableScanningAndBagging();
			//Signal attendant to help
			requestAttendant = true;
		}
		else if (expected_weight == currentWeight){
			station.scanner.enable();
			enableScanningAndBagging();
		}

	}
	public boolean get_requestAttendant(){
		return requestAttendant;
	}

}
