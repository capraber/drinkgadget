package com.globant.iot.drinkgadget.adapters;


import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.globant.iot.drinkgadget.R;
import com.globant.iot.drinkgadget.model.DeviceInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WidgetsDetectedAdapter extends RecyclerView.Adapter<WidgetsDetectedAdapter.WidgetDetectedViewHolder> {

    final List<DeviceInfo> infos;

    public WidgetsDetectedAdapter() {
        this.infos = new ArrayList<>();
    }

    public void add(final DeviceInfo info) {
        infos.add(info);
        notifyItemInserted(infos.size() - 1);
    }

    @Override
    public WidgetDetectedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_widget_detected, parent, false);

        return new WidgetDetectedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WidgetDetectedViewHolder holder, int position) {
        final DeviceInfo deviceInfo = infos.get(position);

        holder.deviceName.setText(deviceInfo.name);

        holder.status.setColorFilter(ContextCompat.getColor(holder.status.getContext(),
                deviceInfo.status == 1
                        ? R.color.green
                        : R.color.yellow
        ));

    }

    @Override
    public int getItemCount() {
        return infos != null ? infos.size() : 0;
    }

    static class WidgetDetectedViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.device_name) TextView deviceName;
        @BindView(R.id.device_status) ImageView status;

        public WidgetDetectedViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
