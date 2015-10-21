package com.nemezis.cloth.storio;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nemezis.cloth.tables.ApplicationTable;
import com.nemezis.cloth.tables.OrganizationTable;
import com.nemezis.cloth.tables.UserTable;

/**
 * Created by Dmitry Kostyrev on 18/10/15
 */
public class SQLiteOpenHelperImpl extends SQLiteOpenHelper {

    private static final int VERSION = 1;

    public SQLiteOpenHelperImpl(Context context, String name) {
        super(context, name, null, VERSION);
    }

    @Override public void onCreate(SQLiteDatabase db) {
        db.execSQL(UserTable.getCreateTableQuery());
        db.execSQL(OrganizationTable.getCreateTableQuery());
        db.execSQL(ApplicationTable.getCreateTableQuery());
    }

    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
