 package com.diy.software.system;

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
public class Start {
	
	public static void main(String[] args) {
		//Fill up the Database
		Utility.fillDatbases();
		
		//Make an attending customer
		CustomerData c = new CustomerData();
		CustomerData customers[] = {c};
		
		//Make the station and go into it	
		AttendantStation attendant = new AttendantStation(customers);
		
	}
}
	