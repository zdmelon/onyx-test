package com.example.zd.myapplication;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.github.druk.rx2dnssd.Rx2Dnssd;
import com.github.druk.rx2dnssd.Rx2DnssdEmbedded;
import com.github.druk.rxdnssd.BonjourService;
import com.github.druk.rxdnssd.RxDnssd;
import com.github.druk.rxdnssd.RxDnssdBindable;
import com.github.druk.rxdnssd.RxDnssdEmbedded;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class RxActivity extends Activity {

    RxDnssd rxDnssd;
    Subscription subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx2);
        rxDnssd = new RxDnssdBindable(this);
//        rxDnssd = new RxDnssdBindable(this);
//        registerServer();
        findserver();
    }

    private void registerServer() {
        BonjourService bs = new BonjourService.Builder(0, 0, Build.DEVICE, "_http._tcp", null).port(9527).build();
        Subscription subscription = rxDnssd.register(bs)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(service -> {
                    Log.i("TAG", "registerServer:注册成功 "+service.toString());
                }, throwable -> {
                    Log.e("DNSSD", "Error: ", throwable);
                });
    }

    private void findServer() {
        subscription = rxDnssd.browse("_http._tcp", "local.")
                .compose(rxDnssd.resolve())
                .compose(rxDnssd.queryRecords())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bonjourService -> Log.i("TAG", bonjourService.toString()),
                        throwable -> Log.e("TAG", "error", throwable));

    }


    private void findserver() {
        Counter counter = new Counter();
        subscription = rxDnssd.browse("_http._tcp", "local.")
                .compose(rxDnssd.resolve())
                .compose(rxDnssd.queryRecords())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BonjourService>() {
                    @Override
                    public void call(BonjourService bonjourService) {
//                        subscription.unsubscribe();
                        if (bonjourService.getServiceName().equals("onyx-test")) {
                        Log.i("TAG", bonjourService.toString());
//                            Log.e("TAG", "call: "+bonjourService.getInet4Address().getHostAddress());
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
                        }

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e("TAG", "error", throwable);
                    }
                });
    }

    public int i = 0;

    class Counter {

        public int num() {
            return  i++;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscription.unsubscribe();
    }
}
