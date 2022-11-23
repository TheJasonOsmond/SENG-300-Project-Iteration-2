package com.diy.hardware;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.sound.sampled.AudioSystem;

import com.jimmyselectronics.abagnale.ReceiptPrinterD;
import com.jimmyselectronics.necchi.BarcodeScanner;
import com.jimmyselectronics.opeechee.CardReader;
import com.jimmyselectronics.virgilio.ElectronicScale;
import com.unitedbankingservices.Sink;
import com.unitedbankingservices.banknote.AbstractBanknoteDispenser;
import com.unitedbankingservices.banknote.Banknote;
import com.unitedbankingservices.banknote.BanknoteDispenserAR;
import com.unitedbankingservices.banknote.BanknoteSlotR;
import com.unitedbankingservices.banknote.BanknoteStorageUnit;
import com.unitedbankingservices.banknote.BanknoteValidator;
import com.unitedbankingservices.banknote.IBanknoteDispenser;
import com.unitedbankingservices.coin.Coin;
import com.unitedbankingservices.coin.CoinDispenserAR;
import com.unitedbankingservices.coin.CoinSlot;
import com.unitedbankingservices.coin.CoinStorageUnit;
import com.unitedbankingservices.coin.CoinValidator;
import com.unitedbankingservices.coin.ICoinDispenser;

import ca.ucalgary.seng300.simulation.InvalidArgumentSimulationException;
import ca.ucalgary.seng300.simulation.NullPointerSimulationException;

/**
 * Represents the overall self-checkout station.
 * <p>
 * A self-checkout possesses the following units of hardware that the customer
 * can see and interact with:
 * <ul>
 * <li>one electronic scale, with a configurable maximum weight before it
 * overloads;</li>
 * <li>one card reader;</li>
 * <li>one scanner;</li>
 * <li>one receipt printer;</li>
 * <li>one input slot for banknotes;</li>
 * <li>one output slot for banknotes;</li>
 * <li>one input slot for coins;</li>
 * <li>one output tray for coins; and,</li>
 * <li>one speaker for audio output (note: you should directly use the
 * {@link AudioSystem} class, if you want to produce sounds).</li>
 * </ul>
 * <p>
 * In addition, these units of hardware are accessible to personnel with a key
 * to unlock the front of the station:
 * <ul>
 * <li>one banknote storage unit, with configurable capacity;</li>
 * <li>one or more banknote dispensers, one for each supported denomination of
 * banknote, as configured;</li>
 * <li>one coin storage unit, with configurable capacity; and,</li>
 * <li>one or more coin dispensers, one for each supported denomination of coin,
 * as configured.</li>
 * </ul>
 * <p>
 * And finally, there are certain, additional units of hardware that would only
 * be accessible to someone with the appropriate tools (like a screwdriver,
 * crowbar, or sledge hammer):
 * <ul>
 * <li>one banknote validator; and</li>
 * <li>one coin validator.</li>
 * </ul>
 * <p>
 * Many of these devices are interconnected, to permit coins or banknotes to
 * pass between them. Specifically:
 * <ul>
 * <li>the coin slot is connected to the coin validator (this is a
 * one-directional chain of devices);</li>
 * <li>the coin validator is connected to each of the coin dispensers (i.e., the
 * coin dispensers can be replenished with coins entered by customers), to the
 * coin storage unit (for any overflow coins that do not fit in the dispensers),
 * and to the coin tray for any rejected coins either because the coins are
 * invalid or because even the overflow storage unit is full (this is a
 * one-directional chain of devices);
 * <li>each coin dispenser is connected to the coin tray, to provide change
 * (this is a one-directional chain of devices);</li>
 * <li>the banknote input slot is connected to the banknote validator (this is a
 * <b>two</b>-directional chain of devices as any entered banknotes that are
 * rejected by the validator can be returned to the customer);</li>
 * <li>the banknote validator is connected to the banknote storage unit (this is
 * a one-directional chain of devices) and to each of the banknote dispensers;
 * and,</li>
 * <li>each banknote dispenser is connected to the output banknote slot; these
 * dispensers can be replenished by banknotes provided by customers (this is a
 * one-directional chain of devices).</li>
 * </ul>
 * <p>
 * All other functionality of the system must be performed in software,
 * installed on the self-checkout station through custom observer classes
 * implementing the various observer interfaces provided.
 * </p>
 * <p>
 * Note that banknote denominations are required to be positive integers, while
 * coin denominations are positive decimal values.
 */
public class DoItYourselfStationAR {
	static {
		resetConfigurationToDefaults();
	}

	private static int banknoteDispenserCapacityConfiguration;

