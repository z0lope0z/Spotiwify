package com.lopefied.spotiwify.auth;

import com.google.common.base.Optional;

/**
 * Created by lope on 4/21/15.
 */
public interface AuthService {
    public static final String AUTH_PREFS = "auth_prefs";
    public static final String AUTH_TOKEN = "auth_token";
    
    public Optional<String> getToken();

    public void cacheToken(String token);
}