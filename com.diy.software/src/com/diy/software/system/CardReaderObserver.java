package com.diy.software.system;

import com.jimmyselectronics.AbstractDevice;
import com.jimmyselectronics.AbstractDeviceListener;
import com.jimmyselectronics.opeechee.Card.CardData;
import com.jimmyselectronics.opeechee.CardReader;
import com.jimmyselectronics.opeechee.CardReaderListener;

/*
 * CardReaderObserver will act as the inbetween for the card READER and the BANK being used
 */
public class CardReaderObserver implements CardReaderListener {
		
	private long holdNumber;
	private DIYSystem sys;
	public CardData localData = null;
	
	public CardReaderObserver(DIYSystem s) {
		sys = s;
	}

	@Override
	public void enabled(AbstractDevice<? extends AbstractDeviceListener> device) {
		//TODO maybe later
	}

	@Override
	public void disabled(AbstractDevice<? extends AbstractDeviceListener> device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void turnedOn(AbstractDevice<? extends AbstractDeviceListener> device) {
		//TODO maybe later
	}

	@Override
	public void turnedOff(AbstractDevice<? extends AbstractDeviceListener> device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cardInserted(CardReader reader) {
		//Will probably post to the GUI that a card has been inserted once implemented
		sys.payWindowMessage("Validating the pin...");
		System.out.println("Validating the pin...");
	}

	@Override
	public void cardRemoved(CardReader reader) {
		//Will probably post to the GUI that a card has been inserted once implemented
		System.out.println("The Card Has Been Removed!");
	}
	
	/*
	 * The card has been successfully read and is ready to charge the account
	 */
	@Override
	public void cardDataRead(CardReader reader, CardData data) {
		//Set a processing payment message
		sys.payWindowMessage("Processing payment...");
		
		localData = data;
		System.out.println("Card Used = " + localData.getCardholder() + " , " + localData.getKind());
		
		//get a hold number from the customers bank on the amount due
		holdNumber = sys.getUserData().getBank().authorizeHold(data.getNumber(), sys.getReceiptPrice());
		
		//Check to see if the hold was successfull...
		if(holdNumber == -1) {
			//The hold was not processed, do nothing
			sys.payWindowMessage("Hold not successfull, insufficent funds!");
		} else {
			//The hold was succesful, tell the bank to post the transaction
			sys.setWasPaymentPosted(sys.getUserData().getBank().postTransaction(data.getNumber(), holdNumber, sys.getReceiptPrice()));
			sys.updatePayStatusGUI();
			sys.getUserData().getBank().releaseHold(data.getNumber(), holdNumber);
			sys.payWindowMessage("Your card has been charged: " + sys.getReceiptPrice());
			sys.resetReceiptPrice();
		}
	}

	@Override
	public void cardTapped(CardReader reader) {
		// TODO Auto-generated method stub
		System.out.println("Card Tapped ");

		
	}

	@Override
	public void cardSwiped(CardReader reader) {
		// TODO Auto-generated method stub
		System.out.println("Card Swipped");
		
	}
}
