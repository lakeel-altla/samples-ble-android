package com.altla.ble.advertise.battery.drain;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.BeaconTransmitter;

import android.content.Context;

import java.util.Timer;
import java.util.TimerTask;

final class BleAdvertiser {

    private static final long ADVERTISE_PERIOD = 10000;

    private static final long ADVERTISE_INTERVAL_PERIOD = 10000;

    private static final String namespaceId = "2f234454cf6d4a0fadf2";

    private static final String instanceId = "aabbccddeeff";

    private BeaconTransmitter mTransmitter;

    private Beacon mBeacon;

    BleAdvertiser(Context context) {
        BeaconParser beaconParser = new BeaconParser()
                .setBeaconLayout(BeaconParser.EDDYSTONE_UID_LAYOUT);

        mTransmitter = new BeaconTransmitter(context, beaconParser);
        mBeacon = new Beacon.Builder()
                .setId1(namespaceId)
                .setId2(instanceId)
                .build();
    }

    void start() {
        advertiseForPeriod();
    }

    private void advertiseForPeriod() {
        mTransmitter.startAdvertising(mBeacon);

        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                timer.cancel();
                mTransmitter.stopAdvertising();
                retransmit();
            }
        }, ADVERTISE_PERIOD, ADVERTISE_PERIOD);
    }

    private void retransmit() {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                timer.cancel();
                start();
            }
        }, ADVERTISE_INTERVAL_PERIOD, ADVERTISE_INTERVAL_PERIOD);
    }
}
