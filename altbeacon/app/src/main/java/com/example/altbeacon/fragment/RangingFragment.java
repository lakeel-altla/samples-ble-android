package com.example.altbeacon.fragment;

import com.example.altbeacon.R;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.utils.UrlBeaconUrlCompressor;

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

import java.util.Collection;

/**
 * This fragment is used to range the beacons.
 */
public final class RangingFragment extends Fragment implements BeaconConsumer {

    private static final String TAG = RangingFragment.class.getSimpleName();

    private BeaconManager mBeaconManager;

    public static RangingFragment newInstance() {
        return new RangingFragment();
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
        mBeaconManager.addRangeNotifier(new RangeNotifier() {

            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    for (Beacon beacon : beacons) {
                        Log.i(TAG, "The first beacon(" + beacon.getIdentifier(0).toHexString() + ") I see is about " + beacon.getDistance() + " meters away.");

                        if (beacon.getServiceUuid() == 0xfeaa && beacon.getBeaconTypeCode() == 0x00) {
                            // This is a Eddystone-UID frame
                            Identifier namespaceId = beacon.getId1();
                            Identifier instanceId = beacon.getId2();

                            Log.d(TAG, "I see a beacon transmitting namespace id: " + namespaceId +
                                    " and instance id: " + instanceId +
                                    " approximately " + beacon.getDistance() + " meters away.");
                        } else if (beacon.getServiceUuid() == 0xfeaa && beacon.getBeaconTypeCode() == 0x20) {
                            // This is a Eddystone-TLM frame
                        } else if (beacon.getServiceUuid() == 0xfeaa && beacon.getBeaconTypeCode() == 0x10) {
                            // This is a Eddystone-URL frame
                            String url = UrlBeaconUrlCompressor.uncompress(beacon.getId1().toByteArray());
                            Log.d(TAG, "I see a beacon transmitting a url: " + url +
                                    " approximately " + beacon.getDistance() + " meters away.");
                        } else if (beacon.getServiceUuid() == 0xfeaa && beacon.getBeaconTypeCode() == 0x30) {
                            // This is a Eddystone-EID frame
                            Identifier ephemeralId = beacon.getId1();
                            Log.d(TAG, "I see a beacon transmitting ephemeral id: " + ephemeralId +
                                    " approximately " + beacon.getDistance() + " meters away.");
                        }
                    }
                }
            }
        });

        try {
            // Start ranging any beacons(iBeacon/Eddystone/AltBeacon). To see App class.
            mBeaconManager.startRangingBeaconsInRegion(new Region("uniqueId", null, null, null));
        } catch (RemoteException e) {
            Log.e(TAG, "Failed to start ranging beacons", e);
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
