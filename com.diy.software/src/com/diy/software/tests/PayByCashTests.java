package com.diy.software.tests;

import ca.ucalgary.seng300.simulation.SimulationException;
import com.diy.hardware.DoItYourselfStationAR;
import com.diy.software.system.AttendantStation;
import com.diy.software.system.CardReaderObserver;
import com.diy.software.system.CustomerData;
import com.diy.software.system.DIYSystem;
import com.unitedbankingservices.banknote.Banknote;
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
		testSystem.resetReceiptPrice();
		testSystem.changeReceiptPrice(10);
		Currency curr = Currency.getInstance(Locale.CANADA);
		long denomination = DIYSystem.acceptedCoinDenominations[0];
		testSystem.InsertCoin(curr,denomination);
		testSystem.InsertCoin(curr,denomination);
		testSystem.InsertCoin(curr,denomination);
		testSystem.InsertCoin(curr,denomination);
		testSystem.InsertCoin(curr,denomination);
		assertEquals(0.0,testSystem.getReceiptPrice(), 0.0 );
	}




	@Test
	public void nothingOwed() {
		//Set amount to pay to $0
		testSystem.resetReceiptPrice();
		// should not be able to reach state where payment executes and amount owing is 0
		testSystem.changeReceiptPrice(0.00);
		testSystem.payByCash(0);
		assertEquals(0.0,testSystem.getReceiptPrice(), 0.0 );
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
		testSystem.InsertCoin(curr,denomination);
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
		testSystem.InsertCoin(curr,denomination);
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
		testSystem.InsertCoin(curr,denomination);
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
		testSystem.InsertCoin(curr,denomination);
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
		testSystem.InsertCoin(curr,denomination);
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
		long denomination = DIYSystem.acceptedCoinDenominations[0];
		//this coin is 0.05  (5 Cents)
		//public static final long[] acceptedCoinDenominations = {200l, 100l, 25l, 10l, 5l}

		//simulate paying by giving $2
		testSystem.InsertCoin(curr,denomination);

		//we should have 1.75 as returned change
		//simulate that is being dispensed
		testSystem.dispenseChangeDue();

		//simulating someone is collecting that change
		double changeCollected = testSystem.collectChange();
		//method to simulate the change is collected
		System.out.println(changeCollected);
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
		testSystem.InsertBanknote(curr,denomination);
		//payment was successfull
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
		testSystem.InsertBanknote(curr,denomination);
		//payment was successfull
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
	public void correctChangeReturned_note()
	{
		testSystem.resetReceiptPrice();
		testSystem.changeReceiptPrice(10);
		Currency curr = Currency.getInstance(Locale.CANADA);
		int denomination = DIYSystem.acceptedNoteDenominations[2];
		//public static final int[] acceptedNoteDenominations = {100, 50, 20, 10, 5}

		//simulate paying by giving $2
		testSystem.InsertBanknote(curr,denomination);

		testSystem.dispenseChangeDue();

		//simulating someone is collecting that change
		double changeCollected = testSystem.collectChange();
		//method to simulate the change is collected
		System.out.println(changeCollected);
		assertEquals(changeCollected, 1.75, 0.0);



	}




}
