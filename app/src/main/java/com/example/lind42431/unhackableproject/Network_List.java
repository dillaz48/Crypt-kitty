package com.example.lind42431.unhackableproject;
/**
 * Version 0.5
 * TeamWeeV
 * Network scanner and analyzer
 */

import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
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


import java.util.List;

import android.database.sqlite.*;

import android.net.wifi.ScanResult;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiConfiguration;
import android.net.NetworkInfo;

import android.widget.Toast;



public class Network_List extends AppCompatActivity {

    WifiManager wifiM;  //Wifi manager used for scanning
    WifiScanReciever wifiR; //Wifi scan recieves
    WifiInfo wifiI;
    ListView networklist;
    String wifis[];
    //String searchNetwork;
    //String ssid;
    String mainCAP;
    String mainSSID;
    String mainBSSID;
    SQLiteDatabase checkdb;
    SQLiteDatabase netDataBase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network__list);




        //final EditText searchNet = (EditText) findViewById(R.id.networkEditText);
        networklist = (ListView) findViewById(R.id.networkList);
        final Toast noOutput = Toast.makeText(getApplicationContext(), "Did not output data", Toast.LENGTH_SHORT);

        checkDBExist();
        Button networkButton = (Button) findViewById(R.id.searchButton);
        Button selectButton = (Button) findViewById(R.id.buttonSelect);
        Button scanButton = (Button) findViewById(R.id.buttonScan);

        wifiM = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        wifiI = wifiM.getConnectionInfo();
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


                mainBSSID = grabItemInfo.substring(grabItemInfo.indexOf('#') +1, grabItemInfo.lastIndexOf('#'));
                mainSSID = grabItemInfo.substring(0,(grabItemInfo.indexOf('#')));
                mainCAP = grabItemInfo.substring(grabItemInfo.lastIndexOf('#')+1, grabItemInfo.length());

                //// TODO: 11/3/2016 Fix SQL 
                netDataBase.execSQL("DECLARE @mainSSID BLOB(50); "+
                        "DECLARE @mainBSSID BLOB(17); " +
                        "DECLARE @mainCAP BLOB(50); " +
                        "SET @mainSSID = '"+mainSSID+"'; " +
                        "SET @mainBSSID = '"+mainBSSID+"'; " +
                        "SET @mainCAP = '"+mainCAP+"'; ");

                netDataBase.execSQL("INSERT INTO netData(SSID, BSSID, CAPABILITIES) VALUES(@mainSSID, @mainBSSID, @mainCAP)");
                Intent intent = new Intent(getApplicationContext(), Attack_Page.class);
                startActivity(intent);



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

    private void checkDBExist(){
        //Catches the SQLiteCantOpenDatabaseException when opening the database throws and exception
        try {
            checkdb = SQLiteDatabase.openDatabase("savedNetDB", null, SQLiteDatabase.OPEN_READONLY);



        }catch(SQLiteCantOpenDatabaseException e){
            //Create the database when caught
            netDataBase = openOrCreateDatabase("savedNetDB", MODE_PRIVATE,null);
            netDataBase.execSQL("CREATE TABLE IF NOT EXISTS netData(SSID VARCHAR, BSSID VARCHAR, CAPABILITIES VARCHAR);");

        }

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
                //Get SSID BSSID capabilites when
                wifis[x] = ((wifiSList.get(x).SSID)+"#"+wifiSList.get(x).BSSID)+"#"+wifiSList.get(x).capabilities;

            }
            networklist.setAdapter(new ArrayAdapter<String>(getApplicationContext(),R.layout.net_view_layout,wifis));

        }

    }


}
