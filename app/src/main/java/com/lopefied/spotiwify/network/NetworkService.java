package com.lopefied.spotiwify.network;

import java.util.List;

/**
 * Created by lope on 4/15/15.
 */
public interface NetworkService {
    public void onNetworkChanged(String name);

    public List<UserNetwork> getNetworkList();
}