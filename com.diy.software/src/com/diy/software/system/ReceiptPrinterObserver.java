package com.diy.software.system;
import java.io.IOException;

import com.diy.hardware.DoItYourselfStationAR;
import com.jimmyselectronics.EmptyException;
import com.jimmyselectronics.OverloadException;
import com.jimmyselectronics.abagnale.ReceiptPrinterD;
import com.jimmyselectronics.abagnale.ReceiptPrinterListener;

public class ReceiptPrinterObserver {
    // Access the low ink and low paper
    private DoItYourselfStationAR station;
    private ReceiptPrinterD printer;
    private ReceiptPrinterListener listener;

    private double totalPrice;
    private boolean paymentMade = false;

    private void initialize() {
        station = new DoItYourselfStationAR();
        station.plugIn();
        station.turnOn();
        station.printer.enable();
    }

    // Simply copied from DIYSystem...
    private void printReceipt() {

        char[] receipt = new char[0];

        for (int c = 0; c < receipt.length-1; c++){
            try {
                station.printer.print(receipt[c]);
            } catch (EmptyException e){

            } catch (OverloadException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        // If payment is made, print receipt : Get total price
        // exception: out of paper / ink -> stop printing, display message to customer
        // suspend station
            //DIYSystem: systemDisable();
        // notify attendant, send duplicate receipt to print at attendant station

        station.printer.cutPaper();
        System.out.println(station.printer.removeReceipt());
    }
}
