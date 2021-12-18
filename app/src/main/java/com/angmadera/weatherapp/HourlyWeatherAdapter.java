package com.angmadera.weatherapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class HourlyWeatherAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private final List<HourlyWeather> hourlyList;
    private final MainActivity mainAct;
    private final boolean metric;

    HourlyWeatherAdapter(List<HourlyWeather> hourlyList, MainActivity ma, boolean metric) {
        this.hourlyList = hourlyList;
        mainAct = ma;
        this.metric = metric;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hourly_layout, parent, false);
        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);
        return new MyViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        HourlyWeather hourly = hourlyList.get(position);

        LocalDateTime ldt =
                LocalDateTime.ofEpochSecond(hourly.getDateTime() + hourly.getTimezoneOffset(), 0, ZoneOffset.UTC);
        DateTimeFormatter dtf =
                DateTimeFormatter.ofPattern("h:mm a", Locale.getDefault());
        String formattedTimeString = ldt.format(dtf);


        DateTimeFormatter dtfTwo =
                DateTimeFormatter.ofPattern("EEEE", Locale.getDefault());

        String formattedTimeStringTwo = ldt.format(dtfTwo);

        holder.day.setText(formattedTimeStringTwo);
        holder.description.setText(hourly.getDescription());
        holder.time.setText(formattedTimeString);
        holder.temp.setText(String.format(Locale.getDefault(),"%d" + (metric ? "°F " : "°C "), hourly.getTemp()));


        String iconCode = "_" + hourly.getIcon();
        int iconResId = mainAct.getResources().getIdentifier(iconCode, "drawable", mainAct.getPackageName());
        holder.icon.setImageResource(iconResId);
    }

    @Override
    public int getItemCount() {
        return hourlyList.size();
    }

}
