package com.altla.ble.advertise.battery.drain;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import java.util.UUID;

public class AdvertiseService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
        builder.setContentTitle(getApplicationContext().getResources().getString(R.string.notification_title));
        builder.setContentText(getApplicationContext().getResources().getString(R.string.notification_content));
        builder.setSmallIcon(android.R.drawable.ic_lock_idle_alarm);

        startForeground(UUID.randomUUID().variant(), builder.build());

        BleAdvertiser advertiser = new BleAdvertiser(getApplicationContext());
        advertiser.start();

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
