package com.unitedbankingservices.banknote;

import com.unitedbankingservices.IDeviceObserver;

/**
 * Observes events emanating from a banknote slot.
 */
public interface BanknoteSlotRObserver extends IDeviceObserver {
	/**
	 * An event announcing that a banknote has been inserted.
	 * 
	 * @param slot
	 *            The device on which the event occurred.
	 */
	default public void banknoteInserted(BanknoteSlotR slot) {}

	/**
	 * An event announcing that a banknote has been returned to the user, dangling
	 * from the slot.
	 * 
	 * @param slot
	 *            The device on which the event occurred.
	 */
	default public void banknoteEjected(BanknoteSlotR slot) {}

	/**
	 * An event announcing that a dangling banknote has been removed by the user.
	 * 
	 * @param slot
	 *            The device on which the event occurred.
	 */
	default public void banknoteRemoved(BanknoteSlotR slot) {}
}
