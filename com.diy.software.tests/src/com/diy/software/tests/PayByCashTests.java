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

		//selfCheckout = new DoItYourselfStation();
		selfCheckout = new DoItYourselfStationAR();
		selfCheckout.plugIn();
		selfCheckout.turnOn();		
			
		// initiate pay by credit 
		testSystem.resetReceiptPrice();
		testSystem.changeReceiptPrice(1.0);

		
	}

	

	@Test 
	public void successfulPayment() {
		
	}
	
	
	

	@Test (expected = SimulationException.class)
	public void nothingOwed() {
		// should not be able to reach state where payment executes and amount owing is 0
		testSystem.changeReceiptPrice(0.00);
		testSystem.payByCredit(CORRECT_PIN, testSystem.getReceiptPrice());
	}

	@Test
	public void correctChangeReturned() {

	}

	@Test
	public void partialPayments() {

	}
	


		
	

}
