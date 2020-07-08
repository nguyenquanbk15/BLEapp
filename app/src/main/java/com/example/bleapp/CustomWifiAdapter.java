package com.example.bleapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CustomWifiAdapter extends ArrayAdapter<Wifi> {
    private Context context;
    private int resource;
    private ArrayList<Wifi> arrWifi;

    public CustomWifiAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Wifi> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.arrWifi = objects;
    }

    public void UpdateWifiAdapter(ArrayList<Wifi> newList) {
        arrWifi.clear();
        arrWifi.addAll(newList);
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        ViewHolder viewHolder;

        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_wifi, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.tvName = convertView.findViewById(R.id.tv_item_wifi_name);
            viewHolder.tvLevel = convertView.findViewById(R.id.tv_custom_level);
            viewHolder.tvChannel = convertView.findViewById(R.id.tv_wifi_channel);
            viewHolder.tvEncryption = convertView.findViewById(R.id.tv_wifi_encode);

            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Wifi wifiList = arrWifi.get(position);
        viewHolder.tvName.setText(wifiList.getWifiName());
        viewHolder.tvLevel.setText(wifiList.getWifiLevel());
        viewHolder.tvChannel.setText(wifiList.getWifiChannel());
        viewHolder.tvEncryption.setText(wifiList.getWifiEncryption());

        return convertView;
    }

    public class ViewHolder{
        TextView tvName;
        TextView tvLevel;
        TextView tvChannel;
        TextView tvEncryption;
    }
}
