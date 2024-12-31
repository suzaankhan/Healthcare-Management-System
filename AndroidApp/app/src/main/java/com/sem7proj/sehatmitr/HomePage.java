package com.sem7project.sehatmitr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.Toast;

public class HomePage extends AppCompatActivity {

    // user data varibles
    private String recievedUID;

    // UI elements
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        toolbar = findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);

        recievedUID = getIntent().getExtras().getString("uid");
        Toast.makeText(this, "Welcome, UID : " + recievedUID, Toast.LENGTH_SHORT).show();
    }
}