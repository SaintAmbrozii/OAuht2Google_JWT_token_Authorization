package com.example.jwtuser.security.oauth;

import java.util.Map;

public abstract class Oauth2userInfo {

    protected Map<String, Object> attributes;

    public Oauth2userInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }


    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public abstract String getId();

    public abstract String getName();

    public abstract String getEmail();

    public abstract String getGender();

    public abstract String getLocale();

    public abstract String getImageUrl();
}
