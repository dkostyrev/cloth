package com.nemezis.cloth.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
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

    public User() {
        organizations = new ArrayList<>();
    }

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

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setCurrentOrganization(Organization currentOrganization) {
        this.currentOrganization = currentOrganization;
    }

    public void setOrganizations(List<Organization> organizations) {
        this.organizations = organizations;
    }
}
