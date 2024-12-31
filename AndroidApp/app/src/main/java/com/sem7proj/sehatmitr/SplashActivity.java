package com.sem7project.sehatmitr;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.sem7project.sehatmitr.utils.NetworkUtil;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // check for inte if(!NetworkUtil.isInternetAvailable(this) || !NetworkUtil.isInternetReachable()){
        ////            showNoInternetDialog();
        ////        }
        ////        else{
        ////            Handler handler = new Handler();  // creates a thread that after some seconds will call the run method
        ////            handler.postDelayed(new Runnable() {
        ////                @Override
        ////                public void run() {
        ////                    startActivity(new Intent(SplashActivity.this, LoginPage.class));
        ////                    finish(); // this doesnt load the activity again
        ////                }
        ////            }, 2500);
        ////        }rnet connectivity
//
        Handler handler = new Handler();  // creates a thread that after some seconds will call the run method
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, LoginPage.class));
                finish(); // this doesnt load the activity again
            }
        }, 2000);

    }

    private void showNoInternetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No Internet")
                .setMessage("Please check your internet connection and try again.")
                .setCancelable(false) // Prevent dismissal by tapping outside
                .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Exit the app
                        finishAffinity(); // Close the app
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

}