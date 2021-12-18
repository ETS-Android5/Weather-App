package com.angmadera.weatherapp;

public class WeeklyWeather {

    private final int timezoneOffset;
    private final int[] temp;
    private final int dt;
    private final int id;
    private final String description;
    private final int precip;
    private final String icon;
    private final int uv;

    WeeklyWeather(int timezoneOffset, int[] temp, int dt, int id, int precip, String description, String icon, int uv) {
        this.timezoneOffset = timezoneOffset;
        this.temp = temp;
        this.dt = dt;
        this.precip = precip;
        this.id = id;
        this.description = description;
        this.icon = icon;
        this.uv = uv;
    }

    public int getTimezoneOffset() {return timezoneOffset; }

    public int getMax() { return temp[0];}

    public int getMin() { return temp[1];}

    public int getMorning() { return temp[2];}

    public int getDay() { return temp[3];}

    public int getEvening() { return temp[4];}

    public int getNight() { return temp[5];}

    public int getPrecip() {
        return precip;
    }

    public int getDt() {
        return dt;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getIcon() {
        return icon;
    }

    public int getUv() {
        return uv;
    }
}