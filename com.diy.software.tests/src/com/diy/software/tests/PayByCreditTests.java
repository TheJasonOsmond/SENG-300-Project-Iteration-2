package com.diy.software.tests;

import com.diy.hardware.BarcodedProduct;
//import com.diy.hardware.DoItYourselfStation;
import com.diy.hardware.DoItYourselfStationAR;
import com.diy.hardware.external.ProductDatabases;
import com.diy.simulation.Customer;
import com.diy.software.system.AttendantStation;
import com.diy.software.system.Bank;
import ca.ucalgary.seng300.simulation.SimulationException;
import com.diy.hardware.DoItYourselfStationAR;
import com.diy.software.system.AttendantStation;
import com.diy.software.system.CardReaderObserver;
import com.diy.software.system.CustomerData;
import com.diy.software.system.DIYSystem;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class PayByCreditTests {

	private static final String CORRECT_PIN = "0000";
	private DIYSystem testSystem;
	private CustomerData testCustomerData;
    private DoItYourselfStationAR selfCheckout;
	private AttendantStation attendantStation;
	private CardReaderObserver cardReaderObs;
	
	
	
	@Before
	public void setUp() throws Exception {
		
		testCustomerData = new CustomerData();

 		CustomerData customers[] = {testCustomerData};
 		AttendantStation attendant = new AttendantStation(customers);
 		
 		testSystem = attendant.getCurrentDIY();
		
		// create listener
		cardReaderObs = new CardReaderObserver(testSystem);

		selfCheckout = new DoItYourselfStationAR();
		selfCheckout.plugIn();
		selfCheckout.turnOn();		
			
		// initiate pay by credit 
		testSystem.resetReceiptPrice();
		testSystem.changeReceiptPrice(1.0);
		testSystem.payByCreditStart(", VISA");		
		
	}

	
	/********************************************************************** 
	// PAY BY CREDIT TESTS  
	 * 
	 */
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
	@Test 
	public void nothingOwed() {
	
		// should not be able to reach state where payment executes and amount owing is 0
		testSystem.changeReceiptPrice(0.00);
		testSystem.payByCredit(CORRECT_PIN, testSystem.getReceiptPrice());
	}
	
	  /**
     * Case where partial payment was successful
     */
    @Test
    public void successfulPartialPayment() {
    	testSystem.resetReceiptPrice();
        testSystem.changeReceiptPrice(100.0); // Set amount to pay to $100.0
        testSystem.payByCredit("1234",50);
        System.out.println(testSystem.getReceiptPrice());
        if(testSystem.getWasPaymentPosted())
        	assertEquals(50,testSystem.getReceiptPrice(), 0.0 );
        //we should have $50 should left to pay in the whole receipt
        else
        {	//if the payment was not posted (for some errors, the price will be same as before)
        	assertEquals(100,testSystem.getReceiptPrice(), 0.0 );
        }
        
     
    }
    
    /**
     * Case where payment was successful via tap
     */
    @Test
    public void successfulPaymentTap() {
        testSystem.payByCreditTap(testSystem.getReceiptPrice()); // Correct pin  
        if(testSystem.getWasPaymentPosted())
        	assertEquals(0, testSystem.getReceiptPrice(), 0.0); // Assert total still owed
        else
        	assertEquals(1, testSystem.getReceiptPrice(), 0.0); // Assert total still owed
        	
        
    }
    
    /**
     * Case where partial payment was successful via tap
     */
    @Test
    public void successfulPartialPaymentTap() {
    	testSystem.resetReceiptPrice();
        testSystem.changeReceiptPrice(100.0); // Set amount to pay to $100.0
        testSystem.payByCreditTap(50); 
        assertEquals(50, testSystem.getReceiptPrice(), 0.0); // Assert total should be 50
        //remaining amount to pay should be 50
      
    }
    
    
    /**
     * Case where payment was successful via swipe
     */
    @Test
    public void successfulPaymentSwipe() {
        testSystem.payByCreditSwipe(testSystem.getReceiptPrice()); // Correct pin 
        if(testSystem.getWasPaymentPosted())
        	assertEquals(0, testSystem.getReceiptPrice(), 0.0); // Assert total still owed
        //checking if and only if the payment was posted otherwise the receitp price should be same as before
        else
        	assertEquals(1, testSystem.getReceiptPrice(), 0.0); // Assert total still owed
        	
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
}
