package com.diy.software.system;

import com.jimmyselectronics.AbstractDevice;
import com.jimmyselectronics.AbstractDeviceListener;
import com.jimmyselectronics.OverloadException;
import com.jimmyselectronics.virgilio.ElectronicScale;
import com.jimmyselectronics.virgilio.ElectronicScaleListener;

public class ElectronicScaleObserver implements ElectronicScaleListener {

	
	private DIYSystem sysRef;
	private AttendantStation attendantRef;
	
	public ElectronicScaleObserver(DIYSystem s, AttendantStation a) {
		this.sysRef = s;
		this.attendantRef = a;

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
		//UPDATE THE GUI THROUGH THE SYSTEM WITH THE NEW UPDATED WEIGHT

		double expected_weight = sysRef.getCurrentExpectedWeight();
		double current_weight = 0;
		current_weight = expected_weight + weightInGrams;

		if (expected_weight < current_weight || expected_weight > current_weight) {
			//Disable scanning and bagging
			sysRef.systemDisable();
			sysRef.sendMsgToGui("Please wait for attendant.");
			//Signal attendant to help
			attendantRef.notifyWeightChange();

		} else if (expected_weight == current_weight) {
			//This case is for successful bagging.
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
