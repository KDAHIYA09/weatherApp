package com.example.weatherapp;

public class weatherRVModel {

    private String time;
    private String icon;
    private String temprature;
    private String windSpeed;

    public weatherRVModel(String time, String icon, String temprature, String windSpeed) {
        this.time = time;
        this.icon = icon;
        this.temprature = temprature;
        this.windSpeed = windSpeed;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTemprature() {
        return temprature;
    }

    public void setTemprature(String temprature) {
        this.temprature = temprature;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }
}
