package com.diy.software.system;


import com.diy.hardware.DoItYourselfStationAR;
import java.io.IOException;
import java.util.NoSuchElementException;
import com.diy.hardware.*;
import com.diy.hardware.external.ProductDatabases;
import com.jimmyselectronics.EmptyException;
import java.lang.Object;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;
import java.util.NoSuchElementException;
import com.diy.hardware.*;
import com.diy.hardware.external.ProductDatabases;

import com.jimmyselectronics.Item;
import com.jimmyselectronics.OverloadException;
import com.jimmyselectronics.disenchantment.TouchScreen;
import com.jimmyselectronics.disenchantment.TouchScreenListener;
import com.jimmyselectronics.necchi.Barcode;
import com.jimmyselectronics.necchi.BarcodedItem;
import com.jimmyselectronics.opeechee.BlockedCardException;
import com.jimmyselectronics.opeechee.Card;
import com.jimmyselectronics.opeechee.ChipFailureException;
import com.jimmyselectronics.opeechee.InvalidPINException;
import com.jimmyselectronics.virgilio.ElectronicScale;

import com.unitedbankingservices.DisabledException;
import com.unitedbankingservices.OutOfCashException;
import com.unitedbankingservices.Sink;
import com.unitedbankingservices.TooMuchCashException;
import com.unitedbankingservices.banknote.Banknote;
import com.unitedbankingservices.banknote.BanknoteDispenserAR;
import com.unitedbankingservices.banknote.BanknoteValidator;
import com.unitedbankingservices.coin.Coin;
import com.unitedbankingservices.coin.CoinDispenserAR;
import com.unitedbankingservices.coin.CoinStorageUnit;
import com.unitedbankingservices.coin.CoinValidator;

import ca.powerutility.NoPowerException;

import ca.powerutility.PowerGrid;

import ca.powerutility.PowerSurge;
import ca.ucalgary.seng300.simulation.InvalidArgumentSimulationException;
import ca.ucalgary.seng300.simulation.NullPointerSimulationException;

