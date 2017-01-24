package com.globant.iot.drinkgadget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.globant.iot.drinkgadget.model.DeviceInfo;
import com.globant.iot.drinkgadget.mvp.model.DrinkDialogModel;
import com.globant.iot.drinkgadget.mvp.presenter.DrinkDialogPresenter;
import com.globant.iot.drinkgadget.mvp.view.DrinkDialogView;
import com.globant.iot.drinkgadget.utils.BusProvider;
import com.globant.iot.drinkgadget.utils.DrinkPreferences;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Context.MODE_PRIVATE;
import static com.globant.iot.drinkgadget.DrawerBaseActivity.DRINK_PREFERENCES;

public class DrinkDialog extends Dialog {
    private DrinkDialogPresenter presenter;
    private DeviceInfo device;
    private byte temperature;
    private byte batteryLevel;

    public DrinkDialog(Context context, DeviceInfo device, byte temperature, byte batteryLevel) {
        super(context);
        this.device = device;
        this.temperature = temperature;
        this.batteryLevel = batteryLevel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_drink);
        ButterKnife.bind(this);
        setTitle(null);
        setCancelable(false);

        presenter = new DrinkDialogPresenter(new DrinkDialogView(this), new DrinkDialogModel(device,  temperature,  batteryLevel),
                new DrinkPreferences(getContext().getSharedPreferences(DRINK_PREFERENCES, MODE_PRIVATE)));
    }


    @OnClick(R.id.dialog_buton_back)
    public void onBackClick() {
        dismiss();
    }

    @Override
    protected void onStart() {
        super.onStart();
        BusProvider.register(presenter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        BusProvider.unregister(presenter);
    }
}
