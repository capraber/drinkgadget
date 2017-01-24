package com.globant.iot.drinkgadget.listeners;


import com.globant.iot.drinkgadget.mvp.events.BeanConnectedEvent;
import com.globant.iot.drinkgadget.mvp.events.BeanDisconnectedEvent;
import com.globant.iot.drinkgadget.mvp.events.BeanInfoReceivedEvent;
import com.globant.iot.drinkgadget.utils.BusProvider;
import com.punchthrough.bean.sdk.Bean;
import com.punchthrough.bean.sdk.BeanListener;
import com.punchthrough.bean.sdk.message.BeanError;
import com.punchthrough.bean.sdk.message.Callback;
import com.punchthrough.bean.sdk.message.DeviceInfo;
import com.punchthrough.bean.sdk.message.ScratchBank;

public class ConnectBeanListener implements BeanListener {

    public static final int BYTE = 1024;

    Bean bean;

    public ConnectBeanListener(Bean bean) {
        this.bean = bean;
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
        final String temperature = Byte.toString(data[0]);
        final String battery = Byte.toString(data[1]);

        BusProvider.getInstance().post(new BeanInfoReceivedEvent(bean, temperature, battery));

//        for (int c = 0; c < data.length; c++) {
//            rxData[rxIndex++] = data[c];
//            if (2 == rxIndex) {
//                //get temperature
//                System.out.println("Temperature: " + Byte.toString(rxData[0]));
//                //get battery level %
//                System.out.println("Battery: " + Byte.toString(rxData[1]));
//                rxIndex = 0;
//                byte temperature = rxData[0];
//                byte batteryLevel = rxData[1];
//                updateViewInfo(temperature, batteryLevel);
//
//                if (!notificationSent) {
//                    if (temperature <= preferences.getNotificationTemperature()) {
//                        Notifications.showNotification(view.getContext(), view.getActivity().getString(R.string.drink_widget),
//                                view.getActivity().getString(R.string.ready_message));
//                        notificationSent = true;
//                    }
//                }
//                break;
//            }
//        }


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