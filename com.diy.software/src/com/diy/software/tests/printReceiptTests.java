package com.diy.software.tests;
import com.diy.software.system.DIYSystem;
import com.jimmyselectronics.AbstractDevice;
import com.jimmyselectronics.AbstractDeviceListener;
import com.jimmyselectronics.abagnale.IReceiptPrinter;
import com.jimmyselectronics.abagnale.ReceiptPrinterD;
import com.jimmyselectronics.abagnale.ReceiptPrinterListener;
import org.junit.Before;
import org.junit.Test;

public class printReceiptTests {

    private DIYSystem testSystem;
    private ReceiptPrinterListener listener;
    private IReceiptPrinter printer;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void fullPaperAndInk() {

    }

    @Test
    public void noPaper() {

    }

    @Test
    public void noInk() {

    }

    @Test
    public void bothNone() {

    }

    @Test
    public void lowInk() {

    }

    @Test
    public void lowPaper() {

    }

    @Test
    public void bothLow() {

    }

    @Test
    public void disabled_filledInk() {

    }

    @Test
    public void disabled_filledPaper() {

    }

}
