package com.nemezis.cloth.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Dmitry Kostyrev on 12/10/15
 */
public class Application {

    private String id;
    private String name;
    @SerializedName("bundle_identifier")
    private String bundleIdentifier;
    @SerializedName("base_identifier")
    private String baseIdentifier;
    private String platform;
    @SerializedName("icon_url")
    private String iconUrl;
    @SerializedName("icon32_url")
    private String icon32Url;
    @SerializedName("icon64_url")
    private String icon64Url;
    @SerializedName("icon128_url")
    private String icon128Url;
    @SerializedName("organization_id")
    private String organizationId;
    @SerializedName("dashboard_url")
    private String dashboardUrl;
    @SerializedName("impacted_devices_count")
    private int impactedDevicesCount;
    @SerializedName("unresolved_issues_count")
    private int unresolvedIssuesCount;
    @SerializedName("crashes_count")
    private int crashesCount;
    @SerializedName("accounts_count")
    private int accountsCount;

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
