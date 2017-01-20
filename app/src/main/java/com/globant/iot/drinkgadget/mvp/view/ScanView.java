package com.globant.iot.drinkgadget.mvp.view;

import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.globant.iot.drinkgadget.Main;
import com.globant.iot.drinkgadget.R;

import at.grabner.circleprogress.CircleProgressView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ScanView extends ActivityView<Main> {

    @BindView(R.id.buttonSearch) Button searchBtn;
    @BindView(R.id.progressBar) ProgressBar spinner;
    @BindView(R.id.circleViewTemperature) CircleProgressView mCircleViewTemperature;
    @BindView(R.id.circleViewBatteryLevel) CircleProgressView mCircleViewBatteryLevel;
    @BindView(R.id.textViewTemperature) TextView tempText;
    @BindView(R.id.textViewBatteryLevel) TextView batteryText;
    boolean convertToFahrenheit = !true;

    public ScanView(Main activity) {
        super(activity);
        ButterKnife.bind(this, activity);

        //circles
        mCircleViewTemperature.setMaxValue(100);
        //mCircleViewTemperature.setMinValue(-10);
        mCircleViewTemperature.setValue(0);
        mCircleViewTemperature.setUnit("°C");
        mCircleViewTemperature.setUnitVisible(false);

        mCircleViewBatteryLevel.setMaxValue(100);
        mCircleViewBatteryLevel.setValue(0);
        mCircleViewBatteryLevel.setUnit("%");
    }

    public void hideSearchBtn() {
        searchBtn.setVisibility(View.GONE);
    }

    public void showSearchBtn() {
        searchBtn.setVisibility(View.VISIBLE);
    }

    public void hideSpinner() {
        spinner.setVisibility(View.GONE);
    }

    public void showSpinner() {
        spinner.setVisibility(View.VISIBLE);
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

    public void setTemperature(byte temperature) {
        //update temperature info
        tempText.setText(Byte.toString(temperature) + "°C");
    }

    public void setBattery(byte battery_level) {
        //update battery level info
        batteryText.setText(Byte.toString(battery_level) + "%");
    }

    public void setCircleViewTemperature(byte temperature) {
        if(convertToFahrenheit) {
            mCircleViewTemperature.setUnit("°F");
            temperature = (byte)(1.8 * (float)temperature + 32);
        }
        else {
            mCircleViewTemperature.setUnit("°C");
        }
        mCircleViewTemperature.setUnitVisible(true);
        mCircleViewTemperature.setValue(temperature);
    }

    public void setCircleViewBatteryLevel(byte battery_level) {
        mCircleViewBatteryLevel.setValue(battery_level);
    }

    public void showPopup(String message)
    {
        Toast.makeText(getContext(), message,
                Toast.LENGTH_LONG).show();
    }
}