package com.nemezis.cloth.tables;

/**
 * Created by Dmitry Kostyrev on 18/10/15
 */
public class UserTable {

    public static final String TABLE = "Users";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String TOKEN = "token";
    public static final String CURRENT_ORGANIZATION = "currentOrganization";
    public static final String ORGANIZATIONS = "organizations";

    public static String getCreateTableQuery() {
        return "CREATE TABLE IF NOT EXISTS '" + TABLE + "' "
                + ID + " TEXT PRIMARY KEY,"
                + NAME + " TEXT,"
                + EMAIL + " TEXT,"
                + TOKEN + " TEXT,"
                + CURRENT_ORGANIZATION + " TEXT,"
                + ORGANIZATIONS + " TEXT";
    }
}
