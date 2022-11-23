package com.diy.software.system;

public class BagDispenser {
	private double BAG_PRICE = 0.1;
	private double BAG_WEIGHT = 0.1;
	private int bagCount;
	
	public BagDispenser(int bagCount) {
		this.bagCount = bagCount;
	}
	
	public double calcTotalBagPrice(int amountOfBags) {
		return Math.round(BAG_PRICE * amountOfBags * 100.0) / 100.0;
		
	}
	
	public double calcTotalBagWeight(int amountOfBags) {
		return Math.round(BAG_WEIGHT * amountOfBags * 100.0) / 100.0;
	}
	
	public boolean isDispenserEmpty() {
		return bagCount <= 0;
	}
	
	public void addBagsToDispenser(int amountToAdd) {
		bagCount += amountToAdd;
	}
	
	public void dispense(int amountToRemove) {
		bagCount -= amountToRemove;
		// Error checking will be in AddBags class.
	}
	
	public int getAmountOfBags() {
		return this.bagCount;
	}
}
