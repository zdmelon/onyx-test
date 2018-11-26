package com.example.zd.myapplication;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class PowerActivity extends AppCompatActivity {

    private static final String TAG = "zdmelon";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_power);
        getWifi();
    }


    public void getWifi() {
        WifiManager wifi_service = (WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);

        WifiInfo wifiInfo = wifi_service.getConnectionInfo();
        int wifi = wifiInfo.getRssi();
        Log.i(TAG, "getWifi: "+wifi);
    }
}
