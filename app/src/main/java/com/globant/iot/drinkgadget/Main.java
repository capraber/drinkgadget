package com.globant.iot.drinkgadget;

import android.os.Bundle;

import com.globant.iot.drinkgadget.mvp.model.ScanModel;
import com.globant.iot.drinkgadget.mvp.presenter.ScanPresenter;
import com.globant.iot.drinkgadget.mvp.view.ScanView;
import com.globant.iot.drinkgadget.utils.BusProvider;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class Main extends DrawerBaseActivity {

    ScanPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);
        if (presenter == null) {
            presenter = new ScanPresenter(new ScanModel(), new ScanView(this), preferences);
        }
    }

    @Override
    public int getToolbarTitle() {
        return R.string.app_name;
    }

    @Override
    public int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void onPause() {
        super.onPause();
        BusProvider.unregister(presenter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        BusProvider.register(presenter);
    }

    @OnClick(R.id.scanFloatingButton)
    public void onButonSearchClick() {
        presenter.initSearch();
    }
}
