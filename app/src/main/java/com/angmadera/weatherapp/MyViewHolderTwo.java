package com.angmadera.weatherapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolderTwo extends RecyclerView.ViewHolder {

    TextView dt;
    TextView minMax;
    TextView description;
    TextView precip;
    TextView uv;
    TextView morning;
    TextView afternoon;
    TextView evening;
    TextView night;
    ImageView icon;

    MyViewHolderTwo(View view) {
        super(view);
        dt = view.findViewById(R.id.weeklyDay);
        minMax = view.findViewById(R.id.minAndMaxWeekly);
        description = view.findViewById(R.id.descriptionWeekly);
        precip = view.findViewById(R.id.precipWeekly);
        uv = view.findViewById(R.id.uvWeekly);
        morning = view.findViewById(R.id.mornWeekly);
        afternoon = view.findViewById(R.id.afterWeekly);
        evening = view.findViewById(R.id.evenWeekly);
        night = view.findViewById(R.id.nightWeekly);
        icon = view.findViewById(R.id.imageView);
    }
}
