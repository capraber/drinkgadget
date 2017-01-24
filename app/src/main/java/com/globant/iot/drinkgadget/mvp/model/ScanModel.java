package com.globant.iot.drinkgadget.mvp.model;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import com.globant.iot.drinkgadget.listeners.ConnectBeanListener;
import com.punchthrough.bean.sdk.Bean;

import java.util.ArrayList;
import java.util.List;

public class ScanModel {

    public List<Bean> beans;

    public ScanModel() {
        beans = new ArrayList();
    }

    public boolean hasBluetoothEnabled() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        return adapter != null && adapter.isEnabled();
    }

    public boolean checkLocationPermission(final Context c) {
        return ContextCompat.checkSelfPermission(c, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public void addBeanDetected(Bean bean) {
        for (Bean b : beans) {
            if (b.getDevice().getAddress().equals(bean.getDevice().getAddress())) {
                return;
            }
        }
        beans.add(bean);
    }

    public void connect(final Activity activity, final Bean bean) {
        bean.connect(activity, new ConnectBeanListener(bean));
    }


}
