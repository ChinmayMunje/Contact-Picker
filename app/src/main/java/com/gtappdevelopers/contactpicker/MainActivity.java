package com.gtappdevelopers.contactpicker;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private Button pickContactBtn;
    private TextView contactNameTV, contactNumberTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pickContactBtn = findViewById(R.id.idBtnPickContact);
        contactNameTV = findViewById(R.id.idTVContactName);
        contactNumberTV = findViewById(R.id.idTVContactNumber);

        pickContactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasContactsPermission()) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                    startActivityForResult(intent, 1);
                } else {
                    requestContactsPermission();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;

        if (requestCode == 1 && data != null) {
//            Uri contactUri = data.getData();
//
//            // Specify which fields you want your
//            // query to return values for
//            String[] queryFields = new String[]{ContactsContract.Contacts.DISPLAY_NAME};
//
//            // Perform your query - the contactUri
//            // is like a "where" clause here
//            Cursor cursor = this.getContentResolver()
//                    .query(contactUri, queryFields, null, null, null);
//            try {
//                // Double-check that you
//                // actually got results
//                if (cursor.getCount() == 0) return;
//
//                // Pull out the first column
//                // of the first row of data
//                // that is your contact's name
//                cursor.moveToFirst();
//
//                String name = cursor.getString(0);
//                String number = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                Log.e("TAG", number);
//
//                contactNameTV.setText(name);
//
//            } finally {
//                cursor.close();
//            }

            Uri contactData = data.getData();
            Cursor cursor = managedQuery(contactData, null, null, null, null);
            cursor.moveToFirst();

            String number = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            contactNameTV.setText("Name : "+name);
            contactNumberTV.setText("Number : "+number);
        }
    }

    private boolean hasContactsPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) ==
                PackageManager.PERMISSION_GRANTED;
    }


    private void requestContactsPermission() {
        if (!hasContactsPermission()) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS}, 1);
        }
    }

}