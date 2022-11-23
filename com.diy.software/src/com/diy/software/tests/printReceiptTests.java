package com.diy.software.tests;
import com.diy.hardware.DoItYourselfStationAR;
import com.diy.software.system.DIYSystem;
import com.jimmyselectronics.EmptyException;
import com.jimmyselectronics.OverloadException;
import com.jimmyselectronics.abagnale.ReceiptPrinterD;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class printReceiptTests {

    private DIYSystem system;
    private DoItYourselfStationAR station;
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

        // Check if the result is same as input
        station.printer.cutPaper();
        assertEquals(station.printer.removeReceipt(), "c");
    }

    @Test (expected = EmptyException.class)
    public void noPaper() throws OverloadException, EmptyException {
        station.printer.addInk(10);
        station.printer.print('c');

        // If it sends alert [no paper]
    }

    @Test (expected = EmptyException.class)
    public void noInk() throws OverloadException, EmptyException {
        station.printer.addPaper(10);
        station.printer.print('c');

        // If it sends alert [no ink]
    }

    @Test (expected = EmptyException.class)
    public void bothNone() throws OverloadException, EmptyException {
        station.printer.print('c');

        // If it sends both alert [no ink], [no paper]
    }

    @Test
    public void lowInk() throws OverloadException, EmptyException {
        station.printer.addPaper(10);
        station.printer.addInk(1);
        station.printer.print('c');

        // If it sends alert [low ink]
    }

    @Test
    public void lowPaper() throws OverloadException, EmptyException {
        station.printer.addPaper(1);
        station.printer.addInk(10);
        station.printer.print('c');

        // If it sends alert [low paper]

    }

    @Test
    public void bothLow() throws OverloadException, EmptyException {
        station.printer.addPaper(1);
        station.printer.addInk(1);
        station.printer.print('c');

        // If it sends alert for both [low paper], [low ink]
    }

}
