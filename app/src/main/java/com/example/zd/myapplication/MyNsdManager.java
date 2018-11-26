package com.example.zd.myapplication;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.Build;

import java.net.InetAddress;

/**
 * Created by zd on 2018/6/5.
 */

public class MyNsdManager {

    public static final String SERVICE_TYPE = "_http._tcp";
    private String serviceName;

    private NsdManager.DiscoveryListener  discoveryListener;
    private NsdManager.ResolveListener  resolveListener;
    private NsdManager nsdManager;
    private Context context;

    public MyNsdManager(Context context) {
        this.context = context;
        initListener();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void initListener() {
        discoveryListener = new NsdManager.DiscoveryListener() {

            @Override
            public void onStartDiscoveryFailed(String serviceType, int errorCode) {

            }

            @Override
            public void onStopDiscoveryFailed(String serviceType, int errorCode) {

            }

            @Override
            public void onDiscoveryStarted(String serviceType) {

            }

            @Override
            public void onDiscoveryStopped(String serviceType) {

            }

            @Override
            public void onServiceFound(NsdServiceInfo serviceInfo) {
                if (serviceInfo.getServiceName().equals(serviceName)){
                    nsdManager.resolveService(serviceInfo,resolveListener);
                }
            }

            @Override
            public void onServiceLost(NsdServiceInfo serviceInfo) {

            }
        };

        resolveListener = new NsdManager.ResolveListener() {
            @Override
            public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {

            }

            @Override
            public void onServiceResolved(NsdServiceInfo serviceInfo) {
                int port = serviceInfo.getPort();
                InetAddress host = serviceInfo.getHost();

            }
        };
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public  void  findService(){
//        nsdManager = (NsdManager) context.getSystemService(MainActivity.NSD_SERVICE);
        nsdManager.discoverServices(SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, discoveryListener);
    }

}
