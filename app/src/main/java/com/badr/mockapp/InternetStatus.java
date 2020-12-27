package com.badr.mockapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class InternetStatus {

    public static boolean isConnect(Context mContext) {

        ConnectivityManager manager = (ConnectivityManager) mContext.getSystemService(CONNECTIVITY_SERVICE);
        assert manager != null;
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected())
        {
            Log.i("TAG", "connected network state...");
            return true;
        }
        else
        {
            return false;
        }
    }

}
