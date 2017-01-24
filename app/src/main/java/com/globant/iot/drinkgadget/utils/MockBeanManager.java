package com.globant.iot.drinkgadget.utils;

import android.os.Handler;

import com.globant.iot.drinkgadget.mvp.events.BeanInfoReceivedEvent;

public class MockBeanManager {
    Handler handler;
    String address;
    int temperature = 20;
    int battery = 100;
    private Runnable runnableTask = new Runnable() {


        @Override
        public void run() {
            temperature--;
            battery--;
            if (temperature == 0) {
                return;
            }
            BusProvider.getInstance().post(new BeanInfoReceivedEvent(address, (byte) temperature, (byte) battery));

            handler.postDelayed(this, 500);
        }
    };

    public void start(final String address) {
        this.address = address;
        handler = new Handler();
        handler.post(runnableTask);


    }

}
