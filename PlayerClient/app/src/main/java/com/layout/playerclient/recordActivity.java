package com.layout.playerclient;

import android.content.Intent;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.app.Activity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class recordActivity extends Activity {
    private String[] records;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        ListView listView1 = (ListView) findViewById(R.id.listView);
        intent = getIntent();
        records =intent.getStringArrayExtra("records");
        if(records!=null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, records);

            listView1.setAdapter(adapter);
        }
    }

}
