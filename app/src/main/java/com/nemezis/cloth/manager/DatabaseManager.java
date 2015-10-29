package com.nemezis.cloth.manager;

import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.util.Log;

import com.nemezis.cloth.App;
import com.nemezis.cloth.model.User;
import com.nemezis.cloth.storio.SQLiteOpenHelperImpl;
import com.nemezis.cloth.storio.UserTypeMapping;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite;

import java.io.IOException;

import rx.functions.Action1;

/**
 * Created by Dmitry Kostyrev on 18/10/15
 */
public class DatabaseManager {

    private static final String LOG_TAG = "Cache";

    private App app;
    private StorIOSQLite storIOSQLite;
    private SQLiteOpenHelper sqLiteOpenHelper;

    public DatabaseManager(App app) {
        this.app = app;
    }

    @WorkerThread
    public void openForUser(@NonNull User user) {
        openForUser(user.getId());
    }

    @WorkerThread
    public void openForUser(@NonNull String userId) {
        close();
        sqLiteOpenHelper = new SQLiteOpenHelperImpl(app, getDatabasePath(userId));
        storIOSQLite = DefaultStorIOSQLite.builder()
                .sqliteOpenHelper(sqLiteOpenHelper)
                .addTypeMapping(User.class, UserTypeMapping.getTypeMapping())
                .build();
    }

    public void close() {
        if (storIOSQLite != null) {
            try {
                storIOSQLite.close();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Failed to close cache", e);
            }
        }
    }

    public @Nullable StorIOSQLite getStorIOSQLite() {
        return storIOSQLite;
    }

    private static String getDatabasePath(String userId) {
        return userId + ".db";
    }
}
