package com.diy.software.system;

import com.jimmyselectronics.AbstractDevice;
import com.jimmyselectronics.AbstractDeviceListener;
import com.jimmyselectronics.OverloadException;
import com.jimmyselectronics.virgilio.ElectronicScale;
import com.jimmyselectronics.virgilio.ElectronicScaleListener;

public class ElectronicScaleObserver implements ElectronicScaleListener {

	
	private DIYSystem sysRef;
	private AttendantStation attendant;
	
	public ElectronicScaleObserver(DIYSystem s) {
		this.sysRef = s;

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
		double expected_weight = sysRef.getCurrentExpectedWeight();
		double current_weight = 0;
		current_weight = expected_weight + weightInGrams;
		//double current_weight = baggingArea.getCurrentWeight();

		if (expected_weight < current_weight) {
			//Station to disabled scanning
			//sysRef.;
			//GUI to disable scanning and bagging
			sysRef.systemDisable();
			//Signal attendant to help
			//attendant.notifyWeightChange();

		} else if (expected_weight == current_weight) {
			//station.scanner.enable();
			sysRef.enableScanningAndBagging();
			sysRef.bagItemSuccess(true);
			sysRef.updateWeightOnGUI(weightInGrams);
			sysRef.setScanStatus(false);
		}
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
