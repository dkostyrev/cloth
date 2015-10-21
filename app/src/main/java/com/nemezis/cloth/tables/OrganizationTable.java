package com.nemezis.cloth.tables;

/**
 * Created by Dmitry Kostyrev on 18/10/15
 */
public class OrganizationTable {

    public static final String TABLE = "Organization";
    public static final String ID = "id";
    public static final String ALIAS = "alias";
    public static final String NAME = "name";
    public static final String API_KEY = "api_key";

    public static String getCreateTableQuery() {
        return "CREATE TABLE IF NOT EXISTS '" + TABLE + "' "
                + ID + " TEXT PRIMARY KEY,"
                + ALIAS + " TEXT,"
                + NAME + " TEXT,"
                + API_KEY + " TEXT";
    }
}