	/**
	 * Configures the maximum capacity of the banknote dispensers.
	 * 
	 * @param count
	 *            The maximum capacity.
	 */
	public static void configureBanknoteDispenserCapacity(int count) {
		if(count <= 0)
			throw new InvalidArgumentSimulationException("Count must be positive.");
		banknoteDispenserCapacityConfiguration = count;
	}

	private static int coinDispenserCapacityConfiguration;

	/**
	 * Configures the maximum capacity of the coin dispensers.
	 * 
	 * @param count
	 *            The maximum capacity.
	 */
	public static void configureCoinDispenserCapacity(int count) {
		if(count <= 0)
			throw new InvalidArgumentSimulationException("Count must be positive.");
		coinDispenserCapacityConfiguration = count;
	}

	private static int banknoteStorageUnitCapacityConfiguration;

	/**
	 * Configures the maximum capacity of the banknote storage unit.
	 * 
	 * @param count
	 *            The maximum capacity.
	 */
	public static void configureBanknoteStorageUnitCapacity(int count) {
		if(count <= 0)
			throw new InvalidArgumentSimulationException("Count must be positive.");
		banknoteStorageUnitCapacityConfiguration = count;
	}

	private static int coinStorageUnitCapacityConfiguration;

	/**
	 * Configures the maximum capacity of the coin storage unit.
	 * 
	 * @param count
	 *            The maximum capacity.
	 */
	public static void configureCoinStorageUnitCapacity(int count) {
		if(count <= 0)
			throw new InvalidArgumentSimulationException("Count must be positive.");
		coinStorageUnitCapacityConfiguration = count;
	}

	private static int coinTrayCapacityConfiguration;

