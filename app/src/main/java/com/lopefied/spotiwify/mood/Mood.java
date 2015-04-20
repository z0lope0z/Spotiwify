package com.lopefied.spotiwify.mood;

import com.lopefied.spotiwify.network.UserNetwork;
import com.lopefied.spotiwify.spotify.PlaylistCache;

import org.joda.time.DateTime;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Mood = work, sleep, play
 * Created by lope on 4/15/15.
 */
public class Mood extends RealmObject {
    @PrimaryKey
    private String name;
    private int hourOfDay;
    private int dayOfWeek;
    private RealmList<UserNetwork> networks;
    private RealmList<PlaylistCache> albums;

    public Mood() {
    }

    public Mood(String name, DateTime dateTime) {
        this.name = name;
        this.hourOfDay = dateTime.getHourOfDay();
        this.dayOfWeek = dateTime.getDayOfWeek();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHourOfDay() {
        return hourOfDay;
    }

    public void setHourOfDay(int hourOfDay) {
        this.hourOfDay = hourOfDay;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public RealmList<UserNetwork> getNetworks() {
        return networks;
    }

    public void setNetworks(RealmList<UserNetwork> networks) {
        this.networks = networks;
    }

    public RealmList<PlaylistCache> getAlbums() {
        return albums;
    }

    public void setAlbums(RealmList<PlaylistCache> albums) {
        this.albums = albums;
    }
}
