package com.example.fyp.App;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import com.example.fyp.Helper.PrefsHelper;
import com.example.fyp.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ContactPickerActivity extends AppCompatActivity {

    private static final String TAG = ContactPickerActivity.class.getSimpleName();
    final int REQUEST_CODE = 1;
    PrefsHelper prefsHelper;
    Boolean isInitialContact = false;
    LinearLayout contactDetailsLayout;
    RelativeLayout noContactLayout;
    ConstraintLayout mainLayout;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_picker);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Emergency Contact Information");
        actionBar.setIcon(R.drawable.camera);
        prefsHelper = new PrefsHelper(this);
        setPageContent();

//        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
//
        //function to check if emergency contact has been set and change page content accordingly

        //set on click listener for contact picker
        Button pickContactButton = (Button)findViewById(R.id.chooseContactButton);

        pickContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "Clicked on contact button", Toast.LENGTH_SHORT).show();
                Uri uri = Uri.parse("content://contacts");
                Intent intent = new Intent(Intent.ACTION_PICK, uri);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        //call button click logic
        Button callButton = findViewById(R.id.callButton);
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent phoneIntent = new Intent(Intent.ACTION_CALL);
                String phoneNumber = prefsHelper.getEmergencyContactNumber();
                phoneIntent.setData(Uri.parse("tel:"+phoneNumber));
                if (ActivityCompat.checkSelfPermission(ContactPickerActivity.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    Log.d("phone call button", "no permission");
                    final int REQUEST_PHONE_CALL = 1;
                    ActivityCompat.requestPermissions(ContactPickerActivity.this,
                            new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL
                            );
                    return;
                }
                Log.d("phone call button", "calling");
                startActivity(phoneIntent);
            }
        });


    }

    public void setPageContent(){

        //check if emergency contacts have been set
        String contact_name = prefsHelper.getEmergencyContactName();
        String contact_number = prefsHelper.getEmergencyContactNumber();
        TextView messageTextView = findViewById(R.id.contactMessageTextView);
        Button pickContactButton = findViewById(R.id.chooseContactButton);
        Button callButton = findViewById(R.id.callButton);
        TextView contactNameTextView = findViewById(R.id.contactNameTextView);
        TextView contactNumberTextView = findViewById(R.id.contactNumberTextView);
        contactDetailsLayout = findViewById(R.id.contact_details_layout);
        noContactLayout =  findViewById(R.id.no_contact_layout);
        mainLayout =  findViewById(R.id.contact_picker_main_layout);

        if(contact_name!=null)
        {
            messageTextView.setText("Current Emergency Contact");
            pickContactButton.setText("Change Contact");
            contactNameTextView.setText(contact_name);
            contactNumberTextView.setText(contact_number);
            callButton.setText("Call "+contact_name);
            callButton.setVisibility(Button.VISIBLE);
            contactDetailsLayout.setVisibility(View.VISIBLE);
            noContactLayout.setVisibility(View.GONE);
        }
        else
        {
            messageTextView.setText("");
            pickContactButton.setText("Pick Contact");
            contactNameTextView.setText("");
            contactNumberTextView.setText("");
            contactDetailsLayout.setVisibility(View.GONE);
            noContactLayout.setVisibility(View.VISIBLE);
            callButton.setVisibility(Button.INVISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        //check if contact picking worked
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri uri = intent.getData();
                String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};

                Cursor cursor = getContentResolver().query(uri, projection,
                        null, null, null);

                if(cursor != null) {
                    cursor.moveToFirst();

                    int numberColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    String number = cursor.getString(numberColumnIndex);

                    int nameColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                    String name = cursor.getString(nameColumnIndex);



                    prefsHelper.setEmergencyContactName(name);
                    prefsHelper.setEmergencyContactNumber(number);
                    contactDetailsLayout.setVisibility(View.VISIBLE);
                    noContactLayout.setVisibility(View.GONE);

                    cursor.close();
                    setPageContent();
                    if(isInitialContact && prefsHelper.getEmergencyContactName() != null && prefsHelper.getEmergencyContactNumber()!=null) {
                        Snackbar snackbar = Snackbar
                                .make(mainLayout, "Contact Successfully Added", Snackbar.LENGTH_LONG);
                        snackbar.show();
                        try {
                            ScheduledFuture<?> countdown = scheduler.schedule(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                }
                            }, 3000, TimeUnit.MILLISECONDS);
                        } catch (Exception e) {
                            Log.e(TAG, "Contact successfully added timer Exception:" + e.toString());
                        }
                    } else{
                        Snackbar snackbar = Snackbar
                                .make(mainLayout, "Contact Successfully Changed", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                }
            }
        }
    }


    @Override
    public void onBackPressed() {
        if(isInitialContact && prefsHelper.getEmergencyContactName() != null && prefsHelper.getEmergencyContactNumber()!=null){
            Intent homeIntent = new Intent(this, DashboardActivity.class);
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
        } else {
            super.onBackPressed();
        }
    }
}
