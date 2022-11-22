package com.diy.software.system;

import com.jimmyselectronics.AbstractDevice;
import com.jimmyselectronics.AbstractDeviceListener;
import com.jimmyselectronics.abagnale.IReceiptPrinter;
import com.jimmyselectronics.abagnale.ReceiptPrinterListener;

/*
 * @author Dayee Lee
 * @author Benjamin Niles
 */
public class ReceiptPrinterObserver implements ReceiptPrinterListener{

	@Override
	public void enabled(AbstractDevice<? extends AbstractDeviceListener> device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disabled(AbstractDevice<? extends AbstractDeviceListener> device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void turnedOn(AbstractDevice<? extends AbstractDeviceListener> device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void turnedOff(AbstractDevice<? extends AbstractDeviceListener> device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void outOfPaper(IReceiptPrinter printer) {
		System.out.println("no paper");	
		
		//find some way to connect to AttendantStation
		//display message
		//enable addPaperButton
		
	}

	@Override
	public void outOfInk(IReceiptPrinter printer) {
		System.out.println("no ink");	
		
		//find some way to connect to AttendantStation
		//display message
		//enable addInkButton
		
		
	}

	@Override
	public void lowInk(IReceiptPrinter printer) {
		System.out.println("low ink");	
		
		//find some way to connect to AttendantStation
		//display message
		//enable addInkButton
		
		
	}

	@Override
	public void lowPaper(IReceiptPrinter printer) {
		System.out.println("low paper");	
		
		//find some way to connect to AttendantStation
		//display message
		//enable addPaperButton
		
	}

	@Override
	public void paperAdded(IReceiptPrinter printer) {
		System.out.println("paper added");	
		
		
	}

	@Override
	public void inkAdded(IReceiptPrinter printer) {
		System.out.println("ink added");	
		
		
		
	}

}
