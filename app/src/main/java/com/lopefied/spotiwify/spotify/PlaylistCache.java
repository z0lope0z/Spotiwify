package com.lopefied.spotiwify.spotify;

import io.realm.RealmObject;

/**
 * Created by lope on 4/15/15.
 */
public class PlaylistCache extends RealmObject {
    private String key;
    private String title;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
