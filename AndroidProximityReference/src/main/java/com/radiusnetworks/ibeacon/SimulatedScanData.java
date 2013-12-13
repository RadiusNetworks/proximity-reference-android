package com.radiusnetworks.ibeacon;

import java.util.ArrayList;
import java.util.List;

public class SimulatedScanData {
    public static List<IBeacon> iBeacons;
    /*
     * You may simulate detection of iBeacons by creating a class like this in your project in the com.radiusnetworks.ibeacon package.
     * Each iBeacon in the list will be detected by the system exactly once in each scan cycle.
     * This is especially useful for when you are testing in an Emulator or on a device without BluetoothLE capability.
     *
     * Note that by default in this demo, this is disabled.  change the line below from false to true to enable it.
     */
    public static boolean USE_SIMULATED_IBEACONS = true;

    static {
        iBeacons = new ArrayList<IBeacon>();

        if (USE_SIMULATED_IBEACONS) {
            IBeacon iBeacon1 = new IBeacon("842af9c4-08f5-11e3-9282-f23c91aec05e",
                    65000, 65001, -55, -55);
            IBeacon iBeacon2 = new IBeacon("842af9c4-08f5-11e3-9282-f23c91aec05e",
                    65000, 65002, -55, -55);
            iBeacons.add(iBeacon1);
            iBeacons.add(iBeacon2);
        }
    }
}

