package com.globant.iot.drinkgadget;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.globant.iot.drinkgadget.mvp.model.DrawerModel;
import com.globant.iot.drinkgadget.mvp.presenter.DrawerPresenter;
import com.globant.iot.drinkgadget.mvp.view.DrawerView;
import com.globant.iot.drinkgadget.utils.BusProvider;
import com.globant.iot.drinkgadget.utils.DrinkPreferences;

import butterknife.ButterKnife;

public abstract class DrawerBaseActivity extends AppCompatActivity {

    public static final String DRINK_PREFERENCES = "DRINK_PREFERENCES";
    private DrawerPresenter drawerPresenter;
    DrinkPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_base);
        getLayoutInflater().inflate(getLayout(), (RelativeLayout) findViewById(R.id.mainContent));
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getToolbarTitle());

        createDrawerPresenter();
    }

    private void createDrawerPresenter() {
        if (drawerPresenter == null) {
            preferences = new DrinkPreferences(getSharedPreferences(DRINK_PREFERENCES, MODE_PRIVATE));
            drawerPresenter = new DrawerPresenter(new DrawerView(this), new DrawerModel(), preferences);
        }
    }

    @StringRes
    public abstract int getToolbarTitle();

    @LayoutRes
    public abstract int getLayout();

    @Override
    protected void onResume() {
        super.onResume();
        BusProvider.register(drawerPresenter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        BusProvider.unregister(drawerPresenter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return (drawerPresenter.getDrawerToggle().onOptionsItemSelected(item)) || super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerPresenter.getDrawerToggle().syncState();
    }
}
