package com.globant.iot.drinkgadget.listeners;


import android.content.Context;

import com.globant.iot.drinkgadget.mvp.events.BeanConnectedEvent;
import com.globant.iot.drinkgadget.mvp.events.BeanDisconnectedEvent;
import com.globant.iot.drinkgadget.mvp.events.BeanInfoReceivedEvent;
import com.globant.iot.drinkgadget.utils.BusProvider;
import com.globant.iot.drinkgadget.utils.DrinkPreferences;
import com.globant.iot.drinkgadget.utils.Notifications;
import com.punchthrough.bean.sdk.Bean;
import com.punchthrough.bean.sdk.BeanListener;
import com.punchthrough.bean.sdk.message.BeanError;
import com.punchthrough.bean.sdk.message.Callback;
import com.punchthrough.bean.sdk.message.DeviceInfo;
import com.punchthrough.bean.sdk.message.ScratchBank;

public class ConnectBeanListener implements BeanListener {

    Bean bean;
    Context context;
    DrinkPreferences preferences;

    public ConnectBeanListener(Bean bean, Context context, DrinkPreferences preferences) {
        this.bean = bean;
        this.context = context;
        this.preferences = preferences;
    }

    @Override
    public void onConnected() {
        System.out.println("onConnected");

        BusProvider.getInstance().post(new BeanConnectedEvent(bean));

        bean.readDeviceInfo(new Callback<DeviceInfo>() {
            @Override
            public void onResult(DeviceInfo deviceInfo) {
//                System.out.println(deviceInfo.hardwareVersion());
//                System.out.println(deviceInfo.firmwareVersion());
//                System.out.println(deviceInfo.softwareVersion());
            }
        });

    }

    @Override
    public void onConnectionFailed() {
        System.out.println("onConnectionFailed");
    }

    @Override
    public void onDisconnected() {
        System.out.println("onDisconnected");
        BusProvider.getInstance().post(new BeanDisconnectedEvent(bean));
    }

    @Override
    public void onSerialMessageReceived(byte[] data) {
        System.out.println("onSerialMessageReceived");
        if (data == null || data.length < 2) {
            return;
        }
        final byte temperature = data[0];
        final byte battery = data[1];

        if (temperature < preferences.getNotificationFreezeTemperature()) {
            Notifications.showNotificationAlmostFrozen(context, bean.getDevice().getAddress());
        } else if (temperature < preferences.getNotificationTemperature()) {
            Notifications.showNotificationDrinkReady(context, bean.getDevice().getAddress());
        }
        BusProvider.getInstance().post(new BeanInfoReceivedEvent(bean.getDevice().getAddress(), temperature, battery));
    }

    @Override
    public void onScratchValueChanged(ScratchBank bank, byte[] value) {
        System.out.println("onScratchValueChanged");
    }

    @Override
    public void onError(BeanError error) {
        System.out.println("onError");
    }

    @Override
    public void onReadRemoteRssi(int i) {
        System.out.println("onReadRemoteRssi");
    }
    // In practice you must implement the other Listener methods
    //...

}
