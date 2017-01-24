package com.globant.iot.drinkgadget.mvp.view;

import android.app.Dialog;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.globant.iot.drinkgadget.R;
import com.globant.iot.drinkgadget.utils.Utils;

import at.grabner.circleprogress.CircleProgressView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DrinkDialogView extends DialogView {

    private static final int TEMP_MAX_VALUE = 100;
    private static final int BATTERY_MAX_VALUE = 100;
    @BindView(R.id.circleViewTemperature) CircleProgressView mCircleViewTemperature;
    @BindView(R.id.circleViewBatteryLevel) CircleProgressView mCircleViewBatteryLevel;
    @BindView(R.id.textViewTemperature) TextView tempText;
    @BindView(R.id.textViewBatteryLevel) TextView batteryText;
    @BindView(R.id.progressBar) ProgressBar progressBar;

    public DrinkDialogView(Dialog dialog) {
        super(dialog);
        ButterKnife.bind(this, dialog);

        //FIXME remose this line and add the necesary logic
        progressBar.setVisibility(View.GONE);

        //circles
        mCircleViewTemperature.setMaxValue(TEMP_MAX_VALUE);
        mCircleViewTemperature.setValue(0);
        mCircleViewTemperature.setUnitVisible(false);

        mCircleViewBatteryLevel.setMaxValue(BATTERY_MAX_VALUE);
        mCircleViewBatteryLevel.setValue(0);
    }

    public void hideTemperature() {
        mCircleViewTemperature.setVisibility(View.GONE);
        tempText.setVisibility(View.GONE);
    }

    public void showTemperature() {
        mCircleViewTemperature.setVisibility(View.VISIBLE);
        tempText.setVisibility(View.VISIBLE);
    }

    public void hideBattery() {
        mCircleViewBatteryLevel.setVisibility(View.GONE);
        batteryText.setVisibility(View.GONE);
    }

    public void showBattery() {
        mCircleViewBatteryLevel.setVisibility(View.VISIBLE);
        batteryText.setVisibility(View.VISIBLE);
    }

    public void setCircleViewTemperatureCelsius(byte temperature) {
        mCircleViewTemperature.setUnit(String.format(getContext().getString(R.string.temp_celsius), ""));
        mCircleViewTemperature.setUnitVisible(true);
        mCircleViewTemperature.setValue(temperature);
    }

    public void setCircleViewTemperatureFahrenheit(byte temperature) {
        mCircleViewTemperature.setUnit(String.format(getContext().getString(R.string.temp_fahrenheit), ""));
        temperature = Utils.convertToFahrenheit(temperature);
        mCircleViewTemperature.setUnitVisible(true);
        mCircleViewTemperature.setValue(temperature);
    }

    public void setCircleViewBatteryLevel(byte batteryLevel) {
        mCircleViewBatteryLevel.setValue(batteryLevel);
    }

}
