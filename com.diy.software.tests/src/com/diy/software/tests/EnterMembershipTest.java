package com.diy.software.tests;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;

import com.diy.software.system.*;

import ca.powerutility.PowerGrid;

import com.diy.hardware.*;

public class EnterMembershipTest {
	DIYSystem diysystem;
	private CustomerData testCustomerData;
	DoItYourselfStationAR station;
	MemberDatabase memberdatabase;
	AttendantStation attendant;
	/**
	 * Do standard set up to get the self-checkout system in normal state
	 * for regular operation. 
	 */
	@Before
	public void setup() {
		testCustomerData = new CustomerData();
		diysystem = new DIYSystem(testCustomerData, attendant);
		//station = new DoItYourselfStationAR();
		//station.plugIn();
		//station.turnOn();
		PowerGrid.instance().forcePowerRestore();
		PowerGrid.engageUninterruptiblePowerSource();
		
	//	memberdatabase = new MemberDatabase();
	//	memberdatabase.MEMBER_DATABASE.put(555,"Jenny");
	}
	
	/**
	 * Cleanup the state of the power grid.
	 */
	@After
	public void teardown() {
		PowerGrid.reconnectToMains();
	}
	
	// Test where customer's inputted number is in database
	@Test
	public void testmemberinDB() {
		//diysystem.enterMembership("555");
		//assertTrue(diysystem.get_memberinDB());
		assertTrue(memberdatabase.MEMBER_DATABASE.containsKey(555));
	}
	
	// Test where customer's inputted number is not in the database.
	@Test
	public void testmembernotinDB() {
		//diysystem.enterMembership("111");
		//assertFalse(diysystem.get_memberinDB());
		assertFalse(memberdatabase.MEMBER_DATABASE.containsKey(111));
	}
	
}
