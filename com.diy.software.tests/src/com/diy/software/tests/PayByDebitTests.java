package com.diy.software.tests;

import com.diy.hardware.DoItYourselfStationAR;
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
        testCustomerData = new CustomerData(); // Create predefined customer data
        testSystem = new DIYSystem(testCustomerData); // Create Test System
        cardReaderObs = new CardReaderObserver(testSystem); // Create Card Reader Observer
        testStation = new DoItYourselfStationAR(); // Create DIY Station
        testStation.plugIn();
        testStation.turnOn();
        testSystem.resetReceiptPrice();
        testSystem.changeReceiptPrice(1.0); // Set amount to pay to $1.0
        testSystem.payByDebitStart(", A Debit Card"); // Pass in card data and start the payment process
    }

    /**
     * Case where payment was successful
     */
    @Test
    public void successfulPayment() {
        testSystem.payByDebit("1234"); // Correct pin should now throw a BlockedCardException
        assertEquals(0, testSystem.getReceiptPrice(), .1); // Assert total still owed
        assertTrue(testSystem.getWasPaymentPosted()); // Assert payment NOT posted
    }

    /**
     * Case where a blank (null) PIN was entered.
     */
    @Test
    public void blankPINEntered() {
        testSystem.payByDebit(""); // Blank PIN should throw InvalidPINException
        assertEquals(1, testSystem.getReceiptPrice(), .1); // Assert total still owed
        assertFalse(testSystem.getWasPaymentPosted()); // Assert payment NOT posted
    }

    /**
     * Case where wrong PIN was entered.
     */
    @Test
    public void wrongPINEntered(){
        testSystem.payByDebit("0000"); // Wrong PIN should throw InvalidPINException
        assertEquals(1, testSystem.getReceiptPrice(), .1); // Assert total still owed
        assertFalse(testSystem.getWasPaymentPosted()); // Assert payment NOT posted
    }

    /**
     * Case where PIN was entered 3 times and card was blocked.
     */
    @Test
    public void blockedCard(){
        // Simulate blocked card by entering wrong pin 3 times.
        testSystem.payByDebit("0000"); // Wrong PIN entered once
        assertEquals(1, testSystem.getReceiptPrice(), .1); // Assert total still owed
        assertFalse(testSystem.getWasPaymentPosted()); // Assert payment NOT posted

        testSystem.changeReceiptPrice(10.00);
        testSystem.payByDebit("0000"); // Wrong PIN entered twice
        assertEquals(11, testSystem.getReceiptPrice(), .1); // Assert total still owed
        assertFalse(testSystem.getWasPaymentPosted()); // Assert payment NOT posted

        testSystem.changeReceiptPrice(10.00);
        testSystem.payByDebit("0000"); // Wrong PIN entered thrice
        assertEquals(21, testSystem.getReceiptPrice(), .1); // Assert total still owed
        assertFalse(testSystem.getWasPaymentPosted()); // Assert payment NOT posted

        testSystem.changeReceiptPrice(10.00);
        testSystem.payByDebit("1234"); // Correct pin should now throw a BlockedCardException
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
        testSystem.payByDebit(CORRECT_PIN);
    }


    /**
     * Case where amount owed is greater than then funds available on card.
     */
    @Test
    public void insufficientFunds() {
        testSystem.resetReceiptPrice();
        testSystem.changeReceiptPrice(2001.00);
        testSystem.payByDebit("1234");

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
            testSystem.payByDebit("1000");
        }

        testSystem.resetReceiptPrice();
        testSystem.changeReceiptPrice(1.0);
        testSystem.payByDebit(CORRECT_PIN);
        assertFalse(testSystem.getWasPaymentPosted());
    }
}
