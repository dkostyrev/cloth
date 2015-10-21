package com.nemezis.cloth.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.text.TextUtils;

import com.nemezis.cloth.App;
import com.nemezis.cloth.model.User;
import com.nemezis.cloth.tables.UserTable;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Dmitry Kostyrev on 18/10/15
 */
public class UserManager {

    private static final String SHARED_PREFERENCES = "user";
    private static final String CURRENT_USER = "current_user";

    @Inject DatabaseManager databaseManager;

    private User currentUser;
    private SharedPreferences sharedPreferences;

    public UserManager(App app) {
        app.getApplicationComponent().inject(this);
        sharedPreferences = app.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(CURRENT_USER)) {
            String userId = sharedPreferences.getString(CURRENT_USER, null);
            if (!TextUtils.isEmpty(userId)) {
                switchToCurrentUserDatabaseAndLoadUser(userId);
            }
        }
    }

    @WorkerThread
    public void logIn(@NonNull User user) {
        databaseManager.openForUser(user);
        StorIOSQLite storIOSQLite = databaseManager.getStorIOSQLite();
        if (storIOSQLite != null) {
            storIOSQLite.put().object(user).prepare().executeAsBlocking();
        }
        sharedPreferences.edit().putString(CURRENT_USER, user.getId()).apply();
        this.currentUser = user;
    }

    public void logOut() {
        databaseManager.close();
        sharedPreferences.edit().remove(CURRENT_USER).apply();
        this.currentUser = null;
    }

    public @Nullable User getCurrentUser() {
        return currentUser;
    }

    private void switchToCurrentUserDatabaseAndLoadUser(String userId) {
        databaseManager.openForUser(userId);
        StorIOSQLite storIOSQLite = databaseManager.getStorIOSQLite();
        if (storIOSQLite != null) {
            List<User> users = storIOSQLite.get().listOfObjects(User.class).withQuery(
                    Query.builder()
                            .table(UserTable.TABLE)
                            .where(UserTable.ID + " = ?")
                            .whereArgs(userId)
                            .limit(1)
                            .build())
                    .prepare().executeAsBlocking();
            if (!users.isEmpty()) {
                currentUser = users.get(0);
            } else {
                throw new RuntimeException("Failed to load current user from database by id" + userId);
            }
        }
    }
}
