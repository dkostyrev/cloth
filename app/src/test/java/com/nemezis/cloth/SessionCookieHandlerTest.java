package com.nemezis.cloth;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;

import com.nemezis.cloth.network.SessionCookieHandler;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Dmitry Kostyrev on 18/10/15
 */
@RunWith(RobolectricTestRunner.class)
@Config(sdk = 21, constants = BuildConfig.class, manifest = Config.NONE)
public class SessionCookieHandlerTest {

    private static final URI TEST_URI = URI.create("http://www.test.com");
    private static final String WRONG_COOKIE_VALUE = "session=wrong session";
    private static final String CORRECT_COOKIE_VALUE = "_fabric_session=correct session";

    SessionCookieHandler sessionCookieHandler;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    App app;

    @SuppressLint("CommitPrefEdits")
    @Before public void setUp() {
        editor = mock(SharedPreferences.Editor.class);
        sharedPreferences = mock(SharedPreferences.class);
        when(sharedPreferences.edit()).thenReturn(editor);

        app = mock(App.class);
        when(app.getSharedPreferences(anyString(), anyInt())).thenReturn(sharedPreferences);
        sessionCookieHandler = new SessionCookieHandler(app);
    }

    @Test public void testPutNoCookie() throws Exception {
        sessionCookieHandler.put(TEST_URI, createHeadersMap("No-Cookie", "Some value"));
        Map<String, List<String>> headers = sessionCookieHandler.get(TEST_URI, new HashMap<String, List<String>>());
        assertEquals(0, headers.size());
    }

    @Test public void testPutWrongCookie() throws Exception {
        when(editor.putString(anyString(), anyString())).thenReturn(editor);
        sessionCookieHandler.put(TEST_URI, createHeadersMap("Set-Cookie", WRONG_COOKIE_VALUE));
        Map<String, List<String>> headers = sessionCookieHandler.get(TEST_URI, new HashMap<String, List<String>>());
        assertEquals(0, headers.size());
    }

    @Test public void testGetSavedCookie() throws Exception {
        when(sharedPreferences.contains(anyString())).thenReturn(Boolean.TRUE);
        when(sharedPreferences.getString(anyString(), anyString())).thenReturn(CORRECT_COOKIE_VALUE);

        // making sure that cookie handler will read cookie from the shared preferences
        sessionCookieHandler = new SessionCookieHandler(app);
        Map<String, List<String>> headers = sessionCookieHandler.get(TEST_URI, new HashMap<String, List<String>>());
        assertNotEmptyHeaders(headers);
        String retrievedCookieValue = headers.values().iterator().next().get(0);
        assertEquals(CORRECT_COOKIE_VALUE, retrievedCookieValue);
    }

    @Test public void testPutCorrectCookie() throws Exception {
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        when(editor.putString(anyString(), argumentCaptor.capture())).thenReturn(editor);

        sessionCookieHandler.put(TEST_URI, createHeadersMap("Set-Cookie", CORRECT_COOKIE_VALUE));

        Map<String, List<String>> headers = sessionCookieHandler.get(TEST_URI, new HashMap<String, List<String>>());
        assertNotEmptyHeaders(headers);
        String retrievedCookieValue = headers.values().iterator().next().get(0);
        assertEquals(CORRECT_COOKIE_VALUE, retrievedCookieValue);
        assertEquals(CORRECT_COOKIE_VALUE, argumentCaptor.getValue());
    }

    @Test public void testPutThenEraseCorrectCookie() throws Exception {
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        when(editor.putString(anyString(), argumentCaptor.capture())).thenReturn(editor);
        when(editor.remove(anyString())).thenReturn(editor);

        sessionCookieHandler.put(TEST_URI, createHeadersMap("Set-Cookie", CORRECT_COOKIE_VALUE));
        Map<String, List<String>> headers = sessionCookieHandler.get(TEST_URI, new HashMap<String, List<String>>());
        assertNotEmptyHeaders(headers);
        String retrievedCookieValue = headers.values().iterator().next().get(0);
        assertEquals(CORRECT_COOKIE_VALUE, retrievedCookieValue);
        assertEquals(CORRECT_COOKIE_VALUE, argumentCaptor.getValue());

        sessionCookieHandler.eraseCookie();
        headers = sessionCookieHandler.get(TEST_URI, new HashMap<String, List<String>>());
        assertEquals(0, headers.size());
    }

    private void assertNotEmptyHeaders(Map<String, List<String>> headers) {
        assertEquals(1, headers.size());

        Iterator<List<String>> iterator = headers.values().iterator();
        assertTrue(iterator.hasNext());

        List<String> value = iterator.next();
        assertNotNull(value);
        assertEquals(1, value.size());
    }

    private static Map<String, List<String>> createHeadersMap(String header, String value) {
        Map<String, List<String>> map = new HashMap<>();
        map.put(header, Collections.singletonList(value));
        return map;
    }
}
