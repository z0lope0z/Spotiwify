package com.lopefied.spotiwify;

import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;

import com.google.common.base.Optional;
import com.lopefied.spotiwify.auth.AuthService;
import com.lopefied.spotiwify.auth.AuthServiceImpl;
import com.lopefied.spotiwify.spotify.Event;
import com.lopefied.spotiwify.spotify.SpotiwifyService;
import com.lopefied.spotiwify.spotify.SpotiwifyServiceImpl;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.squareup.seismic.ShakeDetector;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class MainActivity extends ActionBarActivity {

    private static final int REQUEST_CODE_SPOTIFY = 1337;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */

    SpotiwifyService spotiwifyService;
    AuthService authService;
    List<String> tracks = new ArrayList<>();

    @InjectView(R.id.txt_song_title)
    TextView txtSongTitle;

    @OnClick(R.id.btn_stop)
    public void onClick(View view) {
        EventBus.getDefault().post(new Event.UserPauseMusicEvent());
    }

    protected void init() {
        authService = new AuthServiceImpl(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        EventBus.getDefault().register(this);
        init();

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        ShakeDetector sd = new ShakeDetector(new ShakeDetector.Listener() {
            @Override
            public void hearShake() {
                spotiwifyService.onShake();
            }
        });
        sd.start(sensorManager);
        Optional<String> tokenOptional = authService.getToken();
//        if (!tokenOptional.isPresent()) {
        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(SpotiwifyService.CLIENT_ID,
                AuthenticationResponse.Type.TOKEN,
                REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "streaming"});
        AuthenticationRequest request = builder.build();
        AuthenticationClient.openLoginActivity(this, REQUEST_CODE_SPOTIFY, request);
//        } else {
//            spotiwifyService = new SpotiwifyServiceImpl(this);
//            spotiwifyService.initialize(tokenOptional.get());
//        }
    }

    public void onEventMainThread(Event.PlayerPlayMusicEvent event) {
        txtSongTitle.setText("Now playing: " + event.title);
    }

    private static final String REDIRECT_URI = "lopeemano://callback";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE_SPOTIFY) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            if (response.getType() == AuthenticationResponse.Type.TOKEN) {
                authService.cacheToken(response.getAccessToken());
                spotiwifyService = new SpotiwifyServiceImpl(this);
                spotiwifyService.initialize(response.getAccessToken());
            }
        }
    }

}
