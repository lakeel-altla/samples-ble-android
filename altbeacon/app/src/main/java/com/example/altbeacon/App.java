package com.example.altbeacon;


import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;

import android.app.Application;
import android.util.Log;

public final class App extends Application implements BootstrapNotifier {

    private static final String TAG = App.class.getSimpleName();

    private static final String I_BEACON_LAYOUT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24";

    private static final String EDDYSTONE_EID_LAYOUT = "s:0-1=feaa,m:2-2=30,p:3-3:-41,i:4-11";

    @Override
    public void onCreate() {
        super.onCreate();

        // Create singleton instance.
        BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);

        // Beacon frame settings.
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout(I_BEACON_LAYOUT));
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout(BeaconParser.EDDYSTONE_UID_LAYOUT));
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout(BeaconParser.EDDYSTONE_TLM_LAYOUT));
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout(BeaconParser.EDDYSTONE_URL_LAYOUT));
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout(EDDYSTONE_EID_LAYOUT));
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout(BeaconParser.ALTBEACON_LAYOUT));

        // Background settings.
        // This restart is guaranteed to happen after reboot or after connecting/disconnecting the device to a charger.
        new RegionBootstrap(this, new Region("beacon-region", null, null, null));

        // Save device battery.
        // Simply constructing this class and holding a reference to it in your custom Application class
        // enables auto battery saving of about 60%.
        new BackgroundPowerSaver(this);
    }

    @Override
    public void didEnterRegion(Region region) {
        String uniqueId = region.getUniqueId();
        Log.i(TAG, "I just saw an beacon for the first time!Unique Id: " + uniqueId);
    }

    @Override
    public void didExitRegion(Region region) {
        String uniqueId = region.getUniqueId();
        Log.i(TAG, "I no longer see an beacon.Unique Id: " + uniqueId);
    }

    @Override
    public void didDetermineStateForRegion(int i, Region region) {
        String uniqueId = region.getUniqueId();
        Log.i(TAG, "I have just switched from seeing/not seeing beacons.Unique ID: " + uniqueId);
    }
}
