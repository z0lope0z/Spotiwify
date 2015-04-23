package com.lopefied.spotiwify.spotify;

/**
 * Created by lope on 4/23/15.
 */
public class Event {
    public static class UserPauseMusicEvent {

    }

    public static class PlayerPlayMusicEvent {
        public String title;

        public PlayerPlayMusicEvent(String title) {
            this.title = title;
        }
    }
}
