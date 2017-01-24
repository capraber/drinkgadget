package com.globant.iot.drinkgadget.listeners;


import com.globant.iot.drinkgadget.mvp.events.BeanDiscoveredEvent;
import com.globant.iot.drinkgadget.mvp.events.DiscoveryCompleteEvent;
import com.globant.iot.drinkgadget.utils.BusProvider;
import com.punchthrough.bean.sdk.Bean;
import com.punchthrough.bean.sdk.BeanDiscoveryListener;

public class DiscoveryListener implements BeanDiscoveryListener {
    @Override
    public void onBeanDiscovered(Bean bean, int rssi) {
        BusProvider.getInstance().post(new BeanDiscoveredEvent(bean));
    }

    @Override
    public void onDiscoveryComplete() {
        // This is called when the scan times out, defined by the .setScanTimeout(int seconds) method
        BusProvider.getInstance().post(new DiscoveryCompleteEvent());
    }

}
