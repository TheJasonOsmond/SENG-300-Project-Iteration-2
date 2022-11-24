package com.diy.software.system;

import com.unitedbankingservices.IDeviceObserver;
import com.unitedbankingservices.banknote.Banknote;
import com.unitedbankingservices.banknote.BanknoteDispenserObserver;
import com.unitedbankingservices.banknote.IBanknoteDispenser;
import com.unitedbankingservices.coin.CoinStorageUnit;
import com.unitedbankingservices.coin.CoinStorageUnitObserver;

/**
 * Observes events emanating from a coin storage unit.
 */
public class BanknoteDispenserObs implements BanknoteDispenserObserver {
	
	private DIYSystem sys;
	private long denomination;
	
	public BanknoteDispenserObs(DIYSystem s, long denomination) {
		this.sys = s;
		this.denomination = denomination;
	}
	
	/**
	 * Called to announce that the indicated banknote dispenser is full of
	 * banknotes.
	 * 
	 * @param dispenser
	 *            The dispenser where the event occurred.
	 */
	@Override
	public void moneyFull(IBanknoteDispenser dispenser) {}

	/**
	 * Called to announce that the indicated banknote dispenser is empty of
	 * banknotes.
	 * 
	 * @param dispenser
	 *            The dispenser where the event occurred.
	 */
	@Override
	public void banknotesEmpty(IBanknoteDispenser dispenser) {}

	/**
	 * Called to announce that the indicated banknote has been added to the
	 * indicated banknote dispenser.
	 * 
	 * @param dispenser
	 *            The dispenser where the event occurred.
	 * @param banknote
	 *            The banknote that was added.
	 */
	@Override
	public void billAdded(IBanknoteDispenser dispenser, Banknote banknote) {
		sys.decreaseReceiptPrice(banknote.getValue());
		sys.ValidCashReceived(banknote.getValue());
		sys.payWindowMessage("Your note has been accepted: $" + banknote.getValue());
	}

	/**
	 * Called to announce that the indicated banknote has been added to the
	 * indicated banknote dispenser.
	 * 
	 * @param dispenser
	 *            The dispenser where the event occurred.
	 * @param banknote
	 *            The banknote that was removed.
	 */
	@Override
	public void banknoteRemoved(IBanknoteDispenser dispenser, Banknote banknote) {}

	/**
	 * Called to announce that the indicated sequence of banknotes has been added to
	 * the indicated banknote dispenser. Used to simulate direct, physical loading
	 * of the dispenser.
	 * 
	 * @param dispenser
	 *            The dispenser where the event occurred.
	 * @param banknotes
	 *            The banknotes that were loaded.
	 */
	@Override
	public void banknotesLoaded(IBanknoteDispenser dispenser, Banknote... banknotes) {}

	/**
	 * Called to announce that the indicated sequence of banknotes has been removed
	 * to the indicated banknote dispenser. Used to simulate direct, physical
	 * unloading of the dispenser.
	 * 
	 * @param dispenser
	 *            The dispenser where the event occurred.
	 * @param banknotes
	 *            The banknotes that were unloaded.
	 */
	@Override
	public void banknotesUnloaded(IBanknoteDispenser dispenser, Banknote... banknotes) {}
}
