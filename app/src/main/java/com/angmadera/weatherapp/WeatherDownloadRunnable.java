package com.angmadera.weatherapp;
import android.net.Uri;
import android.os.Build;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class WeatherDownloadRunnable implements Runnable {

    private final MainActivity mainActivity;
    private final String latitude;
    private final String longitude;
    private static final String APIKey = "0a4ccb1405e3916c291c5a4828db2c77";
    private final boolean fahrenheit;

    private static final String weatherURL =
            "https://api.openweathermap.org/data/2.5/onecall";

    WeatherDownloadRunnable(MainActivity mainActivity, String lat, String lon, boolean fahrenheit) {
        this.mainActivity = mainActivity;
        this.latitude = lat;
        this.longitude = lon;
        this.fahrenheit = fahrenheit;
    }


    public void run() {

        Uri.Builder dataUri = Uri.parse(weatherURL).buildUpon();

        dataUri.appendQueryParameter("lat", latitude);
        dataUri.appendQueryParameter("lon", longitude);
        dataUri.appendQueryParameter("units", (fahrenheit ? "imperial" : "metric"));
        dataUri.appendQueryParameter("appid", APIKey);
        dataUri.appendQueryParameter("lang", "en");
        dataUri.appendQueryParameter("exclude", "minutely");
        String urlToUse = dataUri.build().toString();

        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlToUse);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.connect();

            if (conn.getResponseCode() != HttpsURLConnection.HTTP_OK) {
                handleResults(null);
                return;
            }

            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }

        } catch (Exception e) {
            handleResults(null);
            return;
        }
        handleResults(sb.toString());
    }

    public void handleResults(final String jsonString) {
        final CurrentWeather w = parseCurrentWeatherJSON(jsonString);
        final ArrayList<HourlyWeather> hw = parseHourlyWeatherJSON(jsonString);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mainActivity.runOnUiThread(() -> mainActivity.updateData(w, hw));
        }
    }

    private CurrentWeather parseCurrentWeatherJSON(String s) {
        try {
            JSONObject jObjMain = new JSONObject(s);
            double latitude = jObjMain.getDouble("lat");
            double longitude = jObjMain.getDouble("lon");
            String timezone = jObjMain.getString("timezone");
            int timezoneOffset = jObjMain.getInt("timezone_offset");

            //"current" section

            JSONObject current = jObjMain.getJSONObject("current");
            int dt = current.getInt("dt");
            int sunrise = current.getInt("sunrise");
            int sunset = current.getInt("sunset");
            int temp = current.getInt("temp");
            int feelsLike = current.getInt("feels_like");
            int pressure = current.getInt("pressure");
            int humidity = current.getInt("humidity");
            int uvi = current.getInt("uvi");
            int clouds = current.getInt("clouds");
            int visibility = current.getInt("visibility");
            int wind_speed = current.getInt("wind_speed");
            double wind_deg = current.getDouble("wind_deg");
            JSONArray currentWeather = current.getJSONArray("weather");
            JSONObject currentW = (JSONObject) currentWeather.get(0);
            int id = currentW.getInt("id");
            String main = currentW.getString("main");
            String description = currentW.getString("description");
            String icon = currentW.getString("icon");

            //daily
            int[] temps = new int[4];
            JSONArray jaDay = jObjMain.getJSONArray("daily");
            JSONObject joDay = jaDay.getJSONObject(0);
            JSONObject dayTemp = joDay.getJSONObject("temp");
            temps[1] = dayTemp.getInt("day");
            temps[3] = dayTemp.getInt("night");
            temps[2] = dayTemp.getInt("eve");
            temps[0] = dayTemp.getInt("morn");

            //no wind gust, no rain, and no snow
            return new CurrentWeather(temps, latitude, longitude, timezone, timezoneOffset, dt, sunrise,
                    sunset, temp, feelsLike, pressure, humidity, uvi, clouds, visibility, wind_speed,
                    wind_deg, id, main, description, icon);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private ArrayList<HourlyWeather> parseHourlyWeatherJSON(String s) {
        ArrayList<HourlyWeather> HourlyList = new ArrayList<>();
        try {

            JSONObject jObjMain = new JSONObject(s);
            int timezoneOffset = jObjMain.getInt("timezone_offset");
            JSONArray hourly = jObjMain.getJSONArray("hourly");
            for (int i = 1; i < hourly.length(); ++i) {
                JSONObject jHourly = (JSONObject) hourly.get(i);
                int dtHourly = jHourly.getInt("dt");
                int tempHourly = jHourly.getInt("temp");
                int popHourly = jHourly.getInt("pop");
                JSONArray hWeather = jHourly.getJSONArray("weather");
                JSONObject hourlyWeather = (JSONObject) hWeather.get(0);
                String descriptionHourly = hourlyWeather.getString("description");
                String iconHourly = hourlyWeather.getString("icon");

                HourlyList.add(new HourlyWeather(timezoneOffset, dtHourly, tempHourly, descriptionHourly, iconHourly, popHourly));

            }
            return HourlyList;
        }
        catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }
