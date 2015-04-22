package com.lopefied.spotiwify.auth;

import android.content.Context;

import com.google.common.base.Optional;
import com.google.common.base.Strings;

/**
 * Created by lope on 4/21/15.
 */
public class AuthServiceImpl implements AuthService {
    ObscuredSharedPreferences prefs;

    public AuthServiceImpl(Context context) {
        prefs = new ObscuredSharedPreferences(
                context, context.getSharedPreferences(AUTH_PREFS, Context.MODE_PRIVATE));
    }

    @Override
    public Optional<String> getToken() {
        String token = prefs.getString(AUTH_TOKEN, "");
        if (!Strings.isNullOrEmpty(token)) {
            return Optional.of(token);
        } else
            return Optional.absent();
    }

    @Override
    public void cacheToken(String token) {
        prefs.edit().putString(AUTH_TOKEN, token).apply();
    }
}