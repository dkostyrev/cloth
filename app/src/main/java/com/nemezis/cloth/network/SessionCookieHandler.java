package com.nemezis.cloth.network;

import android.content.Context;
import android.content.SharedPreferences;

import com.nemezis.cloth.App;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.HttpCookie;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * Created by Dmitry Kostyrev on 08/10/15
 */
public class SessionCookieHandler extends CookieHandler {

    private static final String SHARED_PREF_NAME = "cookie.handler";
    private static final String SHARED_PREF_COOKIE = "cookie";
    private static final String SET_COOKIE = "Set-Cookie";
    private static final String COOKIE = "Cookie";
    private static final String SESSION_NAME = "_fabric_session";

    private SharedPreferences sharedPreferences;
    private List<String> cookie;

    public SessionCookieHandler(App applicationContext) {
        cookie = new Vector<>(1);
        sharedPreferences = applicationContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(SHARED_PREF_COOKIE)) {
            fillCookieListFromString(sharedPreferences.getString(SHARED_PREF_COOKIE, null));
        }
    }

    public Map<String, List<String>> get(URI uri, Map<String, List<String>> requestHeaders) throws IOException {
        if (!cookie.isEmpty()) {
            Map<String, List<String>> headers = new HashMap<>(requestHeaders);
            headers.put(COOKIE, cookie);
            return Collections.unmodifiableMap(headers);
        }
        return requestHeaders;
    }

    @Override
    public void put(URI uri, Map<String, List<String>> responseHeaders) throws IOException {
        if (responseHeaders.containsKey(SET_COOKIE)) {
            List<String> cookiesList = responseHeaders.get(SET_COOKIE);
            for (String cookie : cookiesList) {
                List<HttpCookie> httpCookies = HttpCookie.parse(cookie);
                for (HttpCookie httpCookie : httpCookies) {
                    if (httpCookie.getName().equals(SESSION_NAME)) {
                        saveCookie(httpCookie);
                        break;
                    }
                }
            }
        }
    }

    public void eraseCookie() {
        cookie.clear();
        sharedPreferences.edit().remove(SHARED_PREF_COOKIE).apply();
    }

    private void saveCookie(HttpCookie httpCookie) {
        String cookie = httpCookie.getName() + "=" + httpCookie.getValue();
        fillCookieListFromString(cookie);
        sharedPreferences.edit().putString(SHARED_PREF_COOKIE, cookie).apply();
    }

    private void fillCookieListFromString(String string) {
        cookie.clear();
        cookie.add(string);
    }
}
