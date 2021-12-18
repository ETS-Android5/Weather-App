package com.angmadera.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class WeeklyActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private RecyclerView recyclerView;
    private WeeklyWeatherAdapter mAdapter;
    private final List<WeeklyWeather> hourlyList = new ArrayList<>();
    private String lat;
    private String lon;
    private String locationName;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //send code for fahrenheit and celcius

        super.onCreate(savedInstanceState);
        setContentView(R.layout.weekly_main);

        boolean fahrenheit = true;
        intent = getIntent();
        if(intent.hasExtra("LAT") && intent.hasExtra("LON") && intent.hasExtra("LOCATION NAME") && intent.hasExtra("FAHRENHEIT")) {
            lat = intent.getStringExtra("LAT");
            lon = intent.getStringExtra("LON");
            locationName = intent.getStringExtra("LOCATION NAME");
            this.setTitle(locationName);
            Bundle bundle = getIntent().getExtras();
            fahrenheit = bundle.getBoolean("FAHRENHEIT");
        }

        recyclerView = findViewById(R.id.recyclerDaily);
        mAdapter = new WeeklyWeatherAdapter(hourlyList, this, fahrenheit);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        WeatherDownloadRunnableTwo countryLoaderRunnable = new WeatherDownloadRunnableTwo(this, lat, lon, fahrenheit);
        new Thread(countryLoaderRunnable).start();
    }

    // From OnClickListener
    @Override
    public void onClick(View v) {  // click listener called by ViewHolder clicks
        int pos = recyclerView.getChildLayoutPosition(v);
        WeeklyWeather m = hourlyList.get(pos);
    }

    // From OnLongClickListener
    @Override
    public boolean onLongClick(View v) {  // long click listener called by ViewHolder long clicks
        int pos = recyclerView.getChildLayoutPosition(v);
        WeeklyWeather m = hourlyList.get(pos);
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void updateData(ArrayList<WeeklyWeather> weeklyWeatherW) {
        hourlyList.addAll(weeklyWeatherW);
        mAdapter.notifyItemRangeChanged(0, weeklyWeatherW.size());
    }
    }

