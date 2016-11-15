package com.example.lind42431.unhackableproject;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.database.sqlite.*;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;


/**
 * Created by teamWeeV on 11/3/2016.
 * Version: 0.68
 */

public class Attack_Page extends Network_List {


    protected void onCreate(Bundle SavedIS){

        super.onCreate(SavedIS);
        setContentView(R.layout.attack_page);


        TextView SSIDview = (TextView) findViewById(R.id.SSIDView);
        TextView BSSIDview = (TextView) findViewById(R.id.BSSIDView);
        TextView CAPview = (TextView) findViewById(R.id.CAPView);
        Button backButton = (Button) findViewById(R.id.backbutton);

        Cursor IDselect = netDataBase.rawQuery("SELECT SSID FROM netDataTable WHERE SSID = "+getMainSSID()+"", wifis);

        SSIDview.setText(IDselect.toString());


        backButton.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){

                Intent bintent = new Intent(getApplicationContext(), Network_List.class);
                startActivity(bintent);

            }


        });

    }



}
