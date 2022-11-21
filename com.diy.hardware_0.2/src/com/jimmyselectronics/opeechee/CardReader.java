package com.jimmyselectronics.opeechee;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

import com.jimmyselectronics.AbstractDevice;
import com.jimmyselectronics.opeechee.Card.CardData;

import ca.powerutility.NoPowerException;
import ca.ucalgary.seng300.simulation.NullPointerSimulationException;
import ca.ucalgary.seng300.simulation.SimulationException;

/**
 * Represents the card reader, capable of tap, chip insert, and swipe. Either
 * the reader or the card may fail, or the data read in can be corrupted, with
 * varying probabilities.
 * 
 * @author Jimmy's Electronics LLP
 */
public class CardReader extends AbstractDevice<CardReaderListener> {
	private boolean cardIsInserted = false;
	private final static ThreadLocalRandom random = ThreadLocalRandom.current();
	private final static double PROBABILITY_OF_INSERT_FAILURE = 0.01;

	/**
	 * Insert the card. Requires power.
	 * 
	 * @param card
	 *            The card to insert.
	 * @param pin
	 *            The customer's PIN.
	 * @return The card data.
	 * @throws SimulationException
	 *             If there is already a card in the slot.
	 * @throws IOException
	 *             The insertion failed.
	 */
	public synchronized CardData insert(Card card, String pin) throws IOException {
		if(!isPoweredUp())
			throw new NoPowerException();

		if(cardIsInserted)
			throw new IllegalStateException("There is already a card in the slot");

		cardIsInserted = true;

		notifyCardInserted();

		if(card.hasChip && random.nextDouble(0.0, 1.0) > PROBABILITY_OF_INSERT_FAILURE) {
			CardData data = card.insert(pin);

			notifyCardDataRead(data);

			return data;
		}

		throw new ChipFailureException();
	}

	/**
	 * Remove the card from the slot. Requires power.
	 * 
	 * @throws NullPointerSimulationException
	 *             if no card is present.
	 */
	public void remove() {
		if(!isPoweredUp())
			throw new NoPowerException();

		if(!cardIsInserted)
			throw new NullPointerSimulationException();

		cardIsInserted = false;
		notifyCardRemoved();
	}

	private void notifyCardInserted() {
		for(CardReaderListener l : listeners())
			l.cardInserted(this);
	}

	private void notifyCardDataRead(CardData data) {
		for(CardReaderListener l : listeners())
			l.cardDataRead(this, data);
	}

	private void notifyCardRemoved() {
		for(CardReaderListener l : listeners())
			l.cardRemoved(this);
	}
}
