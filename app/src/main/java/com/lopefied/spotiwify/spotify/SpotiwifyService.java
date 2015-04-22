package com.lopefied.spotiwify.spotify;

import android.net.NetworkInfo;

/**
 * Created by lope on 4/16/15.
 */
public interface SpotiwifyService {
    // TODO: Replace with your client ID
    public static final String CLIENT_ID = "f67f1d3879b74b898c9fa8371a37fb3f";

    public void initialize(String accessToken);

    public void onShake();

    public void onNetworkChanged(NetworkInfo networkInfo);

    public void onHourChanged(int hour);
}
