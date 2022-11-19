package com.diy.software.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import com.diy.hardware.BarcodedProduct;
//import com.diy.hardware.DoItYourselfStation;
import com.diy.hardware.DoItYourselfStationAR;
import com.diy.hardware.external.ProductDatabases;
import com.diy.software.system.CustomerData;
import com.diy.software.system.DIYSystem;
import com.jimmyselectronics.Item;
import com.jimmyselectronics.necchi.Barcode;
import com.jimmyselectronics.necchi.BarcodedItem;
import com.jimmyselectronics.necchi.IllegalDigitException;
import com.jimmyselectronics.necchi.Numeral;

import ca.ucalgary.seng300.simulation.InvalidArgumentSimulationException;

public class ScanItemTests {

	private DIYSystem testSystem;
	private CustomerData testCustomerData;
	//private DoItYourselfStation selfCheckout;
	private DoItYourselfStationAR selfCheckout;
		
	private Barcode normalBarcode;
	private Item normalItem;
	
	private ProductDatabases productDatabase;
	
	
	@Before
	public void setUp() throws Exception {
		
		testCustomerData = new CustomerData();
		testSystem = new DIYSystem(testCustomerData);

		//selfCheckout = new DoItYourselfStation();
		selfCheckout = new DoItYourselfStationAR();
		selfCheckout.plugIn();
		selfCheckout.turnOn();
		
		// populate product database with random products
		// Utility.fillDatbases();
		String[] ITEM_DESCRIPTIONS = {"Milk", "Bread", "Meat", "Cheese", "Juice", "Soda", "Water"};
		Random rand = new Random();
		for(int i = 0; i < 7; i++) {
			Barcode itemCode = new Barcode(new Numeral[] {Numeral.valueOf((byte)rand.nextInt(9)), Numeral.valueOf((byte)rand.nextInt(9)), Numeral.valueOf((byte)rand.nextInt(9)),
					Numeral.valueOf((byte)rand.nextInt(9)), Numeral.valueOf((byte)rand.nextInt(9))});
			BarcodedProduct p = new BarcodedProduct(itemCode, ITEM_DESCRIPTIONS[i], i+1, (i+1)*200);
			productDatabase.BARCODED_PRODUCT_DATABASE.put(itemCode, p);
		}
		
		normalBarcode = new Barcode(convert("00000"));
		normalItem = new BarcodedItem(normalBarcode, 10.0);
	}

	/**********************************************************************
	 * Attempt to get price of a product that does not exist. 
	 * Product's key not in populated database hashmap.
	 */
	@Test
	public void testGetProductNotExist() {
		assertNull(ProductDatabases.BARCODED_PRODUCT_DATABASE.get(normalBarcode));
	}
	
	/**********************************************************************
	 * Ensure that devices are disabled at the start of the scanning process
	 */
	@Test 
	public void weightChanges() {
		
		// check that the weight is initially 0
		assertEquals(testSystem.getCurrentExpectedWeight(),0, .1);
		
		// scan the list of items customer has
		testSystem.systemStartScan();
		
		// check the weight is greater than 0
		assertNotEquals(testSystem.getCurrentExpectedWeight(),0);
	}
	
	/**********************************************************************
	 * Ensure that devices are no longer disabled at the end of the scanning process
	 */
	@Test
	public void testSystemEnable() {
		testSystem.systemDisable();
		testSystem.systemEnable();
		assert(selfCheckout.scanner.isDisabled() == false);
	}
	

	// Convenience method to convert a string of number characters into the
	// corresponding numeral array. Need this to initialize barcodes.
	private Numeral[] convert(String s) {
		int len = s.length();
		Numeral[] digits = new Numeral[len];

		for(int i = 0; i < len; i++)
			try {
				digits[i] = Numeral.valueOf((byte)Character.digit(s.charAt(i), 10));
			}
			catch(IllegalDigitException e) {
				throw new InvalidArgumentSimulationException("s");
			}

		return digits;
	}
}
