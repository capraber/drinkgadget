package com.globant.iot.drinkgadget.mvp.presenter;

import android.app.Activity;

import com.globant.iot.drinkgadget.BuildConfig;
import com.globant.iot.drinkgadget.R;
import com.globant.iot.drinkgadget.listeners.DiscoveryListener;
import com.globant.iot.drinkgadget.model.DeviceInfo;
import com.globant.iot.drinkgadget.mvp.events.BeanConnectedEvent;
import com.globant.iot.drinkgadget.mvp.events.BeanDisconnectedEvent;
import com.globant.iot.drinkgadget.mvp.events.BeanDiscoveredEvent;
import com.globant.iot.drinkgadget.mvp.events.BeanInfoReceivedEvent;
import com.globant.iot.drinkgadget.mvp.events.DiscoveryCompleteEvent;
import com.globant.iot.drinkgadget.mvp.model.ScanModel;
import com.globant.iot.drinkgadget.mvp.view.ScanView;
import com.punchthrough.bean.sdk.Bean;
import com.punchthrough.bean.sdk.BeanManager;
import com.squareup.otto.Subscribe;

import java.util.List;

public class ScanPresenter {

    private static final int TIMEOUT = 5;
    boolean notificationSent;
    private ScanModel model;
    private ScanView view;

    public ScanPresenter(ScanModel model, ScanView view) {

        this.model = model;
        this.view = view;
        init();
    }

    private void init() {
        view.hideSpinner();
        view.showSearchBtn();
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

    @Subscribe
    public void onDiscovered(BeanDiscoveredEvent event) {
        model.addBeanDetected(event.bean);
    }

    @Subscribe
    public void onDiscoveryComplete(DiscoveryCompleteEvent event) {
        final Activity activity = view.getActivity();
        if (activity == null) {
            return;
        }
        view.hideSpinner();
        view.showSearchBtn();

        final List<Bean> beans = model.beans;
        if (beans == null || beans.isEmpty()) {
            view.showPopup(view.getActivity().getString(R.string.error_message));
            return;
        }
        for (Bean bean : beans) {
            model.connect(activity, bean);
        }
    }


    @Subscribe
    public void onBeanConnected(BeanConnectedEvent event) {
        DeviceInfo info = new DeviceInfo.Builder().add(event.bean.getDevice()).build();
        view.addWidgetDetected(info);
    }

    @Subscribe
    public void onBeanDisconnected(BeanDisconnectedEvent event) {
        view.showPopup(view.getActivity().getString(R.string.widget_disconnected));
    }

    @Subscribe
    public void onBeanInfoReceived(BeanInfoReceivedEvent event) {
        view.update(event.address, event.temperature, event.battery);

    }

    public void initSearch() {
        if (!checkPermission()) {
            return;
        }
        model.cleanBeans();
        view.removeAllItems();
        view.hideSearchBtn();
        view.showRibbon();
        view.showRecyclerView();
        view.showSpinner();

        final DiscoveryListener listener = new DiscoveryListener();
        if (!BuildConfig.BUILD_TYPE.equals("mock")) {
            BeanManager.getInstance().setScanTimeout(TIMEOUT);  // Timeout in seconds, optional, default is 30 seconds
            BeanManager.getInstance().startDiscovery(listener);
            return;
        }


        //TODO mock
        mockData(listener);
    }

    private void mockData(DiscoveryListener listener) {
        DeviceInfo info = new DeviceInfo();
        info.name = "botella Stella Artois";
        info.address = "B4:99:4C:1E:BC:71";
        info.status = 1;
        view.addWidgetDetected(info);
        info = new DeviceInfo();
        info.name = "botella Quilmes";
        info.address = "B4:99:4C:1E:BC:72";
        info.status = 0;
        view.addWidgetDetected(info);
        info = new DeviceInfo();
        info.name = "botella Heineken";
        info.address = "B4:99:4C:1E:BC:73";
        info.status = 0;
        view.addWidgetDetected(info);
        listener.onDiscoveryComplete();
    }
}

