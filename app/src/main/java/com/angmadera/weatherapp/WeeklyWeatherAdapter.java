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

public class WeeklyWeatherAdapter extends RecyclerView.Adapter<MyViewHolderTwo> {

    private final List<WeeklyWeather> weeklyWeatherList;
    private final WeeklyActivity mainAct;
    private final boolean fahrenheit;

    WeeklyWeatherAdapter(List<WeeklyWeather> empList, WeeklyActivity ma, boolean fahrenheit) {
        this.weeklyWeatherList = empList;
        mainAct = ma;
        this.fahrenheit = fahrenheit;
    }

    @NonNull
    @Override
    public MyViewHolderTwo onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.weekly_layout, parent, false);

        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);

        return new MyViewHolderTwo(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolderTwo holder, int position) {

        WeeklyWeather weeklyWeather = weeklyWeatherList.get(position);

        //conditions
        holder.description.setText(weeklyWeather.getDescription().trim());
        //precip
        holder.precip.setText(String.format(Locale.getDefault(), "(%d", weeklyWeather.getPrecip()) + "% precip)");
        //uv index
        holder.uv.setText(String.format(Locale.getDefault(), "UV Index: %d", weeklyWeather.getUv()));

        LocalDateTime ldt =
                LocalDateTime.ofEpochSecond(weeklyWeather.getDt() +  weeklyWeather.getTimezoneOffset(), 0, ZoneOffset.UTC);
        DateTimeFormatter dtf =
                DateTimeFormatter.ofPattern("EEEE M/d", Locale.getDefault());
        String formattedTimeString = ldt.format(dtf);
        holder.dt.setText(formattedTimeString);

        //hours daily, morning, day, evening, night
        holder.minMax.setText(String.format(Locale.getDefault(), "%d" + (fahrenheit ? "°F" : "°C ") + "/%d" + (fahrenheit ? "°F " : "°C "), weeklyWeather.getMax(), weeklyWeather.getMin()));
        holder.morning.setText(String.format(Locale.getDefault(), "%d" + (fahrenheit ? "°F" : "°C "), weeklyWeather.getMorning()));
        holder.afternoon.setText(String.format(Locale.getDefault(), "%d" + (fahrenheit ? "°F" : "°C "), weeklyWeather.getDay()));
        holder.evening.setText(String.format(Locale.getDefault(), "%d" + (fahrenheit ? "°F" : "°C "), weeklyWeather.getEvening()));
        holder.night.setText(String.format(Locale.getDefault(), "%d" + (fahrenheit ? "°F" : "°C "), weeklyWeather.getNight()));

        String iconCode = weeklyWeather.getIcon();
        int iconResId = mainAct.getResources().getIdentifier(iconCode, "drawable", mainAct.getPackageName());
        holder.icon.setImageResource(iconResId);
    }

    @Override
    public int getItemCount() {
        return weeklyWeatherList.size();
    }

}