package com.example.zd.myapplication;

import android.annotation.TargetApi;
import android.app.Activity;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.github.druk.rx2dnssd.Rx2Dnssd;
import com.github.druk.rx2dnssd.Rx2DnssdBindable;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class Rx2Activity extends Activity {


    Rx2Dnssd rxDnssd;
    Disposable browseDisposable;

    NsdManager mNsdManager;
    NsdManager.DiscoveryListener mDiscoveryListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx3);

//        findNsdService();

        rxDnssd = new Rx2DnssdBindable(this);
        findServer();
    }

    private void findServer() {
        Counter counter = new Counter();
        browseDisposable = rxDnssd.browse("_http._tcp", "local.")
                .compose(rxDnssd.resolve())
                .compose(rxDnssd.queryRecords())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bonjourService -> {
                    if (bonjourService.getServiceName().equals("onyx-test")) {
                        Log.d("TAG", bonjourService.toString());
//                    browseDisposable.dispose();
                        if (bonjourService.getInet4Address() == null) {
                            Log.i("TAG", "findServer: ip null = " +counter.num());
                            return;
                        }
                        String ip = bonjourService.getInet4Address().getHostAddress();
                        int port = bonjourService.getPort();
                        if (ip != null && !ip.equals("") && port != 0){
                            String serverUri = "http://" + ip +":" + port;
                            Log.i("TAG", "call: serverUri =" + serverUri);
                        }
//                        Log.i("TAG", "findServer: counter = " +counter.num());
                    }
                }, throwable -> Log.e("TAG", "error", throwable));
    }

    public int i = 0;

    class Counter {

        public int num() {
            return  i++;
        }
    }


    private void findNsdService() {
        mDiscoveryListener = new NsdManager.DiscoveryListener() {
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
//                Log.d(TAG, "serviceName: ..." +  serviceInfo.getServiceName());
                Log.d("zd", "onServiceFound: "+serviceInfo.toString());
                if (serviceInfo.getServiceName().equals("onyx-controller-center")) {
                    mNsdManager.resolveService(serviceInfo, new NsdManager.ResolveListener() {
                        @Override
                        public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
                            Log.e("zd", "onResolveFailed: +errorCode = " + errorCode );
                        }

                        @Override
                        public void onServiceResolved(NsdServiceInfo serviceInfo) {
                            Log.i("zd", "onServiceResolved: " + serviceInfo.toString());
                        }
                    });
                }

            }

            @Override
            public void onServiceLost(NsdServiceInfo serviceInfo) {

            }
        };
        mNsdManager = (NsdManager) getApplicationContext().getSystemService(Rx2Activity.NSD_SERVICE);
        mNsdManager.discoverServices("_http._tcp", NsdManager.PROTOCOL_DNS_SD, mDiscoveryListener);
    }
}
