package com.globant.iot.drinkgadget.adapters;


import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.globant.iot.drinkgadget.BuildConfig;
import com.globant.iot.drinkgadget.DrinkDialog;
import com.globant.iot.drinkgadget.R;
import com.globant.iot.drinkgadget.adapters.WidgetsDetectedAdapter.DrinkViewHolder;
import com.globant.iot.drinkgadget.model.DeviceInfo;
import com.globant.iot.drinkgadget.utils.MockBeanManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WidgetsDetectedAdapter extends RecyclerView.Adapter<DrinkViewHolder> {

    final List<DeviceInfo> infos;

    public WidgetsDetectedAdapter() {
        this.infos = new ArrayList<>();
    }

    public void add(final DeviceInfo info) {
        infos.add(info);
        notifyItemInserted(infos.size() - 1);
        if (BuildConfig.BUILD_TYPE.equals("mock")) {
            new MockBeanManager().start(info.address);
        }

    }

    @Override
    public DrinkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_widget_detected, parent, false);

        return new DrinkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DrinkViewHolder holder, int position) {
        final DeviceInfo deviceInfo = infos.get(position);
        holder.setDrinkInfo(deviceInfo);

        holder.deviceName.setText(deviceInfo.name);

        int color = -1;
        if (deviceInfo.temperature > 20) {
            color = R.color.red;
        } else if (deviceInfo.temperature > 10) {
            color = R.color.yellow;
        } else if (deviceInfo.temperature < 10 && deviceInfo.temperature > 0) {
            color = R.color.blue;
        }

        if (color != -1) {
            holder.status.setColorFilter(ContextCompat.getColor(holder.status.getContext(), color));
        }

    }

    @Override
    public int getItemCount() {
        return infos != null ? infos.size() : 0;
    }

    public void removeAllItems() {
        infos.clear();
        notifyDataSetChanged();
    }

    public void update(String address, byte temperature, byte battery) {
        int position = 0;
        for (DeviceInfo info : infos) {
            if (info.address.equals(address)) {
                info.temperature = temperature;
                info.battery = battery;
                notifyItemChanged(position);
                return;
            }
            position++;
        }

    }

    static class DrinkViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.device_name) TextView deviceName;
        @BindView(R.id.device_status) ImageView status;
        DeviceInfo drinkInfo;

        public DrinkViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setDrinkInfo(DeviceInfo drinkInfo) {
            this.drinkInfo = drinkInfo;
        }

        @OnClick(R.id.device_status)
        public void onImageClick(View view) {
            new DrinkDialog(view.getContext(), drinkInfo).show();
        }

    }
}
