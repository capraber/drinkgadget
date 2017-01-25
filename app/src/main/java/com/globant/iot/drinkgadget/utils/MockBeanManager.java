package com.globant.iot.drinkgadget.utils;

import android.content.Context;
import android.os.Handler;

import com.globant.iot.drinkgadget.mvp.events.BeanInfoReceivedEvent;

import static android.content.Context.MODE_PRIVATE;
import static com.globant.iot.drinkgadget.DrawerBaseActivity.DRINK_PREFERENCES;

public class MockBeanManager {
    public static final int DELAY_MILLIS = 500;
    public static final int TEMPERATURE = 25;
    public static final int BATTERY = 40;
    Handler handler;
    String address;
    int temperature = TEMPERATURE;
    int battery = BATTERY;
    Context context;

    private Runnable runnableTask = new Runnable() {


        @Override
        public void run() {
            temperature--;
            battery--;
            if (temperature == 0) {
                return;
            }

            DrinkPreferences preferences = new DrinkPreferences(context.getSharedPreferences(DRINK_PREFERENCES, MODE_PRIVATE));
            if (temperature < preferences.getNotificationFreezeTemperature()) {
                Notifications.showNotificationAlmostFrozen(context, address);
            } else if (temperature < preferences.getNotificationTemperature()) {
                Notifications.showNotificationDrinkReady(context, address);
            }

            BusProvider.getInstance().post(new BeanInfoReceivedEvent(address, (byte) temperature, (byte) battery));

            handler.postDelayed(this, DELAY_MILLIS);
        }
    };

    public void start(final String address, Context context) {
        this.address = address;
        this.context = context;
        handler = new Handler();
        handler.post(runnableTask);


    }

}
