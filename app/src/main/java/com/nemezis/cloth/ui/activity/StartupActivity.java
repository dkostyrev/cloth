package com.nemezis.cloth.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.nemezis.cloth.App;
import com.nemezis.cloth.di.component.ApplicationComponent;
import com.nemezis.cloth.manager.UserManager;
import com.nemezis.cloth.ui.login.LoginActivity;

import javax.inject.Inject;

/**
 * Created by Dmitry Kostyrev on 21/10/15
 */
public class StartupActivity extends Activity {

    @Inject
    UserManager userManager;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.get(getApplicationContext()).getApplicationComponent().inject(this);
        if (userManager.getCurrentUser() != null) {
            startActivity(new Intent(this, MainActivity.class));
        } else {
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}
