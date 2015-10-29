package com.nemezis.cloth.tables;

/**
 * Created by Dmitry Kostyrev on 18/10/15
 */
public class ApplicationTable {

    public static final String TABLE = "Application";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String BUNDLE_IDENTIFIER = "bundle_identifier";
    public static final String BASE_IDENTIFIER = "base_identifier";
    public static final String PLATFORM = "platform";
    public static final String ICON = "icon";
    public static final String ICON_32 = "icon_32";
    public static final String ICON_64 = "icon_64";
    public static final String ICON_128 = "icon_128";
    public static final String ORGANIZATION = "organization";
    public static final String DASHBOARD_URL = "dashboard_url";
    public static final String IMPACTED_DEVICES_COUNT = "impacted_devices_count";
    public static final String UNRESOLVED_ISSUES_COUNT = "unresolved_issues_count";
    public static final String CRASHES_COUNT = "crashes_count";
    public static final String ACCOUNTS_COUNT = "accounts_count";

    public static String getCreateTableQuery() {
        return "CREATE TABLE IF NOT EXISTS '" + TABLE + "' ("
                + ID + " TEXT PRIMARY KEY,"
                + NAME + " TEXT,"
                + BUNDLE_IDENTIFIER + " TEXT,"
                + BASE_IDENTIFIER + " TEXT,"
                + PLATFORM + " TEXT,"
                + ICON + " TEXT,"
                + ICON_32 + " TEXT,"
                + ICON_64 + " TEXT,"
                + ICON_128 + " TEXT,"
                + ORGANIZATION + " TEXT,"
                + DASHBOARD_URL + " TEXT,"
                + IMPACTED_DEVICES_COUNT + " INTEGER,"
                + UNRESOLVED_ISSUES_COUNT + " INTEGER,"
                + CRASHES_COUNT + " INTEGER,"
                + ACCOUNTS_COUNT + " INTEGER"
                + ")";
    }
}
