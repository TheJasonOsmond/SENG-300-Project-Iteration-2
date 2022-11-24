package com.diy.software.system;

import com.jimmyselectronics.AbstractDevice;
import com.jimmyselectronics.AbstractDeviceListener;
import com.unitedbankingservices.coin.CoinValidator;
import com.unitedbankingservices.coin.CoinValidatorObserver;


public class CoinValidatorObs implements CoinValidatorObserver {

	
	private DIYSystem sysRef;
	
	public CoinValidatorObs(DIYSystem s) {
		sysRef = s;
	}
	
	/**
	 * An event announcing that the indicated coin has been detected and determined
	 * to be valid.
	 * 
	 * @param validator
	 *            The device on which the event occurred.
	 * @param value
	 *            The value of the coin.
	 */
	@Override
	public void validCoinDetected(CoinValidator validator, long value) {
		System.out.println("Valid Coin Detected: $"+ value);
		
	}

	/**
	 * An event announcing that a coin has been detected and determined to be
	 * invalid.
	 * 
	 * @param validator
	 *            The device on which the event occurred.
	 */
	@Override
	public void invalidCoinDetected(CoinValidator validator) {
		
		
		
	}

}
