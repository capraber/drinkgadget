package com.globant.iot.drinkgadget.mvp.presenter;

import android.app.Activity;

import com.globant.iot.drinkgadget.R;
import com.globant.iot.drinkgadget.listeners.DiscoveryListener;
import com.globant.iot.drinkgadget.model.DeviceInfo;
import com.globant.iot.drinkgadget.mvp.events.BeanConnectedEvent;
import com.globant.iot.drinkgadget.mvp.events.BeanDisconnectedEvent;
import com.globant.iot.drinkgadget.mvp.events.BeanDiscoveredEvent;
import com.globant.iot.drinkgadget.mvp.events.DiscoveryCompleteEvent;
import com.globant.iot.drinkgadget.mvp.model.ScanModel;
import com.globant.iot.drinkgadget.mvp.view.ScanView;
import com.globant.iot.drinkgadget.utils.DrinkPreferences;
import com.punchthrough.bean.sdk.Bean;
import com.punchthrough.bean.sdk.BeanManager;
import com.squareup.otto.Subscribe;

import java.util.List;

public class ScanPresenter {

    public static final int TIMEOUT = 5;
    boolean notificationSent;
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
        //enable info container (TBD)
//        view.showTemperature();
//        view.showBattery();

    }

    @Subscribe
    public void onBeanDisconnected(BeanDisconnectedEvent event) {
        view.showPopup(view.getActivity().getString(R.string.widget_disconnected));
    }

//    @Subscribe
//    public void onBeanInfoReceived(BeanInfoReceivedEvent event) {
//    }

    public void initSearch() {
        if (!checkPermission()) {
            return;
        }
        view.hideSearchBtn();
        view.showRibbon();
        view.showRecyclerView();
        view.showSpinner();


        BeanManager.getInstance().setScanTimeout(TIMEOUT);  // Timeout in seconds, optional, default is 30 seconds
        BeanManager.getInstance().startDiscovery(new DiscoveryListener());

    }

}
