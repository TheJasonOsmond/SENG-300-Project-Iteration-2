package com.diy.software.tests;

import ca.ucalgary.seng300.simulation.SimulationException;
import com.diy.hardware.DoItYourselfStationAR;
import com.diy.software.system.AttendantStation;
import com.diy.software.system.CardReaderObserver;
import com.diy.software.system.CustomerData;
import com.diy.software.system.DIYSystem;
import com.unitedbankingservices.banknote.Banknote;
import com.unitedbankingservices.coin.Coin;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Currency;
import java.util.Locale;


public class PayByCashTests {

	private DIYSystem testSystem;
	private CustomerData testCustomerData;
	//private DoItYourselfStation selfCheckout;
	private DoItYourselfStationAR selfCheckout;
	private AttendantStation attendantStation;
	private CardReaderObserver cardReaderObs;

	@Before
	public void setUp() throws Exception {

		testCustomerData = new CustomerData();
		testSystem = new DIYSystem(testCustomerData, attendantStation);

		//selfCheckout = new DoItYourselfStation();
		selfCheckout = new DoItYourselfStationAR();
		selfCheckout.plugIn();
		selfCheckout.turnOn();

		// initiate pay by credit
		testSystem.resetReceiptPrice();
		testSystem.changeReceiptPrice(100.0);

		Currency curr = Currency.getInstance(Locale.CANADA);

		testSystem.payByCashStart();
		//opens the pay by cash window
	}



	@Test
	public void successfulPayment() {

	}




	//@Test (expected = SimulationException.class)
	public void nothingOwed() {
		// should not be able to reach state where payment executes and amount owing is 0
		testSystem.changeReceiptPrice(0.00);
		//testSystem.payByCredit(CORRECT_PIN, testSystem.getReceiptPrice());
	}

	@Test
	/**
	 * Pay for $0.05 out of the total payment, and if that is successful, the compare the remaining amount with expected
	 * @author-name : Simrat Benipal
	 */
	public void successfullyPartialPayments_5c()
	{
		testSystem.resetReceiptPrice();
		testSystem.changeReceiptPrice(10);
		//assuming $0.05 button is pressed
		Currency curr = Currency.getInstance(Locale.CANADA);
		long denomination = DIYSystem.acceptedCoinDenominations[4];
		//this coin is 0.05  (5 Cents)
		//public static final long[] acceptedCoinDenominations = {200l, 100l, 25l, 10l, 5l}
		double initialAmount = testSystem.getReceiptPrice();
		//System.out.println("current amount to pay: " + initialAmount);
		Coin coin = new Coin(curr,denomination);
		testSystem.InsertCoin(coin);
		//payment was successfull
		double amountRemaining = initialAmount - 0.05;
		//System.out.println("current amount to pay: " + amountRemaining);
		//System.out.println("current amount to pay: " + testSystem.getReceiptPrice());
		assertEquals(amountRemaining,testSystem.getReceiptPrice(), 0.0 );
		//System.out.println("current amount to pay: " + testSystem.getReceiptPrice());

	}

