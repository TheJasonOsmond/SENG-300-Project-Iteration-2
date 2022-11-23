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
	
	public DIYSystem sysRef;
	
	public ReceiptPrinterObserver(DIYSystem s) {
		this.sysRef = s;
	}

	

	@Override
	public void outOfPaper(IReceiptPrinter printer) {
		//System.out.println("no paper");	
		
		sysRef.systemDisable();
		sysRef.attendant.sendAlert("Printer Out of Paper");
		sysRef.attendant.lowPaper();
		
	}

	@Override
	public void outOfInk(IReceiptPrinter printer) {
		//System.out.println("no ink");	
		
		sysRef.systemDisable();
		sysRef.attendant.sendAlert("Printer Out of Ink");
		sysRef.attendant.lowInk();
		
		
		
	}

	@Override
	public void lowInk(IReceiptPrinter printer) {
		//System.out.println("low ink");	
		
		sysRef.attendant.sendAlert("Printer Low on Ink");
		sysRef.attendant.lowPaper();
		
		
	}

	@Override
	public void lowPaper(IReceiptPrinter printer) {
		//System.out.println("low paper");	
		
		sysRef.attendant.sendAlert("Printer Low on Paper");
		sysRef.attendant.lowPaper();
		
	}

	@Override
	public void paperAdded(IReceiptPrinter printer) {
		//System.out.println("paper added");	
		sysRef.attendant.sendAlert("Paper added to Printer");
		
		//try printing again?
		// Make system enable again? - by attendant
		sysRef.systemEnable();
		
	}

	@Override
	public void inkAdded(IReceiptPrinter printer) {
		//System.out.println("ink added");	
		sysRef.attendant.sendAlert("Ink added to Printer");
		
		//try printing again?
		// Make system enable again? - by attendant
		sysRef.systemEnable();
		
	}
	
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

}
