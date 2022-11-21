package com.diy.hardware;

import java.util.Currency;
import java.util.Locale;

import com.jimmyselectronics.disenchantment.TouchScreen;
import com.jimmyselectronics.necchi.BarcodeScanner;
import com.jimmyselectronics.opeechee.CardReader;
import com.jimmyselectronics.virgilio.ElectronicScale;

import ca.ucalgary.seng300.simulation.InvalidArgumentSimulationException;
import ca.ucalgary.seng300.simulation.NullPointerSimulationException;

/**
 * Represents the overall DIY self-checkout station.
 * <p>
 * A self-checkout possesses the following units of hardware that the customer
 * can see and interact with:
 * <ul>
 * <li>one electronic scale, with a configurable maximum weight before it
 * overloads;</li>
 * <li>one card reader;</li>
 * <li>one touch screen; and,</li>
 * <li>one scanner.</li>
 * </ul>
 * <p>
 * All other functionality of the system must be performed in software,
 * installed on the self-checkout station through custom observer classes
 * implementing the various observer interfaces provided.
 * </p>
 */
public class DoItYourselfStation {
	static {
		resetConfigurationToDefaults();
	}

	private static Currency currencyConfiguration;

	/**
	 * Configures the currency to be supported.
	 * 
	 * @param curr
	 *            The currency to be supported.
	 */
	public static void configureCurrency(Currency curr) {
		if(curr == null)
			throw new NullPointerSimulationException("currency");
		currencyConfiguration = curr;
	}

	private static double scaleMaximumWeightConfiguration;

	/**
	 * Configures the maximum weight permitted for the scales.
	 * 
	 * @param weight
	 *            The maximum weight permitted for the scales.
	 */
	public static void configureScaleMaximumWeight(double weight) {
		if(weight <= 0.0)
			throw new InvalidArgumentSimulationException("The maximum weight must be positive.");

		scaleMaximumWeightConfiguration = weight;
	}

	private static double scaleSensitivityConfiguration;

	/**
	 * Configures the sensitivity of the scales.
	 * 
	 * @param sensitivity
	 *            The sensitivity of the scales.
	 */
	public static void configureScaleSensitivity(double sensitivity) {
		if(sensitivity <= 0.0)
			throw new InvalidArgumentSimulationException("The sensitivity must be positive.");

		scaleSensitivityConfiguration = sensitivity;
	}

	/**
	 * Resets the configuration to the default values.
	 */
	public static void resetConfigurationToDefaults() {
		currencyConfiguration = Currency.getInstance(Locale.CANADA);
		scaleMaximumWeightConfiguration = 5000.0;
		scaleSensitivityConfiguration = 0.5;
	}

	/**
	 * Represents the large scale where items are to be placed once they have been
	 * scanned or otherwise entered.
	 */
	public final ElectronicScale baggingArea;
	/**
	 * Represents a device that can read electronic cards, through one or more input
	 * modes according to the setup of the card.
	 */
	public final CardReader cardReader;
	/**
	 * Represents a large, central barcode scanner.
	 */
	public final BarcodeScanner scanner;
	/**
	 * Represents the touch screen on which a graphical user interface can be
	 * produced.
	 */
	public final TouchScreen touchScreen;

	/**
	 * Constructor utilizing the current, static configuration.
	 */
	public DoItYourselfStation() {
		// Create the devices.
		baggingArea = new ElectronicScale(scaleMaximumWeightConfiguration, scaleSensitivityConfiguration);
		cardReader = new CardReader();
		scanner = new BarcodeScanner();
		touchScreen = new TouchScreen();
	}

	/**
	 * Plugs in all the devices in the station.
	 */
	public void plugIn() {
		baggingArea.plugIn();
		cardReader.plugIn();
		scanner.plugIn();
		touchScreen.plugIn();
	}

	/**
	 * Unplugs all the devices in the station.
	 */
	public void unplug() {
		baggingArea.unplug();
		cardReader.unplug();
		scanner.unplug();
		touchScreen.unplug();
	}

	/**
	 * Turns on all the devices in the station.
	 */
	public void turnOn() {
		baggingArea.turnOn();
		cardReader.turnOn();
		scanner.turnOn();
		touchScreen.turnOn();
	}

	/**
	 * Turns off all the devices in the station.
	 */
	public void turnOff() {
		baggingArea.turnOff();
		cardReader.turnOff();
		scanner.turnOff();
		touchScreen.turnOff();
	}
}
