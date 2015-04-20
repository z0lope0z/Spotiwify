package com.lopefied.spotiwify.spotify;

import android.net.NetworkInfo;

/**
 * Created by lope on 4/16/15.
 */
public interface SpotiwifyService {
    public void onShake();

    public void onNetworkChanged(NetworkInfo networkInfo);

    public void onHourChanged(int hour);
}
