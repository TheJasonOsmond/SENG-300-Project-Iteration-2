package com.diy.software.system;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import com.diy.hardware.BarcodedProduct;
import com.diy.hardware.external.ProductDatabases;
import com.diy.simulation.Customer;
import com.jimmyselectronics.necchi.Barcode;
import com.jimmyselectronics.necchi.BarcodedItem;
import com.jimmyselectronics.necchi.Numeral;
import com.jimmyselectronics.opeechee.Card;

/** ITERATION 1.0
 * The logic of the system. Booted from Start. 
 * Currenty supports adding via scanning an item, and charging the customer via a selected card
 * @authors Brandon Greene (UCID 30157643)
 * 			Daniel Tangtakoune (UCID 30137487),
 * 			Saja AbuFarha, 
 * 			Travis Hamilton (UCID 30143567), 
 * 			Saianeesh Vinmani (UCID 30125756),
 * 			Noor(Rose) Muhammed
 *
 */

/**
 * Represents the Customer using the DIY Station
 * All information about the customer / bank the customer uses will be initialized and stored here
 * 
 * @author Brandon
 */

public class CustomerData {
	
	public Customer customer;
	private Bank custBank;
	
	public CustomerData() {
		initDefaultCustomer();
	}
	
	private void initDefaultCustomer() {
		customer = new Customer();
		setupBankAndCard();
		fillShoppingCart();
	}
	
	/**
	 * Get the customers bank they use
	 * @return
	 */
	public Bank getBank() {
		return this.custBank;
	}

	private void setupBankAndCard() {
		//Create a new Card and store it in the waller for the customer
		customer.wallet.cards.add(new Card("VISA", "1234567890123456", "John Doe", "909", "0000".intern(), true, true));
		customer.wallet.cards.add(new Card("Master Card", "6543210987654321", "John Doe", "415", "1111".intern(), true, true));
		//Creat a default bank called "Big Bank"
		custBank = new Bank("Big Bank", 10);
		
		/**
		 * @author simrat_benipal
		 */
		//Create a new Debit Card
		customer.wallet.cards.add(new Card("A Debit Card", "123123123123123", "John Debit", "123", "1234".intern(), true, true));
		customer.wallet.cards.add(new Card("Interac", "456456456456456", "John Interac", "123", "1234".intern(), true, true));	
		/** Simrat_benipal Code ends */
		
		//Setup a date to use as the expiry date for the newly created card
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, 12);
		
		//Store the card information into the "Big Bank" database with a limit of 2000
		custBank.addCardData("1234567890123456", "John Doe", cal, "909", 2000);
		custBank.addCardData("6543210987654321", "John Doe", cal, "415", 10);
		
		/** @author simrat_benipal */
		//Store the card information into the "Big Bank" database with a limit of 2000
		custBank.addCardData("123123123123123", "John Debit", cal, "123", 2000);
		custBank.addCardData("456456456456456", "John Interac", cal, "123", 10);
		/** Simrat_benipal Code ends */
		
	}
	
	/**
	 * Fill our customers cart with a random assortment of 25 items each with a random barcode generated
	 * and a random weight of 100-900 grams
	 */
	private void fillShoppingCart() {
		for(Barcode b : ProductDatabases.BARCODED_PRODUCT_DATABASE.keySet()) {
			this.customer.shoppingCart.add(new BarcodedItem(b, ProductDatabases.BARCODED_PRODUCT_DATABASE.get(b).getExpectedWeight()));
		}
	}
	
}
