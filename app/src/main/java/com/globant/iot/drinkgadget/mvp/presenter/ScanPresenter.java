package com.globant.iot.drinkgadget.mvp.presenter;

import com.globant.iot.drinkgadget.R;
import com.globant.iot.drinkgadget.mvp.model.ScanModel;
import com.globant.iot.drinkgadget.mvp.view.ScanView;
import com.globant.iot.drinkgadget.utils.DrinkPreferences;
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

    public static final int BYTE = 1024;
    public static final int TIMEOUT = 5;
    final List<Bean> beans = new ArrayList<>();
    boolean notificationSent;
    int rxIndex = 0;
    boolean connected = false;
    byte[] rxData = new byte[BYTE];
    private DrinkPreferences preferences;
    private ScanModel model;
    private ScanView view;

    public ScanPresenter(ScanModel model, ScanView view, DrinkPreferences preferences) {

        this.model = model;
        this.view = view;
        this.preferences = preferences;
        init();
    }

    private void init() {
        beans.clear();
        view.hideSpinner();
        notificationSent = false;
        checkPermission();
    }

    private boolean checkPermission() {
        if (!model.hasBluetoothEnabled()) {
            view.showPopup(view.getActivity().getString(R.string.error_bluetooth_message));
            return false;
        }

        if (!model.checkLocationPermission(view.getActivity())) {
            view.requestLocationPermission();
            return false;
        }

        return true;
    }

    public void initSearch() {
        if (!checkPermission()) {
            return;
        }
        //FIXME: ver bien el tema si no esta conectado el blu tooth porq se cuelgA!!!!
        view.hideSearchBtn();
        view.showRibbon();
        view.showRecyclerView();
        view.showSpinner();

        BeanDiscoveryListener listener = new BeanDiscoveryListener() {
            @Override
            public void onBeanDiscovered(Bean bean, int rssi) {

                //System.out.println("Encontrado uno!!!");

                //add bean if not yet included
                boolean newBean = true;
                for (int c = 0; c < beans.size(); c++) {
                    if (beans.get(c).getDevice().getAddress().equals(bean.getDevice().getAddress())) {
                        newBean = false;
                        break;
                    }
                }

                if (newBean) {
                    beans.add(bean);
                    System.out.println("Encontrado uno!!!");
                }

            }

            @Override
            public void onDiscoveryComplete() {
//TODO hardcoding
                //TODO hardcoding
                com.globant.iot.drinkgadget.model.DeviceInfo info = new com.globant.iot.drinkgadget.model.DeviceInfo();
                info.name = "device 1";
                info.address = "B4:99:4C:1E:BC:75";
                info.status = 1;
                view.addWidgetDetected(info);
                info = new com.globant.iot.drinkgadget.model.DeviceInfo();
                info.name = "device 2";
                info.address = "B4:99:4C:1E:BC:75";
                info.status = 0;
                view.addWidgetDetected(info);
                info = new com.globant.iot.drinkgadget.model.DeviceInfo();
                info.name = "device 3";
                info.address = "B4:99:4C:1E:BC:75";
                info.status = 0;
                view.addWidgetDetected(info);


                // This is called when the scan times out, defined by the .setScanTimeout(int seconds) method

                for (Bean bean : beans) {
                    System.out.println(bean.getDevice().getName());   // "Bean"              (example)
                    System.out.println(bean.getDevice().getAddress());    // "B4:99:4C:1E:BC:75" (example)
                }

                //connect to first one detected
                if (beans.size() > 0) {
                    connect();
                } else {
                    //reset
                    init();
                    view.showPopup(view.getActivity().getString(R.string.error_message));
                }
            }
        };


        BeanManager.getInstance().setScanTimeout(TIMEOUT);  // Timeout in seconds, optional, default is 30 seconds
        BeanManager.getInstance().startDiscovery(listener);

    }

    private void connect() {
        // Assume we have a reference to the 'beans' ArrayList from above.
        final Bean bean = beans.get(0); //[0];

        //clear rx data
        rxIndex = 0;

        BeanListener beanListener = new BeanListener() {

            @Override
            public void onConnected() {
                System.out.println("onConnected");
                view.hideSpinner();

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
            public void onSerialMessageReceived(byte[] data) {
                System.out.println("onSerialMessageReceived");
                //System.out.println("Longitud del mensaje " + data.length);


                for (int c = 0; c < data.length; c++) {
                    rxData[rxIndex++] = data[c];
                    if (2 == rxIndex) {
                        //get temperature
                        System.out.println("Temperature: " + Byte.toString(rxData[0]));
                        //get battery level %
                        System.out.println("Battery: " + Byte.toString(rxData[1]));
                        rxIndex = 0;
                        byte temperature = rxData[0];
                        byte batteryLevel = rxData[1];
                        //FIXME send event for each bean
                        //updateViewInfo(temperature, batteryLevel);

                        if (!notificationSent) {
                            if (temperature <= preferences.getNotificationTemperature()) {
                                Notifications.showNotification(view.getContext(), view.getActivity().getString(R.string.drink_widget),
                                        view.getActivity().getString(R.string.ready_message));
                                notificationSent = true;
                            }
                        }
                        break;
                    }
                }


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
        };

        // Assuming you are in an Activity, use 'this' for the context
        bean.connect(view.getActivity(), beanListener);
    }
}
