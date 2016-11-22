package com.example.lind42431.unhackableproject;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class Saved_Networks extends Network_List {

    String NetDataArray[];
    ListView NetDataList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_networks);

        NetDataList = (ListView) findViewById(R.id.netdatalist);
        Button updateButton = (Button) findViewById(R.id.buttonUpdate);

        final Cursor SSIDselect = netDataBase.query("netDataTable", new String[]{"NetID", "SSID", "BSSID"}, null, null, null, null, null);


        updateButton.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {

                NetDataArray = new String[SSIDselect.getCount()];
                SSIDselect.moveToFirst();

                for (int x = 0; SSIDselect.getCount() < x; x++) {


                    NetDataArray[x] = SSIDselect.getString(x);


                }
                SSIDselect.close();
                Toast fixtoast = Toast.makeText(getApplicationContext(), NetDataArray[1], Toast.LENGTH_SHORT);
                fixtoast.show();
                //NetDataList.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.net_view_layout, NetDataArray));
            }
        });

    }

}
