package com.diy.software.system;

import java.util.Currency;

import com.unitedbankingservices.IDeviceObserver;
import com.unitedbankingservices.banknote.BanknoteValidator;
import com.unitedbankingservices.banknote.BanknoteValidatorObserver;
import com.unitedbankingservices.coin.CoinStorageUnit;
import com.unitedbankingservices.coin.CoinStorageUnitObserver;

/**
 * Observes events emanating from a coin storage unit.
 */
public class BanknoteValidatorObs implements BanknoteValidatorObserver {
	
	private DIYSystem sys;
	
	public BanknoteValidatorObs(DIYSystem s) {
		this.sys = s;
	}

	/**
	 * An event announcing that the indicated banknote has been detected and
	 * determined to be valid.
	 * 
	 * @param validator
	 *            The device on which the event occurred.
	 * @param currency
	 *            The kind of currency of the inserted banknote.
	 * @param value
	 *            The value of the inserted banknote.
	 */
	@Override
	public void validBanknoteDetected(BanknoteValidator validator, Currency currency, long value) {
		sys.updateLastValidNoteValue(value);
		System.out.println("VALID BANKNOTE DETECTED: $" + value);
	}

	/**
	 * An event announcing that the indicated banknote has been detected and
	 * determined to be invalid.
	 * 
	 * @param validator
	 *            The device on which the event occurred.
	 */
	@Override
	public void invalidBanknoteDetected(BanknoteValidator validator) {
		System.out.println("INVALID BANKNOTE DETECTED");
		//Bank Node Rejected back to insertion slot
		
	}
}
