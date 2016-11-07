package com.example.lind42431.unhackableproject;

import android.content.Intent;
import android.os.Bundle;
import android.database.sqlite.*;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


/**
 * Created by teamWeeV on 11/3/2016.
 * Version: 0.5
 */

public class Attack_Page extends Network_List {

    protected void onCreate(Bundle SavedIS){

        super.onCreate(SavedIS);
        setContentView(R.layout.attack_page);


        TextView SSIDview = (TextView) findViewById(R.id.SSIDView);
        TextView BSSIDview = (TextView) findViewById(R.id.BSSIDView);
        TextView CAPview = (TextView) findViewById(R.id.CAPView);
        Button backButton = (Button) findViewById(R.id.backbutton);

        SSIDview.setText(mainSSID);
        BSSIDview.setText(mainBSSID);
        CAPview.setText(mainCAP);

        backButton.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){

                Intent bintent = new Intent(getApplicationContext(), Network_List.class);
                startActivity(bintent);

            }


        });

    }



}
