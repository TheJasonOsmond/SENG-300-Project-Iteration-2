package com.unitedbankingservices.banknote;

import com.unitedbankingservices.DisabledException;
import com.unitedbankingservices.PassiveSource;
import com.unitedbankingservices.Sink;
import com.unitedbankingservices.TooMuchCashException;

import ca.ucalgary.seng300.simulation.SimulationException;

/**
 * Represents a device that stores banknotes (as known as bills, paper money,
 * etc.) of a particular denomination to dispense them as change. This device
 * can receive additional banknotes both automatically and manually.
 */
public final class BanknoteDispenserAR extends AbstractBanknoteDispenser implements Sink<Banknote> {
	/**
	 * Represents the input source of this dispenser.
	 */
	public PassiveSource<Banknote> source;

	/**
	 * Creates a banknote dispenser that can be automatically refilled, with the indicated maximum capacity.
	 * 
	 * @param capacity
	 *            The maximum number of banknotes that can be stored in the
	 *            dispenser. Must be positive.
	 * @throws SimulationException
	 *             If capacity is not positive.
	 */
	public BanknoteDispenserAR(int capacity) {
		super(capacity);
	}

	@Override
	public void receive(Banknote cash) throws TooMuchCashException, DisabledException {
		super.receive(cash);
	}

	@Override
	public boolean hasSpace() {
		return super.hasSpace();
	}
}
