package com.angmadera.weatherapp;

class CurrentWeather {

    private final int[] temps;
    private final double lat;
    private final double lon;
    private final String timezone;
    private final int timezone_offset;
    private final int dateTime;
    private final int sunrise;
    private final int sunset;
    private final int temp;
    private final int feelsLike;
    private final int pressure;
    private final int humidity;
    private final int uvInd;
    private final int clouds;
    private final int visibility;
    private final int wind_speed;
    private final double wind_deg;
    private final int id;
    private final String main;
    private final String description;
    private final String icon;

    //NONE
    CurrentWeather(int[] temps, double lat, double lon, String timezone, int timezone_offset, int dateTime, int sunrise, int sunset, int temp, int feelsLike, int pressure,
                   int humidity, int uvInd, int clouds, int visibility, int wind_speed, double wind_deg, int id, String main, String description, String icon) {
        this.temps = temps;
        this.lat = lat;
        this.lon = lon;
        this.timezone = timezone;
        this.timezone_offset = timezone_offset;
        this.dateTime = dateTime;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.temp = temp;
        this.feelsLike = feelsLike;
        this.pressure = pressure;
        this.humidity = humidity;
        this.uvInd = uvInd;
        this.clouds = clouds;
        this.visibility = visibility;
        this.wind_speed = wind_speed;
        this.wind_deg = wind_deg;
        this.id = id;
        this.main = main;
        this.description = description;
        this.icon = icon;
    }

    public int[] getTemps() { return temps; }

    public int morning() { return temps[0]; }

    public int afternoon() { return temps[1]; }

    public int evening() { return temps[2]; }

    public int night() { return temps[3]; }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String getTimezone() {
        return timezone;
    }

    public int getTimezone_offset() {
        return timezone_offset;
    }

    public int getDateTime() {
        return dateTime;
    }

    public int getSunrise() {
        return sunrise;
    }

    public int getSunset() {
        return sunset;
    }

    public int getTemp() {
        return temp;
    }

    public int getFeelsLike() {
        return feelsLike;
    }

    public int getPressure() {
        return pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public int getUvInd() {
        return uvInd;
    }

    public int getClouds() {
        return clouds;
    }

    public int getVisibility() {
        return visibility;
    }

    public int getWind_speed() {
        return wind_speed;
    }

    public double getWind_deg() {
        return wind_deg;
    }

    public int getId() {
        return id;
    }

    public String getMain() {
        return main;
    }

    public String getDescription() {
        return description;
    }

    public String getIcon() {
        return icon;
    }

}
