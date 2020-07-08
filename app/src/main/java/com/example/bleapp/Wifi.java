package com.example.bleapp;

public class Wifi {

    private String mWifiName;
    private String mWifiLevel;
    private String mWifiChannel;
    private String mWifiEncryption;

    public Wifi(String mWifiName, String mWifiLevel, String mWifiChannel, String mWifiEncryption) {
        this.mWifiName = mWifiName;
        this.mWifiLevel = mWifiLevel;
        this.mWifiChannel = mWifiChannel;
        this.mWifiEncryption = mWifiEncryption;
    }

    public String getWifiName() {
        return mWifiName;
    }

    public String getWifiLevel() {
        return mWifiLevel;
    }

    public String getWifiChannel() {
        return mWifiChannel;
    }

    public String getWifiEncryption() {
        return mWifiEncryption;
    }

    public void setWifiName(String mWifiName) {
        this.mWifiName = mWifiName;
    }

    public void setWifiLevel(String mWifiLevel) {
        this.mWifiLevel = mWifiLevel;
    }

    public void setWifiChannel(String mWifiChannel) {
        this.mWifiChannel = mWifiChannel;
    }

    public void setWifiEncryption(String mWifiEncryption) {
        this.mWifiEncryption = mWifiEncryption;
    }
}
