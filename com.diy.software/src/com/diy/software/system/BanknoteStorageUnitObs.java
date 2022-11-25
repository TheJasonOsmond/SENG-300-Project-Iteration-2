package com.diy.software.system;

import com.unitedbankingservices.banknote.BanknoteStorageUnit;
import com.unitedbankingservices.banknote.BanknoteStorageUnitObserver;

public class BanknoteStorageUnitObs implements BanknoteStorageUnitObserver {

	private DIYSystem sys;
	
	public BanknoteStorageUnitObs(DIYSystem s) {
		this.sys = s;
	}
	
	/**
	 * Announces that the indicated banknote storage unit is full of banknotes.
	 * 
	 * @param unit
	 *            The storage unit where the event occurred.
	 */
	@Override
	public void banknotesFull(BanknoteStorageUnit unit) {}

	/**
	 * Announces that a banknote has been added to the indicated storage unit.
	 * 
	 * @param unit
	 *            The storage unit where the event occurred.
	 */
	@Override
	public void banknoteAdded(BanknoteStorageUnit unit) {
		int noteValue = sys.getLastValidNoteValue();
		sys.decreaseReceiptPrice(noteValue);
		sys.ValidCashReceived(noteValue);
		sys.payWindowMessage("Your coin has been accepted: $" + noteValue);
		
	}

	/**
	 * Announces that the indicated storage unit has been loaded with banknotes.
	 * Used to simulate direct, physical loading of the unit.
	 * 
	 * @param unit
	 *            The storage unit where the event occurred.
	 */
	@Override
	public void banknotesLoaded(BanknoteStorageUnit unit) {}

	/**
	 * Announces that the storage unit has been emptied of banknotes. Used to
	 * simulate direct, physical unloading of the unit.
	 * 
	 * @param unit
	 *            The storage unit where the event occurred.
	 */
	@Override
	public void banknotesUnloaded(BanknoteStorageUnit unit) {}
}
