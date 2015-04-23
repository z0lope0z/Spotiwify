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
import com.google.common.math.DoubleMath;
import com.lopefied.spotiwify.receiver.AlarmReceiver;
import com.lopefied.spotiwify.time.TimeServiceImpl;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerNotificationCallback;
import com.spotify.sdk.android.player.PlayerState;
import com.spotify.sdk.android.player.Spotify;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.greenrobot.event.EventBus;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.Playlist;
import kaaes.spotify.webapi.android.models.PlaylistTrack;
import kaaes.spotify.webapi.android.models.Track;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by lope on 4/16/15.
 */
public class SpotiwifyServiceImpl implements SpotiwifyService {
    Context context;
    List<String> albumIds = new ArrayList<>();
    List<String> trackIds = new ArrayList<>();

    private Player mPlayer;
    TimeServiceImpl timeService;
    SpotifyService spotify;

    public SpotiwifyServiceImpl(Context context) {
        this.context = context;
        this.timeService = new TimeServiceImpl();
        EventBus.getDefault().register(this);
    }

    @Override
    public void initialize(String accessToken) {
        initPlayer(accessToken);
        SpotifyApi api = new SpotifyApi();
        api.setAccessToken(accessToken);
        spotify = api.getService();

        spotify.getPlaylists("lopegwapo", new Callback<Pager<Playlist>>() {
            @Override
            public void success(Pager<Playlist> playlistPager, Response response) {
                Pattern pattern = Pattern.compile("^\\[([A-Z]+)\\]");
                for (Playlist playlist : playlistPager.items) {
                    Matcher matcher = pattern.matcher(playlist.name);
                    if (matcher.find()) {
                        String group = matcher.group(1);
                        if (timeService.getTagsNow().contains(group))
                            albumIds.add(playlist.id);
                    }
                }
                for (String albumID : albumIds) {
                    spotify.getPlaylist("lopegwapo", albumID, new Callback<Playlist>() {
                        @Override
                        public void success(Playlist playlist, Response response) {
                            for (PlaylistTrack track : playlist.tracks.items) {
                                trackIds.add(track.track.id);
                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {

                        }
                    });
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    @Override
    public void onShake() {
        Random r = new Random();
        int rangeMin = 0;
        int rangeMax = trackIds.size();
        int randomValue = DoubleMath.roundToInt(rangeMin + (rangeMax - rangeMin) * r.nextDouble(), RoundingMode.FLOOR);
        String selectedTrack = trackIds.get(randomValue);
        mPlayer.play("spotify:track:" + selectedTrack);
        if (spotify != null) {
            spotify.getTrack(selectedTrack, new Callback<Track>() {
                @Override
                public void success(Track track, Response response) {
                    EventBus.getDefault().post(new Event.PlayerPlayMusicEvent(track.name));
                }

                @Override
                public void failure(RetrofitError error) {
                    System.out.println("");
                }
            });
        }
    }

    @Override
    public void onNetworkChanged(NetworkInfo netInfo) {
        if (netInfo.isConnected()) {
            if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
                if (connectionInfo != null && !Strings.isNullOrEmpty(connectionInfo.getSSID())) {

                }
            } else if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                ConnectivityManager connMgr = (ConnectivityManager)
                        context.getSystemService(Context.CONNECTIVITY_SERVICE);
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
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
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

    public void onEventMainThread(Event.UserPauseMusicEvent event) {
        if (mPlayer != null) {
            mPlayer.pause();
        }
    }

    protected void initPlayer(String token) {
        Config playerConfig = new Config(context, token, CLIENT_ID);
        mPlayer = Spotify.getPlayer(playerConfig, this, new Player.InitializationObserver() {
            @Override
            public void onInitialized(Player player) {
                mPlayer.addConnectionStateCallback(new ConnectionStateCallback() {
                    @Override
                    public void onLoggedIn() {
                        System.out.println("");
                    }

                    @Override
                    public void onLoggedOut() {
                        System.out.println("");
                    }

                    @Override
                    public void onLoginFailed(Throwable throwable) {
                        System.out.println("");
                    }

                    @Override
                    public void onTemporaryError() {
                        System.out.println("");
                    }

                    @Override
                    public void onConnectionMessage(String s) {
                        System.out.println("");
                    }
                });
                mPlayer.addPlayerNotificationCallback(new PlayerNotificationCallback() {
                    @Override
                    public void onPlaybackEvent(EventType eventType, PlayerState playerState) {

                    }

                    @Override
                    public void onPlaybackError(ErrorType errorType, String s) {

                    }
                });
            }

            @Override
            public void onError(Throwable throwable) {
                Log.e("MainActivity", "Could not initialize player: " + throwable.getMessage());
            }
        });
    }
}
