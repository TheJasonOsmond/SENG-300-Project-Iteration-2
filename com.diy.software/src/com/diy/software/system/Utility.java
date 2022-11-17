package com.diy.software.system;

import java.util.Random;

import com.diy.hardware.BarcodedProduct;
import com.diy.hardware.external.ProductDatabases;
import com.jimmyselectronics.necchi.Barcode;
import com.jimmyselectronics.necchi.Numeral;

/** ITERATION 1.0
 * The logic of the system. Booted from Start. 
 * Currenty supports adding via scanning an item, and charging the customer via a selected card
 * @authors Brandon Greene (UCID 30157643)
 * 			Daniel Tangtakoune (UCID 30137487),
 * 			Saja AbuFarha (UCID 30163097), 
 * 			Travis Hamilton (UCID 30143567), 
 * 			Saianeesh Vinmani (UCID 30125756),
 * 			Noor(Rose) Muhammed
 *
 */


/**
 * 
 * Utility class to populate the given fake database
 */
public class Utility {
	
	private final static String[] ITEM_DESCRIPTIONS = {"Milk", "Bread", "Meat", "Cheese", "Juice", "Soda", "Water"};
	
	
	/**
	 * Fills our systems database of products with some irrelevant data
	 */
	public static final void fillDatbases() {
		
		if(ProductDatabases.BARCODED_PRODUCT_DATABASE.isEmpty() == false)
			return;
		
		Random rand = new Random();
		for(int i = 0; i < 7; i++) {
			Barcode itemCode = new Barcode(new Numeral[] {Numeral.valueOf((byte)rand.nextInt(9)), Numeral.valueOf((byte)rand.nextInt(9)), Numeral.valueOf((byte)rand.nextInt(9)),
					Numeral.valueOf((byte)rand.nextInt(9)), Numeral.valueOf((byte)rand.nextInt(9))});
			BarcodedProduct p = new BarcodedProduct(itemCode, ITEM_DESCRIPTIONS[i], i+1, (i+1)*50);
			ProductDatabases.BARCODED_PRODUCT_DATABASE.put(itemCode, p);
		}
	}
}
