package com.example.lind42431.unhackableproject;
/**
 * Version 0.9
 * TeamWeeV
 * Network scanner and analyzer
 */

import android.content.ContentValues;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import android.content.Context;
import android.content.BroadcastReceiver;
import android.content.Intent;

import java.io.IOException;
import java.util.List;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import android.widget.Toast;



public class Network_List extends AppCompatActivity {

    WifiManager wifiM;  //Wifi manager used for scanning
    WifiScanReciever wifiR; //Wifi scan recieves
    WifiInfo wifiI;
    ListView networklist;
    String wifis[];
    String netid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network__list);
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        final Toast noSU = Toast.makeText(getApplicationContext(), "Did not get root access!", Toast.LENGTH_SHORT);
        try {
            Process p = Runtime.getRuntime().exec("su");
        }catch(IOException e){
            noSU.show();
        }


        networklist = (ListView) findViewById(R.id.networkList);
        final Toast noOutput = Toast.makeText(getApplicationContext(), "Did not output data", Toast.LENGTH_SHORT);

        Button savedButton = (Button) findViewById(R.id.buttonSaved);
        Button scanButton = (Button) findViewById(R.id.buttonScan);//Scan Button

        wifiM = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiI = wifiM.getConnectionInfo();
        wifiR = new WifiScanReciever();
        scanButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                wifiM.startScan(); // Starts scan when they tap the SCAN button

            }

        });


        networklist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String grabItemInfo = wifis[position];

                Network_Info info1 = new Network_Info();

                info1.setMainBSSID( grabItemInfo.substring(grabItemInfo.indexOf('#') +1, grabItemInfo.lastIndexOf('#')));
                info1.setMainSSID( grabItemInfo.substring(0,(grabItemInfo.indexOf('#'))));
                info1.setMainCAP( grabItemInfo.substring(grabItemInfo.lastIndexOf('#')+1, grabItemInfo.length()));



                ContentValues dbv = new ContentValues();

                dbv.put("SSID", info1.getMainSSID());
                dbv.put("BSSID", info1.getMainBSSID());
                dbv.put("CAPABILITIES", info1.getMainCAP());


                Intent intent = new Intent(getApplicationContext(), Attack_Page.class);
                intent.putExtra("EXTRA_SSID", info1.getMainSSID());
                intent.putExtra("EXTRA_BSSID", info1.getMainBSSID());
                intent.putExtra("EXTRA_CAP", info1.getMainCAP());
                startActivity(intent);

            }
        });

        savedButton.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){

                Intent sNetworks = new Intent(getApplicationContext(), Saved_Networks.class);
                startActivity(sNetworks);

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


    // Wifi Scan receiver
    public class WifiScanReciever extends BroadcastReceiver{

        public void onReceive(Context c, Intent intent){
            //When received put the scan results in a list
            List<ScanResult> wifiSList = wifiM.getScanResults();
            //Size the array to the size of the list
            wifis = new String[wifiSList.size()];
            //Loop to grab information for every received network point
            for (int x = 0; x<wifiSList.size(); x++){
                //Get SSID BSSID capabilites
                wifis[x] = ((wifiSList.get(x).SSID)+"#"+wifiSList.get(x).BSSID)+"#"+wifiSList.get(x).capabilities;

            }
            networklist.setAdapter(new ArrayAdapter<String>(getApplicationContext(),R.layout.net_view_layout,wifis));

        }

    }


}