	/**
	 * Configures the maximum capacity of the coin tray.
	 * 
	 * @param count
	 *            The maximum capacity.
	 */
	public static void configureCoinTrayCapacity(int count) {
		if(count <= 0)
			throw new InvalidArgumentSimulationException("Count must be positive.");
		coinStorageUnitCapacityConfiguration = count;
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

	private static int[] banknoteDenominationsConfiguration;

	/**
	 * Configures the set of banknote denominations.
	 * 
	 * @param denominations
	 *            The denominations to use for banknotes.
	 */
	public static void configureBanknoteDenominations(int[] denominations) {
		if(denominations == null)
			throw new NullPointerSimulationException("denominations");

		if(denominations.length < 1)
			throw new InvalidArgumentSimulationException("There must be at least one denomination.");

		HashSet<Integer> set = new HashSet<>();
		for(int denomination : denominations) {
			if(denomination < 1)
				throw new InvalidArgumentSimulationException("Each denomination must be positive.");

			set.add(denomination);
		}

		if(set.size() != denominations.length)
			throw new InvalidArgumentSimulationException("The denominations must all be unique.");

		// Copy the array to avoid the potential for a security hole
		banknoteDenominationsConfiguration = Arrays.copyOf(denominations, denominations.length);
	}

	private static List<Long> coinDenominationsConfiguration;

	/**
	 * Configures the set of coin denominations.
	 * 
	 * @param denominations
	 *            The denominations to use for coins.
	 */
	public static void configureCoinDenominations(long[] denominations) {
		if(denominations == null)
			throw new NullPointerSimulationException("denominations");

		if(denominations.length < 1)
			throw new InvalidArgumentSimulationException("There must be at least one denomination.");

		HashSet<Long> set = new HashSet<>();
		for(Long denomination : denominations) {
			if(denomination <= 0L)
				throw new InvalidArgumentSimulationException("Each denomination must be positive.");

			set.add(denomination);
		}

		if(set.size() != denominations.length)
			throw new InvalidArgumentSimulationException("The denominations must all be unique.");

		// Copy the array to avoid the potential for a security hole
		coinDenominationsConfiguration = new ArrayList<Long>();
		for(long denomination : denominations)
			coinDenominationsConfiguration.add(denomination);
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
		banknoteDenominationsConfiguration = new int[] { 1 };
		banknoteDispenserCapacityConfiguration = 100;
		banknoteStorageUnitCapacityConfiguration = 1000;
		coinDenominationsConfiguration = new ArrayList<>();
		coinDenominationsConfiguration.add(Long.valueOf(1L));
		coinDispenserCapacityConfiguration = 100;
		coinStorageUnitCapacityConfiguration = 1000;
		coinTrayCapacityConfiguration = 25;
		currencyConfiguration = Currency.getInstance(Locale.CANADA);
		scaleMaximumWeightConfiguration = 5000.0;
		scaleSensitivityConfiguration = 0.5;
	}

	/**
	 * Represents the large scale where items are to be placed once they have been
	 * scanned or otherwise entered.
	 */
	public final ElectronicScale scale;
	/**
	 * Represents a device that can read electronic cards, through one or more input
	 * modes according to the setup of the card.
	 */
	public final CardReader cardReader;
	/**
	 * Represents a device that can print receipts.
	 */
	public final ReceiptPrinterD printer;
	/**
	 * Represents a large, central barcode scanner.
	 */
	public final BarcodeScanner scanner;
	/**
	 * Represents a device that permits banknotes to be entered.
	 */
	public final BanknoteSlotR banknoteInput;
	/**
	 * Represents a device that permits banknotes to be given to the customer.
	 */
	public final BanknoteSlotR banknoteOutput;
	/**
	 * Represents a device that checks the validity of a banknote, and determines
	 * its denomination.
	 */
	public final BanknoteValidator banknoteValidator;
	/**
	 * Represents a device that stores banknotes.
	 */
	public final BanknoteStorageUnit banknoteStorage;
	/**
	 * Represents the set of denominations supported by the self-checkout system.
	 */
	public final int[] banknoteDenominations;
	/**
	 * Represents the set of banknote dispensers, indexed by the denomination that
	 * each contains. Note that nothing prevents banknotes of the wrong denomination
	 * to be loaded into a given dispenser.
	 */
	public final Map<Integer, BanknoteDispenserAR> banknoteDispensers;

	/**
	 * Represents a device that permits coins to be entered.
	 */
	public final CoinSlot coinSlot;
	/**
	 * Represents a device that checks the validity of a coin, and determines its
	 * denomination.
	 */
	public final CoinValidator coinValidator;
	/**
	 * Represents a device that stores coins that have been entered by customers.
	 */
	public final CoinStorageUnit coinStorage;
	/**
	 * Represents the set of denominations of coins supported by this self-checkout
	 * system.
	 */
	public final List<Long> coinDenominations;
	/**
	 * Represents the set of coin dispensers, indexed by the denomination of coins
	 * contained by each.
	 */
	public final Map<Long, CoinDispenserAR> coinDispensers;
	/**
	 * Represents a device that receives coins to return to the customer.
	 */
	public final CoinTray coinTray;

	/**
	 * Constructor utilizing the current, static configuration.
	 */
	public DoItYourselfStationAR() {
		// Create the devices.
		scale = new ElectronicScale(scaleMaximumWeightConfiguration, scaleSensitivityConfiguration);
		cardReader = new CardReader();
		scanner = new BarcodeScanner();
		printer = new ReceiptPrinterD();

		// Since the array in banknoteDenominationsConfiguration was already copied, we
		// can just use it in multiple stations, as it is immutable.
		banknoteDenominations = banknoteDenominationsConfiguration;
		banknoteInput = new BanknoteSlotR(false);
		banknoteValidator = new BanknoteValidator(currencyConfiguration, banknoteDenominations);
		banknoteStorage = new BanknoteStorageUnit(banknoteStorageUnitCapacityConfiguration);
		banknoteOutput = new BanknoteSlotR(true);

		banknoteDispensers = new HashMap<>();

		for(int i = 0; i < banknoteDenominations.length; i++)
			banknoteDispensers.put(banknoteDenominations[i],
				new BanknoteDispenserAR(banknoteDispenserCapacityConfiguration));

		coinDenominations = coinDenominationsConfiguration;
		coinSlot = new CoinSlot();
		coinValidator = new CoinValidator(currencyConfiguration, coinDenominations);
		coinStorage = new CoinStorageUnit(coinStorageUnitCapacityConfiguration);
		coinTray = new CoinTray(coinTrayCapacityConfiguration);

		coinDispensers = new HashMap<>();

		for(int i = 0; i < coinDenominations.size(); i++)
			coinDispensers.put(coinDenominations.get(i), new CoinDispenserAR(coinDispenserCapacityConfiguration));

		// Hook up everything.
		interconnect(banknoteInput, banknoteValidator);
		interconnect(banknoteValidator, banknoteStorage);

		// BUG: We are having difficulty getting the automatic refill part of this
		// functionality to work properly. In this simulation, it is not hooked up
		// correctly as yet, therefore.
		for(IBanknoteDispenser dispenser : banknoteDispensers.values())
			interconnect(dispenser, banknoteOutput);

		interconnect(coinSlot, coinValidator);
		interconnect(coinValidator, coinTray, coinDispensers, coinStorage);

		for(CoinDispenserAR coinDispenser : coinDispensers.values())
			interconnect(coinDispenser, coinTray);
	}

	private void interconnect(BanknoteSlotR slot, BanknoteValidator validator) {
		BidirectionalChannel<Banknote> channel = new BidirectionalChannel<Banknote>(slot, validator);
		slot.sink = channel;
		validator.source = channel;
	}

	private void interconnect(BanknoteValidator validator, BanknoteStorageUnit storage) {
		UnidirectionalChannel<Banknote> channel = new UnidirectionalChannel<Banknote>(storage);
		validator.sink = channel;
	}

	private void interconnect(IBanknoteDispenser dispenser, BanknoteSlotR slot) {
		UnidirectionalChannel<Banknote> channel = new UnidirectionalChannel<Banknote>(slot);
		((AbstractBanknoteDispenser)dispenser).sink = channel;
	}

	private void interconnect(CoinSlot slot, CoinValidator validator) {
		UnidirectionalChannel<Coin> channel = new UnidirectionalChannel<Coin>(validator);
		slot.sink = channel;
	}

	private void interconnect(CoinValidator validator, CoinTray tray, Map<Long, CoinDispenserAR> dispensers,
		CoinStorageUnit storage) {
		UnidirectionalChannel<Coin> rejectChannel = new UnidirectionalChannel<Coin>(tray);
		Map<Long, Sink<Coin>> dispenserChannels = new HashMap<Long, Sink<Coin>>();

		for(Long denomination : dispensers.keySet()) {
			CoinDispenserAR dispenser = dispensers.get(denomination);
			dispenserChannels.put(denomination, new UnidirectionalChannel<Coin>(dispenser));
		}

		UnidirectionalChannel<Coin> overflowChannel = new UnidirectionalChannel<Coin>(storage);

		validator.rejectionSink = rejectChannel;
		validator.standardSinks.putAll(dispenserChannels);
		validator.overflowSink = overflowChannel;
	}

	private void interconnect(CoinDispenserAR dispenser, CoinTray tray) {
		UnidirectionalChannel<Coin> channel = new UnidirectionalChannel<Coin>(tray);
		dispenser.sink = channel;
	}

	/**
	 * Plugs in all the devices in the station.
	 */
	public void plugIn() {
		scale.plugIn();
		for(IBanknoteDispenser bd : banknoteDispensers.values())
			bd.connect();
		banknoteInput.connect();
		banknoteOutput.connect();
		banknoteStorage.connect();
		banknoteValidator.connect();
		cardReader.plugIn();
		for(ICoinDispenser cd : coinDispensers.values())
			cd.connect();
		coinSlot.connect();
		coinStorage.connect();
		// Don't turn on the coin tray
		coinValidator.connect();
		scanner.plugIn();
		printer.plugIn();
	}

	/**
	 * Unplugs all the devices in the station.
	 */
	public void unplug() {
		scale.unplug();
		for(IBanknoteDispenser bd : banknoteDispensers.values())
			bd.disconnect();
		banknoteInput.disconnect();
		banknoteOutput.disconnect();
		banknoteStorage.disconnect();
		banknoteValidator.disconnect();
		cardReader.unplug();
		for(ICoinDispenser cd : coinDispensers.values())
			cd.disconnect();
		coinSlot.disconnect();
		coinStorage.disconnect();
		// Don't turn on the coin tray
		coinValidator.disconnect();
		scanner.unplug();
		printer.unplug();
	}

	/**
	 * Turns on all the devices in the station.
	 */
	public void turnOn() {
		scale.turnOn();
		for(IBanknoteDispenser bd : banknoteDispensers.values())
			bd.activate();
		banknoteInput.activate();
		banknoteOutput.activate();
		banknoteStorage.activate();
		banknoteValidator.activate();
		cardReader.turnOn();
		for(CoinDispenserAR cd : coinDispensers.values())
			cd.activate();
		coinSlot.activate();
		coinStorage.activate();
		// Don't turn on the coin tray
		coinValidator.activate();
		scanner.turnOn();
		printer.turnOn();
	}

	/**
	 * Turns off all the devices in the station.
	 */
	public void turnOff() {
		scale.turnOff();
		for(IBanknoteDispenser bd : banknoteDispensers.values())
			bd.disactivate();
		banknoteInput.disactivate();
		banknoteOutput.disactivate();
		banknoteStorage.disactivate();
		banknoteValidator.disactivate();
		cardReader.turnOff();
		for(CoinDispenserAR cd : coinDispensers.values())
			cd.disactivate();
		coinSlot.disactivate();
		coinStorage.disactivate();
		// Don't turn on the coin tray
		coinValidator.disactivate();
		scanner.turnOff();
		printer.turnOff();
	}
}
