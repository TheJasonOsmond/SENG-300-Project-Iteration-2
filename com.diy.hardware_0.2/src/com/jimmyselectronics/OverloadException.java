package com.jimmyselectronics;

/**
 * Represents situations where a device has been overloaded, in terms of weight,
 * quantity of items, etc.
 * 
 * @author Jimmy's Electronics LLP
 */
public class OverloadException extends Exception {
	private static final long serialVersionUID = 7813659161520664284L;

	/**
	 * Create an exception without an error message.
	 */
	public OverloadException() {}
}
