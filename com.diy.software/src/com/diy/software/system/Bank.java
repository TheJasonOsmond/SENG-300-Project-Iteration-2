package com.diy.software.system;

import com.diy.hardware.external.CardIssuer;

/*
 * Implmenetation of the "Bank" just using the CardIssuer class
 */
public class Bank extends CardIssuer {

	public Bank(String name, long maximumHoldCount) {
		super(name, maximumHoldCount);
	}

}
