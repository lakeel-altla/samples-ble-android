package com.example.altbeacon.fragment;

import com.example.altbeacon.R;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.Region;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * This fragment is used to monitor the beacons.
 */
public final class MonitorFragment extends Fragment implements BeaconConsumer {

    private static final String TAG = MonitorFragment.class.getSimpleName();

    private BeaconManager mBeaconManager;

    public static MonitorFragment newInstance() {
        return new MonitorFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mBeaconManager = BeaconManager.getInstanceForApplication(getApplicationContext());
        mBeaconManager.bind(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(TAG);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBeaconManager.unbind(this);
    }

    @Override
    public void onBeaconServiceConnect() {

        // Below callback methods are just monitor region, and can't get beacon instance from below callback methods.
        // To get beacon instance, you should use BeaconManager#addRangeNotifier() method.
        mBeaconManager.addMonitorNotifier(new MonitorNotifier() {

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
            public void didDetermineStateForRegion(int state, Region region) {
                String uniqueId = region.getUniqueId();
                Log.i(TAG, "I have just switched from seeing/not seeing beacons.Unique ID: " + uniqueId);
            }
        });
        
        try {
            // Start monitoring any beacons(iBeacon/Eddystone/AltBeacon). To see App class.
            mBeaconManager.startMonitoringBeaconsInRegion(new Region("beacon-region", null, null, null));
        } catch (RemoteException e) {
            Log.e(TAG, "Failed to start monitoring beacon in region", e);
        }
    }

    @Override
    public Context getApplicationContext() {
        return getActivity().getApplicationContext();
    }

    @Override
    public void unbindService(ServiceConnection serviceConnection) {
        getActivity().unbindService(serviceConnection);
    }

    @Override
    public boolean bindService(Intent intent, ServiceConnection serviceConnection, int i) {
        return getActivity().bindService(intent, serviceConnection, i);
    }
}
