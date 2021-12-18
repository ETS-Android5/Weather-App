package com.angmadera.weatherapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    String initialLat = "41.8675766";
    String initialLon = "-87.616232";
    double lat;
    double lon;
    boolean fahrenheit = true;
    String inputLocation = "Chicago, Illinois";
    String locale;
    private final MainActivity main = this;
    HourlyWeather position;
    boolean internet;
    int pos;
    private final List<HourlyWeather> hourlyList = new ArrayList<>();
    private HourlyWeatherAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.setTitle("OpenWeather App");

        RecyclerView recycler = findViewById(R.id.recHourly);
        mAdapter = new HourlyWeatherAdapter(hourlyList, this, fahrenheit);
        recycler.setAdapter(mAdapter);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        doDownload();
    }

    private void doDownload() {
        WeatherDownloadRunnable loaderTaskRunnable = new WeatherDownloadRunnable(this, initialLat, initialLon, fahrenheit);
        new Thread(loaderTaskRunnable).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.units_c) {
            if (!internet) {
                Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG).show();
            } else {
                if (fahrenheit) {
                    fahrenheit = false;
                    int iconResId = main.getResources().getIdentifier("units_f", "drawable", main.getPackageName());
                    item.setIcon(iconResId);
                    doDownload();
                    RecyclerView recycler = findViewById(R.id.recHourly);
                    mAdapter = new HourlyWeatherAdapter(hourlyList, this, fahrenheit);
                    recycler.swapAdapter(mAdapter, true);
                } else {
                    fahrenheit = true;
                    int iconResId = main.getResources().getIdentifier("units_c", "drawable", main.getPackageName());
                    item.setIcon(iconResId);
                    doDownload();
                    RecyclerView recycler = findViewById(R.id.recHourly);
                    mAdapter = new HourlyWeatherAdapter(hourlyList, this, fahrenheit);
                    recycler.swapAdapter(mAdapter, true);
                }
                doDownload();
                return false;
            }

        } else if (item.getItemId() == R.id.daily) {
            if (!internet) {
                Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG).show();
            } else {
                Intent intent = new Intent(this, WeeklyActivity.class);
                intent.putExtra("LAT", initialLat);
                intent.putExtra("LON", initialLon);
                intent.putExtra("LOCATION NAME", inputLocation);
                intent.putExtra("FAHRENHEIT", fahrenheit);
                startActivity(intent);
            }
        } else if (item.getItemId() == R.id.location) {
            if (!internet) {
                Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG).show();
            } else {
                LayoutInflater inflater = LayoutInflater.from(this);
                @SuppressLint("InflateParams") final View view = inflater.inflate(R.layout.layout_dialog, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("For US locations, enter as 'City', or 'City, State'\n\n For international locations, enter 'City, Country'");
                builder.setTitle("Enter a Location");
                builder.setView(view);
                builder.setPositiveButton("OK", (dialog, id) -> {
                    EditText location = view.findViewById(R.id.edit_location);
                    inputLocation = location.getText().toString();
                    double[] tempLocation;
                    if (getLatLon(inputLocation) == null) {
                        Toast.makeText(this, "Location Not Found", Toast.LENGTH_LONG).show();
                    } else {
                        tempLocation = getLatLon(inputLocation);
                        assert tempLocation != null;
                        double latitude = tempLocation[0];
                        double longitude = tempLocation[1];
                        initialLat = Double.toString(latitude);
                        initialLon = Double.toString(longitude);
                        TextView currentLocation = findViewById(R.id.locationCurrent);
                        currentLocation.setText(inputLocation);
                        doDownload();
                    }
                });
                builder.setNegativeButton("CANCEL", (dialog, id) -> {
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        } else {
            return super.onOptionsItemSelected(item);
        }
        return false;
    }

    private double[] getLatLon(String userProvidedLocation) {
        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> address =
                    geocoder.getFromLocationName(userProvidedLocation, 1);
            if (address == null || address.isEmpty()) {
                return null;
            }
            lat = address.get(0).getLatitude();
            lon = address.get(0).getLongitude();

            return new double[] {lat, lon};
        } catch (IOException e) {
            return null;
        }
    }

    private String getLocationName(String userProvidedLocation) {
        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> address =
                    geocoder.getFromLocationName(userProvidedLocation, 1);
            if (address == null || address.isEmpty()) {
                return null;
            }
            String country = address.get(0).getCountryCode();
            String p1;
            String p2;
            if (country.equals("US")) {
                p1 = address.get(0).getLocality();
                p2 = address.get(0).getAdminArea();
            } else {
                p1 = address.get(0).getLocality();
                if (p1 == null)
                    p1 = address.get(0).getSubAdminArea();
                p2 = address.get(0).getCountryName();
            }
            locale = p1 + ", " + p2;
            return locale;
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public void onClick(View view) {
        position = hourlyList.get(pos);
    }

    @Override
    public boolean onLongClick(View view) {
        return false;
    }

    private String getDirection(double degrees) {
        if (degrees >= 337.5 || degrees < 22.5)
            return "N";
        if (degrees >= 22.5 && degrees < 67.5)
            return "NE";
        if (degrees >= 67.5 && degrees < 112.5)
            return "E";
        if (degrees >= 112.5 && degrees < 157.5)
            return "SE";
        if (degrees >= 157.5 && degrees < 202.5)
            return "S";
        if (degrees >= 202.5 && degrees < 247.5)
            return "SW";
        if (degrees >= 247.5 && degrees < 292.5)
            return "W";
        if (degrees >= 292.5 && degrees < 337.5)
            return "NW";
        return "X"; // We'll use 'X' as the default if we get a bad value
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateData(CurrentWeather weather, ArrayList<HourlyWeather> hourlyW) {
        String iconCode;
        TextView location = findViewById(R.id.locationCurrent);
        if (weather == null || hourlyW.isEmpty()) {
            internet = false;
            location.setText("No Internet Connection");
            TextView eight = findViewById(R.id.morningCurrent);
            TextView one = findViewById(R.id.dayCurrent);
            TextView five =  findViewById(R.id.eveningCurrent);
            TextView eleven = findViewById(R.id.nightCurrent);
            RecyclerView recycler = findViewById(R.id.recHourly);
            eight.setVisibility(TextView.INVISIBLE);
            one.setVisibility(TextView.INVISIBLE);
            five.setVisibility(TextView.INVISIBLE);
            eleven.setVisibility(TextView.INVISIBLE);
            recycler.setVisibility(RecyclerView.INVISIBLE);
            return;
        }

        internet = true;
        location.setText(inputLocation);

        //date and time
        LocalDateTime ldt =
                LocalDateTime.ofEpochSecond(weather.getDateTime() + weather.getTimezone_offset(), 0, ZoneOffset.UTC);
        DateTimeFormatter dtf =
                DateTimeFormatter.ofPattern("EEE MMM dd h:mm a, yyyy", Locale.getDefault());
        String formattedTimeString = ldt.format(dtf); // Thu Sep 30 10:06 PM, 2021
        TextView dt = findViewById(R.id.dtCurrent);
        dt.setText(formattedTimeString);

        //temp
        TextView temperature = findViewById(R.id.currentTemp);
        temperature.setText(String.format("%d" + (fahrenheit ? "°F " : "°C "), weather.getTemp()));

        //feels like
        TextView feelsLike = findViewById(R.id.currentFeelsLIke);
        feelsLike.setText(String.format("Feels Like %d"+ (fahrenheit ? "°F " : "°C "), weather.getFeelsLike()));

        //weather description and clouds --- might need to capitalize word
        TextView description = findViewById((R.id.currentDescription));
        description.setText(String.format(Locale.getDefault(), "%s (%d", weather.getDescription(), weather.getClouds()) + "% clouds)");

        //image icon
        ImageView image = findViewById(R.id.imageViewCurrent);
        iconCode = "_" + weather.getIcon();
        int iconResId = main.getResources().getIdentifier(iconCode, "drawable", main.getPackageName());
        image.setImageResource(iconResId);

        //winds
        String direction = getDirection(weather.getWind_deg());
        TextView winds = findViewById(R.id.windsCurrent);
        winds.setText("Winds: " + direction + String.format(Locale.getDefault(), " at %d " + (fahrenheit ? "mph" : "mps"), weather.getWind_speed()));

        //humidity
        TextView humidity = findViewById(R.id.humidityCurrent);
        humidity.setText(String.format(Locale.getDefault(), "Humidity: %d", weather.getHumidity()) + "%");

        //uv index
        TextView uv = findViewById(R.id.uvIndexCurrent);
        uv.setText(String.format(Locale.getDefault(), "UV Index: %d", weather.getUvInd()));

        //visibility -- need to change this to km
        double meterToMiles = weather.getVisibility() * 0.00062137119;
        double meterToKilometer = weather.getVisibility() * 0.001;
        TextView visibility = findViewById((R.id.visibilityCurrent));
        visibility.setText(String.format(Locale.getDefault(), "Visibility: %.1f ", (fahrenheit ? meterToMiles : meterToKilometer)) + (fahrenheit ? "mi" : "km"));

        //8 am
        TextView eightAM = findViewById(R.id.morningTempCurrent);
        eightAM.setText(String.format(Locale.getDefault(), "%d" + (fahrenheit ? "°F " : "°C "), weather.morning()));

        TextView onePM = findViewById(R.id.dayTempCurrent);
        onePM.setText(String.format(Locale.getDefault(), "%d" + (fahrenheit ? "°F " : "°C "), weather.afternoon()));

        TextView fivePM = findViewById(R.id.eveningTempCurrent);
        fivePM.setText(String.format(Locale.getDefault(), "%d" + (fahrenheit ? "°F " : "°C "), weather.evening()));

        TextView elevenPM = findViewById(R.id.nightTempCurrent);
        elevenPM.setText(String.format(Locale.getDefault(), "%d" + (fahrenheit ? "°F " : "°C "), weather.night()));

        if (!hourlyList.isEmpty()) {
            hourlyList.clear();
            mAdapter.notifyItemRangeChanged(0, hourlyW.size());
        }
        hourlyList.addAll(hourlyW);
        mAdapter.notifyItemRangeChanged(0, hourlyW.size());


        LocalDateTime ldtTwo =
                LocalDateTime.ofEpochSecond(weather.getSunrise() + weather.getTimezone_offset(), 0, ZoneOffset.UTC);
        TextView sunrise = findViewById(R.id.sunrise);
        DateTimeFormatter dtfTwo =
                DateTimeFormatter.ofPattern("h:mm a", Locale.getDefault());
        String formattedTimeStringTwo = ldtTwo.format(dtfTwo);
        sunrise.setText("Sunrise: " + formattedTimeStringTwo);

        LocalDateTime ldtThree =
                LocalDateTime.ofEpochSecond(weather.getSunset() + weather.getTimezone_offset(), 0, ZoneOffset.UTC);
        TextView sunset = findViewById(R.id.sunset);
        DateTimeFormatter dtfThree =
                DateTimeFormatter.ofPattern("h:mm a", Locale.getDefault());
        String formattedTimeStringThree = ldtThree.format(dtfThree);
        sunset.setText("Sunset: " + formattedTimeStringThree);
    }
}