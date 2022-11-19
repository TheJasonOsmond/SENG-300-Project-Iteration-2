package com.diy.software.system;

import com.jimmyselectronics.AbstractDevice;
import com.jimmyselectronics.AbstractDeviceListener;
import com.jimmyselectronics.virgilio.ElectronicScale;
import com.jimmyselectronics.virgilio.ElectronicScaleListener;

public class ElectronicScaleObserver implements ElectronicScaleListener {

	
	private DIYSystem sysRef;
	
	public ElectronicScaleObserver(DIYSystem s) {
		sysRef = s;
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

	@Override
	public void weightChanged(ElectronicScale scale, double weightInGrams) {
		//SUCCESSFULL ON ADDING ITEM TO BAGGING AREA
		//UPDATE THE GUI THROUGH THE SYSTEM WITH THE NEW UPDATED WEIGHT
		//TODO Fix this observer class, as the system needs to be blocked when a weightChanged event happens.
		sysRef.bagItemSuccess(true);
		sysRef.updateWeightOnGUI(weightInGrams);
		sysRef.setScanStatus(false);
	}

	@Override
	public void overload(ElectronicScale scale) {
		// TODO Auto-generated method stub

	}

	@Override
	public void outOfOverload(ElectronicScale scale) {
		// TODO Auto-generated method stub
		
	}

}
