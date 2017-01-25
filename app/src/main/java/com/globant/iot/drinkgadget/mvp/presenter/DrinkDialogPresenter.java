package com.globant.iot.drinkgadget.mvp.presenter;

import com.globant.iot.drinkgadget.mvp.events.BeanInfoReceivedEvent;
import com.globant.iot.drinkgadget.mvp.model.DrinkDialogModel;
import com.globant.iot.drinkgadget.mvp.view.DrinkDialogView;
import com.globant.iot.drinkgadget.utils.DrinkPreferences;
import com.squareup.otto.Subscribe;

public class DrinkDialogPresenter {
    public static final int TEN = 10;
    public static final int TWENTY = 20;
    private DrinkDialogView view;
    private DrinkDialogModel model;
    private DrinkPreferences preferences;

    public DrinkDialogPresenter(DrinkDialogView view, DrinkDialogModel model, DrinkPreferences preferences) {
        this.view = view;
        this.model = model;
        this.preferences = preferences;
        update();
    }

    private void update() {
        if (preferences.isCelsius()) {
            view.setCircleViewTemperatureCelsius(model.getTemperature());
        } else {
            view.setCircleViewTemperatureFahrenheit(model.getTemperature());
        }
        setTemperatureColor();

        view.setCircleViewBatteryLevel(model.getBatteryLevel());
        setBatteryColor();
    }

    private void setTemperatureColor() {
        if (model.getTemperature() > TWENTY) {
            view.setTemperatureColorRed();
        } else if (model.getTemperature() > TEN) {
            view.setTemperatureColorBlue();
        } else {
            view.setTemperatureColorBlue();
        }
    }

    private void setBatteryColor() {
        if (model.getBatteryLevel() > TEN) {
            view.setTemperatureColorRed();
        } else {
            view.setBatteryColorGreen();
        }
    }

    @Subscribe
    public void onBeanInfoReceived(BeanInfoReceivedEvent event) {
        if (!model.isValid(event.address)) {
            return;
        }
        model.setBatteryLevel(event.battery);
        model.setTemperature(event.temperature);
        update();

    }

}
