/**
 * Testing for AddBags.java
 * Inputs given by the tester should be in this order:
 * 3, OK, OK
 * 50, OK, OK
 * CANCEL
 * 3, OK, CANCEL
 * CANCEL
 * OK
 * a, OK, CANCEL
 * -1, OK, CANCEL
 * @author Quang(Brandon) Nguyen
 */

package com.diy.software.tests;
import com.diy.hardware.DoItYourselfStationAR;
import com.diy.software.system.AddBags;
import com.diy.software.system.AttendantStation;
import com.diy.software.system.BagDispenser;
import com.diy.software.system.CustomerData;
import com.diy.software.system.DIYSystem;
import com.jimmyselectronics.EmptyException;
import com.jimmyselectronics.OverloadException;
import com.jimmyselectronics.abagnale.ReceiptPrinterD;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.assertEquals;

// Running tests in a fixed order.
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AddBagsTests {
    private DIYSystem system;
    private DoItYourselfStationAR station;
    private AttendantStation attendant;
    private CustomerData testCustomerData;

    private BagDispenser bag_dispenser;
    private AddBags testAddBags;

    @Before
    public void setUp() throws Exception {
        station = new DoItYourselfStationAR();

        station.plugIn();
        station.turnOn();

        station.scale.plugIn();
        station.scale.turnOn();
        station.scale.enable();

        station.scanner.plugIn();
        station.scanner.turnOn();
        station.scanner.enable();

        //Test system
        testCustomerData = new CustomerData();
        attendant = new AttendantStation(new CustomerData[]{testCustomerData});
        system = new DIYSystem(testCustomerData, attendant);

        system.systemEnable();
        system.addBag();
        testAddBags = system.getAddBagsRef();

    }

    /**
     * Testing adding 3 store bought bags. User input should be 3. Button clicks should be "OK" followed with "OK"
     * Should pass, as the bags were purchased successfully.
     */
    @Test
    public void add3Bags() {
    	testAddBags.triggerStoreBagButton();
    	
    	assertEquals(3, testAddBags.getBag_purchased());
    	assertEquals(true, testAddBags.bagPurchasedSuccessful());
    	assertEquals(false, testAddBags.getBagDispenserRef().isDispenserEmpty());
    }
    
    /**
     * Testing adding 50 store bought bags, emptying the bag dispenser. 
     * User input should be 50. Button clicks should be "OK" followed with "OK"
     * Should pass, as the bags were purchased successfully, the dispenser is empty, and the main system is blocked.
     */
    @Test
    public void add50BagsEmptyDispenser() {
    	testAddBags.triggerStoreBagButton();
    	
    	assertEquals(50, testAddBags.getBag_purchased());
    	assertEquals(true, testAddBags.bagPurchasedSuccessful());
    	assertEquals(true, testAddBags.getBagDispenserRef().isDispenserEmpty());
    	assertEquals(false, system.isEnabled());
    }
    
    /**
     * Testing first cancel operation. 
     * Button click should be "Cancel"
     * Should pass, as no bags should have been bought.
     */
    @Test
    public void cancelOperation1() {
    	testAddBags.triggerStoreBagButton();
    	
    	assertEquals(0, testAddBags.getBag_purchased());
    	assertEquals(false, testAddBags.bagPurchasedSuccessful());
    }

    /**
     * Testing second cancel operation. User input should be 3. 
     * Button click should be "OK", followed with "Cancel"
     * Should pass, as no bags should have been bought.
     */
    @Test
    public void cancelOperation2() {
    	testAddBags.triggerStoreBagButton();
    	
    	assertEquals(3, testAddBags.getBag_purchased());
    	assertEquals(false, testAddBags.bagPurchasedSuccessful());
    }
    
    /**
     * Testing negative user input. User input should be -1. 
     * Button click should be "OK", followed with "Cancel"
     * Should pass, as no bags should have been bought and the exception will be caught.
     */
    @Test
    public void testNegativeBags() {
    	testAddBags.triggerStoreBagButton();
    	
    	assertEquals(-1, testAddBags.getBag_purchased());
    	assertEquals(false, testAddBags.bagPurchasedSuccessful());
    }
    
    /**
     * Testing NaN user input. User input should be the string "a". 
     * Button click should be "OK", followed with "Cancel"
     * Should pass, as no bags should have been bought and the exception will be caught.
     */
    @Test
    public void testNaNBags() {
    	testAddBags.triggerStoreBagButton();
    	
    	assertEquals(0, testAddBags.getBag_purchased());
    	assertEquals(false, testAddBags.bagPurchasedSuccessful());
    }
    
    /**
     * Testing adding own bags. 
     * Button click should be "OK", followed with "OK"
     * Should pass, as the bags were added to the bagging area and the system is blocked.
     */
    @Test
    public void testAddOwnBagSuccess() {
    	testAddBags.triggerOwnBagButton();
    	
    	assertEquals(true, testAddBags.bagAddedSuccessful());
    	assertEquals(false, system.isEnabled());
    }
    
    /**
     * Testing canceling adding own bags. 
     * Button click should be "Cancel"
     * Should pass, as no bags were added to the bagging area and the system should not be blocked.
     */
    @Test
    public void testAddOwnBagCancel() {
    	testAddBags.triggerOwnBagButton();
    	
    	assertEquals(false, testAddBags.bagAddedSuccessful());
    	assertEquals(true, system.isEnabled());
    }
    
    /**
     * Testing exit button. 
     * Should pass, as no bags were added to the bagging area, system should not be blocked, and 0 bags have been bought.
     */
    @Test
    public void testExit() {
    	testAddBags.triggerExitButton();
    	
    	assertEquals(false, testAddBags.bagAddedSuccessful());
    	assertEquals(true, system.isEnabled());
    	assertEquals(0, testAddBags.getBag_purchased());
    }

}