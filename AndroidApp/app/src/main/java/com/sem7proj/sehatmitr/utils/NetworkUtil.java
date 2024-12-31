package com.sem7project.sehatmitr.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkUtil {
    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                return capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
            } else {
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                return activeNetworkInfo != null && activeNetworkInfo.isConnected();
            }
        }
        return false;
    }

    public static boolean isInternetReachable() {
        try {
            HttpURLConnection urlConnection = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
            urlConnection.setRequestProperty("User-Agent", "test");
            urlConnection.setRequestProperty("Connection", "close");
            urlConnection.setConnectTimeout(1500); // timeout in milliseconds
            urlConnection.connect();
            return (urlConnection.getResponseCode() == 200);
        } catch (IOException e) {
            return false;
        }
    }
}


