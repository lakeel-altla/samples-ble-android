package com.example.save_ble_consumption;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;

import android.app.Application;
import android.util.Log;

import java.util.UUID;

public final class BeaconApp extends Application implements BootstrapNotifier {

    private static final String TAG = BeaconApp.class.getSimpleName();

    // 10 seconds
    private static final long SCAN_PERIOD = 10000L;

    // 10 seconds
    private static final long BETWEEN_SCAN_PERIOD = 30000L;

    private static final String I_BEACON_LAYOUT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24";

    private static final String EDDYSTONE_EID_LAYOUT = "s:0-1=feaa,m:2-2=30,p:3-3:-41,i:4-11";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Scan period is " + SCAN_PERIOD);
        Log.d(TAG, "Scan interval is " + BETWEEN_SCAN_PERIOD);

        BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);

        // Set layouts of beacons.
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

        // Set scan period.
        beaconManager.setBackgroundMode(true);
        beaconManager.setBackgroundScanPeriod(SCAN_PERIOD);
        beaconManager.setBackgroundBetweenScanPeriod(BETWEEN_SCAN_PERIOD);

        // Start Service in background.
        String id = UUID.randomUUID().toString();

        Log.d(TAG, "Start a service in background.Process id =" + id);

        new RegionBootstrap(this, new Region(id, null, null, null));
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
