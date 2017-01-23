package com.globant.iot.drinkgadget.mvp.view;

import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.globant.iot.drinkgadget.Main;
import com.globant.iot.drinkgadget.R;
import com.globant.iot.drinkgadget.utils.DrinkPreferences;
import com.globant.iot.drinkgadget.utils.Utils;

import at.grabner.circleprogress.CircleProgressView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ScanView extends ActivityView {

    public static final int TEMP_MAX_VALUE = 100;
    public static final int BATTERY_MAX_VALUE = 100;
    @BindView(R.id.buttonSearch) Button searchBtn;
    @BindView(R.id.progressBar) ProgressBar spinner;
    @BindView(R.id.circleViewTemperature) CircleProgressView mCircleViewTemperature;
    @BindView(R.id.circleViewBatteryLevel) CircleProgressView mCircleViewBatteryLevel;
    @BindView(R.id.textViewTemperature) TextView tempText;
    @BindView(R.id.textViewBatteryLevel) TextView batteryText;
    private DrinkPreferences preferences;

    public ScanView(Main activity, DrinkPreferences preferences) {
        super(activity);
        ButterKnife.bind(this, activity);
        this.preferences = preferences;

        //circles
        mCircleViewTemperature.setMaxValue(TEMP_MAX_VALUE);
        //mCircleViewTemperature.setMinValue(-10);
        mCircleViewTemperature.setValue(0);
        mCircleViewTemperature.setUnit(activity.getString(R.string.celsius));
        mCircleViewTemperature.setUnitVisible(false);

        mCircleViewBatteryLevel.setMaxValue(BATTERY_MAX_VALUE);
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
        if (preferences.isCelsius()) {
            tempText.setText(Byte.toString(temperature) + getActivity().getString(R.string.celsius));
        }  else {
            tempText.setText(Byte.toString(Utils.convertToFahrenheit(temperature)) + getActivity().getString(R.string.fahrenheit));
        }
    }

    public void setBattery(byte batteryLevel) {
        //update battery level info
        batteryText.setText(Byte.toString(batteryLevel) + "%");
    }

    public void setCircleViewTemperature(byte temperature) {
        if (preferences.isCelsius()) {
            mCircleViewTemperature.setUnit(getActivity().getString(R.string.celsius));
        }  else {
            mCircleViewTemperature.setUnit(getActivity().getString(R.string.fahrenheit));
            temperature = Utils.convertToFahrenheit(temperature);
        }
        mCircleViewTemperature.setUnitVisible(true);
        mCircleViewTemperature.setValue(temperature);
    }

    public void setCircleViewBatteryLevel(byte batteryLevel) {
        mCircleViewBatteryLevel.setValue(batteryLevel);
    }

    public void showPopup(String message) {
        Toast.makeText(getContext(), message,
                Toast.LENGTH_LONG).show();
    }
}
