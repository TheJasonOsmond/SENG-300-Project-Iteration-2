package com.diy.software.tests;
import com.diy.hardware.DoItYourselfStationAR;
import com.diy.software.system.DIYSystem;
import com.diy.software.system.ReceiptPrinterObserver;
import com.jimmyselectronics.EmptyException;
import com.jimmyselectronics.OverloadException;
import com.jimmyselectronics.abagnale.ReceiptPrinterD;
import org.junit.Before;
import org.junit.Test;

public class printReceiptTests {

    private DIYSystem system;
    private DoItYourselfStationAR station;
    private ReceiptPrinterObserver listener;
    private ReceiptPrinterD printer;

    @Before
    public void setUp() throws Exception {
        station = new DoItYourselfStationAR();
        printer = new ReceiptPrinterD();

        station.plugIn();
        station.turnOn();

        station.printer.plugIn();
        station.printer.turnOn();
        station.printer.enable();
    }

    @Test
    public void fullPaperAndInk() throws OverloadException, EmptyException {
        station.printer.addInk(10);
        station.printer.addPaper(10);
        station.printer.print('c');
    }

    @Test
    public void noPaper() throws OverloadException, EmptyException {

    }

    @Test
    public void noInk() throws OverloadException, EmptyException {

    }

    @Test
    public void bothNone() throws OverloadException, EmptyException {

    }

    @Test
    public void lowInk() throws OverloadException, EmptyException {

    }

    @Test
    public void lowPaper() throws OverloadException, EmptyException {

    }

    @Test
    public void bothLow() throws OverloadException, EmptyException {

    }

}
