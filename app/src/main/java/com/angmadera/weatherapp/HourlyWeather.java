package com.angmadera.weatherapp;

public class HourlyWeather {
    private final int timezoneOffset;
    private final int dateTime;
    private final int temp;
    private final String description;
    private final String icon;
    private final int pop;

    HourlyWeather (int timezoneOffset, int dateTime, int temp, String description, String icon, int pop) {
        this.timezoneOffset = timezoneOffset;
        this.dateTime = dateTime;
        this.temp = temp;
        this.description = description;
        this.icon = icon;
        this.pop = pop;
    }

    int getTimezoneOffset() {return timezoneOffset;}

    int getDateTime() {return dateTime;}

    String getDescription() {
        return description;
    }

    String getIcon() { return icon;}

    int getTemp() { return temp;}

    int getPop() { return pop;}

}
