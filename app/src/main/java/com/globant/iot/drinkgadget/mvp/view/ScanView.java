package com.globant.iot.drinkgadget.mvp.view;

import android.Manifest;
import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.globant.iot.drinkgadget.Main;
import com.globant.iot.drinkgadget.R;
import com.globant.iot.drinkgadget.adapters.WidgetsDetectedAdapter;
import com.globant.iot.drinkgadget.model.DeviceInfo;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScanView extends ActivityView {


    private static final int REQUEST_LOCATION_CONTACTS = 1;
    @BindView(R.id.ribbonTextView) TextView ribbonTextView;
    @BindView(R.id.scanFloatingButton) FloatingActionButton searchButton;
    @BindView(R.id.widgetsRecyclerView) RecyclerView widgetsRecyclerView;
    @BindView(R.id.progressBar) ProgressBar spinner;
    WidgetsDetectedAdapter adapter;

    public ScanView(Main activity) {
        super(activity);
        ButterKnife.bind(this, activity);
        initRecyclerView();
    }

    private void initRecyclerView() {
        final Activity activity = getActivity();
        if (activity == null) {
            return;
        }
        widgetsRecyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        adapter = new WidgetsDetectedAdapter();
        widgetsRecyclerView.setAdapter(adapter);
    }

    public void hideRibbon() {
        ribbonTextView.setVisibility(View.GONE);
    }
    public void showRibbon() {
        ribbonTextView.setVisibility(View.VISIBLE);
    }
    public void showRecyclerView() {
        widgetsRecyclerView.setVisibility(View.VISIBLE);
    }
    public void hideSearchBtn() {
        searchButton.setVisibility(View.GONE);
    }

    public void showSearchBtn() {
        searchButton.setVisibility(View.VISIBLE);
    }

    public void hideSpinner() {
        spinner.setVisibility(View.GONE);
    }

    public void showSpinner() {
        spinner.setVisibility(View.VISIBLE);
    }


    public void showPopup(String message) {
        Toast.makeText(getContext(), message,
                Toast.LENGTH_LONG).show();
    }


    public void requestLocationPermission() {
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                REQUEST_LOCATION_CONTACTS);
    }

    public void addWidgetDetected(final DeviceInfo info) {
        adapter.add(info);
    }

    public void removeAllItems() {
        adapter.removeAllItems();
    }
}