	/**
	 * Pay for $0.10 out of the total payment, and if that is successful, the compare the remaining amount with expected
	 * @author-name : Simrat Benipal
	 */
	@Test
	public void successfullyPartialPayments_10c()
	{
		testSystem.resetReceiptPrice();
		testSystem.changeReceiptPrice(10);
		//assuming $0.10 button is pressed
		Currency curr = Currency.getInstance(Locale.CANADA);
		long denomination = DIYSystem.acceptedCoinDenominations[3];
		//this coin is 0.10  (10 Cents)
		//public static final long[] acceptedCoinDenominations = {200l, 100l, 25l, 10l, 5l}
		double initialAmount = testSystem.getReceiptPrice();
		//System.out.println("current amount to pay: " + initialAmount);
		Coin coin = new Coin(curr,denomination);
		testSystem.InsertCoin(coin);
		//payment was successfull
		double amountRemaining = initialAmount - 0.10;
		//System.out.println("current amount to pay: " + amountRemaining);
		//System.out.println("current amount to pay: " + testSystem.getReceiptPrice());
		assertEquals(amountRemaining,testSystem.getReceiptPrice(), 0.0 );
		//System.out.println("current amount to pay: " + testSystem.getReceiptPrice());

	}
	/**
	 * Pay for $0.25 out of the total payment, and if that is successful, the compare the remaining amount with expected
	 * @author-name : Simrat Benipal
	 */
	@Test
	public void successfullyPartialPayments_25c()
	{
		testSystem.resetReceiptPrice();
		testSystem.changeReceiptPrice(10);
		//assuming $0.25 button is pressed
		Currency curr = Currency.getInstance(Locale.CANADA);
		long denomination = DIYSystem.acceptedCoinDenominations[2];
		//this coin is 0.25  (25 Cents)
		//public static final long[] acceptedCoinDenominations = {200l, 100l, 25l, 10l, 5l}
		double initialAmount = testSystem.getReceiptPrice();
		//System.out.println("current amount to pay: " + initialAmount);
		Coin coin = new Coin(curr,denomination);
		testSystem.InsertCoin(coin);
		//payment was successfull
		double amountRemaining = initialAmount - 0.25;
		//System.out.println("current amount to pay: " + amountRemaining);
		//System.out.println("current amount to pay: " + testSystem.getReceiptPrice());
		assertEquals(amountRemaining,testSystem.getReceiptPrice(), 0.0 );
		//System.out.println("current amount to pay: " + testSystem.getReceiptPrice());

	}
	/**
	 * Pay for $1.0 out of the total payment, and if that is successful, the compare the remaining amount with expected
	 * @author-name : Simrat Benipal
	 */
	@Test
	public void successfullyPartialPayments_1d()
	{
		testSystem.resetReceiptPrice();
		testSystem.changeReceiptPrice(10);
		//assuming $1.0 button is pressed
		Currency curr = Currency.getInstance(Locale.CANADA);
		long denomination = DIYSystem.acceptedCoinDenominations[1];
		//this coin is 1.0  (1.0 Dollars)
		//public static final long[] acceptedCoinDenominations = {200l, 100l, 25l, 10l, 5l}
		double initialAmount = testSystem.getReceiptPrice();
		//System.out.println("current amount to pay: " + initialAmount);
		Coin coin = new Coin(curr,denomination);
		testSystem.InsertCoin(coin);
		//payment was successfull
		double amountRemaining = initialAmount - 1.0;
		//System.out.println("current amount to pay: " + amountRemaining);
		//System.out.println("current amount to pay: " + testSystem.getReceiptPrice());
		assertEquals(amountRemaining,testSystem.getReceiptPrice(), 0.0 );
		//System.out.println("current amount to pay: " + testSystem.getReceiptPrice());

	}
	/**
	 * Pay for $2.0 out of the total payment, and if that is successful, the compare the remaining amount with expected
	 * @author-name : Simrat Benipal
	 */
	@Test
	public void successfullyPartialPayments_2d()
	{
		testSystem.resetReceiptPrice();
		testSystem.changeReceiptPrice(10);
		//assuming $2.0 button is pressed
		Currency curr = Currency.getInstance(Locale.CANADA);
		long denomination = DIYSystem.acceptedCoinDenominations[0];
		//this coin is 2.0 ($2)
		//public static final long[] acceptedCoinDenominations = {200l, 100l, 25l, 10l, 5l}
		double initialAmount = testSystem.getReceiptPrice();
		//System.out.println("current amount to pay: " + initialAmount);
		Coin coin = new Coin(curr,denomination);
		testSystem.InsertCoin(coin);
		//payment was successfull
		double amountRemaining = initialAmount - 2.0;
		//System.out.println("current amount to pay: " + amountRemaining);
		//System.out.println("current amount to pay: " + testSystem.getReceiptPrice());
		assertEquals(amountRemaining,testSystem.getReceiptPrice(), 0.0 );
		//System.out.println("current amount to pay: " + testSystem.getReceiptPrice());

	}

	/**
	 * Paying for extra and then comparing the change returned is same as expected
	 *@author-name : Simrat Benipal
	 */
	@Test
	public void correctChangeReturned()
	{
		testSystem.resetReceiptPrice();
		testSystem.changeReceiptPrice(0.25);
		Currency curr = Currency.getInstance(Locale.CANADA);
		long denomination = DIYSystem.acceptedCoinDenominations[0]; // {200l, 100l, 25l, 10l, 5l}

		//simulate paying by giving $2
		Coin coin = new Coin(curr,denomination);
		testSystem.InsertCoin(coin);

		//we should have 1.75 as returned change
		//simulate that is being dispensed
		testSystem.dispenseChangeDue();

		//simulating someone is collecting that change
		double changeCollected = testSystem.collectChange();
		//method to simulate the change is collected
		System.out.println("\nCORRECT Change Returned:" + changeCollected);
		assertEquals(changeCollected, 1.75, 0.0);



	}

	/**
	 * Pay for $5.0 out of the total payment, and if that is successful, the compare the remaining amount with expected
	 * @author-name : Simrat Benipal
	 */
	@Test
	public void successfullyPartialPayments_5d()
	{
		testSystem.resetReceiptPrice();
		testSystem.changeReceiptPrice(10);
		//assuming $5 note is inserted
		Currency curr = Currency.getInstance(Locale.CANADA);
		int denomination = DIYSystem.acceptedNoteDenominations[4];
		//public static final int[] acceptedNoteDenominations = {100, 50, 20, 10, 5}
		double initialAmount = testSystem.getReceiptPrice();
		//System.out.println("current amount to pay: " + initialAmount);
		Banknote banknote = new Banknote(curr,denomination);
		testSystem.InsertBanknote(banknote);
		//payment was successful
		double amountRemaining = initialAmount - 5.0;
		//System.out.println("current amount to pay: " + amountRemaining);
		//System.out.println("current amount to pay: " + testSystem.getReceiptPrice());
		assertEquals(amountRemaining,testSystem.getReceiptPrice(), 0.0 );
		//System.out.println("current amount to pay: " + testSystem.getReceiptPrice());

	}

