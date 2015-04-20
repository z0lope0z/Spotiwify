package com.lopefied.spotiwify.spotify;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.google.common.base.Strings;
import com.lopefied.spotiwify.receiver.AlarmReceiver;

import java.util.Calendar;

/**
 * Created by lope on 4/16/15.
 */
public class SpotiwifyServiceImpl implements SpotiwifyService {
    Context mContext;

    @Override
    public void onShake() {

    }

    @Override
    public void onNetworkChanged(NetworkInfo netInfo) {
        if (netInfo.isConnected()) {
            if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                final WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
                final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
                if (connectionInfo != null && !Strings.isNullOrEmpty(connectionInfo.getSSID())) {

                }
            } else if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                ConnectivityManager connMgr = (ConnectivityManager)
                        mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            }
        }
        if (netInfo != null && netInfo.getType() == ConnectivityManager.TYPE_WIFI)
            Log.d("WifiReceiver", "Have Wifi Connection");
        else
            Log.d("WifiReceiver", "Don't have Wifi Connection");
    }

    @Override
    public void onHourChanged(int hour) {
        AlarmManager alarmMgr = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(mContext, AlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(mContext, 0, intent, 0);
        // Wake up the device to fire the alarm at precisely 8:30 a.m., and every 20 minutes thereafter:
        // Set the alarm to start at 8:30 a.m.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 30);
        // setRepeating() lets you specify a precise custom interval--in this case,
        // 20 minutes.
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                1000 * 60 * 20, alarmIntent);
        alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                AlarmManager.INTERVAL_HOUR,
                AlarmManager.INTERVAL_HOUR, alarmIntent);
    }
}
