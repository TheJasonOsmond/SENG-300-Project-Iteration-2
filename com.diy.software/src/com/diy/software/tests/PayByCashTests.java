package com.diy.software.tests;

import ca.ucalgary.seng300.simulation.SimulationException;
import com.diy.hardware.DoItYourselfStationAR;
import com.diy.software.system.AttendantStation;
import com.diy.software.system.CardReaderObserver;
import com.diy.software.system.CustomerData;
import com.diy.software.system.DIYSystem;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class PayByCashTests {

	private static final String CORRECT_PIN = "0000";
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
		
		// create listener
		cardReaderObs = new CardReaderObserver(testSystem);

		//selfCheckout = new DoItYourselfStation();
		selfCheckout = new DoItYourselfStationAR();
		selfCheckout.plugIn();
		selfCheckout.turnOn();		
			
		// initiate pay by credit 
		testSystem.resetReceiptPrice();
		testSystem.changeReceiptPrice(1.0);
		testSystem.payByCashStart();		
		
	}

	
	/********************************************************************** 
	// PAY BY CASH TESTS  
	/********************************************************************** 

	/********************************************************************** 
	 * Blank PIN Entered
	 */
	@Test
	public void blankPIN() {
	
		// blank PIN should throw InvalidPINException
		testSystem.payByCredit("",testSystem.getReceiptPrice());
		
		// assert total still owed and payment NOT posted
		assertEquals(1, testSystem.getReceiptPrice(), .1);
		assertFalse(testSystem.getWasPaymentPosted());
	}

	
	
	/********************************************************************** 
	 * test entering wrong pin sends correct messages, no update to totals
	 */
	@Test 
	public void wrongPINBlockedPIN() {
	
		// add amount to total
		// attempt to pay with wrong pin 
		testSystem.payByCredit("8675", testSystem.getReceiptPrice());
		// insure total amount owed is unchanged, correct error is set
		assertEquals(1, testSystem.getReceiptPrice(), .1);
		assertFalse(testSystem.getWasPaymentPosted());
				
		// update total
		testSystem.changeReceiptPrice(1.00);
		// attempt to pay with wrong pin 
		testSystem.payByCredit("3009", testSystem.getReceiptPrice());
		// insure total amount owed is unchanged
		assertEquals(2, testSystem.getReceiptPrice(), .1);
		assertFalse(testSystem.getWasPaymentPosted());
		
		// update total
		// attempt to pay with wrong pin 
		testSystem.changeReceiptPrice(1.00);
		testSystem.payByCredit("2112", testSystem.getReceiptPrice());
		// insure total amount owed is unchanged
		assertEquals(3, testSystem.getReceiptPrice(), .1);
		assertFalse(testSystem.getWasPaymentPosted());				
		
		// update total
		testSystem.changeReceiptPrice(1.00);
		// check with valid pin final time, but card should be locked (ie no payment)
		testSystem.payByCredit(CORRECT_PIN, testSystem.getReceiptPrice());	
		// insure total amount owed is unchanged even with 
		assertEquals(4, testSystem.getReceiptPrice(), .1);
		assertFalse(testSystem.getWasPaymentPosted());
	}	
	
	
	
	/**********************************************************************
	 * complete a successful payment
	 */
	@Test 
	public void successfulPayment() {

		// make valid payment
		testSystem.payByCredit(CORRECT_PIN, testSystem.getReceiptPrice());
		
		// check amount owing is 0, and successful payment posted
		assertEquals(0, testSystem.getReceiptPrice(), .1);
		assertTrue(testSystem.getWasPaymentPosted());

		
		// Try another transaction
		// increase total again
		testSystem.resetReceiptPrice();
		testSystem.changeReceiptPrice(42.0);
			
		// make anther valid payment
		testSystem.payByCredit(CORRECT_PIN, testSystem.getReceiptPrice());
		
		// check amount owing is 0, and successful payment posted
		assertEquals(0, testSystem.getReceiptPrice(), .1);
		assertTrue(testSystem.getWasPaymentPosted());
		
	}
	
	
	
	/**********************************************************************
	 * Attempt to pay for nothing 
	 */
	@Test (expected = SimulationException.class)
	public void nothingOwed() {
	
		// should not be able to reach state where payment executes and amount owing is 0
		testSystem.changeReceiptPrice(0.00);
		testSystem.payByCredit(CORRECT_PIN, testSystem.getReceiptPrice());
	}
	
	

	
	/**********************************************************************
	 * Attempt to pay for an amount greater than the credit available on card
	 */
	@Test 
	public void noCreditOnCard() {
	
		// set amount over limit
		testSystem.resetReceiptPrice();
		testSystem.changeReceiptPrice(2001.00);
				
		// attempt to pay
		testSystem.payByCredit(CORRECT_PIN, testSystem.getReceiptPrice());
		
		// ensure the amount is still the same
		assertEquals(2001, testSystem.getReceiptPrice(), .1);		
	}
		
	
	/**********************************************************************
	 * Try and pay after reaching maximum bank hold
	 */
	@Test 
	public void bankHoldReached() {
			
		// complete 11 transactions to reach maximum holds for VISA
		for (int i = 0; i < 11; i++) {
			
			// set up successful transaction
			testSystem.resetReceiptPrice();
			testSystem.changeReceiptPrice(1.0);
			testSystem.payByCredit(CORRECT_PIN, testSystem.getReceiptPrice());
			//assertTrue(testSystem.getWasPaymentPosted());
		}
		
		testSystem.resetReceiptPrice();
		testSystem.changeReceiptPrice(1.0);
		testSystem.payByCredit(CORRECT_PIN, testSystem.getReceiptPrice());
		assertFalse(testSystem.getWasPaymentPosted());
	}
}
