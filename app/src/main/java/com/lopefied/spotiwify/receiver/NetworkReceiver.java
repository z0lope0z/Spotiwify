package com.lopefied.spotiwify.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.lopefied.spotiwify.spotify.SpotiwifyService;

/**
 * Created by lope on 4/15/15.
 */
public class NetworkReceiver extends BroadcastReceiver {

    SpotiwifyService spotiwifyService;

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMan.getActiveNetworkInfo();
//        spotiwifyService.onNetworkChanged(netInfo);
    }
}
