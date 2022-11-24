package com.diy.software.system;

import ca.ucalgary.seng300.simulation.SimulationException;

import com.jimmyselectronics.Item;
import com.jimmyselectronics.necchi.Barcode;
import com.jimmyselectronics.necchi.BarcodedItem;

public class CustomerBag extends Item {
    /**
     * Basic constructor for a customer bag.
     *
     * @param barcode       The barcode representing the identifier of the product of which
     *                      this is an item.
     * @param weightInGrams The real weight of the item.
     */
    public CustomerBag(double weightInGrams) {
        super(weightInGrams);
    }
}
