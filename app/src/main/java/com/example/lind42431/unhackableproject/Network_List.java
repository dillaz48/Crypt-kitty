package com.example.lind42431.unhackableproject;
/**
 * Version 0.68
 * TeamWeeV
 * Network scanner and analyzer
 */

import android.app.Activity;
import android.content.ContentValues;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.wifi.WifiInfo;
import android.support.v4.app.ActivityCompat;
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


import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.jar.Manifest;

import android.database.sqlite.*;

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
    //String searchNetwork;
    //String ssid;
    private String mainCAP;
    private String mainSSID;
    private String mainBSSID;
    SQLiteDatabase netDataBase;

    public void setMainSSID(String newMainSSID){

        mainSSID = newMainSSID;

    }

    public void setMainBSSID(String newMainBSSID){

        mainBSSID = newMainBSSID;

    }

    public void setMainCAP(String newMainCAP){

        mainCAP = newMainCAP;

    }

    public String getMainSSID(){

        return mainSSID;

    }
    public String getMainBSSID(){

        return mainBSSID;

    }
    public String getMainCAP(){

        return mainCAP;

    }

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


        //final EditText searchNet = (EditText) findViewById(R.id.networkEditText);
        networklist = (ListView) findViewById(R.id.networkList);
        final Toast noOutput = Toast.makeText(getApplicationContext(), "Did not output data", Toast.LENGTH_SHORT);

        checkDBExist();
        Button networkButton = (Button) findViewById(R.id.searchButton);
        Button selectButton = (Button) findViewById(R.id.buttonSelect);
        Button scanButton = (Button) findViewById(R.id.buttonScan);//Scan Button

        wifiM = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        wifiI = wifiM.getConnectionInfo();
        wifiR = new WifiScanReciever();
        scanButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                wifiM.startScan(); // Starts scan when they tap the SCAN button

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

                setMainSSID(mainSSID);
                setMainBSSID(mainBSSID);
                setMainCAP(mainCAP);

                ContentValues dbv = new ContentValues();

                dbv.put("SSID", getMainSSID());
                dbv.put("BSSID", getMainBSSID());
                dbv.put("CAPABILITIES", getMainCAP());




                netDataBase.insert("netDataTable", "NULL", dbv);





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
        // Opens the database if it is created and creates if it does not

        netDataBase = openOrCreateDatabase("networkBase", MODE_PRIVATE, null);

        netDataBase.execSQL("CREATE TABLE IF NOT EXISTS netDataTable(NetID int NOT NULL, SSID BLOB, BSSID BLOB, CAPABILITIES BLOB, PRIMARY KEY(NetID));");

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
