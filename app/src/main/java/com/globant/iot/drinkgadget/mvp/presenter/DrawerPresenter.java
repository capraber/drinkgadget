package com.globant.iot.drinkgadget.mvp.presenter;

import android.support.v7.app.ActionBarDrawerToggle;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;

import com.globant.iot.drinkgadget.R;
import com.globant.iot.drinkgadget.mvp.model.DrawerModel;
import com.globant.iot.drinkgadget.mvp.view.DrawerView;
import com.globant.iot.drinkgadget.utils.DrinkPreferences;

public class DrawerPresenter {


    private DrawerView view;
    private DrawerModel model;
    private DrinkPreferences preferences;

    public DrawerPresenter(DrawerView view, DrawerModel model, DrinkPreferences preferences) {
        this.view = view;
        this.model = model;
        this.preferences = preferences;

        init(view);

    }

    private void init(final DrawerView view) {
        view.getSeekbar().setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                preferences.setNotificationTemperature((byte) i);
                if (!preferences.isCelsius()) {
                    i = DrawerView.FAHRENHEIT_MIN_VALUE + i;

                }

                String seekbarValue = String.valueOf(i);
                view.getNum().setText(seekbarValue);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        view.getTemperatureRadioGroup().setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if (checkedId == R.id.celsius) {
                    preferences.setCelsius(true);
                    view.setSeekbarCelsius();
                } else {
                    preferences.setCelsius(false);
                    view.setSeekBarFahrenheit();
                }

                view.setSeekbarProgress(preferences.getNotificationTemperature());
            }

        });

        int previousSelection = preferences.getNotificationTemperature();
        if (preferences.isCelsius()) {
            view.setSeekbarCelsius();
            view.selectCelsiusRadioBtn();
        } else {
            view.setSeekBarFahrenheit();
            view.selectFahrenheitRadioBtn();
        }

        view.setSeekbarProgress(previousSelection);
    }

    public ActionBarDrawerToggle getDrawerToggle() {
        return view.getDrawerToggle();
    }
}
