package com.diy.software.tests;

import com.diy.hardware.DoItYourselfStationAR;
import com.diy.software.system.AttendantStation;
import com.diy.software.system.CardReaderObserver;
import com.diy.software.system.CustomerData;
import com.diy.software.system.DIYSystem;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PayByDebitTests {
    private static final String CORRECT_PIN = "1234";
    private DIYSystem testSystem;
    private CustomerData testCustomerData;
    private DoItYourselfStationAR testStation;
    private CardReaderObserver cardReaderObs;


    @Before
    public void setUp() {
    	
    	System.out.println("=================================");
    	testCustomerData = new CustomerData();
 		CustomerData customers[] = {testCustomerData};
 		AttendantStation attendant = new AttendantStation(customers);
 		
 		testSystem = attendant.getCurrentDIY();
         
        testSystem.resetReceiptPrice();
        testSystem.changeReceiptPrice(1.0); // Set amount to pay to $1.0
        testSystem.payByDebitStart(", A Debit Card"); // Pass in card data and start the payment process
        
        
       
    }

    /**
     * Case where payment was successful
     */
    @Test
    public void successfulPaymentInsert() {
        testSystem.payByDebit("1234",testSystem.getReceiptPrice()); // Correct pin should now throw a BlockedCardException
        assertEquals(0, testSystem.getReceiptPrice(), .1); // Assert total still owed
        assertTrue(testSystem.getWasPaymentPosted()); // Assert payment NOT posted
    }
    
    /**
     * Case where payment was successful via tap
     */
    @Test
    public void successfulPaymentTap() {
        testSystem.payByDebitTap(testSystem.getReceiptPrice()); 
        assertEquals(0, testSystem.getReceiptPrice(), 0.0); // Assert total still owed
        assertTrue(testSystem.getWasPaymentPosted()); // Assert payment IS posted
    }
    
    /**
     * Case where partial payment was successful via tap
     */
    @Test
    public void successfulPartialPaymentTap() {
    	testSystem.resetReceiptPrice();
        testSystem.changeReceiptPrice(100.0); // Set amount to pay to $100.0
        testSystem.payByDebitTap(50); 
        assertEquals(50, testSystem.getReceiptPrice(), 0.0); // Assert total should be 50
        //remaining amount to pay should be 50
      
    }
    
    /**
     * Case where payment was successful via swipe
     */
    @Test
    public void successfulPaymentSwipe() {
        testSystem.payByDebitSwipe(testSystem.getReceiptPrice()); 
        assertEquals(0, testSystem.getReceiptPrice(), 0.0); // Assert total still owed
        assertTrue(testSystem.getWasPaymentPosted()); // Assert payment IS posted
    }
    

    /**
     * Case where partial payment was successful
     */
    @Test
    public void successfulPartialPayment() {
    	testSystem.resetReceiptPrice();
        testSystem.changeReceiptPrice(100.0); // Set amount to pay to $100.0
        testSystem.payByDebit("1234",50);
        System.out.println(testSystem.getReceiptPrice());
        
        assertEquals(50,testSystem.getReceiptPrice(), 0.0 );
        //we should have $50 should left to pay in the whole receipt
        
     
    }

    /**
     * Case where a blank (null) PIN was entered.
     */
    @Test
    public void blankPINEntered() {
        testSystem.payByDebit("",testSystem.getReceiptPrice());
        assertEquals(1, testSystem.getReceiptPrice(), 0.0); // Assert total still owed
        assertFalse(testSystem.getWasPaymentPosted()); // Assert payment NOT posted
    }

    /**
     * Case where wrong PIN was entered.
     */
    @Test
    public void wrongPINEntered(){
        testSystem.payByDebit("0000", testSystem.getReceiptPrice());
        //Receit Price will be same, and transaction will not be posted
        assertEquals(1, testSystem.getReceiptPrice(), .1); // Assert total still owed
        assertFalse(testSystem.getWasPaymentPosted()); // Assert payment NOT posted
    }

    /**
     * Case where PIN was entered 3 times and card was blocked.
     */
    @Test
    public void blockedCard(){
        // Simulate blocked card by entering wrong pin 3 times.
        testSystem.payByDebit("0000", testSystem.getReceiptPrice()); // Wrong PIN entered once
        assertEquals(1, testSystem.getReceiptPrice(), .1); // Assert total still owed
        assertFalse(testSystem.getWasPaymentPosted()); // Assert payment NOT posted

        testSystem.changeReceiptPrice(10.00);
        testSystem.payByDebit("0000",testSystem.getReceiptPrice()); // Wrong PIN entered twice
        assertEquals(11, testSystem.getReceiptPrice(), .1); // Assert total still owed
        assertFalse(testSystem.getWasPaymentPosted()); // Assert payment NOT posted

        testSystem.changeReceiptPrice(10.00);
        testSystem.payByDebit("0000",testSystem.getReceiptPrice()); // Wrong PIN entered thrice
        assertEquals(21, testSystem.getReceiptPrice(), .1); // Assert total still owed
        assertFalse(testSystem.getWasPaymentPosted()); // Assert payment NOT posted

        testSystem.changeReceiptPrice(10.00);
        testSystem.payByDebit("1234",testSystem.getReceiptPrice()); // Correct pin should now throw a BlockedCardException
        assertEquals(31, testSystem.getReceiptPrice(), .1); // Assert total still owed
        assertFalse(testSystem.getWasPaymentPosted()); // Assert payment NOT posted
    }


    /**
     * Case where nothing is owed.
     */
    @Test
    public void nothingOwed() {
        // should not be able to reach state where payment executes and amount owing is 0
        testSystem.changeReceiptPrice(0.00);
        testSystem.payByDebit(CORRECT_PIN,testSystem.getReceiptPrice());
    }


    /**
     * Case where amount owed is greater than then funds available on card.
     */
    @Test
    public void insufficientFunds() {
        testSystem.resetReceiptPrice();
        testSystem.changeReceiptPrice(2001.00);
        testSystem.payByDebit("1234",testSystem.getReceiptPrice());

        assertEquals(2001, testSystem.getReceiptPrice(), .1);
        assertFalse(testSystem.getWasPaymentPosted());
    }


    /**********************************************************************
     * Try and pay after reaching maximum bank hold
     */
    @Test
    public void bankHoldReached() {
        for (int i = 0; i <= 10; i++) {
            // set up successful transaction
            testSystem.resetReceiptPrice();
            testSystem.changeReceiptPrice(1.0);
            testSystem.payByDebit("1000",testSystem.getReceiptPrice());
        }

        testSystem.resetReceiptPrice();
        testSystem.changeReceiptPrice(1.0);
        testSystem.payByDebit(CORRECT_PIN,testSystem.getReceiptPrice());
        assertFalse(testSystem.getWasPaymentPosted());
    }
}
