package com.diy.software.system;

import com.unitedbankingservices.coin.Coin;
import com.unitedbankingservices.coin.CoinDispenserObserver;
import com.unitedbankingservices.coin.CoinStorageUnit;
import com.unitedbankingservices.coin.ICoinDispenser;

/**
 * Observes events emanating from a coin storage unit.
 */
public class CoinDispenserObs implements CoinDispenserObserver {
	
	private DIYSystem sys;
	private long denomination;
	
	public CoinDispenserObs(DIYSystem s, long denomination) {
		this.sys = s;
		this.denomination = denomination;
	}
	
	/**
	 * Announces that the indicated coin dispenser is full of coins.
	 * 
	 * @param dispenser
	 *            The dispenser where the event occurred.
	 */
	@Override
	public void coinsFull(ICoinDispenser dispenser) {
		
	}

	/**
	 * Announces that the indicated coin dispenser is empty of coins.
	 * 
	 * @param dispenser
	 *            The dispenser where the event occurred.
	 */
	@Override
	public void coinsEmpty(ICoinDispenser dispenser) {
		
	}

	/**
	 * Announces that the indicated coin has been added to the indicated coin
	 * dispenser.
	 * 
	 * @param dispenser
	 *            The dispenser where the event occurred.
	 * @param coin
	 *            The coin that was added.
	 */
	@Override
	public void coinAdded(ICoinDispenser dispenser, Coin coin) {
		double dollarValue = DIYSystem.convertCentsToDollars(coin.getValue());
		System.out.println("Coin Val = " + coin.getValue() + "c or $" + dollarValue);
		sys.decreaseReceiptPrice(dollarValue);
		sys.ValidCashReceived(dollarValue);
		sys.payWindowMessage("Your coin has been accepted: $" + dollarValue);
	}

	/**
	 * Announces that the indicated coin has been added to the indicated coin
	 * dispenser.
	 * 
	 * @param dispenser
	 *            The dispenser where the event occurred.
	 * @param coin
	 *            The coin that was removed.
	 */
	@Override
	public void coinRemoved(ICoinDispenser dispenser, Coin coin) {
		double dollarValue = DIYSystem.convertCentsToDollars(coin.getValue());
		sys.decreaseChangeDue(dollarValue);
	}

	/**
	 * Announces that the indicated sequence of coins has been added to the
	 * indicated coin dispenser. Used to simulate direct, physical loading of the
	 * dispenser.
	 * 
	 * @param dispenser
	 *            The dispenser where the event occurred.
	 * @param coins
	 *            The coins that were loaded.
	 */
	@Override
	public void coinsLoaded(ICoinDispenser dispenser, Coin... coins) {
		System.out.println("Coin Dispenser $"+ DIYSystem.convertCentsToDollars(denomination) + " loaded with " + coins.length + " coins");
	}

	/**
	 * Announces that the indicated sequence of coins has been removed to the
	 * indicated coin dispenser. Used to simulate direct, physical unloading of the
	 * dispenser.
	 * 
	 * @param dispenser
	 *            The dispenser where the event occurred.
	 * @param coins
	 *            The coins that were unloaded.
	 */
	@Override
	public void coinsUnloaded(ICoinDispenser dispenser, Coin... coins) {}

}
