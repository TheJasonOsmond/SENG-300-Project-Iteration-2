package com.diy.software.system;

import com.unitedbankingservices.IDeviceObserver;
import com.unitedbankingservices.coin.CoinStorageUnit;
import com.unitedbankingservices.coin.CoinStorageUnitObserver;

/**
 * Observes events emanating from a coin storage unit.
 */
public class CoinStoreDenomObs implements CoinStorageUnitObserver {
	
	private DIYSystem sysRef;
	private long denomination;
	
	public CoinStoreDenomObs(DIYSystem s, long denomination) {
		this.sysRef = s;
		this.denomination = denomination;
	}
	
	/**
	 * Announces that the indicated coin storage unit is full of coins.
	 * 
	 * @param unit
	 *            The storage unit where the event occurred.
	 */
	public void coinsFull(CoinStorageUnit unit) {
		//TODO Block further input of coins of this value, or use overflow
	}

	/**
	 * Announces that a coin has been added to the indicated storage unit.
	 * 
	 * @param unit
	 *            The storage unit where the event occurred.
	 */
	public void coinAdded(CoinStorageUnit unit) {
		sysRef.ValidCashReceived(denomination);
	}

	/**
	 * Announces that the indicated storage unit has been loaded with coins. Used to
	 * simulate direct, physical loading of the unit.
	 * 
	 * @param unit
	 *            The storage unit where the event occurred.
	 */
	public void coinsLoaded(CoinStorageUnit unit) {}

	/**
	 * Announces that the storage unit has been emptied of coins. Used to simulate
	 * direct, physical unloading of the unit.
	 * 
	 * @param unit
	 *            The storage unit where the event occurred.
	 */
	public void coinsUnloaded(CoinStorageUnit unit) {}
}
