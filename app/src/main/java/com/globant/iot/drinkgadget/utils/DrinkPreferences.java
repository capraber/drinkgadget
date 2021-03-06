package com.globant.iot.drinkgadget.utils;

import android.content.SharedPreferences;

public class DrinkPreferences {

    private static final String CELSIUS_KEY = "celsius_key";
    private static final String NOTIFICATION_FREEZE_KEY = "freeze_key";
    private static final String NOTIFICATION_TEMPERATURE_KEY = "notification_temperature_key";
    private static final byte TEMPERATURE_SELECTED = 4;
    private static final byte FREEZE_TEMPERATURE_SELECTED = 0;

    private SharedPreferences preferences;


    public DrinkPreferences(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    public boolean isCelsius() {
        return preferences.getBoolean(CELSIUS_KEY, true);
    }

    public void setCelsius(boolean isCelsius) {
        preferences.edit().putBoolean(CELSIUS_KEY, isCelsius).apply();
    }

    public void setNotificationTemperature(byte temperature) {
        preferences.edit().putInt(NOTIFICATION_TEMPERATURE_KEY, temperature).apply();
    }

    public int getNotificationTemperature() {
        return preferences.getInt(NOTIFICATION_TEMPERATURE_KEY, TEMPERATURE_SELECTED);
    }


    public void setNotificationFreezeTemperature(byte temperature) {
        preferences.edit().putInt(NOTIFICATION_TEMPERATURE_KEY, temperature).apply();
    }

    public int getNotificationFreezeTemperature() {
        return preferences.getInt(NOTIFICATION_FREEZE_KEY, FREEZE_TEMPERATURE_SELECTED);
    }
}
