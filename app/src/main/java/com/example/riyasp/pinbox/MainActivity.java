package com.example.riyasp.pinbox;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String MTAG = "com.example.riyasp.pinbox.MESSAGE";

    ArrayList<String> bmsMessagesList = new ArrayList<>();
    ArrayList<String> travelMessagesList = new ArrayList<>();
    ArrayList<String> bankMessagesList = new ArrayList<>();

    ListView messages;
    public static ArrayAdapter travelArrayAdapter;
    public static ArrayAdapter bankArrayAdapter;
    public static ArrayAdapter bmsArrayAdapter;
    EditText input;
    private static MainActivity inst;

    private static final int READ_SMS_PERMISSIONS_REQUEST = 1;

    public static MainActivity instance() {
        return inst;
    }

    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        travelArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, travelMessagesList);
        bankArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,bankMessagesList);
        bmsArrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,bmsMessagesList);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            getPermissionToReadSMS();
        } else {
            refreshSmsInbox();
        }
    }

    public void onTicketSMSButtonClick(View view){
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        intent.putExtra(MTAG,"PNR");
        startActivity(intent);
    }
    public void onBankSMSButtonClick(View view){
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        intent.putExtra(MTAG,"bank");
        startActivity(intent);
    }
    public void onBMSSMSButtonClick(View view){
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        intent.putExtra(MTAG,"bms");
        startActivity(intent);
    }
    public void getPermissionToReadSMS() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_SMS)) {
                Toast.makeText(this, "Please allow permission!", Toast.LENGTH_SHORT).show();
            }
            requestPermissions(new String[]{Manifest.permission.READ_SMS},
                    READ_SMS_PERMISSIONS_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        if (requestCode == READ_SMS_PERMISSIONS_REQUEST) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Read SMS permission granted", Toast.LENGTH_SHORT).show();
                refreshSmsInbox();
            } else {
                Toast.makeText(this, "Read SMS permission denied", Toast.LENGTH_SHORT).show();
            }

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void refreshSmsInbox() {

        filterSMSByTag("PNR",travelArrayAdapter);
        filterSMSByTag("BANK",bankArrayAdapter);
        filterSMSByTag("SHOW",bmsArrayAdapter);
    }

    private void filterSMSByTag(String tag,ArrayAdapter adapter) {
        ContentResolver contentResolver = getContentResolver();
        Cursor smsInboxCursor = contentResolver.query(Uri.parse("content://sms/inbox"), null, null, null, null);
        int indexBody = smsInboxCursor.getColumnIndex("body");
        int indexAddress = smsInboxCursor.getColumnIndex("address");
        if (indexBody < 0 || !smsInboxCursor.moveToFirst()) return;
        adapter.clear();
        do {
            if(smsInboxCursor.getString(indexBody).contains(tag)) {
                String str = "SMS From: " + smsInboxCursor.getString(indexAddress) +
                        "\n" + smsInboxCursor.getString(indexBody) + "\n";
                adapter.add(str);
            }
        } while (smsInboxCursor.moveToNext());
    }

    public void updateInbox(final String smsMessage) {
//        arrayAdapter.insert(smsMessage, 0);
//        arrayAdapter.notifyDataSetChanged();
    }

}
