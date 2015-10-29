package com.nemezis.cloth.model;

import com.google.gson.annotations.SerializedName;
import com.nemezis.cloth.tables.ApplicationTable;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

/**
 * Created by Dmitry Kostyrev on 12/10/15
 */
@StorIOSQLiteType(table = ApplicationTable.TABLE)
public class Application {

    @StorIOSQLiteColumn(name = ApplicationTable.ID, key = true)
    String id;

    @StorIOSQLiteColumn(name = ApplicationTable.NAME)
    String name;

    @SerializedName("bundle_identifier")
    @StorIOSQLiteColumn(name = ApplicationTable.BUNDLE_IDENTIFIER)
    String bundleIdentifier;

    @SerializedName("base_identifier")
    @StorIOSQLiteColumn(name = ApplicationTable.BASE_IDENTIFIER)
    String baseIdentifier;

    @StorIOSQLiteColumn(name = ApplicationTable.PLATFORM)
    String platform;

    @SerializedName("icon_url")
    @StorIOSQLiteColumn(name = ApplicationTable.ICON)
    String iconUrl;

    @SerializedName("icon32_url")
    @StorIOSQLiteColumn(name = ApplicationTable.ICON_32)
    String icon32Url;

    @SerializedName("icon64_url")
    @StorIOSQLiteColumn(name = ApplicationTable.ICON_64)
    String icon64Url;

    @SerializedName("icon128_url")
    @StorIOSQLiteColumn(name = ApplicationTable.ICON_128)
    String icon128Url;

    @SerializedName("organization_id")
    @StorIOSQLiteColumn(name = ApplicationTable.ORGANIZATION)
    String organizationId;

    @SerializedName("dashboard_url")
    @StorIOSQLiteColumn(name = ApplicationTable.DASHBOARD_URL)
    String dashboardUrl;

    @SerializedName("impacted_devices_count")
    @StorIOSQLiteColumn(name = ApplicationTable.IMPACTED_DEVICES_COUNT)
    int impactedDevicesCount;

    @SerializedName("unresolved_issues_count")
    @StorIOSQLiteColumn(name = ApplicationTable.UNRESOLVED_ISSUES_COUNT)
    int unresolvedIssuesCount;

    @SerializedName("crashes_count")
    @StorIOSQLiteColumn(name = ApplicationTable.CRASHES_COUNT)
    int crashesCount;

    @SerializedName("accounts_count")
    @StorIOSQLiteColumn(name = ApplicationTable.ACCOUNTS_COUNT)
    int accountsCount;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBundleIdentifier() {
        return bundleIdentifier;
    }

    public String getBaseIdentifier() {
        return baseIdentifier;
    }

    public String getPlatform() {
        return platform;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public String getIcon32Url() {
        return icon32Url;
    }

    public String getIcon64Url() {
        return icon64Url;
    }

    public String getIcon128Url() {
        return icon128Url;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public String getDashboardUrl() {
        return dashboardUrl;
    }

    public int getImpactedDevicesCount() {
        return impactedDevicesCount;
    }

    public int getUnresolvedIssuesCount() {
        return unresolvedIssuesCount;
    }

    public int getCrashesCount() {
        return crashesCount;
    }

    public int getAccountsCount() {
        return accountsCount;
    }
}
