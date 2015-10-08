package com.nemezis.cloth.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Dmitry Kostyrev on 29/09/15
 */
public class User {
	private String id;
	private String name;
	private String email;
	private String token;
	@SerializedName("current_organization")
	private Organization currentOrganization;
	private List<Organization> organizations;

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getId() {
		return id;
	}

	public String getToken() {
		return token;
	}

    public Organization getCurrentOrganization() {
        return currentOrganization;
    }

    public List<Organization> getOrganizations() {
        return organizations;
    }
}
