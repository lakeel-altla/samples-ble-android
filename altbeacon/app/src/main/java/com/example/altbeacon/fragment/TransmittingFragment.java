package com.example.altbeacon.fragment;

import com.example.altbeacon.R;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.BeaconTransmitter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * This fragment is used to transmit as a beacon.
 */
public final class TransmittingFragment extends Fragment {

    public static TransmittingFragment newInstance() {
        return new TransmittingFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transmit, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.button_transmit)
    public void onTransmit() {
        // Transmit settings.
        Beacon beacon = new Beacon.Builder()
                .setId1("2f234454-cf6d-4a0f-adf2-f4911ba9ffa6")
                .setId2("1")
                .setId3("2")
                .setManufacturer(0x0118)
                .setTxPower(-59)
                .setDataFields(Collections.singletonList(0L))
                .build();

        // As a Eddystone UID.
        BeaconParser beaconParser = new BeaconParser()
                .setBeaconLayout(BeaconParser.EDDYSTONE_UID_LAYOUT);
        BeaconTransmitter beaconTransmitter = new BeaconTransmitter(getContext(), beaconParser);
        beaconTransmitter.startAdvertising(beacon);
    }

}
