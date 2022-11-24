package com.diy.software.tests;
import com.diy.hardware.DoItYourselfStationAR;
import com.diy.software.system.AttendantStation;
import com.diy.software.system.BagDispenser;
import com.diy.software.system.CustomerData;
import com.diy.software.system.DIYSystem;
import com.jimmyselectronics.EmptyException;
import com.jimmyselectronics.OverloadException;
import com.jimmyselectronics.abagnale.ReceiptPrinterD;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AddBagsTests {
    private DIYSystem system;
    private DoItYourselfStationAR station;
    private AttendantStation attendant;
    private CustomerData testCustomerData;

    private BagDispenser bag_dispenser;

    @Before
    public void setUp() throws Exception {
        station = new DoItYourselfStationAR();

        station.plugIn();
        station.turnOn();

        station.scale.plugIn();
        station.scale.turnOn();
        station.scale.enable();

        station.scanner.plugIn();
        station.scanner.turnOn();
        station.scanner.enable();

        //Test system
        system = new DIYSystem(testCustomerData, attendant);
        testCustomerData = new CustomerData();
        attendant = new AttendantStation(new CustomerData[]{testCustomerData});

        system.systemEnable();
        system.addBag();

    }




}
