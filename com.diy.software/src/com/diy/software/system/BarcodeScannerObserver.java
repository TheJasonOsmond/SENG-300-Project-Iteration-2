package com.diy.software.system;

import com.diy.hardware.BarcodedProduct;
import com.diy.hardware.external.ProductDatabases;
import com.jimmyselectronics.AbstractDevice;
import com.jimmyselectronics.AbstractDeviceListener;
import com.jimmyselectronics.necchi.Barcode;
import com.jimmyselectronics.necchi.BarcodeScanner;
import com.jimmyselectronics.necchi.BarcodeScannerListener;

public class BarcodeScannerObserver implements BarcodeScannerListener {

	private DIYSystem sysRef;
	
	public BarcodeScannerObserver(DIYSystem s) {
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
	public void barcodeScanned(BarcodeScanner barcodeScanner, Barcode barcode) {
		
		//Tell the system the scan was GOOD
		sysRef.setScanStatus(true);
		
		//adding scanned product to Interface
		BarcodedProduct product = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(barcode);
		
		sysRef.updateExpectedWeight(product.getExpectedWeight());
		sysRef.changeReceiptPrice((double)product.getPrice());
		sysRef.updateGUIItemList(product.getDescription(), product.getExpectedWeight(), (double)product.getPrice());
		
		//sysRef.addScannedProductToGUI
		//window.addUIProduct(product);
		//adding scanned product description to interface
		//String desc = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(barcode).getDescription();
		//window.addUIItem(desc);
		
	}
}
