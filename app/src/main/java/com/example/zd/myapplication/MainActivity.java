//package com.example.zd.myapplication;
//
//import android.annotation.TargetApi;
//import android.app.Activity;
//import android.net.nsd.NsdManager;
//import android.net.nsd.NsdServiceInfo;
//import android.os.Build;
//import android.os.Bundle;
//import android.support.annotation.RequiresApi;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//
//import com.github.druk.dnssd.BrowseListener;
//import com.github.druk.dnssd.DNSSD;
//import com.github.druk.dnssd.DNSSDEmbedded;
//import com.github.druk.dnssd.DNSSDException;
//import com.github.druk.dnssd.DNSSDService;
//import com.github.druk.dnssd.ResolveListener;
//
//import java.io.IOException;
//import java.net.InetAddress;
//import java.net.ServerSocket;
//import java.net.UnknownHostException;
//import java.util.Map;
//
//import static android.R.attr.port;
//
//@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//public class MainActivity extends Activity {
//
//    private static final String TAG = "MainActivity";
//
//    private NsdManager.DiscoveryListener mDiscoveryListener;
//    private NsdManager.ResolveListener mResolveListener;
//    private NsdManager.RegistrationListener mRegistrationListener;
//    private NsdManager mNsdManager;
//
//
//    DNSSD dnssd;
//    DNSSDService browseService;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        dnssd = new DNSSDEmbedded();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
////        findService();
//        findNsdService();
////        registerServer();
//    }
//
//    private void registerServer() {
//        NsdServiceInfo serviceInfo  = new NsdServiceInfo();
//
//        serviceInfo.setServiceName("onyx-zd-server");
//        serviceInfo.setServiceType("_http._tcp.");
//
//        serviceInfo.setPort(9527);
//
//        mRegistrationListener = new NsdManager.RegistrationListener() {
//            @Override
//            public void onRegistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
//                Log.i(TAG, "onRegistrationFailed: 注册失败");
//            }
//
//            @Override
//            public void onUnregistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
//
//            }
//
//            @Override
//            public void onServiceRegistered(NsdServiceInfo serviceInfo) {
//                Log.d(TAG, "onServiceRegistered: 注册成功");
//            }
//
//            @Override
//            public void onServiceUnregistered(NsdServiceInfo serviceInfo) {
//                Log.d(TAG, "onServiceUnregistered: 反注册成功");
//            }
//        };
//
//
//        mNsdManager = (NsdManager) getApplicationContext().getSystemService(MainActivity.NSD_SERVICE);
//
//        mNsdManager.registerService(
//                serviceInfo, NsdManager.PROTOCOL_DNS_SD, mRegistrationListener);
//
//    }
//
//    private void findNsdService() {
//        mDiscoveryListener = new NsdManager.DiscoveryListener() {
//            @Override
//            public void onStartDiscoveryFailed(String serviceType, int errorCode) {
//
//            }
//
//            @Override
//            public void onStopDiscoveryFailed(String serviceType, int errorCode) {
//
//            }
//
//            @Override
//            public void onDiscoveryStarted(String serviceType) {
//
//            }
//
//            @Override
//            public void onDiscoveryStopped(String serviceType) {
//
//            }
//
//            @Override
//            public void onServiceFound(NsdServiceInfo serviceInfo) {
////                Log.d(TAG, "serviceName: ..." +  serviceInfo.getServiceName());
//                Log.d(TAG, "onServiceFound: "+serviceInfo.toString());
//                if (serviceInfo.getServiceName().equals("onyx-meeting-server")) {
//                    mNsdManager.resolveService(serviceInfo, new NsdManager.ResolveListener() {
//                        @Override
//                        public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
//                            Log.e(TAG, "onResolveFailed: +errorCode = " + errorCode );
//                        }
//
//                        @Override
//                        public void onServiceResolved(NsdServiceInfo serviceInfo) {
//                            Log.i(TAG, "onServiceResolved: " + serviceInfo.toString());
//                        }
//                    });
//                }
//
//            }
//
//            @Override
//            public void onServiceLost(NsdServiceInfo serviceInfo) {
//
//            }
//        };
//        mNsdManager = (NsdManager) getApplicationContext().getSystemService(MainActivity.NSD_SERVICE);
//        mNsdManager.discoverServices("_http._tcp", NsdManager.PROTOCOL_DNS_SD, mDiscoveryListener);
//    }
//
//    public void initializeResolveListener() {
//        mResolveListener = new NsdManager.ResolveListener() {
//
//            @Override
//            public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
//                // Called when the resolve fails.  Use the error code to debug.
//                Log.e(TAG, "Resolve failed" + errorCode);
//            }
//
//            @Override
//            public void onServiceResolved(NsdServiceInfo serviceInfo) {
//                Log.e(TAG, "Resolve Succeeded. " + serviceInfo);
//
//
//            }
//        };
//    }
//
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
////        mNsdManager.stopServiceDiscovery(mDiscoveryListener);
////        mNsdManager.unregisterService(mRegistrationListener);
//        browseService.stop();
//    }
//
//    private void findService() {
//        try {
//            browseService = dnssd.browse("_http._tcp", new BrowseListener() {
//
//                @Override
//                public void serviceFound(DNSSDService browser, int flags, int ifIndex,
//                                         final String serviceName, String regType, String domain) {
//                    Log.i(TAG, "Found " + serviceName);
//                    if (serviceName.equals("onyx-meeting-server")){
//                        try {
//                            dnssd.resolve(flags, ifIndex, serviceName, regType, domain, new ResolveListener() {
//                                @Override
//                                public void serviceResolved(DNSSDService resolver, int flags, int ifIndex, String fullName, String hostName, int port, Map<String, String> txtRecord) {
//                                    Log.d(TAG, "serviceResolved: success");
//                                    Log.i(TAG, "serviceResolved: flags = " + flags);
//                                    Log.i(TAG, "serviceResolved: fullName = " + fullName);
//                                    Log.i(TAG, "serviceResolved: hostName = " + hostName);
//                                    Log.i(TAG, "serviceResolved: port = " + port);
//                                    Log.i(TAG, "serviceResolved: txtRecord = " + txtRecord.toString());
//                                }
//
//                                @Override
//                                public void operationFailed(DNSSDService service, int errorCode) {
//                                    Log.e(TAG, "operationFailed: failed" );
//                                }
//                            });
//                        } catch (DNSSDException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                }
//
//                @Override
//                public void serviceLost(DNSSDService browser, int flags, int ifIndex,
//                                        String serviceName, String regType, String domain) {
//                    Log.i(TAG, "Lost " + serviceName);
//                }
//
//                @Override
//                public void operationFailed(DNSSDService service, int errorCode) {
//                    Log.e(TAG, "error: " + errorCode);
//                }
//            });
//        } catch (DNSSDException e) {
//            Log.e(TAG, "error", e);
//        }
//    }
//}
