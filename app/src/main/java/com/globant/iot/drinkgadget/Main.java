package com.globant.iot.drinkgadget;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.globant.iot.drinkgadget.mvp.model.ScanModel;
import com.globant.iot.drinkgadget.mvp.presenter.ScanPresenter;
import com.globant.iot.drinkgadget.mvp.view.ScanView;
import com.globant.iot.drinkgadget.utils.BusProvider;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class Main extends AppCompatActivity {

    ScanPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        if (presenter == null) {
            presenter = new ScanPresenter(new ScanModel(), new ScanView(this));
        }
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

    @OnClick(R.id.buttonSearch)
    public void onButonSearchClick() {
        presenter.initSearch();
    }
}
