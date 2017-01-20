package com.globant.iot.drinkgadget.mvp.presenter;

import com.globant.iot.drinkgadget.R;
import com.globant.iot.drinkgadget.mvp.model.ScanModel;
import com.globant.iot.drinkgadget.mvp.view.ScanView;
import com.globant.iot.drinkgadget.utils.Notifications;
import com.punchthrough.bean.sdk.Bean;
import com.punchthrough.bean.sdk.BeanDiscoveryListener;
import com.punchthrough.bean.sdk.BeanListener;
import com.punchthrough.bean.sdk.BeanManager;
import com.punchthrough.bean.sdk.message.BeanError;
import com.punchthrough.bean.sdk.message.Callback;
import com.punchthrough.bean.sdk.message.DeviceInfo;
import com.punchthrough.bean.sdk.message.ScratchBank;

import java.util.ArrayList;
import java.util.List;

public class ScanPresenter {

    private ScanModel model;
    private ScanView view;
    final List<Bean> beans = new ArrayList<>();
    boolean notification_sent;
    int rx_index = 0;
    boolean connected = false;
    byte[] rx_data = new byte[1024];
    byte temperatureSelected = 4;

    public ScanPresenter(ScanModel model, ScanView view) {

        this.model = model;
        this.view = view;
        init();
    }

    private void init() {
        beans.clear();
        view.showSearchBtn();
        view.hideSpinner();
        view.hideTemperature();
        view.hideBattery();
        notification_sent = false;
    }

    public void initSearch() {
        //FIXME: ver bien el tema si no esta conectado el blu tooth porq se cuelgA!!!!
        view.hideSearchBtn();
        view.showSpinner();

        BeanDiscoveryListener listener = new BeanDiscoveryListener() {
            @Override
            public void onBeanDiscovered(Bean bean, int rssi) {

                //System.out.println("Encontrado uno!!!");

                //add bean if not yet included
                boolean new_bean = true;
                for(int c = 0; c < beans.size(); c++) {
                    if(beans.get(c).getDevice().getAddress().equals(bean.getDevice().getAddress())) {
                        new_bean = false;
                        break;
                    }
                }

                if(new_bean)
                {
                    beans.add(bean);
                    System.out.println("Encontrado uno!!!");
                }

            }

            @Override
            public void onDiscoveryComplete() {
                // This is called when the scan times out, defined by the .setScanTimeout(int seconds) method

                for (Bean bean : beans) {
                    System.out.println(bean.getDevice().getName());   // "Bean"              (example)
                    System.out.println(bean.getDevice().getAddress());    // "B4:99:4C:1E:BC:75" (example)
                }

                //connect to first one detected
                if(beans.size() > 0) {
                    connect();
                }
                else {
                    //reset
                    init();
                    view.showPopup(view.getActivity().getString(R.string.error_message));
                }
            }
        };



        BeanManager.getInstance().setScanTimeout(5);  // Timeout in seconds, optional, default is 30 seconds
        BeanManager.getInstance().startDiscovery(listener);

    }

    private void connect()
    {
        // Assume we have a reference to the 'beans' ArrayList from above.
        final Bean bean = beans.get(0);//[0];

        //clear rx data
        rx_index = 0;

        BeanListener beanListener = new BeanListener() {

            @Override
            public void onConnected() {
                System.out.println("onConnected");
                view.hideSpinner();

                //enable info container (TBD)
                view.showTemperature();
                view.showBattery();

                bean.readDeviceInfo(new Callback<DeviceInfo>() {
                    @Override
                    public void onResult(DeviceInfo deviceInfo) {
                        System.out.println(deviceInfo.hardwareVersion());
                        System.out.println(deviceInfo.firmwareVersion());
                        System.out.println(deviceInfo.softwareVersion());
                    }
                });

                connected = true;
            }

            @Override
            public void onConnectionFailed() {
                System.out.println("onConnectionFailed");
            }

            @Override
            public void onDisconnected() {
                connected = false;

                System.out.println("onDisconnected");
                init();
                view.showPopup(view.getActivity().getString(R.string.widget_disconnected));
            }

            @Override
            public void onSerialMessageReceived(byte[] data)
            {
                System.out.println("onSerialMessageReceived");
                //System.out.println("Longitud del mensaje " + data.length);


                for(int c=0;c< data.length;c++)
                {
                    rx_data[rx_index++] = data[c];
                    if(2 == rx_index)
                    {
                        //get temperature
                        System.out.println("Temperature: " + Byte.toString(rx_data[0]));
                        //get battery level %
                        System.out.println("Battery: " + Byte.toString(rx_data[1]));
                        rx_index = 0;
                        byte temperature = rx_data[0];
                        byte battery_level = rx_data[1];
                        updateViewInfo(temperature, battery_level);

                        if(!notification_sent)
                        {
                            if(temperature <= temperatureSelected)
                            {
                                Notifications.showNotification(view.getContext(), view.getActivity().getString(R.string.drink_widget),
                                        view.getActivity().getString(R.string.ready_message));
                                notification_sent = true;
                            }
                        }
                        break;
                    }
                }


            }

            @Override
            public void onScratchValueChanged(ScratchBank bank, byte[] value)
            {
                System.out.println("onScratchValueChanged");
            }

            @Override
            public void onError(BeanError error)
            {
                System.out.println("onError");
            }

            @Override
            public void onReadRemoteRssi(int i) {
                System.out.println("onReadRemoteRssi");
            }
            // In practice you must implement the other Listener methods
            //...
        };

        // Assuming you are in an Activity, use 'this' for the context
        bean.connect(view.getActivity(), beanListener);
    }

    private void updateViewInfo(byte temperature, byte battery_level)
    {
        view.setTemperature(temperature);
        view.setBattery(battery_level);
        view.setCircleViewTemperature(temperature);
        view.setCircleViewBatteryLevel(battery_level);
    }
}
