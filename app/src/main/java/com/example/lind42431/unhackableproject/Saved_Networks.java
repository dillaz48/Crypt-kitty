package com.example.lind42431.unhackableproject;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class Saved_Networks extends Network_List {

    String NetDataArray[];
    ListView NetDataList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_networks);

        NetDataList = (ListView) findViewById(R.id.netdatalist);
        Button updateButton = (Button) findViewById(R.id.buttonUpdate);

        final Cursor SSIDselect = netDataBase.rawQuery("SELECT SSID FROM NetDataTable", null);


        updateButton.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {

                NetDataArray = new String[SSIDselect.getColumnCount()];

                for (int x = 0; SSIDselect.getColumnCount() < x; x++) {

                    SSIDselect.moveToPosition(x);
                    NetDataArray[x] = SSIDselect.getString(SSIDselect.getColumnIndex("SSID"));

                }
                NetDataList.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.net_view_layout, NetDataArray));
            }
        });

    }
}
