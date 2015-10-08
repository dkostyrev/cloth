package com.nemezis.cloth.model;

import java.net.HttpCookie;

/**
 * Created by Dmitry Kostyrev on 07/10/15
 */
public class AuthorizationInfo {
    public final String csrfToken;
    public final HttpCookie cookie;

    public AuthorizationInfo(HttpCookie cookie, String csrfToken) {
        this.cookie = cookie;
        this.csrfToken = csrfToken;
    }
}
