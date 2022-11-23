package com.diy.software.system;

import ca.ucalgary.seng300.simulation.SimulationException;
import com.jimmyselectronics.necchi.Barcode;
import com.jimmyselectronics.necchi.BarcodedItem;

public class CustomerBag extends BarcodedItem {
    /**
     * Basic constructor.
     *
     * @param barcode       The barcode representing the identifier of the product of which
     *                      this is an item.
     * @param weightInGrams The real weight of the item.
     * @throws SimulationException If the barcode is null.
     * @throws SimulationException If the weight is &le;0.
     */
    public CustomerBag(Barcode barcode, double weightInGrams) {
        super(barcode, weightInGrams);
    }
}
