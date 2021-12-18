package com.angmadera.weatherapp;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MyViewHolder extends RecyclerView.ViewHolder {

    public TextView day;
    public TextView time;
    public TextView temp;
    public TextView description;
    public ImageView icon;

    MyViewHolder(View view) {
        super(view);
        day = view.findViewById(R.id.dayHourly);
        time = view.findViewById(R.id.timeHourly);
        temp = view.findViewById(R.id.temperatureHourly);
        description = view.findViewById(R.id.descriptionHourly);
        icon = view.findViewById(R.id.hourlyIcon);
    }
}

