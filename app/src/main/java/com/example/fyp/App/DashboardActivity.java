package com.example.fyp.App;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.fyp.R;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
    }



    public void launchStats(View v) {
        Intent intent = new Intent(this, statsActivity.class);
        startActivity(intent);
    }

    public void launchFaceTrcker(View view){
        Intent i = new Intent(this, AppFunctionality.class);
        startActivity(i);
    }
}
