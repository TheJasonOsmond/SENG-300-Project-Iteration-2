package com.diy.software.system;

import com.unitedbankingservices.banknote.BanknoteSlotR;
import com.unitedbankingservices.banknote.BanknoteSlotRObserver;

public class BanknoteSlotROutputObs implements BanknoteSlotRObserver{
	private DIYSystem sys;
	
	public BanknoteSlotROutputObs(DIYSystem s) {
		this.sys = s;
	}
	/**
	 * An event announcing that a banknote has been inserted.
	 * 
	 * @param slot
	 *            The device on which the event occurred.
	 */
	@Override
	public void banknoteInserted(BanknoteSlotR slot) {}

	/**
	 * An event announcing that a banknote has been returned to the user, dangling
	 * from the slot.
	 * 
	 * @param slot
	 *            The device on which the event occurred.
	 */
	@Override
	public void banknoteEjected(BanknoteSlotR slot) {
		sys.CollectBanknoteFromOutput();
		
	}

	/**
	 * An event announcing that a dangling banknote has been removed by the user.
	 * 
	 * @param slot
	 *            The device on which the event occurred.
	 */
	@Override
	public void banknoteRemoved(BanknoteSlotR slot) {
		
	}
}
