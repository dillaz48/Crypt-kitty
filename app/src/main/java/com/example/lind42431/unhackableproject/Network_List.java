package com.example.lind42431.unhackableproject;


import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import android.content.Context;
import android.content.BroadcastReceiver;
import android.content.Intent;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.io.File;
import java.util.List;

import android.net.wifi.ScanResult;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiConfiguration;
import android.net.NetworkInfo;

import android.widget.Toast;



public class Network_List extends AppCompatActivity {

    WifiManager wifiM;
    WifiScanReciever wifiR;
    ListView networklist;
    String wifis[];
    String searchNetwork;
    String ssid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network__list);

        final EditText searchNet = (EditText) findViewById(R.id.networkEditText);
        networklist = (ListView) findViewById(R.id.networkList);
        final Toast noOutput = Toast.makeText(getApplicationContext(), "Did not output data", Toast.LENGTH_SHORT);
        Button networkButton = (Button) findViewById(R.id.searchButton);
        Button selectButton = (Button) findViewById(R.id.buttonSelect);
        Button scanButton = (Button) findViewById(R.id.buttonScan);
        File netFile = new File(getApplicationContext().getFilesDir(), "networkScanResults.txt");
        wifiM = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        wifiR = new WifiScanReciever();
        scanButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                wifiM.startScan();

            }

        });
        networkButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){



            }

        });

        networklist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String grabItemInfo = wifis[position];

                FileOutputStream outputNetData;
                try{
                    outputNetData = openFileOutput("networkScanResults.txt", Context.MODE_PRIVATE);
                    outputNetData.write(grabItemInfo.getBytes());
                    outputNetData.close();

                }catch(Exception e){
                    noOutput.show();
                }

            }
        });



    }

    protected void onPause(){
        unregisterReceiver(wifiR);
        super.onPause();
    }
    protected void onResume(){
        registerReceiver(wifiR, new IntentFilter(wifiM.SCAN_RESULTS_AVAILABLE_ACTION));
        super.onResume();
    }
    public class WifiScanReciever extends BroadcastReceiver{

        public void onReceive(Context c, Intent intent){

            List<ScanResult> wifiSList = wifiM.getScanResults();
            wifis = new String[wifiSList.size()];
            for (int x = 0; x<wifiSList.size(); x++){

                wifis[x] = ((wifiSList.get(x)).toString());

            }
            networklist.setAdapter(new ArrayAdapter<String>(getApplicationContext(),R.layout.net_view_layout,wifis));

        }

    }


}
