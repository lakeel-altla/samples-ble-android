package com.altla.ble.advertise.battery.drain;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.BeaconTransmitter;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class AdvertiseService extends Service {

    private static final String TAG = AdvertiseService.class.getSimpleName();

    private static final String namespaceId = "2f234454cf6d4a0fadf2";

    private static final String instanceId = "aabbccddeeff";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");

        Beacon beacon = new Beacon.Builder()
                .setId1(namespaceId)
                .setId2(instanceId)
                .build();

        BeaconParser beaconParser = new BeaconParser()
                .setBeaconLayout(BeaconParser.EDDYSTONE_UID_LAYOUT);

        BeaconTransmitter beaconTransmitter = new BeaconTransmitter(getApplicationContext(), beaconParser);
        beaconTransmitter.startAdvertising(beacon);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
        builder.setContentTitle(getApplicationContext().getResources().getString(R.string.notification_title));
        builder.setContentText(getApplicationContext().getResources().getString(R.string.notification_content));
        builder.setSmallIcon(android.R.drawable.ic_lock_idle_alarm);

        startForeground(1, builder.build());
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        return null;
    }
}
