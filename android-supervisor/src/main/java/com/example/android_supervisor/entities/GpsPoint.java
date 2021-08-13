package com.example.android_supervisor.entities;

import java.util.Date;

/**
 * @author wujie
 */
public class GpsPoint {
    private float latitude;
    private float longitude;
    private int elevation;
    private float speed;
    private int direction;
    private int coordType;
    private int provider;
    private Date time;
    private boolean isInGrid;

    private float Accuracy;

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public int getElevation() {
        return elevation;
    }

    public void setElevation(int elevation) {
        this.elevation = elevation;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getCoordType() {
        return coordType;
    }

    public void setCoordType(int coordType) {
        this.coordType = coordType;
    }

    public int getProvider() {
        return provider;
    }

    public void setProvider(int provider) {
        this.provider = provider;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public boolean isInGrid() {
        return isInGrid;
    }

    public void setInGrid(boolean inGrid) {
        isInGrid = inGrid;
    }

    public float getAccuracy() {
        return Accuracy;
    }

    public void setAccuracy(float accuracy) {
        Accuracy = accuracy;
    }

    @Override
    public String toString() {
        return "GpsPoint{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", elevation=" + elevation +
                ", speed=" + speed +
                ", direction=" + direction +
                ", coordType=" + coordType +
                ", provider=" + provider +
                ", time=" + time +
                ", isInGrid=" + isInGrid +
                ", Accuracy=" + Accuracy +
                '}';
    }
}
