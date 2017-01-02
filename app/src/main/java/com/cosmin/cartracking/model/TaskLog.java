package com.cosmin.cartracking.model;


public class TaskLog {
    private long time;
    private double latitude;
    private double longitude;
    private double speed;
    private double altitude;

    public TaskLog(long time, double latitude, double longitude) {
        this.time = time;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public long getTime() {
        return time;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }
}
