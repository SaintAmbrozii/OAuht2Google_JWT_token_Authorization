package com.example.jwtuser.security.oauth;

import java.util.Map;

import static com.example.jwtuser.utils.CotstantUtil.*;

public class GoogleOauth2UserInfo extends Oauth2userInfo{

    public GoogleOauth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return (String) attributes.get(PROVIDER_ID);
    }

    @Override
    public String getName() {
        return (String) attributes.get(PROVIDER_NAME);
    }

    @Override
    public String getEmail() {
        return (String) attributes.get(PROVIDER_EMAIL);
    }

    @Override
    public String getGender() {
        return (String) attributes.get(PROVIDER_GENDER);
    }

    @Override
    public String getLocale() {
        return (String) attributes.get(PROVIDER_LOCALE);
    }

    @Override
    public String getImageUrl() {
        return (String) attributes.get(PROVIDER_PICTURE);
    }
}