	/**
	 * Pay for $100.0 out of the total payment, and if that is successful, the compare the remaining amount with expected
	 * @author-name : Simrat Benipal
	 */
	@Test
	public void successfullyPartialPayments_100d()
	{
		testSystem.resetReceiptPrice();
		testSystem.changeReceiptPrice(100);
		//assuming $5 note is inserted
		Currency curr = Currency.getInstance(Locale.CANADA);
		int denomination = DIYSystem.acceptedNoteDenominations[0];
		//public static final int[] acceptedNoteDenominations = {100, 50, 20, 10, 5}
		double initialAmount = testSystem.getReceiptPrice();
		//System.out.println("current amount to pay: " + initialAmount);
		Banknote banknote = new Banknote(curr,denomination);
		testSystem.InsertBanknote(banknote);
		//payment was successful
		double amountRemaining = initialAmount - 100.0;
		//System.out.println("current amount to pay: " + amountRemaining);
		//System.out.println("current amount to pay: " + testSystem.getReceiptPrice());
		assertEquals(amountRemaining,testSystem.getReceiptPrice(), 0.0 );
		System.out.println("current amount to pay: " + testSystem.getReceiptPrice());

	}

	/**
	 * Paying for extra and then comparing the change returned is same as expected
	 *@author-name : Simrat Benipal
	 */
	@Test
	public void Insert()
	{
		testSystem.resetReceiptPrice();
		testSystem.changeReceiptPrice(10);
		Currency curr = Currency.getInstance(Locale.CANADA);
		int denomination = DIYSystem.acceptedNoteDenominations[2];
		//public static final int[] acceptedNoteDenominations = {100, 50, 20, 10, 5}

		//simulate paying by giving $20
		Banknote banknote = new Banknote(curr,denomination);
		testSystem.InsertBanknote(banknote);

		testSystem.dispenseChangeDue();

		//simulating someone is collecting that change
		double changeCollected = testSystem.collectChange();
		//method to simulate the change is collected
		System.out.println(changeCollected);
		assertEquals(changeCollected, 10, 0.0);
	}
	
	/**
	 * Inserting invalid coin gets rejected 
	 * @author-name : Jason Osmond
	 */
	@Test
	public void insertInvalidCoin()
	{
		double testPrice = 10;
		testSystem.resetReceiptPrice();
		testSystem.changeReceiptPrice(testPrice);
		Currency curr = Currency.getInstance(Locale.US);
		long denomination = DIYSystem.acceptedCoinDenominations[2]; // 25 cents {200l, 100l, 25l, 10l, 5l}

		//simulate paying by giving 25 cents USD
		Coin coin = new Coin(curr,denomination);
		testSystem.InsertCoin(coin);
		
		//simulating someone is collecting that change
		double changeCollected = testSystem.collectChange();
		//method to simulate the change is collected
		assertEquals(changeCollected, DIYSystem.convertCentsToDollars(denomination), 0.00); //coin returned
		assertEquals(testPrice, testSystem.getReceiptPrice(), 0.00); //Price not changed
	}
	
	/**
	 * Insert invalid banknote is returned
	 * @author Jason Osmond
	 */
	public void insertInvalidBankNote()
	{
		double testPrice = 10;
		testSystem.resetReceiptPrice();
		testSystem.changeReceiptPrice(testPrice);
		Currency curr = Currency.getInstance(Locale.US);
		long denomination = DIYSystem.acceptedNoteDenominations[2]; // 25 cents {200l, 100l, 25l, 10l, 5l}

		//simulate paying by giving 25 cents USD
		Banknote banknote = new Banknote(curr,denomination);
		testSystem.InsertBanknote(banknote);
		
		//simulating someone is collecting that change
		Banknote note = testSystem.CollectBanknoteFromInput();
		assertEquals(note, banknote); //note returned
		assertEquals(testPrice, testSystem.getReceiptPrice(), 0.00); //Price not changed

	}
	
	/**
	 * Inserting invalid coin gets rejected 
	 * @author-name : Jason Osmond
	 */
//	@Test
//	public void notEnoughChangeReturned()
//	{
//		testSystem.resetReceiptPrice();
//		testSystem.changeReceiptPrice(1);
//		Currency curr = Currency.getInstance(Locale.CANADA);
//		int denomination = DIYSystem.acceptedNoteDenominations[0]; //$100
//
//		for(int i = 0; i < 20 ; i++) {
//			testSystem.InsertBanknote(new Banknote(curr,denomination));
//			testSystem.changeReceiptPrice(1);
//			testSystem.collectChange();
//			testSystem.dispenseChangeDue();
//			
//			if(testSystem.getChangeDue() > 0) {
//				break;
//			}
//		}
//
//		System.out.println("SDikhdsFKhfasdgfafgasgagga"+ testSystem.getChangeDue());
//		assertFalse(testSystem.isEnabled());
//	}
	
	
	




}
