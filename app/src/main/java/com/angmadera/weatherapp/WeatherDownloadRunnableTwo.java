package com.angmadera.weatherapp;

import android.net.Uri;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class WeatherDownloadRunnableTwo implements Runnable {
    private final WeeklyActivity mainActivity;
    private final String latitude;
    private final String longitude;
    private static final String APIKey = "0a4ccb1405e3916c291c5a4828db2c77";
    private final boolean fahrenheit;

    private static final String weatherURL =
            "https://api.openweathermap.org/data/2.5/onecall";


    WeatherDownloadRunnableTwo(WeeklyActivity mainActivity, String lat, String lon, boolean fahrenheit) {
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
        final ArrayList<WeeklyWeather> hw = parseDailyWeatherJSON(jsonString);
        mainActivity.runOnUiThread(() -> mainActivity.updateData(hw));
    }

    private ArrayList<WeeklyWeather> parseDailyWeatherJSON(String s) {
        ArrayList<WeeklyWeather> dWeatherList = new ArrayList<>();
        try
        {
            JSONObject jObjMain = new JSONObject(s);
            int timezoneOffset = jObjMain.getInt("timezone_offset");
            JSONArray jaDay = jObjMain.getJSONArray("daily");
            for (int i = 0; i < jaDay.length(); i++) {
                int [] temp = new int[6];
                JSONObject joDay = jaDay.getJSONObject(i);
                int dt = joDay.getInt("dt");
                JSONObject dayTemp = joDay.getJSONObject("temp");
                temp[0] = dayTemp.getInt("day");
                temp[1] = dayTemp.getInt("min");
                temp[2] = dayTemp.getInt("max");
                temp[3] = dayTemp.getInt("night");
                temp[4] = dayTemp.getInt("eve");
                temp[5] = dayTemp.getInt("morn");
                JSONArray daWeather = joDay.getJSONArray("weather");
                JSONObject dayWeather = (JSONObject) daWeather.get(0);
                String description = dayWeather.getString("description");
                String icon = "_" + dayWeather.getString("icon");
                int id = dayWeather.getInt("id");
                int pop = joDay.getInt("pop");
                int uvi = joDay.getInt("uvi");

                dWeatherList.add(new WeeklyWeather(timezoneOffset, temp, dt, id, pop, description, icon, uvi));
            }
            return dWeatherList;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}