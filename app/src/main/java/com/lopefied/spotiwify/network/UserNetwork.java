package com.lopefied.spotiwify.network;

import io.realm.RealmObject;

/**
 * Created by lope on 4/15/15.
 */
public class UserNetwork extends RealmObject {
    private String networkName;

    public String getNetworkName() {
        return networkName;
    }

    public void setNetworkName(String networkName) {
        this.networkName = networkName;
    }
}
