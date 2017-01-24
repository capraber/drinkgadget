package com.globant.iot.drinkgadget.mvp.view;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.globant.iot.drinkgadget.BuildConfig;
import com.globant.iot.drinkgadget.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DrawerView extends ActivityView {

    private static final int CELSIUS_MAX_VALUE = 10;
    @BindView(R.id.drawerLayout) DrawerLayout drawerLayout;
    @BindView(R.id.temp_selected) TextView num;
    @BindView(R.id.version) TextView version;
    @BindView(R.id.seekbar) SeekBar seekbar;
    @BindView(R.id.temperatureRadioGroup) RadioGroup temperatureRadioGroup;
    @BindView(R.id.celsius) RadioButton celsiusRadioBtn;
    @BindView(R.id.fahrenheit) RadioButton fahrenheitRadioBtn;

    private ActionBarDrawerToggle drawerToggle;

    public DrawerView(AppCompatActivity activity) {
        super(activity);

        ButterKnife.bind(this, activity);
        initToolbar();
    }

    public ActionBarDrawerToggle getDrawerToggle() {
        return drawerToggle;
    }

    private void initToolbar() {

        drawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout,
                    R.string.drawer_open, R.string.drawer_close);

        // Set the drawer toggle as the DrawerListener
        drawerLayout.addDrawerListener(drawerToggle);

        drawerLayout.invalidate();
        drawerToggle.syncState();

        version.setText(String.format(getActivity().getResources().getString(R.string.version), BuildConfig.VERSION_NAME));
        seekbar.setMax(CELSIUS_MAX_VALUE);
    }

    public SeekBar getSeekbar() {
        return seekbar;
    }

    public void setSeekbarProgress(int value) {
        seekbar.setProgress(value);
    }

    public void selectCelsiusRadioBtn() {
        celsiusRadioBtn.setSelected(true);
    }

    public void selectFahrenheitRadioBtn() {
        fahrenheitRadioBtn.setSelected(true);
    }

    public TextView getNum() {
        return num;
    }

    public RadioGroup getTemperatureRadioGroup() {
        return temperatureRadioGroup;
    }
}
