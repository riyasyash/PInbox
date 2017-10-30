package com.example.riyasp.pinbox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class DisplayMessageActivity extends AppCompatActivity {

    ListView messages;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);
        messages = (ListView) findViewById(R.id.messages);
        Intent intent = getIntent();
        String tag = intent.getStringExtra(MainActivity.MTAG);
        messages = (ListView) findViewById(R.id.messages);
        if(tag.equals("PNR")){
            messages.setAdapter(MainActivity.travelArrayAdapter);
        }
        else if(tag.equals("bank")){
            messages.setAdapter(MainActivity.bankArrayAdapter);
        }
        else if (tag.equals("bms")){
            messages.setAdapter(MainActivity.bmsArrayAdapter);
        }

    }
}