import com.jimmyselectronics.abagnale.ReceiptPrinterD;
import com.jimmyselectronics.abagnale.ReceiptPrinterListener;



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
  
	private CoinValidatorObs coinValidatorObs;
	private BanknoteValidatorObs banknoteValidatorObs;
	private BanknoteStorageUnitObs banknoteStorageObs;
	private BanknoteSlotROutputObs banknoteSlotROutputObs;
	//private AddBags bagWindow;
	
	//Cusomter IO Windows
	private AddBags bagWindow;
	private Payment payWindow;
	private PaymentDebit payWindowDebit;
	
	private Membership membershipWindow;
	
	private PaymentCash payWindowCash;
	private DiyInterface mainWindow;
	
	//Hold an instance of the customer
	private CustomerData customerData;
	
	//System Variables
	private double amountToBePayed; //TOTAL AMOUNT OWED BY CUSTOMER, INCREMENTED ON SUCCESSFULL ITEM SCAN VIA BARCODESCANNEROBSERVER

	private double baggingAreaCurrentWeight;
	private double baggingAreaExpectedWeight;
	private boolean wasSuccessScan = false;
	private boolean bagItemSuccess = false;
	private boolean wasPaymentPosted = false;
	private boolean requestAttendant = true;
	private boolean systemEnabled = true;


	private TouchScreen touchScreen;
	private ElectronicScale baggingArea;
	private BagDispenser bagDispenser;
	private static double scaleMaximumWeightConfiguration = 5000.0;
	private static double scaleSensitivityConfiguration = 0.5;
	
	private double amountToPay;
	
	private final Currency currency = Currency.getInstance(Locale.CANADA);
	
	public static final long[] acceptedCoinDenominations = {200l, 100l, 25l, 10l, 5l}; //HARDCODE ACCEPTED COINS IN DECREASING ORDER
	public static final int[] acceptedNoteDenominations = {100, 50, 20, 10, 5}; //HARDCODE ACCEPTED NOTES IN DECREASING ORDER
	
	private double changeDue = 0; //amount we owe customer
	private double changeReturned = 0;
	private double notesReturned = 0; //Tracks total returned using notes, solely for collectChange to return properly
	
	private Card debitCardSelected = null;
	private Card creditCardSelected = null;


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
		
		DoItYourselfStationAR.configureCoinDenominations(acceptedCoinDenominations);
		DoItYourselfStationAR.configureBanknoteDenominations(acceptedNoteDenominations);
		
		station = new DoItYourselfStationAR();

		station.plugIn();

		station.turnOn();
		
		//Initialize a bag dispenser with 50 bags.
		bagDispenser = new BagDispenser(50);
		
		touchScreen = new TouchScreen();
		baggingArea = new ElectronicScale(scaleMaximumWeightConfiguration, scaleSensitivityConfiguration);
		baggingArea.plugIn();
		//baggingArea.turnOn();
		
		station.plugIn();
		baggingArea.plugIn();
		touchScreen.plugIn();

		//touchScreen.turnOn();
		//These turnOn can result in power failure
		boolean goodPower = false;
		while (!goodPower)
		{
			try
			{
				station.turnOn();
				baggingArea.turnOn();
				touchScreen.turnOn();
				goodPower = true;
			}
			catch (Exception e)
			{
				goodPower = false;
			}
		}
		
	
		
		try {
			station.printer.addPaper(100);
			station.printer.addInk(10000);
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
		scaleObs = new ElectronicScaleObserver(this, this.attendant);
		touchObs = new TouchScreenObserver();
		printerObs = new ReceiptPrinterObserver(this);
		
		coinValidatorObs = new CoinValidatorObs(this);
		banknoteValidatorObs = new BanknoteValidatorObs(this);
		banknoteStorageObs =  new BanknoteStorageUnitObs(this);
		banknoteSlotROutputObs = new BanknoteSlotROutputObs(this);
		
		
		
		//Register the observers on the DIY Station
		station.cardReader.register(cardReaderObs);
		//station.baggingArea.register(scaleObs);
		baggingArea.register(scaleObs);
		station.scanner.register(scannerObs);
		//station.touchScreen.register(touchObs);
		touchScreen.register(touchObs);
		station.printer.register(printerObs);

		//Attach Listeners to cash sinks
		station.banknoteValidator.attach(banknoteValidatorObs);
		station.banknoteStorage.attach(banknoteStorageObs);
		station.banknoteOutput.attach(banknoteSlotROutputObs);
		attachToCoinDispensers();
		attachToNoteDispensers();
		
		//Setup cash payments
//		SetupCoinValidator();
		simulateLoadAllCoinDispensers(15);
		simulateLoadAllNoteDispensers(15);
		
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
		//mainWindow.disableBagging();
		mainWindow.disablePaying();
		mainWindow.disableScanning();
		mainWindow.disableMembership();
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
		//mainWindow.enableBagging();
		mainWindow.enablePaying();
		mainWindow.enableScanning();
		mainWindow.enableMembership();
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
		mainWindow.disableMembership();
		mainWindow.disableAddBagging();
		mainWindow.setMsg("Bag Your Item:");
	}
	
	public void reEnableScanning() {
		systemEnable();
		sendMsgToGui("Scan Next Item:");
		mainWindow.enablePaying();
		mainWindow.enableScanning();
		mainWindow.disableBagging();
		mainWindow.enableMembership();
		mainWindow.enableAddBagging();
	}
	
	public void disableScanningAndBagging() {
		mainWindow.disableBagging();
		mainWindow.disableScanning();
		mainWindow.disablePaying();
		mainWindow.disableMembership();
		mainWindow.disableAddBagging();
	}
	
	public void enableScanningAndBagging() {
		mainWindow.enableScanning();
		mainWindow.enablePaying();
		mainWindow.enableMembership();
		mainWindow.enableAddBagging();
	}
	
	public boolean getWasPaymentPosted() {
		return wasPaymentPosted;
	}
	
	public void setwasPaymentPosted(boolean state) {
		this.wasPaymentPosted = state;
	}
	
	public void sendMsgToGui(String string) {
		// TODO Auto-generated method stub
		mainWindow.setMsg(string);
	}

	public double getCurrentExpectedWeight() {
		return baggingAreaExpectedWeight;
	}
	public double getCurrentWeight() throws OverloadException {
		baggingAreaCurrentWeight = baggingArea.getCurrentWeight();
		return baggingAreaCurrentWeight;
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
			mainWindow.setMsg("Please Scan at least one item");
			return; //TODO: display error that cant make payment on no money
		} 
		if(type == null)
		{
			mainWindow.setMsg("Please select a valid Card");
			return; // display error that cant make payment on no money
		}
			
		
		String[] arrOfStr = type.split(",");
		type = arrOfStr[1].substring(1);//removing the leading space
		//else we start the selection process
		for(Card card : this.getUserData().customer.wallet.cards)
			if(card.kind.equals(type)) 
			{
				creditCardSelected = card;
				//to be used in next methods
				//in tap method
				break;
			}
		
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
			mainWindow.setMsg("Please Scan at least one item");
			
			return; //TODO: display error that cant make payment on no money
		}
		//just an error check
		if(type == null)
		{
			mainWindow.setMsg("Please select a valid Card");
			return; // display error that cant make payment on no money
		}
		String[] arrOfStr = type.split(",");
		type = arrOfStr[1].substring(1);//remove the leading zero
		
		for(Card card : this.getUserData().customer.wallet.cards)
			if(card.kind.equals(type)) 
			{
				debitCardSelected = card;
				//to be used in next methods
				//in tap method
				break;
			}
		System.out.println(debitCardSelected.isTapEnabled);
		//else we start the selection process
		//System.out.println("card selected = "+ type);
		//Type contains "John Interac, Interac"
		//split the string based on ',' and get the second 
		//https://www.geeksforgeeks.org/split-string-java-examples/
		//System.out.println("card selected = "+ arrOfStr[1].replace(" ", ""));
		
		//System.out.println(type);
		customerData.customer.selectCard(type);
		//Boot up the pin window
		disableScanningAndBagging();
		payWindowDebit = new PaymentDebit(this);
	}

	/**
	 * @author Jesse Dirks, Jason Osmond
	 * Start the pay by cash process from the main window
	 */
	public void payByCashStart() {
		if(amountToBePayed <= 0 ) {
			mainWindow.setMsg("Please Scan at least one item");
			return; //TODO: display error that cant make payment on no money
		}
		//Customer class does not contain any cash.
		disableScanningAndBagging();
		payWindowCash = new PaymentCash(this);
	}
	
	/**
	 * @author Saja Abufarha
	 * Start the Enter Membership Number process from the main window
	 */
	public void enterMembershipStart() {		
		//Boot up the membership window
		disableScanningAndBagging();
		membershipWindow = new Membership(this);
		//creates the membership gui
	}
	
	/**
	 * @author Saja Abufarha
	 * Gets error message to signals the Customer I/O regarding the weight discrepancy. 
	 * Will be called by Roze's code
	 * 
	 * Updates the message on the GUI
	 * @param message
	 */
	public void notifyWeightDiscrepancy(String message) {
		mainWindow.updateWeightDiscrepancyLabel(message);

	}
	
	/**
	 * @author brandonn38
	 * Start the process of adding a bag
	 */
	public void addBag() {
		disableScanningAndBagging();
		bagWindow = new AddBags(this, attendant);
	}
	
	
	/**
	 * Gets the data of the bag dispenser
	 * @return The bagDispenser data
	 */
	public BagDispenser getBagDispenserData() {
		return bagDispenser;
	}
	
	/**
	 * Gets a reference of the bagging area
	 * @return The reference of the bagging area
	 */
	public ElectronicScale getBaggingAreaRef() {
		return baggingArea;
	}
	
	/**
	 * Gets a reference of the bag window
	 * @return The reference of the bagging window
	 */
	public AddBags getAddBagsRef() {
		return bagWindow;
	}
	
	/**
	 * Finalizes the pay by credit sequence
	 * @param pin, the pin from customer input
	 */
	public void payByCredit(String pin, double amountToPay) {
		this.amountToPay = amountToPay;
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
			//Data read on card reader observer
			station.cardReader.remove();
		}

		printReceipt();
		
		//WE GET HERE, THE PAYMENT WAS PROCESSED
		disablePayOnGui();
	}
	
	/**
	 * Finalizes the pay by Debit sequenece
	 * @param pin, the pin from customer input
	 */
	public void payByDebit(String pin, double amountToPay) 
	{
		//for partial payments
		this.amountToPay = amountToPay;
		//normal insertion of card
		
		
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
	 * Rose
	
	public void weightDiscrepancy(ElectronicScale baggingArea, double baggingAreaCurrentWeight) throws OverloadException {
		if (!assertEquals(baggingAreaExpectedWeight, baggingAreaCurrentWeight, 0.01)){
			systemDisable();
			requestAttendant = true;
		}
		else
			systemEnable();
	}
	*/
  
	
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
	
	public void enterMembership(String membershipNumber) {
		// MembershipWindow.memberNumber.getText() == membershipNumber;
		
		//membershipWindow = new Membership(this);
		// Check if inputted membership number is in the MembershipDatabase
		//System.out.println("+"+membershipNumber+"+");
		//Built In method for Hash Maps
		if (MemberDatabase.MEMBER_DATABASE.containsKey(Integer.parseInt(membershipNumber)))
		{	
			//membershipWindow.setMessage("Success!");
			membershipWindow.setMessage("Welcome " + MemberDatabase.MEMBER_DATABASE.get(Integer.parseInt(membershipNumber))+"!");
		}
		else
			membershipWindow.setMessage("Error! Try again.");
			requestAttendant = true;
	}
	
  
	/**
	 * Finalizes the pay by Debit sequenece (using TAP)
	 * @author simrat_benipal
	 * @param nothing, we can tap without PIN
	 */
	
	public void payByDebitTap(double amountToPay) 
	{
		//for partial payments
		this.amountToPay = amountToPay;
		//check if the card has tap enabled or not
		if(!debitCardSelected.isTapEnabled)
		{
			payWindowDebit.setMessage("This card cannot tap!");
			return;
		}
		else
		{
		//Try and Catch here because a bunch of exceptions can be thrown before hitting the CardReaderListener
		
		try {
			//a card has been selected because of payByDebitStart() method
			//so just tap the selected card
			station.cardReader.tap(debitCardSelected); //method in CardReader.Java
			//every station has a CardReader Object
			//This Card Reader should be powered-up and running
			//this call will notify that the card is tapped in the CardReaderObserver
			
			//if thiis method returned success (based on probabilities of TapFailure
			//then we execute 'notifyCardDataRead(data), method in listener 
			
			//and this return CardData Object
		} catch(BlockedCardException e) 
		{
			payWindowDebit.setMessage("The card has been blocked!");
			return;
		} catch(IllegalStateException e) 
		{
			payWindowDebit.setMessage(e.getMessage());
			return;
		} catch(ChipFailureException e) 
		{
			payWindowDebit.setMessage("Random Tap Failure! Try Again!!");
			return;
		} catch(IOException e) 
		{
			payWindowDebit.setMessage(e.getMessage());
			return;
		}
		}
		printReceipt();
		//WE GET HERE, THE PAYMENT WAS PROCESSED
		disablePayOnGui();
	}
	
	/**
	 * Inserts a coin into the coin slot
	 * @author Jesse Dirks, Jason Osmond
	 */
	public void InsertCoin(Coin coin) { //TODO
		
		try {
			station.coinSlot.receive(coin);
		} catch(DisabledException e) {
			payWindowCash.setMessage("The coin slot is currently disabled");
		} catch(TooMuchCashException e) {
			payWindowCash.setMessage("The machine is full of coins");
		}/* catch(Throwable e) {
			if (e instanceof PowerSurge) {
				payWindowCash.setMessage("The system has encountered an unexpected Power surge");
			}
			else {
				payWindowCash.setMessage("An unexpected error has occurred");
			}
		}*/
		
	}
	
	/**
	 * Inserts a banknote into the banknote slot
	 * @author Jesse Dirks, Jason Osmond
	 */
	public boolean InsertBanknote(Banknote banknote) { //TODO
		
		try {
			station.banknoteInput.receive(banknote);
		} catch(DisabledException e) {
			payWindowCash.setMessage("The banknote slot is currently disabled");
		} catch(TooMuchCashException e) {
			payWindowCash.setMessage(e.getMessage());
		}/* catch(Throwable e) {
			if (e instanceof PowerSurge) {
				payWindowCash.setMessage("The system has encountered an unexpected Power surge");
			}
			else {
				payWindowCash.setMessage("An unexpected error has occurred");
			}
		}*/
		if (station.banknoteInput.hasDanglingBanknote()) {
			payWindowCash.setMessage("Your banknote has been rejected and is dangling from the slot.");
			return false;
		}
		return true;
	}
	
	private int lastValidNoteValue;
	
	public int getLastValidNoteValue() {
		return lastValidNoteValue;}
	
	public void updateLastValidNoteValue(long value){
		lastValidNoteValue = (int) value;}
	
	
	/**
	 * Called when a denomination storage unit receives a coin
	 * @author Jason Osmond
	 * @param cashAmount
	 */
	public void ValidCashReceived(double cashAmount) {
		System.out.println("Valid Cash Received = " + cashAmount); 
		payWindowCash.cashReceived(cashAmount); //Update UI of cash Inserted
		
	}
	
	/**
	 * Called when customer presses confirm in Pay with cash screen
	 * Finalizes Pay with cash sequence
	 * Only affects GUI, actual transaction processed when cash is inserted
	 * @author Jason Osmond
	 */
	public void payByCash(double amount) {
		if (amount <= 0) {
			payWindowCash.setMessage("Please Insert Cash Before Confirming");
			return;
		}		
		updateGUIItemListPayment(roundToHundredth(amount));
	}
	

	
	
	/**
	 * Adds Listeners to all the coin dispensers
	 * @author Jason Osmond
	 */
	private void attachToCoinDispensers() {
		for (long denomination : station.coinDenominations) {
			CoinDispenserAR coinDispenser = station.coinDispensers.get(denomination);
			coinDispenser.attach(new CoinDispenserObs(this, denomination));
		}
	}
	
	/**
	 * Adds Listeners to all the bank note dispensers
	 * @author Jason Osmond
	 */
	private void attachToNoteDispensers() {
		for (int denomination : station.banknoteDenominations) {
			BanknoteDispenserAR banknoteDispenser = station.banknoteDispensers.get(denomination);
			banknoteDispenser.attach(new BanknoteDispenserObs(this, denomination));
		}
	}
	
	/**
	 * Probably call this with Attendant Station
	 * @author Jason Osmond
	 * @param amountToLoad
	 * 		Number of coins to simulate loading
	 */
	public void simulateLoadAllCoinDispensers(long amountToLoad) {
		for (long denomination: station.coinDenominations) {
			//Get dispenser
			CoinDispenserAR changeDispenser = 
					station.coinDispensers.get(denomination);
			if (changeDispenser == null)
				continue;
			
			//Simulate coins
			Coin[] coins = new Coin[(int) amountToLoad];
			
			for (int i = 0; i < coins.length; i++)
				coins[i] = new Coin(currency, denomination);
			
			//Try to load into dispenser
			try {
				changeDispenser.load(coins);
			}catch (TooMuchCashException e){
				System.out.println(e);
				continue;
			}catch (NoPowerException e) {
				System.out.println(e);
				continue;
			}			
		}
	}
	
	/**
	 * Probably call this with Attendant Station
	 * @author Jason Osmond
	 * @param amountToLoad
	 * 		Number of notes to simulate loading for each denomination
	 */
	public void simulateLoadAllNoteDispensers(int amountToLoad) {
		for (int denomination: station.banknoteDenominations) {
			//Get dispenser
			BanknoteDispenserAR noteDispenser = 
					station.banknoteDispensers.get(denomination);
			if (noteDispenser == null)
				continue;
			
			//Simulate coins
			Banknote[] banknotes = new Banknote[amountToLoad];
			
			for (int i = 0; i < banknotes.length; i++)
				banknotes[i] = new Banknote(currency, denomination);
			
			//Try to load into dispenser
			try {
				noteDispenser.load(banknotes);
			}catch (TooMuchCashException e){
				System.out.println(e);
				continue;
			}catch (NoPowerException e) {
				System.out.println(e);
				continue;
			}			
		}
	}
	
	
	/**
	 * Dispenses all change into the coin tray
	 * Change returned favors larger denominations
	 * @author Jason Osmond
	 */
	public void dispenseChangeDue() {
		//Looks at each denomination and if denom is larger than change due
		//BEST WHEN: Denominations are be sorted in DECREASING order
		for(int denomination : acceptedNoteDenominations) {
			while (changeDue + 0.02 >= denomination){//Round to the nearest nickel
				try {
					BanknoteDispenserAR dispenser = station.banknoteDispensers.get(denomination);
					dispenser.emit();
				}catch (OutOfCashException e) { //if cannot emit, try another denom
					break; //move to next denomination
				} catch (TooMuchCashException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					break;
				} catch (DisabledException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					break;
				}
				//TODO Dispensing in this case does not 100% guarantee the customer has received the cash
				
			}
		}
		//dispense coins
		for(long denomination : acceptedCoinDenominations) {
			double dollarVal = DIYSystem.convertCentsToDollars(denomination);
			while (changeDue + 0.02 >= dollarVal){//Round to the nearest nickel
				try {
					CoinDispenserAR dispenser = station.coinDispensers.get(denomination);
					dispenser.emit();
//					CollectBanknoteFromOutput();
				}catch (OutOfCashException e) {
//					e.printStackTrace();
					break; //move to next denomination
				} catch (TooMuchCashException e) {
//					 TODO Auto-generated catch block
//					e.printStackTrace();
					break;
				} catch (DisabledException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					break;
				}
			}
		}
		if(changeDue > 0.02) { //Attendant is alerted if too few change is given
			attendant.sendAlert("Insufficient Change Returned! Customer is owed $" + changeDue);
			systemDisable();
		}
		else if(changeDue < -.02) {
			System.out.println("Too Much Change Returned: " + changeDue);
		}
		
		resetChangeDue();
		//Update GUI after dispensing
		changeReceiptPrice(changeReturned);
		updateGUIItemListCollectCash(changeReturned);
			
	}
	
	public Banknote CollectBanknoteFromOutput() {
		try {
			Banknote note = station.banknoteOutput.removeDanglingBanknote();
			System.out.println("Collected a $"+ note.getValue() + " note");
			return note;			
		}catch (NullPointerSimulationException e) {
			return null;
		}
	}
	
	public Banknote CollectBanknoteFromInput() {
		try {
			return station.banknoteOutput.removeDanglingBanknote();			
		}catch (NullPointerSimulationException e) {
			return null;
		}
	}

	/**
	 * Simulates Collecting Change 
	 * @author Jason Osmond
	 */
	public double collectChange() {
		ArrayList<Coin> coinsCollected = (ArrayList<Coin>) station.coinTray.collectCoins();
		double totalChangeCollected = 0;
		for (Coin coin : coinsCollected) {
			double dollarValue = convertCentsToDollars(coin.getValue());
			System.out.println("Collected a $"+ dollarValue + " coin");
			totalChangeCollected += (double) dollarValue;
		}
		System.out.println("Coins Returned = $" + totalChangeCollected);
		System.out.println("Notes Returned = $" + notesReturned);
		
		totalChangeCollected += notesReturned;
		
		payWindowCash.changeCollected();
		resetChangeReturned();		
		
		return roundToHundredth(totalChangeCollected);
	}	
	
	

	/* Finalizes the pay by Debit sequenece (using TAP)
	 * @author simrat_benipal
	 * @param nothing, we can tap without PIN
	 */
	
	public void payByDebitSwipe(double amountToPay) 
	{
		//for partial payments
		this.amountToPay = amountToPay;
		
		
		//Try and Catch here because a bunch of exceptions can be thrown before hitting the CardReaderListener
		try {
			//a card has been selected because of payByDebitStart() method
			//so just swipe the selected card
			station.cardReader.swipe(debitCardSelected);
			//method in CardReader.Java
			//every station has a CardReader Object
			//This Card Reader should be powered-up and running
			//this call will notify that the card is swipped in the CardReaderObserver
			
			//if this method returned success (based on probabilities of SWIPE Failure
			//then we execute 'notifyCardDataRead(data), method in listener (already implemented in our listener/obs) 
			
			//and this return CardData Object
		} catch(BlockedCardException e) 
		{
			payWindowDebit.setMessage("The card has been blocked!");
			return;
		} catch(IllegalStateException e) 
		{
			payWindowDebit.setMessage(e.getMessage());
			return;
		} catch(ChipFailureException e) 
		{
			payWindowDebit.setMessage("Random Swipe Failure! Try Again!!");
			return;
		} catch(IOException e) 
		{
			payWindowDebit.setMessage(e.getMessage());
			return;
		}
		printReceipt();
		//WE GET HERE, THE PAYMENT WAS PROCESSED
		disablePayOnGui();
	}
	
	
	/**Tap and Swipe for CREDIT CARDS
	 * @author simrat_benipal 
	 * Iteration 2.0 
	 */
	public void payByCreditTap(double amountToPay) 
	{
		//for partial payments
		this.amountToPay = amountToPay;
		//check if the card has tap enabled or not
		if(!creditCardSelected.isTapEnabled)
		{
			payWindow.setMessage("This card cannot tap!");
			return;
		}
		else
		{
		
		//Try and Catch here because a bunch of exceptions can be thrown before hitting the CardReaderListener
		try {
			//a card has been selected because of payByCreditStart() method
			//so just tap the selected card
			station.cardReader.tap(creditCardSelected); //method in CardReader.Java
			//every station has a CardReader Object
			//This Card Reader should be powered-up and running
			//this call will notify that the card is tapped in the CardReaderObserver
			
			//if this method returned success (based on probabilities of TapFailure
			//then we execute 'notifyCardDataRead(data), method in listener 
			
			//and this return CardData Object
		} catch(BlockedCardException e) 
		{
			payWindow.setMessage("The card has been blocked!");
			return;
		} catch(IllegalStateException e) 
		{
			payWindow.setMessage(e.getMessage());
			return;
		} catch(ChipFailureException e) 
		{
			payWindow.setMessage("Random Tap Failure! Try Again!!");
			return;
		} catch(IOException e) 
		{
			payWindow.setMessage(e.getMessage());
			return;
		}
		}
		printReceipt();
		//WE GET HERE, THE PAYMENT WAS PROCESSED
		disablePayOnGui();
	}
	
	/**
	 * Finalizes the pay by Debit sequenece (using TAP)
	 * @author simrat_benipal
	 * @param nothing, we can tap without PIN
	 */
	public void payByCreditSwipe(double amountToPay) 
	{
		//for partial payments
		this.amountToPay = amountToPay;
		
		
		
		//Try and Catch here because a bunch of exceptions can be thrown before hitting the CardReaderListener
		try {
			//a card has been selected because of payByDebitStart() method
			//so just swipe the selected card
			station.cardReader.swipe(creditCardSelected);
			//method in CardReader.Java
			//every station has a CardReader Object
			//This Card Reader should be powered-up and running
			//this call will notify that the card is swipped in the CardReaderObserver
			
			//if this method returned success (based on probabilities of SWIPE Failure
			//then we execute 'notifyCardDataRead(data), method in listener (already implemented in our listener/obs) 
			
			//and this return CardData Object
		} catch(BlockedCardException e) 
		{
			payWindow.setMessage("The card has been blocked!");
			return;
		} catch(IllegalStateException e) 
		{
			payWindow.setMessage(e.getMessage());
			return;
		} catch(ChipFailureException e) 
		{
			payWindow.setMessage("Random Swipe Failure! Try Again!!");
			return;
		} catch(IOException e) 
		{
			payWindow.setMessage(e.getMessage());
			return;
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
		else if (payWindowCash != null)
			payWindowCash.disablePaying();
			
	}
	
	public void payWindowMessage(String msg) {
		if(payWindow != null)
			payWindow.setMessage(msg);
		else if (payWindowDebit != null)
			payWindowDebit.setMessage(msg);
		else if (payWindowCash != null)
			payWindowCash.setMessage(msg);
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
		return roundToHundredth(amountToBePayed);
	}
	
	/**
	 * update the price of the current receipt
	 * @param price
	 */
	public void changeReceiptPrice(double price) {
		amountToBePayed = roundToHundredth(amountToBePayed + price);
		setPriceOnGui();
	}
	

	/**
	 * reset receipt after payment done?
	 */
	public void resetReceiptPrice() {
		amountToBePayed = 0;
		setPriceOnGui();
	}
	
	/**
	 */
	public void decreaseReceiptPrice(double price) {
		amountToBePayed = roundToHundredth(amountToBePayed - price);
		if(amountToBePayed < 0) {
			changeDue = -(amountToBePayed);
		}
		setPriceOnGui();
	}
	
	public void decreaseChangeDue(double amount) {
		changeDue = roundToHundredth(changeDue - amount);
		changeReturned = roundToHundredth(changeReturned + amount);
	}
	public void decreaseChangeDueUsingNotes(double amount) {
		changeDue = roundToHundredth(changeDue - amount);
		changeReturned = roundToHundredth(changeReturned + amount);
		notesReturned = roundToHundredth(notesReturned + amount);;
	}
	
	private void resetChangeReturned() {
		changeReturned = 0;
		notesReturned = 0;
	}
	private void resetChangeDue() {
		changeDue = 0;
	}
	public double getChangeDue() {
		return roundToHundredth(changeDue);
	}
	
	public double getAmountToPay() {;
		return roundToHundredth(amountToPay);
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
				System.out.println("printer error ");
				
				//notify attendant->via ReceiptPrinterObserver

				//send duplicate receipt to print at attendant station?
				
			//	e.printStackTrace();
				
			} catch (OverloadException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (PowerSurge p)
			{
				PowerGrid.reconnectToMains();
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
	
	public Currency getCurrency() {
		return currency;
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

	public void updateGUIItemList(String desc, double weight, double price) {
		mainWindow.addProductDetails(desc, price, weight);
	}
	
	public void updateGUIItemListPayment(double amountPaid) {
		mainWindow.addPaymentToItems(roundToHundredth(amountPaid));
	}
	
	public void updateGUIItemListCollectCash(double amountPaid) {
		mainWindow.addCollectCashToItems(roundToHundredth(amountPaid));
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
	

	public boolean get_requestAttendant(){
		return requestAttendant;
	}
	
	public void outOfBags() {
		mainWindow.setMsg("Out of bags. Please wait for attendant");
	}
	
	public void bagsRefilled() {
		mainWindow.setMsg("");
	}

	
	public static final double roundToHundredth(double input) {
		return (double) Math.round(input * 100) / 100; //round to nears 100th (i.e. $0.0500001 -> $0.05)
	}
	
	public static final double convertCentsToDollars(long longVal) {
		double doubVal = (double) longVal / 100; // 5c = $0.05
		doubVal = roundToHundredth(doubVal); //round to nears 100th (i.e. $0.0500001 -> $0.05)
		return doubVal; 
	}
}
